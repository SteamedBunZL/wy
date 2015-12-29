package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.service.WYService;
import com.whoyao.ui.MessageDialog;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class TransActivity extends BasicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.acitivity_trans);
		MessageDialog dialog = new MessageDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("您的互邀帐号被其它客户端重复登录,您可以选择");
		dialog.setLeftButton("退出程序", new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(new Intent(TransActivity.this, WYService.class));
				int currentVersion = android.os.Build.VERSION.SDK_INT;
				if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
					System.exit(0);
				} else {
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					am.restartPackage(getPackageName());
				}
			}
		});
		dialog.setRightButton("重新登录", new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				UserEngine.logout();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
