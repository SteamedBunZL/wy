package com.whoyao.model;

public class PrivateDetailListItem {
	private int senderuserid;
	private String content;
	private String entertime;
	private boolean isowner;
	private String headpicture;
	private int msgcontentid;
	private int id;

	public int getSenderuserid() {
		return senderuserid;
	}

	public void setSenderuserid(int senderuserid) {
		this.senderuserid = senderuserid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEntertime() {
		return entertime;
	}

	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}

	public String getHeadpicture() {
		return headpicture;
	}

	public void setHeadpicture(String headpicture) {
		this.headpicture = headpicture;
	}

	public boolean isIsowner() {
		return isowner;
	}

	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}

	public int getMsgcontentid() {
		return msgcontentid;
	}

	public void setMsgcontentid(int msgcontentid) {
		this.msgcontentid = msgcontentid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PrivateDetailListItem [senderuserid=" + senderuserid
				+ ", content=" + content + ", entertime=" + entertime
				+ ", isowner=" + isowner + ", headpicture=" + headpicture
				+ ", msgcontentid=" + msgcontentid + ", id=" + id + "]";
	}

}
