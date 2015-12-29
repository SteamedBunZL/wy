package com.whoyao.model;


/**
 * @author hyh 
 * creat_at：2014-1-22-下午3:44:34
 */
public class TopicRemarkModel {
	private int id;
	private String time;
	private int userid;
	private String userface;
	private String username;
	private String content;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUserface() {
		return userface;
	}
	public void setUserface(String userface) {
		this.userface = userface;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
