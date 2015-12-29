package com.whoyao.model;

/**
 * @author hyh creat_at：2013-11-12-下午1:14:06
 */
public class UserListItemModel {
	private int userid;// ":448,
	private String username;// ":"绿林大冒险",
	private String usertag;// ":"篮球,篮球,篮球,篮球,篮球,篮球,",
	private String userface;// ":"http://avatar.d.whoyao.com/448/448/1374199565/50x50.jpg",
	private String userbirthday;// ":"1990/1/1 0:00:00",
	// private double longitude;//":"116.31628",
	// private double latitude;//":"39.983913",
	private float size;// ":"0.0003",
	private int usersex;// ":"2"},
	private int usermobilestate;// ":2,
	private int userhonestystate;// ":1,
	private int isfreetime;// ":"0"
	private boolean isChecked = false;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertag() {
		return usertag;
	}

	public void setUsertag(String usertag) {
		this.usertag = usertag;
	}

	public String getUserface() {
		return userface;
	}

	public void setUserface(String userface) {
		this.userface = userface;
	}

	public String getUserbirthday() {
		return userbirthday;
	}

	public void setUserbirthday(String userbirthday) {
		this.userbirthday = userbirthday;
	}

	// public double getLongitude() {
	// return longitude;
	// }
	// public void setLongitude(double longitude) {
	// this.longitude = longitude;
	// }
	// public double getLatitude() {
	// return latitude;
	// }
	// public void setLatitude(double latitude) {
	// this.latitude = latitude;
	// }
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public int getUsersex() {
		return usersex;
	}

	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}

	public int getUserhonestystate() {
		return userhonestystate;
	}

	public void setUserhonestystate(int userhonestystate) {
		this.userhonestystate = userhonestystate;
	}

	public int getUsermobilestate() {
		return usermobilestate;
	}

	public void setUsermobilestate(int usermobilestate) {
		this.usermobilestate = usermobilestate;
	}

	public int getIsfreetime() {
		return isfreetime;
	}

	public void setIsfreetime(int isfreetime) {
		this.isfreetime = isfreetime;
	}

	public boolean isFreetime() {
		return isfreetime == 1;
	}

	public boolean isMobileV() {
		return usermobilestate == 2;
	}

	public boolean isHonestyV() {
		return userhonestystate == 1;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "UserListItemModel [userid=" + userid + ", username=" + username
				+ ", usertag=" + usertag + ", userface=" + userface
				+ ", userbirthday=" + userbirthday + ", size=" + size
				+ ", usersex=" + usersex + ", usermobilestate="
				+ usermobilestate + ", userhonestystate=" + userhonestystate
				+ ", isfreetime=" + isfreetime + ", isChecked=" + isChecked
				+ "]";
	}

}
