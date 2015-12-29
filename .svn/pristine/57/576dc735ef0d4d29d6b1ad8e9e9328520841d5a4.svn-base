package com.whoyao.engine.user;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.LoginActivity;
import com.whoyao.activity.RetPhoneCheckCodeActivity;
import com.whoyao.activity.RetPhoneSetPwdActivity;
import com.whoyao.common.Countdown;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.CheckAccountTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;

/**
 * 
 * @author hyh creat_at：2013-7-22-下午4:28:16
 */
public class RetrievePwdManager {

	private String phoneNumber;
	private Countdown mCountdown;
	private static RetrievePwdManager RetrievePwdManager;

	public static RetrievePwdManager getManager() {
		if (RetrievePwdManager == null) {
			RetrievePwdManager = new RetrievePwdManager();
		}
		return RetrievePwdManager;
	}

	/**
	 * 发送注册验证码
	 */
	public void sendRetPwdVCode(String phoneNum) {
		if (!UserEngine.checkPhoneFormat(phoneNum)) {
			return;
		}
		phoneNumber = phoneNum;
		CheckAccountTModel checkAccountModel = new CheckAccountTModel(phoneNum, null, null);
		Net.request(checkAccountModel, WebApi.User.CheckAccount, new ResponseHandler(true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				CheckAccountResponseModel model = JSON.getObject(content, CheckAccountResponseModel.class);
				if (model.isPhoneRegistered()) {
					UserEngine.SendVerifyCode(phoneNumber, Type.VCode_RetPwd, new ResponseHandler(true) {
						@Override
						public void onFailure(Throwable e, int statusCode, String content) {
							super.onFailure(e, statusCode, content);
							if (400 == statusCode) {
								Toast.show(R.string.warn_phone_unregister);
							}
						}
						@Override
						public void onSuccess(String content) {
							ResultModel model = JSON.getObject(content, ResultModel.class);
							if(null == model || 3 != model.getResult()){
								AppContext.startAct(RetPhoneCheckCodeActivity.class, true);
							}else{
								Toast.show(R.string.warn_phone_over5times);
							}
						}
					});
				} else {
					Toast.show(R.string.warn_phone_unregister);
				}
			}
		});

	}

	/**
	 * 发送注册验证码
	 */
	public void resendRetPwdVCode(Countdown countdown) {
		this.mCountdown = countdown;
		ResponseHandler handler = new ResponseHandler(true) {
			@Override
			public void onSuccess(String content) {
				ResultModel model = JSON.getObject(content, ResultModel.class);
				if(null == model || 3 != model.getResult()){
					Toast.show(R.string.smscode_resend);
					mCountdown.start();
				}else{
					Toast.show(R.string.warn_phone_over5times);
				}
			}
		};
		UserEngine.SendVerifyCode(phoneNumber, Type.VCode_RetPwd, handler);
	}

	/**
	 * 验证注册码(短信)是否正确
	 * 
	 * @param phoneNum
	 * @param vcode
	 */
	public void checkRetPwdVCode(String vcode) {

		ResponseHandler handler = new ResponseHandler(true) {

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				if (400 == statusCode) {
					Toast.show(R.string.warn_code_wrong);
				}
				super.onFailure(e, statusCode, content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				ResultModel model = JSON.getObject(content, ResultModel.class);
				switch (model.getResult()) {
				case 0:
					Toast.show(R.string.warn_code_wrong);
					break;
				case 1:
					AppContext.startAct(RetPhoneSetPwdActivity.class, true);
					break;
				case 2:
					Toast.show(R.string.warn_code_timeout);
					break;
				default:
					break;
				}
			}

		};
		UserEngine.CheckVerifyCode(phoneNumber, vcode, Type.VCode_RetPwd, handler);
	}

	// public void registerPhone(String password, String nickname ,AddrModel
	// addr){
	// RegisterModel info = new RegisterModel();
	// info.setUserphone(phoneNumber);
	// info.setUsername(nickname);
	// info.setUserpassword(password);
	// info.setUsercounty(addr.getUsercounty());
	// info.setUsercity(addr.getUsercounty());
	// info.setUserprovince(addr.getUserprovince());
	// UserEngine.register(info);
	// }

	public void retrievePwdByEmail(String email) {
		if (UserEngine.checkEmailFormat(email)) {
			RequestParams params = new RequestParams();
			params.put(KEY.Type, Type.Email_RetPwd);
			params.put(KEY.Email, email);
			Net.request(params, WebApi.User.ResetPwdEmail, new RetPwdEmailHandler(email));
		}
	}

	public class RetPwdEmailHandler extends ResponseHandler {
		String email;
		private String emailUrl;
		private MessageDialog dialog;

		public RetPwdEmailHandler(String email) {
			super(true);
			this.email = email;
		}

		@Override
		public void onSuccess(String content) {
			ResultModel result = JSON.getObject(content, ResultModel.class);
			if(2 == result.getResult()){
				Toast.show(R.string.warn_email_over5times);
				return;
			}
			
			String domain = email.substring(email.indexOf("@")+1);
			if ("gmail.com".equals(domain)) {
				emailUrl = "http://www.gmail.com";
			} else {
				emailUrl = "http://mail." + domain;
			}
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 发Intent打开连接
					AppContext.startAct(LoginActivity.class,true);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(emailUrl));
					AppContext.startAct(intent);
					dialog.dismiss();
					recycle();
				}
			};
			dialog = new MessageDialog(AppContext.curActivity);
			dialog.setTitle("邮箱找回密码");
			dialog.setMessage("重置链接已发送到您的邮箱，请进入邮箱点击连接。");
			dialog.setLeftButton("确定", listener);
			dialog.show();
		}

		@Override
		public void onFailure(Throwable e, int statusCode, String content) {
			if (400 == statusCode) {
				Toast.show("您的邮箱未注册，请检查是否拼写错误，如需注册，请点击注册");
			}
			super.onFailure(e, statusCode, content);

		}
	}

	public void recycle() {
		RetrievePwdManager = null;
	}

	public void upPassword(String password) {
		if (UserEngine.checkPasswordFormat(password)) {
			RequestParams params = new RequestParams();
			params.put(KEY.Phone, phoneNumber);
			params.put(KEY.ResetPassword, password);
			Net.request(params, WebApi.User.RetrievePwdByPhone, new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					Toast.show(R.string.warn_pwd_reset_success);
					AppContext.startAct(LoginActivity.class,true);
				}
			});
		}

	}
}
