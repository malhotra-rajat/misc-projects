package hw5;

public class DocQueryScore {
	private String doc_no;
	private String query_no;
	private float score;
	
	public DocQueryScore(String doc_no, String query_no, float score) {
		super();
		this.doc_no = doc_no;
		this.query_no = query_no;
		this.score = score;
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

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	
	
	
	
}
