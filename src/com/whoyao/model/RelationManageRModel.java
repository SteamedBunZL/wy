package com.whoyao.model;


/**
 * 判断关系
 * @author hyh 
 * creat_at：2013-11-11-下午4:46:20
 */
public class RelationManageRModel {
	private int friend;
	private int favicons;
	private int blacklist;
	public boolean isFriend() {
		return 1 == friend;
	}
	public void setFriend(int friend) {
		this.friend = friend;
	}
	public void setFriend(boolean isFriend) {
		if(isFriend){
			friend = 1;
		}else{
			friend = 0;
		}
	}
	public boolean isFavicons() {
		return 1 == favicons;
	}
	public void setFavicons(int favicons) {
		this.favicons = favicons;
	}
	public void setFavicons(boolean isFavicons) {
		if(isFavicons){
			favicons = 1;
		}else{
			favicons = 0;
		}
	}
	public boolean isBlacklist() {
		return 1 == blacklist;
	}
	public void setBlacklist(int blacklist) {
		this.blacklist = blacklist;
	}
	public void setBlacklist(boolean isBlack) {
		if(isBlack){
			blacklist = 1;
		}else{
			blacklist = 0;
		}
	}
}
