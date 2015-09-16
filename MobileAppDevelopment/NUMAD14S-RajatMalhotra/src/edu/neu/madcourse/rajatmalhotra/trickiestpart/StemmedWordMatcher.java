package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tartarus.snowball.ext.EnglishStemmer;

import edu.neu.madcourse.rajatmalhotra.finalproject.WordMatcher;

public class StemmedWordMatcher extends WordMatcher {
	 public StemmedWordMatcher(String... wordsIn)
	    {
	        this(Arrays.asList(wordsIn));
	    }

	    public StemmedWordMatcher(List<String> wordsIn)
	    {
	        super(encode(wordsIn));
	    }
	    
	    private static List<String> encode(List<String> input)
	    {
	        List<String> encoded = new ArrayList<String>();
	        for (String in : input)
	        {
	            encoded.add(stem(in));
	        }
	        return encoded;
	    }

	    @Override
	    public boolean isIn(String word)
	    {
	        return super.isIn(stem(word));
	    } 

	    /**
	     * run the stemmer from Lucene
	     */
	    private static String stem(String word)
	    {
	        EnglishStemmer stemmer = new EnglishStemmer();
	        stemmer.setCurrent(word);
	        boolean result = stemmer.stem();
	        if (!result) 
	        {
	            return word;
	        }
	        return stemmer.getCurrent(); 
	    }
}
