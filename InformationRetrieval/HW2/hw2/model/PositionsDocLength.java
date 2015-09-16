package hw2.model;

import java.util.ArrayList;

public class PositionsDocLength {
	
	String term;
	ArrayList<Integer> positions;
	int doc_length;
	
	public PositionsDocLength(String term, ArrayList<Integer> positions,
			int doc_length) {
		super();
		this.term = term;
		this.positions = positions;
		this.doc_length = doc_length;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Integer> positions) {
		this.positions = positions;
	}

	public int getDoc_length() {
		return doc_length;
	}

	public void setDoc_length(int doc_length) {
		this.doc_length = doc_length;
	}
	
}
