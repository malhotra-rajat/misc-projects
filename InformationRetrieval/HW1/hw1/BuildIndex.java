package hw1;


import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import hw1.model.XContentBuilderWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;

public class BuildIndex {

	public static ArrayList<String> stopWords = new ArrayList<String>();

	static String folder = "E:\\Dropbox\\Dropbox\\IR\\InformationRetrieval\\IR_data\\AP89_DATA\\AP_DATA\\ap89_collection";
	//static String folder = "//Users//rmalhotra//Downloads//IRData//AP_DATA//ap89_collection";

	//static String stopWordsFilePath = "//Users//rmalhotra//Downloads//IRData//AP_DATA//stoplist.txt";
	static String stopWordsFilePath = "E://Dropbox//Dropbox//IR//InformationRetrieval//IR_data//AP89_DATA//AP_DATA//stoplist.txt";


	static String clusterName = "IRHW3Team2015";


	public static void main(String[] args) throws Exception {

		Node node = nodeBuilder().client(true).clusterName(clusterName).node();
		Client client = node.client();

		populateStopWordsSet();


		List<File> files = DirWalker.getFiles(folder);
		// index, starting from 0
		int count = 0;
		for (File file : files) {
			List<XContentBuilderWrapper> builderWrappers = getBuilders(file);
			System.out.println("creating index now...");
			for (XContentBuilderWrapper builderWrapper : builderWrappers) {
				System.out.println("Count: " + count);
				client.prepareIndex("ap_dataset", "document", builderWrapper.getDocno())
				.setSource(builderWrapper.getXcb())
				.execute()
				.actionGet();
				++count;
			}
		}
		node.close();
	}

	private static List<XContentBuilderWrapper> getBuilders(File file)
	{
		List<XContentBuilderWrapper> xContentBuilderWrapperList = new ArrayList<XContentBuilderWrapper>();
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("<DOC>"))
				{
					XContentBuilderWrapper xcbw = new XContentBuilderWrapper();
					xcbw.setXcb(XContentFactory.jsonBuilder());

					String docno = null;
					StringBuilder text = new StringBuilder();
					String textString = null;
					String docLine;
					while (!(docLine = br.readLine()).contains("</DOC>"))
					{
						if (docLine.contains("<DOCNO>"))
						{
							int indexStart = docLine.indexOf("<DOCNO>") + 7;
							int indexEnd = docLine.indexOf("</DOCNO>");
							docno = docLine.substring(indexStart, indexEnd).trim();
						}

						if (docLine.contains("<TEXT>"))
						{
							String textLine; 
							while (!(textLine = br.readLine()).contains("</TEXT>"))
							{
								text.append(" " + textLine);
							}
						}
					}

					textString =  text.toString().trim().toLowerCase().replaceAll("\\s+", " ");
					String[] words = textString.split("\\s+");

					ArrayList<String> textStringWords = new ArrayList<String>();

					for (int z = 0; z < words.length; z++) {

						if (words[z].contains("-"))
						{
							String wordsHyphen[] = words[z].split("-");
							for (int l = 0; l<wordsHyphen.length; l++)
							{
								textStringWords.add(wordsHyphen[l].toLowerCase());
							}
						}
						else
						{
							words[z] = words[z].replaceAll("[^a-zA-Z0-9]", "");
							textStringWords.add(words[z].toLowerCase());
						}
					}

					String finalString = "";
					for(int i=0; i<textStringWords.size(); i++)
					{
						finalString = finalString + textStringWords.get(i) + " ";
					}
					finalString = removeStopWords(finalString.trim());

					xcbw.getXcb().startObject()
					.field("docno", docno)
					.field("text",  finalString)
					.field("doc_length_without_stopwords", getWordcount(finalString))
					.endObject();

					xcbw.setDocno(docno);
					xContentBuilderWrapperList.add(xcbw);

					System.out.println(xcbw.getXcb().string());
				}
			}
			br.close();
		}
		catch (Exception e)
		{	
			e.printStackTrace();
		}

		return xContentBuilderWrapperList;

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
			System.out.println(stopWords.size());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getWordcount (String text)
	{
		String trimmed = text.trim();
		Integer words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return words.toString();
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


}