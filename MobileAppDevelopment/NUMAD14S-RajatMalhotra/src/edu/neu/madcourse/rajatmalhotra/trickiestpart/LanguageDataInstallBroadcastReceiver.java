package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class LanguageDataInstallBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG = "LanguageDataInstallBroadcastReceiver";

    private static final String PREFERENCES_NAME = "installedLanguageData";

    private static final String WAITING_PREFERENCE_NAME =
            "WAITING_PREFERENCE_NAME";

    private static final Boolean WAITING_DEFAULT = false;

    public LanguageDataInstallBroadcastReceiver() {
    	
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
                TextToSpeech.Engine.ACTION_TTS_DATA_INSTALLED))
        {
            Log.d(TAG, "language data preference: " + intent.getAction());
            // clear waiting state
            setWaiting(context, false);
        }
	}
	
	/**
     * check if the receiver is waiting for a language data install
     */
    @SuppressWarnings("deprecation")
	public static boolean isWaiting(Context context)
    {
        SharedPreferences preferences;
        preferences =
                context.getSharedPreferences(PREFERENCES_NAME,
                        Context.MODE_WORLD_READABLE);
        boolean waiting =
                preferences
                        .getBoolean(WAITING_PREFERENCE_NAME, WAITING_DEFAULT);
        return waiting;
    }

    /**
     * start waiting by setting a flag
     */
    @SuppressWarnings("deprecation")
	public static void setWaiting(Context context, boolean waitingStatus)
    {
        SharedPreferences preferences;
        preferences =
                context.getSharedPreferences(PREFERENCES_NAME,
                        Context.MODE_WORLD_WRITEABLE);
        Editor editor = preferences.edit();
        editor.putBoolean(WAITING_PREFERENCE_NAME, waitingStatus);
        editor.commit();
    }
}
