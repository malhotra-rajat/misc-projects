package hw2;


import hw2.model.DocLengthTermTF;
import hw2.model.JelinekMercer;
import hw2.model.OkapiBM25;
import hw2.model.OkapiTF;
import hw2.model.Proximity;
import hw2.model.Query;
import hw2.model.QueryDocScoreRank;
import hw2.model.DocNosPositions;
import hw2.model.PositionsDocLength;
import hw2.model.TermDocNoPositions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.search.QueryWrapperFilter;
import org.tartarus.snowball.ext.PorterStemmer;

import com.google.common.collect.Sets;

public class RunQuery {
    
    static String stopWordsFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//stoplist.txt";
    static String queryFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//query_desc.51-100.short.txt";
    
    static String docIdFilePath = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\docIdFile";
    
    static HashMap<String, ArrayList<String>> docIdLengthHashMap;
    
    static ArrayList<String> queryList = new ArrayList<String>();
    
    public static ArrayList<String> stopWords = new ArrayList<String>();
    
    static ArrayList<Query> queries = new ArrayList<Query>();
    
    static ArrayList<OkapiTF> docQueryTermTFs = new ArrayList<OkapiTF>();
    
    static long totalDocs;
    static double avg_doc_length;
    static double vocabulary_size;
    static double total_doc_length;
    
    private static void makeDocIdHashMap() throws IOException
    {
        File docIdFile = new File(docIdFilePath);
        BufferedReader br = new BufferedReader(new FileReader(docIdFile));
        docIdLengthHashMap = new HashMap<String, ArrayList<String>>();
        String line;
        while ((line = br.readLine()) != null) {
            String words[] = line.split(",");
            ArrayList<String> docNoLength = new ArrayList<String>();
            docNoLength.add(words[1]);
            docNoLength.add(words[2]);
            docIdLengthHashMap.put(words[0], docNoLength);
        }
        br.close();
    }
    
    static HashMap<String, ArrayList<String>> superMainCatalogHashMap;
    
    public static void populateSuperMainCatalogHashMap() throws IOException, ClassNotFoundException 
    {
        FileInputStream fis = new FileInputStream (new File("E:\\Dropbox\\Dropbox\\IR\\indexMain\\superMainCatalogHashMap"));
        ObjectInputStream ois = new ObjectInputStream(fis);
        superMainCatalogHashMap = new HashMap<String, ArrayList<String>>();
        
        superMainCatalogHashMap = (HashMap<String, ArrayList<String>>) ois.readObject();
        ois.close();
        fis.close();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        populateStopWordsSet();
        populateQueryList();
        
        makeQueryList();
        makeDocIdHashMap();
        
        populateSuperMainCatalogHashMap();
        
        avg_doc_length = getAvgDocumentLength();
        vocabulary_size = getVocabularySize();
        totalDocs = getNumberOfDocs();
        total_doc_length = getTotalDocLength();
        
        okapiTFToFile();
        bm25ToFile();
        jelinekMercerToFile();
        proximityToFile();
    	
    }
    
    public static ArrayList<OkapiTF> getOkapiTfResults(String query_no, String term) throws ClassNotFoundException, IOException {
        ArrayList<OkapiTF> results = new  ArrayList<OkapiTF>();
        
        ArrayList<DocNosPositions> docNosTfPositionsListInIndex = getDocNosTfPositionsInIndex(term);
        for (int i=0; i<docNosTfPositionsListInIndex.size(); i++) {
            
            DocNosPositions temp = docNosTfPositionsListInIndex.get(i);
            
            ArrayList<String> value = docIdLengthHashMap.get(new Integer(temp.getDoc_no_hash()).toString());
            
            String docno = value.get(0);
            int tf =  temp.getPositions().size();
            int doc_length = Integer.parseInt(value.get(1));
            
            double okapiTf = tf/(tf + 0.5 + (1.5 * doc_length/avg_doc_length));
            results.add(new OkapiTF(docno, query_no, tf, okapiTf));
        }
        return results;
    }
    
    public static ArrayList<OkapiBM25> getOkapiBM25Results(String query_no, String term, int termFreqInQuery, long doc_frequency) throws ClassNotFoundException, IOException {
        ArrayList<OkapiBM25> results = new  ArrayList<OkapiBM25>();
        ArrayList<DocNosPositions> docNosTfPositionsListInIndex = getDocNosTfPositionsInIndex(term);
        for (int i=0; i<docNosTfPositionsListInIndex.size(); i++) {
            
            DocNosPositions temp = docNosTfPositionsListInIndex.get(i);
            
            ArrayList<String> value = docIdLengthHashMap.get(new Integer(temp.getDoc_no_hash()).toString());
            
            String docno = value.get(0);
            int tf =  temp.getPositions().size();
            int doc_length = Integer.parseInt(value.get(1));
            
            double term1 = Math.log((totalDocs + 0.5)/(doc_frequency + 0.5));
            
            double k1 = 1.2;
            double k2 = 500;
            double b = 0.75;
            
            double term2 =  (tf + (k1 * tf))/(tf + k1 * ((1-b) + (b * (doc_length/avg_doc_length))));
            double term3 = (termFreqInQuery + (k2 * termFreqInQuery))/(termFreqInQuery + k2);
            
            double bm25Score = term1 * term2 * term3;
            
            results.add(new OkapiBM25(docno, query_no, term, tf, bm25Score));
            
        }
        return results;
    }
    
    public static ArrayList<JelinekMercer> getJelinekMercerResults(String term) throws ClassNotFoundException, IOException {
        
        ArrayList<JelinekMercer> results = new  ArrayList<JelinekMercer>();
        
        ArrayList<DocNosPositions> docNosTfPositionsListInIndex = getDocNosTfPositionsInIndex(term);
        for (int i=0; i<docNosTfPositionsListInIndex.size(); i++) {
            
            DocNosPositions temp = docNosTfPositionsListInIndex.get(i);
            
            ArrayList<String> value = docIdLengthHashMap.get(new Integer(temp.getDoc_no_hash()).toString());
            
            String docno = value.get(0);
            int tf =  temp.getPositions().size();
            int doc_length = Integer.parseInt(value.get(1));
            
            results.add(new JelinekMercer(docno, doc_length, tf, term));
        }
        
        return results;
    }
    
    public static ArrayList<QueryDocScoreRank> okapiTFToFile() throws ClassNotFoundException, IOException
    {
        ArrayList<QueryDocScoreRank> okapiTFQueryDocScoreRankListList = new ArrayList<QueryDocScoreRank>();
        
        for (int i=0; i<queries.size(); i++)
        {
            String queryNumberTemp = queries.get(i).getQueryNumber();
            ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();
            
            
            LinkedHashMap<String, Double> docNoOkapiTfMapForTerm = new LinkedHashMap<String, Double>();
            
            for (int j=0; j<queryWordsTemp.size(); j++)
            {
            
                
                System.out.println(queryWordsTemp.get(j));
                ArrayList<OkapiTF> queryTFresultsForTerm = getOkapiTfResults(queryNumberTemp, queryWordsTemp.get(j));
                
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
            writer = new PrintWriter("C:\\Users\\malho_000\\Desktop\\eval\\okapi.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
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
    
    
    public static ArrayList<Proximity> getProximityResults(String query_no, ArrayList<String> queryWordsTemp) throws ClassNotFoundException, IOException {
       
    	ArrayList<Proximity> results = new  ArrayList<Proximity>();
        ArrayList<TermDocNoPositions> termDocNoPositionsList = new ArrayList<TermDocNoPositions>();
        for (int j=0; j<queryWordsTemp.size(); j++)
        {
        	   ArrayList<DocNosPositions> docNosPositionsForTerm = getDocNosTfPositionsInIndex(queryWordsTemp.get(j));
        	   termDocNoPositionsList.add(new TermDocNoPositions(queryWordsTemp.get(j), docNosPositionsForTerm));
        }
        
        HashMap<String, ArrayList<PositionsDocLength>> docNoTermsPositionsMap = new HashMap<String, ArrayList<PositionsDocLength>>();
        
        for (int h=0; h<termDocNoPositionsList.size(); h++)
        {
        	String term = termDocNoPositionsList.get(h).getTerm();
        	 ArrayList<DocNosPositions> docNosPositionsForTerm =  termDocNoPositionsList.get(h).getDocNoPositionsList();
        	 
        	 for (int k=0; k<docNosPositionsForTerm.size(); k++)
             {
             	 DocNosPositions temp = docNosPositionsForTerm.get(k);
                  
                  ArrayList<String> value = docIdLengthHashMap.get(new Integer(temp.getDoc_no_hash()).toString());
                  
                  String docno = value.get(0);
                  int doc_length = Integer.parseInt(value.get(1));
                  if (docNoTermsPositionsMap.containsKey(docno))
                  {
                 	 ArrayList<PositionsDocLength> positionsDocLengthList = docNoTermsPositionsMap.get(docno);
                 	 positionsDocLengthList.add(new PositionsDocLength(term, temp.getPositions(), doc_length));
                      docNoTermsPositionsMap.replace(docno, positionsDocLengthList);
                  }
                  else
                  {
                 	 ArrayList<PositionsDocLength> positionsDocLengthList = new ArrayList<PositionsDocLength>();
                 	 positionsDocLengthList.add(new PositionsDocLength(term, temp.getPositions(), doc_length));
                 	 docNoTermsPositionsMap.put(docno, positionsDocLengthList);
                  }
             }
        }
        
       
        Iterator it1 = docNoTermsPositionsMap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry)it1.next();
            String doc_no = (String) pair.getKey();
            ArrayList<PositionsDocLength> positionsDocLengthList = (ArrayList<PositionsDocLength>) pair.getValue();
          
            double doc_length = positionsDocLengthList.get(0).getDoc_length();
            double rangeOfWindow = 0;
            
            if (positionsDocLengthList.size() >= 2)
            {
            	ArrayList<ArrayList<Integer>> listOfPositions = new ArrayList<ArrayList<Integer>>();
            	
            	for (int i=0; i<positionsDocLengthList.size(); i++)
            	{
            		ArrayList<Integer> positionList = positionsDocLengthList.get(i).getPositions();
            		listOfPositions.add(positionList);
            	}
            	
            	rangeOfWindow = getRange(listOfPositions);
            }
            else
            {
            	rangeOfWindow = 0;
            }

            
            double C = 1500.0;
            double scoreForQueryInDoc = ((C - rangeOfWindow) * positionsDocLengthList.size())/ (doc_length + vocabulary_size);
            
            results.add(new Proximity(doc_no, query_no, scoreForQueryInDoc));
          
            it1.remove(); // avoids a ConcurrentModificationException
          }
        
        return results;
    }
    
    
    public static int getRange (ArrayList<ArrayList<Integer>> listOfPositions)
    {
    	ArrayList<HashSet<Integer>> positionsHashSet = new ArrayList<HashSet<Integer>>();
    	for (int i=0; i<listOfPositions.size(); i++)
    	{
    		HashSet<Integer> temp = new HashSet<Integer>();
    		for (int j=0; j<listOfPositions.get(i).size(); j++)
    		{
    			temp.add(listOfPositions.get(i).get(j));
    		}
    		positionsHashSet.add(temp);
    	}
    	
    	  Set<List<Integer>> merged = Sets.cartesianProduct(positionsHashSet);
    	  int range = Integer.MAX_VALUE;
    	  for (List<Integer> positionList : merged) {
    		  int temp = Math.abs((Collections.max(positionList) - Collections.min(positionList)));
    		  if (temp < range)
    		  {
    			  range = temp;
    		  }
    		}
    	  return range;
    }
    
    public static ArrayList<QueryDocScoreRank> proximityToFile() throws ClassNotFoundException, IOException
    {
        ArrayList<QueryDocScoreRank> queryDocScoreRankListList = new ArrayList<QueryDocScoreRank>();
        
        for (int i=0; i<queries.size(); i++)
        {
            String queryNumberTemp = queries.get(i).getQueryNumber();
            ArrayList<String> queryWordsTemp = queries.get(i).getQueryWords();
            
            
            LinkedHashMap<String, Double> docNoProxmityMapForQuery = new LinkedHashMap<String, Double>();
            
            
            ArrayList<Proximity> queryTFresultsForQuery = getProximityResults(queryNumberTemp, queryWordsTemp);
            
            for (int x=0; x < queryTFresultsForQuery.size(); x++)
            {
                String doc_no = queryTFresultsForQuery.get(x).getDoc_no();
                double proxmity = queryTFresultsForQuery.get(x).getProximity();
                docNoProxmityMapForQuery.put(doc_no, proxmity);
            }
            
            System.out.println(queryNumberTemp);
            
            LinkedHashMap<String, Double> docNoProxmityMapForQuerySorted = new LinkedHashMap<String, Double>();
            
            docNoProxmityMapForQuerySorted = sortByComparator(docNoProxmityMapForQuery);
            
            int rank = 1;
            Iterator it = docNoProxmityMapForQuerySorted.entrySet().iterator();
            while (it.hasNext() && rank <= 1000) {
                Map.Entry pair = (Map.Entry)it.next();
                String doc_no = (String) pair.getKey();
                Double score = (Double) pair.getValue();
                queryDocScoreRankListList.add(new QueryDocScoreRank(queryNumberTemp, doc_no, score, rank));
                it.remove(); // avoids a ConcurrentModificationException
                rank++;
            }
            docNoProxmityMapForQuery.clear();
        }
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("C:\\Users\\malho_000\\Desktop\\eval\\proximity.txt", "UTF-8");
           // writer = new PrintWriter("//Users//rmalhotra//Downloads//eval//okapi.txt", "UTF-8");
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
    
    
    public static ArrayList<QueryDocScoreRank> bm25ToFile() throws ClassNotFoundException, IOException
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
                long doc_frequency = getDocumentFrequency(queryWordsTemp.get(j));
                
                System.out.println(queryWordsTemp.get(j));
                ArrayList<OkapiBM25> bm25Results = getOkapiBM25Results(queryNumberTemp, queryWordsTemp.get(j), getFrequencyInQuery(queryWordsTemp.get(j), queryWordsTemp),
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
        	  writer = new PrintWriter("C:\\Users\\malho_000\\Desktop\\eval\\bm25.txt", "UTF-8");
            //writer = new PrintWriter("//Users//rmalhotra//Downloads//eval//bm25.txt", "UTF-8");
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
    
    public static ArrayList<QueryDocScoreRank> jelinekMercerToFile() throws IOException, ClassNotFoundException
    {
        ArrayList<QueryDocScoreRank> jelinekMercerQueryDocScoreRankList = new ArrayList<QueryDocScoreRank>();
        
        HashMap<String, Long> totalFrequenciesOfTermsMap = new HashMap<String, Long>();
        
        for (int p=0; p<queries.size(); p++)
        {
            ArrayList<String> queryWordsTemp = queries.get(p).getQueryWords();
            for (int j=0; j<queryWordsTemp.size(); j++)
            {
                String term =  queryWordsTemp.get(j);
                System.out.println(term);
                long ttf = getTotalTermFrequency (term);
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
                System.out.println(queries.size() - i + " ----> " + queryWordsTemp.get(j));
                ArrayList<JelinekMercer> jelinekMercerResultsforTerm = getJelinekMercerResults(queryWordsTemp.get(j));
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
        	  writer = new PrintWriter("C:\\Users\\malho_000\\Desktop\\eval\\jelinekmercer.txt", "UTF-8");
           // writer = new PrintWriter("//Users//rmalhotra//Downloads//eval//jelinekmercer.txt", "UTF-8");
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
    
    
    private static long getTotalTermFrequency (String term) throws IOException, ClassNotFoundException
    {
        ArrayList<DocNosPositions> listInIndex =  getDocNosTfPositionsInIndex(term);
        long ttf = 0;
        for (int i=0; i<listInIndex.size(); i++)
        {
            ttf =  ttf + listInIndex.get(i).getPositions().size();
        }
        return ttf;
        
    }
    
    private static String stemmer(String word){
        PorterStemmer obj = new PorterStemmer();
        obj.setCurrent(word);
        obj.stem();
        return obj.getCurrent();
    }
    
    
    private static long getVocabularySize() {
        return superMainCatalogHashMap.size();
    }
    
    public static long getNumberOfDocs ()
    {
        return docIdLengthHashMap.size();
        
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
                	//queryWordsFinal.add(stemmer(queryWords.get(k))); //stemming query words
                }
            }
            queries.add(new Query(queryNumber, queryWordsFinal));
            
        }
    }
    
    
    
    public static double getAvgDocumentLength ()
    {
        double total_length = 0;
        
        Iterator<Entry<String, ArrayList<String>>> it = docIdLengthHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<String> listInMap = (ArrayList<String>) pair.getValue();
            
            total_length = total_length + Integer.parseInt(listInMap.get(1));
        }
        return total_length/docIdLengthHashMap.size();
    }
    
    public static double getTotalDocLength ()
    {
        double total_length = 0;
        Iterator it = docIdLengthHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<String> listInMap = (ArrayList<String>) pair.getValue();
            
            total_length = total_length + Integer.parseInt(listInMap.get(1));
        }
        return total_length;
        
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
    
    public static long getDocumentFrequency (String term) throws ClassNotFoundException, IOException
    {
        ArrayList<DocNosPositions> listInIndex =  getDocNosTfPositionsInIndex(term);
        return listInIndex.size();
    }
    
    private static ArrayList<DocNosPositions> returnTermStatsFromFile(File mainIndexFile, File mainCatalogFile, String term) throws IOException
    {
        HashMap<String, String> catalogMainMap = new HashMap<String, String>();
        
        BufferedReader brCatalogMain  = new BufferedReader(new FileReader(mainCatalogFile));
        String line;
        while ((line = brCatalogMain.readLine()) != null) {
            String words[] = line.split(",");
            catalogMainMap.put(words[0], words[1] + "," + words[2]);
        }
        
        brCatalogMain.close();
        
        RandomAccessFile mainIndexFileRandom = new RandomAccessFile(mainIndexFile, "r");
        
        String offsetLengthString = catalogMainMap.get(term);
        String words[] = offsetLengthString.split(",");
        int offsetInFile = Integer.parseInt(words[0]);
        int lengthInFile = Integer.parseInt(words[1]);
        
        mainIndexFileRandom.seek(offsetInFile);
        
        byte[] bytes = new byte[lengthInFile];
        
        mainIndexFileRandom.read(bytes);
        String newTermStatsInFile =  new String(bytes, "UTF-8");
        newTermStatsInFile = newTermStatsInFile.trim();
        
        ArrayList<DocNosPositions> docNosTfPositionsListInFile = getDocNosPositionsFromText(newTermStatsInFile);
        
        mainIndexFileRandom.close();
        
        return docNosTfPositionsListInFile;
        
    }
    
    private static ArrayList<DocNosPositions> getDocNosPositionsFromText(String s)
    {
        ArrayList<DocNosPositions> docNosTfPositionsList = new ArrayList<DocNosPositions>();
        
        s = s.substring(1, s.length()-1);
        
        String[] docs = s.split(",");
        
        for (int i=0; i<docs.length; i++)
        {
            docs[i] =  docs[i].substring(1,docs[i].length() - 1);
        }
        
        for (int i=0; i<docs.length; i++)
        {
            String[] docNoPositions = docs[i].split("\\?");
            
            String doc_no_hash = docNoPositions[0];
            docNoPositions[1] = docNoPositions[1].substring(1, docNoPositions[1].length() - 1);
            String[] positionsStrings = docNoPositions[1].split(";");
            
            ArrayList<Integer> positions = new ArrayList<Integer>();
            for (int j=0; j<positionsStrings.length; j++)
            {
                positions.add(Integer.parseInt(positionsStrings[j]));
            }
            
            docNosTfPositionsList.add(new DocNosPositions(Integer.parseInt(doc_no_hash), positions));
        }
        
         return docNosTfPositionsList;
        
    }
    
    private static ArrayList<DocNosPositions> getDocNosTfPositionsInIndex(String term) throws IOException, ClassNotFoundException
    {
        ArrayList<DocNosPositions> docNosTfPositionsListInIndex = new ArrayList<DocNosPositions>();
        
        if (superMainCatalogHashMap.containsKey(term))
        {
            List<String> docs = superMainCatalogHashMap.get(term);
            
            for (int i=0; i<docs.size(); i++)
            {
                String parts[] = docs.get(i).split(",");
                
                File mainIndexFile = new File(parts[0]);
                File mainCatalogFile = new File(parts[1]);
                docNosTfPositionsListInIndex.addAll(returnTermStatsFromFile(mainIndexFile, mainCatalogFile, term));
            }
        }
        return docNosTfPositionsListInIndex;
    }
}