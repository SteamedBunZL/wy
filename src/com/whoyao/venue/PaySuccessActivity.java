package com.whoyao.venue;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.EventAddActivity;

public class PaySuccessActivity extends BasicActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		findViewById(R.id.success_tv_mybill).setOnClickListener(this);
		findViewById(R.id.success_tv_activity).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.success_tv_mybill:
			AppContext.startAct(MyBillActivity.class);
			break;
		case R.id.success_tv_activity:
			AppContext.startAct(EventAddActivity.class);
			break;

		}

	}
}
