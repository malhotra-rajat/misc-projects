package hw7;


import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import hw1.model.XContentBuilderWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.JDomSerializer;
import org.htmlcleaner.TagNode;

public class BuildIndex {

		static String folder = "E:\\hw7data\\trec07p\\trec07p\\data";
	
	static String clusterName = "IRHW3Team2015";
	
	static HashMap<String, String> labelMap = new HashMap<String, String>();
	static HashMap<String, String> spamMap = new HashMap<String, String>();
	static HashMap<String, String> hamMap = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {

		Node node = nodeBuilder().client(true).clusterName(clusterName).node();
		Client client = node.client();

		populateLabelsAndSpamHamMap();
		
		List<File> files = DirWalker.getFiles(folder);
		// index, starting from 0
		Integer count = 0;
		for (File file : files) {
			List<XContentBuilder> builders = getBuilders(file);
			System.out.println("creating index now...");
			for (XContentBuilder builder : builders) {
				System.out.println("Count: " + count);
				client.prepareIndex("trec07", "document", count.toString())
				.setSource(builder)
				.execute()
				.actionGet();
				++count;
			}
		}
		node.close();
	}

	private static void populateLabelsAndSpamHamMap() throws Exception
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
			
			if (label.equals("spam"))
			{
				spamMap.put(file_name, "");
			}
			if (label.equals("ham"))
			{
				hamMap.put(file_name, "");
			}
			i++;
			System.out.println(i);
		}
		
		int pc20Spam = (int) (0.2 * spamMap.size());
		int pc20Ham = (int) (0.2 * hamMap.size());
		
		int countSpam = 1;
		for (Map.Entry<String, String> entry : spamMap.entrySet()) {
			
			if (countSpam < pc20Spam)
			{
				entry.setValue("test");
			}
			else
			{
				entry.setValue("train");
			}
			countSpam++;
		}
		
		int countHam = 1;
		for (Map.Entry<String, String> entry : hamMap.entrySet()) {
			
			if (countHam < pc20Ham)
			{
				entry.setValue("test");
			}
			else
			{
				entry.setValue("train");
			}
			countHam++;
		}
		br.close();
		
		System.out.println(labelMap.size());
	}
	private static List<XContentBuilder> getBuilders(File file)
	{
		List<XContentBuilder> xContentBuilderList = new ArrayList<XContentBuilder>();
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(file));
			String line;
			//String textString = null;
			StringBuilder text = new StringBuilder();
			XContentBuilder xcb = XContentFactory.jsonBuilder();
			
			while ((line = br.readLine()) != null) {
				text.append(" " + line);
			}
			String docno = file.getName();
			String textString =  text.toString().trim().toLowerCase().replaceAll("\\s+", " ");
			System.out.println(textString);
			
			System.out.println("------------");
			HtmlCleaner cleaner = new HtmlCleaner();
		
			TagNode node = cleaner.clean(textString);
			
			System.out.println(node.getText());
			
			
			String split = "";			
			if  (labelMap.get(docno).equals("spam"))
			{
				split = spamMap.get(docno);
			}
			if  (labelMap.get(docno).equals("ham"))
			{
				split = hamMap.get(docno);
			}
			
			xcb.startObject()
			.field("file_name", docno)
			.field("label", labelMap.get(docno))
			.field("body",  textString)
			.field("split", split)
			.endObject();

			xContentBuilderList.add(xcb);

			br.close();
		}
		catch (Exception e)
		{	
			e.printStackTrace();
		}

		return xContentBuilderList;
	}
}