package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.rajatmalhotra.R;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MultiTurnExerciseDialogActivity extends
		SpeechRecognizingAndSpeakingActivity {

	private static final String TAG = "MultiTurnExerciseDialogActivity";

	private FtsIndexedExerciseDatabase exerciseDb;

	private TextView log;

	private VoiceActionExecutor executor;

	// private VoiceAction lookupVoiceAction;
	private VoiceAction addExerciseAction;
	
	Button btnBeginWorkout;
	Button btnAddManually;
	TextView tvLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_dialog_multi);
		
		btnBeginWorkout = (Button) findViewById(R.id.btnBeginWorkout);
		btnAddManually = (Button) findViewById(R.id.btnAddManually);
		tvLoading = (TextView) findViewById(R.id.tvLoading);

		initDbs();
		initDialog();
	}

	private void initDialog() {
		if (executor == null) {
			executor = new VoiceActionExecutor(this);
		}

		addExerciseAction = makeAddExercise();
	}

	private VoiceAction makeAddExercise() {
		// match it with two levels of strictness
		boolean relaxed = false;

		FtsIndexedExerciseDatabase exerciseDb = FtsIndexedExerciseDatabase
				.getInstance(MultiTurnExerciseDialogActivity.this);

		VoiceActionCommand cancelCommand = new CancelCommand(this, executor);

		VoiceActionCommand addCommand = new AddExercise(this, executor,
				exerciseDb, relaxed);

		relaxed = true;

		VoiceActionCommand addCommandRelaxed = new AddExercise(this, executor,
				exerciseDb, relaxed);

		VoiceAction voiceAction = new MultiCommandVoiceAction(Arrays.asList(
				cancelCommand, addCommand, addCommandRelaxed));

		// don't retry
		voiceAction.setNotUnderstood(new WhyNotUnderstoodListener(this,
				executor, false));

		final String EDIT_PROMPT = getResources().getString(
				R.string.exercise_edit_prompt);
		
		// no spoken prompt
		voiceAction.setPrompt(EDIT_PROMPT);

		return voiceAction;
	}

	private void initDbs() {
		exerciseDb = FtsIndexedExerciseDatabase.getInstance(this);

		if (exerciseDb.isEmpty()) {
			Log.d(TAG, "loading exercises");

			InputStream stream = getResources()
					.openRawResource(R.raw.exercises);

			try {
				exerciseDb.loadFrom(stream);
			} catch (IOException io) {
				Log.d(TAG, "failed to load db");
			}
		}
	}

	@Override
	public void onSuccessfulInit(TextToSpeech tts) {
		Log.d(TAG, "Successful Init");
		Log.d(TAG, tts.toString());
		super.onSuccessfulInit(tts);
		Log.d(TAG, "activate ui, set tts");
		executor.setTts(getTts());
		
		btnBeginWorkout.setEnabled(true);
		btnAddManually.setEnabled(true);
		tvLoading.setEnabled(false);
		tvLoading.setVisibility(View.INVISIBLE);
	}

	protected void receiveWhatWasHeard(List<String> heard,
			float[] confidenceScores) {
		Log.d(TAG, "received " + heard.size());
		//TODO
//		clearLog();
//		for (int i = 0; i < heard.size(); i++) {
//			appendToLog(heard.get(i) + " " + confidenceScores[i]);
//		}
		executor.handleReceiveWhatWasHeard(heard, confidenceScores);
	}

	private void clearLog() {
		log.setText("");
	}

	private void appendToLog(String appendThis) {
		String currentLog = log.getText().toString();
		currentLog = currentLog + "\n" + appendThis;
		log.setText(currentLog);
	}

	protected void recognitionFailure() {
		// don't do anything here
		Log.d(TAG, "cancelled with button");
	}

	public void beginWorkout(View view) {
//		executor.setTts(getTts());
		executor.execute(addExerciseAction);
	}
	
	public void addManually(View view) {
		Intent i = new Intent(this, ManualInput.class);
		startActivity(i);
	}

	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (item.getItemId() == R.id.m_resetdb_param) {
	// final ProgressDialog dialog = ProgressDialog.show(this,
	// "Please wait", "Updating Food Database");
	// dialog.setIndeterminate(true);
	// Runnable updateDbRunnable = new Runnable() {
	// @Override
	// public void run() {
	// FtsIndexedExerciseDatabase.getInstance(
	// MultiTurnExerciseDialogActivity.this).clean(
	// MultiTurnExerciseDialogActivity.this);
	// // now load
	// initDbs();
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// Toast.makeText(MultiTurnExerciseDialogActivity.this,
	// "Reset database", Toast.LENGTH_SHORT)
	// .show();
	// dialog.hide();
	// }
	// });
	// }
	// };
	// Thread updateDbThread = new Thread(updateDbRunnable);
	// updateDbThread.start();
	// } else if (item.getItemId() == R.id.m_showdb_param) {
	// Intent browseIntent = new Intent(this, FoodBrowser.class);
	// startActivity(browseIntent);
	// }
	// return true;
	// }

}
