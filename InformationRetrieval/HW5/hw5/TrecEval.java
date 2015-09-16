package hw5;

import hw1.model.QueryDocScoreRank;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class TrecEval {
	
	static boolean q = true;

	static HashMap<String, HashMap<String, Integer>> queryIdDocGradeMap;
	
	static HashMap<String, HashMap<String, Integer>> queryIdDocGradeMapNDCG;
	
	static HashMap<String, ArrayList<String>> queryDocsMapInRanking = new HashMap<String, ArrayList<String>>();
	
	static ArrayList<Double> recallValues = new ArrayList<Double>();
	
	static ArrayList<Double> f1Values = new ArrayList<Double>();
	
	static ArrayList<Double> rPrecisionValues = new ArrayList<Double>();
	
	static ArrayList<Double> avgPrecisionForQueryList = new ArrayList<Double>();
	
	static ArrayList<Double> nDCGValues = new ArrayList<Double>();
	
	
	public static void main(String[] args) throws Exception {
		
		readQrel();
		readRankedFile();
		readQrelNDCG();
		
		for(Entry<String, ArrayList<String>> entry : queryDocsMapInRanking.entrySet())
		{
			calculateValues(entry.getKey());
		}
		
		for(Entry<String, ArrayList<String>> entry : queryDocsMapInRanking.entrySet())
		{
			calculateNDCG(entry.getKey());
		}
		
		calculateAverageRecall();
		calculateAverageF1();
		calculateAveragePrecision();
		calculateAverageRPrecision();	
		calculateAverageNDCG();
	}
	
	

	private static void calculateValues(String queryNo) throws Exception
	{
		ArrayList<Double> precisionValuesQuery = new ArrayList<Double>();
		
		final XYSeries precisionRecallSeries = new XYSeries("PrecisionRecall");
		
		if (q==true)
		{
		System.out.println("Query Number : " + queryNo + "\n");
		}
		ArrayList<String> relevantDocsInRanking = new ArrayList<String>();
		ArrayList<String> nonRelevantDocsInRanking = new ArrayList<String>(); 
		ArrayList<String> nonRelevantDocsMissingInRanking = new ArrayList<String>();
		ArrayList<String> relevantDocsMissingInRanking = new ArrayList<String>();
		
		ArrayList<String> relevantDocsInQrel = new ArrayList<String>();
		ArrayList<String> nonRelevantDocsInQrel = new ArrayList<String>(); 
		
		HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMap.get(queryNo);
		
		Iterator it = docIdGradeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String doc_no = (String) pair.getKey();
			Integer grade = (Integer) pair.getValue();
			if (grade == 1)
			{
				relevantDocsInQrel.add(doc_no);
			}
			else
			{
				nonRelevantDocsInQrel.add(doc_no);
			}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		
		ArrayList<String> docNoList = queryDocsMapInRanking.get(queryNo);
		
		
		for (int j=0; j<relevantDocsInQrel.size(); j++)
		{
			String doc_no = relevantDocsInQrel.get(j);
			if (!docNoList.contains(doc_no)) //relevant docs missing in ranking FN
			{
				relevantDocsMissingInRanking.add(doc_no);
			}
		}
		
		
		for (int j=0; j<nonRelevantDocsInQrel.size(); j++)
		{
			String doc_no = nonRelevantDocsInQrel.get(j);
			if (!docNoList.contains(doc_no)) //non-relevant documents missing from the ranking FP
			{
				nonRelevantDocsMissingInRanking.add(doc_no);
			}
		}
		
		
		
		for (int k=0; k<docNoList.size(); k++)
		{
			String doc_no = docNoList.get(k);
			if (relevantDocsInQrel.contains(doc_no)) //relevant docs in the ranking TP
			{
				relevantDocsInRanking.add(doc_no);
			}
			
			if (nonRelevantDocsInQrel.contains(doc_no)) //Non relevant docs in the ranking FP
			{
				nonRelevantDocsInRanking.add(doc_no);
			}
			
			int z = k+1;
			
			
			double recall = (double) relevantDocsInRanking.size() / 
					((double)(relevantDocsInQrel.size()));
			
			double precision = (double) relevantDocsInRanking.size() / 
					((double)(z));
			
			precisionRecallSeries.add(recall, precision);
			
			double f1 = (2 * precision * recall) / (precision + recall);
			
			if (relevantDocsInQrel.contains(doc_no)) //relevant docs in the ranking TP
			{
				precisionValuesQuery.add(precision);
			}
			
			if (z == 5 || z == 10 || z == 20 || z == 50 || z == 100)
			{
				if (q== true)
				{
				System.out.println("Recall At " + z + " docs" + " : " + recall);
				System.out.println("Precision At " + z + " docs" + " : " + precision);
				System.out.println("F1 At " + z + " docs" + " : " + f1 + "\n");
				}
			}
			
			if (recall == precision && recall != 0.0)
			{
				rPrecisionValues.add(recall);
				if (q==true){
				System.out.println("R-precision for Query " + queryNo + " : " + recall);}
			}
		}
		
		
		double recall = (double) relevantDocsInRanking.size() / 
				((double)(relevantDocsInQrel.size()));
		
		if (q == true)
		{
		System.out.println("\nAverage recall for " + queryNo + " : " + recall);
		}
		double precision = (double) relevantDocsInRanking.size() / 
				((double)(docNoList.size()));
		if (q == true)
		{
		System.out.println("Precision at 1000 docs for " + queryNo + " : " + precision);
		}
		double f1 = (2 * precision * recall) / (precision + recall);
		
		if (q == true)
		{
		System.out.println("Average F1 for " + queryNo + " : " + f1);
		}
		
		recallValues.add(recall);
		
		if (!Double.isNaN(f1))
		{
			f1Values.add(f1);
		}
		
		double sumPrecisionValuesForQuery = 0;
		for (int i=0; i<precisionValuesQuery.size(); i++)
		{
			sumPrecisionValuesForQuery += precisionValuesQuery.get(i);
		}
		if (q==true){
		System.out.println("Average precision for query : " + sumPrecisionValuesForQuery/relevantDocsInQrel.size());
		}
		avgPrecisionForQueryList.add(sumPrecisionValuesForQuery/relevantDocsInQrel.size());
		
		System.out.println("Relevant docs in ranking: " + relevantDocsInRanking.size());
		System.out.println("Relevant docs missing in ranking: " + relevantDocsMissingInRanking.size());
		System.out.println("Non - Relevant docs in ranking: " + nonRelevantDocsInRanking.size());
		System.out.println("Non - Relevant docs missing in ranking: " + nonRelevantDocsMissingInRanking.size());
		
		XYSeriesCollection preceisionRecallDataSet = new XYSeriesCollection();
		preceisionRecallDataSet.addSeries(precisionRecallSeries);
		
		createChart(preceisionRecallDataSet, queryNo);
		
		System.out.println("---------------------------------------------------------");

	}
	
	private static void createChart(final XYDataset dataset, String queryNumber) throws Exception {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
        	queryNumber,    // chart title
            "Recall",                      // x axis label
            "Precision",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */ 
        File lineChartFile = new File( queryNumber + ".jpeg" ); 
        ChartUtilities.saveChartAsJPEG(lineChartFile ,chart, width ,height);
        
    }
	
	
	private static void calculateNDCG(String queryNo)
	{
		HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMapNDCG.get(queryNo);
		
		ArrayList<String> docNoList = queryDocsMapInRanking.get(queryNo);
		ArrayList<Integer> relevanceGradeVector = new ArrayList<Integer>();
		
		for (int k=0; k<docNoList.size(); k++)
		{
			String doc_no = docNoList.get(k);
			if (docIdGradeMap.containsKey(doc_no)) //relevant docs in the ranking TP
			{
				relevanceGradeVector.add(docIdGradeMap.get(doc_no));
			}
			else
			{
				relevanceGradeVector.add(0);
			}
		}
		
		double dcg = relevanceGradeVector.get(0);
		for (int i=1; i<relevanceGradeVector.size(); i++)
		{
			dcg += relevanceGradeVector.get(i) / Math.log(i+1);
		}
		
		Collections.sort(relevanceGradeVector, Collections.reverseOrder());
	
		double dcgDesc = relevanceGradeVector.get(0);
		for (int i=1; i<relevanceGradeVector.size(); i++)
		{
			dcgDesc += relevanceGradeVector.get(i) / Math.log(i+1);
		}
		
		double nDcg = dcg/dcgDesc;
		
		if (!Double.isNaN(nDcg))
		{
			nDCGValues.add(nDcg);
		}
		
		if (q==true){
		System.out.println("NDCG for query: " + queryNo + " : " + nDcg);
		System.out.println("---------------------------------------------------------");}

	}
	
	private static void readQrel() throws Exception
	{
		queryIdDocGradeMap = new HashMap<String, HashMap<String, Integer>>();
		//File file = new File ("..//qrels.adhoc.51-100.AP89.txt");
		File file = new File ("..//qrels.txt");
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
		
		for (Map.Entry<String, HashMap<String, Integer>> entry : queryIdDocGradeMap.entrySet()) {
			//System.out.println(entry.getValue().entrySet().size());//if grade < 1, then grade = 0, if it is 1 or 2, grade = 1
			for (Entry<String, Integer> docIdGradeMapEntry : entry.getValue().entrySet()) {
				
				if (docIdGradeMapEntry.getValue() < 1)
				{
					docIdGradeMapEntry.setValue(0);
				}
				else
				{
					docIdGradeMapEntry.setValue(1);
				}
				
			}
		}
		
		br.close();
	}
	
	private static void readQrelNDCG() throws Exception
	{
		queryIdDocGradeMapNDCG = new HashMap<String, HashMap<String, Integer>>();
		//File file = new File ("..//qrels.adhoc.51-100.AP89.txt");
		File file = new File ("..//qrels.txt");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
		
			String[] parts = line.split(" ");
			String queryId = parts[0];
			String assessorId = parts[1];
			String docId = parts[2];
			int grade = Integer.parseInt(parts[3]);
			
			
			if (!queryIdDocGradeMapNDCG.containsKey(queryId))
			{
				HashMap<String, Integer> docIdGradeMap = new HashMap<String, Integer>();
				docIdGradeMap.put(docId, grade);
				queryIdDocGradeMapNDCG.put(queryId, docIdGradeMap);
				
			
			}
			else
			{
				HashMap<String, Integer> docIdGradeMap = queryIdDocGradeMapNDCG.get(queryId);
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
				queryIdDocGradeMapNDCG.replace(queryId, docIdGradeMap);
			}
			i++;
		}
		
		br.close();
	}
	
	
	private static void readRankedFile() throws Exception
	{
		ArrayList<QueryDocScoreRank> queryDocScoreRankList; 
        queryDocScoreRankList = new ArrayList<QueryDocScoreRank>();
		
    	//File file = new File ("..//okapi.txt");
        File file = new File ("..//ranking.txt");
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
		
			String[] parts = line.split(" ");
			String queryId = parts[0];
			String docId = parts[2];
			Integer rank = Integer.parseInt(parts[3]);
			Double score = Double.parseDouble(parts[4]);
			
			queryDocScoreRankList.add(new QueryDocScoreRank(queryId, docId, score, rank));
			i++;
		}
		
		br.close();
		
		for (int k=0; k<queryDocScoreRankList.size(); k++)
		{
			QueryDocScoreRank temp = queryDocScoreRankList.get(k);
			if (!queryDocsMapInRanking.containsKey(temp.getQuery_no()))
			{
				ArrayList<String> docNoList = new ArrayList<String>();
				docNoList.add(temp.getDoc_no());
				queryDocsMapInRanking.put(temp.getQuery_no(), docNoList);
			}
			else
			{
				ArrayList<String> docNoList = queryDocsMapInRanking.get(temp.getQuery_no());
				docNoList.add(temp.getDoc_no());
				queryDocsMapInRanking.replace(temp.getQuery_no(), docNoList);
			}
		}
		
	}
	
	private static void calculateAverageRecall()
	{
		double sumRecall = 0;
		for (int i=0; i<recallValues.size(); i++)
		{
			sumRecall += recallValues.get(i);
		}
		System.out.println("Average recall for all queries : " + sumRecall/queryDocsMapInRanking.size());
	}
	
	private static void calculateAverageF1()
	{	double sumF1 = 0;
		for (int i=0; i<f1Values.size(); i++)
		{
			sumF1 += f1Values.get(i);
		}
		System.out.println("Average F1 for all queries (excluding NaNs): " + sumF1/queryDocsMapInRanking.size());
	}
	
	private static void calculateAveragePrecision()
	{	
		double avgPrecisionScores = 0;
		for (int i=0; i<avgPrecisionForQueryList.size(); i++)
		{
			avgPrecisionScores += avgPrecisionForQueryList.get(i);
		}
		System.out.println("Average precision for all queries : " + avgPrecisionScores/queryDocsMapInRanking.size());
		
	}
	
	private static void calculateAverageRPrecision()
	{	
		double sumRPrecisionValues = 0;
		for (int i=0; i<rPrecisionValues.size(); i++)
		{
			sumRPrecisionValues += rPrecisionValues.get(i);
		}
		System.out.println("R-precision for all queries : " + sumRPrecisionValues/queryDocsMapInRanking.size());
		
	}
	
	private static void calculateAverageNDCG()
	{	
		double sumNDCGValues = 0;
		for (int i=0; i<nDCGValues.size(); i++)
		{
			sumNDCGValues += nDCGValues.get(i);
		}
		System.out.println("NDCG for all queries : " + sumNDCGValues/queryDocsMapInRanking.size());
	}

	

}
