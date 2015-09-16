package hw6;


import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import hw6.model.DocLengthTF;
import hw6.model.DocLengthTermTF;
import hw6.model.JelinekMercer;
import hw6.model.Laplace;
import hw6.model.OkapiBM25;
import hw6.model.OkapiTF;
import hw6.model.Query;
import hw6.model.QueryDoc;
import hw6.model.QueryDocFeaturesLabel;
import hw6.model.TFIDF;

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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.tartarus.snowball.ext.PorterStemmer;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

public class Main {

	static String stopWordsFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//stoplist.txt";
	static String queryFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//query_desc.51-100.short.txt";  
	static String indexPath = "E:\\elasticsearch-1.6.0\\elasticsearch-1.6.0\\data\\IRHW3Team2015\\nodes\\0\\indices\\ap_dataset\\0\\index";

	static String basePath = "C:\\Users\\malho_000\\Desktop\\IR\\eval\\";

	static String clusterName = "IRHW3Team2015";
	
	static ArrayList<String> queryList = new ArrayList<String>();

	public static ArrayList<String> stopWords = new ArrayList<String>();

	static Node node;
	static Client client;

	static ArrayList<Query> queries = new ArrayList<Query>();
	
	
	static ArrayList<Query> trainingQueries = new ArrayList<Query>();
	static ArrayList<Query> testingQueries = new ArrayList<Query>();

	static ArrayList<OkapiTF> docQueryTermTFs = new ArrayList<OkapiTF>();

	static String index = "ap_dataset";
	static String type = "document";


	static long totalDocs;
	static double avg_doc_length;
	static double vocabulary_size;
	static double total_doc_length;

	static LinkedHashMap<QueryDoc, Double> okapiTfMapForQuery = new LinkedHashMap<QueryDoc, Double>();

	static LinkedHashMap<QueryDoc, Double> bm25MapForQuery = new LinkedHashMap<QueryDoc, Double>();

	static LinkedHashMap<QueryDoc, Double> tfIdfMapForQuery = new LinkedHashMap<QueryDoc, Double>();

	static LinkedHashMap<QueryDoc, Double> laplaceMapForQuery = new LinkedHashMap<QueryDoc, Double>();

	static LinkedHashMap<QueryDoc, Double> jelinekMercerMapForQuery = new LinkedHashMap<QueryDoc, Double>();
	
	static ArrayList<QueryDocFeaturesLabel> queryDocFeaturesLabelList = new ArrayList<QueryDocFeaturesLabel>();
	
	static HashMap<String, HashMap<String, Integer>> queryIdDocGradeMap = new HashMap<String, HashMap<String,Integer>>();

	public static void main(String[] args) throws Exception {
		readQrel();
		
		node = nodeBuilder().client(true).clusterName(clusterName).node();
		client = node.client();

		populateStopWordsSet();
	
		populateQueryList();
		makeQueryList();
		splitQueries();
		
		avg_doc_length = getAvgDocumentLength();
		vocabulary_size = getVocabularySize("text");
		totalDocs = getNumberOfDocs();
		total_doc_length = getTotalDocLength();

		makeOkapiTfMap(trainingQueries);
		makeTfIDfMap(trainingQueries);
		makeBm25Map(trainingQueries);
		makeLaplaceMap(trainingQueries);
		makeJelinekMercerMap(trainingQueries);
		
		System.out.println(okapiTfMapForQuery.size());
		System.out.println(tfIdfMapForQuery.size());
		System.out.println(bm25MapForQuery.size());
		System.out.println(laplaceMapForQuery.size());
		System.out.println(jelinekMercerMapForQuery.size());
		
		makeTrainingMatrix();
	
		//----------------------------------
		okapiTfMapForQuery.clear();
		tfIdfMapForQuery.clear();
		bm25MapForQuery.clear();
		laplaceMapForQuery.clear();
		jelinekMercerMapForQuery.clear();
		queryDocFeaturesLabelList.clear();
		
		makeOkapiTfMap(testingQueries);
		makeTfIDfMap(testingQueries);
		makeBm25Map(testingQueries);
		makeLaplaceMap(testingQueries);
		makeJelinekMercerMap(testingQueries);
		
		System.out.println(okapiTfMapForQuery.size());
		System.out.println(tfIdfMapForQuery.size());
		System.out.println(bm25MapForQuery.size());
		System.out.println(laplaceMapForQuery.size());
		System.out.println(jelinekMercerMapForQuery.size());
		
		makeTestingMatrix();
		
		
		node.close();
		client.close();
		
		
		getTestingResults();
		getTrainingResults();
		
	}

	
	
	private static void readQrel() throws Exception
	{
		queryIdDocGradeMap = new HashMap<String, HashMap<String, Integer>>();
		
		File file = new File ("..//qrels.adhoc.51-100.AP89.txt");
		//File file = new File ("..//qrels.txt");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			//System.out.println(i + " : " + line);
			String[] parts = line.split(" ");
			String queryId = parts[0];
			String assessorId = parts[1];
			String docId = parts[2];
			int grade = Integer.parseInt(parts[3]);
			
			if (!queryIdDocGradeMap.containsKey(queryId))
			{
				HashMap<String, Integer> docIdGradeMap = new HashMap<String, Integer>();
				docIdGradeMap.put(docId, grade);
				queryIdDocGradeMap.put(queryId, docIdGradeMap);
				
			
			}
			else
			{
				HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryId);
				if (!docIdGradeMap.containsKey(docId))
				{
					docIdGradeMap.put(docId, grade);
				}
				else
				{
					int oldGrade = docIdGradeMap.get(docId);
					if (grade > oldGrade)
					{
						docIdGradeMap.replace(docId, grade); //max value of grade
					}
				}
				queryIdDocGradeMap.replace(queryId, docIdGradeMap);
			}
			i++;
		}
		
	
		br.close();
		
		System.out.println(queryIdDocGradeMap.size());
	
	}
	
	
	public static void makeTrainingMatrix() throws Exception
	{
		Iterator it = okapiTfMapForQuery.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			QueryDoc queryDoc = (QueryDoc) pair.getKey();
			Double okapiScore = (Double) pair.getValue();
			Double tfIdfScore = (Double)tfIdfMapForQuery.get(queryDoc);
			Double bm25Score = (Double)bm25MapForQuery.get(queryDoc);
			Double laplaceScore = (Double)laplaceMapForQuery.get(queryDoc);
			Double jelinekMercerScore = (Double)jelinekMercerMapForQuery.get(queryDoc);
			
			HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryDoc.getQuery_no());
			String label;
			if (!docIdGradeMap.containsKey(queryDoc.getDoc_no()))
			{
				label = "?";
			}
			else
			{
				label = docIdGradeMap.get(queryDoc.getDoc_no()).toString();
			}
			
			queryDocFeaturesLabelList.add(new QueryDocFeaturesLabel(queryDoc.getQuery_no(), queryDoc.getDoc_no(), okapiScore, 
					bm25Score, tfIdfScore, laplaceScore, jelinekMercerScore, label));
			it.remove(); // avoids a ConcurrentModificationException
			
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "trainingMatrix.arff", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.println("@RELATION training");
		//writer.println("@ATTRIBUTE QueryNumberDocNumber numeric");
		//writer.println("@ATTRIBUTE DocNumber numeric");
		writer.println("@ATTRIBUTE Okapi numeric");
		writer.println("@ATTRIBUTE Bm25 numeric");
		writer.println("@ATTRIBUTE Tfidf numeric");
		writer.println("@ATTRIBUTE Laplace numeric");
		writer.println("@ATTRIBUTE JelinekMercer numeric");
		writer.println("@ATTRIBUTE label numeric");
		writer.println("@DATA");
		
		HashMap<Integer, QueryDoc> idQueryDocMap = new HashMap<Integer, QueryDoc>();
		
		for (int i=0; i<queryDocFeaturesLabelList.size(); i++)
		{
			QueryDocFeaturesLabel queryDocFeaturesLabel = queryDocFeaturesLabelList.get(i);
			writer.println(
					/*queryDocFeaturesLabel.getQuery_no() + 
					"-" + queryDocFeaturesLabel.getDoc_no() +
					"," + */queryDocFeaturesLabel.getOkapi() + 
					"," + queryDocFeaturesLabel.getBm25() + 
					"," + queryDocFeaturesLabel.getTfidf() + 
					"," + queryDocFeaturesLabel.getLaplace() + 
					"," + queryDocFeaturesLabel.getJelinekmercer() +
					"," + queryDocFeaturesLabel.getLabel());
			
			idQueryDocMap.put(i, new QueryDoc(queryDocFeaturesLabel.getQuery_no(), queryDocFeaturesLabel.getDoc_no()));
		}
		writer.close();
		
		String filename = "idQueryDocMapTraining";
		FileOutputStream f = new FileOutputStream(basePath + filename);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(idQueryDocMap);
		s.close();

		System.out.println("Done writing to file");
	}
	
	public static void getTestingResults() throws Exception
	{
		String filename = "idQueryDocMapTesting";
		FileInputStream f = new FileInputStream(basePath + filename);
		ObjectInputStream s = new ObjectInputStream(f);

		HashMap<Integer, QueryDoc> idQueryDocMap = (HashMap<Integer, QueryDoc>) s.readObject();
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
		
		//System.out.println(eval.precision(0));
		/*System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		System.out.println(cls.classifyInstance(test.instance(7)));
		System.out.println(test.numInstances());
		System.out.println(test.instance(0).toString(1));*/
		
		HashMap<String, HashMap<String, Double>> queryDocScoreMap = new HashMap<String, HashMap<String, Double>>();
		
		for (int i = 0; i < test.numInstances(); i++) {
			double[] p = cls.distributionForInstance(test.instance(i));
			
			String query_no = ((QueryDoc)idQueryDocMap.get(i)).getQuery_no();
			String doc_no = ((QueryDoc)idQueryDocMap.get(i)).getDoc_no();
		
			
			if (!queryDocScoreMap.containsKey(query_no))
			{
				HashMap<String, Double> docScoreMap = new HashMap<String, Double>();
				docScoreMap.put(doc_no, p[0]);
				queryDocScoreMap.put(query_no, docScoreMap);
			}
			else
			{
				HashMap<String, Double> docScoreMap = queryDocScoreMap.get(query_no);
				docScoreMap.put(doc_no, p[0]);
				queryDocScoreMap.replace(query_no, docScoreMap);
			}
		}
		
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "testingResults.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done writing to file");
		
		for (String query_no : queryDocScoreMap.keySet()) 
		{
			Map<String, Double> sortedMap = sortByComparator(queryDocScoreMap.get(query_no));
			int rank = 1;
			for (String doc_no : sortedMap.keySet())
			{
				double score = sortedMap.get(doc_no);
				if (rank <= 1000)
				{
					
					writer.println(query_no + " Q0 " + doc_no
							+ " " + rank + " " + score + " Exp");
					System.out.println(query_no + " Q0 " + doc_no
							+ " " + rank + " " + score + " Exp");
				}
				else
				{
					break;
				}
				rank++;
			}
		}
		writer.close();
	}
	
	
	public static void getTrainingResults() throws Exception
	{
		String filename = "idQueryDocMapTraining";
		FileInputStream f = new FileInputStream(basePath + filename);
		ObjectInputStream s = new ObjectInputStream(f);

		HashMap<Integer, QueryDoc> idQueryDocMap = (HashMap<Integer, QueryDoc>) s.readObject();
		s.close();
		
		BufferedReader reader = new BufferedReader(new FileReader(basePath + "trainingMatrix.arff"));
		Instances train = new Instances(reader);
		reader.close();
		// setting class attribute
		train.setClassIndex(train.numAttributes() - 1);
		
		Classifier cls = new LinearRegression();
		cls.buildClassifier(train);
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(cls, train);
	
		//System.out.println(eval.precision(0));
		/*System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		System.out.println(cls.classifyInstance(test.instance(7)));
		System.out.println(test.numInstances());
		System.out.println(test.instance(0).toString(1));*/
		
		HashMap<String, HashMap<String, Double>> queryDocScoreMap = new HashMap<String, HashMap<String, Double>>();
		
		for (int i = 0; i < train.numInstances(); i++) {
			double[] p = cls.distributionForInstance(train.instance(i));
			
			String query_no = ((QueryDoc)idQueryDocMap.get(i)).getQuery_no();
			String doc_no = ((QueryDoc)idQueryDocMap.get(i)).getDoc_no();
		
			
			if (!queryDocScoreMap.containsKey(query_no))
			{
				HashMap<String, Double> docScoreMap = new HashMap<String, Double>();
				docScoreMap.put(doc_no, p[0]);
				queryDocScoreMap.put(query_no, docScoreMap);
			}
			else
			{
				HashMap<String, Double> docScoreMap = queryDocScoreMap.get(query_no);
				docScoreMap.put(doc_no, p[0]);
				queryDocScoreMap.replace(query_no, docScoreMap);
			}
		}
		
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "trainingResults.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		System.out.println("Done writing to file");
		
		for (String query_no : queryDocScoreMap.keySet()) 
		{
			Map<String, Double> sortedMap = sortByComparator(queryDocScoreMap.get(query_no));
			int rank = 1;
			for (String doc_no : sortedMap.keySet())
			{
				double score = sortedMap.get(doc_no);
				if (rank <= 1000)
				{
					
					writer.println(query_no + " Q0 " + doc_no
							+ " " + rank + " " + score + " Exp");
					System.out.println(query_no + " Q0 " + doc_no
							+ " " + rank + " " + score + " Exp");
				}
				else
				{
					break;
				}
				rank++;
			}
		}
		writer.close();
	}
	
	
	public static void makeTestingMatrix() throws Exception
	{
		Iterator it = okapiTfMapForQuery.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			QueryDoc queryDoc = (QueryDoc) pair.getKey();
			Double okapiScore = (Double) pair.getValue();
			Double tfIdfScore = (Double)tfIdfMapForQuery.get(queryDoc);
			Double bm25Score = (Double)bm25MapForQuery.get(queryDoc);
			Double laplaceScore = (Double)laplaceMapForQuery.get(queryDoc);
			Double jelinekMercerScore = (Double)jelinekMercerMapForQuery.get(queryDoc);
			
			HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryDoc.getQuery_no());
			String label;
			if (!docIdGradeMap.containsKey(queryDoc.getDoc_no()))
			{
				label = "?";
			}
			else
			{
				label = docIdGradeMap.get(queryDoc.getDoc_no()).toString();
			}
			
			queryDocFeaturesLabelList.add(new QueryDocFeaturesLabel(queryDoc.getQuery_no(), queryDoc.getDoc_no(), okapiScore, 
					bm25Score, tfIdfScore, laplaceScore, jelinekMercerScore, label));
			it.remove(); // avoids a ConcurrentModificationException
			
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(basePath + "testingMatrix.arff", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		writer.println("@RELATION training");
		//writer.println("@ATTRIBUTE QueryNumberDocNumber numeric");
		//writer.println("@ATTRIBUTE DocNumber numeric");
		writer.println("@ATTRIBUTE Okapi numeric");
		writer.println("@ATTRIBUTE Bm25 numeric");
		writer.println("@ATTRIBUTE Tfidf numeric");
		writer.println("@ATTRIBUTE Laplace numeric");
		writer.println("@ATTRIBUTE JelinekMercer numeric");
		writer.println("@ATTRIBUTE label numeric");
		writer.println("@DATA");
		
		HashMap<Integer, QueryDoc> idQueryDocMap = new HashMap<Integer, QueryDoc>();

		for (int i=0; i<queryDocFeaturesLabelList.size(); i++)
		{
			QueryDocFeaturesLabel queryDocFeaturesLabel = queryDocFeaturesLabelList.get(i);
			writer.println(
				/*	queryDocFeaturesLabel.getQuery_no() + 
					"-" + queryDocFeaturesLabel.getDoc_no() +
					"," +*/ queryDocFeaturesLabel.getOkapi() + 
					"," + queryDocFeaturesLabel.getBm25() + 
					"," + queryDocFeaturesLabel.getTfidf() + 
					"," + queryDocFeaturesLabel.getLaplace() + 
					"," + queryDocFeaturesLabel.getJelinekmercer() +
					"," + queryDocFeaturesLabel.getLabel());
			
			idQueryDocMap.put(i, new QueryDoc(queryDocFeaturesLabel.getQuery_no(), queryDocFeaturesLabel.getDoc_no()));
		}
		
		String filename = "idQueryDocMapTesting";
		FileOutputStream f = new FileOutputStream(basePath + filename);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(idQueryDocMap);
		s.close();
		
		writer.close();

		System.out.println("Done writing to file");
	}
	
	
	public static void splitQueries()
	{
		for (int i=0; i<queries.size(); i++)
		{
			String queryNumber = queries.get(i).getQueryNumber();
			if (queryNumber.equals("98") || queryNumber.equals("60") || 
					queryNumber.equals("80") || queryNumber.equals("97") 
					|| queryNumber.equals("85"))
			{
				testingQueries.add(queries.get(i));
			}
			else
			{
				trainingQueries.add(queries.get(i));
			}
		}
	}
	

	public static ArrayList<OkapiTF> getOkapiTfResults(QueryBuilder qb, String query_no, String term) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<OkapiTF>();
		}

		ArrayList<OkapiTF> results = new  ArrayList<OkapiTF>();

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				int tf =  (int) hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();
				int doc_length = hit.getFields().get("doc_length_without_stopwords").getValue();

				double okapiTf = tf/(tf + 0.5 + (1.5 * doc_length/avg_doc_length));
				results.add(new OkapiTF(docno, query_no, tf, okapiTf));
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}

		return results;
	}

	public static ArrayList<OkapiBM25> getOkapiBM25Results(QueryBuilder qb, String query_no, String term, int termFreqInQuery, long doc_frequency) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<OkapiBM25>();
		}

		ArrayList<OkapiBM25> results = new  ArrayList<OkapiBM25>();

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				int tf =  (int) hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();

				int doc_length = hit.getFields().get("doc_length_without_stopwords").getValue();

				double term1 = Math.log((totalDocs + 0.5)/(doc_frequency + 0.5));

				double k1 = 1.2;
				double k2 = 500;
				double b = 0.75;

				double term2 =  (tf + (k1 * tf))/(tf + k1 * ((1-b) + (b * (doc_length/avg_doc_length))));
				double term3 = (termFreqInQuery + (k2 * termFreqInQuery))/(termFreqInQuery + k2);

				double bm25Score = term1 * term2 * term3; 

				results.add(new OkapiBM25(docno, query_no, term, tf, bm25Score));

			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}

		return results;
	}


	public static ArrayList<TFIDF> getTfIdfResults(QueryBuilder qb, String query_no, String term, long doc_frequency) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<TFIDF>();
		}


		ArrayList<TFIDF> results = new  ArrayList<TFIDF>();


		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				int tf =  (int) hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();

				int doc_length = hit.getFields().get("doc_length_without_stopwords").getValue();

				double okapiTf = tf/(tf + 0.5 + (1.5 * doc_length/avg_doc_length));

				double logOfTotalDocsByDocFreq = Math.log(totalDocs/doc_frequency);

				double tfIdfScore = okapiTf * logOfTotalDocsByDocFreq;

				results.add(new TFIDF(docno, query_no, tf, tfIdfScore));

			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}

		return results;
	}


	public static ArrayList<Laplace> getLaplaceResults(QueryBuilder qb, String query_no, String term) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<Laplace>();
		}

		ArrayList<Laplace> results = new  ArrayList<Laplace>();

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				float tf =  hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();
				int doc_length = hit.getFields().get("doc_length_without_stopwords").getValue();

				results.add(new Laplace(docno, query_no, doc_length, (int)tf));

			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}

		return results;
	}

	public static ArrayList<JelinekMercer> getJelinekMercerResults(QueryBuilder qb, String term) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<JelinekMercer>();
		}

		ArrayList<JelinekMercer> results = new  ArrayList<JelinekMercer>();

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				float tf =  hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();
				int doc_length = hit.getFields().get("doc_length_without_stopwords").getValue();

				results.add(new JelinekMercer(docno, doc_length, (int)tf, term));

			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		return results;
	}

	public static void makeOkapiTfMap(ArrayList<Query> queriesPassed)
	{
		for (int i=0; i<queriesPassed.size(); i++)
		{
			String queryNumber = queriesPassed.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesPassed.get(i).getQueryWords();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));

				System.out.println(queryWordsTemp.get(j));
				ArrayList<OkapiTF> queryTFresultsForTerm = getOkapiTfResults(qb, queryNumber, queryWordsTemp.get(j));

				for (int x=0; x < queryTFresultsForTerm.size(); x++)
				{
					String doc_no = queryTFresultsForTerm.get(x).getDoc_no();
					double okapiTf = queryTFresultsForTerm.get(x).getOkapiTf();
					
					if (queryIdDocGradeMap.containsKey(queryNumber))
					{
						HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNumber);
						if (docIdGradeMap.containsKey(doc_no))
						{
							QueryDoc queryDoc = new QueryDoc(queryNumber, doc_no);
							if (okapiTfMapForQuery.containsKey(queryDoc))
							{
								double okapiTfPrevious = okapiTfMapForQuery.get(queryDoc);
								okapiTfMapForQuery.put(queryDoc, okapiTfPrevious + okapiTf);
							}
							else
							{
								okapiTfMapForQuery.put(queryDoc, okapiTf);
							}
						}
					}
				}
			}
			System.out.println(queryNumber);
		}
	
		System.out.println(okapiTfMapForQuery.size());
	}

	
	public static void makeTfIDfMap(ArrayList<Query> queriesPassed)
	{
		for (int i=0; i<queriesPassed.size(); i++)
		{
			String queryNumber = queriesPassed.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesPassed.get(i).getQueryWords();

		

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));

				System.out.println(queryWordsTemp.get(j));

				QueryBuilder qbDocFreq = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				long doc_frequency = getDocumentFrequency(qbDocFreq);

				ArrayList<TFIDF> tfIdfResults = getTfIdfResults(qb, queryNumber, queryWordsTemp.get(j), doc_frequency);

				for (int x=0; x < tfIdfResults.size(); x++)
				{
					String doc_no = tfIdfResults.get(x).getDoc_no();
					double tfidf = tfIdfResults.get(x).getTfIdf();

					
					if (queryIdDocGradeMap.containsKey(queryNumber))
					{
						HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNumber);
						if (docIdGradeMap.containsKey(doc_no))
						{
							QueryDoc queryDoc = new QueryDoc(queryNumber, doc_no);
							
							if (tfIdfMapForQuery.containsKey(queryDoc))
							{
								double tfIdfPrevious = tfIdfMapForQuery.get(queryDoc);
								tfIdfMapForQuery.put(queryDoc, tfIdfPrevious + tfidf);
							}
							else
							{
								tfIdfMapForQuery.put(queryDoc, tfidf);
							}
						}
					}
					
					
				}
			}
			System.out.println(queryNumber);
			
		}

	}
	
	public static void makeBm25Map(ArrayList<Query> queriesPassed)
	{
		for (int i=0; i<queriesPassed.size(); i++)
		{
			String queryNumber = queriesPassed.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesPassed.get(i).getQueryWords();

			

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));


				QueryBuilder qbDocFreq = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				long doc_frequency = getDocumentFrequency(qbDocFreq);

				System.out.println(queryWordsTemp.get(j));
				ArrayList<OkapiBM25> bm25Results = getOkapiBM25Results(qb, queryNumber, queryWordsTemp.get(j), getFrequencyInQuery(queryWordsTemp.get(j), queryWordsTemp),
						doc_frequency);

				for (int x=0; x < bm25Results.size(); x++)
				{
					String doc_no = bm25Results.get(x).getDoc_no();
					double bm25 = bm25Results.get(x).getBm25();

					if (queryIdDocGradeMap.containsKey(queryNumber))
					{
						HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNumber);
						if (docIdGradeMap.containsKey(doc_no))
						{
							QueryDoc queryDoc = new QueryDoc(queryNumber, doc_no);
							
							if (bm25MapForQuery.containsKey(queryDoc))
							{
								double bm25Previous = bm25MapForQuery.get(queryDoc);


								bm25MapForQuery.put(queryDoc, bm25Previous + bm25);
							}
							else
							{
								bm25MapForQuery.put(queryDoc, bm25);
							}
						}
					}
				
				}
			}
			System.out.println(queryNumber);
		}
	}

	public static void makeLaplaceMap(ArrayList<Query> queriesPassed)
	{
		for (int i=0; i<queriesPassed.size(); i++)
		{
			String queryNumber = queriesPassed.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesPassed.get(i).getQueryWords();

			ArrayList<Laplace> laplaceResultsforQuery = new ArrayList<Laplace>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				System.out.println(queryWordsTemp.get(j));
				laplaceResultsforQuery.addAll(getLaplaceResults(qb, queryNumber, queryWordsTemp.get(j)));
			}

			HashMap<String, ArrayList<DocLengthTF>> uniqueDocsLengthsTFsMap = new HashMap<String, ArrayList<DocLengthTF>>();

			for (int h=0; h<laplaceResultsforQuery.size(); h++)
			{
				String doc_no = laplaceResultsforQuery.get(h).getDoc_no();
				if (uniqueDocsLengthsTFsMap.containsKey(doc_no))
				{
					ArrayList<DocLengthTF> docLengthTFList = uniqueDocsLengthsTFsMap.get(doc_no);
					docLengthTFList.add(new DocLengthTF(laplaceResultsforQuery.get(h).getDoc_length(), laplaceResultsforQuery.get(h).getTf()));
					uniqueDocsLengthsTFsMap.replace(doc_no, docLengthTFList);
				}
				else
				{	
					ArrayList<DocLengthTF> docLengthTFList = new ArrayList<DocLengthTF>();
					docLengthTFList.add(new DocLengthTF(laplaceResultsforQuery.get(h).getDoc_length(), laplaceResultsforQuery.get(h).getTf()));
					uniqueDocsLengthsTFsMap.put(doc_no, docLengthTFList);
				}
			}

			
			Iterator it = uniqueDocsLengthsTFsMap.entrySet().iterator();

			while (it.hasNext() ) {
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				String doc_no = (String) pair.getKey();
				ArrayList<DocLengthTF> docLengthTFList = (ArrayList<DocLengthTF>) pair.getValue();

				int sizeDiff = queryWordsTemp.size() - docLengthTFList.size();

				if (sizeDiff > 0) //adding 0 tfs to docs who don't contain the term
				{
					int doc_length = docLengthTFList.get(0).getDoc_length();
					for (int v = 0; v<sizeDiff; v++)
					{
						docLengthTFList.add(new DocLengthTF(doc_length, 0));
					}
				}

				double laplaceScoreForQuery = 0;
				for (int k=0; k<docLengthTFList.size(); k++)
				{
					int tf = docLengthTFList.get(k).getTf();
					int doc_length = docLengthTFList.get(k).getDoc_length();

					double laplaceTerm = ((double)tf + 1.0) / ((double)doc_length + 178050);

					double laplaceScore = Math.log(laplaceTerm);
					laplaceScoreForQuery = laplaceScoreForQuery + laplaceScore;
				}
				
				if (queryIdDocGradeMap.containsKey(queryNumber))
				{
					HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNumber);
					if (docIdGradeMap.containsKey(doc_no))
					{
						laplaceMapForQuery.put(new QueryDoc(queryNumber, doc_no), laplaceScoreForQuery);
					}
				}

				


				it.remove(); // avoids a ConcurrentModificationException
			}

			System.out.println(queryNumber);
		}
	}

	public static void makeJelinekMercerMap(ArrayList<Query> queriesPassed) throws IOException
	{
		Directory index = FSDirectory.open(new File(indexPath));
		IndexReader reader = DirectoryReader.open(index);

		HashMap<String, Long> totalFrequenciesOfTermsMap = new HashMap<String, Long>();

		for (int p=0; p<queries.size(); p++)
		{
			ArrayList<String> queryWordsTemp = queries.get(p).getQueryWords();
			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				String term =  queryWordsTemp.get(j);
				//long ttf = getTotalTermFrequency (stemmer(term), reader);
				long ttf = getTotalTermFrequency (stemmer(term), reader);
				totalFrequenciesOfTermsMap.put(queryWordsTemp.get(j), ttf);
			}
		}

		for (int i=0; i<queriesPassed.size(); i++)
		{
			String queryNumber = queriesPassed.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesPassed.get(i).getQueryWords();

			ArrayList<JelinekMercer> jelinekMercerResultsforQuery = new ArrayList<JelinekMercer>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				//String stemmedWord = stemmer(queryWordsTemp.get(j));
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				//QueryBuilder qb = QueryBuilders.matchQuery("text", stemmedWord);
				System.out.println(queries.size() - i + " ----> " + queryWordsTemp.get(j));
				ArrayList<JelinekMercer> jelinekMercerResultsforTerm = getJelinekMercerResults(qb, queryWordsTemp.get(j));
				jelinekMercerResultsforQuery.addAll(jelinekMercerResultsforTerm);
			}

			HashMap<String, ArrayList<DocLengthTermTF>> uniqueDocsLengthsTFsMap = new HashMap<String, ArrayList<DocLengthTermTF>>();

			for (int h=0; h<jelinekMercerResultsforQuery.size(); h++)
			{
				String doc_no = jelinekMercerResultsforQuery.get(h).getDoc_no();

				if (uniqueDocsLengthsTFsMap.containsKey(doc_no))
				{
					ArrayList<DocLengthTermTF> docLengthTFTermList = uniqueDocsLengthsTFsMap.get(doc_no);

					int doc_length = jelinekMercerResultsforQuery.get(h).getDoc_length();
					int tf = jelinekMercerResultsforQuery.get(h).getTf();
					String term = jelinekMercerResultsforQuery.get(h).getTerm();

					docLengthTFTermList.add(new DocLengthTermTF(doc_length, tf, term));

					uniqueDocsLengthsTFsMap.replace(doc_no, docLengthTFTermList);
				}
				else
				{	
					ArrayList<DocLengthTermTF> docLengthTFTermList = new ArrayList<DocLengthTermTF>();

					int doc_length = jelinekMercerResultsforQuery.get(h).getDoc_length();
					int tf = jelinekMercerResultsforQuery.get(h).getTf();
					String term = jelinekMercerResultsforQuery.get(h).getTerm();

					docLengthTFTermList.add(new DocLengthTermTF(doc_length, tf, term));
					uniqueDocsLengthsTFsMap.put(doc_no, docLengthTFTermList);
				}
			}

		

			Iterator it = uniqueDocsLengthsTFsMap.entrySet().iterator();

			while (it.hasNext() ) {
				//System.out.println(count + " of " + uniqueDocsLengthsTFsMap.size());
				Map.Entry pair = (Map.Entry)it.next();
				String doc_no = (String) pair.getKey();
				ArrayList<DocLengthTermTF> docLengthTFTermList = (ArrayList<DocLengthTermTF>) pair.getValue();

				int sizeDiff = queryWordsTemp.size() - docLengthTFTermList.size();

				if (sizeDiff > 0)
				{
					for (int f = 0; f<queryWordsTemp.size(); f++)
					{
						boolean flag = false;
						String term = queryWordsTemp.get(f);
						for (int k=0; k<docLengthTFTermList.size(); k++)
						{
							DocLengthTermTF dctf = docLengthTFTermList.get(k);
							String dctfTerm = dctf.getTerm();
							if (dctfTerm.equals(term))
							{
								flag = true;
								break;
							}
						}
						if (flag == false)
						{
							int doc_length = docLengthTFTermList.get(0).getDoc_length();
							docLengthTFTermList.add(new DocLengthTermTF(doc_length, 0, term));
						}
					}
				}

				double jelinekMercerScoreForQuery = 0;
				for (int k=0; k<docLengthTFTermList.size(); k++)
				{
					double tf = (double)docLengthTFTermList.get(k).getTf();
					double doc_length = (double)docLengthTFTermList.get(k).getDoc_length();
					String term = docLengthTFTermList.get(k).getTerm();
					double ttf = totalFrequenciesOfTermsMap.get(term);

					if (ttf == 0 && tf == 0)
					{
						continue;
					}

					double lambda = 0.1995;
					//double lambda = doc_length / (doc_length + avg_doc_length);
					double term1 = lambda * (tf/doc_length);
					double term2 = (1.00 - lambda) *  (ttf - tf)/(total_doc_length - doc_length);
					double jelinekMercerScoreForTerm =  Math.log(term1 + term2);

					jelinekMercerScoreForQuery = jelinekMercerScoreForQuery + jelinekMercerScoreForTerm;
				}
				
				
				if (queryIdDocGradeMap.containsKey(queryNumber))
				{
					HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNumber);
					if (docIdGradeMap.containsKey(doc_no))
					{
						jelinekMercerMapForQuery.put(new QueryDoc(queryNumber, doc_no), jelinekMercerScoreForQuery);
					}
				}
				
				it.remove(); // avoids a ConcurrentModificationException

			}
		}
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

	private static int getFrequencyInQuery (String queryTerm, ArrayList<String> query)
	{
		int freq = 0;
		for (int i=0; i<query.size(); i++)
		{
			if (query.get(i).equals(queryTerm))
			{
				freq++;
			}
		}
		return freq;
	}


	private static long getTotalTermFrequency (String term, IndexReader reader) throws IOException
	{
		Term termInstance = new Term("text", term);                              
		long termFreq = reader.totalTermFreq(termInstance);
		System.out.println("term: "+term+", termFreq = "+termFreq);
		return termFreq;

	}

	private static String stemmer(String word){
		PorterStemmer obj = new PorterStemmer();
		obj.setCurrent(word);
		obj.stem();
		return obj.getCurrent();
	}


	private static long getVocabularySize(String field) {
		MetricsAggregationBuilder aggregation =
				AggregationBuilders
				.cardinality("agg")
				.field(field);
		SearchResponse sr = client.prepareSearch(index).setTypes(type)
				.addAggregation(aggregation)
				.execute().actionGet();

		Cardinality agg = sr.getAggregations().get("agg");
		long value = agg.getValue();
		return value;
	}

	public static long getNumberOfDocs ()
	{
		CountResponse cr = client.prepareCount().setIndices(index).setTypes(type).execute().actionGet();
		return cr.getCount();	
	}

	private static void populateQueryList()
	{
		File queryFile = new File(queryFilePath);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(queryFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		try {
			int count = 1;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0)
				{	
					break;
				}
				//System.out.println(count + "-------->>>" + line);
				queryList.add(line);
				count++;
			}
			br.close();

		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void makeQueryList()
	{
		for (int i = 0; i < queryList.size(); i++)
		{
			String queryNumber = "";
			char ch[] = queryList.get(i).toCharArray();
			for (int j=0; j < ch.length; j++)
			{ 
				if (Character.isDigit(ch[j]))
				{
					queryNumber = queryNumber + ch[j];
				}
				else
				{
					break;
				}
			}
			stopWords.add("document"); 
			stopWords.add("will");
			stopWords.add("discuss");
			stopWords.add("report");
			stopWords.add("include");
			stopWords.add("describe");
			stopWords.add("identify");
			stopWords.add("predict");
			stopWords.add("cite");

			String query = removeStopWords(queryList.get(i));

			String[] words = query.split("\\s+");

			ArrayList<String> queryWords = new ArrayList<String>();

			for (int z = 0; z < words.length; z++) {
				if (words[z].contains("-"))
				{
					String wordsHyphen[] = words[z].split("-");
					for (int l = 0; l<wordsHyphen.length; l++)
					{
						queryWords.add(wordsHyphen[l].toLowerCase());
					}
				}

				else if (words[z].toLowerCase().contains("u.s"))
				{
					queryWords.add("united");
					queryWords.add("states");
				}
				else
				{
					queryWords.add(words[z].toLowerCase());
				}
			}
			queryWords.remove(0);

			for (int z=0; z<queryWords.size(); z++)
			{

				queryWords.set(z, queryWords.get(z).replaceAll("[^a-zA-Z0-9.\\s\']", ""));
			}

			queryWords.set(queryWords.size()-1, queryWords.get(queryWords.size()-1).replaceAll("\\.", ""))  ; //removing . at the end of the sentence

			ArrayList<String> queryWordsFinal = new ArrayList<String>();
			for (int k=0; k<queryWords.size(); k++)
			{
				if (!isStopword(queryWords.get(k)) && !queryWordsFinal.contains(queryWords.get(k)))
				{
					queryWordsFinal.add(queryWords.get(k));
				}
			}
			queries.add(new Query(queryNumber, queryWordsFinal));

		}
	}
	


	public static double getAvgDocumentLength ()
	{
		MetricsAggregationBuilder aggregation =
				AggregationBuilders
				.stats("agg")
				.field("doc_length_without_stopwords")
				.script("doc['text'].values.size()");
		SearchResponse sr = node.client().prepareSearch()
				.setQuery( QueryBuilders.matchAllQuery() )
				.addAggregation( aggregation )
				.execute().actionGet();

		Stats agg = sr.getAggregations().get("agg");

		return agg.getAvg();

	}

	public static double getTotalDocLength ()
	{
		MetricsAggregationBuilder aggregation =
				AggregationBuilders
				.stats("agg")
				.field("doc_length_without_stopwords")
				.script("doc['text'].values.size()");
		SearchResponse sr = node.client().prepareSearch()
				.setQuery( QueryBuilders.matchAllQuery() )
				.addAggregation( aggregation )
				.execute().actionGet();

		// sr is here your SearchResponse object
		Stats agg = sr.getAggregations().get("agg");


		return agg.getSum();
	}

	private static void populateStopWordsSet()
	{

		File stopWordsFile = new File(stopWordsFilePath);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
				stopWords.add(line);
			}
			br.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isStopword(String word) {
		if(stopWords.contains(word)) return true;
		else return false;
	}

	public static String removeStopWords(String string) {
		String result = "";
		String[] words = string.split(" ");
		ArrayList<String> wordsList = new ArrayList<String>();
		for(String word : words)
		{
			String wordCompare = word.toLowerCase();
			if(!stopWords.contains(wordCompare))
			{
				wordsList.add(word);
			}
		}

		for (String str : wordsList){
			result = result + str + " ";
		}

		return result;
	}

	public static long getDocumentFrequency (QueryBuilder qb)
	{
		CountResponse cr = client.prepareCount().setIndices(index).setTypes(type).setQuery(qb).execute().actionGet();
		return cr.getCount();	
	}

}