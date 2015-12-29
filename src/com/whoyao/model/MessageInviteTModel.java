package com.whoyao.model;

import com.whoyao.Const;

public class MessageInviteTModel {
	private int pageindex = Const.PAGE_DEFAULT_INDEX;
	private int pagesize = Const.PAGE_SIZE;
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	

}
