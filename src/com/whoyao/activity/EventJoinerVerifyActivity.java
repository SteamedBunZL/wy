package com.whoyao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.Countdown;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.VerifyManager;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

/**
 * 参加动前认证手机
 * 
 * @author hyh creat_at：2013-8-5-下午7:04:54
 */
public class EventJoinerVerifyActivity extends BasicActivity implements OnKeyListener, OnClickListener {

	private EditText etPhone;
	private Button btnSend;
	private TextView tvStep0;
	private TextView tvStep1;
	private int step = 0;
	private String phoneNum;
	private String codeNum;
	
	protected OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			NetCache.clearCaches();
			MyinfoManager.getManager().getMyDetil(true,new CallBack<UserDetialModel>(){
				public void onCallBack(boolean isSuccess) {
					finish();
				};
			});
		}
	};
	private Button btnResend;
	private Countdown countdown;
	private ResponseHandler handler;
//	private RequestParams params;
	private EditText etRealname;
	private String realName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_join_verify);
		initView();
	}

	private void initView() {
		etRealname = (EditText) findViewById(R.id.event_verify_et_realname);
		etPhone = (EditText) findViewById(R.id.event_verify_et_phone);
		tvStep0 = (TextView) findViewById(R.id.event_verify_tv_step0);
		tvStep1 = (TextView) findViewById(R.id.event_verify_tv_step1);
		btnSend = (Button) findViewById(R.id.event_verify_btn_sendcode);
		btnResend = (Button)findViewById(R.id.event_verify_btn_resendcode);
		etPhone.setOnKeyListener(this);
		btnSend.setOnClickListener(this);
		btnResend.setOnClickListener(this);
		
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		countdown = new Countdown(59, btnResend, "秒后重新获取");
		
		if(null != MyinfoManager.getUserInfo() && !TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserRelName())){
			etRealname.setText(MyinfoManager.getUserInfo().getUserRelName());
			if(0 != MyinfoManager.getUserInfo().getUserHonestyState()){
				etRealname.setEnabled(false);
				etRealname.setFocusable(false);
			}
		}
		if(null != MyinfoManager.getUserInfo() && !TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserPhone())){
			etPhone.setText(MyinfoManager.getUserInfo().getUserPhone());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_verify_btn_resendcode:
			VerifyManager.getManager().sendVCode(phoneNum,Type.VCode_Verify, new CallBack<String>(){
				@Override
				public void onCallBack() {
					countdown.start();
				}
			});
			break;
		case R.id.event_verify_btn_sendcode:
			switch (step) {
			case 0:
				phoneNum = etPhone.getText().toString();
				realName = etRealname.getText().toString();
				if (UserEngine.checkPhoneFormat(phoneNum)) {
					if(TextUtils.isEmpty(realName) || 2 > Utils.getStringLength(realName)){
						Toast.show(R.string.warn_realname_empty);
						return;
					}
					VerifyManager.getManager().verifyPhone(phoneNum, new CallBack<CheckAccountResponseModel>(){
						@Override
						public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
							if(isSuccess && null != data){
								if(!data.isPhoneRegistered() || phoneNum.equals(MyinfoManager.getUserInfo().getUserPhone())){
									VerifyManager.getManager().sendVCode(phoneNum,Type.VCode_Verify, new CallBack<String>(){
										@Override
										public void onCallBack() {
											setState1();
										}
									});
								}else{
									Toast.show(R.string.warn_phone_registered);					
								}
							}
						}
					});
				}
				break;
			case 1:
				codeNum = etPhone.getText().toString();
				if (codeNum.length() == Const.CodeLenght_SMS) {
					RequestParams params = new RequestParams();
					params.put(KEY.Phone, phoneNum);
					params.put(KEY.Type, Type.VCode_Verify);
					params.put(KEY.VerifyCode, codeNum);
					params.put(KEY.User_ID, MyinfoManager.getUserInfo().getUserID()+"");
					
					Net.request(params, WebApi.User.CheckVerifyCode, new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {// 验证成功
							ResultModel result = JSON.getObject(content, ResultModel.class);
							switch (result.getResult()) {
							case 0:
								Toast.show(R.string.warn_code_wrong);
								break;
							case 1:
								RequestParams params = new RequestParams();
								params.put(KEY.Phone, phoneNum);
								params.put(KEY.RealName, realName);
								handler = new ResponseHandler(true) {
									@Override
									public void onSuccess(String content) {
										MyinfoManager.getManager().getMyDetil(true,new CallBack<UserDetialModel>(){
											public void onCallBack(boolean isSuccess) {
												finish();
											};
										});
									}
								};
								Net.request(params, WebApi.Event.ValidateCode, handler);
								break;
							case 2:
								Toast.show(R.string.warn_code_timeout);
								break;
							default:
								break;
							}								
						
						}
					});
				}else{
					Toast.show(R.string.warn_code_formatwrong);
				}
				break;
			default:
				break;

			}
			break;
		default:
			break;
		}
	}

	protected void setState0() {
		step = 0;
		tvStep0.setVisibility(View.VISIBLE);
		tvStep1.setVisibility(View.GONE);
		etRealname.setVisibility(View.VISIBLE);
		etPhone.setHint("请输入手机号码");
		etPhone.setText("");
		btnSend.setText("向我发送验证码/下一步");
		btnResend.setVisibility(View.GONE);// 隐藏倒计时
	}
	protected void setState1() {
		step = 1;
		tvStep0.setVisibility(View.GONE);
		tvStep1.setVisibility(View.VISIBLE);
		etRealname.setVisibility(View.GONE);
		etPhone.setHint("请输入验证码");
		etPhone.setText("");
		btnSend.setText("下一步");
		// 显示倒计时
		btnResend.setVisibility(View.VISIBLE);
		countdown.start();
	}



	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			onClick(btnSend);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Activity joiner validate";
	}
}
