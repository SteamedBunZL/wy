package com.whoyao.model;

/**
 * 条件设置(接收到的数据)
 * @author hyh 
 * creat_at：2013-8-15-下午2:29:44
 */
/**
 * @author hyh 
 * creat_at：2013-8-21-下午12:27:04
 */
public class UserConditionSettingRModel {
	private boolean ReceiveImmediateInvite;
	private boolean ReceiveOneToOneInvite;
	private boolean RealAttestationUser;
	private int UserSex;
	private int UserMinAge;
	private int UserMaxAge;
	
	public int getUserSex() {
		return UserSex;
	}
	public String getUserSexStr() {
		switch (UserSex) {
		case 0:
			return "不限";
		case 1:
			return "男";
		case 2:
			return "女";
		default:
			return null;
		}
	}
	public void setUserSex(int userSex) {
		UserSex = userSex;
	}
	public int getUserMinAge() {
		return UserMinAge;
	}
	public void setUserMinAge(int userMinAge) {
		UserMinAge = userMinAge;
	}
	public int getUserMaxAge() {
		return UserMaxAge;
	}
	public void setUserMaxAge(int userMaxAge) {
		UserMaxAge = userMaxAge;
	}
	public boolean isReceiveImmediateInvite() {
		return ReceiveImmediateInvite;
	}
	public void setReceiveImmediateInvite(boolean receiveImmediateInvite) {
		ReceiveImmediateInvite = receiveImmediateInvite;
	}
	public boolean isReceiveOneToOneInvite() {
		return ReceiveOneToOneInvite;
	}
	public void setReceiveOneToOneInvite(boolean receiveOneToOneInvite) {
		ReceiveOneToOneInvite = receiveOneToOneInvite;
	}
	public boolean isRealAttestationUser() {
		return RealAttestationUser;
	}
	public void setRealAttestationUser(boolean realAttestationUser) {
		RealAttestationUser = realAttestationUser;
	}

}
