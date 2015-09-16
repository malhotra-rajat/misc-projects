package hw5;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class RunQuery {

	static Node node;
	static Client client;

	static ArrayList<Query> queries = new ArrayList<Query>();

	static String index = "hw3ir2015new";
	static String type = "document";

	public static void main(String[] args) throws IOException {
		node = nodeBuilder().client(true).clusterName("IRHW3Team2015").node();
		client = node.client();

		makeQueryList();

		resultsToFile();
		
		node.close();
		client.close();
	}

	public static ArrayList<DocQueryScore> getResults(QueryBuilder qb, String query_no) {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("doc_length_without_stopwords")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(10000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return new ArrayList<DocQueryScore>();
		}

		ArrayList<DocQueryScore> results = new ArrayList<DocQueryScore>();

		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				float score = hit.getScore();
				results.add(new DocQueryScore(docno, query_no, score));
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}

		return results;
	}

	

	public static ArrayList<QueryDocScoreRank> resultsToFile()
	{
		ArrayList<QueryDocScoreRank> queryDocScoreRankListList = new ArrayList<QueryDocScoreRank>();

		for (int i=0; i<queries.size(); i++)
		{
			String queryNumberTemp = queries.get(i).getQueryNumber();
			String query = queries.get(i).getQuery();


			LinkedHashMap<String, Float> docNoOkapiTfMapForTerm = new LinkedHashMap<String, Float>();
			QueryBuilder qb = QueryBuilders.matchQuery("text", query);
				
			ArrayList<DocQueryScore> queryResultsForTerm = getResults(qb, queryNumberTemp);

			for (int x=0; x < queryResultsForTerm.size(); x++)
			{
				String doc_no = queryResultsForTerm.get(x).getDoc_no();
				float score = queryResultsForTerm.get(x).getScore();
				docNoOkapiTfMapForTerm.put(doc_no, score);
			}
				
			System.out.println(queryNumberTemp);

			LinkedHashMap<String, Float> docNoMapForTermSorted = new LinkedHashMap<String, Float>();

			docNoMapForTermSorted = sortByComparator(docNoOkapiTfMapForTerm);

			int rank = 1;
			Iterator it = docNoMapForTermSorted.entrySet().iterator();
			while (it.hasNext() && rank <= 1000) {
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				String doc_no = (String) pair.getKey();
				Float score = (Float) pair.getValue();
				queryDocScoreRankListList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
				it.remove(); // avoids a ConcurrentModificationException
				rank++;
			}
			docNoOkapiTfMapForTerm.clear();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//ranking.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//write results to file
		for (int i=0; i<queryDocScoreRankListList.size(); i++)
		{
			writer.println(queryDocScoreRankListList.get(i).getQuery_no() + " Q0 " + queryDocScoreRankListList.get(i).getDoc_no() 
					+ " " + queryDocScoreRankListList.get(i).getRank() + " " + queryDocScoreRankListList.get(i).getScore() + " Exp");
			//<query-number> Q0 <docno> <rank> <score> Exp
		}
		writer.close();

		return null;

	}

	private static LinkedHashMap<String, Float> sortByComparator(LinkedHashMap<String, Float> docNoOkapiTfMapForTerm) {

		// Convert Map to List
		List<Map.Entry<String, Float>> list = 
				new LinkedList<Map.Entry<String, Float>>(docNoOkapiTfMapForTerm.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o1,
					Map.Entry<String, Float> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for (Iterator<Map.Entry<String, Float>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Float> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	private static void makeQueryList()
	{
		ArrayList<String> queryList = new ArrayList<String>();
		
		queryList.add("152701 Fukushima nuclear accident");
		queryList.add("152702 Chernobyl accident");
		queryList.add("152703 Three Mile Island accident");
		queryList.add("152704 Kyshtym disaster");
		
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
	
			String[] words = queryList.get(i).split("\\s+");
			String query = "";
			
			for (int j=1; j<words.length; j++)
			{
				if (j == words.length - 1)
				{
					query += words[j];
				}
				else
				{
					query += words[j] + " ";
				}
			}
			queries.add(new Query(queryNumber, query));
		}
		System.out.println("done");
	}
}