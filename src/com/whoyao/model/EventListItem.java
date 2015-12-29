package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-9-13-上午10:20:02
 */
public class EventListItem {
	private int activityid;
	private String title;
	private String activitypicture;
	private int typeid;
	private int activitycategory;
	private float longitude;
	private float latitude;
	private String address;
	private String begintime;
	private String endtime;
	private String keyword;
	private int activityprogressstate;
	private int activitystate;
	private int joinnum;
	private float activityevaluationvalue;
	private ValuationModel evaluated;
	private int photonum;
	private int activityhidestatus;
	private int pv;
	private int commentnum;
	
	public int getPv() {
		return pv;
	}
	public int getCommentnum() {
		return commentnum;
	}
	public void setCommentnum(int commentnum) {
		this.commentnum = commentnum;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getActivityid() {
		return activityid;
	}
	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getActivitypicture() {
		return activitypicture;
	}
	public void setActivitypicture(String activitypicture) {
		this.activitypicture = activitypicture;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public int getActivitycategory() {
		return activitycategory;
	}
	public void setActivitycategory(int activitycategory) {
		this.activitycategory = activitycategory;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getActivityprogressstate() {
		return activityprogressstate;
	}
	public void setActivityprogressstate(int activityprogressstate) {
		this.activityprogressstate = activityprogressstate;
	}
	public int getActivitystate() {
		return activitystate;
	}
	public void setActivitystate(int activitystate) {
		this.activitystate = activitystate;
	}
	public int getJoinnum() {
		return joinnum;
	}
	public void setJoinnum(int joinnum) {
		this.joinnum = joinnum;
	}
	public float getActivityevaluationvalue() {
		return activityevaluationvalue;
	}
	public void setActivityevaluationvalue(float activityevaluationvalue) {
		this.activityevaluationvalue = activityevaluationvalue;
	}
	public ValuationModel getEvaluated() {
		return evaluated;
	}
	public void setEvaluated(ValuationModel evaluated) {
		this.evaluated = evaluated;
	}
	public int getPhotonum() {
		return photonum;
	}
	public void setPhotonum(int photonum) {
		this.photonum = photonum;
	}
	public int getActivityhidestatus() {
		return activityhidestatus;
	}
	public void setActivityhidestatus(int activityhidestatus) {
		this.activityhidestatus = activityhidestatus;
	}
}
