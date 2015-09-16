package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import edu.neu.madcourse.rajatmalhotra.R;
import android.content.Context;

public class AskForWeights implements VoiceActionCommand {
	private String exerciseToAdd;
	private Integer repsToAdd;
	private VoiceActionExecutor executor;
    private Context context;   
    private FtsIndexedExerciseDatabase exerciseFts;

	public AskForWeights(Context context, VoiceActionExecutor executor,
			FtsIndexedExerciseDatabase exerciseFts, String exerciseToAdd,
			Integer repsToAdd) {
		this.context = context;
		this.executor = executor;
		this.exerciseToAdd = exerciseToAdd;
		this.repsToAdd = repsToAdd;
		this.exerciseFts = exerciseFts;
	}
	
	private boolean isNumber(String word)
    {
        boolean isNumber = false;
        try
        {
            Integer.parseInt(word);
            isNumber = true;
        } catch (NumberFormatException e)
        {
            isNumber = false;
        }
        return isNumber;
    }

	@Override
	public boolean interpret(WordList heard, float[] confidenceScores) {
		boolean understood = false;
		
		// look for a number within "heard"
        for (String word : heard.getWords()) {
        	if (isNumber(word)) {
        		Integer poundsToAdd = Integer.parseInt(word);
        		
        		// TODO
//        		exerciseFts.insertExercise(exerciseToAdd, repsToAdd, poundsToAdd);
        		
        		String responseFormat =
                        context.getResources().getString(
                                R.string.exercise_add_result);
        		
        		String response =
                        String.format(responseFormat, repsToAdd, exerciseToAdd, poundsToAdd);
        		
        		executor.speak(response);
        		
        		understood = true;
        	}
        }
        
		return understood;
	}

}
