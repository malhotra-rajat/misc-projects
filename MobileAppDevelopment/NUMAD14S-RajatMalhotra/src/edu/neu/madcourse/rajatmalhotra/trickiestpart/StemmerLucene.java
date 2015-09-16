package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import org.tartarus.snowball.ext.EnglishStemmer;

public class StemmerLucene {
private static EnglishStemmer stemmer;
    
    /**
     * run the stemmer from Lucene
     */
    public static String stem(String word)
    {
        stemmer = new EnglishStemmer();
        stemmer.setCurrent(word);
        boolean result = stemmer.stem();
        if (!result) 
        {
            return word;
        }
        return stemmer.getCurrent(); 
    }
}
