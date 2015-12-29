package com.whoyao.model;

import com.whoyao.Const;

/**
 * 活动搜索,请求数据模型
 * @author hyh 
 * creat_at：2013-10-17-下午1:23:03
 */
public class EventSearchTModel {
	private String keyword;
	private int areaid;
	private int userprovince;
	private int usercity;
	private double size;

	private int typeid;
	private int timespan;
	private int order = -1;
	private int activityprogress;
	private int pageindex = Const.PAGE_DEFAULT_INDEX;
	private int pagesize = Const.PAGE_SIZE;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getAreaid() {
		return areaid;
	}
	public void setAreaid(int areaid) {
		this.areaid = areaid;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public int getTimespan() {
		return timespan;
	}
	public void setTimespan(int timespan) {
		this.timespan = timespan;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getActivityprogress() {
		return activityprogress;
	}
	public void setActivityprogress(int activityprogress) {
		this.activityprogress = activityprogress;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public int getPagesize() {
		return pagesize;
	}
	public int getUserprovince() {
		return userprovince;
	}
	public void setUserprovince(int userprovince) {
		this.userprovince = userprovince;
	}
	public int getUsercity() {
		return usercity;
	}
	public void setUsercity(int usercity) {
		this.usercity = usercity;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	
	
}
