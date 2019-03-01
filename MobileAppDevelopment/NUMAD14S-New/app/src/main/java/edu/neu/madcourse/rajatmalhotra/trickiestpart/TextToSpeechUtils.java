package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.HashMap;

import android.speech.tts.TextToSpeech;

public class TextToSpeechUtils {
	private static final String TAG = "TextToSpeechUtils";
	
	public static HashMap<String, String> EMPTY_PARAMS = new HashMap<String, String>();
	
	static
    {
        EMPTY_PARAMS = makeParamsWith("dummy_id");
    }
	
	public static HashMap<String, String> makeParamsWith(String key)
    {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, key);
        return params;
    }

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}
}
