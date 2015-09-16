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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public class PageRankAndHits {

	 private static HashMap<String, HashSet<String>> inLinks;
	 private static HashMap<String, Integer> outLinks;
	 
	 private static HashSet<String> allPageIds;
	 private static HashSet<String> sinkPageIds;
	 
		
	 
	static String clusterName = "IRHW3Team2015";
	static String index = "hw3ir2015new";
	static String type = "document";
	
	static double d1 = 0.85;
	
	static Node node;
	static Client client;
	
	static HashMap<String, Double> pageRanks = new HashMap<String, Double>();
	
	//--------
	static HashMap<String, Double> pageRanksWt2g = new HashMap<String, Double>();
	static HashSet<String> sinkPageIdsWt2g = new HashSet<String>();;
	
	//--------
	
	
	static HashSet<String> rootSet = new HashSet<String>();

	static HashSet<String> baseSet = new HashSet<String>();

	static HashMap<String, HashSet<String>> inLinksMapHits = new HashMap<String, HashSet<String>>();

	static HashMap<String, HashSet<String>> outLinksMapHits = new HashMap<String, HashSet<String>>();

	static HashMap<String, Double> hubsMap = new HashMap<String, Double>();

	static HashMap<String, Double> authorityMap = new HashMap<String, Double>();

	static ArrayList<Double> perplexityList = new ArrayList<Double>();

	static ArrayList<Double> hubsList = new ArrayList<Double>();

	static ArrayList<Double> authorityList = new ArrayList<Double>();

	private static HashMap<String, HashSet<String>> inLinksWt2g;

	private static HashMap<String, HashSet<String>> outLinksWt2g;

	private static HashSet<String> allDocIdsWt2g;

	public static void main(String[] args) throws Exception {
		
		/*   Settings settings = ImmutableSettings.settingsBuilder()
					.put("http.enabled", "false")
					.put("transport.tcp.port", "9300-9400")
					.put("discovery.zen.ping.multicast.enabled", "false")
					.put("discovery.zen.ping.unicast.hosts", "localhost").build();
		
		node = nodeBuilder().client(true).clusterName(clusterName).settings(settings).node();*/
		
		node = nodeBuilder().client(true).clusterName(clusterName).node();
		client = node.client();
		
		//-------------------------
	
		//makeInLinksOutlinksMaps();
		/*populateInLinksOutlinksMaps();
		populatePageSets();
		writePageRanksToFile();

		//--------------
		makeMapsForWt2g();
		writeWt2gPageRanksToFile();*/
		//--------------
		
		//----------------
		writeHITStoFile();
		//----------------

		System.out.println("Done");
		
		client.close();
		node.close();
		
		
	}
	
	private static void getRootSetAndBaseSet()
	{
		
		
		int d = 50;
		
		MatchQueryBuilder qb = QueryBuilders.matchQuery("text", "major nuclear accidents");
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addField("in_links").addField("out_links")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(50)
				.execute()
				.actionGet();


		int docCount = 0;
		
		while (true) {
			
			if (docCount < 1000)
			{
			
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				System.out.println(hit.getId());
			
					
					rootSet.add(hit.getId());
					baseSet.add(hit.getId());
					docCount++;
					System.out.println(docCount);
				
					
					if ((hit.getFields().get("out_links") != null))
					{
						HashSet<String> outLinksHashSet = new HashSet<String>();
						
						Iterator outLinksListIterator = hit.getFields().get("out_links").iterator();
						
						
						 while (outLinksListIterator.hasNext()){
							 
							 String outlink = (String)outLinksListIterator.next();
							 
							 outLinksHashSet.add(outlink);
							 baseSet.add(outlink);
					    }
						 
						 outLinksMapHits.put(hit.getId(), outLinksHashSet);
						
					}
					
					if ((hit.getFields().get("in_links") != null))
					{
						HashSet<String> inLinksHashSet = new HashSet<String>();
						HashSet<String> inLinksHashSetNew = new HashSet<String>();
						
						Iterator inLinksListIterator = hit.getFields().get("in_links").iterator();
						
						
						 while (inLinksListIterator.hasNext()){
							 
							 String inLink = (String) inLinksListIterator.next();
							 inLinksHashSet.add(inLink);
						    }
						
						if (inLinksHashSet.size() <= d)
						{
							for (String s : inLinksHashSet)
							{
								baseSet.add(s);
							}
							inLinksMapHits.put(hit.getId(), inLinksHashSet);
						}
						else
						{
							int countInLinks = 1;
							for (String s : inLinksHashSet)
							{
								if (countInLinks <= 50)
								{
									baseSet.add(s);
									inLinksHashSetNew.add(s);
									
								}
								countInLinks++;
							}
							inLinksMapHits.put(hit.getId(), inLinksHashSetNew);
						}
							
						
						
					}
				}
				scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
				if (scrollResp.getHits().getHits().length == 0) {
				break;
				}
			}
			else
			{
				break;
			}
			
		}
		System.out.println(rootSet.size());
		System.out.println(baseSet.size());
		System.out.println(inLinksMapHits.size());
		System.out.println(outLinksMapHits.size());
		System.out.println("done");

		
	}
	
	private static void calculateHubsAndAuthority()
	{
		getRootSetAndBaseSet();
		
		for (String docId : baseSet)
		{
			hubsMap.put(docId, (double) 1);
			authorityMap.put(docId, (double) 1);
		}
		
	
		int i = 0;
		
		double sumHubs = 0;
		double sumAuthority = 0;
		
	
		
		do
		{
			sumHubs = 0;
			sumAuthority = 0;
			double norm = 0;
			
			for (String docId : baseSet)
			{
				double authorityScore = 0;
				
				if (inLinksMapHits.get(docId) != null)
				{
					HashSet<String> inLinks = inLinksMapHits.get(docId);
			
					for (String inLink : inLinks)
					{
						authorityScore = authorityScore + hubsMap.get(inLink);
					}
					
					authorityMap.put(docId, authorityScore);
					norm = norm + Math.pow(authorityScore, 2);
					
					
					
				}
			}
			norm = Math.sqrt(norm);
			
			
			for (String docId : baseSet)
			{
				double authorityScore = authorityMap.get(docId);
				authorityMap.replace(docId, authorityScore/norm);
				
				sumAuthority += authorityScore/norm;
			}
			
			//--------------------------
			norm = 0;
			
			for (String docId : baseSet)
			{
				double hubScore = 0;
				
				if (outLinksMapHits.get(docId) != null)
				{
					HashSet<String> outLinks = outLinksMapHits.get(docId);
			
					for (String outLink : outLinks)
					{
						hubScore = hubScore + authorityMap.get(outLink);
					}
					
					hubsMap.put(docId, hubScore);
					norm = norm + Math.pow(hubScore, 2);
				}
			}
			norm = Math.sqrt(norm);
			
			
			
			for (String docId : baseSet)
			{
				double hubScore = hubsMap.get(docId);
				hubsMap.replace(docId, hubScore/norm);
				sumHubs += hubScore/norm;
			}
			
			i++;
			System.out.println(i + " : " + sumHubs + ", " + sumAuthority);
		} while (isNotConvergedHits(sumHubs, sumAuthority));
		System.out.println("done");
	}
	
	private static void writeHITStoFile()
	{
		calculateHubsAndAuthority();
		
		writeHubs();
		System.out.println("Done writing hubs");
		writeAuthority();
		System.out.println("Done writing authority");
	}
	
	private static void writeHubs()
	{
		LinkedHashMap<String, Double> hubsSorted = new LinkedHashMap<String, Double>();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//hubs.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hubsSorted = sortByComparator(hubsMap);

		int rank = 1;
		Iterator it1 = hubsSorted.entrySet().iterator();
		while (it1.hasNext() && rank <= 500) {
			Map.Entry pair = (Map.Entry)it1.next();
			String doc_no = (String) pair.getKey();
			Double score = (Double) pair.getValue();
			writer.println(doc_no + "\t" + score);
			it1.remove(); // avoids a ConcurrentModificationException
			rank++;
		}
		
		writer.close();
	}
	
	private static void writeAuthority()
	{
		LinkedHashMap<String, Double> authoritySorted = new LinkedHashMap<String, Double>();

		PrintWriter writer1 = null;
		try {
			writer1 = new PrintWriter("..//authority.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		authoritySorted = sortByComparator(authorityMap);

		int rank1 = 1;
		Iterator it2 = authoritySorted.entrySet().iterator();
		while (it2.hasNext() && rank1 <= 500) {
			Map.Entry pair = (Map.Entry)it2.next();
			String doc_no = (String) pair.getKey();
			Double score = (Double) pair.getValue();
			writer1.println(doc_no + "\t" + score);
			it2.remove(); // avoids a ConcurrentModificationException
			rank1++;
		}
		
		writer1.close();
		
	}

	
	

	
	
	
	
	private static void populatePageSets() {
		allPageIds = new HashSet<String>();
		
		sinkPageIds = new HashSet<String>();
		
		Iterator it = outLinks.entrySet().iterator();

		while (it.hasNext() ) {
			Map.Entry pair = (Map.Entry)it.next();
			String doc_no = (String) pair.getKey();
			Integer outLinks = (Integer) pair.getValue();

			allPageIds.add(doc_no); //outlinks map contains all pages
			
			if (outLinks == 0)
			{
				sinkPageIds.add(doc_no);
			}
		}
	}
	
	

	private static void makeInLinksOutlinksMaps() throws Exception
	{
		inLinks = new HashMap<String, HashSet<String>>();
		
		outLinks = new HashMap<String, Integer>();
		
		populateInLinks();
		
		FileOutputStream f = new FileOutputStream("..//inLinksMap");
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(inLinks);
		s.close();
		f.close();
		
		inLinks.clear();
		
		populateOutLinks();
		
		FileOutputStream f1 = new FileOutputStream("..//outLinksMap");
		ObjectOutputStream s1 = new ObjectOutputStream(f1);
		s1.writeObject(outLinks);
		s1.close();
		f1.close();
		
		outLinks.clear();
		
		
	}
	
	
	private static void populateInLinksOutlinksMaps() throws Exception
	{
		inLinks = new HashMap<String, HashSet<String>>();
		
		outLinks = new HashMap<String, Integer>();
		
		
		
		FileInputStream f = new FileInputStream("..//inLinksMap");
		ObjectInputStream s = new ObjectInputStream(f);
		inLinks =  (HashMap<String, HashSet<String>>) s.readObject();
		s.close();
		f.close();
		
		
		FileInputStream f1 = new FileInputStream("..//outLinksMap");
		ObjectInputStream s1 = new ObjectInputStream(f1);
		outLinks =  (HashMap<String, Integer>) s1.readObject();
		s1.close();
		f1.close();
		
	}
	
	
	private static void calculatePageRanks()
	{
		
		double h = 0;
		
		double sizeAllPages = allPageIds.size();
		for (String docId : allPageIds)
		{
			double pr = 1/sizeAllPages;
			pageRanks.put(docId, pr);
			h = h - (pr * (Math.log(pr) / Math.log(2)));
		}
		
		double perplexity = Math.pow(2, h);
		System.out.println(perplexity);
		
		int i=0;
		
		while (isNotConverged(perplexity))
		{
			
			double sinkPr = 0;
			for (String docId : sinkPageIds)
			{
				sinkPr = sinkPr + pageRanks.get(docId);
			}
			
			h = 0;
			
			for (String docId : allPageIds)
			
			{
				double newPRvalue = (1-d1)/sizeAllPages;
				newPRvalue += d1 * (sinkPr/sizeAllPages);
				
				if (inLinks.get(docId) != null)
				{
					for (String inLink : inLinks.get(docId))
					{
							double pageRankForInlink = 1/sizeAllPages;
					
							if (pageRanks.get(inLink) != null)
							{
								pageRankForInlink = pageRanks.get(inLink);
							}
							double outlinksForInlink = 1;
						
							if (outLinks.get(inLink) != null)
							{
								outlinksForInlink = outLinks.get(inLink);
							}
					
							newPRvalue += d1 * (pageRankForInlink / outlinksForInlink);
					}
				}
				
			
				pageRanks.put(docId, newPRvalue);
			}
			
			for (String docId : allPageIds)
			{
				double pr = pageRanks.get(docId);
				h = h - (pr * (Math.log(pr) / Math.log(2)));
			}
			//System.out.println(h);
			
			perplexity = Math.pow(2, h);
			System.out.println(perplexity);
			
			System.out.println(i);
			i++;
			
		}
	}
	
	private static boolean isNotConverged(double perplexity)
	{
		if (perplexityList.size() > 4)
		{
			perplexityList.remove(0);
		}
		perplexityList.add(perplexity);
		
		if (perplexityList.size() < 4)
		{
			return true;
		}
		else
		{
			int val1 = (int) (perplexityList.get(0) % 10);
			int val2 = (int) (perplexityList.get(1) % 10);
			int val3 = (int) (perplexityList.get(2) % 10);
			int val4 = (int) (perplexityList.get(3) % 10);
			
			if (val1 == val2 && val2 == val3 && val3 == val4)
			{
				return false;
			}
		}
		
		perplexityList.remove(0);
		return true;
	}

	private static void calculatePageRanksForWt2g()
	{
		double h = 0;
		
		double sizeAllPages = allDocIdsWt2g.size();
		for (String docId : allDocIdsWt2g)
		{
			double pr = 1/sizeAllPages;
			pageRanksWt2g.put(docId, pr);
			
			h = h - (pr * (Math.log(pr) / Math.log(2)));
			//System.out.println(h);
		}
		System.out.println(h);
		
		double perplexity = Math.pow(2, h);
		System.out.println(perplexity);
		int i=0;
		while (isNotConvergedW2tg(perplexity))
		{
			double sinkPr = 0;
			for (String docId : sinkPageIdsWt2g)
			{
				sinkPr = sinkPr + pageRanksWt2g.get(docId);
			}
			
			h = 0;
			
			
			for (String docId : allDocIdsWt2g)
			{
				double newPRvalue = (1-d1)/sizeAllPages;
				newPRvalue += d1 * (sinkPr/sizeAllPages);
				
				if (inLinksWt2g.get(docId) != null)
				{
					for (String inLink : inLinksWt2g.get(docId))
					{
						double pageRankForInlink = 1;
					
							if (pageRanksWt2g.get(inLink) != null)
							{
								pageRankForInlink = pageRanksWt2g.get(inLink);
							}
							double outlinksForInlink = 1;
						
							if (outLinksWt2g.get(inLink) != null)
							{
								outlinksForInlink = outLinksWt2g.get(inLink).size();
							}
					
							newPRvalue += d1 * (pageRankForInlink / outlinksForInlink);
					}
				}
				//System.out.println(oldPrValue + " : " + newPRvalue);
				
				pageRanksWt2g.put(docId, newPRvalue);
			}
			
			for (String docId : allDocIdsWt2g)
			{
				double pr = pageRanksWt2g.get(docId);
				h = h - (pr * (Math.log(pr) / Math.log(2)));
			}
			System.out.println(h);
			
			perplexity = Math.pow(2, h);
			System.out.println(perplexity);
		
			
			
			i++;
			System.out.println(i);
			
		}
	}

	static ArrayList<Double> perplexityListW2tg = new ArrayList<Double>();
	
	private static boolean isNotConvergedW2tg(double perplexity)
	{
		if (perplexityListW2tg.size() > 4)
		{
			perplexityListW2tg.remove(0);
		}
		perplexityListW2tg.add(perplexity);
		
		if (perplexityListW2tg.size() < 4)
		{
			return true;
		}
		else
		{
			int val1 = (int) (perplexityListW2tg.get(0) % 10);
			int val2 = (int) (perplexityListW2tg.get(1) % 10);
			int val3 = (int) (perplexityListW2tg.get(2) % 10);
			int val4 = (int) (perplexityListW2tg.get(3) % 10);
			
			if (val1 == val2 && val2 == val3 && val3 == val4)
			{
				return false;
			}
		}
		
		perplexityListW2tg.remove(0);
		return true;
	}
	
	
	private static boolean isNotConvergedHits(double hubsScore, double authorityScore)
	{
		if (hubsList.size() > 2 && authorityList.size() > 2)
		{
			hubsList.remove(0);
			authorityList.remove(0);
		}
		hubsList.add(hubsScore);
		authorityList.add(authorityScore);
		
		if (hubsList.size() < 2 && authorityList.size() < 2)
		{
			return true;
		}
		
		else
		{
			double hubsVal1 = hubsList.get(0);
			double hubsVal2 = hubsList.get(1);
			double hubsValDiff = Math.abs(hubsVal1 - hubsVal2);
			double authorityVal1 = authorityList.get(0);
			double authorityVal2 = authorityList.get(1);
			double authorityValDiff = Math.abs(authorityVal1 - authorityVal2);
			
			if (hubsValDiff <= 0.00001 && authorityValDiff <= 0.00001)
			{
				return false;
			}
		}
		
		hubsList.remove(0);
		authorityList.remove(0);
		return true;
	}
	
	
	
	private static void makeMapsForWt2g() throws Exception
	{
		inLinksWt2g = new HashMap<String, HashSet<String>>();
		outLinksWt2g = new HashMap<String, HashSet<String>>();
		
		allDocIdsWt2g = new HashSet<String>();
		
		File file = new File("..//wt2g_inlinks.txt");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = br.readLine()) != null) {
			
			String[] docIds = line.split(" ");
			
			HashSet<String> inLinksHashSet = new HashSet<String>();
			
			if (!allDocIdsWt2g.contains(docIds[0]))
			{
				allDocIdsWt2g.add(docIds[0]);
				outLinksWt2g.put(docIds[0], new HashSet<String>());
			}
			
			for (int i=1; i<docIds.length; i++)
			{
				inLinksHashSet.add(docIds[i]);
				
				if (!allDocIdsWt2g.contains(docIds[i]))
				{
					allDocIdsWt2g.add(docIds[i]);
				}
				
				
				if (outLinksWt2g.containsKey(docIds[i]))
				{
					HashSet<String> outLinksSet = outLinksWt2g.get(docIds[i]);
					outLinksSet.add(docIds[0]);
					outLinksWt2g.replace(docIds[i], outLinksSet);
				}
				else
				{
					HashSet<String> outLinksSet = new HashSet<String>();
					outLinksSet.add(docIds[0]);
					outLinksWt2g.put(docIds[i], outLinksSet);
				}
				
				
					
			}
			
			inLinksWt2g.put(docIds[0], inLinksHashSet);
		}
		
		br.close();
		
		Iterator it = outLinksWt2g.entrySet().iterator();

		int count = 1;
		while (it.hasNext() ) {
			Map.Entry pair = (Map.Entry)it.next();
			String doc_no = (String) pair.getKey();
			HashSet<String> outLinks = (HashSet<String>) pair.getValue();
			
			System.out.println(count + " : " + outLinks.size());
			count++;
			
			if (outLinks.size() == 0)
			{
				sinkPageIdsWt2g.add(doc_no);
			}
		}

		
		System.out.println("done");
		
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
	
	private static void writePageRanksToFile()
	{
		calculatePageRanks();
		
		LinkedHashMap<String, Double> pageRanksSorted = new LinkedHashMap<String, Double>();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//pageRanks.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pageRanksSorted = sortByComparator(pageRanks);

		int rank = 1;
		Iterator it1 = pageRanksSorted.entrySet().iterator();
		while (it1.hasNext() && rank <= 500) {
			Map.Entry pair = (Map.Entry)it1.next();
			String doc_no = (String) pair.getKey();
			Double score = (Double) pair.getValue();
			writer.println(doc_no + " " + score);
			it1.remove(); // avoids a ConcurrentModificationException
			rank++;
		}
		
		writer.close();
		
	}
	
	
	private static void writeWt2gPageRanksToFile()
	{
		calculatePageRanksForWt2g();
		
		LinkedHashMap<String, Double> pageRanksSorted = new LinkedHashMap<String, Double>();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("..//pageRanksWt2g.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pageRanksSorted = sortByComparator(pageRanksWt2g);

		int rank = 1;
		Iterator it1 = pageRanksSorted.entrySet().iterator();
		while (it1.hasNext() && rank <= 500) {
			Map.Entry pair = (Map.Entry)it1.next();
			String doc_no = (String) pair.getKey();
			Double score = (Double) pair.getValue();
			writer.println(doc_no + " " + score);
			it1.remove(); // avoids a ConcurrentModificationException
			rank++;
		}
		
		writer.close();
		
	}
	
	private static void populateInLinks()
	{
		MatchAllQueryBuilder qb = QueryBuilders.matchAllQuery();
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("in_links")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(1000)
				.execute()
				.actionGet();

		int count = 1;
		
		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				System.out.println(count);
				count++;
				
				if ((hit.getFields().get("in_links") != null))
				{
					HashSet<String> inLinksHashSet = new HashSet<String>();
					
					Iterator inLinksListIterator = hit.getFields().get("in_links").iterator();
					
					 while (inLinksListIterator.hasNext()){
						 inLinksHashSet.add((String) inLinksListIterator.next());
					    }
					
					 inLinks.put(docno, inLinksHashSet);
				}
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
	}
	
	private static void populateOutLinks()
	{
		MatchAllQueryBuilder qb = QueryBuilders.matchAllQuery();
		
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type).addFields("out_links")
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setSize(100)
				.execute()
				.actionGet();
		int count = 1;
		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = (String) hit.getId();
				count++;
				
				if ((hit.getFields().get("out_links") != null))
				{
					Iterator outLinksListIterator = hit.getFields().get("out_links").iterator();
					
					int sizeoutLinks = 0;
					 while (outLinksListIterator.hasNext()){
						 outLinksListIterator.next();
						 sizeoutLinks++;
					    }
					System.out.println(count + " : " + sizeoutLinks );
					outLinks.put(docno, sizeoutLinks);
				}
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
					new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
	}

}
