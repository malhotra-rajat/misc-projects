package hw2.model;

public class Proximity {
	private String doc_no;
	private String query_no;
	private double proximity;
	
	public Proximity(String doc_no, String query_no, double proximity) {
		super();
		this.doc_no = doc_no;
		this.query_no = query_no;
		this.proximity = proximity;
	}

	public String getDoc_no() {
		return doc_no;
	}

	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}

	public String getQuery_no() {
		return query_no;
	}

	public void setQuery_no(String query_no) {
		this.query_no = query_no;
	}

	public double getProximity() {
		return proximity;
	}

	public void setProximity(double proximity) {
		this.proximity = proximity;
	}
	
	
	
}
