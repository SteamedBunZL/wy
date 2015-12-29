package com.whoyao.engine.user;

import java.io.File;
import java.io.FileNotFoundException;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.CheckAccountTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.JSON;

/**
 * @author hyh creat_at：2013-8-15-下午7:26:36
 */
public class VerifyManager {
	private static VerifyManager manager;

	public static synchronized VerifyManager getManager() {
		if (null == manager) {
			manager = new VerifyManager();
		}
		return manager;
	}

	private String nameStr;
	private String birthdayStr;
	private String idnumStr;
	private String phoneNumber;
	/**检查实名认证信息*/
	public boolean CheckHonestyInfo(String nameStr, String birthdayStr, String idnumStr) {
		if (TextUtils.isEmpty(nameStr)) {
			Toast.show(R.string.user_warn_name_empty);
			return false;
		}
		if (TextUtils.isEmpty(birthdayStr)) {
			Toast.show(R.string.user_warn_birthday_empty);
			return false;
		}
		if (TextUtils.isEmpty(idnumStr)) {
			Toast.show(R.string.user_warn_id_empty);
			return false;
		}
		if (!FormatUtils.isChineseOnly(nameStr) ||2 > nameStr.length() || 8 < nameStr.length()) {
			Toast.show(R.string.user_warn_realname_wrong);
			return false;
		}
		if (!FormatUtils.isIDCard(idnumStr)) {
			Toast.show(R.string.user_warn_id_wrong);
			return false;
		}


		this.nameStr = nameStr;
		this.birthdayStr = birthdayStr;
		this.idnumStr = idnumStr;
		return true;
	}
	/**实名认证*/
	public void verifyHonesty(String photoPath) {
		RequestParams params = new RequestParams();
		params.put("userrelname", nameStr);
		params.put("birthday", birthdayStr);
		params.put("usercode", idnumStr);
		File file = new File(photoPath);
		if (file.length()>2*1024*1024) {
			Toast.show(R.string.uploading_fail_big);
		}else {
			try {
				params.put("image", new File(photoPath));
			} catch (FileNotFoundException e) {
				AppException.handle(e);
			}
			Net.request(params , WebApi.User.UpHonestyInfo, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					NetCache.clearCaches();
					MyinfoManager.getManager().getMyDetil(true,new CallBack<UserDetialModel>(){
						@Override
						public void onCallBack() {
							Toast.show(R.string.uploading_success);
//							AppContext.startAct(MyMoreinfoActivity.class,true);
							AppContext.curActivity.finish();
						}
					});
				}
			});
			AppContext.curActivity.finish();}
		}
	
	/**
	 * 检查手机是否被注册
	 * @param phoneNum
	 */
	public void verifyPhone(String phoneNum ,CallBack<CheckAccountResponseModel> callBack){
		
		this.phoneNumber = phoneNum;
		
		ResponseHandler handler = new CallBackHandler<CheckAccountResponseModel>(false,callBack){

			@Override
			public void onSuccess(int statusCode, String content) {
				CheckAccountResponseModel model = JSON.getObject(content, CheckAccountResponseModel.class);
				doCallBack(true,model);
				super.onSuccess(statusCode, content);
			}
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				doCallBack(false, null);
				super.onFailure(e, statusCode, content);
			}
		};
		CheckAccountTModel checkAccountModel = new CheckAccountTModel(phoneNum, null, null);
		Net.request(checkAccountModel, WebApi.User.CheckAccount, handler );
	}
	/**
	 * 发送注册验证码
	 */
	public void sendVCode(String phoneNumber, String type, CallBack<String> calBack){
		
		ResponseHandler handler = new CallBackHandler<String>(true,calBack){
			@Override
			public void onSuccess(int statusCode, String content) {
				ResultModel model = JSON.getObject(content, ResultModel.class);
				if(null == model || 3 != model.getResult()){
					doCallBack(true,content);
				}else{
					Toast.show(R.string.warn_phone_over5times);
				}
			}			
		};
		UserEngine.SendVerifyCode(phoneNumber, type, handler);
	}
	
	
	/**
	 * 验证注册码(短信)是否正确
	 * @param phoneNum
	 * @param vcode
	 */
	public void checkRegisterVCode(String type, String vcode, CallBack<String> calBack){
		
		ResponseHandler handler = new CallBackHandler<String>(true,calBack){
			@Override
			public void onSuccess(String content) {
				doCallBack(true,content);
			}
		};
		UserEngine.CheckVerifyCode(phoneNumber, vcode, type, handler );
	}
	
}
