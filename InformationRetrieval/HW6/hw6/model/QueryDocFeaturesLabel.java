package hw6.model;

public class QueryDocFeaturesLabel {
	private String query_no;
	private String doc_no;
	private double okapi;
	private double bm25;
	private double tfidf;
	private double laplace;
	private double jelinekmercer;
	private String label;
	
	public QueryDocFeaturesLabel(String query_no, String doc_no, double okapi,
			double bm25, double tfidf, double laplace, double jelinekmercer,
			String label) {
		super();
		this.query_no = query_no;
		this.doc_no = doc_no;
		this.okapi = okapi;
		this.bm25 = bm25;
		this.tfidf = tfidf;
		this.laplace = laplace;
		this.jelinekmercer = jelinekmercer;
		this.label = label;
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

	public double getOkapi() {
		return okapi;
	}

	public void setOkapi(double okapi) {
		this.okapi = okapi;
	}

	public double getBm25() {
		return bm25;
	}

	public void setBm25(double bm25) {
		this.bm25 = bm25;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

	public double getLaplace() {
		return laplace;
	}

	public void setLaplace(double laplace) {
		this.laplace = laplace;
	}

	public double getJelinekmercer() {
		return jelinekmercer;
	}

	public void setJelinekmercer(double jelinekmercer) {
		this.jelinekmercer = jelinekmercer;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
	
	
}
