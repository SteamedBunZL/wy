package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.engine.user.RegisterManager;
import com.whoyao.model.RegisterModel;
import com.whoyao.ui.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 邮箱注册
 * 
 * @author hyh creat_at：2013-7-17-下午7:04:54
 *         <p>
 */
public class RegEmailActivity extends BasicActivity implements OnClickListener {
	private EditText emailEt;
	private View backBtn;
//	private CheckBox agreeCb;
	private View licenceTv;
	private View regEmailBtn;
	private View commtiBtn;
	private EditText pwdEt;
	private EditText nickEt;
	private TextView addrTv;
	private TextView birthdayTv;
	private String birthday;
	private int provinceCode;
	private int cityCode;
	private int districtCode;
	private TextView sexTv;
	private int sex;
	private TextView tvAgree;
	private long birthdayMils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_email);
		initView();
	}

	private void initView() {
		emailEt = (EditText) findViewById(R.id.rege_et_email);
		emailEt.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		emailEt.setHint(R.string.hint_input_email);
		pwdEt = (EditText) findViewById(R.id.rege_et_pwd);
		pwdEt.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		pwdEt.setHint(R.string.hint_input_pwd_register);
		nickEt = (EditText) findViewById(R.id.rege_et_nick);
		
		backBtn = findViewById(R.id.rege_btn_back);
		sexTv = (TextView) findViewById(R.id.rege_tv_sex);
		birthdayTv = (TextView) findViewById(R.id.rege_tv_birthday);
		addrTv = (TextView) findViewById(R.id.rege_tv_addr);
		commtiBtn = findViewById(R.id.rege_btn_commit);
		licenceTv = findViewById(R.id.rege_tv_licence);
		regEmailBtn = findViewById(R.id.rege_btn_phone);

		tvAgree = (TextView) findViewById(R.id.rege_tv_agree);
		tvAgree.setSelected(true);
		tvAgree.setOnClickListener(this);
		sexTv.setOnClickListener(this);
		birthdayTv.setOnClickListener(this);
		addrTv.setOnClickListener(this);

		backBtn.setOnClickListener(this);
		addrTv.setOnClickListener(this);
		birthdayTv.setOnClickListener(this);
		commtiBtn.setOnClickListener(this);
		licenceTv.setOnClickListener(this);
		regEmailBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rege_btn_back:
			onBack();
			break;
		case R.id.rege_btn_commit:
			if (!tvAgree.isSelected()) {
				Toast.show(R.string.warn_register_licence);
				return;
			}
			String email = emailEt.getText().toString();
			String password = pwdEt.getText().toString();
			String nickname = nickEt.getText().toString();

			RegisterModel model = new RegisterModel();
			model.setUseremail(email);
			model.setUserpassword(password);
			model.setUsername(nickname);
			model.setUsersex(sex);
			model.setUserbirthday(birthday);
			model.setUserprovince(provinceCode);
			model.setUsercity(cityCode);
			model.setUserDistrict(districtCode);
			if (TextUtils.isEmpty(email)) {
				Toast.show(R.string.warn_email_empty);
				return;
			}
			RegisterManager.getManager().register(model);
			break;
		case R.id.rege_tv_agree:
			tvAgree.setSelected(!tvAgree.isSelected());
			break;
		case R.id.rege_tv_sex:
			Intent sexIntent = new Intent(this, SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			startActivityForResult(sexIntent, R.id.rege_tv_sex);
			break;
		case R.id.rege_tv_birthday:
			Intent birIntent = new Intent(this, SelectDateActivity.class);
			birIntent.putExtra(Extra.DefaultTimeMillis, birthdayMils);
			startActivityForResult(birIntent, R.id.rege_tv_birthday);
			break;
		case R.id.rege_tv_addr:
			Intent intent = new Intent(this, SelectAddrActivity.class);
			startActivityForResult(intent, R.id.rege_tv_addr);
			break;
		case R.id.rege_tv_licence:
			AppContext.startAct(LicenceActivity.class);
			break;
		case R.id.rege_btn_phone:
			AppContext.startAct(RegPhoneActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.rege_tv_sex:
			if (RESULT_OK == resultCode && null != data) {
				int intExtra = data.getIntExtra(Extra.SelectedItem, State.Selected_cancle);
				if(intExtra != State.Selected_cancle){
					sex = intExtra+1;
					sexTv.setText(data.getStringExtra(Extra.SelectedItemStr));
				}
			}
			break;
		case R.id.rege_tv_birthday:
			if (RESULT_OK == resultCode && null != data) {
				birthdayMils = data.getLongExtra(Extra.SelectedTime,0);
				String birStr = data.getStringExtra(Extra.SelectedTimeStr);
				birthday = birStr;
				birthdayTv.setText(birStr.substring(0, 10));
			}
			break;
		case R.id.rege_tv_addr:
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
		RegisterManager.getManager().cancleEmailRegDialog();
		return true;
	}

	@Override
	public String toString() {
		return "邮箱注册";
	}
}
