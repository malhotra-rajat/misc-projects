package hw3;

import java.util.HashSet;
import java.util.LinkedHashMap;


public class UrlMap
{
    private static HashSet<String> visited = new HashSet<>();
    private LinkedHashMap<String, Integer> map;

    /**
     * Constructor.
     */
    public UrlMap()
    {
        map = new LinkedHashMap<>();
    }
    
    class UrlMark
    {
        String url;
        boolean present;

        UrlMark(String url, boolean present)
        {
            this.url = url;
            this.present = present;
        }
    }

    /**
     * Get the internal map.
     */
    public LinkedHashMap<String, Integer> getInternalMap()
    {
        return map;
    }

    /**
     * Check if the object contains the URL.
     */
    public boolean isUrlContained(Object obj, String url) //obj can be a HashSet or a LinkedHashMap
    {
        if(obj instanceof HashSet)
        {
            return ((HashSet) obj).contains(url);
        }
        else
        {
            return ((LinkedHashMap) obj).containsKey(url);
        }
    }

    /**
     * Check if the object contains any form of the URL.
     */
    public UrlMark haveALink(Object obj, String url) //check if the url is contained in the given object which can be a Hashset 
    												//or a LinkedHashMap
    {
    	boolean present = false;

        if(isUrlContained(obj, url))
        {
            present = true;
        }
      
        return new UrlMark(url, present);
    }

    /**
     * Check if the map contains the given URL.
     */
    public boolean urlCheck(String url)
    {
        return haveALink(map, url).present;
    }

    /**
     * Add a URL and its in-link count to the map. 
     */
    public boolean add(String url, Integer inLinkCount)
    {
        map.put(url, inLinkCount);
        return true;
    }

    /**
     * Update the entry corresponding to the URL. 
     */
    public boolean update(String url, int inLinkCount)
    {
    	UrlMark t1 = haveALink(map, url);
        UrlMark t2 = haveALink(visited, url);

        /* URL is not present and hasn't been visited. */
        if(!t1.present && !t2.present)
        {
            return add(url, 1);
        }
        /* URL has been visited. */
        else if(t2.present)
        {
            return false;
        }

        /* If none of the above condition are met, update the map. */
        map.put(t1.url, inLinkCount);
        return true;
    }

    /**
     * Fetch the Frontier object with the maximum in-link count.
     */
    public Frontier remove()
    {
        if(map.size() == 0)
        {
            return null;
        }

        /* Mark the canonical Frontier as visited and return it. */
        
        //get the url with the max inLink count, key is url here
        
        String maxKey = "";
        int maxValue = Integer.MIN_VALUE;
        for(String key : map.keySet())
        {
            if(map.get(key) > maxValue)
            {
                maxKey   = key;
                maxValue = map.get(key);
            }
        }
        map.remove(maxKey);
        visited.add(maxKey);
        System.out.println(maxKey + " : " + maxValue);
        return new Frontier(maxKey, maxValue);
    }

    /**
     * Clear the map.
     */
    public void clear()
    {
        map.clear();
    }

    /**
     * Get the map's size. 
     */
    public int getSize()
    {
        return map.size();
    }
} 