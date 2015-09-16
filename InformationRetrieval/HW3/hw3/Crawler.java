package hw3;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.robots.SimpleRobotRulesParser;

public class Crawler
{
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) " +
            "Chrome/13.0.782.112 Safari/535.1";
    //public static final String PATH_CRAWL = "E:/Crawler/Docs";
    public static final String PATH_CRAWL = "//Users//rmalhotra//Downloads//Docs1";
    public static final String PATH_ROBOTS = "robots.txt";
    
    public static final String PATH_INLINK  = "//Users//rmalhotra//Downloads//Docs//graph.txt";
    //public static final String PATH_INLINK  = "E:/Crawler/Docs/graph.txt";
    private static int docNum = 1;
    
    private static FileWriter fw;
    private HashMap<String, HashSet<String>> inLinks;
    private HashMap<String, HashSet<String>> outLinks;
    private HashMap<String, BaseRobotRules> notAvailableLinks;


    public Crawler()
    {
        inLinks = new HashMap<>();
        outLinks = new HashMap<>();
        notAvailableLinks = new HashMap<>();
    }

    public String getCanonicalDomain(URL url) //convert to lower case and remove port numbers from urls
    {
        int port = url.getPort();
        if((url.getProtocol().equals("http")  && port == 80) ||
                (url.getProtocol().equals("https") && port == 443))
        {
            port = -1;
        }
        return (url.getProtocol() + "://" +
                url.getHost()     +
                (port == -1 ? "" : ":" + port)).toLowerCase();
    }

    public String getCanonicalUrl(String urlStr, String parentDomain) 
    {
        if(urlStr.contains("href=\"\""))
        {
            return null;
        }

        String curDomain = null;
        String path = null;
        try
        {
        	Pattern urlPattern = Pattern.compile("href=\"(?=(?:([^\"]+)))");
        	Matcher matcher = urlPattern.matcher(urlStr);
            if(matcher.find())
            {
                urlStr = matcher.group(1); //extract link from <a href> tag
            }
            if(urlStr.startsWith("//"))
            {
                urlStr = "http:" + urlStr;
            }

            if(urlStr.contains("http://") || urlStr.contains("https://"))  //set curDomain from url if it contains http or https
            {
                URL url = new URL(urlStr);
                curDomain = getCanonicalDomain(url);
                path = url.getPath();
                if(path.equals("/") || path.equals("")) //if path is / or empty, then return the domain
                {
                    return curDomain;
                }
            }
            else
            {
                path = urlStr;
                if(path.charAt(0) == '/')
                {
                    parentDomain = getCanonicalDomain(new URL(parentDomain));
                }

                while(path.contains("../")) //making relative path absolute
                {
                    int i = parentDomain.length() - 1;
                    while(i > 0 && parentDomain.charAt(i--) != '/');
                    while(i > 0 && parentDomain.charAt(i--) != '/');
                    if(i == 0)
                    {
                        return null;
                    }
                    parentDomain = parentDomain.substring(0, i + 2); 
                    path = path.replaceFirst("../", "");
                }
                curDomain = parentDomain.replaceAll("/$", "");
            }

         
            if(curDomain.charAt(curDomain.length() - 1) != '/' &&
               path.length() > 0 &&
               path.charAt(0) != '/') // if current domain doesn't contain /, then add / to the path
            {
                path = "/" + path;
            }
        }
        catch(IOException ioe)
        {}

        return curDomain + path;
    }

    public boolean getUnavailableLinksFromRobotsFile(String urlStr)
    {
    	SimpleRobotRulesParser parser = new SimpleRobotRulesParser();
        BaseRobotRules rRules = null;
        try
        {
            URL url = new URL(urlStr);
            String domain = url.getProtocol() + "://" +
                            url.getHost() +
                            (url.getPort() > -1 ? ":" + url.getPort() : "");
            rRules = notAvailableLinks.get(domain);

            if(rRules == null)
            {
            	System.out.println(domain + "/" + PATH_ROBOTS);
                HttpResponse res = new DefaultHttpClient()
                                   .execute(new HttpGet(domain + "/" + PATH_ROBOTS),
                                            new BasicHttpContext());
                if(res.getStatusLine().getStatusCode() == 404 &&
                   res.getStatusLine() != null)
                {
                	rRules = new SimpleRobotRules(RobotRulesMode.ALLOW_ALL);
                    EntityUtils.consume(res.getEntity());
                }
                else
                {
                	rRules = parser.parseContent(domain,
                        IOUtils.toByteArray(new BufferedHttpEntity(res.getEntity()).getContent()),
                        "text/plain", USER_AGENT);
                }
                notAvailableLinks.put(domain, rRules);
            }
        }
        catch(IOException ioe)
        {}

        if(rRules == null)
        {
            return true;
        }
        return rRules.isAllowed(urlStr);
    }

    public void writeFiles(String docNo, Document doc) throws IOException
    {
       
        System.out.println("Crawling count: " + docNum);
        StringBuilder sb = new StringBuilder();
        for(String s : outLinks.get(docNo))
        {
            sb.append(s + " ");
        }
        outLinks.remove(docNo);

        fw.write("<DOC>\n"    +
                        "<DOCNO>"    + docNo                    + "</DOCNO>\n"    +
                        "<HEAD>"     + doc.title().trim()       + "</HEAD>\n"     +
                        "<OUTLINKS>" + sb.toString().trim()     + "</OUTLINKS>\n" +
                        "<TEXT>\n"   + doc.body().text().trim() + "\n</TEXT>\n"   +
                        "<HTML>\n"   + doc.html()               + "\n</HTML>\n"   +
                        "</DOC>\n");

        if(++docNum % 100 == 1)
        {
            fw.flush();
            fw.close();
            fw = new FileWriter(PATH_CRAWL + "/cDocument" + docNum, true);
        }
    }
    
    public void writeInLinksToFile() throws IOException
    {
        FileWriter graphWriter = new FileWriter(PATH_INLINK, true);
        for(String url : inLinks.keySet())
        {
            StringBuilder sb = new StringBuilder(url + " ");
            for(String s : inLinks.get(url))
            {
                sb.append(s + " ");
            }
            graphWriter.write(sb.toString().trim() + "\n");
        }
        graphWriter.close();
    }

    public void nextFrontier(UrlMap map, UrlMap newMap) //only when the first level has 0 elements, then copy the elements from the next level into it
    {
        if(map.getSize() == 0)
        {
            map.getInternalMap().putAll(newMap.getInternalMap());
            newMap.clear();
        }
    }

    public void crawlWebpages(ArrayList<String> urls)
    {
        if(urls == null || urls.size() == 0)
        {
            return;
        }

       
        Document doc;
        UrlMap map = new UrlMap();
        UrlMap newMap = new UrlMap();

        for(String url : urls)
        {
            url = getCanonicalUrl(url, "");
            map.add(url, 0);
            inLinks.put(url, new HashSet<String>());
            outLinks.put(url, new HashSet<String>());
        }
        
        //only when the current map's size is 0, we move to the next level, so the seeds are always crawled first

         Implement BFS 
        while(map.getSize() > 0 && docNum <= 20000)
        {
            String url = null;
            try
            {
                url = map.remove().getUrl(); //remove and get url from map
                if(!getUnavailableLinksFromRobotsFile(url))
                {
                    nextFrontier(map, newMap); //if size of map is zero, put newMap in Map and clear newMap
                    continue;
                }
                Response res;
                Thread.sleep(1000);
                res = Jsoup.connect(url).userAgent(USER_AGENT).timeout(1000).execute();

                if(res == null || !res.contentType().contains("text/html")) //check if the response content type is text/html
                {
                    nextFrontier(map, newMap); //if size of map is zero, put newMap in Map and clear newMap
                    continue;
                }

                System.out.println("Crawling the URL:  " + url);
                doc = res.parse();
                for(Element e : doc.select("a[href]")) //for each outlink in doc
                {
                    if(e.toString().contains("javascript"))
                    {
                        continue;
                    }

                    String newUrl = getCanonicalUrl(e.toString(), url); //get canonicalized url from element e
                    if(newUrl != null && !newUrl.equals(url))
                    {
                         Update data structures for the parent URL. 
                        outLinks.get(url).add(newUrl); //add outlinks for the parent url

                         Create and update data structures for the child URL. 
                        if (!inLinks.containsKey(newUrl)) //add inlinks for the child url
                        {
                            inLinks.put(newUrl, new HashSet<String>()); 
                            outLinks.put(newUrl, new HashSet<String>());
                        }
                        inLinks.get(newUrl).add(url); //add the parent url to the inlink count of the child url
                        if(map.urlCheck(newUrl)) //check if the map contains the given url, if yes -> update
                        {
                            map.update(newUrl, inLinks.get(newUrl).size());
                        }
                        else //if not, put/update in the new map
                        {
                            newMap.update(newUrl, inLinks.get(newUrl).size());
                        }
                    }
                }
                writeFiles(url, doc);
                nextFrontier(map, newMap); //if size of map is zero, put newMap in Map and clear newMap
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	continue;
            }
            catch (OutOfMemoryError ignored) {
                System.out.println("Out of memory");
                continue;
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
    	fw = new FileWriter(PATH_CRAWL + "/Doc" + docNum, true);
        
    	Crawler crawler = new Crawler();
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://en.wikipedia.org/wiki/Lists_of_nuclear_disasters_and_radioactive_incidents");
        urls.add("http://en.wikipedia.org/wiki/Nuclear_and_radiation_accidents_and_incidents");
        urls.add("https://en.wikipedia.org/wiki/Kyshtym_disaster");

        crawler.crawlWebpages(urls);
        crawler.writeInLinksToFile();
        System.out.println("Crawling done");
    }
}