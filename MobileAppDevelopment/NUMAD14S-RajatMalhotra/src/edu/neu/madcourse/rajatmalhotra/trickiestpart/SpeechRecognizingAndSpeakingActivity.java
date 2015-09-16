package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

@SuppressLint("NewApi")
public abstract class SpeechRecognizingAndSpeakingActivity extends
		SpeechRecognizingActivity implements TextToSpeechStartupListener {
	
	private static final String TAG = "SpeechRecognizingAndSpeakingActivity";

	private TextToSpeechInitializer ttsInit;

	private TextToSpeech tts;

	/**
	 * @see root.gast.speech.SpeechRecognizingActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		deactivateUi();
		ttsInit = new TextToSpeechInitializer(this, Locale.getDefault(), this);
	}

	@Override
	public void onSuccessfulInit(TextToSpeech tts) {
		Log.d(TAG, "successful init");
		this.tts = tts;
		activateUi();
		setTtsListener();
	}

	private void setTtsListener() {
		final SpeechRecognizingAndSpeakingActivity callWithResult = this;
		if (Build.VERSION.SDK_INT >= 15) {
			int listenerResult = tts
					.setOnUtteranceProgressListener(new UtteranceProgressListener() {
						@Override
						public void onDone(String utteranceId) {
							callWithResult.onDone(utteranceId);
						}

						@Override
						public void onError(String utteranceId) {
							callWithResult.onError(utteranceId);
						}

						@Override
						public void onStart(String utteranceId) {
							callWithResult.onStart(utteranceId);
						}
					});
			if (listenerResult != TextToSpeech.SUCCESS) {
				Log.e(TAG, "failed to add utterance progress listener");
			}
		} else {
			int listenerResult = tts
					.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
						@Override
						public void onUtteranceCompleted(String utteranceId) {
							callWithResult.onDone(utteranceId);
						}
					});
			if (listenerResult != TextToSpeech.SUCCESS) {
				Log.e(TAG, "failed to add utterance completed listener");
			}
		}
	}

	// sub class may override these, otherwise, one or the other
	// will occur depending on the Android version
	public void onDone(String utteranceId) {
	}

	public void onError(String utteranceId) {
	}

	public void onStart(String utteranceId) {
	}

	@Override
	public void onFailedToInit() {
		DialogInterface.OnClickListener onClickOk = makeOnFailedToInitHandler();
		AlertDialog a = new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Unable to create text to speech")
				.setNeutralButton("Ok", onClickOk).create();
		a.show();
	}

	@Override
	public void onRequireLanguageData() {
		DialogInterface.OnClickListener onClickOk = makeOnClickInstallDialogListener();
		DialogInterface.OnClickListener onClickCancel = makeOnFailedToInitHandler();
		AlertDialog a = new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(
						"Requires Language data to proceed, would you like to install?")
				.setPositiveButton("Ok", onClickOk)
				.setNegativeButton("Cancel", onClickCancel).create();
		a.show();
	}

	@Override
	public void onWaitingForLanguageData() {
		// either wait for install
		DialogInterface.OnClickListener onClickWait = makeOnFailedToInitHandler();
		DialogInterface.OnClickListener onClickInstall = makeOnClickInstallDialogListener();

		AlertDialog a = new AlertDialog.Builder(this)
				.setTitle("Info")
				.setMessage(
						"Please wait for the language data to finish installing and try again.")
				.setNegativeButton("Wait", onClickWait)
				.setPositiveButton("Retry", onClickInstall).create();
		a.show();
	}

	private DialogInterface.OnClickListener makeOnClickInstallDialogListener() {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ttsInit.installLanguageData();
			}
		};
	}

	private DialogInterface.OnClickListener makeOnFailedToInitHandler() {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};
	}

	// override in subclass

	protected void deactivateUi() {
		Log.d(TAG, "deactivate ui");
	}

	protected void activateUi() {
		Log.d(TAG, "activate ui");
	}

	@Override
	protected void speechNotAvailable() {
		DialogInterface.OnClickListener onClickOk = makeOnFailedToInitHandler();
		AlertDialog a = new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(
						"This device does not support speech recognition. Click ok to quit.")
				.setPositiveButton("Ok", onClickOk).create();
		a.show();
	}

	@Override
	protected void directSpeechNotAvailable() {
		// do nothing
	}

	protected void languageCheckResult(String languageToUse) {
		// not used
	}

	protected void recognitionFailure(int errorCode) {
		String message = SpeechRecognitionUtil.diagnoseErrorCode(errorCode);
		Log.d(TAG, "speech error: " + message);
	}

	protected TextToSpeech getTts() {
		return tts;
	}

	@Override
	protected void onDestroy() {
		if (getTts() != null) {
			getTts().shutdown();
		}
		super.onDestroy();
	}
}
