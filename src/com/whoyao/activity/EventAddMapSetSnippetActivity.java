package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.ui.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 设置活动地点名称
 * @author hyh 
 * creat_at：2013-8-12-下午5:30:02
 */
public class EventAddMapSetSnippetActivity extends BasicActivity implements OnClickListener {
	private EditText contentEt;
	private TextView numTv;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_map_set_snippet);
		intent = getIntent();
		contentEt = (EditText)findViewById(R.id.feedback_et_content);
		numTv = (TextView)findViewById(R.id.feedback_tv_num);
		contentEt.addTextChangedListener(new TextNumWatcher(numTv, 50));
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.feedback_btn_enter).setOnClickListener(this);
		contentEt.setText(intent.getStringExtra(Extra.Snippet));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.feedback_btn_enter:
			String contentStr = contentEt.getText().toString();
			if (TextUtils.isEmpty(contentStr)) {
				Toast.show(R.string.warn_event_addr_empty);
				return;
			}else if(contentStr.length()>50){
				Toast.show(R.string.warn_event_addr_tolong);
			}
			intent.putExtra(Extra.Snippet, contentStr);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "设置活动地点名称";
	}
}
