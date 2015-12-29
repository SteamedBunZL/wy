package com.whoyao.fund;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.MyFundActivity;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.venue.OrderConfirmActivity;

public class SetOrChangePasswordActivity extends BasicActivity implements
		OnClickListener {
	private TextView tvTitle;
	private TextView tvPassword;
	private EditText etPassword;
	private Button btnBack;
	private Button btnConfim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setorchangepassword);
		initView();
	}
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass) ? "修改密码" : "设置密码");
		Log.i("互邀+++!!!!!@@",(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass)? "修改密码" : "设置密码").toString());
		btnBack = (Button) findViewById(R.id.regpcc_btn_back);
		btnConfim = (Button) findViewById(R.id.regpcc_btn_next);
		tvPassword = (TextView) findViewById(R.id.tv_setPassword);
		tvPassword.setText(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass) ? "新密码：" : "输入密码：");
		etPassword = (EditText) findViewById(R.id.regpcc_et_code);
		btnBack.setOnClickListener(this);
		btnConfim.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regpcc_btn_back:
			finish();
			break;
		case R.id.regpcc_btn_next:
			if((MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass)? "修改密码" : "设置密码").equals("修改密码")){
				AppContext.startAct(MyFundActivity.class);	
			}else{
				Net.request("newpaypassword", etPassword.getText().toString()
						.trim(), WebApi.User.SetPassword, new ResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						Toast.show("设置成功!");
						MyinfoManager.getUserConfigFile().edit().putBoolean("IsSetPass", true);
						Log.i("互邀-------@",""+MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass));
						AppContext.startAct(OrderConfirmActivity.class);
					}
					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
					}
					
				});
			}
			break;
		}

	}

}
