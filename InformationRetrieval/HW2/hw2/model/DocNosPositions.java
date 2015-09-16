package hw2.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DocNosPositions implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int doc_no_hash;
	private ArrayList<Integer> positions;
	
	public DocNosPositions(int doc_no_hash, ArrayList<Integer> positions) {
		super();
		this.doc_no_hash = doc_no_hash;
		this.positions = positions;
	}

	public int getDoc_no_hash() {
		return doc_no_hash;
	}

	public void setDoc_no_hash(int doc_no_hash) {
		this.doc_no_hash = doc_no_hash;
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Integer> positions) {
		this.positions = positions;
	}
	
	
	
	
}
