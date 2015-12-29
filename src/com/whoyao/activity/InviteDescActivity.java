package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 填写邀请描述
 * 
 * @author hyh creat_at：2013-7-29-下午7:39:58
 */
public class InviteDescActivity extends BasicActivity implements
		OnClickListener {

	private TextView numTv;
	private EditText contentEt;
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_desc);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		numTv = (TextView) findViewById(R.id.invite_desc_tv_num);
		contentEt = (EditText) findViewById(R.id.invite_desc_et_content);

		findViewById(R.id.invite_desc_back).setOnClickListener(this);
		findViewById(R.id.invite_desc_btn_enter).setOnClickListener(this);

		contentEt.addTextChangedListener(new TextNumWatcher(numTv, 500));
		String content = getIntent().getStringExtra("content");
		contentEt.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invite_desc_back:
			finish();
			break;
		case R.id.invite_desc_btn_enter:
			String content = contentEt.getText().toString();
			if (TextUtils.isEmpty(content)) {
				Toast.show("请输入邀请描述");
				return;
			}
			if (15 > Utils.getStringLength(content)) {
				Toast.show("邀请描述为15-500个字");
				return;
			}
			if (500 < Utils.getStringLength(content)) {
				Toast.show("邀请描述为15-500个字");
				return;
			}
			Intent dataContent = new Intent();
			dataContent.putExtra("content", content);
			setResult(RESULT_OK, dataContent);
			finish();
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;

		}
	}

	// @Override
	// public String toString() {
	// return "填写邀请描述";
	// }
}
