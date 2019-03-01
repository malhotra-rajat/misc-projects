package edu.neu.madcourse.rajatmalhotra.trickiestpart;

public class MatchAnythingCommand implements VoiceActionCommand {

	private static final String TAG = "MatchAnythingCommand";

	private OnUnderstoodListener onUnderstood;

	public MatchAnythingCommand(OnUnderstoodListener onUnderstood) {
		this.onUnderstood = onUnderstood;
	}

	@Override
	public boolean interpret(WordList heard, float[] confidence) {
		boolean understood = false;
		if (heard.getWords().length > 0) {
			understood = true;
			if (onUnderstood != null) {
				onUnderstood.understood();
			}
		}
		return understood;
	}

	public OnUnderstoodListener getOnUnderstood() {
		return onUnderstood;
	}

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
	}

}
