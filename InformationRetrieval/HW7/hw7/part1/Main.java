package hw7.part1;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

public class Main {

	static ArrayList<String> spamWords = new ArrayList<String>();
	
	static Node node;
	static Client client;
	
	static String clusterName = "IRHW3Team2015";
	static String index = "trec07";
	static String type = "document";
	
	static String basePath = "C:\\Users\\malho_000\\Desktop\\IR\\eval\\part1\\";
	
	static HashMap<String, String> labelMap = new HashMap<String, String>();

	
	static HashMap<String , ArrayList<NGramScore>> trainingMap = new HashMap<String, ArrayList<NGramScore>>();
	static HashMap<String , ArrayList<NGramScore>> testingMap = new HashMap<String, ArrayList<NGramScore>>();
	
	private static void populateLabelsMap() throws Exception
	{
		File file = new File ("E:\\hw7data\\trec07p\\trec07p\\full\\index");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(" ");
			String label = parts[0];
			String file_name = parts[1].split("/")[2];
			labelMap.put(file_name, label);
			
		
			i++;
			System.out.println(i);
		}
		br.close();
		System.out.println(labelMap.size());
	}
	
	
	public static void main(String[] args) throws Exception {
		
		node = nodeBuilder().client(true).clusterName(clusterName).node();
		client = node.client();

		populateSpamList();
		populateLabelsMap();
		
		for (int i=0; i<spamWords.size(); i++)
		{
			populateTrainingMap(spamWords.get(i).toLowerCase());
		}
		
		for (int i=0; i<spamWords.size(); i++)
		{
			populateTestingMap(spamWords.get(i).toLowerCase());
		}
		
		System.out.println(trainingMap.size());
		System.out.println(testingMap.size());
	
		makeTrainingMatrix();
		makeTestingMatrix();
		
		node.close();
		client.close();
		getTestingResults();
		
	}
	
	public static void populateTrainingMap(String nGram) {
		
		QueryBuilder qb = QueryBuilders.matchQuery("body", nGram);
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(1000).execute().actionGet();

		
	
		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				float score = hit.getScore();
				String file_name = (String) hit.getSource().get("file_name");
				String label = (String) hit.getSource().get("label");
				String split = (String) hit.getSource().get("split");
		
				System.out.println(file_name + " : " + nGram + " : " + score + " : " + label + " : " + split);
				
				if (split.equals("train"))
				{
					if (!trainingMap.containsKey(file_name))
					{
						ArrayList<NGramScore> nGramScores = new ArrayList<NGramScore>();
						
						for (int i=0; i<spamWords.size(); i++)
						{
							nGramScores.add(new NGramScore(spamWords.get(i).toLowerCase(), 0));
						}
						
						nGramScores.set(nGramScores.indexOf(new NGramScore(nGram, 0)), new NGramScore(nGram, score));
						trainingMap.put(file_name, nGramScores);
					}
					else
					{
						ArrayList<NGramScore> nGramScores = trainingMap.get(file_name);
						nGramScores.set(nGramScores.indexOf(new NGramScore(nGram, 0)), new NGramScore(nGram, score));
						trainingMap.replace(file_name, nGramScores);
					}
				}
			
				
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
	}
	
	public static void populateTestingMap(String nGram) {
		
		QueryBuilder qb = QueryBuilders.matchQuery("body", nGram);
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(1000).execute().actionGet();

		
		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				float score = hit.getScore();
				String file_name = (String) hit.getSource().get("file_name");
				String label = (String) hit.getSource().get("label");
				String split = (String) hit.getSource().get("split");
		
				System.out.println(file_name + " : " + nGram + " : " + score + " : " + label + " : " + split);
				
				if (split.equals("test"))
				{
					if (!testingMap.containsKey(file_name))
					{
						ArrayList<NGramScore> nGramScores = new ArrayList<NGramScore>();
						
						for (int i=0; i<spamWords.size(); i++)
						{
							nGramScores.add(new NGramScore(spamWords.get(i).toLowerCase(), 0));
						}
						
						nGramScores.set(nGramScores.indexOf(new NGramScore(nGram, 0)), new NGramScore(nGram, score));
						testingMap.put(file_name, nGramScores);
					}
					else
					{
						ArrayList<NGramScore> nGramScores = testingMap.get(file_name);
						nGramScores.set(nGramScores.indexOf(new NGramScore(nGram, 0)), new NGramScore(nGram, score));
						testingMap.replace(file_name, nGramScores);
					}
				}
			
				
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
	}

	public static void makeTrainingMatrix() throws Exception
	{
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "trainingMatrix.arff", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.println("@RELATION training");
		
		for (int i=0; i<spamWords.size(); i++)
		{
			writer.println("@ATTRIBUTE " + "f" + (i+1) + " numeric");
		}
		writer.println("@ATTRIBUTE label numeric");
		
		writer.println("@DATA");
		
		HashMap<Integer, String> idFileNameMap = new HashMap<Integer, String>();
		
		
		Iterator it = trainingMap.entrySet().iterator();
		
		int i=0;
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			String file_name = (String) pair.getKey();
			ArrayList<NGramScore> nGramScores = (ArrayList<NGramScore>) pair.getValue();
			
			System.out.println(nGramScores.size());
			for (int j=0; j<nGramScores.size(); j++)
			{
				writer.print(nGramScores.get(j).getScore() + ",");
			}
			String label = "";
			if (labelMap.get(file_name).equals("spam"))
			{
				label = "1";
			}
			if (labelMap.get(file_name).equals("ham"))
			{
				label = "0";
			}
			
			writer.print(label + "\n");
			idFileNameMap.put(i, file_name);
			i++;
			it.remove(); // avoids a ConcurrentModificationException
			
		}
		
		writer.close();
		
		String filename = "idFileNameMapTraining.ser";
		FileOutputStream f = new FileOutputStream(basePath + filename);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(idFileNameMap);
		s.close();

		System.out.println("Done writing to file");
	}
	
	
	
	public static void makeTestingMatrix() throws Exception
	{
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "testingMatrix.arff", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.println("@RELATION testing");
		
		for (int i=0; i<spamWords.size(); i++)
		{
			writer.println("@ATTRIBUTE " + "f" + (i+1) + " numeric");
		}
		writer.println("@ATTRIBUTE label numeric");
		
		
		writer.println("@DATA");
		
		HashMap<Integer, String> idFileNameMap = new HashMap<Integer, String>();

		Iterator it = testingMap.entrySet().iterator();
		
		int i=0;
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			String file_name = (String) pair.getKey();
			ArrayList<NGramScore> nGramScores = (ArrayList<NGramScore>) pair.getValue();
			
			System.out.println(nGramScores.size());
			for (int j=0; j<nGramScores.size(); j++)
			{
				writer.print(nGramScores.get(j).getScore() + ",");
			}
			String label = "?";
			
			writer.print(label + "\n");
			idFileNameMap.put(i, file_name);
			i++;
			it.remove(); // avoids a ConcurrentModificationException
			
		}
		
		writer.close();
		
		String filename = "idFileNameMapTesting.ser";
		FileOutputStream f = new FileOutputStream(basePath + filename);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(idFileNameMap);
		s.close();

		System.out.println("Done writing to file");
	}
	
	public static void getTestingResults() throws Exception
	{
		String filename = "idFileNameMapTesting.ser";
		FileInputStream f = new FileInputStream(basePath + filename);
		ObjectInputStream s = new ObjectInputStream(f);

		HashMap<Integer, String> idFileNameMapTesting = (HashMap<Integer, String>) s.readObject();
		s.close();
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader(basePath + "trainingMatrix.arff"));
		Instances train = new Instances(reader);
		reader.close();
		// setting class attribute
		train.setClassIndex(train.numAttributes() - 1);
		BufferedReader reader1 = new BufferedReader(new FileReader(basePath + "testingMatrix.arff"));
		Instances test = new Instances(reader1);
		reader1.close();
		// setting class attribute
		test.setClassIndex(test.numAttributes() - 1);
		
		
		Classifier cls = new LinearRegression();
		cls.buildClassifier(train);
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(cls, test);

		HashMap<String, Double> fileScoreMap = new HashMap<String, Double>();
		
		for (int i = 0; i < test.numInstances(); i++) {
			double[] p = cls.distributionForInstance(test.instance(i));
			
			String file_name = (String)idFileNameMapTesting.get(i);
			double score = p[0];
		
			
			fileScoreMap.put(file_name, score);
		}
		
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "testingResults.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Double> sortedMap = sortByComparator(fileScoreMap);
		
		int rank = 1;
		for (String file_name : sortedMap.keySet())
		{
			double score = sortedMap.get(file_name);
			writer.println(file_name + " " + score
						+ " " + rank);
			System.out.println(file_name + " " + score
					+ " " + rank);
		
			rank++;
		}
		System.out.println("Done writing to file");
		writer.close();
	}
	
	private static LinkedHashMap<String, Double> sortByComparator(Map<String, Double> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Double>> list = 
				new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	
	
	static void populateSpamList()
	{
		spamWords.add("$$$");
		spamWords.add("100% free");
		spamWords.add("Act Now");
		spamWords.add("Ad");
		spamWords.add("Affordable");
		spamWords.add("Amazing stuff");
		spamWords.add("Apply now");
		spamWords.add("Auto email removal");
		spamWords.add("Billion");
		spamWords.add("Cash bonus");
		spamWords.add("Cheap");
		spamWords.add("Collect child support");
		spamWords.add("Compare rates");
		spamWords.add("Compete for your business");
		spamWords.add("Credit");
		spamWords.add("Credit bureaus");
		spamWords.add("Dig up dirt on friends");
		spamWords.add("Double your income");
		spamWords.add("Earn $");
		spamWords.add("Earn extra cash");
		spamWords.add("Eliminate debt");
		spamWords.add("Email marketing");
		spamWords.add("Explode your business");
		spamWords.add("Extra income");
		spamWords.add("F r e e");
		spamWords.add("Fast cash");
		spamWords.add("Financial freedom");
		spamWords.add("Financially independent");
		spamWords.add("Free");
		spamWords.add("Free gift");
		spamWords.add("Free grant money");
		spamWords.add("Free info");
		spamWords.add("Free installation");
		spamWords.add("Free investment");
		spamWords.add("Free leads");
		spamWords.add("Free membership");
		spamWords.add("Free offer");
		spamWords.add("Free preview");
		spamWords.add("Guarantee");
		spamWords.add("‘Hidden’ assets");
		spamWords.add("Home based");
		spamWords.add("Homebased business");
		spamWords.add("Income from home");
		spamWords.add("Increase sales");
		spamWords.add("Increase traffic");
		spamWords.add("Increase your sales");
		spamWords.add("Incredible deal");
		spamWords.add("Info you requested");
		spamWords.add("Information you requested");
		spamWords.add("Internet market");
		spamWords.add("Leave");
		spamWords.add("Limited time offer");
		spamWords.add("Serious cash");
		spamWords.add("Stock disclaimer statement");
		spamWords.add("Stop snoring");
		spamWords.add("Thousands");
		spamWords.add("Unsubscribe");
		spamWords.add("Web traffic");
		spamWords.add("Weight loss");
		spamWords.add("porn");
		spamWords.add("click here");
	}
}
