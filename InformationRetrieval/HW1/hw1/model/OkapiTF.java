package hw1.model;


public class OkapiTF {
	private String doc_no;
	private String query_no;
	private int tf;
	private double okapiTf;
	
	public OkapiTF(String doc_no, String query_no, int tf,
			double okapiTf) {
		super();
		this.doc_no = doc_no;
		this.query_no = query_no;
		this.tf = tf;
		this.okapiTf = okapiTf;
		
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

	public double getOkapiTf() {
		return okapiTf;
	}

	public void setOkapiTf(double okapiTf) {
		this.okapiTf = okapiTf;
	}
	
}
