package hw2.model;

import java.util.ArrayList;

public class TermDocNoPositions {
	
	String term;
	ArrayList<DocNosPositions> docNoPositionsList;
	
	public TermDocNoPositions(String term,
			ArrayList<DocNosPositions> docNoPositionsList) {
		super();
		this.term = term;
		this.docNoPositionsList = docNoPositionsList;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public ArrayList<DocNosPositions> getDocNoPositionsList() {
		return docNoPositionsList;
	}

	public void setDocNoPositionsList(ArrayList<DocNosPositions> docNoPositionsList) {
		this.docNoPositionsList = docNoPositionsList;
	}
	
	

}
