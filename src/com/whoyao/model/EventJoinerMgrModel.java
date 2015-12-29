package com.whoyao.model;

import java.io.Serializable;

/**
 * 活动人员管理
 * @author hyh 
 * creat_at：2013-10-11-下午4:45:15
 */
public class EventJoinerMgrModel implements Serializable {
	private static final long serialVersionUID = 3152758993876294458L;
	private int activityid;
	private int userid;
	private int joinstate;
	private String ownerremark;
	
	public int getActivityid() {
		return activityid;
	}
	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getJoinstate() {
		return joinstate;
	}
	public void setJoinstate(int joinstate) {
		this.joinstate = joinstate;
	}
	public String getOwnerremark() {
		return ownerremark;
	}
	public void setOwnerremark(String ownerremark) {
		this.ownerremark = ownerremark;
	}
}
