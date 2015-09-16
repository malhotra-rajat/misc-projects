package edu.neu.madcourse.rajatmalhotra.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.rajatmalhotra.R;

public class BeginWorkoutActivity extends Activity {

	/**
	 * The UI elements to change in the activity
	 */
	TextView tvWorkoutSessionWelcome;
	TextView tvListening;
	TextView tvAddHint;
	Button btnPauseRestartSession;
	Button btnEndSession;

	// Timer variables
	private long startTime = 0L;
	private Handler myHandler = new Handler();;
	long timeInMillies = 0L;
	long timeSwap = 0L;
	long finalTime = 0L;

	/**
	 * Flag to indicate if session is paused
	 */
	static boolean isListeningPaused = false;

	/**
	 * The Wait Handler
	 */
	private Handler waitHandler = new Handler();

	/**
	 * The TAG
	 */
	//private static final String TAG = "BeginWorkoutActivity";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	Intent listeningIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin_workout);

		tvWorkoutSessionWelcome = (TextView) findViewById(R.id.tvWorkoutSessionWelcome);
		tvListening = (TextView) findViewById(R.id.tvListening);
		tvAddHint = (TextView) findViewById(R.id.tvAddHint);
		btnPauseRestartSession = (Button) findViewById(R.id.btnPauseRestartSession);
		btnEndSession = (Button) findViewById(R.id.btnEndSession);

		isListeningPaused = true;
		btnPauseRestartSession.setText(R.string.pause_restart_btn_restart_text);

		listeningIntent = new Intent(this, RecordWorkoutsService.class);
		tvAddHint.setVisibility(View.INVISIBLE);
		
		prefs = getSharedPreferences("LTW", MODE_PRIVATE);
		editor = prefs.edit();
	}

	@Override
	public void onNewIntent(Intent intent){
	 
		super.onNewIntent(intent);
	    setIntent(intent);
	}
	
	@Override
	protected void onPause() {
		
		/*SharedPreferences prefs = getSharedPreferences("LTW", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		if (isListeningPaused == false)
		{
			
			editor.putBoolean("isListening", true);
			btnPauseRestartSession.setText(R.string.pause_restart_btn_pause_text);
			isListeningPaused = false;
			tvAddHint.setVisibility(View.VISIBLE);
		}
		else
		{
			editor.putBoolean("isListening", false);
			btnPauseRestartSession.setText(R.string.pause_restart_btn_restart_text);
			tvListening.setText(R.string.stopped_listening_text);
			tvAddHint.setVisibility(View.INVISIBLE);
		}
		editor.commit();*/
		myHandler.removeCallbacks(updateTimerMethod);
		super.onPause();
	}

	@Override
	protected void onResume() {

		//SharedPreferences prefs = getSharedPreferences("LTW", MODE_PRIVATE);
		boolean isListening = prefs.getBoolean("isListening", false);
		
		if (isListening == true)
		{
			isListeningPaused = false;
			btnPauseRestartSession.setText(R.string.pause_restart_btn_pause_text);
			isListeningPaused = false;
			tvAddHint.setVisibility(View.VISIBLE);
		}
		else
		{
			isListeningPaused = true;
			btnPauseRestartSession.setText(R.string.pause_restart_btn_restart_text);
			tvListening.setText(R.string.stopped_listening_text);
			tvAddHint.setVisibility(View.INVISIBLE);
		}
		
		myHandler.postDelayed(updateTimerMethod, 1);
		
		btnEndSession.setEnabled(false);
		waitHandler.postDelayed(new Runnable() {
			public void run() {
				btnEndSession.setEnabled(true);
			}
		}, 2000);
		super.onResume();
	}

	private Runnable updateTimerMethod = new Runnable() {

		@Override
		public void run() {
			timeInMillies = SystemClock.uptimeMillis() - startTime;
			finalTime = timeSwap + timeInMillies;

			int seconds = (int) (finalTime / 1000);
			int minutes = seconds / 60;
			minutes = minutes % 60;
			seconds = seconds % 60;

			if (!isListeningPaused) {
				int nDots = seconds % 3;

				switch (nDots) {
				case 0:
					tvListening.setText("Listening.");
					break;

				case 1:
					tvListening.setText("Listening..");
					break;

				case 2:
					tvListening.setText("Listening...");
					break;

				default:
					break;
				}
			}
			myHandler.postDelayed(this, 100);
		}
	};

	public void handleStartOrPauseClick(View view) {
		// Toggle the flag
		isListeningPaused = !isListeningPaused;

		if (true == isListeningPaused) {
			
			tvListening.setText(R.string.stopped_listening_text);
		
			btnPauseRestartSession.setEnabled(false);
			waitHandler.postDelayed(new Runnable() {
				public void run() {
					btnPauseRestartSession
							.setText(R.string.pause_restart_btn_restart_text);
					btnPauseRestartSession.setEnabled(true);
				}
			}, 2000);

			tvAddHint.setVisibility(View.INVISIBLE);
			stopService(listeningIntent);
			
			editor.putBoolean("isListening", false);
			editor.commit();

		} else {
			// Initialize the start time
			tvWorkoutSessionWelcome
					.setText(R.string.workout_session_in_progress_text);
		
			btnPauseRestartSession.setEnabled(false);
			waitHandler.postDelayed(new Runnable() {
				public void run() {
					btnPauseRestartSession
							.setText(R.string.pause_restart_btn_pause_text);
					btnPauseRestartSession.setEnabled(true);
				}
			}, 2000);

			tvAddHint.setVisibility(View.VISIBLE);
			startService(listeningIntent);
			
			editor.putBoolean("isListening", true);
			editor.commit();
		}
	}

	public void handleAddManuallyClick(View view) {

		Intent addManuallyIntent = new Intent(this,
				SelectManuallyActivity.class);
		
		startActivity(addManuallyIntent);
	}

	public void handleEndSessionClick(View view) {
		
		stopService(listeningIntent);
		editor.putBoolean("isListening", false);
		editor.commit();
		finish();
	}

}
