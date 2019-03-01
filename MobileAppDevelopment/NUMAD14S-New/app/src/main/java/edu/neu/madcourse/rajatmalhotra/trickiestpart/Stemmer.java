package edu.neu.madcourse.rajatmalhotra.trickiestpart;

public class Stemmer {
	public static String stem(String word)
    {
        return StemmerLucene.stem(word);
    }
}
