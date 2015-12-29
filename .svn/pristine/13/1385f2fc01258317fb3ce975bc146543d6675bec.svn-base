package com.whoyao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

/**
 * 填写活动描述
 * 
 * @author hyh creat_at：2013-7-29-下午7:39:58
 */
public class EventAddKeywordActivity extends BasicActivity implements OnClickListener {

	private EditText contentEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_keyword);

		contentEt = (EditText) findViewById(R.id.event_keyword_et_content);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.event_keyword_btn_enter).setOnClickListener(this);
		String content = getIntent().getStringExtra(Extra.Snippet);
		contentEt.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_keyword_btn_enter:
			String content = contentEt.getText().toString().trim();
			while (-1 != content.indexOf("  ")) {
				content = content.replace("  ", " ");
			}
			String[] strings = content.split(" ");
			if(5<strings.length){
				Toast.show(R.string.warn_event_keyword_toomuch);
				return;
			}
			for(String keyword:strings){
				int length = Utils.getStringLength(keyword.trim());
				if (2>length || 8 < length) {
					Toast.show(R.string.warn_event_keyword_toolong);
					return;
				}
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
		return "填写活动关键字";
	}
}
