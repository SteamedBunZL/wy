package com.whoyao.model;
/**
 * 注册信息
 * @author hyh 
 * creat_at：2013-7-16-下午5:36:23 <p>
 *
 */
public class RegisterModel {

	public String  username;
	public String  userpassword;
	public String  useremail;
	public String  userphone;
	public int usersex;
	public String userbirthday;
	public int  userprovince;//省
	public int  usercity;//市
	public int  userdistrict;//区县
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
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
	public int getUserDistrict() {
		return userdistrict;
	}
	public void setUserDistrict(int userdistrict) {
		this.userdistrict = userdistrict;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public int getUsersex() {
		return usersex;
	}
	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}
	public String getUserbirthday() {
		return userbirthday;
	}
	public void setUserbirthday(String userbirthday) {
		this.userbirthday = userbirthday;
	}
	
}
