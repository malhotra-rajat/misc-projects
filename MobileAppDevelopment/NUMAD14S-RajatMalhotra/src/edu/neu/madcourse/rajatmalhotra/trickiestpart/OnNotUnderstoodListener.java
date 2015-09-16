package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.List;

public interface OnNotUnderstoodListener {
	/**
	* no explanation
	*/
	public static final int REASON_UNKNOWN = 0;
	/**
	* Recognition was inaccurate, perhaps because of poor audio quality
	*/
	public static final int REASON_INACCURATE_RECOGNITION = 1;
	/**
	* Recognition was accurate, but no match was found
	*/
	public static final int REASON_NOT_A_COMMAND = 2;
	/**
	* didn't understand the user's utterance for a particular reason
	* and provide some contextual information to construct useful feedback
	*/
	public void notUnderstood(List<String> heard, int reason);
}
