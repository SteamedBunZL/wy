package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * 修改过密码
 * @author hyh creat_at：2013-7-30-上午9:49:17
 */
public class MyChangePwdActivity extends BasicActivity implements OnClickListener {
	private EditText oldEt;
	private EditText newEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_changepwd);
		initView();
	}

	private void initView() {
		oldEt = (EditText) findViewById(R.id.mycp_et_old);
		newEt = (EditText) findViewById(R.id.mycp_et_new);
		findViewById(R.id.mycp_btn_back).setOnClickListener(this);
		findViewById(R.id.mycp_btn_enter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mycp_btn_back:
			finish();
			break;
		case R.id.mycp_btn_enter:
			String oldPwd = oldEt.getText().toString();
			String newPwd = newEt.getText().toString();
			MyinfoManager.getManager().changePassword(oldPwd, newPwd);
			
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "修改密码";
	}

}
