package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.Arrays;

import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.finalproject.SoundsLikeThresholdWordMatcher;
import edu.neu.madcourse.rajatmalhotra.finalproject.WordMatcher;

import android.content.Context;
import android.util.Log;

public class AddExercise implements VoiceActionCommand {
	/**
	 * The Tag
	 */
	private static final String TAG = "AddExercise";
	
	private WordMatcher match;
	private VoiceActionExecutor executor;
	
	private FtsIndexedExerciseDatabase exerciseFts;	
	private Context context;
	
	public AddExercise(Context context, VoiceActionExecutor executor,
			FtsIndexedExerciseDatabase exerciseFts, boolean relaxed)
    {
        String[] commandWords =
                context.getResources().getStringArray(R.array.exercise_add_command);
        Log.d(TAG, "add with words: " + Arrays.toString(commandWords));

        if (relaxed)
        {
            // match "add" if 3 of the 4 soundex characters match
            // allows it to match add (code: A3OO) with bad (code: B300)
            match = new SoundsLikeThresholdWordMatcher(3, commandWords);
        }
        else
        {
            // match only if the use says "add" exactly
            match = new WordMatcher(commandWords);
        }
        this.context = context;
        this.executor = executor;
        this.exerciseFts = exerciseFts;
    }

	@Override
	public boolean interpret(WordList heard, float[] confidenceScores) {
		boolean understood = false;

        //match first part: "add"
        int matchIndex = match.isInAt(heard.getWords());
        if (matchIndex >= 0)
        {
            //match second part: the food name
            String freeText = heard.getStringAfter(matchIndex);
            if (freeText.length() > 0)
            {
                String exerciseToAdd = freeText;

                // first command
                VoiceActionCommand askForReps =
                        new AskForReps(context, executor, exerciseFts,
                                exerciseToAdd);
                String calPromptFormat =
                        context.getString(R.string.exercise_reps_prompt);
                String calPrompt = String.format(calPromptFormat, exerciseToAdd);

                // second command
                CancelCommand cancel = new CancelCommand(context, executor);

                // match either command, cancel first
                MultiCommandVoiceAction responseAction =
                        new MultiCommandVoiceAction(Arrays.asList(cancel,
                                askForReps));

                // speak and display the same prompt when executing
                responseAction.setPrompt(calPrompt);
                responseAction.setSpokenPrompt(calPrompt);

                // retry if did not understood
                responseAction.setNotUnderstood(new WhyNotUnderstoodListener(
                        context, executor, true));

                understood = true;
                executor.execute(responseAction);
            }
        }

        return understood;
	}

}
