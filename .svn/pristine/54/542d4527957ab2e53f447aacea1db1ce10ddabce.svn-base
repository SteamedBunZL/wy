package com.whoyao.activity;

import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.model.UserConditionSettingRModel;
import com.whoyao.model.UserConditionSettingTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 条件设置
 * 
 * @author hyh creat_at：2013-8-12-上午10:28:50
 */
public class SettingConditionActivity extends BasicActivity implements OnCheckedChangeListener, OnClickListener {
	private CheckBox immediateCb;
	private CheckBox one2oneCb;
	private CheckBox attestationCb;
	private TextView ageTv;
	private TextView sexTv;
	private View ageLl;
	private View sexLl;
	private boolean isSettingChanged = false;
	private UserConditionSettingRModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_condition);
		initView();
		initData();
	}

	private void initView() {
		immediateCb = (CheckBox) findViewById(R.id.setcon_cb_receive_immediate_invite);
		one2oneCb = (CheckBox) findViewById(R.id.setcon_cb_receive_one2one_invite);
		attestationCb = (CheckBox) findViewById(R.id.setcon_cb_real_attestation);
		sexTv = (TextView) findViewById(R.id.setcon_tv_sex);
		ageTv = (TextView) findViewById(R.id.setcon_tv_age);
		sexLl = findViewById(R.id.setcon_ll_sex);
		ageLl = findViewById(R.id.setcon_ll_age);
		sexLl.setOnClickListener(this);
		ageLl.setOnClickListener(this);
		immediateCb.setOnCheckedChangeListener(this);
		one2oneCb.setOnCheckedChangeListener(this);
		attestationCb.setOnCheckedChangeListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
	}

	private void initData() {
		UserSettingManager.getManager().getUserConditionSetting( MyinfoManager.getUserInfo().getUserID(), new CallBack<UserConditionSettingRModel>(){
			@Override
			public void onCallBack(boolean isSuccess, UserConditionSettingRModel data) {
				if(isSuccess && null != data){
					model = data;
					immediateCb.setChecked(model.isReceiveImmediateInvite());
					one2oneCb.setChecked(model.isReceiveOneToOneInvite());
//					if(!model.isReceiveOneToOneInvite()){
//					}
					//  显示内容
					setOne2OneState(model.isReceiveOneToOneInvite());
					attestationCb.setChecked(model.isRealAttestationUser());
					sexTv.setText(model.getUserSexStr());
					if (0 != model.getUserMaxAge()) {
						if(0 == model.getUserMinAge() && 0 == model.getUserMaxAge()){
							ageTv.setText("不限");
						}else if (0 == model.getUserMinAge()) {
							ageTv.setText("18岁以下");
						} else if (45 < model.getUserMaxAge()) {
							ageTv.setText("45岁以上");
						} else {
							ageTv.setText(model.getUserMinAge() + "-" + model.getUserMaxAge() + "岁");
						}
					}
				}else{
					model = new UserConditionSettingRModel();
					Toast.show("获取设置失败");
				}
				isSettingChanged = false;
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isSettingChanged = true;
		switch (buttonView.getId()) {
		case R.id.setcon_cb_receive_one2one_invite:
			setOne2OneState(isChecked);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.setcon_ll_sex:
			Intent sexIntent = new Intent(this, SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			sexIntent.putExtra(Extra.ButtonCancel_Text, "不限");
			startActivityForResult(sexIntent, R.id.setcon_ll_sex);
			break;
		case R.id.setcon_ll_age:
			Intent bloodIntent = new Intent(this, SelectSingleWheelActivity.class);
			bloodIntent.putExtra(Extra.ArrayRes, R.array.age_group);
			startActivityForResult(bloodIntent, R.id.setcon_ll_age);
			break;

		default:
			break;
		}
	}

	private void setOne2OneState(boolean state){
		if (state) {
			sexLl.setVisibility(View.VISIBLE);
			ageLl.setVisibility(View.VISIBLE);
			attestationCb.setVisibility(View.VISIBLE);
			one2oneCb.setBackgroundResource(R.drawable.selector_radius_top);
		} else {
			sexLl.setVisibility(View.GONE);
			ageLl.setVisibility(View.GONE);
			attestationCb.setVisibility(View.GONE);
			one2oneCb.setBackgroundResource(R.drawable.selector_radius_white_gary);
		}
	}
	
	@Override
	protected boolean onBack() {
		if(isSettingChanged){
			UserConditionSettingTModel modelT = new UserConditionSettingTModel();
			modelT.setReceiveimmediateinvite(immediateCb.isChecked());
			modelT.setReceiveonetooneinvite(one2oneCb.isChecked());
			modelT.setRealAttestationUser(attestationCb.isChecked());
			modelT.setUsersex(model.getUserSex());
			modelT.setUsermaxage(model.getUserMaxAge());
			modelT.setUserminage(model.getUserMinAge());
			Net.request(modelT, WebApi.User.UpConditionSetting, new ResponseHandler());
			NetCache.clearCaches();
		}
		finish();
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.setcon_ll_sex:
			if (RESULT_OK == resultCode) {
				isSettingChanged = true;
				model.setUserSex(1 + data.getIntExtra(Extra.SelectedItem, State.Selected_cancle));
				sexTv.setText(data.getStringExtra(Extra.SelectedItemStr));
			}
			break;
		case R.id.setcon_ll_age:
			if (RESULT_OK == resultCode) {
				isSettingChanged = true;
				ageTv.setText(data.getStringExtra(Extra.SelectedItemStr));
				switch (data.getIntExtra(Extra.SelectedItem, State.Selected_cancle)) {
				case 0:
					model.setUserMinAge(0);
					model.setUserMaxAge(0);
					break;
				case 1:
					model.setUserMinAge(0);
					model.setUserMaxAge(18);
					break;
				case 2:
					model.setUserMinAge(18);
					model.setUserMaxAge(25);
					break;
				case 3:
					model.setUserMinAge(25);
					model.setUserMaxAge(28);
					break;
				case 4:
					model.setUserMinAge(28);
					model.setUserMaxAge(32);
					break;
				case 5:
					model.setUserMinAge(32);
					model.setUserMaxAge(35);
					break;
				case 6:
					model.setUserMinAge(35);
					model.setUserMaxAge(38);
					break;
				case 7:
					model.setUserMinAge(38);
					model.setUserMaxAge(45);
					break;
				case 8:
					model.setUserMinAge(45);
					model.setUserMaxAge(100);
					break;
				default:
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "条件设置";
	}
}
