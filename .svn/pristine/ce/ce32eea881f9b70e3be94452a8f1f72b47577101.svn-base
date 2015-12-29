package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.Const.Type;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.Countdown;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.VerifyManager;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.content.Intent;
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
 * 发活动前认证手机
 * 
 * @author hyh creat_at：2013-8-5-下午7:04:54
 */
public class VerifyNameAndPhoneActivity extends BasicActivity implements OnKeyListener, OnClickListener {


	private EditText etPhone;
	private Button btnSend;
	private TextView tvStep0;
	private TextView tvStep1;
	private int step = 0;
	private String phoneNum;
	private String codeNum;

	private Button btnResend;
	private Countdown countdown;
	private ResponseHandler handler;
	private EditText etRealname;
	private String realName;
	private View vRealname;
	private int state;
	private View vPhone;
	private EditText etCode;
	private Intent intent;
	private int id;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		state = intent.getIntExtra(Extra.VerifyState, 0);
		id = intent.getIntExtra(Extra.SelectedID, 0);
		if (state == 0) {
			finish();
			return;
		}
		if (state != State.Verify_EventCreater && id == 0) {
			finish();
			return;
		}
		setContentView(R.layout.activity_verify_name_phone);
		initView();
	}

	private void initView() {
		tvTitle = (TextView)findViewById(R.id.page_tv_title);
		vRealname = findViewById(R.id.event_verify_ll_realname);
		etRealname = (EditText) findViewById(R.id.event_verify_et_realname);
		vPhone = findViewById(R.id.event_verify_ll_phone);
		etPhone = (EditText) findViewById(R.id.event_verify_et_phone);
		etCode = (EditText) findViewById(R.id.event_verify_et_code);
		tvStep0 = (TextView) findViewById(R.id.event_verify_tv_step0);
		tvStep1 = (TextView) findViewById(R.id.event_verify_tv_step1);
		btnSend = (Button) findViewById(R.id.event_verify_btn_sendcode);
		btnResend = (Button) findViewById(R.id.event_verify_btn_resendcode);
//		 <!-- 活动发起人需验证手机，请务必填写真实资料。 -->
//		    <!-- 邀请发起人需验证手机，请务必填写真实资料。 -->
//		    <!-- 参与需要验证身份，请务必填写真实资料 -->
//		    <!-- 请填写真实资料，方便活动时联系哦~ -->
		switch (state) {
		case State.Verify_EventCreater:
			break;
		case State.Verify_EventJoiner:
			tvTitle.setText("我要报名");
			tvStep0.setText("请填写真实资料，方便活动时联系哦~");
			break;
		case State.Verify_InviteCreater:
			tvStep0.setText("邀请发起人需验证手机，请务必填写真实资料");
			break;
		default:
			break;
		}
		
		etPhone.setOnKeyListener(this);
		btnSend.setOnClickListener(this);
		btnResend.setOnClickListener(this);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		countdown = new Countdown(59, btnResend, "秒后重新获取");

		if (null != MyinfoManager.getUserInfo() && !TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserRelName())) {
			etRealname.setText(MyinfoManager.getUserInfo().getUserRelName());
			int honestyState = MyinfoManager.getUserInfo().getUserHonestyState();
			if (1 == honestyState || 2 == honestyState) {
				etRealname.setEnabled(false);
				etRealname.setFocusable(false);
			}
		}
		if (null != MyinfoManager.getUserInfo() && !TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserPhone())) {
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
			VerifyManager.getManager().sendVCode(phoneNum, Type.VCode_Verify, new CallBack<String>() {
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
				if (TextUtils.isEmpty(realName) || 2 > Utils.getStringLength(realName)) {
					Toast.show(R.string.warn_realname_empty);
					return;
				}
				if (UserEngine.checkPhoneFormat(phoneNum)) {
					VerifyManager.getManager().verifyPhone(phoneNum, new CallBack<CheckAccountResponseModel>() {
						@Override
						public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
							if(isSuccess && null != data){
								if(!data.isPhoneRegistered() || phoneNum.equals(MyinfoManager.getUserInfo().getUserPhone())){
									VerifyManager.getManager().sendVCode(phoneNum, Type.VCode_Verify, new CallBack<String>() {
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
					// RegisterManager.getManager().verifyPhone(phoneNum, new
					// CallBack<String>(){
					// @Override
					// public void onCallBack() {
					// setState1();
					// }
					// });
				}
				break;
			case 1:
				codeNum = etCode.getText().toString();
				if (UserEngine.checkSmsCodeFormat(codeNum)) {
					RequestParams params = new RequestParams();
					params.put(KEY.Phone, phoneNum);
					params.put(KEY.Type, Type.VCode_Verify);
					params.put(KEY.VerifyCode, codeNum);
					params.put(KEY.User_ID, MyinfoManager.getUserInfo().getUserID() + "");

					Net.request(params, WebApi.User.CheckVerifyCode, new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {
							// 验证成功
							ResultModel model = JSON.getObject(content, ResultModel.class);
							if(null == model){
								Toast.show(R.string.warn_netrequest_failure);
								return;
							}
							switch (model.getResult()) {
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
										switch (state) {
										case State.Verify_EventCreater:
											intent.setClass(AppContext.curActivity, EventAddGuideActivity.class);
											startActivity(intent);
											break;
										case State.Verify_EventJoiner:
											EventEngine.join(id,new CallBack<String>(){
												public void onCallBack(boolean isSuccess, String data) {
													if(isSuccess && null != data){
														EventDetialManager.getInstance().getDetial(id, new CallBack<String>());
													}
												};
											});
											break;
										case State.Verify_InviteCreater:
											intent.setClass(AppContext.curActivity, InvitePubliseActivity.class);
											startActivity(intent);
											break;
										default:
											break;
										}
										MyinfoManager.getManager().getMyDetil(true,new CallBack<UserDetialModel>());
										finish();
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
		vRealname.setVisibility(View.VISIBLE);
		vPhone.setVisibility(View.VISIBLE);
		etCode.setVisibility(View.GONE);
		btnSend.setText("向我发送验证码");
		btnResend.setVisibility(View.GONE);// 隐藏倒计时
	}

	protected void setState1() {
		step = 1;
		tvStep0.setVisibility(View.GONE);
		tvStep1.setVisibility(View.VISIBLE);
		vRealname.setVisibility(View.GONE);
		vPhone.setVisibility(View.GONE);
		etCode.setVisibility(View.VISIBLE);
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
	protected boolean onBack() {
		if (1 == step) {
			setState0();
		} else {
			finish();
		}
		return true;
	}

	@Override
	public String toString() {
		return "Activity creater validate";
	}
}
