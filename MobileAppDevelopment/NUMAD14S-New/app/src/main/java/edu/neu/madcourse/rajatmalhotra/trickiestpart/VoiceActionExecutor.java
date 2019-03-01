package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

@SuppressWarnings("deprecation")
public class VoiceActionExecutor {
	private static final String TAG = "VoiceActionExecutor";

	private VoiceAction active;

	private SpeechRecognizingActivity speech;

	/**
	 * parameter for TTS to identify utterance
	 */
	private final String EXECUTE_AFTER_SPEAK = "EXECUTE_AFTER_SPEAK";
	
	private TextToSpeech tts;

	public VoiceActionExecutor(SpeechRecognizingActivity speech) {
		this.speech = speech;
		active = null;
	}

	/**
	 * set the TTS when it is ready to complete initialization
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setTts(TextToSpeech tts) {
		
		this.tts = tts;
		
		if (Build.VERSION.SDK_INT >= 15) {
			try {
				tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
					
					@Override
					public void onStart(String utteranceId) {
						
					}
					
					@Override
					public void onError(String utteranceId) {
						
					}
					
					@Override
					public void onDone(String utteranceId) {
						onDoneSpeaking(utteranceId);					
					}
				});
			} catch (NullPointerException e) {
				Log.d(TAG, e.toString());
			}
		} else {
			tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
				@Override
				public void onUtteranceCompleted(String utteranceId) {
					onDoneSpeaking(utteranceId);
				}
			});
		}
	}

	/**
	 * external handleReceiveWhatWasHeard must call this
	 */
	public void handleReceiveWhatWasHeard(List<String> heard,
			float[] confidenceScores) {
		active.interpret(heard, confidenceScores);
	}

	private void onDoneSpeaking(String utteranceId) {
		if (utteranceId.equals(EXECUTE_AFTER_SPEAK)) {
			doRecognitionOnActive();
		}
	}

	/**
	 * convenient way to just reply with something spoken
	 */
	public void speak(String toSay) {
		tts.speak(toSay, TextToSpeech.QUEUE_FLUSH, null);
	}

	/**
	 * execute the current active {@link VoiceAction} again speaking extraPrompt
	 * before
	 */
	public void reExecute(String extraPrompt) {
		if ((extraPrompt != null) && (extraPrompt.length() > 0)) {
			tts.speak(extraPrompt, TextToSpeech.QUEUE_FLUSH,
					TextToSpeechUtils.makeParamsWith(EXECUTE_AFTER_SPEAK));
		} else {
			execute(getActive());
		}
	}

	/**
	 * change the current voice action to this and then execute it, optionally
	 * saying a prompt first
	 */
	public void execute(VoiceAction voiceAction) {
		if (tts == null) {
			throw new RuntimeException("Text to speech not initialized");
		}
		
		setActive(voiceAction);
		
		if (voiceAction.hasSpokenPrompt()) {
			tts.speak(voiceAction.getSpokenPrompt(), TextToSpeech.QUEUE_FLUSH,
					TextToSpeechUtils.makeParamsWith(EXECUTE_AFTER_SPEAK));
		} else {
			doRecognitionOnActive();
		}
	}

	private void doRecognitionOnActive() {
		Intent recognizerIntent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getActive()
				.getPrompt());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 500);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2000);
		
		speech.recognize(recognizerIntent);
	}

	private VoiceAction getActive() {
		return active;
	}

	private void setActive(VoiceAction active) {
		this.active = active;
	}

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}
}
