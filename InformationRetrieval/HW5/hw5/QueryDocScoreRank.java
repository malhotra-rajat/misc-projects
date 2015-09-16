package hw5;

public class QueryDocScoreRank {
	private String query_no;
	private String doc_no;
	private double score;
	private int rank;
	
	public QueryDocScoreRank(String query_no, String doc_no, double score,
			int rank) {
		super();
		this.query_no = query_no;
		this.doc_no = doc_no;
		this.score = score;
		this.rank = rank;
	}

	public String getQuery_no() {
		return query_no;
	}

	public void setQuery_no(String query_no) {
		this.query_no = query_no;
	}

	public String getDoc_no() {
		return doc_no;
	}

	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}
