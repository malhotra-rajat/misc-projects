package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.util.Locale;

import edu.neu.madcourse.rajatmalhotra.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LtwTutorialsActivity extends Activity {

	/**
	 * The Tag for this activity
	 */
	private static final String TAG = "LtwTutorialsActivity";

	/**
	 * The TextToSpeech for this activity
	 */
	private TextToSpeech tts = null;

	/**
	 * The Application Context
	 */
	private Context context;
	
	/**
	 * Ui Elements
	 */
	ProgressBar pbTutorialInit;
	TextView tvLoadingTutorials;
	Button btnAboutApp;
	Button btnHowToUse;
	Button btnStopListening;
	Button btnBack;
	
	/**
	 * The PowerManager
	 */
	PowerManager pm = null;
	
	/**
	 * The WakeLock
	 */
	WakeLock wl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ltw_tutorials);
		
		pm = (PowerManager) getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tutorials TTS Wake Lock");
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		context = this;
		
		tvLoadingTutorials = (TextView) findViewById(R.id.tvLoadingTutorials);
		pbTutorialInit = (ProgressBar) findViewById(R.id.pbTutorialInit);
		btnAboutApp = (Button) findViewById(R.id.btnAboutApp);
		btnHowToUse = (Button) findViewById(R.id.btnHowToUse);
		btnStopListening = (Button) findViewById(R.id.btnStopListening);
		btnBack = (Button) findViewById(R.id.btnBack);

		tts = new TextToSpeech(context, new OnInitListener() {

			@Override
			public void onInit(int status) {
				Log.d(TAG, "TTS Init");
				
				int result = tts.setLanguage(Locale.getDefault());

				if ((result == TextToSpeech.LANG_MISSING_DATA)
						|| (result == TextToSpeech.LANG_NOT_SUPPORTED)) {
					Log.d(TAG,
							"TTS Set Language Result: "
									+ Integer.toString(result));
					// TODO Display the same text instead of tts. New activity to
					// be created
				}
				
				tvLoadingTutorials.setVisibility(View.INVISIBLE);
				pbTutorialInit.setVisibility(View.INVISIBLE);
				btnAboutApp.setVisibility(View.VISIBLE);
				btnHowToUse.setVisibility(View.VISIBLE);
				btnBack.setVisibility(View.VISIBLE);
			}
		});
		
		tts.setSpeechRate(0.90f);
	}

	@Override
	public void onPause() {
		if(wl.isHeld()) {
			wl.release();
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		
		if(wl.isHeld()) {
			wl.release();
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		wl.acquire();
		
		super.onResume();
	}	

	public void handleAboutAppClick(View view) {
		// TODO
		Log.d(TAG, "Speaking about the app");

		String toSpeak = context.getResources().getString(
				R.string.about_app_tts_text);
		
		tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
		
		
		btnStopListening.setVisibility(View.VISIBLE);
	}

	public void handleHowToUseClick(View view) {
		// TODO
		Log.d(TAG, "Speaking how to use the app");

		String toSpeak = context.getResources().getString(
				R.string.about_usage_tts_text);
		
		tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
		
		btnStopListening.setVisibility(View.VISIBLE);
	}
	
	public void handleStopListeningClick(View view) {
		if (tts != null) {
			tts.stop();
		}
		btnStopListening.setVisibility(View.INVISIBLE);
	}

	public void handleBackClick(View view) {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		
		if(wl.isHeld()) {
			wl.release();
		}

		finish();
	}
}
