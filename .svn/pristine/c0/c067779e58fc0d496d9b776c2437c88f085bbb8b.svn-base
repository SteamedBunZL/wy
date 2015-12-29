package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.Countdown;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 认证手机
 * 
 * @author hyh creat_at：2013-8-5-下午7:04:54
 */
public class MyVerifyPhoneActivity extends BasicActivity implements OnKeyListener, OnClickListener {

	private EditText phoneEt;
	private Button sendBtn;
	private TextView step1Tv;
	private TextView step2Tv;
	private int step = 0;
	private String phoneNum;
	private String codeNum;

	protected OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			NetCache.clearCaches();
			MyinfoManager.getManager().getMyDetil(true, new CallBack<UserDetialModel>(){
				public void onCallBack(boolean isSuccess, UserDetialModel data) {
					finish();
				};
			});
		}
	};
	private Button resendBtn;
	private Countdown countdown;
	private ResponseHandler handler;
	private RequestParams requestParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_verify_phone);
		initView();
	}

	private void initView() {
		phoneEt = (EditText) findViewById(R.id.verifyphone_et_phone);
		sendBtn = (Button) findViewById(R.id.verifyphone_btn_sendcode);
		step1Tv = (TextView) findViewById(R.id.verifyphone_tv_step1);
		step2Tv = (TextView) findViewById(R.id.verifyphone_tv_step2);
		resendBtn = (Button) findViewById(R.id.verifyphone_btn_resendcode);
		phoneEt.setOnKeyListener(this);
		sendBtn.setOnClickListener(this);
		resendBtn.setOnClickListener(this);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		countdown = new Countdown(59, resendBtn, "秒后重新获取");
		if (MyinfoManager.getUserInfo().isMobileV()) {
			((TextView) findViewById(R.id.page_tv_title)).setText("修改手机号");
		} else if (!TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserPhone())) {
			phoneEt.setText(MyinfoManager.getUserInfo().getUserPhone());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.verifyphone_btn_resendcode:
			Net.request(requestParams, WebApi.User.SendVerifyCode, handler);
			// countdown.start();
			break;
		case R.id.verifyphone_btn_sendcode:
			switch (step) {
			case 0:
				phoneNum = phoneEt.getText().toString();
				if (UserEngine.checkPhoneFormat(phoneNum)) {
					if(phoneNum.equals(MyinfoManager.getUserInfo().getUserPhone())){
						if(MyinfoManager.getUserInfo().isMobileV()){
							Toast.show(R.string.warn_phone_duplicate);
						}else{
							requestParams = new RequestParams();
							requestParams.put(KEY.Phone, phoneNum);
							requestParams.put(KEY.Type, Type.VCode_Verify);
							handler = new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									ResultModel model = JSON.getObject(content, ResultModel.class);
									if(null == model || 3 != model.getResult()){
										setVerifyCodeState();
									}else{
										Toast.show(R.string.warn_phone_over5times);
									}
								}
							};
							Net.request(requestParams, WebApi.User.SendVerifyCode, handler);
						}
					}else{
						UserEngine.checkAccount(phoneNum, null, null, new CallBack<CheckAccountResponseModel>(){
							@Override
							public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
								if(isSuccess && null != data){
									if(!data.isPhoneRegistered()){
										requestParams = new RequestParams();
										requestParams.put(KEY.Phone, phoneNum);
										requestParams.put(KEY.Type, Type.VCode_Verify);
										handler = new ResponseHandler(true) {
											@Override
											public void onSuccess(String content) {
												ResultModel model = JSON.getObject(content, ResultModel.class);
												if(null == model || 3 != model.getResult()){
													setVerifyCodeState();
												}else{
													Toast.show(R.string.warn_phone_over5times);
												}
											}
										};
										Net.request(requestParams, WebApi.User.SendVerifyCode, handler);
									}else{
										Toast.show(R.string.warn_phone_registered);
									}
								}
							}
						});
					}

				}
				break;
			case 1:
				codeNum = phoneEt.getText().toString();
				if (!UserEngine.checkSmsCodeFormat(codeNum)) {
					return;
				}
			
				RequestParams params = new RequestParams();
				params.put(KEY.Phone, phoneNum);
				params.put(KEY.Type, Type.VCode_Verify);
				params.put(KEY.VerifyCode, codeNum);
				params.put(KEY.User_ID, MyinfoManager.getUserInfo().getUserID() + "");

				Net.request(params, WebApi.User.CheckVerifyCode, new ResponseHandler(true) {
					@Override
					public void onSuccess(String content) {
						// 验证成功
						ResultModel result = JSON.getObject(content, ResultModel.class);
						if(result != null){
							switch (result.getResult()) {
							case 0:
								Toast.show(R.string.warn_code_wrong);
								break;
							case 1:
								MessageDialog dialog = new MessageDialog(MyVerifyPhoneActivity.this);
								dialog.setTitle("认证手机号");
								dialog.setMessage("恭喜您，认证成功！");
								dialog.setLeftButton("确定", listener);
								dialog.show();
								break;
							case 2:
								Toast.show(R.string.warn_code_timeout);
								break;
							default:
								break;
							}
							
						}else{
							Toast.show(R.string.warn_netrequest_failure);
						}
					}
				});

				break;
			default:
				break;

			}

			break;
		default:
			break;
		}
	}

	protected void setVerifyCodeState() {
		step = 1;
		step1Tv.setTextColor(getResources().getColor(R.color.gray_text));
		step2Tv.setTextColor(getResources().getColor(android.R.color.black));
		phoneEt.setHint("请输入验证码");
		phoneEt.setText("");
		sendBtn.setText("下一步");
		// 显示倒计时
		resendBtn.setVisibility(View.VISIBLE);
		countdown.start();

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			onClick(sendBtn);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "认证手机";
	}
}
