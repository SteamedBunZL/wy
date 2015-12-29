package com.whoyao;

import com.whoyao.Const.KEY;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author hyh 
 * creat_at：2014-3-20-上午10:08:59
 */
public class SharePreferenceUtil {
	
	private SharedPreferences sp;
	private static SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context) {
		sp = context.getSharedPreferences("互邀", Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	public static void setIsPass(boolean isPass) {
		editor.putBoolean("isPass", isPass);
		editor.commit();
	}

	public boolean getIsPass() {
		return sp.getBoolean("isPass",false);
	}
	// 用户自己的头像
	public String getPicurl() {
			return sp.getString(KEY.Picurl, "");
	}
	public static void setPicurl(String picurl) {
			editor.putString(KEY.Picurl, picurl);
			editor.commit();
	}
	// 用户自己的昵称
	public String getUsernickname(){
		return sp.getString(KEY.Usernickname,"");
	}
	public static void setUsernickname(String usernickname){
		editor.putString(KEY.Usernickname, usernickname);
		editor.commit();
	}
	// 用户自己的id
	public String getLoginUserid() {
		return sp.getString(KEY.Loginuserid, "");
	}
	public static void setLoginUserid(String userid) {
		editor.putString(KEY.Loginuserid, userid);
		editor.commit();
	}
	// 用户accesstoken
	public String getAccesstoken(){
		return sp.getString(KEY.Accesstoken,"");
	}
	public static void setAccesstoken(String accesstaken){
		editor.putString(KEY.Accesstoken, accesstaken);
		editor.commit();
	}
	// logintype
	public int getLogintype(){
		return sp.getInt(KEY.Logintype,0);
	}
	public static void setLogintype(int logintype){
			editor.putInt(KEY.Logintype, logintype);
			editor.commit();
	}
	
}
