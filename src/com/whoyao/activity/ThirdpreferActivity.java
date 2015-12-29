package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.user.ThirdUserManager;
import com.whoyao.model.ThirdloginModel;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 第三方登录完善信息
 * 
 * @author hyh creat_at：2013-7-17-下午7:04:54
 *         <p>
 */
public class ThirdpreferActivity extends BasicActivity implements OnClickListener {
	private View backBtn;
	private View commtiBtn;
	private TextView addrTv;
	private TextView birthdayTv;
	private String birthday;
	private int provinceCode;
	private int cityCode;
	private int districtCode;
	private EditText nickEt;
	private TextView sexTv;
	private int sex;
	private long birthdayMils;
	private ThirdloginModel model;
	SharedPreferences share;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thirdperfectcomplete_login);
		initView();
	}
	private void initView() {
		backBtn = findViewById(R.id.thirdprefer_btn_back);
		nickEt = (EditText) findViewById(R.id.thirdlogin_et_nick);
		sexTv = (TextView) findViewById(R.id.thirdprefer_tv_sex);
		birthdayTv = (TextView) findViewById(R.id.thirdprefer_tv_birthday);
		addrTv = (TextView) findViewById(R.id.thirdprefer_tv_addr);
		commtiBtn = findViewById(R.id.logthird_btn_commit);
		backBtn.setVisibility(View.INVISIBLE);
		sexTv.setOnClickListener(this);
		birthdayTv.setOnClickListener(this);
		addrTv.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		addrTv.setOnClickListener(this);
		birthdayTv.setOnClickListener(this);
		commtiBtn.setOnClickListener(this);
		model = new ThirdloginModel();
		share=AppContext.getConfigFile();
		nickEt.setText(share.getString(KEY.Usernickname,""));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.thirdprefer_tv_sex:
			Intent sexIntent = new Intent(this, SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			startActivityForResult(sexIntent, R.id.thirdprefer_tv_sex);
			break;
		case R.id.thirdprefer_tv_birthday:
			Intent birIntent = new Intent(this, SelectDateActivity.class);
			birIntent.putExtra(Extra.DefaultTimeMillis, birthdayMils);
			startActivityForResult(birIntent, R.id.thirdprefer_tv_birthday);
			break;
		case R.id.thirdprefer_tv_addr:
			Intent intent = new Intent(this, SelectAddrActivity.class);
			startActivityForResult(intent, R.id.thirdprefer_tv_addr);
			break;
		case R.id.logthird_btn_commit:
			String name = nickEt.getText().toString();
			L.i("互邀----1",name);
			Editor editor = AppContext.getConfigFile().edit();
			editor.putString(KEY.Usernickname, name);
			editor.commit();//提交修改
			L.i("互邀----2",share.getString(KEY.Usernickname, ""));
			model.setUsernickname(name);
			model.setAccesstoken(share.getString(KEY.Accesstoken, ""));
			model.setLoginuserid(share.getString(KEY.Loginuserid, ""));
			model.setPicurl(share.getString(KEY.Picurl,""));
			model.setLogintype(share.getInt(KEY.Logintype,0));
			model.setUsersex(sex);
			model.setUserbirthday(birthday);
			model.setUserprovince(provinceCode);
			model.setUsercity(cityCode);
			model.setUserDistrict(districtCode);
			if(checkInfo(model)){
				L.i("互邀-----",""+model.toString());
				ThirdUserManager.getManager().ThirdLogin(model);	
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.thirdprefer_tv_sex:
			if (RESULT_OK == resultCode && null != data) {
				int intExtra = data.getIntExtra(Extra.SelectedItem, State.Selected_cancle);
				if(intExtra != State.Selected_cancle){
					sex = intExtra+1;
					sexTv.setText(data.getStringExtra(Extra.SelectedItemStr));
				}
			}
			break;
		case R.id.thirdprefer_tv_birthday:
			if (RESULT_OK == resultCode && null != data) {
				birthdayMils = data.getLongExtra(Extra.SelectedTime,0);
				String birStr = data.getStringExtra(Extra.SelectedTimeStr);
				birthday = birStr;
				birthdayTv.setText(birStr.substring(0, 10));
			}
			break;
		case R.id.thirdprefer_tv_addr:
			if (RESULT_OK == resultCode && null != data) {
				provinceCode = data.getIntExtra(Extra.ProvinceCode, 0);
				cityCode = data.getIntExtra(Extra.CityCode, 0);
				districtCode = data.getIntExtra(Extra.DistrictCode, 0);
				String addrName = data.getStringExtra(Extra.ProvinceName) + " " + data.getStringExtra(Extra.CityName)
						+ " " + data.getStringExtra(Extra.DistrictName);
				addrTv.setText(addrName);
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected boolean onBack() {
		return false;
	}
	public boolean checkInfo(ThirdloginModel model) {
		if(0 == model.getUsersex()){//检查性别
			Toast.show(R.string.hint_select_sex);
			return false;
		}
		if(TextUtils.isEmpty(model.getUserbirthday())){//检查生日
			Toast.show(R.string.warn_birthday_empty);
			return false;
		}
		if(0 == model.getUserprovince() || 0 == model.getUsercity() || 0 == model.getUserDistrict()){//检查所在地
			Toast.show(R.string.hint_select_addr);
			return false;
		}
		return true;
	}	
	@Override
	public String toString() {
		return "信息完善";
	}
}