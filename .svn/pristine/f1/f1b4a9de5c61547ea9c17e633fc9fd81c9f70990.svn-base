package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.RetrievePwdManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.ui.Toast;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

/**
 * 邮箱找回密码
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 *
 */
public class RetEmailActivity extends BasicActivity implements OnKeyListener, OnClickListener{

	private EditText emailEt;
	private View backBtn;
	private View retPhoneBtn;
	private View sendEmailBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_email);
		initView();
	}

	private void initView() {
		emailEt = (EditText) findViewById(R.id.rete_et_email);
		backBtn = findViewById(R.id.rete_btn_back);
		sendEmailBtn = findViewById(R.id.rete_btn_sendemail);
		retPhoneBtn = findViewById(R.id.rete_btn_phone);
		
		emailEt.setOnKeyListener(this);
		
		backBtn.setOnClickListener(this);
		sendEmailBtn.setOnClickListener(this);
		retPhoneBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rete_btn_back:
			finish();
			break;
		case R.id.rete_btn_sendemail:
			String email = emailEt.getText().toString();
			if(!UserEngine.checkEmailFormat(email)){
				return;
			}
			UserEngine.checkAccount(null, email, null,new CallBack<CheckAccountResponseModel>(email){
				@Override
				public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
					if(isSuccess && null != data){
						if(data.isEmailRegistered()){
							RetrievePwdManager.getManager().retrievePwdByEmail((String)params[0]);
						}else{
							Toast.show(R.string.warn_email_unregister);
						}
					}
				}
			});
			break;

		case R.id.rete_btn_phone:
			AppContext.startAct(RetPhoneActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB) {
			onClick(sendEmailBtn);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "邮箱找回密码";
	}
}
