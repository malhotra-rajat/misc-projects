package hw6.model;

import java.io.Serializable;

public class QueryDoc implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String query_no;
	private String doc_no;
	
	public QueryDoc(String query_no, String doc_no) {
		super();
		this.query_no = query_no;
		this.doc_no = doc_no;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doc_no == null) ? 0 : doc_no.hashCode());
		result = prime * result
				+ ((query_no == null) ? 0 : query_no.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryDoc other = (QueryDoc) obj;
		if (doc_no == null) {
			if (other.doc_no != null)
				return false;
		} else if (!doc_no.equals(other.doc_no))
			return false;
		if (query_no == null) {
			if (other.query_no != null)
				return false;
		} else if (!query_no.equals(other.query_no))
			return false;
		return true;
	}
	
	
}
