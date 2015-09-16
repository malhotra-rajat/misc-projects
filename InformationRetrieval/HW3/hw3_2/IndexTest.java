package hw3_2;

/* Import list */
import java.io.*;
import java.util.*;
import org.json.JSONObject;
//import global.Utils;
//import global.Properties;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.RemoteTransportException;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class IndexTest
{
    private static Node node;
    private static int id;
    private static final String CLUSTER_NAME = "IRHW3Team2015";
    private static final String INDEX_NAME = "hw3ir2015";
    private static final String PATH_CRAWL = "E:\\Crawler\\Docs";    
    public static final String PATH_INLINK  = "E:\\Crawler\\Docs\\graph.txt";
    public static final String PATH_INDEXSET  = "E:\\Crawler\\index.set";
    
	public static final File dataFolder = new File(PATH_CRAWL);

    /**
     * Constructor.
     */
    public IndexTest()
    {
        node = null;
        id   = 0;
    }

    /**
     * Write the set of indexed URLs to the file system.
     * @param set
     *           The set of indexed URLs.
     * @return
     *           'true' if the write is successful.
     * @throws IOException
     */
    public boolean writeIndexedSet(HashSet<String> set)
        throws IOException
    {
        if(set == null || set.size() == 0)
        {
            return false;
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH_INDEXSET));
        out.writeObject(set);
        out.close();
        return true;
    }

    /**
     * Get the set of indexed URLs.
     * @return
     *           The set of indexed URLs.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<String> createIndexedSet()
        throws IOException, ClassNotFoundException
    {
        HashSet<String> set = new HashSet<>();
        if(new File(PATH_INDEXSET).exists())
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH_INDEXSET));
            set = (HashSet<String>) in.readObject();
            in.close();
        }
        return set;
    }


    /**
     * List all the files in the specified directory.
     * @param dirName
     *            The directory name.
     * @return
     *            The list of files in the directory.
     */
    public File[] listFiles(String dirName)
    {
        return new File(dirName).listFiles();
    }

    /**
     * Create a map of all the in-link entries from the graph file.
     * @return
     *            The resulting map.
     * @throws IOException
     */
    public HashMap<String, ArrayList<String>> createInLinkMap(String file)
        throws IOException
    {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while((line = br.readLine()) != null)
        {
            String[] words = line.split(" ");
            ArrayList<String> list = new ArrayList<>();

            if(map.containsKey(words[0]))
            {
                Utils.warning(words[0] + " is a repeated document");
            }
            for(int i = 1; i < words.length; i++)
            {
                list.add(words[i]);
            }
            map.put(words[0], list);
        }

        return map;
    }

    /**
     * Parse the contents of a file.
     * @param file
     *            The file.
     * @return
     *            The resulting content builder.
     * @throws IOException
     */
    public List<XContentBuilder> createBuilders(File file, HashMap<String, ArrayList<String>> graph)
        throws IOException
    {
        ArrayList<XContentBuilder> builderList = new ArrayList<XContentBuilder>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String docNo    = "";
        String title    = "";
        String outLinks = "";
        StringBuilder text    = new StringBuilder();
        StringBuilder html    = new StringBuilder();
        List<String> inLinks = new ArrayList<>();

        while((line = br.readLine()) != null)
        {
            if(line.equals("<DOC>"))
            {
                while(!(line = br.readLine()).contains("</DOC>"))
                {
                    if(line.contains("<DOCNO>"))
                    {
                        docNo = line.substring(line.indexOf(">") + 1,
                                               line.indexOf("</")).trim();
                    }
                    else if(line.contains("<TITLE>"))
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
                for (String s : graph.get(docNo))
                {
                    inLinks.add(s);
                }
                graph.remove(docNo);

                builderList.add(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("docno", docNo)
                        .field("title", title)
                        .field("text", text.toString())
                        .field("html_Source", html)
                        .field("in_links", inLinks)
                        .field("out_links", outLinks)
                        
                        .endObject());
                text    = new StringBuilder();
                html    = new StringBuilder();
                inLinks = new ArrayList<>();
            }
        }

        br.close();
        return builderList;
    }

    /**
     * Main method for unit testing.
     * @param args
     *            Program arguments.
     * @throws Exception
     */
    public static void main(String[] args)
    {
    	/* Calculate start time. */
        long startTime = System.nanoTime();
        Utils.cout("\n");
        Utils.cout("=======\n");
        Utils.cout("INDEXER\n");
        Utils.cout("=======\n");

        try
        {
            IndexTest i = new IndexTest();
            node = nodeBuilder().client(true).clusterName(CLUSTER_NAME).node();
            Client client = node.client();

            /* Create the in-link map. */
            Utils.cout("\n>Loading the graph into memory\n");
            HashMap<String, ArrayList<String>> graph = i.createInLinkMap(PATH_INLINK);
            /* Index files to documents. */
            File[] files = i.listFiles(PATH_CRAWL);

            /* Index, starting from 0. */
            Utils.cout("\n>Creating the index\n");
            for (File file : files)
            {
                /* Parse the file and return a list of JSON documents. */
                List<XContentBuilder> builders = i.createBuilders(file, graph);

                /* Iterate through the list of documents and index each one. */
                for (XContentBuilder builder : builders)
                {
                    String docNo = String.valueOf(new JSONObject(XContentHelper.convertToJson(
                        builder.bytes(), false)).get("docno"));
                    try
                    {
//                        if(id % 100 == 0)
//                        {
//                            Utils.echo("Processed the data for " + id + " documents");
//                        }
                    	Utils.echo("Processed the data for " + id + " documents");
                        SearchResponse res = client.prepareSearch(INDEX_NAME)
                                                   .setTypes("document")
                                                   .setQuery(QueryBuilders.matchQuery("docno", docNo))
                                                   .setExplain(true)
                                                   .execute()
                                                   .actionGet();
                        if(res.getHits().getHits().length == 0 ||
                           !(res.getHits().getHits()[0].getId()).equals(docNo))
                        {
                            client.prepareIndex(INDEX_NAME, "document", "" + docNo)
                                  .setSource(builder)
                                  .execute()
                                  .actionGet();
                        }
                        else
                        {
                            // Utils.echo("Update request for document " + docNo);
                        	if (res.getHits().getHits()[0].getSource().get("in_links") != null)
                        	{
                        	
                        	
                            HashSet<String> inLinkSet = new HashSet<>(
                                Arrays.asList(res.getHits().getHits()[0].getSource().get("in_links").toString().split(" ")));
                            Collections.addAll(inLinkSet, String.valueOf(
                                new JSONObject(XContentHelper.convertToJson(builder.bytes(), false)).get("in_links")).split(" "));
                            List<String> inLinks = new ArrayList<>();
                            for(String s : inLinkSet)
                            {
                                inLinks.add(s);
                            }
                            client.update(new UpdateRequest(INDEX_NAME, "document", "" + docNo)
                                          .doc(XContentFactory.jsonBuilder()
                                               .startObject()
                                               .field("in_links", inLinks)
                                               .endObject())).get();
                        	}
                        }
                        ++id;
                    }
                    catch(RemoteTransportException rte)
                    {
                        Utils.echo("Update request for an absent document number");
                        Utils.echo("Creating an index entry instead...");
                        client.prepareIndex(INDEX_NAME, "document", "" + docNo)
                              .setSource(builder)
                              .execute()
                              .actionGet();
                    }
                }
            }
            Utils.echo("Processed the data for " + id + " documents");
        }
        catch(Exception e)
        {
            Utils.error("Exception in main(...)");
            Utils.cout(">Stack trace\n");
            e.printStackTrace();
            Utils.cout("\n");
        }
        finally
        {
            if(node != null)
            {
                node.close();
            }
            Utils.elapsedTime(startTime, "\nCreation of index completed");
        }
    }
}
/* End of Indexer.java */

