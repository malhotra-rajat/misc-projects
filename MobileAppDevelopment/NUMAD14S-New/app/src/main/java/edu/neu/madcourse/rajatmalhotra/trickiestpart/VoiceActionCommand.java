package edu.neu.madcourse.rajatmalhotra.trickiestpart;

public interface VoiceActionCommand {
	boolean interpret(WordList heard, float[] confidenceScores);
}
