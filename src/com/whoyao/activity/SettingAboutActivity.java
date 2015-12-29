package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.client.ClientEngine;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 关于
 * @author hyh 
 * creat_at：2013-8-12-上午10:32:13
 */
public class SettingAboutActivity extends BasicActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_about);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.about_tv_service_phone).setOnClickListener(this);
		findViewById(R.id.about_tv_licence).setOnClickListener(this);
		findViewById(R.id.about_tv_service_weibo).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.about_tv_service_phone:
			ClientEngine.showCallServiceDialog(this);
			break;
		case R.id.about_tv_licence:
			AppContext.startAct(LicenceActivity.class);
			break;
		case R.id.about_tv_service_weibo:
			ClientEngine.openWeibo();
			break;
		default:
			break;
		}
	}
	@Override
	public String toString() {
		return "关于互邀";
	}
}
