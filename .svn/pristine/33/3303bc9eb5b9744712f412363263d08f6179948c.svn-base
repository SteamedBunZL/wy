package com.whoyao.model;

import java.util.ArrayList;
import com.whoyao.utils.JSON;

public class NoticeListItem {
	private int senduserid;
	private String senduserpicture;
	private String sendusername;
	private String entertime;
	private String informcontent;
	private int contenttype;
	private int messagecontentid;
	private int isread;
	private int result;
	private String data;
	private ArrayList<RecommendUser> userList;

	public ArrayList<RecommendUser> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<RecommendUser> userList) {
		this.userList = userList;
	}
	public ArrayList<NoticeData> getData() {
		try {
			data.replace("\\\"", "");
			return JSON.getList(data, NoticeData.class);
		} catch (Exception e) {
			return null;
		}
	}


	// private List<NoticeData> relation;
	public int getSenduserid() {
		return senduserid;
	}

	public void setSenduserid(int senduserid) {
		this.senduserid = senduserid;
	}

	public String getSenduserpicture() {
		return senduserpicture;
	}

	public void setSenduserpicture(String senduserpicture) {
		this.senduserpicture = senduserpicture;
	}

	public String getSendusername() {
		return sendusername;
	}

	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}

	public String getEntertime() {
		return entertime;
	}

	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}

	public String getInformcontent() {
		return informcontent;
	}

	public void setInformcontent(String informcontent) {
		this.informcontent = informcontent;
	}

	public int getContenttype() {
		return contenttype;
	}

	public void setContenttype(int contenttype) {
		this.contenttype = contenttype;
	}

	public int getMessagecontentid() {
		return messagecontentid;
	}

	public void setMessagecontentid(int messagecontentid) {
		this.messagecontentid = messagecontentid;
	}

	public int getIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "NoticeListItem [senduserid=" + senduserid
				+ ", senduserpicture=" + senduserpicture + ", sendusername="
				+ sendusername + ", entertime=" + entertime
				+ ", informcontent=" + informcontent + ", contenttype="
				+ contenttype + ", messagecontentid=" + messagecontentid
				+ ", isread=" + isread + ", result=" + result + "]";
	}

}
