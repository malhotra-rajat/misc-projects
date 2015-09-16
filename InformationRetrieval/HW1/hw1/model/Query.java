package hw1.model;

import java.util.ArrayList;

public class Query {
	
	private String queryNumber;
	private ArrayList<String> queryWords;
	
	
	public Query(String queryNumber, ArrayList<String> queryWords) {
	
		this.queryNumber = queryNumber;
		this.queryWords = queryWords;
	}
	
	public String getQueryNumber() {
		return queryNumber;
	}
	public void setQueryNumber(String queryNumber) {
		this.queryNumber = queryNumber;
	}
	public ArrayList<String> getQueryWords() {
		return queryWords;
	}
	public void setQueryWords(ArrayList<String> queryWords) {
		this.queryWords = queryWords;
	}
	
	

}
