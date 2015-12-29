package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.user.RetrievePwdManager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

/**
 * 手机找回密码-1
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 */
public class RetPhoneActivity extends BasicActivity implements OnKeyListener, OnClickListener{

	private EditText phoneEt;
	private View backBtn;
	private View regEmailBtn;
	private View sendVcodeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_phone);
		initView();
	}

	private void initView() {
		phoneEt = (EditText) findViewById(R.id.retp_et_phone);
		
		backBtn = findViewById(R.id.retp_btn_back);
		sendVcodeBtn = findViewById(R.id.retp_btn_sendcode);
		regEmailBtn = findViewById(R.id.retp_btn_email);
		
		phoneEt.setOnKeyListener(this);
		
		backBtn.setOnClickListener(this);
		sendVcodeBtn.setOnClickListener(this);
		regEmailBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retp_btn_back:
			finish();
			break;
		case R.id.retp_btn_sendcode:
			String phoneNum = phoneEt.getText().toString();
			RetrievePwdManager.getManager().sendRetPwdVCode(phoneNum);
//			AppContext.startAct(RetPhoneCheckCodeActivity.class);
			break;

		case R.id.retp_btn_email:
			AppContext.startAct(RetEmailActivity.class,true);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB) {
			onClick(sendVcodeBtn);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "手机找回密码-1";
	}
}
