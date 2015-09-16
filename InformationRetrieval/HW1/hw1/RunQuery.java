package hw1;


import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import hw1.model.DocLengthTF;
import hw1.model.DocLengthTermTF;
import hw1.model.JelinekMercer;
import hw1.model.Laplace;
import hw1.model.OkapiBM25;
import hw1.model.OkapiTF;
import hw1.model.Query;
import hw1.model.QueryDocScoreRank;
import hw1.model.TFIDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class RunQuery {

	//static String stopWordsFilePath = "//Users//rmalhotra//Downloads//IRData//AP_DATA//stoplist.txt";
	static String stopWordsFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//stoplist.txt";

	//static String queryFilePath = "//Users//rmalhotra//Downloads//IRData//AP_DATA//query_desc.51-100.short.txt";  
	static String queryFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//query_desc.51-100.short.txt";  

	static String indexPath = "E:\\elasticsearch-1.5.2\\elasticsearch-1.5.2\\data\\elasticsearch\\nodes\\0\\indices\\ap_dataset\\0\\index";
	//static String indexPath = "//Users//rmalhotra//Downloads//elasticsearch-1.5.2//data//elasticsearch//nodes//0//indices//ap_dataset//0//index";


	static ArrayList<String> queryList = new ArrayList<String>();

	public static ArrayList<String> stopWords = new ArrayList<String>();

	static Node node;
	static Client client;

	static ArrayList<Query> queries = new ArrayList<Query>();

	static ArrayList<OkapiTF> docQueryTermTFs = new ArrayList<OkapiTF>();

	static String index = "ap_dataset";
	static String type = "document";


	static long totalDocs;
	static double avg_doc_length;
	static double vocabulary_size;
	static double total_doc_length;

	public static void main(String[] args) throws IOException {
		node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		client = node.client();

		populateStopWordsSet();
		populateQueryList();

		makeQueryList();

		avg_doc_length = getAvgDocumentLength();
		vocabulary_size = getVocabularySize("text");
		totalDocs = getNumberOfDocs();
		total_doc_length = getTotalDocLength();

		okapiTFToFile();
		tFidFToFile();
		bm25ToFile();
		laplaceToFile();
		jelinekMercerToFile();
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

	public static ArrayList<QueryDocScoreRank> okapiTFToFile()
	{
		ArrayList<QueryDocScoreRank> okapiTFQueryDocScoreRankListList = new ArrayList<QueryDocScoreRank>();

		for (int i=0; i<queries.size(); i++)
		{
			String queryNumberTemp = queries.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();


			LinkedHashMap<String, Double> docNoOkapiTfMapForTerm = new LinkedHashMap<String, Double>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));

				System.out.println(queryWordsTemp.get(j));
				ArrayList<OkapiTF> queryTFresultsForTerm = getOkapiTfResults(qb, queryNumberTemp, queryWordsTemp.get(j));

				for (int x=0; x < queryTFresultsForTerm.size(); x++)
				{
					String doc_no = queryTFresultsForTerm.get(x).getDoc_no();
					double okapiTf = queryTFresultsForTerm.get(x).getOkapiTf();


					if (docNoOkapiTfMapForTerm.containsKey(doc_no))
					{
						double okapiTfPrevious = docNoOkapiTfMapForTerm.get(doc_no);
						docNoOkapiTfMapForTerm.put(doc_no, okapiTfPrevious + okapiTf);
					}
					else
					{
						docNoOkapiTfMapForTerm.put(doc_no, okapiTf);
					}
				}

			}
			System.out.println(queryNumberTemp);

			LinkedHashMap<String, Double> docNoOkapiTfMapForTermSorted = new LinkedHashMap<String, Double>();

			docNoOkapiTfMapForTermSorted = sortByComparator(docNoOkapiTfMapForTerm);

			int rank = 1;
			Iterator it = docNoOkapiTfMapForTermSorted.entrySet().iterator();
			while (it.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				String doc_no = (String) pair.getKey();
				Double score = (Double) pair.getValue();
				okapiTFQueryDocScoreRankListList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				it.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			docNoOkapiTfMapForTerm.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//eval//okapi.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//write results to file
		for (int i=0; i<okapiTFQueryDocScoreRankListList.size(); i++)
		{
			writer.println(okapiTFQueryDocScoreRankListList.get(i).getQuery_no() + " Q0 " + okapiTFQueryDocScoreRankListList.get(i).getDoc_no() 
					+ " " + okapiTFQueryDocScoreRankListList.get(i).getRank() + " " + okapiTFQueryDocScoreRankListList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		return null;

	}


	public static ArrayList<QueryDocScoreRank> tFidFToFile()
	{
		ArrayList<QueryDocScoreRank> tfIdfQueryDocScoreRankList = new ArrayList<QueryDocScoreRank>();
		
		for (int i=0; i<queries.size(); i++)
		{
			String queryNumberTemp = queries.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();

			LinkedHashMap<String, Double> tfIdfMapForTerm = new LinkedHashMap<String, Double>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));

				System.out.println(queryWordsTemp.get(j));

				QueryBuilder qbDocFreq = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				long doc_frequency = getDocumentFrequency(qbDocFreq);

				ArrayList<TFIDF> tfIdfResults = getTfIdfResults(qb, queryNumberTemp, queryWordsTemp.get(j), doc_frequency);

				for (int x=0; x < tfIdfResults.size(); x++)
				{
					String doc_no = tfIdfResults.get(x).getDoc_no();
					double tfidf = tfIdfResults.get(x).getTfIdf();


					if (tfIdfMapForTerm.containsKey(doc_no))
					{
						double tfIdfPrevious = tfIdfMapForTerm.get(doc_no);


						tfIdfMapForTerm.put(doc_no, tfIdfPrevious + tfidf);
					}
					else
					{
						tfIdfMapForTerm.put(doc_no, tfidf);
					}
				}
			}
			System.out.println(queryNumberTemp);
			LinkedHashMap<String, Double> tfIdfMapForTermSorted = new LinkedHashMap<String, Double>();

			tfIdfMapForTermSorted = sortByComparator(tfIdfMapForTerm);

			int rank = 1;
			Iterator it = tfIdfMapForTermSorted.entrySet().iterator();
			while (it.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				String doc_no = (String) pair.getKey();
				Double score = (Double) pair.getValue();
				tfIdfQueryDocScoreRankList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				it.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			tfIdfMapForTerm.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//eval//tfIdf.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//write results to file
		for (int i=0; i<tfIdfQueryDocScoreRankList.size(); i++)
		{
			writer.println(tfIdfQueryDocScoreRankList.get(i).getQuery_no() + " Q0 " + tfIdfQueryDocScoreRankList.get(i).getDoc_no() 
					+ " " + tfIdfQueryDocScoreRankList.get(i).getRank() + " " + tfIdfQueryDocScoreRankList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		return null;

	}

	public static ArrayList<QueryDocScoreRank> bm25ToFile()
	{
		ArrayList<QueryDocScoreRank> bm25QueryDocScoreRankList = new ArrayList<QueryDocScoreRank>();
		
		//ArrayList<Query> queriesBM25 = makeQueryListBM25();
		ArrayList<Query> queriesBM25 = queries;
		
		for (int i=0; i<queriesBM25.size(); i++)
		{
			String queryNumberTemp = queriesBM25.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queriesBM25.get(i).getQueryWords();


			LinkedHashMap<String, Double> bm25MapForTerm = new LinkedHashMap<String, Double>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));


				QueryBuilder qbDocFreq = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				long doc_frequency = getDocumentFrequency(qbDocFreq);

				System.out.println(queryWordsTemp.get(j));
				ArrayList<OkapiBM25> bm25Results = getOkapiBM25Results(qb, queryNumberTemp, queryWordsTemp.get(j), getFrequencyInQuery(queryWordsTemp.get(j), queryWordsTemp),
						doc_frequency);

				for (int x=0; x < bm25Results.size(); x++)
				{
					String doc_no = bm25Results.get(x).getDoc_no();
					double tfidf = bm25Results.get(x).getBm25();


					if (bm25MapForTerm.containsKey(doc_no))
					{
						double tfIdfPrevious = bm25MapForTerm.get(doc_no);


						bm25MapForTerm.put(doc_no, tfIdfPrevious + tfidf);
					}
					else
					{
						bm25MapForTerm.put(doc_no, tfidf);
					}
				}
			}
			System.out.println(queryNumberTemp);

			LinkedHashMap<String, Double> tfIdfMapForTermSorted = new LinkedHashMap<String, Double>();

			tfIdfMapForTermSorted = sortByComparator(bm25MapForTerm);

			int rank = 1;
			Iterator it = tfIdfMapForTermSorted.entrySet().iterator();
			while (it.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				String doc_no = (String) pair.getKey();
				Double score = (Double) pair.getValue();
				bm25QueryDocScoreRankList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				it.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			bm25MapForTerm.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//eval//bm25.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i=0; i<bm25QueryDocScoreRankList.size(); i++)
		{
			writer.println(bm25QueryDocScoreRankList.get(i).getQuery_no() + " Q0 " + bm25QueryDocScoreRankList.get(i).getDoc_no() 
					+ " " + bm25QueryDocScoreRankList.get(i).getRank() + " " + bm25QueryDocScoreRankList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		return null;

	}


	public static ArrayList<QueryDocScoreRank> laplaceToFile()
	{
		ArrayList<QueryDocScoreRank> laplaceQueryDocScoreRankList = new ArrayList<QueryDocScoreRank>();

		for (int i=0; i<queries.size(); i++)
		{
			String queryNumberTemp = queries.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();

			ArrayList<Laplace> laplaceResultsforQuery = new ArrayList<Laplace>();

			for (int j=0; j<queryWordsTemp.size(); j++)
			{
				QueryBuilder qb = QueryBuilders.matchQuery("text", queryWordsTemp.get(j));
				System.out.println(queryWordsTemp.get(j));
				laplaceResultsforQuery.addAll(getLaplaceResults(qb, queryNumberTemp, queryWordsTemp.get(j)));
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



			LinkedHashMap<String, Double> laplaceMapForQuery = new LinkedHashMap<String, Double>();

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

				laplaceMapForQuery.put(doc_no, laplaceScoreForQuery);


				it.remove(); // avoids a ConcurrentModificationException
			}

			System.out.println(queryNumberTemp);

			LinkedHashMap<String, Double> laplaceMapForTermSorted = new LinkedHashMap<String, Double>();

			laplaceMapForTermSorted = sortByComparator(laplaceMapForQuery);

			int rank = 1;
			Iterator it1 = laplaceMapForTermSorted.entrySet().iterator();
			while (it1.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it1.next();
				String doc_no = (String) pair.getKey();
				Double score = (Double) pair.getValue();
				laplaceQueryDocScoreRankList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				it1.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			laplaceMapForQuery.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//eval//laplace.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//write results to file
		for (int i=0; i<laplaceQueryDocScoreRankList.size(); i++)
		{
			writer.println(laplaceQueryDocScoreRankList.get(i).getQuery_no() + " Q0 " + laplaceQueryDocScoreRankList.get(i).getDoc_no() 
					+ " " + laplaceQueryDocScoreRankList.get(i).getRank() + " " + laplaceQueryDocScoreRankList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		System.out.println("Done writing to file");

		return null;

	}


	public static ArrayList<QueryDocScoreRank> jelinekMercerToFile() throws IOException
	{
		ArrayList<QueryDocScoreRank> jelinekMercerQueryDocScoreRankList = new ArrayList<QueryDocScoreRank>();

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

		for (int i=0; i<queries.size(); i++)
		{
			String queryNumberTemp = queries.get(i).getQueryNumber();
			ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();

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

			LinkedHashMap<String, Double> JelinekMercereMapForQuery = new LinkedHashMap<String, Double>();

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
				JelinekMercereMapForQuery.put(doc_no, jelinekMercerScoreForQuery);
				it.remove(); // avoids a ConcurrentModificationException


			}

			LinkedHashMap<String, Double> JelinekMercerMapForTermSorted = new LinkedHashMap<String, Double>();

			JelinekMercerMapForTermSorted = sortByComparator(JelinekMercereMapForQuery);

			int rank = 1;
			Iterator it1 = JelinekMercerMapForTermSorted.entrySet().iterator();
			while (it1.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it1.next();
				String doc_no = (String) pair.getKey();
				Double score = (Double) pair.getValue();
				jelinekMercerQueryDocScoreRankList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				System.out.println(queryNumberTemp + " " + doc_no + " " + score + " " + rank);
				it1.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			JelinekMercereMapForQuery.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//eval//jelinekmercer.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//write results to file
		for (int i=0; i<jelinekMercerQueryDocScoreRankList.size(); i++)
		{
			writer.println(jelinekMercerQueryDocScoreRankList.get(i).getQuery_no() + " Q0 " + jelinekMercerQueryDocScoreRankList.get(i).getDoc_no() 
					+ " " + jelinekMercerQueryDocScoreRankList.get(i).getRank() + " " + jelinekMercerQueryDocScoreRankList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		System.out.println("Done writing to file");

		return null;

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