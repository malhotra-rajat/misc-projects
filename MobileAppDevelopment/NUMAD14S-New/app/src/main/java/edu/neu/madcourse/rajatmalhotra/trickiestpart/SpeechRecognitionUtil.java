package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SpeechRecognitionUtil {

	private static final String TAG = "SpeechRecognitionUtil";

	// Note: Google seems to send back these values
	// use at your own risk
	public static final String UNSUPPORTED_GOOGLE_RESULTS_CONFIDENCE = "com.google.android.voicesearch.UNSUPPORTED_PARTIAL_RESULTS_CONFIDENCE";
	public static final String UNSUPPORTED_GOOGLE_RESULTS = "com.google.android.voicesearch.UNSUPPORTED_PARTIAL_RESULTS";

	/**
	 * checks if the device supports speech recognition at all
	 */
	public static boolean isSpeechAvailable(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		boolean available = true;
		if (activities.size() == 0) {
			available = false;
		}
		return available;
	}

	/**
	 * collects language details and returns result to andThen
	 */
	public static void getLanguageDetails(Context context,
			OnLanguageDetailsListener andThen) {
		Intent detailsIntent = new Intent(
				RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
		LanguageDetailsChecker checker = new LanguageDetailsChecker(andThen);
		context.sendOrderedBroadcast(detailsIntent, null, checker, null,
				Activity.RESULT_OK, null, null);
	}

	public static List<String> getHeardFromDirect(Bundle bundle) {
		List<String> results = new ArrayList<String>();
		if ((bundle != null)
				&& bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
			results = bundle
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		}
		return results;
	}

	public static float[] getConfidenceFromDirect(Bundle bundle) {
		float[] scores = null;
		if ((bundle != null)
				&& bundle.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
			scores = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
		}
		return scores;
	}

	public static List<String> getHeardFromDirectPartial(Bundle bundle) {
		List<String> results = new ArrayList<String>();
		if (bundle.containsKey(UNSUPPORTED_GOOGLE_RESULTS)) {
			String[] resultsArray = bundle
					.getStringArray(UNSUPPORTED_GOOGLE_RESULTS);
			results = Arrays.asList(resultsArray);
		} else {
			return getHeardFromDirect(bundle);
		}
		return results;
	}

	public static float[] getConfidenceFromDirectPartial(Bundle bundle) {
		float[] scores = null;

		if (bundle.containsKey(UNSUPPORTED_GOOGLE_RESULTS_CONFIDENCE)) {
			scores = bundle
					.getFloatArray(UNSUPPORTED_GOOGLE_RESULTS_CONFIDENCE);
		} else {
			scores = getConfidenceFromDirect(bundle);
		}

		return scores;
	}

	public static void recognizeSpeechDirectly(Context context,
			Intent recognizerIntent, RecognitionListener listener,
			SpeechRecognizer recognizer) {
		// need to have a calling package for it to work
		if (!recognizerIntent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE)) {
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
					"com.dummy");
		}

		recognizer.setRecognitionListener(listener);
		recognizer.startListening(recognizerIntent);
	}

	public static String diagnoseErrorCode(int errorCode) {
		String message;
		switch (errorCode) {
		case SpeechRecognizer.ERROR_AUDIO:
			message = "Audio recording error";
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			message = "Client side error";
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			message = "Insufficient permissions";
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			message = "Network error";
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			message = "Network timeout";
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			message = "No match";
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			message = "RecognitionService busy";
			break;
		case SpeechRecognizer.ERROR_SERVER:
			message = "error from server";
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = "No speech input";
			break;
		default:
			message = "Didn't understand, please try again.";
			break;
		}
		return message;
	}

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}
}
