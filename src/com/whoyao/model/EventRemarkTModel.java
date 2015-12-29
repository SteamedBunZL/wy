package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-9-29-下午4:17:53
 */
public class EventRemarkTModel {
	private int activityid;
	private String commentcontent;
	private String codekey;
	private String codevalue;
	
	public EventRemarkTModel(int eventID) {
		super();
		activityid = eventID;
	}
	public int getActivityid() {
		return activityid;
	}
	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}
	public String getCommentcontent() {
		return commentcontent;
	}
	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}
	public String getCodekey() {
		return codekey;
	}
	public void setCodekey(String codekey) {
		this.codekey = codekey;
	}
	public String getCodevalue() {
		return codevalue;
	}
	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}
}
