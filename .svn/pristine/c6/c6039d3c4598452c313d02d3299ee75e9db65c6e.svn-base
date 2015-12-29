package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 填写活动描述
 * 
 * @author hyh creat_at：2013-7-29-下午7:39:58
 */
public class EventAddDescActivity extends BasicActivity implements OnClickListener {

	private TextView numTv;
	private EditText contentEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_desc);

		numTv = (TextView) findViewById(R.id.event_desc_tv_num);
		contentEt = (EditText) findViewById(R.id.event_desc_et_content);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.event_desc_btn_enter).setOnClickListener(this);
		
		contentEt.addTextChangedListener(new TextNumWatcher(numTv, 500));
		String content = getIntent().getStringExtra(Extra.Snippet);
		contentEt.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_desc_btn_enter:
			String content = contentEt.getText().toString();
			if (TextUtils.isEmpty(content)) {
				Toast.show(R.string.warn_event_desc_empty);
				return;
			}
			if (15 > Utils.getStringLength(content)) {
				Toast.show(R.string.warn_event_desc_formatewrong);
				return;
			}
			if (500 < Utils.getStringLength(content)) {
				Toast.show(R.string.warn_event_desc_formatewrong);
				return;
			}
			Intent data = new Intent();
			data.putExtra(Extra.Snippet, content);
			setResult(RESULT_OK, data);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "填写活动描述";
	}
}
