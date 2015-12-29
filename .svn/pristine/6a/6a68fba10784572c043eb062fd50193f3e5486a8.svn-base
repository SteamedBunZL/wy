package com.whoyao.fund;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.common.Countdown;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.RegisterManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.widget.ClearEditText;
import com.whoyao.widget.XRTextView;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 认证手机获取验证码
 * 
 * @author hyh creat_at：2013-7-17-下午7:04:54
 *         <p>
 * 
 */
public class GetPhoneCodeActivity extends BasicActivity implements
		OnClickListener {
	// "手机注册-2";
	private EditText codeEt;
	private View backBtn;
	private View nextBtn;
	private Countdown countdown;
	private Button btnGetCode;
	private TextView tvTitle;
	private com.whoyao.widget.ClearEditText etPhone;
	private com.whoyao.widget.XRTextView tvPhone;
	private CheckCodeTModel tModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_fund_phone_checkcode);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(MyinfoManager.IsSetPass ? "修改支付密码" : "设置支付密码");
		initView();
	}

	private void initView() {
		tModel = new CheckCodeTModel();
		tvPhone = (XRTextView) findViewById(R.id.regpcc_tv_inputcode);
		tvPhone.setText("已认证手机：" + MyinfoManager.getUserInfo().getUserPhone());
		btnGetCode = (Button) findViewById(R.id.btn_get_code);
		btnGetCode.setOnClickListener(this);
		codeEt = (EditText) findViewById(R.id.regpcc_et_code);
		etPhone = (ClearEditText) findViewById(R.id.input_et_phone);
		tvPhone.setVisibility(MyinfoManager.getUserInfo().isMobileV() ? View.VISIBLE
				: View.GONE);
		etPhone.setVisibility(MyinfoManager.getUserInfo().isMobileV() ? View.GONE
				: View.VISIBLE);
		// codeEt.setInputType(InputType.TYPE_CLASS_NUMBER);
		// codeEt.setImeOptions(EditorInfo.IME_ACTION_GO);
		// codeEt.setHint("请输入验证码");
		backBtn = findViewById(R.id.regpcc_btn_back);
		nextBtn = findViewById(R.id.regpcc_btn_next);
		backBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		countdown = new Countdown(59, btnGetCode, "秒后重新获取");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_code:
			countdown.start();
			Net.request("phone",
					MyinfoManager.getUserInfo().isMobileV() ? MyinfoManager
							.getUserInfo().getUserPhone() : etPhone.getText()
							.toString().trim(), WebApi.User.GetPhoneCode,
					new ResponseHandler() {

						@Override
						public void onSuccess(String content) {
							try {
								JSONObject json = new JSONObject(content);
								int result = (Integer) json.get("result");
								Toast.show(result == 0 ? "获取成功" : "获取失败");

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
						}

					});
			break;
		case R.id.regpcc_btn_back:
			finish();
			break;
		case R.id.regpcc_btn_next:
			String vcode = codeEt.getText().toString();
			if (UserEngine.checkSmsCodeFormat(vcode)) {
				// RegisterManager.getManager().checkRegisterVCode(vcode);
				tModel.setCode(codeEt.getText().toString().trim());
				tModel.setPhone(MyinfoManager.getUserInfo().isMobileV() ? MyinfoManager
						.getUserInfo().getUserPhone() : etPhone.getText()
						.toString().trim());
				Net.request(tModel, WebApi.User.CheckPhoneCode,
						new ResponseHandler() {
							@Override
							public void onSuccess(String content) {
								// TODO Auto-generated method stub
								super.onSuccess(content);
								JSONObject json;
								try {
									json = new JSONObject(content);
									int result = (Integer) json.get("result");
									if (result==0) {
										AppContext.startAct(SetOrChangePasswordActivity.class);
										finish();
									}else {
										Toast.show("验证失败");
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							@Override
							public void onFailure(Throwable e, int statusCode,
									String content) {
								// TODO Auto-generated method stub
								super.onFailure(e, statusCode, content);
							}
						});
			}
			break;
		case R.id.regpcc_btn_resendcode:
			// RegisterManager.getManager().resendRegisterVCode(countdown);
			break;
		default:
			break;
		}
	}
	// @Override
	// protected boolean onBack() {
	// // 返回时提醒
	// RegisterManager.getManager().canclePhoneRegDialog();
	// return true;
	// }

	@Override
	public String toString() {
		return "手机注册-2";
	}

	private class CheckCodeTModel {
		private String phone;
		private String code;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}
}
