package hw1.model;

import org.elasticsearch.common.xcontent.XContentBuilder;

public class XContentBuilderWrapper {
	XContentBuilder xcb;
	String docno;
	
	public XContentBuilder getXcb() {
		return xcb;
	}
	public void setXcb(XContentBuilder xcb) {
		this.xcb = xcb;
	}
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
}
