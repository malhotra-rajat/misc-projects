package hw2.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TermStats implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<DocNosPositions> docNosTFPositions;

	public TermStats(ArrayList<DocNosPositions> docNosTFPositions) {
		super();
		this.docNosTFPositions = docNosTFPositions;
	}

	public ArrayList<DocNosPositions> getDocNosTFPositions() {
		return docNosTFPositions;
	}

	public void setDocNosTFPositions(ArrayList<DocNosPositions> docNosTFPositions) {
		this.docNosTFPositions = docNosTFPositions;
	}
	

	
	
}
