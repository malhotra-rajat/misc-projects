package hw7.part2;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.regex.Regex;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

public class Main2 {

	static ArrayList<String> unigrams = new ArrayList<String>();
	static HashSet<String> unigramsHashset = new HashSet<String>();
	
	static Node node;
	static Client client;
	
	static String clusterName = "IRHW3Team2015";
	static String index = "trec07";
	static String type = "document";
	
	static String basePath = "C:\\Users\\malho_000\\Desktop\\IR\\eval\\part2\\";
	
	static HashMap<String, String> labelMap = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {
		
		node = nodeBuilder().client(true).clusterName(clusterName).node();
		client = node.client();
		
		populateDictionarySet();
		populateUnigramList();
		readUnigramsList();
		writeUnigramsIndexToFile();
		
		
		populateLabelsMap();
		makeTrainingMatrix();
		makeTestingMatrix();

		node.close();
		client.close();
		
		writeFeatureAnalysisFile();
	}
	
	private static void writeUnigramsIndexToFile()
	{
		PrintWriter writerUnigrams = null;
		try {
			writerUnigrams = new PrintWriter(basePath + "unigramsIndex.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i<unigrams.size(); i++)
		{
			writerUnigrams.println(unigrams.get(i) + " " + (i+1));
			System.out.println(unigrams.get(i) + " " + (i+1));
		}
		writerUnigrams.close();
	}
		
	public static void makeTrainingMatrix() throws Exception
	{
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "trainingMatrix1.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		QueryBuilder qb = QueryBuilders.matchQuery("split", "train");
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(8000))
				.setQuery(qb)
				.setSize(5).execute().actionGet();

		HashMap<Integer, String> indexFileNameMap = new HashMap<Integer, String>();
		
		int l=0;
		while (true) {
			
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				
				
				String file_name = (String) hit.getSource().get("file_name");
				
				HashMap<String, Integer> tfMap = new HashMap<String, Integer>();
				
				String body = (String) hit.getSource().get("body");
				
				String[] parts = body.split(" ");
				for (int i = 0; i < parts.length; i++)
				{
					String word = parts[i];
					word = word.replaceAll("[^a-zA-Z0-9]", "");
					if (word.length() < 25)	
					{
						if (unigramsHashset.contains(word))
						{
							if (!tfMap.containsKey(word))
							{
								tfMap.put(word, 1);
							}
							else
							{
								int count = tfMap.get(word);
								tfMap.replace(word, count + 1);
							}
						}
					}
				}
				
				
				
				Map<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>();
				
				for (Map.Entry<String, Integer> entry : tfMap.entrySet())
				{
					String unigram = entry.getKey();
					int tf = entry.getValue();
					int index = unigrams.indexOf(unigram);
					sortedMap.put(index + 1, tf);
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
				
				String instanceString = label + " ";
				
				for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet())
				{
					int index = entry.getKey();
					int tf = entry.getValue();
					instanceString += index + ":" + tf + " ";
				}
				
				
				instanceString = instanceString.trim();
				
				writer.println(instanceString);
				
				System.out.println(l);
				indexFileNameMap.put(l, file_name);
				l++;
			}
				
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
				new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		
			
		}
		
		writer.close();
		
		String filename1 = "idFileNameMapTraining1.ser";
		FileOutputStream f1 = new FileOutputStream(basePath + filename1);
		ObjectOutputStream s1 = new ObjectOutputStream(f1);
		s1.writeObject(indexFileNameMap);
		s1.close();

		System.out.println("Done writing to file");
	}
	
	public static void makeTestingMatrix() throws Exception
	{
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "testingMatrix1.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		QueryBuilder qb = QueryBuilders.matchQuery("split", "test");
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(8000))
				.setQuery(qb)
				.setSize(5).execute().actionGet();

		HashMap<Integer, String> indexFileNameMap = new HashMap<Integer, String>();
		
		int l=0;
		while (true) {
			
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				
				
				String file_name = (String) hit.getSource().get("file_name");
				
				HashMap<String, Integer> tfMap = new HashMap<String, Integer>();
				
				String body = (String) hit.getSource().get("body");
				
				String[] parts = body.split(" ");
				for (int i = 0; i < parts.length; i++)
				{
					String word = parts[i];
					word = word.replaceAll("[^a-zA-Z0-9]", "");
					if (word.length() < 25)	
					{
						if (unigramsHashset.contains(word))
						{
							if (!tfMap.containsKey(word))
							{
								tfMap.put(word, 1);
							}
							else
							{
								int count = tfMap.get(word);
								tfMap.replace(word, count + 1);
							}
						}
					}
				}
				
				
				
				Map<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>();
				
				for (Map.Entry<String, Integer> entry : tfMap.entrySet())
				{
					String unigram = entry.getKey();
					int tf = entry.getValue();
					int index = unigrams.indexOf(unigram);
					sortedMap.put(index + 1, tf);
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
				
				String instanceString = label + " ";
				
				for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet())
				{
					int index = entry.getKey();
					int tf = entry.getValue();
					instanceString += index + ":" + tf + " ";
				}
				
				
				instanceString = instanceString.trim();
				
				writer.println(instanceString);
				
				System.out.println(l);
				indexFileNameMap.put(l, file_name);
				l++;
			}
				
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
				new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		
			
		}
		
		writer.close();
		
		String filename1 = "idFileNameMapTesting1.ser";
		FileOutputStream f1 = new FileOutputStream(basePath + filename1);
		ObjectOutputStream s1 = new ObjectOutputStream(f1);
		s1.writeObject(indexFileNameMap);
		s1.close();

		System.out.println("Done writing to file");
	}
	
	static void populateUnigramList() throws Exception
	{
		String filename = "words.ser";
		FileInputStream f = new FileInputStream(basePath + filename);
		ObjectInputStream s = new ObjectInputStream(f);

		HashSet<String> dictionarySet = (HashSet<String>) s.readObject();
		s.close();
		
		HashMap<String, Integer> unigramsDocFreqMap = new HashMap<String, Integer>();
		
		HashSet<String> unigramsSet = new HashSet<String>();
		
		
		QueryBuilder qb = QueryBuilders.matchQuery("split", "train");
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(200).execute().actionGet();
		int count = 1;

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
		
				//newDoc = true;
				String body = (String) hit.getSource().get("body");
				
				HashSet<String> wordSet = new HashSet<String>();
				
				String[] parts = body.split(" ");
				String temp = "";
				for (int i = 0; i < parts.length; i++)
				{
					
					String word = parts[i];
					if (!wordSet.contains(word))
					{
						word = word.replaceAll("[^a-zA-Z0-9]", "");
						if (word.length() < 25)	
						{
							if (word.matches("[a-zA-Z]"))
							{
								if (dictionarySet.contains(word))
								{
									if (!unigramsDocFreqMap.containsKey(word))
									{
										unigramsDocFreqMap.put(word, 1);
									}
									else
									{
										int docFreq = unigramsDocFreqMap.get(word);
										unigramsDocFreqMap.replace(word, docFreq + 1);
									}
								}
							}
							else
							{
								if (!unigramsDocFreqMap.containsKey(word))
								{
									unigramsDocFreqMap.put(word, 1);
								}
								else
								{
									int docFreq = unigramsDocFreqMap.get(word);
									unigramsDocFreqMap.replace(word, docFreq + 1);
								}
							}
						}
						wordSet.add(word);
					}
				}
				temp = temp.trim();
				System.out.println(count + " : " + scrollResp.getHits().getTotalHits() + " : " + unigramsDocFreqMap.size() + " : " + temp);
				
				count++;
			
				
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		
		int unigramSize = 0;
		for (Map.Entry<String, Integer> entry : unigramsDocFreqMap.entrySet()) {
		    String key = entry.getKey();
		    Integer docFreq = entry.getValue();
		    if (!(docFreq < 5))
		    {
		    	unigramsSet.add(key);
		    	System.out.println("Unigram size: " + unigramSize + " : " + key + " : " + docFreq);
		    	unigramSize++;
		    }
		}
		
		
		
		ArrayList<String> unigramsList = new ArrayList<String>();
		
		for (String unigram : unigramsSet) {
			unigramsList.add(unigram);
		}
		
		String filename1 = "unigrams.ser";
		FileOutputStream f1 = new FileOutputStream(basePath + filename1);
		ObjectOutputStream s1 = new ObjectOutputStream(f1);
		s1.writeObject(unigramsList);
		s1.close();


	}
	
	static void readUnigramsList() throws Exception
	{
		String filename = "unigrams.ser";
		FileInputStream f = new FileInputStream(basePath + filename);
		ObjectInputStream s = new ObjectInputStream(f);

		unigrams = (ArrayList<String>) s.readObject();
		for (int i=0; i<unigrams.size(); i++)
		{
			unigramsHashset.add(unigrams.get(i));
		}
		s.close();
	}
	
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
		}
		br.close();
		System.out.println(labelMap.size());
	}
	
	private static void populateDictionarySet() throws Exception
	{
		HashSet<String> dictionarySet = new HashSet<String>();
		File file = new File (basePath + "words.txt");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			if (!line.trim().equals(""))
			{
				dictionarySet.add(line);
				i++;
				System.out.println(i);
			}
		}
		br.close();
		System.out.println(dictionarySet.size());
		
		String filename = "words.ser";
		FileOutputStream f = new FileOutputStream(basePath + filename);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(dictionarySet);
		s.close();

		System.out.println("Done writing to file");
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
	
	private static void writeFeatureAnalysisFile() throws Exception
	{
		HashMap<String, Double> featureMap = new HashMap<String, Double>();
		
		File modelFile = new File (basePath + "linear.model");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(modelFile));
		
		
		File unigramsFile = new File (basePath + "unigramsIndex.txt");
		BufferedReader br2 = null;
		br2 = new BufferedReader(new FileReader(unigramsFile));
		
		String lineModel;
		String lineUnigram;
		
		int i = 0;
		while (((lineModel = br.readLine()) != null) && ((lineUnigram = br2.readLine())!= null)) {
			String unigram = lineUnigram.split(" ")[0];
			Double score = Double.parseDouble(lineModel);
			
			featureMap.put(unigram, score);
		}
		
		br.close();
		br2.close();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "featureMapSorted.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Double> sortedMap = sortByComparator(featureMap);
		
		for (String unigram : sortedMap.keySet())
		{
			double score = sortedMap.get(unigram);
			writer.println(unigram + " " + score);
		}
		System.out.println("Done writing to file");
		writer.close();
		
		
	}
	
}
