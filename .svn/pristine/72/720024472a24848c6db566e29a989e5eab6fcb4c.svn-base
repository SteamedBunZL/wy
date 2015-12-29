package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.RegisterManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.ui.Toast;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 手机注册
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 */
public class RegPhoneActivity extends BasicActivity implements OnKeyListener, OnClickListener{
	private EditText phoneEt;
	private View backBtn;
	private View licenceTv;
	private View regEmailBtn;
	private View sendVcodeBtn;
	private TextView tvAgree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		initView();
	}

	private void initView() {
		phoneEt = (EditText) findViewById(R.id.regp_et_phone);
		backBtn = findViewById(R.id.regp_btn_back);
		sendVcodeBtn = findViewById(R.id.regp_btn_sendcode);
		licenceTv = findViewById(R.id.regp_tv_licence);
		regEmailBtn = findViewById(R.id.regp_btn_email);
		
		tvAgree = (TextView)findViewById(R.id.regp_tv_agree);
		tvAgree.setSelected(true);
		tvAgree.setOnClickListener(this);
		
		phoneEt.setOnKeyListener(this);
		
		backBtn.setOnClickListener(this);
		sendVcodeBtn.setOnClickListener(this);
		licenceTv.setOnClickListener(this);
		regEmailBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regp_btn_back:
			onBack();
			break;
		case R.id.regp_btn_sendcode:
			String phoneNum = phoneEt.getText().toString();
			if(!UserEngine.checkPhoneFormat(phoneNum)){
				return;
			}
			if(!tvAgree.isSelected()){
				Toast.show(R.string.warn_register_licence);
				return;
			}
			RegisterManager.getManager().verifyPhone(phoneNum,new CallBack<String>(){
				@Override
				public void onCallBack() {
					AppContext.startAct(RegPhoneCheckCodeActivity.class,true);
				}
			});
			break;
		case R.id.regp_tv_agree:
			v.setSelected(!v.isSelected());
			break;
		case R.id.regp_tv_licence:
			AppContext.startAct(LicenceActivity.class);
			break;
		case R.id.regp_btn_email:
			AppContext.startAct(RegEmailActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == event.KEYCODE_TAB) {
			onClick(sendVcodeBtn);
			return true;
		}
		return false;
	}

	@Override
	protected boolean onBack() {
		RegisterManager.getManager().canclePhoneRegDialog();
		return true;
	}	
	
	@Override
	public String toString() {
		return "手机注册-1";
	}
}
