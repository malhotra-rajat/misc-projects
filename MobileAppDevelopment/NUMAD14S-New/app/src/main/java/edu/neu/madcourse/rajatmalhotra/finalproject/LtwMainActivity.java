package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.TextToSpeechInitializer;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.TextToSpeechStartupListener;

/**
 * LtwMainActivity
 */
public class LtwMainActivity extends Activity implements TextToSpeechStartupListener {

	private static final String TAG = "LtwMainActivity";

	private ExerciseDatabase exerciseDb;

	private TextToSpeech tts;

	Button btnBeginWorkout;
	Button btnViewPrevWorkout;
	Button btnViewAllExercises;
	Button btnTutorials;
	Button btnQuit;
	ProgressBar pbInit;
	TextView tvWelcome;

	private boolean initDone = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ltw_main);
		
		exerciseDb = ExerciseDatabase.getInstance(getApplicationContext());
		
		// Init all UI elements
		btnTutorials = (Button) findViewById(R.id.btnTutorials);
		btnViewAllExercises = (Button) findViewById(R.id.btnViewAllExercises);
		btnBeginWorkout = (Button) findViewById(R.id.btnBeginWorkout);
		btnViewPrevWorkout = (Button) findViewById(R.id.btnViewPrevWorkout);
		btnQuit = (Button) findViewById(R.id.btnLtwExit);
		pbInit = (ProgressBar) findViewById(R.id.pbInitialization);
		tvWelcome = (TextView) findViewById(R.id.tvWelcomeUser);
		
		initDone = false;
		
		new TextToSpeechInitializer(this, Locale.getDefault(), this);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		
		if(false != initDone)
		{			
			if(!exerciseDb.isWorkoutsTableEmpty()) {
				btnViewPrevWorkout.setVisibility(View.VISIBLE);
			}
		}
		
		super.onResume();
	}

	@Override
	public void onSuccessfulInit(TextToSpeech tts) {
		Log.d(TAG, "onSuccessfulInit");
		
		initDone  = true;
		
		this.tts = tts;
		
		tvWelcome.setText(R.string.welcome_user_text);
		pbInit.setVisibility(View.INVISIBLE);
		
		btnTutorials.setVisibility(View.VISIBLE);
		btnBeginWorkout.setVisibility(View.VISIBLE);
		btnViewAllExercises.setVisibility(View.VISIBLE);
		
		/*if(!exerciseDb.isWorkoutsTableEmpty()) {*/
		btnViewPrevWorkout.setVisibility(View.VISIBLE);
		/*}*/
		
		btnQuit.setVisibility(View.VISIBLE);
	}

	@Override
	public void onFailedToInit()
	{
		Log.d(TAG, "onFailedToInit");
		DialogInterface.OnClickListener onClickOk = makeOnFailedToInitHandler();
		AlertDialog a =
				new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Unable to create text to speech")
				.setNeutralButton("Ok", onClickOk).create();
		a.show();
	}

	private DialogInterface.OnClickListener makeOnFailedToInitHandler()
	{
		return new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(tts != null) {

					tts.stop();
					tts.shutdown();
					Log.d(TAG, "TTS Destroyed");
				}
				finish();
			}
		};
	}

	@Override
	public void onRequireLanguageData()
	{
		Log.d(TAG, "onRequireLanguageData");
		DialogInterface.OnClickListener onClickOk =
				makeOnClickInstallDialogListener();
		DialogInterface.OnClickListener onClickCancel =
				makeOnFailedToInitHandler();
		AlertDialog a =
				new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage(
				"Requires Language data to proceed, " +
				"would you like to install?")
				.setPositiveButton("Ok", onClickOk)
				.setNegativeButton("Cancel", onClickCancel).create();
		a.show();
	}
	private DialogInterface.OnClickListener makeOnClickInstallDialogListener()
	{
		return new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				new TextToSpeechInitializer(LtwMainActivity.this, Locale.getDefault(), LtwMainActivity.this).
				installLanguageData();
			}
		};
	}

	@Override
	public void onWaitingForLanguageData()
	{
		Log.d(TAG, "onWaitingForLanguageData");
		// either wait for install
		DialogInterface.OnClickListener onClickWait =
				makeOnFailedToInitHandler();
		DialogInterface.OnClickListener onClickInstall =
				makeOnClickInstallDialogListener();

		AlertDialog a =
				new AlertDialog.Builder(this)
		.setTitle("Info")
		.setMessage(
				"Please wait for the language data to finish" +
				" installing and try again.")
				.setNegativeButton("Wait", onClickWait)
				.setPositiveButton("Retry", onClickInstall).create();
		a.show();
	}

	public void beginWorkoutClick(View view) {

		Intent beginWorkoutIntent = new Intent(this, BeginWorkoutActivity.class);
		//beginWorkoutIntent.putExtra("NOTIFICATION", false);
		beginWorkoutIntent.putExtra("edu.neu.madcourse.rajatmalhotra.finalproject.fromMainScreen", true);
		startActivity(beginWorkoutIntent);
	}


	public void viewPrevWorkoutsClick(View view) {
		if (exerciseDb.isWorkoutsTableEmpty()) 
		{
			Toast.makeText(this, "There are no pevious workouts available.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent i = new Intent(this, ViewPrevDetailsActivity.class);
			startActivity(i);
		}
		
	}
	
	public void handleTutorialsClick(View view) {
		Intent i = new Intent(this, LtwTutorialsActivity.class);
		startActivity(i);
	}
	
	public void handleViewAllExercisesClick(View view) {
		Intent i = new Intent(this, ViewAllExercisesActivity.class);
		startActivity(i);		
	}

	public void quitLtwClick(View view) {
		if(tts != null) {

			tts.stop();
			tts.shutdown();
			Log.d(TAG, "TTS Destroyed");
		}
		finish();
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		
		if(tts != null) {

			tts.stop();
			tts.shutdown();
			Log.d(TAG, "OnDestroy: TTS Destroyed");
		}
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		
		if(tts != null) {
			tts.stop();
			tts.shutdown();
			Log.d(TAG, "OnPause: TTS Destroyed");
		}
		super.onPause();
	}
}
