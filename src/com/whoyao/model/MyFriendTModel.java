package com.whoyao.model;

import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2013-11-14-上午10:32:23
 */
public class MyFriendTModel {
	private int friendtype = 1;
	private int pagesize = Const.PAGE_SIZE;
	private int pageindex = Const.PAGE_DEFAULT_INDEX;
	public int getFriendtype() {
		return friendtype;
	}
	public void setFriendtype(int friendtype) {
		this.friendtype = friendtype;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}

}
