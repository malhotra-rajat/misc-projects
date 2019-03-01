package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatcherUtil {
	// /**
	// * The String from which the data is to be retrieved
	// */
	// private String searchString = new String();
	//
	// /**
	// * @return the searchString
	// */
	// public String getSearchString() {
	// return searchString;
	// }
	//
	// /**
	// * @param searchString the searchString to set
	// */
	// public void setSearchString(String searchString) {
	// this.searchString = searchString;
	// }

	// /**
	// * Words that rhyme with "I"
	// */
	// List<String> iRhymes = Arrays.asList("i", "eye", "aye", "ai");
	//
	// /**
	// * Words that rhyme with "Just"
	// */
	// List<String> justRhymes = Arrays.asList("just", "");

	/**
	 * Trigger phrases
	 */
	static List<String> triggers = Arrays.asList("add", "and", "ad", "ack", "ag", "at", "akc", "app", "odd", "og", "op", "opp", "ott");

	/**
	 * 
	 * @param strList
	 * @return
	 */
	public static boolean findTrigger(ArrayList<String> strList) {
		boolean isTriggerFound = false;
		String[] parts;
		String trigWord;
//		int index = 0;

		for (String str : strList) {
			parts = str.split(" ");
			trigWord = parts[0];
			
			if(triggers.contains(trigWord)) {
				isTriggerFound = true;
				break;
			}
			
//			index++;
		}

		return isTriggerFound;
	}

	public static String getExerciseName(ArrayList<String> list,
			int index) {
		String exerciseName = null;
		return exerciseName;
	}

}
