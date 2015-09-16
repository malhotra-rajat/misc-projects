package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

public class LanguageDetailsChecker extends BroadcastReceiver {

	private static final String TAG = "LanguageDetailsChecker";
	private List<String> supportedLanguages;
	private String languagePreference;
	private OnLanguageDetailsListener doAfterReceive;

	public LanguageDetailsChecker(OnLanguageDetailsListener doAfterReceive) {
		supportedLanguages = new ArrayList<String>();
		this.doAfterReceive = doAfterReceive;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle results = getResultExtras(true);
		if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
			languagePreference = results
					.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
		}
		if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
			supportedLanguages = results
					.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
		}
		if (doAfterReceive != null) {
			doAfterReceive.onLanguageDetailsReceived(this);
		}
	}

	public String matchLanguage(Locale toCheck) {
		String matchedLanguage = null;
		// modify the returned languages to look like the output from
		// Locale.toString()
		String targetLanguage = toCheck.toString().replace('_', '-');
		for (String supportedLanguage : supportedLanguages) {
			// use contains, so that partial matches are possible
			// for example, if the Locale is
			// en-US-POSIX, it will still match en-US
			// and that if the target language is en, it will match something
			Log.d(TAG, targetLanguage + " contains " + supportedLanguage);
			if ((targetLanguage.contains(supportedLanguage))
					|| supportedLanguage.contains(targetLanguage)) {
				matchedLanguage = supportedLanguage;
			}
		}
		return matchedLanguage;
	}

	/**
	 * @return the supportedLanguages
	 */
	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * @return the languagePreference
	 */
	public String getLanguagePreference() {
		return languagePreference;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Language Preference: ").append(getLanguagePreference())
				.append("\n");
		sb.append("languages supported: ").append("\n");
		for (String lang : getSupportedLanguages()) {
			sb.append(" ").append(lang).append("\n");
		}
		return sb.toString();
	}
}
