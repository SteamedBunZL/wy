package com.whoyao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.whoyao.AppException;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.Const.Extra;

/**
 * 两个选择按键
 * @author hyh creat_at：2013-8-6-下午4:10:05
 */
public class SelectButton2Activity extends AbsSelectActivity implements OnClickListener {
	private View chooseLl;
	private Button btnEnter;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_button2);
		initView();		
		show();
	}

	private void initView() {
		btnEnter = (Button) findViewById(R.id.select_btn_enter);
		btnCancel = (Button) findViewById(R.id.select_btn_cancle);
		chooseLl = findViewById(R.id.select_ll_button);
		findViewById(R.id.select_background);
		
		setSelectView(chooseLl);
		btnEnter.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_btn_enter:
			Intent intent1 = new Intent();
			intent1.putExtra(Extra.SelectedItem, State.Selected_enter);
			setResult(RESULT_OK, intent1);
			dismiss();
			break;
		case R.id.select_btn_cancle:
			Intent intent = new Intent();
			intent.putExtra(Extra.SelectedItem, State.Selected_cancle);
			setResult(RESULT_OK, intent);
			dismiss();
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "两个选择按键";
	}
}
