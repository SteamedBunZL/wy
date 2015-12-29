package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.engine.user.UserSettingManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 安全和隐私设置
 * @author hyh creat_at：2013-8-12-上午10:06:02
 */
public class SettingSecurityActivity extends BasicActivity implements OnCheckedChangeListener, OnClickListener {
	private CheckBox showPointCb;
	private CheckBox showEventCb;
	private boolean isSettingChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_security);
		showPointCb = (CheckBox)findViewById(R.id.set_cb_show_location);
		showEventCb = (CheckBox)findViewById(R.id.set_cb_show_event);
		showPointCb.setOnCheckedChangeListener(this);
		showEventCb.setOnCheckedChangeListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		UserSettingManager manager = UserSettingManager.getManager();
		showPointCb.setChecked(manager.isShowPoint());
		showEventCb.setChecked(manager.isShowEvent());
		isSettingChanged = false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isSettingChanged = true;	
	}

	@Override
	public void onClick(View v) {
		onBack();
	}
	
	@Override
	protected boolean onBack() {
		if(isSettingChanged){
			UserSettingManager.getManager().setUserSafeSetting(showPointCb.isChecked(), showEventCb.isChecked());			
		}
		finish();
		return true;
	}
	
	@Override
	public String toString() {
		return "安全和隐私设置";
	}
}
