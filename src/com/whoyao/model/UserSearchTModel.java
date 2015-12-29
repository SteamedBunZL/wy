package com.whoyao.model;

import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2013-11-13-下午2:21:02
 */
public class UserSearchTModel {
	private String keyword;
	private float size;
	private int userdistrict;
	private int usersex = -1;
	private int userage;
	private int pagesize = Const.PAGE_SIZE;
	private int pageindex = Const.PAGE_DEFAULT_INDEX;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public int getUserdistrict() {
		return userdistrict;
	}
	public void setUserdistrict(int userdistrict) {
		this.userdistrict = userdistrict;
	}
	public int getUsersex() {
		return usersex;
	}
	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}
	public int getUserage() {
		return userage;
	}
	public void setUserage(int userage) {
		this.userage = userage;
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
