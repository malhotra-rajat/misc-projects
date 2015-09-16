package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.Arrays;

import edu.neu.madcourse.rajatmalhotra.R;
import android.content.Context;

public class AskForReps implements VoiceActionCommand {

	private String exerciseToAdd;
	private FtsIndexedExerciseDatabase exerciseFts;
	private VoiceActionExecutor executor;
	private Context context;

	public AskForReps(Context context, VoiceActionExecutor executor,
			FtsIndexedExerciseDatabase exerciseFts, String exerciseToAdd) {
		this.context = context;
		this.executor = executor;
		this.exerciseFts = exerciseFts;
		this.exerciseToAdd = exerciseToAdd;
	}

	private boolean isNumber(String word) {
		boolean isNumber = false;
		try {
			Integer.parseInt(word);
			isNumber = true;
		} catch (NumberFormatException e) {
			isNumber = false;
		}
		return isNumber;
	}

	@Override
	public boolean interpret(WordList heard, float[] confidenceScores) {
		boolean understood = false;

		for (String word : heard.getWords()) {
			if (isNumber(word)) {
				// The number of reps to add to the database.
				// This information will be inserted to the database once the
				// weights information has been obtained from the user in the
				// next VoiceActionCommand.
				Integer repsToAdd = Integer.parseInt(word);

				// First command. Ask for weights
				VoiceActionCommand askForWeights = new AskForWeights(context,
						executor, exerciseFts, exerciseToAdd, repsToAdd);
				String weightsPromptFormat = context
						.getString(R.string.exercise_weights_prompt);
				String weightsPrompt = String.format(weightsPromptFormat,
						repsToAdd);

				// second command
				CancelCommand cancel = new CancelCommand(context, executor);

				// match either command, cancel first
				MultiCommandVoiceAction responseAction = new MultiCommandVoiceAction(
						Arrays.asList(cancel, askForWeights));

				// speak and display the same prompt when executing
				responseAction.setPrompt(weightsPrompt);
				responseAction.setSpokenPrompt(weightsPrompt);

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
