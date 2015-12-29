package com.whoyao.activity;

import java.io.File;

import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.user.VerifyManager;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.GraphicUtils;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 实名认证
 * 
 * @author hyh creat_at：2013-8-15-下午6:19:06
 */
public class MyVerifyHonestyActivity extends BasicActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	private EditText etName;
	private TextView tvBirthday;
	private EditText etIDnum;
	private String birthdayStr;
	private String photoPath;
	private View rlUpload;
	private ImageView ivPhoto;
	private String beforeStr = "";
	private EditText currentEditText;
	private long birthdayMils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_verify_honesty);
		etName = (EditText) findViewById(R.id.myvh_et_name);
		tvBirthday = (TextView) findViewById(R.id.myvh_tv_birthday);
		etIDnum = (EditText) findViewById(R.id.myvh_et_idnum);
		rlUpload = findViewById(R.id.myvh_rl_upload);
		ivPhoto = (ImageView) findViewById(R.id.myvh_iv_photo);
//		etName.addTextChangedListener(this);
//		etIDnum.addTextChangedListener(this);
		etName.setOnFocusChangeListener(this);
		etIDnum.setOnFocusChangeListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.myvh_ll_birthday).setOnClickListener(this);
		findViewById(R.id.myvh_btn_enter).setOnClickListener(this);
		findViewById(R.id.page_btn_back_1).setOnClickListener(this);
		findViewById(R.id.myvh_btn_cancle).setOnClickListener(this);
		findViewById(R.id.myvh_btn_enter).setOnClickListener(this);
		findViewById(R.id.myvh_btn_upload).setOnClickListener(this);
		try {
			etName.setText(MyinfoManager.getUserInfo().getUserRelName());
			birthdayStr = MyinfoManager.getUserInfo().getUserBirthday();
			tvBirthday.setText(CalendarUtils.formatYMD(birthdayStr));
		} catch (Exception e) {
			AppException.handle(e);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.page_btn_back_1:
		case R.id.myvh_btn_cancle:
			ivPhoto.setImageDrawable(null);
			rlUpload.setVisibility(View.GONE);
			break;
		case R.id.myvh_ll_birthday:
			Intent birIntent = new Intent(this, SelectDateActivity.class);
			long timeMills = CalendarUtils.parseDate(MyinfoManager.getUserInfo().getUserBirthday());
			birIntent.putExtra(Extra.DefaultTimeMillis, timeMills);
			startActivityForResult(birIntent, R.id.myvh_ll_birthday);
			break;
		case R.id.myvh_btn_enter:
			String nameStr = etName.getText().toString();
			String idnumStr = etIDnum.getText().toString();
			// 已在方法中验证
//			if (TextUtils.isEmpty(etName.getText())) {
//			Toast.show(R.string.real_name_null);
//			return;
//		}
//		if (TextUtils.isEmpty(etIDnum.getText())) {
//			Toast.show(R.string.id_null);
//			return;
//		}
		
//		int length = etName.getText().length();
//		if (!FormatUtils.isChineseOnly(etName.getText().toString())||length<2||length>8) {
//			Toast.show(R.string.real_name_wrong);
//			return;
//		}
//		if(!FormatUtils.isIDCard(etIDnum.getText().toString())){
//			Toast.show(R.string.id_wrong);
//			return;
//		}
			if (VerifyManager.getManager().CheckHonestyInfo(nameStr, birthdayStr, idnumStr)) {
				Intent intent = new Intent(this, SelectPhotoActivity.class);
				startActivityForResult(intent, R.id.myvh_btn_enter);
			}
			break;
		case R.id.myvh_btn_upload:
			if(TextUtils.isEmpty(photoPath)){
				Toast.show("请选择身份证正面照片");
				return;
			}
			VerifyManager.getManager().verifyHonesty(photoPath);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.myvh_ll_birthday:
			if (RESULT_OK == resultCode && null != data) {
				birthdayMils = data.getLongExtra(Extra.SelectedTime,0);
				birthdayStr = data.getStringExtra(Extra.SelectedTimeStr);
				if(TextUtils.isEmpty(birthdayStr)){
					tvBirthday.setText("");
				}else{
					tvBirthday.setText(CalendarUtils.formatYMD(birthdayStr));
				}
			}
			break;
		case R.id.myvh_btn_enter:
			if (RESULT_OK == resultCode && null != data) {
				photoPath = data.getStringExtra(Extra.IMAGE_PATH);
				if (new File(photoPath).exists()) {
					rlUpload.setVisibility(View.VISIBLE);
					Utils.measureView(ivPhoto);
					Bitmap compressImageSize = GraphicUtils.compressImageSize(photoPath, ClientEngine.getMobileInfo()
							.getVgaWidth(), ClientEngine.getMobileInfo().getVgaWidth());
					ivPhoto.setImageBitmap(compressImageSize);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBack() {
		if (rlUpload.getVisibility() == View.VISIBLE) {
			ivPhoto.setImageDrawable(null);
			rlUpload.setVisibility(View.GONE);
			return true;
		}
		return super.onBack();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			currentEditText = (EditText) v;
			currentEditText.setSelection(currentEditText.getText().length());
		}else if(currentEditText == v){
			currentEditText = null;
		}
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		beforeStr = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(null != currentEditText){
			switch (currentEditText.getId()) {
			case R.id.myvh_et_name:
				if(!TextUtils.isEmpty(s) && !FormatUtils.isChineseOnly(s.toString())){
					if(null == beforeStr){
						beforeStr = "";
					}
					currentEditText.setText(beforeStr);
				}
				break;
			case R.id.myvh_et_idnum:
				if((18>s.length() && !TextUtils.isDigitsOnly(s)) || 18< s.length()){
					if(null == beforeStr){
						beforeStr = "";
					}
					currentEditText.setText(beforeStr);
				}
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) { }
	
	@Override
	public String toString() {
		return "实名认证";
	}

}
