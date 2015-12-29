package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.engine.user.RetrievePwdManager;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * 手机找回密码-3
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 *
 */
public class RetPhoneSetPwdActivity extends BasicActivity implements OnClickListener{
	private EditText passwordEt;
	private View backBtn;
	private View nextBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_phone_setpwd);
		initView();
	}

	private void initView() {
		passwordEt = (EditText) findViewById(R.id.retpsp_et_password);

		backBtn = findViewById(R.id.retpsp_btn_back);
		nextBtn = findViewById(R.id.retpsp_btn_enter);
				
		backBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retpsp_btn_back:
			finish();			
			break;
		case R.id.retpsp_btn_enter:
			String pwd = passwordEt.getText().toString();
			RetrievePwdManager.getManager().upPassword(pwd);
			
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "手机找回密码-3";
	}
	
}
