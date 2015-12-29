package com.whoyao.engine.user;


import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.Config;
import com.whoyao.Const.KEY;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.MainActivity;
import com.whoyao.activity.ThirdpreferActivity;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.ThirdloginModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.content.SharedPreferences.Editor;
import android.util.Log;
/**
 * 用户相关业务逻辑
 * 
 * @author HYH create at：2013-5-6 下午01:00:39
 */
public class ThirdUserManager{

	private LoadingDialog loadingdialog;
	private static ThirdUserManager manager;
	int result;
	public static ThirdUserManager getManager() {
		synchronized (ThirdUserManager.class) {
			if(manager == null){
				manager = new ThirdUserManager();
			}
		}
		return manager;
	}
	/**第三方登录*/
	public void ThirdLogin(final ThirdloginModel model){
		model.setMobileNum(ClientEngine.getMobileInfo().mobilenum);
		model.setNetWork(ClientEngine.getMobileInfo().network);
		model.setPhoneModel(ClientEngine.getMobileInfo().phonemodel);
		model.setSystemVerison(ClientEngine.getMobileInfo().systemversion);
		model.setClientVersion(ClientEngine.getMobileInfo().clientversion);
		model.setVgaHeight(ClientEngine.getMobileInfo().vgaheight);
		model.setVgaWidth(ClientEngine.getMobileInfo().vgawidth);
		if(null != AppContext.location){
			model.setLatitude((float)AppContext.location.getLatitude());
			model.setLongitude((float)AppContext.location.getLongitude());
		}else{
			model.setLatitude(0);
			model.setLongitude(0);
		}
		Net.request(model, WebApi.User.LoginThird, new ResponseHandler(true){
		@Override
		public void onSuccess(String content) {
			try {				
				L.i("互邀----","ThirdLogin["+content.toString());
				Toast.show("ThirdLogin onSuccess"+content.toString());
				JSONObject object = new JSONObject(content);
				L.i("互邀----","ThirdLogin["+result);
					if(object.has("result")){
						result = object.getInt("result");
						if(result==2){
						L.i("互邀----","ThirdLogin["+result);
						Toast.show(R.string.warn_third_repeat);
						}
					}else{
						UserInfoModel info = JSON.getObject(content, UserInfoModel.class);
						L.i("互邀","ThirdLogin else"+info.toString());
						MyinfoManager.setUserInfo(info);
						AppContext.loginTime = AppContext.serviceTimeMillis();
						Editor edit = AppContext.getConfigFile().edit();
						edit.putString(Config.Account, model.usernickname);
						edit.putString(Config.Accesstoken,model.accesstoken);
						edit.putString(Config.Openid, model.loginuserid);
						edit.putInt(KEY.User_ID, info.getUserID());
						edit.commit();
						AppContext.startAct(MainActivity.class,true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		@Override
		public void onFailure(Throwable e, int statusCode, String content) {
				L.i("互邀----","ThirdLogin[onFailure "+content.toString());	
			if(400 == statusCode || 406 == statusCode){
			Toast.show(R.string.warn_netrequest_failure);
		}
			super.onFailure(e, statusCode, content);
		}
		});
	}
	/**检查第三方登录*/
	public void CheckLogin(final ThirdloginModel model){
		L.i("互邀----","i am in  CheckLogin[");
		RequestParams params = Net.getRequestParamsShowNull(ClientEngine
				.getMobileInfo());
		params.put(KEY.Loginid,model.getLoginuserid());
		params.put(KEY.Logintype,model.getLogintype()+ "");
		if(null != AppContext.location){
			params.put("longitude",(float)AppContext.location.getLatitude()+"");
			params.put("latitude",(float)AppContext.location.getLongitude()+"");
		}else{
			params.put("longitude",0+"");
			params.put("latitude",0+"");
		}
		L.i("互邀----","i am in  CheckLogin[["+model.getLoginuserid());
		L.i("互邀----","i am in  CheckLogin1[[["+params.toString());
		Net.request(params, WebApi.User.CheckLoginThird, new ResponseHandler(){
			@Override
		public void onSuccess(String content) {
			try {
				Log.i("互邀----","CheckLogin2["+content.toString());
				JSONObject object = new JSONObject(content);
				Log.i("互邀----","onSuccess["+object.has("result"));
				if(!object.has("result")){
					Log.i("互邀----","跳转啦  MainActivity[[[");
					AppContext.startAct(MainActivity.class,true);
				}else{
					result = object.getInt("result");
					if(result==1)
					Log.i("互邀----","跳转啦  ThirdpreferActivity[[["+params.toString());
					AppContext.startAct(ThirdpreferActivity.class,true);
			    }
				}catch (JSONException e){
					e.printStackTrace();
				}
		}
			@Override
		public void onFailure(Throwable e, int statusCode, String content) {
				if(400 == statusCode || 406 == statusCode){
					Toast.show(R.string.warn_netrequest_failure);
				}
					super.onFailure(e, statusCode, content);
				}
				});
	}
	/**显示LoadingDialog*/
	@SuppressWarnings("unused")
	private void showLoading(){
		if(null == loadingdialog){
			loadingdialog = LoadingDialog.getDialog();
		}
		if(!loadingdialog.isShowing()){
			loadingdialog.show();
		}
	}
	
	/**隐藏LoadingDialog*/
	@SuppressWarnings("unused")
	private void dismissLoading(){
		if(null != loadingdialog && loadingdialog.isShowing()){
			loadingdialog.dismiss();
		}
	}
	
	public void recycle(){
		manager = null;
	}
}
