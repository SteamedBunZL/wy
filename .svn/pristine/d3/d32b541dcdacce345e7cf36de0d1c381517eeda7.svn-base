package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.ui.Toast;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 意见反馈
 * @author hyh 
 * creat_at：2013-8-12-下午5:30:02
 */
public class SettingFeedbackActivity extends BasicActivity implements OnClickListener {
	private EditText contentEt;
	private TextView numTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting_feedback);
		contentEt = (EditText)findViewById(R.id.feedback_et_content);
		numTv = (TextView)findViewById(R.id.feedback_tv_num);
		contentEt.addTextChangedListener(new TextNumWatcher(numTv, 140));
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.feedback_btn_enter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.feedback_btn_enter:
			String contentStr = contentEt.getText().toString();
			if (contentStr.length()>140||contentStr.length()<5) {
				Toast.show(R.string.setting_input_wrong);
			}else {
				ClientEngine.feedback(contentStr, new CallBack(){
					@Override
					public void onCallBack() {
						Toast.show(R.string.setting_input_right);
						finish();
					}
				});
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "意见反馈";
	}
}
