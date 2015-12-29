package com.whoyao.topic;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.ui.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class TopicTagFreeChoiceActivity extends BasicActivity implements
		OnClickListener {
	private EditText etFree;
	private String tagFree = "";
	private TextView tvNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_tag_free_choice);
		etFree = (EditText) findViewById(R.id.et_tag);
		tvNum = (TextView) findViewById(R.id.tv_num);
		etFree.addTextChangedListener(new TextNumWatcher(tvNum, 8));
		findViewById(R.id.btn_add).setOnClickListener(this);
		findViewById(R.id.page_btn_back_initial).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			if (TextUtils.isEmpty(etFree.getText().toString())) {
				Toast.show("请输入自定义标签");
				return;
			}
			if (etFree.getText().length()>8) {
				Toast.show("自定义标签限1-8个字");
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(Extra.SelectedID, 0);
			intent.putExtra(Extra.Search_Keyword, etFree.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.page_btn_back_initial:
			finish();
			break;

		}

	}

}
