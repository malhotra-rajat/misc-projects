package edu.neu.madcourse.rajatmalhotra.wordgame;

import java.io.Serializable;

public class PuzzleObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char puzzle[][];

	public char[][] getPuzzle() {
		return puzzle;
	}
	public void setPuzzle(char[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	
}
