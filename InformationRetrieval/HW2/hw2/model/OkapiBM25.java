package hw2.model;

public class OkapiBM25 {
	private String doc_no;
	private String query_no;
	private String term;
	private int tf;
	private double bm25;
	
	public OkapiBM25(String doc_no, String query_no, String term, int tf,
			double bm25) {

		this.doc_no = doc_no;
		this.query_no = query_no;
		this.term = term;
		this.tf = tf;
		this.bm25 = bm25;
		
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
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getTf() {
		return tf;
	}
	public void setTf(int tf) {
		this.tf = tf;
	}

	public double getBm25() {
		return bm25;
	}

	public void setBm25(double bm25) {
		this.bm25 = bm25;
	}

}
