package hw6.model;

public class TFIDF {
	private String doc_no;
	private String query_no;
	private int tf;
	private double tfIdf;
	
	public TFIDF(String doc_no, String query_no, int tf,
			double tfIdf) {

		this.doc_no = doc_no;
		this.query_no = query_no;
		this.tf = tf;
		this.tfIdf = tfIdf;
		
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

	public int getTf() {
		return tf;
	}
	public void setTf(int tf) {
		this.tf = tf;
	}

	public double getTfIdf() {
		return tfIdf;
	}

	public void setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
	}

}
