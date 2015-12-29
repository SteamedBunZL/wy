package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.engine.user.RegisterManager;
import com.whoyao.model.RegisterModel;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 手机注册-3
 * @author hyh 
 * creat_at：2013-7-17-下午7:04:54 <p>
 *
 */
public class RegPhoneInfoActivity extends BasicActivity implements OnClickListener{
	//"手机注册-3";
	private View backBtn;
	private View registerBtn;
	private EditText pwdEt;
	private EditText nickEt;
	private TextView addrTv;

	private TextView sexTv;
	private TextView birthdayTv;
	private int sex;
	private int provinceCode;
	private int cityCode;
	private int districtCode;
	private String birthday;
	private long birthdayMils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone_info);
		initView();
	}

	private void initView() {
		pwdEt = (EditText) findViewById(R.id.regpi_et_pwd);
		pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		nickEt = (EditText) findViewById(R.id.regpi_et_nick);

		
		backBtn = findViewById(R.id.regpi_btn_back);
		sexTv = (TextView)findViewById(R.id.regpi_tv_sex);
		birthdayTv = (TextView)findViewById(R.id.regpi_tv_birthday);
		addrTv = (TextView)findViewById(R.id.regpi_tv_addr);
		registerBtn = findViewById(R.id.regpi_btn_register);
		
		backBtn.setOnClickListener(this);
		sexTv.setOnClickListener(this);
		birthdayTv.setOnClickListener(this);
		addrTv.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regpi_btn_back:
			onBack();
			break;
		case R.id.regpi_tv_sex:
			Intent sexIntent = new Intent(this,SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			startActivityForResult(sexIntent, R.id.regpi_tv_sex);
			break;
		case R.id.regpi_tv_birthday:
			Intent birIntent = new Intent(this,SelectDateActivity.class);
			birIntent.putExtra(Extra.DefaultTimeMillis, birthdayMils);
			startActivityForResult(birIntent, R.id.regpi_tv_birthday);
			break;
		case R.id.regpi_tv_addr:
			Intent intent = new Intent(this, SelectAddrActivity.class);
			startActivityForResult(intent, R.id.regpi_tv_addr);
			break;
		case R.id.regpi_btn_register:
			
			String password = pwdEt.getText().toString();
			String nickname = nickEt.getText().toString();

			RegisterModel model = new RegisterModel();
			model.setUsername(nickname);
			model.setUserpassword(password);
			model.setUsersex(sex);
			model.setUserbirthday(birthday);
			model.setUserprovince(provinceCode);
			model.setUsercity(cityCode);
			model.setUserDistrict(districtCode);
			RegisterManager.getManager().registerPhone(model);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.regpi_tv_sex:
			if(RESULT_OK == resultCode && null != data){
				int intExtra = data.getIntExtra(Extra.SelectedItem, State.Selected_cancle);
				if(intExtra != State.Selected_cancle){
					sex = intExtra+1;
					sexTv.setText(data.getStringExtra(Extra.SelectedItemStr));
				}
			}
			break;
		case R.id.regpi_tv_birthday:
			if(RESULT_OK == resultCode && null != data){
				birthdayMils = data.getLongExtra(Extra.SelectedTime,0);
				String birStr = data.getStringExtra(Extra.SelectedTimeStr);
				birthday = birStr;
				birthdayTv.setText(birStr.substring(0, 10));
			}
			break;
		case R.id.regpi_tv_addr:
			if(RESULT_OK == resultCode && null != data){
				provinceCode = data.getIntExtra(Extra.ProvinceCode, 0);
				cityCode = data.getIntExtra(Extra.CityCode, 0);
				districtCode = data.getIntExtra(Extra.DistrictCode, 0);
				String addrName = data.getStringExtra(Extra.ProvinceName)+" "+data.getStringExtra(Extra.CityName)+" "+data.getStringExtra(Extra.DistrictName);
				addrTv.setText(addrName);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBack() {
		//  返回时提醒
		RegisterManager.getManager().canclePhoneRegDialog();
		return true;
	}

	@Override
	public String toString() {
		return "手机注册-3";
	}
}
