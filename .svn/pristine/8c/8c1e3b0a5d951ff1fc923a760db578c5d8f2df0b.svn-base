package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.common.Countdown;
import com.whoyao.engine.user.RegisterManager;
import com.whoyao.engine.user.UserEngine;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 手机注册-2
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 *
 */
public class RegPhoneCheckCodeActivity extends BasicActivity implements OnClickListener{
	// "手机注册-2";
	private EditText codeEt;
	private View backBtn;
	private View nextBtn;
	private Button resendBtn;
	private Countdown countdown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone_checkcode);
		
		initView();
	}

	private void initView() {
		codeEt = (EditText) findViewById(R.id.regpcc_et_code);
//		codeEt.setInputType(InputType.TYPE_CLASS_NUMBER);
//		codeEt.setImeOptions(EditorInfo.IME_ACTION_GO);
//		codeEt.setHint("请输入验证码");
		backBtn = findViewById(R.id.regpcc_btn_back);
		nextBtn = findViewById(R.id.regpcc_btn_next);
		resendBtn = (Button)findViewById(R.id.regpcc_btn_resendcode);
				
		backBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		resendBtn.setOnClickListener(this);
		countdown = new Countdown(59, resendBtn, "秒后重新获取");
		countdown.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regpcc_btn_back:
			onBack();
			break;
		case R.id.regpcc_btn_next:
			String vcode = codeEt.getText().toString();
			if(UserEngine.checkSmsCodeFormat(vcode)){
				RegisterManager.getManager().checkRegisterVCode(vcode);
			}
			break;
		case R.id.regpcc_btn_resendcode:
			RegisterManager.getManager().resendRegisterVCode(countdown);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBack() {
		//  返回时提醒
		RegisterManager.getManager().canclePhoneRegDialog();
		return true;
	}
	
	@Override
	public String toString() {
		return "手机注册-2";
	}
}
