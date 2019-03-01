package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import edu.neu.madcourse.rajatmalhotra.finalproject.WordMatcher;

public class MatcherCommand implements VoiceActionCommand {

private static final String TAG = "MatcherCommand";
    
    private WordMatcher matcher;

    private OnUnderstoodListener onUnderstood;

    public MatcherCommand(WordMatcher matcher, 
            OnUnderstoodListener onUnderstood)
    {
        this.matcher = matcher;
        this.onUnderstood = onUnderstood;
    }

    @Override
    public boolean interpret(WordList heard, float[] confidence)
    {
        boolean understood = false;
        if (matcher.isIn(heard.getWords()))
        {
            understood = true;
            if (onUnderstood != null)
            {
                onUnderstood.understood();
            }
        }
        return understood;
    }

    public OnUnderstoodListener getOnUnderstood()
    {
        return onUnderstood;
    }

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}

}
