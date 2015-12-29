package com.whoyao.activity;

import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.common.Countdown;
import com.whoyao.engine.user.RetrievePwdManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

/**
 * 手机找回密码-2
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 */
public class RetPhoneCheckCodeActivity extends BasicActivity implements OnClickListener{
	private EditText codeEt;
	private View backBtn;
	private View nextBtn;
	private Button resendBtn;
	private Countdown countdown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_phone_checkcode);
		initView();
	}

	private void initView() {
		codeEt = (EditText) findViewById(R.id.retpcc_et_code);
		
		backBtn = findViewById(R.id.retpcc_btn_back);
		nextBtn = findViewById(R.id.retpcc_btn_next);
		resendBtn = (Button)findViewById(R.id.retpcc_btn_resendcode);
				
		backBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		resendBtn.setOnClickListener(this);
		countdown = new Countdown(59, resendBtn, "秒后重新获取");
		countdown.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retpcc_btn_back:
			finish();
			break;
		case R.id.retpcc_btn_next:
			String code = codeEt.getText().toString();
			if (!UserEngine.checkSmsCodeFormat(code)) {
				return;
			}
			RetrievePwdManager.getManager().checkRetPwdVCode(code);
//			AppContext.startAct(RetPhoneSetPwdActivity.class);
			break;
		case R.id.retpcc_btn_resendcode:
			RetrievePwdManager.getManager().resendRetPwdVCode(countdown);
			break;
		default:
			break;
		}
	}	
	
	@Override
	public String toString() {
		return "手机找回密码-2";
	}
}
