package com.whoyao.model;

import java.util.ArrayList;

import com.whoyao.utils.JSON;

public class InviteListItem {
	private int senduserid;
	private String sendusername;
	private String senduserface;
	private String invitetime;
	private String description;
	private int messagecontentid;
	private int isread;
	private int contenttype;
	private String data;
	public int getSenduserid() {
		return senduserid;
	}
	public void setSenduserid(int senduserid) {
		this.senduserid = senduserid;
	}
	public String getSendusername() {
		return sendusername;
	}
	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}
	public String getSenduserface() {
		return senduserface;
	}
	public void setSenduserface(String senduserface) {
		this.senduserface = senduserface;
	}
	public String getInvitetime() {
		return invitetime;
	}
	public void setInvitetime(String invitetime) {
		this.invitetime = invitetime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public ArrayList<InviteData> getData() {
		try {
			data = data.replace("\\\"", "").replace("\"\\","");
			return JSON.getList(data, InviteData.class);
		} catch (Exception e) {
			return null;
		}
	}
	public int getContenttype() {
		return contenttype;
	}
	public void setContenttype(int contenttype) {
		this.contenttype = contenttype;
	}
	@Override
	public String toString() {
		return "InviteListItem [senduserid=" + senduserid + ", sendusername="
				+ sendusername + ", senduserface=" + senduserface
				+ ", invitetime=" + invitetime + ", description=" + description
				+ ", messagecontentid=" + messagecontentid + ", isread="
				+ isread + ", contenttype=" + contenttype + ", data=" + data
				+ "]";
	}
	
	
}
