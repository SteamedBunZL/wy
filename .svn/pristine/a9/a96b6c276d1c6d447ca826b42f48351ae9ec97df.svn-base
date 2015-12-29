package com.whoyao.engine.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.WebApi;
import com.whoyao.activity.MainActivity;
import com.whoyao.activity.RegPhoneInfoActivity;
import com.whoyao.common.Countdown;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.RegisterModel;
import com.whoyao.model.ResultModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.JSON;

/**
 * 
 * @author hyh 
 * creat_at：2013-7-17-上午11:11:04
 * 
 */
public class RegisterManager {

	private String phoneNumber;
	private LoadingDialog loadingdialog;
	private Countdown countdown;
	private RegisterModel mRegisterModel;
	private static RegisterManager manager;
	
	public static RegisterManager getManager() {
		if(manager == null){
			manager = new RegisterManager();
		}
		return manager;
	}
	
	/**
	 * 检查手机是否被注册
	 * @param phoneNum
	 */
	public void verifyPhone(String phoneNum ,CallBack<String> callBack){
		this.phoneNumber = phoneNum;
		UserEngine.checkAccount(phoneNumber, null, null, new CallBack<CheckAccountResponseModel>(callBack){
			@SuppressWarnings("unchecked")
			@Override
			public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
				if(isSuccess && null != data){
					if(!data.isPhoneRegistered()){
						sendRegisterVCode((CallBack<String>)params[0]);
					}else{
						Toast.show(R.string.warn_phone_registered);
					}
				}
			}
		});	
	}
		
	/**
	 * 发送注册验证码
	 */
	private void sendRegisterVCode(CallBack<String> calBack){
		
		ResponseHandler handler = new CallBackHandler<String>(true,calBack){

			@Override
			public void onSuccess(String content) {
				ResultModel model = JSON.getObject(content, ResultModel.class);
				if(null == model || 3 != model.getResult()){
					doCallBack(true,content);
				}else{
					Toast.show(R.string.warn_phone_over5times);
				}
			}
		};
		UserEngine.SendVerifyCode(phoneNumber, Type.VCode_Register, handler);
	}
	/**
	 * 发送注册验证码
	 */
	public void resendRegisterVCode(Countdown countdown){
		this.countdown = countdown;
		ResponseHandler handler = new ResponseHandler(true){
									
			@Override
			public void onSuccess(int statusCode, String content) {
				ResultModel model = JSON.getObject(content, ResultModel.class);
				if(null == model || 3 != model.getResult()){
					Toast.show(R.string.smscode_resend);
					RegisterManager.this.countdown.start();
				}else{
					Toast.show(R.string.warn_phone_over5times);
				}
			}
		};
		UserEngine.SendVerifyCode(phoneNumber, Type.VCode_Register, handler);
	}
	
	/**
	 * 验证注册码(短信)是否正确
	 * @param phoneNum
	 * @param vcode
	 */
	public void checkRegisterVCode(String vcode){
		
		ResponseHandler handler = new ResponseHandler(true){

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(String content) {
				try {
					JSONObject object = new JSONObject(content);
					int result = (Integer) object.get(KEY.Result);
					switch (result) {
					case 0:
						Toast.show(R.string.warn_code_wrong);
						break;
					case 1:
						AppContext.startAct(RegPhoneInfoActivity.class, true);
						break;
					case 3:
						Toast.show(R.string.warn_code_timeout);
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					AppException.handle(e);
				}
			}
			
		};
		UserEngine.CheckVerifyCode(phoneNumber, vcode, Type.VCode_Register, handler );
	}
	
	public void registerPhone(RegisterModel model){
		model.setUserphone(phoneNumber);
		register(model);
	}
	
	
	/**注册*/
	public void register(RegisterModel model){
		if(checkRegisterInfo(model)){
			mRegisterModel = model;
			UserEngine.checkAccount(null, model.useremail, model.username, new CallBack<CheckAccountResponseModel>(){
				@Override
				public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
					if(isSuccess && null != data){
						if((!data.isEmailRegistered() && !data.isPhoneRegistered() && !data.isNickRegistered())){
							// 可以注册
							Net.request(mRegisterModel, WebApi.User.Register, new ResponseHandler(true){
								@Override
								public void onSuccess(String content) {
									//AppContext.startAct(LoginActivity.class);
									UserEngine.isFirstLogin = true;
									String account = null;
									if(!TextUtils.isEmpty(mRegisterModel.useremail)){
										account = mRegisterModel.useremail;
									}else if(!TextUtils.isEmpty(mRegisterModel.userphone)){
										account = mRegisterModel.userphone;
									}
									if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(mRegisterModel.userpassword)){
										UserEngine.login(account, mRegisterModel.userpassword, true,new CallBack<Object>(){
											@Override
											public void onCallBack(boolean isSuccess) {
												if(isSuccess){
													AppContext.startAct(MainActivity.class,true);
												}
											}
										});
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
							
						}else /*if(data.isEmailRegistered() && data.isNickRegistered()){
							Toast.show("邮箱和昵称已被注册");
						}else */if(data.isEmailRegistered()){
							Toast.show(R.string.warn_email_registered);
						}else if(data.isNickRegistered()){
							Toast.show(R.string.warn_nick_registered);
						}else if(data.isPhoneRegistered()){
							Toast.show(R.string.warn_phone_registered);
						}
					
					}
				}
			});
			
/*			Net.request(model, WebApi.User.Register, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					//AppContext.startAct(LoginActivity.class);
					String account = null;
					if(TextUtils.isEmpty(mRegisterModel.useremail)){
						account = mRegisterModel.useremail;
					}else if(TextUtils.isEmpty(mRegisterModel.userphone)){
						account = mRegisterModel.userphone;
					}
					if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(mRegisterModel.userphone)){
						UserEngine.login(account, mRegisterModel.userphone, true);
					}
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					if(400 == statusCode || 406 == statusCode){
						Toast.show("注册失败");
					}
					super.onFailure(e, statusCode, content);
				}
			});// 注册后的事件
*/		}
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
	
	public void cancleEmailRegDialog(){
		cancleRegDialog("邮箱注册");		
	}
	public void canclePhoneRegDialog(){
		cancleRegDialog("手机注册");
	}
	private void cancleRegDialog(String title){
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppContext.curActivity.finish();
				recycle();
			}
		};
		MessageDialog cancleDialog = new MessageDialog(AppContext.curActivity);
		cancleDialog.setTitle(title);
		cancleDialog.setMessage("确定要退出本次注册吗？");
		cancleDialog.setLeftButton("确定", listener);
		cancleDialog.setRightButton("取消", null);
		cancleDialog.show();
	}

	public boolean checkRegisterInfo(RegisterModel model) {
		//检查是否为空
		if(TextUtils.isEmpty(model.getUserpassword())){
			Toast.show(R.string.warn_pwd_empty);
			return false;
		}
		if(TextUtils.isEmpty(model.getUsername())){
			Toast.show(R.string.warn_nick_empty);
			return false;
		}
		if(0 == model.getUsersex()){//检查性别
			Toast.show(R.string.hint_select_sex);
			return false;
		}
		if(TextUtils.isEmpty(model.getUserbirthday())){//检查生日
			Toast.show(R.string.warn_birthday_empty);
			return false;
		}
		if(0 == model.getUserprovince() || 0 == model.getUsercity() || 0 == model.getUserDistrict()){//检查所在地
			Toast.show(R.string.hint_select_addr);
			return false;
		}
		//检查格式是否正确
		if(!TextUtils.isEmpty(model.getUseremail()) && !FormatUtils.isEmailAddr(model.getUseremail())){
			Toast.show(R.string.warn_email_formatwrong);
			return false;
		}
		if(!FormatUtils.isPwdComplex(model.getUserpassword())){
			Toast.show(R.string.warn_pwd_formatwrong);
			return false;
		}
		if(!UserEngine.checkNickFormat(model.getUsername())){//检查昵称格式
			return false;
		}
		return true;
	}	
}
