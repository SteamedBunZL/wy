package com.whoyao.thirlogin.sina;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.whoyao.AppContext;
import com.whoyao.Const.KEY;
import com.whoyao.engine.user.ThirdUserManager;
import com.whoyao.model.ThirdloginModel;
import com.whoyao.utils.L;

/**
 * 微博认证授权回调类。
 * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
 *    该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息。
 */
public class SinaAuthListener implements WeiboAuthListener {
	private Activity activity;
	private String sina_token; //新浪token
	private  String sina_uid ; //新浪uid
	private ThirdloginModel model;
	 /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 用户信息接口 */
    private UsersAPI mUsersAPI;
	public SinaAuthListener(Activity act) {
		activity = act;
	}
	@Override
	public void onCancel() {
		Toast.makeText(activity, "Auth cancel", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onComplete(Bundle values) {
		 Toast.makeText(activity,"onComplete", 0).show();
		 Log.i("互邀=======", "SinaAuthListener onComplete");
		 model = new ThirdloginModel();
		 sina_token = values.getString("access_token");
		 String expires_in = values.getString("expires_in");
	     sina_uid = values.getString("uid");
	     model.setAccesstoken(sina_token);
	     model.setLoginuserid(sina_uid);
	     mAccessToken = new Oauth2AccessToken(sina_token, expires_in);
	     Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
         if (accessToken.isSessionValid()) {
             Log.i("TAG", "mAccessToken.isSessionValid() = "+ mAccessToken.isSessionValid());
             Toast.makeText(activity,"mAccessToken.isSessionValid() = "+ mAccessToken.isSessionValid(), 0).show();
             AccessTokenKeeper.writeAccessToken(activity, accessToken);
             // 获取用户信息接口
             mUsersAPI = new UsersAPI(mAccessToken);
             long uid =Long.parseLong(sina_uid) ;
             LogUtil.i("onComplete", "AuthListener... getid" +mAccessToken.getUid());
             mUsersAPI.show(uid, mListener);
             }else{
             // 以下几种情况，您会收到 Code：
             // 1. 当您未在平台上注册的应用程序的包名与签名时；
             // 2. 当您注册的应用程序包名与签名不正确时；
             // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
             String code = values.getString("code");
             String message = values.getString("授权失败");
             if (!TextUtils.isEmpty(code)) {
                 message = message + "\nObtained the code: " + code;
             }
             Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
         }
	}
	/**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i("TAG", response);
                JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					Log.i("sinaJsonObject", "sinaJsonObject........" + jsonObject.toString());
					String sinaName = jsonObject.getString("name");//
					String picurl = jsonObject.getString("profile_image_url");
					model.setLogintype(4); 
					model.setUsernickname(sinaName);
					model.setPicurl(picurl);
					Editor editor = AppContext.getConfigFile().edit();
				    editor.putString(KEY.Usernickname, sinaName);
					editor.putString(KEY.Picurl, picurl);
					editor.putString(KEY.Loginuserid,sina_uid );
					editor.putString(KEY.Accesstoken,sina_token);
					editor.putInt(KEY.Logintype,4);
					editor.commit();//提交修改
					Log.i("互邀-----", ""+AppContext.getConfigFile().getString(KEY.Loginuserid,"")+
							AppContext.getConfigFile().getString(KEY.Accesstoken,"")+
							AppContext.getConfigFile().getString(KEY.Usernickname,"")+
							AppContext.getConfigFile().getString(KEY.Picurl,""));
					ThirdUserManager.getManager().CheckLogin(model);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("TAG", e.getMessage()); 
        }
    };
	@Override
	public void onWeiboException(WeiboException arg0) {
		Toast.makeText(activity, 
                "Auth exception : " +arg0.getMessage(), Toast.LENGTH_LONG).show();
	}
}