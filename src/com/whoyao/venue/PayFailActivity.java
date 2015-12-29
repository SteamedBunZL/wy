package com.whoyao.venue;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.whoyao.R;
import com.whoyao.activity.BasicActivity;

public class PayFailActivity extends BasicActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_fail);
		findViewById(R.id.fail_tv_repay).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.page_btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	

}
