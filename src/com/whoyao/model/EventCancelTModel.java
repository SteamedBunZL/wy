package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-9-17-下午1:39:24
 */
public class EventCancelTModel {
	private int activityid;
	private String cancelremark;
	private String cancelremarkcomment;
	
	public int getActivityid() {
		return activityid;
	}
	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}
	public String getCancelremark() {
		return cancelremark;
	}
	public void setCancelremark(String cancelremark) {
		this.cancelremark = cancelremark;
	}
	public String getCancelremarkcomment() {
		return cancelremarkcomment;
	}
	public void setCancelremarkcomment(String cancelremarkcomment) {
		this.cancelremarkcomment = cancelremarkcomment;
	}
}
