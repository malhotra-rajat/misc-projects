package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.finalproject.WordMatcher;
import android.content.Context;

public class CancelCommand implements VoiceActionCommand {
	
	private VoiceActionExecutor executor;
    private String cancelledPrompt;
    private WordMatcher matcher;
    
    public CancelCommand(Context context, VoiceActionExecutor executor)
    {
        this.executor = executor;
        this.cancelledPrompt = context.getResources().getString(R.string.exercise_cancelled);
        this.matcher = new WordMatcher(context.getResources().getStringArray(R.array.exercise_cancel));
    }

	@Override
	public boolean interpret(WordList heard, float[] confidenceScores) {
		boolean understood = false;
        if (matcher.isIn(heard.getWords()))
        {
            executor.speak(cancelledPrompt);
            understood = true;
        }
        return understood;
	}

}
