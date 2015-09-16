package hw6.model;

public class JelinekMercer {
	private String doc_no;
	
	private int doc_length;
	private int tf;
	private String term;
	public JelinekMercer(String doc_no, int doc_length,
			int tf, String term) {
	
		this.doc_no = doc_no;
	
		this.doc_length = doc_length;
		this.tf = tf;
		this.term = term;
	}
	
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
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
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	
	
}
