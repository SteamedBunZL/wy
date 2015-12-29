package com.whoyao.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.R.color;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.ThirdUserManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.ThirdloginModel;
import com.whoyao.thirlogin.qq.Util;
import com.whoyao.thirlogin.sina.SinaAuthListener;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class LoginActivity extends BasicActivity implements OnClickListener, OnKeyListener {
	private EditText accountEt;
	private EditText passwordEt;
	public final static String SER_KEY = "Thiramodel";
	private View loginBtn;
	private View loginBtn_QQ;
	private View loginBtn_WeiBo;
	private View retrieveTv;
	private View registerTv;
	private Tencent mTencent;
	private String nickname;
	private String uImgUrl ;
	private String userid;
	private String openId;
	private String accesstoken;
    /** 微博 Web 授权类，提供登陆等功能  */
    private WeiboAuth mWeiboAuth;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    ThirdloginModel mThirdloginmodel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		// 创建授权认证信息
		mWeiboAuth = new WeiboAuth(this, Const.SINA_APP_KEY, Const.SINA_REDIRECT_URL, Const.SCOPE);
	}
	private void initView() {
		accountEt =(EditText) findViewById(R.id.login_et_account);
		accountEt.setText(UserEngine.getLastAccount());
		passwordEt =(EditText) findViewById(R.id.login_et_password);
		
		loginBtn = findViewById(R.id.login_btn_login);
		loginBtn_QQ = findViewById(R.id.login_btn_QQ);
		loginBtn_WeiBo = findViewById(R.id.login_btn_WeiBo);
		retrieveTv = findViewById(R.id.login_tv_retrieve_pwd);
		registerTv = findViewById(R.id.login_tv_register);
		passwordEt.setOnKeyListener(this);
		loginBtn.setOnClickListener(this);
		loginBtn_QQ.setVisibility(View.VISIBLE);
		loginBtn_WeiBo.setVisibility(View.VISIBLE);
		loginBtn_QQ.setOnClickListener(this);
		loginBtn_WeiBo.setOnClickListener(this);
		retrieveTv.setOnClickListener(this);
		registerTv.setOnClickListener(this);
		mThirdloginmodel = new ThirdloginModel();
	}
	@Override
	public void onClick(View v) {
		if(!AppContext.isNetAvailable()){
			Toast.show(R.string.warn_network_unavailable);
			return;
		}
		switch (v.getId()) {
		case R.id.login_btn_login:
			String account = accountEt.getText().toString();
			String password = passwordEt.getText().toString();
			UserEngine.login(account, password,true,new CallBack<Object>(){
				@Override
				public void onCallBack(boolean isSuccess) {
					if(isSuccess){
						//TODO 登录成功向服务器请求密钥，并保存在本地，socket请求连接服务器
						AppContext.startAct(MainActivity.class,true);
					}
				}
			});
			break;
		case R.id.login_btn_WeiBo:
			mSsoHandler = new SsoHandler(LoginActivity.this,mWeiboAuth);
			mSsoHandler.authorize(new SinaAuthListener(LoginActivity.this));
			break;
		case R.id.login_btn_QQ:
			onClickLogin();
			break;
		case R.id.login_tv_register:
			AppContext.startAct(RegPhoneActivity.class);//到手机注册界面
			break;
		case R.id.login_tv_retrieve_pwd:
			AppContext.startAct(RetPhoneActivity.class);//到手机找回密码界面;
			break;
		default:
			break;
		}
	}
	//重写回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// must call mTencent.onActivityResult.	 
        // SSO 授权回调
        // 重要：发起 SSO 登陆的Activity必须重写onActivityResult
   	if (!mTencent.onActivityResult(requestCode, resultCode, data)) {
               if (data != null) {
               }
		}
	if (mSsoHandler != null) {
	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	}
	private void onClickLogin() {
	    Context ctxContext = context.getApplicationContext();
		mTencent = Tencent.createInstance(Const.QQ_APP_KEY, ctxContext);
		L.i("TAG", "mTencent.isSessionValid()  = "+mTencent.isSessionValid());
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				public void onComplete(JSONObject values) {
					try {
					L.i("TAG", "IUiListener listener = new BaseUiListener()"+values.toString());
						userid = values.getString("openid");
						accesstoken = values.getString("access_token");
						L.i("互邀","userid:["+userid+"],accesstoken:[ "+accesstoken+"]");
						mThirdloginmodel.setLoginuserid(userid);
						mThirdloginmodel.setAccesstoken(accesstoken);
						L.i("互邀---","BaseUiListener onComplete["+mThirdloginmodel.getLoginuserid());
						L.i("互邀---","BaseUiListener onComplete["+mThirdloginmodel.getAccesstoken());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					toGetUserInfo();
				}
			};
			mTencent.login(this,"login", listener);
		}
	}
	//获取用户信息
	private void toGetUserInfo() {
	       if (ready()) { 
	           mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
	                   Constants.HTTP_GET,new BaseApiListener("get_simple_userinfo", false), null);
	       }
	   }
	public  boolean ready() {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getOpenId() != null;
		if (!ready)
			Toast.makeText(context, "login and get openId first, please!",Toast.LENGTH_SHORT).show();
		return ready;
	}
	private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(JSONObject response) {
        	L.i("互邀---","BaseUiListener onComplete["+response);
			doComplete(response);
        }
        protected void doComplete(JSONObject values) {
			
		}
		@Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "QQ登录出错了: " + e.errorDetail);
        }
        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "QQ登录已取消.");
        }
    }
    private class BaseApiListener implements IRequestListener {
        private String mScope = "login";
        private Boolean mNeedReAuth = false;

        public BaseApiListener(String scope, boolean needReAuth) {
            mScope = scope;
            mNeedReAuth = needReAuth;
        }
        @Override
        public void onComplete(final JSONObject response, Object state) {
        	//保存用户信息
			keepUserInfo(response);
			doComplete(response, state);
            Util.dismissDialog();
        }
        protected void doComplete(JSONObject response, Object state) {
        	L.i("互邀---","BaseApiListener");
	        try {
	            int ret = response.getInt("ret");
	            L.i("互邀---",""+ret);
	            if (ret == 100030) {
	                if (mNeedReAuth) {
	                    Runnable r = new Runnable() {
	                        public void run() {
	                            mTencent.reAuth(LoginActivity.this,mScope, new BaseUiListener());
	                        }
	                    };
	                    LoginActivity.this.runOnUiThread(r);
	                }
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	            L.e("toddtest", response.toString());
	        }
	
	    }
        @Override
        public void onIOException(final IOException e, Object state) {
            Util.toastMessage(LoginActivity.this, "onIOException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onMalformedURLException(final MalformedURLException e,
                Object state) {
            Util.toastMessage(LoginActivity.this, "onMalformedURLException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onJSONException(final JSONException e, Object state) {
            Util.toastMessage(LoginActivity.this, "onJSONException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e,
                Object arg1) {
            Util.toastMessage(LoginActivity.this, "onConnectTimeoutException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onSocketTimeoutException(SocketTimeoutException e,
                Object arg1) {
            Util.toastMessage(LoginActivity.this, "onSocketTimeoutException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onUnknowException(Exception e, Object arg1) {
            Util.toastMessage(LoginActivity.this, "onUnknowException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onHttpStatusException(HttpStatusException e, Object arg1) {
            Util.toastMessage(LoginActivity.this, "onHttpStatusException: " + e.getMessage());
            Util.dismissDialog();
        }
        @Override
        public void onNetworkUnavailableException(
                NetworkUnavailableException e, Object arg1) {
            Util.toastMessage(LoginActivity.this, "onNetworkUnavailableException: " + e.getMessage());
            Util.dismissDialog();
        }
    }
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		switch (v.getId()) {
		case R.id.login_et_password:
			if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB) {
				onClick(loginBtn);
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
		 //记录用户信息
	private void keepUserInfo(JSONObject response) {
		try {
		L.i("互邀-----","I am in keepuserinfo");
				nickname = response.getString("nickname");
				uImgUrl = response.getString("figureurl_2");
				Editor editor = AppContext.getConfigFile().edit();
				editor.putString(KEY.Usernickname, nickname);
				editor.putString(KEY.Picurl, uImgUrl);
				editor.putString(KEY.Loginuserid,userid );
				editor.putString(KEY.Accesstoken,accesstoken);
				editor.putInt(KEY.Logintype,2);
				editor.commit();//提交修改
				mThirdloginmodel.setUsernickname(nickname);
				mThirdloginmodel.setPicurl(uImgUrl);
				mThirdloginmodel.setLogintype(2);
				L.i("互邀---","QQ   ["+mThirdloginmodel.toString());
				L.i("互邀---","QQ   [");
				ThirdUserManager.getManager().CheckLogin(mThirdloginmodel);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		   }
	@Override
	public String toString() {
		return "登录界面";
	}
}
