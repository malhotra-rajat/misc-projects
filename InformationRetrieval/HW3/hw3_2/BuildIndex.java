package hw3_2;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.json.JSONObject;

public class BuildIndex {

	public static ArrayList<String> stopWords = new ArrayList<String>();

	static String folder = "E:\\Crawler\\Docs";
	static String clusterName = "IRHW3Team2015";
	
	public static final String PATH_INLINK  = "E:\\Crawler\\Docs\\graph.txt";

	public static void main(String[] args) throws Exception {

		Node node = nodeBuilder().client(true).clusterName(clusterName).node();
		Client client = node.client();

		populateInLinksFromFile();
		List<File> files = DirWalker.getFiles(folder);

		int count = 0;
		for (File file : files) {
			List<XContentBuilderWrapper> builderWrappers = getBuilders(file);
			System.out.println("creating index now...");
			
			for (XContentBuilderWrapper builderWrapper : builderWrappers) {
				
				String docno = String.valueOf(new JSONObject(XContentHelper.convertToJson(
						builderWrapper.getXcb().bytes(), false)).get("docno"));
	                  SearchResponse res = client.prepareSearch("hw3ir2015")
	                                             .setTypes("document")
	                                             .setQuery(QueryBuilders.matchQuery("docno", docno))
	                                             .setExplain(true)
	                                             .execute()
	                                             .actionGet();
	                  if(!String.valueOf(res.getHits().getHits()[0].getSource().get("docno")).equals(docno)
	                  		|| res.getHits().getHits().length==0)
	                  {
	                      client.prepareIndex("hw3ir2015", "document", "" + docno)
	                            .setSource(builderWrapper.getXcb())
	                            .execute()
	                            .actionGet();
	                  }
	                  else
	                  {
	                      HashSet<String> inLinkSet = new HashSet<>(
	                          Arrays.asList(res.getHits().getHits()[0].getSource().get("in_links").toString().split(" ")));
	                      Collections.addAll(inLinkSet, String.valueOf(
	                          new JSONObject(XContentHelper.convertToJson(builderWrapper.getXcb().bytes(), false)).get("in_links")).split(" "));
	                      StringBuilder inLinks = new StringBuilder();
	                      for(String s : inLinkSet)
	                      {
	                          inLinks.append(s + " ");
	                      }
	                      client.update(new UpdateRequest("hw3ir2015", "document", "" + docno)
	                                    .doc(XContentFactory.jsonBuilder()
	                                         .startObject()
	                                         .field("in_links", inLinkSet)
	                                         .endObject())).get();
	                  }
	                 
	              }
				
			}
		
		client.close();
		node.close();
	}

	private static List<XContentBuilderWrapper> getBuilders(File file)
	{
		List<XContentBuilderWrapper> xContentBuilderWrapperList = new ArrayList<XContentBuilderWrapper>();
		try {
			
			
			
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(file));
			int count = 0;
			String line;
			
			 while((line = br.readLine()) != null)
		        {
		            if(line.equals("<DOC>"))
		            {
						XContentBuilderWrapper xcbw = new XContentBuilderWrapper();
						xcbw.setXcb(XContentFactory.jsonBuilder());
		            	
		            	String docno = null;
		            	String title = null;
		            	String outLinks = null;
		            	StringBuilder text    = new StringBuilder();
		            	StringBuilder    html    = new StringBuilder();
		            	StringBuilder   inLinks = new StringBuilder();
		            	
		                while(!(line = br.readLine()).contains("</DOC>"))
		                {
		                    if(line.contains("<DOCNO>"))
		                    {
		                    	docno = line.substring(line.indexOf(">") + 1,
		                                               line.indexOf("</")).trim();
		                    }
		                    if(line.contains("<TITLE>"))
		                    {
		                        title = line.substring(line.indexOf(">") + 1,
		                                               line.indexOf("</")).trim();
		                    }
		                    else if(line.contains("<OUTLINKS>"))
		                    {
		                        outLinks = line.substring(line.indexOf(">") + 1,
		                                                  line.indexOf("</")).trim();
		                    }
		                    else if(line.contains("<TEXT>"))
		                    {
		                        while(!(line = br.readLine()).contains("</TEXT>"))
		                        {
		                            text.append(line + " ");
		                        }
		                    }
		                    else if(line.contains("<HTML>"))
		                    {
		                        while(!(line = br.readLine()).contains("</HTML>"))
		                        {
		                            html.append(line + " ");
		                        }
		                    }
		                }
		                
		                for(String s : inLinkUrlMap.get(docno))
	                    {
	                        inLinks.append(s + " ");
	                    }
		                inLinkUrlMap.remove(docno);

		                
		            	xcbw.getXcb().startObject()
						    .field("docno", docno)
		                        .field("title", title)
		                        .field("text", text.toString())
		                        .field("html", html)
		                        .field("inlinks", inLinks.toString().trim())
		                        .field("outlinks", outLinks)
		                     
					
						.endObject();

						xcbw.setDocno(docno);
						xContentBuilderWrapperList.add(xcbw);
						
						System.out.println(docno);
						count++;
						System.out.println(count);
						//System.out.println(xcbw.getXcb().string());
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
	
	static HashMap<String, ArrayList<String>> inLinkUrlMap = new HashMap<>();
	
	private static void populateInLinksFromFile() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(PATH_INLINK));
	    /* Loading InLink URLs*/
		 String line;
        while((line = br.readLine()) != null)
        {
            String[] links = line.split(" ");
            ArrayList<String> list = new ArrayList<>();
            
            for(int len = 1; len < links.length; len++)
            {
                list.add(links[len]);
            }
            inLinkUrlMap.put(links[0], list);
        }
        br.close();
	}
}






