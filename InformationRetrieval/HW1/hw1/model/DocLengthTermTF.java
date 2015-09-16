package hw1.model;

public class DocLengthTermTF {

	int doc_length;
	int tf;
	String term;
	
	public DocLengthTermTF(int doc_length, int tf, String term) {
		super();
		this.doc_length = doc_length;
		this.tf = tf;
		this.term = term;
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
