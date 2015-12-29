package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UserSearchInitialActivity extends BasicActivity implements OnClickListener, OnFocusChangeListener,
		OnKeyListener, TextWatcher {
	private TextView tvClear;
	private TextView tvCancel;
	private EditText etSearch;
	private View vNearby;
	private InputMethodManager imm;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search_intial);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		intent = new Intent(this, UserSearchActivity.class);
		initView();

	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		tvClear = (TextView) findViewById(R.id.user_search_iv_clear_initial);
		tvClear.setOnClickListener(this);
		tvCancel = (TextView) findViewById(R.id.user_search_tv_cancel);
		tvCancel.setOnClickListener(this);
		etSearch = (EditText) findViewById(R.id.user_search_et_initial);
		etSearch.setOnFocusChangeListener(this);
		etSearch.addTextChangedListener(this);
		etSearch.setOnKeyListener(this);
		vNearby = findViewById(R.id.user_search_tv_nearby);
		vNearby.setOnClickListener(this);
		findViewById(R.id.user_search_tv_sex_man).setOnClickListener(this);
		findViewById(R.id.user_search_tv_sex_woman).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_0_18).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_18_25).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_25_28).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_28_32).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_32_35).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_35_38).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_38_45).setOnClickListener(this);
		findViewById(R.id.user_search_tv_age_45_100).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.user_search_iv_clear_initial:
			etSearch.setText("");
			break;
		case R.id.user_search_tv_cancel:
			etSearch.setText("");
			etSearch.clearFocus();
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			tvClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.user_search_tv_nearby:
			boolean showPoint = UserSettingManager.getManager().isShowPoint();
			if(showPoint){
				if (AppContext.location != null) {
					intent.putExtra(Extra.State, State.Search_Loc);
					startActivity(intent);
				} else {
					Toast.show("获取当前位置失败");
				}
			}else{
				MessageDialog dialog = new MessageDialog(context);
				dialog.setTitle("操作提示");
				dialog.setMessage("使用此功能将开启“设置>安全和隐私>让附近的人看见我”功能，确定继续？");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						UserSettingManager manager = UserSettingManager.getManager();
						manager.setUserSafeSetting(true, manager.isShowEvent());
						intent.putExtra(Extra.State, State.Search_Loc);
						startActivity(intent);
					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
			}
			break;
		case R.id.user_search_tv_sex_man:
			//TODO 从男点过去传男
			intent.putExtra(Extra.State, State.Search_Sex);
			intent.putExtra(Extra.Sex, State.Sex_Man);
			startActivity(intent);
			break;
		case R.id.user_search_tv_sex_woman:
			intent.putExtra(Extra.State, State.Search_Sex);
			intent.putExtra(Extra.Sex, State.Sex_Woman);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_0_18:
			//TODO 从岁数点过去传岁数
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 0);
			intent.putExtra(Extra.Max, 18);
			intent.putExtra(Extra.SelectedItem, 1);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_18_25:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 18);
			intent.putExtra(Extra.Max, 25);
			intent.putExtra(Extra.SelectedItem, 2);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_25_28:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 25);
			intent.putExtra(Extra.Max, 28);
			intent.putExtra(Extra.SelectedItem, 3);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_28_32:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 28);
			intent.putExtra(Extra.Max, 32);
			intent.putExtra(Extra.SelectedItem, 4);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_32_35:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 32);
			intent.putExtra(Extra.Max, 35);
			intent.putExtra(Extra.SelectedItem, 5);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_35_38:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 35);
			intent.putExtra(Extra.Max, 38);
			intent.putExtra(Extra.SelectedItem, 6);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_38_45:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 38);
			intent.putExtra(Extra.Max, 45);
			intent.putExtra(Extra.SelectedItem, 7);
			startActivity(intent);
			break;
		case R.id.user_search_tv_age_45_100:
			intent.putExtra(Extra.State, State.Search_Age);
			intent.putExtra(Extra.Min, 45);
			intent.putExtra(Extra.Max, 100);
			intent.putExtra(Extra.SelectedItem, 8);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {// 关键字搜索
		if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etSearch.getText())) {
				Toast.show("请输入搜索内容");
				return false;
			}
			intent.putExtra(Extra.State, State.Search_Str);
			intent.putExtra(Extra.Search_Keyword, etSearch.getText().toString());
			startActivity(intent);
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			etSearch.setSelection(etSearch.getText().length());
			// tvClear.setVisibility(View.VISIBLE);
			 tvCancel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etSearch.getText().length() == 0) {
			tvClear.setVisibility(View.GONE);
		} else {
			tvClear.setVisibility(View.VISIBLE);
		}
	}

}
