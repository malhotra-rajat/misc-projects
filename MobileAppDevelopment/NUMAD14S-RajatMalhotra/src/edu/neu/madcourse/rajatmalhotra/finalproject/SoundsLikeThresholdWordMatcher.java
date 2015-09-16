package edu.neu.madcourse.rajatmalhotra.finalproject;

import org.apache.commons.codec.language.Soundex;


public class SoundsLikeThresholdWordMatcher extends WordMatcher {
	private int minimumCharactersSame;
	protected static Soundex soundex;

    public SoundsLikeThresholdWordMatcher(int minimumCharactersSame,
            String... wordsIn)
    {
        super(wordsIn);
        this.minimumCharactersSame = minimumCharactersSame;
        soundex = new Soundex();
    }

    @Override
    public boolean isIn(String wordCheck)
    {
        boolean in = false;
        String compareTo = soundex.encode(wordCheck);
        for (String word : getWords())
        {
            if (sameEncodedString(word, compareTo))
            {
                in = true;
                break;
            }
        }
        return in;
    }

    private boolean sameEncodedString(String s1, String s2)
    {
        int numSame = 0;
        for (int i = 0; i < s1.length(); i++)
        {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 == c2)
            {
                numSame++;
            }
        }
        return (numSame >= minimumCharactersSame);
    }
}
