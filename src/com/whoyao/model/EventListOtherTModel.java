package com.whoyao.model;

import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2013-10-17-下午3:00:32
 */
public class EventListOtherTModel {
	private int UserId;
	private int Type;
	private int pageindex = Const.PAGE_DEFAULT_INDEX;
	private int pagesize = Const.PAGE_SIZE;
	
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
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
}
