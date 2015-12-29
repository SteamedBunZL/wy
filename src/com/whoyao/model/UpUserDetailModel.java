package com.whoyao.model;

/**
 * @author hyh 
 * creat_at：2013-8-9-上午8:47:29
 */
public class UpUserDetailModel {
	private String username;
	private String userbirthday;
	private String usercontent;
	private int userprovince;
	private int usercity;
	private int userdistrict;
	private int usersex;
	private int userheight;
	private int userwaist;
	private int userbloodgroup;
	private String qq;
	private String msn;
	private String usermicromessage;
	private String usermicroblog;
	private String userrelname;
	
	public UpUserDetailModel() {
		super();
	}
	public UpUserDetailModel(UserInfoModel info) {
		super();
		username = info.getUserName();
		userbirthday = info.getUserBirthday();
		usercontent = info.getUserContent();
		userprovince = info.getUserProvince();
		usercity = info.getUserCity();
		userdistrict = info.getUserDistrict();
		usersex = info.getUserSex();
		userheight = info.getUserHeight();
		userwaist = info.getUserWaist();
		userbloodgroup = info.getUserBloodGroup();
		qq = info.getQQ();
		msn = info.getMSN();
		usermicromessage = info.getUserMicroMessage();
		usermicroblog = info.getUserMicroblog();
		userrelname = info.getUserRelName();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public int getUserdistrict() {
		return userdistrict;
	}
	public void setUserdistrict(int userdistrict) {
		this.userdistrict = userdistrict;
	}
	public String getUserbirthday() {
		return userbirthday;
	}
	public void setUserbirthday(String userbirthday) {
		this.userbirthday = userbirthday;
	}
	public int getUsersex() {
		return usersex;
	}
	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}
	public int getUserheight() {
		return userheight;
	}
	public void setUserheight(int userheight) {
		this.userheight = userheight;
	}
	public int getUserwaist() {
		return userwaist;
	}
	public void setUserwaist(int userwaist) {
		this.userwaist = userwaist;
	}
	public int getUserbloodgroup() {
		return userbloodgroup;
	}
	public void setUserbloodgroup(int userbloodgroup) {
		this.userbloodgroup = userbloodgroup;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getUsermicromessage() {
		return usermicromessage;
	}
	public void setUsermicromessage(String usermicromessage) {
		this.usermicromessage = usermicromessage;
	}
	public String getUsermicroblog() {
		return usermicroblog;
	}
	public void setUsermicroblog(String usermicroblog) {
		this.usermicroblog = usermicroblog;
	}
	public String getUsercontent() {
		return usercontent;
	}
	public void setUsercontent(String usercontent) {
		this.usercontent = usercontent;
	}
	public String getUserrelname() {
		return userrelname;
	}
	public void setUserrelname(String userrelname) {
		this.userrelname = userrelname;
	}	
}
