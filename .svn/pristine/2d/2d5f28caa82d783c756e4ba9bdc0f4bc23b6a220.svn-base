package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 设置签名
 * @author hyh creat_at：2013-8-7-下午3:22:52
 */
public class MySetContentActivity extends BasicActivity implements OnClickListener {
	private String oldContent;
	private EditText etContent;
	private TextView numTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_setcontent);
		
		oldContent = getIntent().getStringExtra(Extra.MyContent);
		
		etContent = (EditText)findViewById(R.id.setcontent_et);
		numTv = (TextView)findViewById(R.id.setcontent_tv_num);
		etContent.addTextChangedListener(new TextNumWatcher(numTv, 30));
		etContent.setText(oldContent);
		findViewById(R.id.setcontent_btn_enter).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setcontent_btn_enter:
			String newContent = etContent.getText().toString();
			if(30 < Utils.getStringLength(newContent)){
				Toast.show("签名内容在30个字以内");
				return;
			}
			if(!newContent.equals(oldContent)){
				Intent intent = new Intent();
				intent.putExtra(Extra.MyContent, newContent);
				setResult(RESULT_OK, intent);
			}
			finish();			
			break;
		case R.id.page_btn_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "设置签名";
	}
}
