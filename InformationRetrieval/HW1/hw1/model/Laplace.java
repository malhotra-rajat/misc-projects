package hw1.model;

public class Laplace {
	private String doc_no;
	private String query_no;
	private int doc_length;
	private int tf;
	
	public Laplace(String doc_no, String query_no, int doc_length, int tf) {
		this.doc_no = doc_no;
		this.query_no = query_no;
		this.doc_length = doc_length;
		this.tf = tf;
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
	public int getDoc_length() {
		return doc_length;
	}
	public void setDoc_length(int doc_length) {
		this.doc_length = doc_length;
	}
	public int getTf() {
		return tf;
	}
	public void setTf(int tf) {
		this.tf = tf;
	}
	
	
	
	

}
