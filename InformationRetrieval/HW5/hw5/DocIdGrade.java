package hw5;

public class DocIdGrade {
	private String DocId;
	private int grade;
	
	public DocIdGrade(String docId, int grade) {
		DocId = docId;
		this.grade = grade;
	}
	public String getDocId() {
		return DocId;
	}
	public void setDocId(String docId) {
		DocId = docId;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	
}
