package edu.neu.madcourse.rajatmalhotra.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;
import edu.neu.madcourse.rajatmalhotra.R;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.SpeechRecognitionUtil;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.TextToSpeechInitializer;
import edu.neu.madcourse.rajatmalhotra.trickiestpart.TextToSpeechStartupListener;

public class RecordWorkoutsService extends Service implements
		RecognitionListener, TextToSpeechStartupListener {

	// AudioManager mAudioManager;

	private static final String TAG = "SpeechActivationService";
	//public static final String NOTIFICATION_ICON_RESOURCE_INTENT_KEY = "NOTIFICATION_ICON_RESOURCE_INTENT_KEY";

	public static final int NOTIFICATION_ID = 10300;

	private boolean isStarted;

	boolean isSpeechRecognizerAlive;
	boolean inTTS;

	private static final String UTT_ID = "dummy_id";

	private Context context;
	private SpeechRecognizer recognizer;
	private TextToSpeech tts;

	boolean triggerSaid = false;
	boolean exerciseSaid = false;
	boolean repsSaid = false;
	boolean weightSaid = false;
	boolean cancelSaid = false;
	boolean recordSaid = false;

	boolean add_prompted = false;
	boolean exercise_prompted = false;
	boolean reps_prompted = false;
	boolean pounds_prompted = false;

	boolean cancel_prompted = false;
	boolean record_prompted = false;
	boolean recorded_prompted = false;

	boolean ttsSrStopped = false;
	boolean ttsSrStarted = false;

	boolean increasedBool = false;
	int increased;

	String exercise;
	int reps;
	int weights;

	boolean partialResultsCalled = false;
	
	List<String> commandList = new ArrayList<String>();
	List<String> commandListExercise = new ArrayList<String>();

	SoundsLikeWordMatcher swmCl;
	SoundsLikeWordMatcher swmExercise;

	private Handler waitHandler = new Handler();

	

	/**
	 * The Exercise Database Instance
	 */
	ExerciseDatabase exerciseDb;

	@Override
	public void onCreate() {
		super.onCreate();
		isStarted = false;
		this.context = getApplicationContext();

		// Adding all exercises in commandListExercise ArrayList
		try {
			populateExercises();
		} catch (IOException e) {
			Log.d(TAG, "IOException in populating exercises");
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Init DB
		exerciseDb = ExerciseDatabase.getInstance(this);

		this.context = getApplicationContext();
		if (intent != null) {
			if (!isStarted) {
				startRecording(intent);
			}
		}

		// restart in case the Service gets canceled
		return START_REDELIVER_INTENT;
	}

	private void startRecording(Intent intent) {
		
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		 * AudioManagerUtil.am.setStreamMute(AudioManager.STREAM_MUSIC, false);
		 * }
		 */
		triggerSaid = false;
		exerciseSaid = false;
		repsSaid = false;
		weightSaid = false;
		cancelSaid = false;
		recordSaid = false;

		add_prompted = false;
		exercise_prompted = false;
		reps_prompted = false;
		pounds_prompted = false;

		cancel_prompted = false;
		record_prompted = false;
		recorded_prompted = false;

		commandList.add("I just did");
		commandList.add("Cancel");
		commandList.add("Save");

		swmCl = new SoundsLikeWordMatcher(commandList);

		swmExercise = new SoundsLikeWordMatcher(commandListExercise);

		useTtsHandler();
		useSRHandler();
		isStarted = true;

		new TextToSpeechInitializer(context, Locale.getDefault(), this);

		waitHandler.postDelayed(new Runnable() {
			public void run() {
				/*
				 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				 * {
				 * AudioManagerUtil.am.setStreamMute(AudioManager.STREAM_MUSIC,
				 * false); }
				 */
				recognizeSpeechDirectly();

			}
		}, 2000);

		startForeground(NOTIFICATION_ID, getNotification());
	}

	private void populateExercises() throws IOException {
		InputStream stream = getResources().openRawResource(R.raw.exercises);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream,
				"UTF8"));
		String line;

		line = br.readLine();

		while (line != null) {
			commandListExercise.add(line);
			line = br.readLine();
		}
	}

	@Override
	public void onDestroy() {

		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		 * AudioManagerUtil.am.setStreamMute(AudioManager.STREAM_MUSIC, false);
		 * }
		 */

		if (tts != null) {
			tts.stop();
			tts.shutdown();
			Log.d(TAG, "TTS Destroyed");
		}

		ttsHandler.removeCallbacks(ttsRunnable);
		srHandler.removeCallbacks(srRunnable);

		stop();

		isStarted = false;
		stopSelf();
		stopForeground(true);

		Log.d(TAG, "On destroy");
		super.onDestroy();

	}


	@SuppressLint("NewApi")
	private Notification getNotification() {

		String message = getString(R.string.listening_workout);
		String title = getString(R.string.title_activity_ltw_main);
		int icon = R.drawable.voice_input;

		
		Intent notificationIntent;
		notificationIntent = new Intent(this, BeginWorkoutActivity.class);
		
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Going to BeginWorkoutActivity by clicking on the notification

		

		Notification notification;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Notification.Builder builder = new Notification.Builder(this);
			 builder.setSmallIcon(icon)
			 		.setWhen(System.currentTimeMillis())
					.setTicker(message)
					.setContentTitle(title)
					.setContentText(message)
					.setContentIntent(pi)
					.setOngoing(true);
			
			notification = builder.build();
			notification.flags = Notification.FLAG_ONGOING_EVENT;

		} else {
			notification = new Notification(icon, message,
					System.currentTimeMillis());
			notification.setLatestEventInfo(this, title, message, pi);
		}

		return notification;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * set the TTS listener to call {@link #onDone(String)} depending on the
	 * Build.Version.SDK_INT
	 */
	@SuppressLint("NewApi")
	private void setTtsListener() {
		if (Build.VERSION.SDK_INT >= 15) {
			int listenerResult = tts
					.setOnUtteranceProgressListener(new UtteranceProgressListener() {
						@Override
						public void onDone(String utteranceId) {
							inTTS = false;
							Log.d(TAG, "TTS end");
						}

						@Override
						public void onError(String utteranceId) {
							Log.e(TAG, "TTS error");
							// Toast.makeText(context, "TTS error",
							// Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onStart(String utteranceId) {
							inTTS = true;
							Log.d(TAG, "TTS start");

						}
					});
			if (listenerResult != TextToSpeech.SUCCESS) {
				Log.e(TAG, "failed to add utterance progress listener");
			}
		}

	}

	private void recognizeSpeechDirectly() {

		Intent recognizerIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		// accept partial results if they come
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		SpeechRecognitionUtil.recognizeSpeechDirectly(context,
				recognizerIntent, this, getSpeechRecognizer());

	}

	public void stop() {
		if (getSpeechRecognizer() != null) {
			getSpeechRecognizer().stopListening();
			getSpeechRecognizer().cancel();
			getSpeechRecognizer().destroy();
		}
	}

	@Override
	public void onResults(Bundle results) {
		Log.d(TAG, "full results");
		receiveResults(results);
		// onResultsCalled = true;
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		Log.d(TAG, "partial results");
		partialResultsCalled = true;
		receiveResults(partialResults);
	}

	/**
	 * common method to process any results bundle from {@link SpeechRecognizer}
	 */
	private void receiveResults(Bundle results) {
		if ((results != null)
				&& results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
			List<String> heard = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
/*			float[] scores = results
					.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
*/
			for (int i = 0; i < heard.size(); i++) {
				Log.d("TESTING: SPEECH SERVICE", (String) (heard.get(i)));
			}
			for (int i = 0; i < heard.size(); i++) {
				try {
					if (commandList.get(
							swmCl.isInAt(SoundsLikeWordMatcher.encode(heard
									.get(i)))).equals("Cancel")) {
						cancelSaid = true;

						triggerSaid = false;
						exerciseSaid = false;
						repsSaid = false;
						weightSaid = false;

						return;
					}
				} catch (Exception e) {
					Log.d(TAG, e.toString());
				}
			}

			if (triggerSaid == true && exerciseSaid == true && repsSaid == true
					&& weightSaid == true) {
				for (int i = 0; i < heard.size(); i++) {
					try {
						if (swmCl.isInAt(SoundsLikeWordMatcher.encode(heard
								.get(i))) != -1) {
							if (commandList.get(
									swmCl.isInAt(SoundsLikeWordMatcher
											.encode(heard.get(i)))).equals(
									"Save")) {
								recordSaid = true;
								break;
							}
						}

					} catch (Exception e) {
						Log.d(TAG, e.toString());
					}
				}
			}

			else if (triggerSaid == true && exerciseSaid == true
					&& repsSaid == true && weightSaid == false) {
				try {
					if (partialResultsCalled == false) {
						weights = Integer.parseInt(heard.get(0));
						weightSaid = true;
					}
				} catch (NumberFormatException nfe) {
					// Toast.makeText(context,
					// "Please speak a number for weight",
					// Toast.LENGTH_SHORT).show();
				}
			} else if (triggerSaid == true && exerciseSaid == true
					&& repsSaid == false && weightSaid == false) {
				try {
					if (partialResultsCalled == false) {
						reps = Integer.parseInt(heard.get(0));
						repsSaid = true;
					}

				} catch (NumberFormatException nfe) {
					// Toast.makeText(getApplicationContext(),
					// "Please speak a number for reps",
					// Toast.LENGTH_SHORT).show();
				}
			} else if (triggerSaid == true && exerciseSaid == false
					&& repsSaid == false && weightSaid == false) {

				for (int i = 0; i < heard.size(); i++) {
					try {
						if (swmExercise.isInAt(SoundsLikeWordMatcher
								.encode(heard.get(i))) != -1) {
							{
								exerciseSaid = true;
								exercise = commandListExercise.get(swmExercise
										.isInAt(SoundsLikeWordMatcher
												.encode(heard.get(i))));
								break;
							}
						}
					} catch (Exception e) {
						Log.d(TAG, e.toString());
					}
				}
			}

			else if (triggerSaid == false && exerciseSaid == false
					&& repsSaid == false && weightSaid == false) {
				for (int i = 0; i < heard.size(); i++) {
					try {
						if (swmCl.isInAt(SoundsLikeWordMatcher.encode(heard
								.get(i))) != -1) {
							if (commandList.get(
									swmCl.isInAt(SoundsLikeWordMatcher
											.encode(heard.get(i)))).equals(
									"I just did")) {
								triggerSaid = true;
								break;
							}
						}
					} catch (Exception e) {
						Log.d(TAG, e.toString());
					}
				}
			}
		} else {
			Log.d(TAG, "no results");
		}

		recognizeSpeechDirectly();
		partialResultsCalled = false;
		// onResultsCalled = false;
	}

	@Override
	public void onError(int errorCode) {
		if ((errorCode == SpeechRecognizer.ERROR_NO_MATCH)
				|| (errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) {
			Log.d(TAG, "didn't recognize anything");
			// keep going
			recognizeSpeechDirectly();
		} else if ((errorCode == SpeechRecognizer.ERROR_RECOGNIZER_BUSY)) {
			isSpeechRecognizerAlive = false;
			Log.d(TAG, "ERROR_RECOGNIZER_BUSY");

		} else {
			Log.d(TAG,
					"FAILED "
							+ SpeechRecognitionUtil
									.diagnoseErrorCode(errorCode));

			isSpeechRecognizerAlive = false;
		}
	}

	/**
	 * lazy initialize the speech recognizer
	 */
	private SpeechRecognizer getSpeechRecognizer() {
		if (recognizer == null) {
			recognizer = SpeechRecognizer.createSpeechRecognizer(context);
		}
		return recognizer;
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		 * AudioManagerUtil.am.setStreamMute(AudioManager.STREAM_MUSIC, true); }
		 */

		isSpeechRecognizerAlive = true;
		Log.d(TAG, "ready for speech " + params);
	}

	@Override
	public void onEndOfSpeech() {
	}

	/**
	 * @see android.speech.RecognitionListener#onBeginningOfSpeech()
	 */
	@Override
	public void onBeginningOfSpeech() {
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	@Override
	public void onRmsChanged(float rmsdB) {
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
	}

	private void ttsCheck() {
		if (tts != null) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTT_ID);

			if (recordSaid == true && triggerSaid == true
					&& exerciseSaid == true && repsSaid == true
					&& weightSaid == true) {
				if (recorded_prompted == false) {

					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
					String todayDate = sdf.format(date);

					int x = exerciseDb.increaseInWeightsForExercise(exercise,
							weights);
					if (x != -1) {
						increased = x;
						increasedBool = true;
					}
					if (-1 != exerciseDb.insertExercise(todayDate, exercise,
							reps, weights)) {
						// Toast.makeText(getApplicationContext(),
						// "Successfully inserted.",
						// Toast.LENGTH_SHORT).show();

					} else {
						tts.speak(
								"Sorry. Could not save exercise. Speak 'I Just Did'",
								TextToSpeech.QUEUE_FLUSH, params);
					}
					if (increasedBool == true) {

						tts.speak(
								"Saved. This was your highest weight lifted for "
										+ exercise
										+ ". You lifted "
										+ increased
										+ " pounds more than your previous best. Speak I Just Did.",
								TextToSpeech.QUEUE_FLUSH, params);
						increasedBool = false;
					} else {
						tts.speak("Saved. Speak I Just Did",
								TextToSpeech.QUEUE_FLUSH, params);
					}

					recordSaid = false;

					triggerSaid = false;
					exerciseSaid = false;
					repsSaid = false;
					weightSaid = false;

					add_prompted = true; // *

					exercise_prompted = false;
					reps_prompted = false;
					pounds_prompted = false;
					record_prompted = false;
					recorded_prompted = false;

				}
			}

			if (cancelSaid == true) {
				if (cancel_prompted == false) {
					tts.speak("Cancelled. Speak I Just Did",
							TextToSpeech.QUEUE_FLUSH, params);
					cancelSaid = false;

					triggerSaid = false;
					exerciseSaid = false;
					repsSaid = false;
					weightSaid = false;

					add_prompted = true; // *

					exercise_prompted = false;
					reps_prompted = false;
					pounds_prompted = false;
					record_prompted = false;
					recorded_prompted = false;
				}
			}

			if (triggerSaid == false && exerciseSaid == false
					&& repsSaid == false && weightSaid == false) {
				if (add_prompted == false) {
					tts.speak("Speak I Just Did.", TextToSpeech.QUEUE_FLUSH,
							params);
					add_prompted = true;
				}
			} else if (triggerSaid == true && exerciseSaid == false
					&& repsSaid == false && weightSaid == false) {
				if (exercise_prompted == false) {
					tts.speak("Exercise Name?", TextToSpeech.QUEUE_FLUSH,
							params);
					exercise_prompted = true;
				}
			}

			else if (triggerSaid == true && exerciseSaid == true
					&& repsSaid == false && weightSaid == false) {
				if (reps_prompted == false) {
					tts.speak(exercise + ". Reps?", TextToSpeech.QUEUE_FLUSH,
							params);
					reps_prompted = true;
				}
			}

			else if (triggerSaid == true && exerciseSaid == true
					&& repsSaid == true && weightSaid == false) {
				if (pounds_prompted == false) {
					tts.speak(reps + " reps. Pounds?",
							TextToSpeech.QUEUE_FLUSH, params);
					pounds_prompted = true;
				}

			} else if (triggerSaid == true && exerciseSaid == true
					&& repsSaid == true && weightSaid == true) {
				if (record_prompted == false) {
					tts.speak(weights + " pounds. Save or Cancel?",
							TextToSpeech.QUEUE_FLUSH, params);
					record_prompted = true;
				}
			} else {
			}
		} else {
			// Toast.makeText(context, "Initializing TTS",
			// Toast.LENGTH_SHORT).show();
		}
	}

	public Handler ttsHandler;

	public void useTtsHandler() {
		ttsHandler = new Handler();
		ttsHandler.postDelayed(ttsRunnable, 10);
	}

	public Runnable ttsRunnable = new Runnable() {
		@Override
		public void run() {
			// Log.e(TAG, "tts_play_check");
			if (inTTS == false) {
				ttsCheck();
				if (ttsSrStarted == false) {
					recognizeSpeechDirectly();
					Log.d(TAG, "Stopped TTS - Speech Recognition started");
					ttsSrStarted = true;
					// ttsSrStopped = false;
				}
			}
			if (inTTS == true) {
				/*
				 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				 * {
				 * AudioManagerUtil.am.setStreamMute(AudioManager.STREAM_MUSIC,
				 * false); }
				 */

				stop();
				Log.d(TAG, "Playing TTS - Speech Recognition stopped");
				ttsSrStarted = false;

			}

			/*
			 * Log.d(TAG, "addSaid = " + addSaid); Log.d(TAG, "exSaid = " +
			 * exerciseSaid); Log.d(TAG, "repSaid = " + repsSaid); Log.d(TAG,
			 * "poundsSaid = " + weightSaid);
			 */

			ttsHandler.postDelayed(ttsRunnable, 10);
		}
	};

	public Handler srHandler;

	public void useSRHandler() {
		srHandler = new Handler();
		srHandler.postDelayed(srRunnable, 2500);
	}

	public Runnable srRunnable = new Runnable() {

		@Override
		public void run() {

			if (tts == null) {
				Toast.makeText(context, "Initializing", Toast.LENGTH_SHORT)
						.show();
			}

			if (isSpeechRecognizerAlive == false) {
				Log.e(TAG, "Speech Recognizer not alive.. starting again");
				stop();
				recognizeSpeechDirectly();
			}
			srHandler.postDelayed(srRunnable, 4000);
		}
	};

	@Override
	public void onSuccessfulInit(TextToSpeech tts) {
		this.tts = tts;
		setTtsListener();
		// Toast.makeText(context, "TTS Initialized",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRequireLanguageData() {
		// Toast.makeText(context, "TTS requires Language Data!",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onWaitingForLanguageData() {
		// Toast.makeText(context, "Waiting for Language Data!",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFailedToInit() {
		Toast.makeText(
				context,
				"Failed to initialize listening service. Please use Add Exercise Manually Button",
				Toast.LENGTH_LONG).show();
	}

}