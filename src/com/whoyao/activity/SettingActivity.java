package com.whoyao.activity;

import com.tencent.tauth.Tencent;
import com.whoyao.AppContext;
import com.whoyao.Const.Config;
import com.whoyao.R;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.client.UpdateManager;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.UpdateModel;
import com.whoyao.service.WYService;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 设置页
 * 
 * @author hyh creat_at：2013-8-11-下午4:47:45
 */
public class SettingActivity extends BasicActivity implements OnClickListener,
		OnCheckedChangeListener {
	private CheckBox soundCb;
	private CheckBox vibratoCb;
	private CheckBox messageCb;
	private TextView versionTv;
	private UpdateModel update;
	private WYService mWyService;
	public  Tencent mTencent;
	private Vibrator mVibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
//		mActivityCallBack = 
		initView();
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}
//	ServiceConnection serviceConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			mWyService = null;
//		}
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			mWyService = ((WYService.WYBinder) service).getService();
//
//		}
//	};
//	private void bindWyService() {
//		Intent mServiceIntent = new Intent(this, WYService.class);
//		bindService(mServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE
//				+ Context.BIND_DEBUG_UNBIND);
//	}

	private void initView() {
		soundCb = (CheckBox) findViewById(R.id.set_cb_sound);
		vibratoCb = (CheckBox) findViewById(R.id.set_cb_vibrato);
		messageCb = (CheckBox) findViewById(R.id.set_cb_message);
		versionTv = (TextView) findViewById(R.id.set_tv_version);
		ClientEngine.initClientSetting();
		soundCb.setChecked(ClientEngine.soundSetting);
		vibratoCb.setChecked(ClientEngine.vibratoSetting);
		messageCb.setChecked(ClientEngine.messageSetting);
		soundCb.setOnCheckedChangeListener(this);
		vibratoCb.setOnCheckedChangeListener(this);
		messageCb.setOnCheckedChangeListener(this);
		// TODO 初始化更新状态
		String updateStr = AppContext.getConfigFile().getString(Config.UpdateInfo, null);
		if(!TextUtils.isEmpty(updateStr)){
			update = JSON.getObject(updateStr, UpdateModel.class);
			versionTv.setText("有新版本");
		}
			 		
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.set_tv_security).setOnClickListener(this);
		findViewById(R.id.set_tv_spacetime).setOnClickListener(this);
		findViewById(R.id.set_tv_condition).setOnClickListener(this);
		findViewById(R.id.set_ll_version).setOnClickListener(this);
		findViewById(R.id.set_tv_help).setOnClickListener(this);
		findViewById(R.id.set_tv_about).setOnClickListener(this);
		findViewById(R.id.set_btn_logout).setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.set_tv_security:// 安全
			AppContext.startAct(SettingSecurityActivity.class);
			break;
		case R.id.set_tv_spacetime:// 时空
			AppContext.startAct(SettingSpaceTimeActivity.class);
			break;
		case R.id.set_tv_condition:// 条件
			AppContext.startAct(SettingConditionActivity.class);
			break;
		case R.id.set_ll_version:// 版本更新
			if(update == null){
				Toast.show("已经是最新版本");
			}else{
				UpdateManager manager = UpdateManager.getUpdateManager(update);
				manager.showNoticeDialog();
			}
			break;
		case R.id.set_tv_help:// 帮助反馈
			AppContext.startAct(SettingHelpActivity.class);
			break;
		case R.id.set_tv_about:// 关于
			AppContext.startAct(SettingAboutActivity.class);
			break;
		case R.id.set_btn_logout:// 注销
			boolean isLogoutChecked = MyinfoManager.getUserConfigFile().getBoolean(Config.isLogoutChecked, false);
			if (isLogoutChecked) {
				UserEngine.logout();
			}else{
				MessageDialog dialog = new MessageDialog(this);
				dialog.setTitle("操作提示");
				dialog.setMessage("确定注销互邀网帐号吗？");
				dialog.setCheck(null,MyinfoManager.getUserConfigFile(), Config.isLogoutChecked);
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						UserEngine.logout();
					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.set_cb_sound:
			ClientEngine.storeSoundSeting(isChecked);
			if (isChecked) {
				MediaPlayer.create(this, R.raw.music).start();
			}
			break;
		case R.id.set_cb_vibrato:
			ClientEngine.storeVibratoSeting(isChecked);
			if (isChecked) {
				mVibrator.vibrate(400);
			}
			break;
		case R.id.set_cb_message:
			ClientEngine.storeMessageSeting(isChecked);
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "设置页";
	}
}
