package com.whoyao.model;

/**
 * 条件设置(发送的数据)
 * @author hyh 
 * creat_at：2013-8-15-下午2:32:53
 */
public class UserConditionSettingTModel {
	private int receiveimmediateinvite;
	private int receiveonetooneinvite;
	private int usersex;
	private int userminage;
	private int usermaxage;
	private int realattestationuser;
	
	public int getReceiveimmediateinvite() {
		return receiveimmediateinvite;
	}
	public void setReceiveimmediateinvite(int receiveimmediateinvite) {
		this.receiveimmediateinvite = receiveimmediateinvite;
	}
	public void setReceiveimmediateinvite(boolean receiveImmediateInvite) {
		if(receiveImmediateInvite){
			receiveimmediateinvite = 1;
		}else{
			receiveimmediateinvite = 0;
		}
	}
	
	public int getReceiveonetooneinvite() {
		return receiveonetooneinvite;
	}
	public void setReceiveonetooneinvite(int receiveonetooneinvite) {
		this.receiveonetooneinvite = receiveonetooneinvite;
	}
	public void setReceiveonetooneinvite(boolean receiveOne2OneInvite) {
		if(receiveOne2OneInvite){
			receiveonetooneinvite = 1;
		}else{
			receiveonetooneinvite = 0;
		}
	}
	public int getUsersex() {
		return usersex;
	}
	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}
	public int getUserminage() {
		return userminage;
	}
	public void setUserminage(int userminage) {
		this.userminage = userminage;
	}
	public int getUsermaxage() {
		return usermaxage;
	}
	public void setUsermaxage(int usermaxage) {
		this.usermaxage = usermaxage;
	}
	public int getRealattestationuser() {
		return realattestationuser;
	}
	public void setRealattestationuser(int realattestationuser) {
		this.realattestationuser = realattestationuser;
	}
	public void setRealAttestationUser(boolean realAttestationUser) {
		if(realAttestationUser){
			realattestationuser = 1;
		}else{
			realattestationuser = 0;
		}
	}
}
