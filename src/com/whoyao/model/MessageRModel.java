package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2014-1-3-下午5:32:01
 */
public class MessageRModel {
	//{"id":8723,"subject":"","content":"jx","data":"[]","entertime":"2013-12-31 16:21:55"},
	
	private int id;
	private String subject;
	private String content;
	private String[] data;
	private String entertime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}
	public String getEntertime() {
		return entertime;
	}
	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}
}
