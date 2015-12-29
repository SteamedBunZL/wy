package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.WebApi;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.HonestyModel;
import com.whoyao.model.UpUserDetailModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.JSON;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 更多资料
 * @author hyh 
 * creat_at：2013-7-29-上午10:13:49
 */
public class MyMoreinfoActivity extends BasicActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	private boolean isSettingChanged = false;
	private View honestyLl;
	private View realnameLl;
	private View idnumberLl;
	private View verifyTimeLl;
	private View unhonestyLl;
	private EditText etName;
	private UserInfoModel userInfo;
	private int honestyState;
	private TextView unhonestyTv;
	private TextView mobileTv;
	private EditText etQQ;
	private EditText etWeixin;
	private EditText etMSN;
	private EditText etWeibo;
	private TextView emailTv;
	private TextView realnameTv;
	private TextView idnumberTv;
	private TextView verifyTimeTv;
	private HonestyModel honestyInfo;
	private EditText currentEditText;
	private String beforeStr = "";
	private TextView tvEmailEdit;
//	private View ivEmailEdit;
	private TextView tvMobileEdit;
//	private View ivMobileEdit;
	private View ivName;
	private View tvHonesty;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_moreinfo);		
		initView();
	}
	

	private void initView() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		unhonestyLl = findViewById(R.id.more_ll_unhonesty);
		honestyLl = findViewById(R.id.more_ll_honesty);
		realnameLl = findViewById(R.id.more_ll_honesty_realname);
		idnumberLl = findViewById(R.id.more_ll_honesty_idnumber);		
		verifyTimeLl = findViewById(R.id.more_ll_honesty_time);
		
		tvHonesty = findViewById(R.id.more_tv_honesty);
		realnameTv = (TextView) findViewById(R.id.more_tv_honesty_realname);
		idnumberTv = (TextView) findViewById(R.id.more_tv_honesty_idnumber);
		verifyTimeTv = (TextView) findViewById(R.id.more_tv_honesty_time);
		
		etName = (EditText)findViewById(R.id.more_tv_name);
		ivName = findViewById(R.id.more_iv_name);
		unhonestyTv = (TextView)findViewById(R.id.more_tv_unhonesty);
		mobileTv = (TextView)findViewById(R.id.more_tv_mobile);
		tvMobileEdit = (TextView)findViewById(R.id.more_tv_mobile_edit);
//		ivMobileEdit = findViewById(R.id.more_iv_mobile_edit);
		mobileTv = (TextView)findViewById(R.id.more_tv_mobile);
		emailTv = (TextView)findViewById(R.id.more_tv_email);
		tvEmailEdit = (TextView) findViewById(R.id.more_tv_email_edit);
//		ivEmailEdit = findViewById(R.id.more_iv_email_edit);
		unhonestyLl.setOnClickListener(this);
		honestyLl.setOnClickListener(this);
		findViewById(R.id.more_title_back).setOnClickListener(this);
		findViewById(R.id.more_ll_name).setOnClickListener(this);
		findViewById(R.id.more_ll_mobile).setOnClickListener(this);
		findViewById(R.id.more_ll_email).setOnClickListener(this);
		etQQ = (EditText)findViewById(R.id.more_et_qq);
		etWeixin = (EditText)findViewById(R.id.more_et_weixin);
		etMSN = (EditText)findViewById(R.id.more_et_msn);
		etWeibo = (EditText)findViewById(R.id.more_et_weibo);
		etName.addTextChangedListener(this);
		etQQ.addTextChangedListener(this);
		etWeixin.addTextChangedListener(this);
		etMSN.addTextChangedListener(this);
		etWeibo.addTextChangedListener(this);
		etName.setOnFocusChangeListener(this);
		etQQ.setOnFocusChangeListener(this);
		etWeixin.setOnFocusChangeListener(this);
		etMSN.setOnFocusChangeListener(this);
		etWeibo.setOnFocusChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		userInfo = MyinfoManager.getUserInfo();
		if(null != userInfo){
			setHonestyState(userInfo.getUserHonestyState());
			setMobileState(userInfo.getUserMobileState());
			setEmailState(userInfo.getUserEmailState());
			etName.setText(userInfo.getUserRelName());
			etQQ.setText(userInfo.getQQ());
			etWeixin.setText(userInfo.getUserMicroMessage());
			etMSN.setText(userInfo.getMSN());
			etWeibo.setText(userInfo.getUserMicroblog());
			honestyState = userInfo.getUserHonestyState();
			if(State.Honesty_ok == honestyState){
				RequestParams params = new RequestParams();
				Net.request(params, WebApi.User.GetHonestyInfo, new ResponseHandler(true){
					@Override
					public void onSuccess(String content) {
						honestyInfo = JSON.getObject(content, HonestyModel.class);
						idnumberTv.setText(honestyInfo.getUserCode());
						String formatDate = CalendarUtils.formatYMD(honestyInfo.AuditTime);
						verifyTimeTv.setText(formatDate);						
					}
					
					@Override
					public void onFailure(Throwable e, int statusCode, String content) {
						super.onFailure(e, statusCode, content);
					}
				});
			}
		}
		isSettingChanged = false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_title_back:
			onBack();
			break;
		case R.id.more_ll_unhonesty:
			//  去认证
			switch (honestyState) {
			case State.Honesty_none://未认证
				AppContext.startAct(MyVerifyHonestyActivity.class);
				break;
			case State.Honesty_auditing://审核中
				AppContext.startAct(MyHonestyActivity.class);
				break;
			case State.Honesty_reject://已驳回
				AppContext.startAct(MyHonestyActivity.class);
				break;
			default:
				break;
			}
			break;
		case R.id.more_ll_honesty:
			//  展开/未展开,切换时更换右侧图片
			if(View.VISIBLE == realnameLl.getVisibility()){
				honestyLl.setBackgroundResource(R.drawable.selector_radius_bottom);
				tvHonesty.setSelected(false);
				goneHonesty();
			}else{
				honestyLl.setBackgroundResource(R.drawable.selector_radius_middle);
				tvHonesty.setSelected(true);
				showHonesty();
			}						
			break;
		case R.id.more_ll_mobile:
			//  认证or修改
			AppContext.startAct(MyVerifyPhoneActivity.class);
			break;
		case R.id.more_ll_email:
			//  认证or修改
			AppContext.startAct(MyVerifyEmailActivity.class);
			break;
		default:
			break;
		}
		
	}
	
	private void setMobileState(int mobileState) {
		switch (mobileState) {
		case State.Mobile_none:
		case State.Mobile_unvalid:
			tvMobileEdit.setText("未认证");
			tvMobileEdit.setTextColor(getResources().getColor(R.color.red_text));
			mobileTv.setText(userInfo.getUserPhone());
			break;
		case State.Mobile_ok:
			if(!TextUtils.isEmpty(userInfo.getUserPhone())){
				tvMobileEdit.setText("修改");
				tvMobileEdit.setTextColor(getResources().getColor(R.color.smalt_titlebar));
				mobileTv.setText(userInfo.getUserPhone());
			}else{
				tvMobileEdit.setText("未认证");
				tvMobileEdit.setTextColor(getResources().getColor(R.color.red_text));
			}
			break;
		default:
			break;
		}
	}
	private void setEmailState(int mobileState) {
		switch (mobileState) {
		case State.Email_none:
		case State.Email_unvalid:
			tvEmailEdit.setText("未认证");
			tvEmailEdit.setTextColor(getResources().getColor(R.color.red_text));
			emailTv.setText(userInfo.getUserEmail());
			break;
		case State.Email_ok:
//			ivEmailEdit.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(userInfo.getUserEmail())){
				tvEmailEdit.setText("修改");
				tvEmailEdit.setTextColor(getResources().getColor(R.color.smalt_titlebar));
				emailTv.setText(userInfo.getUserEmail());
			}else{
				tvEmailEdit.setText("未认证");
				tvEmailEdit.setTextColor(getResources().getColor(R.color.red_text));
			}
			break;
		default:
			break;
		}
	}
	private void showHonesty(){
		realnameLl.setVisibility(View.VISIBLE);
		idnumberLl.setVisibility(View.VISIBLE);
		verifyTimeLl.setVisibility(View.VISIBLE);
	}
	private void goneHonesty(){
		realnameLl.setVisibility(View.GONE);
		idnumberLl.setVisibility(View.GONE);
		verifyTimeLl.setVisibility(View.GONE);
	}
	private void setHonestyState(int state){
		honestyState = state;
		switch (honestyState) {
		case State.Honesty_none:
			unhonestyTv.setText("未认证");			
			break;
		case State.Honesty_ok:		
			unhonestyLl.setVisibility(View.GONE);
			honestyLl.setVisibility(View.VISIBLE);
			realnameTv.setText(userInfo.getUserRelName());
			etName.setEnabled(false);
			ivName.setVisibility(View.INVISIBLE);
			break;
		case State.Honesty_auditing:
			unhonestyTv.setText(R.string.state_checking);
			etName.setEnabled(false);
			ivName.setVisibility(View.INVISIBLE);
			break;
		case State.Honesty_reject:
			unhonestyTv.setText(R.string.state_check_fail);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBack() {
		if(isSettingChanged){
			userInfo = MyinfoManager.getUserInfo();
			String name = etName.getText().toString();
			if(!TextUtils.isEmpty(name)){
				if(!FormatUtils.isChineseOnly(name) || 2>name.length() || 8<name.length()){
					Toast.show(R.string.real_name_wrong);
					return true;
				}
			}
			String qq = etQQ.getText().toString();
			if(!TextUtils.isEmpty(qq) && TextUtils.isDigitsOnly(qq) &&(5> qq.length() || 11< qq.length())){
				Toast.show(R.string.qq_wrong);
				return true;
			}
			String weixin = etWeixin.getText().toString();
			if(!TextUtils.isEmpty(weixin)){
				if(TextUtils.isDigitsOnly(weixin)){
					if (5> weixin.length() || 11< weixin.length()){
						Toast.show(R.string.wechat_wrong);
						return true;
					}
				}else if(!FormatUtils.isEmailAddr(weixin)){
					Toast.show(R.string.wechat_wrong);
					return true;
				}
			}
			String msn = etMSN.getText().toString();
			if(!TextUtils.isEmpty(msn) && !FormatUtils.isEmailAddr(msn)){
				Toast.show(R.string.msn_wrong);
				return true;
			}
			String weibo = etWeibo.getText().toString();
			if(!TextUtils.isEmpty(weibo) && !FormatUtils.isHttpAddr(weibo)){
				Toast.show(R.string.blog_wrong);
				return true;
			}
			userInfo.setQQ(qq);
			userInfo.setMSN(msn);
			userInfo.setUserMicroblog(weibo);
			userInfo.setUserMicroMessage(weixin);
			userInfo.setUserRelName(name);
			UpUserDetailModel model = new UpUserDetailModel(userInfo);
			MyinfoManager.getManager().upUserInfo(model);
		}
		
		finish();
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		return true;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		beforeStr = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		if(null != currentEditText){
//			switch (currentEditText.getId()) {
//			case R.id.more_tv_name:
//				if(!TextUtils.isEmpty(s) && !FormatUtils.isChineseOnly(s.toString())){
//					if(null == beforeStr){
//						beforeStr = "";
//					}
//					etName.setText(beforeStr);
//				}
//				break;
//			case R.id.more_et_qq:
//				if(!TextUtils.isEmpty(s) && (!TextUtils.isDigitsOnly(s) || 11 < s.length())){
//					if(null == beforeStr){
//						beforeStr = "";
//					}
//					etName.setText(beforeStr);
//				}
//				break;
//			default:
//				break;
//			}
//		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		isSettingChanged  = true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			currentEditText = (EditText)v;
			currentEditText.setSelection(currentEditText.getText().length());
		}else{
			if(currentEditText == v){
				currentEditText = null;
			}
		}
	}
	
	@Override
	public String toString() {
		return "更多资料";
	}
}
