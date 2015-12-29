package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.adapter.UserListAdapter;
import com.whoyao.db.AreaListDB;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.model.AreaData;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.model.UserSearchTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-4-下午2:26:40
 */
public class UserSearchActivity extends BasicActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher, OnKeyListener,
		OnTouchListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private InputMethodManager imm;
	private String[] ages;
	private EditText etSearch;
	private View ivClear;
	private TextView tvCancel;

	private View vAreaBack;
	private View vSexBack;
	private View vAgeBack;
	private TextView tvArea;
	private TextView tvSex;
	private TextView tvAge;
	private View vShadowAll;

	private View popArea;
	private View popAge;
	private View popSex;

	private BaseAdapter adapter;
	private List<UserListItemModel> mList = new ArrayList<UserListItemModel>();
	private UserSearchTModel searchModel = new UserSearchTModel();

	private GridView gvArea;
	private ArrayList<AreaData> cityList;
	private BaseAdapter gvAdapter;
	protected int selectedCity = -1;
	private AreaListDB db;
	private int provinceCode;
	private PullToRefreshListView mPullRefreshListView;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private View tvNearby;
	private View tvCity;
	private View vDistance;
	private View vClearLoc;
	private MessageDialog clearDialog;
	private View[] sexViews = new View[3];
	private View[] ageViews = new View[9];
	private int selectSex;
	private int selectAge;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		ages = getResources().getStringArray(R.array.age_group);

		db = new AreaListDB();
		provinceCode = 110000;// TODO 目前只有北京
		initView();
		initFilter();
		initState();
		initPop();
	}

	private void initState() {
		Intent intent = getIntent();
		int state = intent.getIntExtra(Extra.State, State.Selected_cancle);
		switch (state) {
		case State.Search_Str:
			String keyword = intent.getStringExtra(Extra.Search_Keyword);
			etSearch.setText(keyword);
			searchModel.setKeyword(keyword);
			search();
			break;
		case State.Search_Loc:
			searchModel.setSize(0.5f);
			tvArea.setText("500m");
			search();
			break;
		case State.Search_Sex:
			selectSex = intent.getIntExtra(Extra.Sex, State.Sex_None);
			if (State.Sex_Man == selectSex) {
				tvSex.setText(R.string.user_sex_man);
			} else if (State.Sex_Woman == selectSex) {
				tvSex.setText(R.string.user_sex_woman);
			}
			searchModel.setUsersex(selectSex);
			search();
			break;
		case State.Search_Age:
			selectAge = intent.getIntExtra(Extra.SelectedItem, 0);
			if (0 < selectAge) {
				tvAge.setText(ages[selectAge]);
				searchModel.setUserage(selectAge);
			}
			search();
			break;
		default:
			break;
		}
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		vClearLoc = findViewById(R.id.page_btn_right);
		vClearLoc.setVisibility(View.INVISIBLE);
		vClearLoc.setOnClickListener(this);
		vClearLoc.setSelected(UserSettingManager.getManager().isShowPoint());
		etSearch = (EditText) findViewById(R.id.user_search_et);
		tvCancel = (TextView) findViewById(R.id.user_search_tv_cancel);
		ivClear = findViewById(R.id.user_search_iv_clear);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.user_search_lv);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);

		vShadowAll = findViewById(R.id.user_search_shadow_all);

		ivClear.setOnClickListener(this);
		tvCancel.setOnClickListener(this);

		vShadowAll.setOnClickListener(this);

		etSearch.setOnFocusChangeListener(this);
		etSearch.addTextChangedListener(this);
		etSearch.setOnKeyListener(this);
		adapter = new UserListAdapter(LayoutInflater.from(this), mList);
		mPullRefreshListView.setAdapter(adapter);
	}

	private void initFilter() {
		tvArea = (TextView) findViewById(R.id.user_search_tv_area);
		tvSex = (TextView) findViewById(R.id.user_search_tv_sex);
		tvAge = (TextView) findViewById(R.id.user_search_tv_age);

		vAreaBack = findViewById(R.id.user_search_ll_area);
		vSexBack = findViewById(R.id.user_search_ll_sex);
		vAgeBack = findViewById(R.id.user_search_ll_age);

		FilterClickListener listener = new FilterClickListener();
		vAreaBack.setOnClickListener(listener);
		vSexBack.setOnClickListener(listener);
		vAgeBack.setOnClickListener(listener);

		vAreaBack.setOnTouchListener(this);
		vSexBack.setOnTouchListener(this);
		vAgeBack.setOnTouchListener(this);
	}

	private void initPop() {
		popArea = findViewById(R.id.user_search_pop_area);
		gvArea = (GridView) findViewById(R.id.user_search_gv_area);
		vDistance = findViewById(R.id.user_search_ll_distance);
		tvNearby = findViewById(R.id.user_search_tv_nearby);
		tvCity = findViewById(R.id.user_search_tv_city);
		tvNearby.setOnClickListener(this);
		tvCity.setOnClickListener(this);

		popSex = findViewById(R.id.user_search_pop_sex);
		popAge = findViewById(R.id.user_search_pop_age);

		PopClickListener listener = new PopClickListener();

		findViewById(R.id.user_search_tv_distance_500).setOnClickListener(listener);
		findViewById(R.id.user_search_tv_distance_1).setOnClickListener(listener);
		findViewById(R.id.user_search_tv_distance_2).setOnClickListener(listener);
		findViewById(R.id.user_search_tv_distance_5).setOnClickListener(listener);
		findViewById(R.id.user_search_tv_distance_7).setOnClickListener(listener);
		
		sexViews[0] = findViewById(R.id.user_search_sex_none);
		sexViews[1] = findViewById(R.id.user_search_sex_man);
		sexViews[2] = findViewById(R.id.user_search_sex_woman);
		sexViews[0].setOnClickListener(listener);
		sexViews[1].setOnClickListener(listener);
		sexViews[2].setOnClickListener(listener);
		sexViews[selectSex].setSelected(true);

		ageViews[0] = findViewById(R.id.user_search_age_0);
		ageViews[1] = findViewById(R.id.user_search_age_0_18);
		ageViews[2] = findViewById(R.id.user_search_age_18_25);
		ageViews[3] = findViewById(R.id.user_search_age_25_28);
		ageViews[4] = findViewById(R.id.user_search_age_28_32);
		ageViews[5] = findViewById(R.id.user_search_age_32_35);
		ageViews[6] = findViewById(R.id.user_search_age_35_38);
		ageViews[7] = findViewById(R.id.user_search_age_38_45);
		ageViews[8] = findViewById(R.id.user_search_age_45_100);
		ageViews[0].setOnClickListener(listener);
		ageViews[1].setOnClickListener(listener);
		ageViews[2].setOnClickListener(listener);
		ageViews[3].setOnClickListener(listener);
		ageViews[4].setOnClickListener(listener);
		ageViews[5].setOnClickListener(listener);
		ageViews[6].setOnClickListener(listener);
		ageViews[7].setOnClickListener(listener);
		ageViews[8].setOnClickListener(listener);
		ageViews[selectAge].setSelected(true);

		cityList = db.getChildArea(provinceCode);
		gvAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = null;
				if (null == convertView) {
					convertView = View.inflate(context,
							R.layout.item_event_pop_grid, null);
					tv = (TextView) convertView.findViewById(R.id.item_pop_tv);
					convertView.setTag(tv);
				} else {
					tv = (TextView) convertView.getTag();
				}
				if (0 == position && 0 < cityList.size()) {
					tv.setText("不限");
				} else {
					tv.setText(cityList.get(position - 1).getAreaName());
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return cityList.size() + 1;
			}
		};
		gvArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvArea.setAdapter(gvAdapter);
		gvArea.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hidePop();
				hideShadow();
				clearSelected();
				if (0 == position) {
					searchModel.setUserdistrict(0);
					searchModel.setSize(0);
					tvArea.setText("区域");
				} else {
					searchModel.setUserdistrict(cityList.get(position - 1)
							.getCode());
					tvArea.setText(cityList.get(position - 1).getAreaName());
				}
				search();
			}
		});

	}

	private void setSexState(View v){
		int length = sexViews.length;
		for(int i = 0;i<length;i++){
			sexViews[i].setSelected(false);
		}
		v.setSelected(true);
	}
	private void setAgeState(View v){
		int length = ageViews.length;
		for(int i = 0;i<length;i++){
			ageViews[i].setSelected(false);
		}
		v.setSelected(true);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.page_btn_right:
			if (!vClearLoc.isSelected()) {
				if (clearDialog == null) {
					clearDialog = new MessageDialog(context);
					clearDialog.setTitle("清除位置");
					clearDialog.setMessage("清除位置信息后，别人就没有办法找到你了。确定继续？");
					clearDialog.setLeftButton("确定", this);
					clearDialog.setRightButton("取消", null);
				}
				clearDialog.show();
			}
			break;
		case R.id.user_search_iv_clear:
			etSearch.setText("");
			break;
		case R.id.user_search_tv_cancel:
			etSearch.setText("");
			etSearch.clearFocus();
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			ivClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.user_search_tv_nearby:
			tvNearby.setSelected(true);
			tvCity.setSelected(false);
			gvArea.setVisibility(View.GONE);
			vDistance.setVisibility(View.VISIBLE);
			break;
		case R.id.user_search_tv_city:
			tvCity.setSelected(true);
			tvNearby.setSelected(false);
			vDistance.setVisibility(View.GONE);
			gvArea.setVisibility(View.VISIBLE);
			break;
		case R.id.user_search_shadow_all:
			hidePop();
			hideShadow();
			clearSelected();
			break;
		case MessageDialog.ID_LEFT_BUTTON:
			vClearLoc.setSelected(true);
			UserSettingManager manager = UserSettingManager.getManager();
			manager.setUserSafeSetting(false, manager.isShowEvent());
			break;
		default:
			break;
		}
	}

	class FilterClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			boolean state = v.isSelected();
			// 复位
			clearSelected();
			hidePop();
			v.setSelected(!state);
			// 设置状态
			if (state) {
				hideShadow();
			} else {
				showShadow();
			}
			// 每个按键的响应事件
			switch (v.getId()) {
			case R.id.user_search_ll_area:
				tvArea.setSelected(v.isSelected());
				if (v.isSelected()) {
					popArea.setBackgroundResource(R.drawable.main_dropdown_list_bkg_left);
					tvCity.setSelected(false);
					tvNearby.setSelected(true);
					gvArea.setVisibility(View.GONE);
					vDistance.setVisibility(View.VISIBLE);
					popArea.setVisibility(View.VISIBLE);
				} else {
					popArea.setVisibility(View.GONE);
				}
				break;
			case R.id.user_search_ll_sex:
				tvSex.setSelected(v.isSelected());
				if (v.isSelected()) {
					popSex.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.user_search_ll_age:
				tvAge.setSelected(v.isSelected());
				if (v.isSelected()) {
					popAge.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	}

	class PopClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			hidePop();
			hideShadow();
			clearSelected();
			switch (v.getId()) {
			case R.id.user_search_tv_nearby:
				tvNearby.setSelected(true);
				tvCity.setSelected(false);
				gvArea.setVisibility(View.GONE);
				vDistance.setVisibility(View.VISIBLE);
				break;
			case R.id.user_search_tv_city:
				tvNearby.setSelected(false);
				tvCity.setSelected(true);
				vDistance.setVisibility(View.GONE);
				gvArea.setVisibility(View.VISIBLE);
				break;
			case R.id.user_search_tv_distance_500:
				searchModel.setUserdistrict(0);
				searchModel.setSize(0.5f);
				tvArea.setText("500m");
				break;
			case R.id.user_search_tv_distance_1:
				searchModel.setUserdistrict(0);
				searchModel.setSize(1);
				tvArea.setText("1km");
				break;
			case R.id.user_search_tv_distance_2:
				searchModel.setUserdistrict(0);
				searchModel.setSize(2);
				tvArea.setText("2km");
				break;
			case R.id.user_search_tv_distance_5:
				searchModel.setUserdistrict(0);
				searchModel.setSize(5);
				tvArea.setText("5km");
				break;
			case R.id.user_search_tv_distance_7:
				searchModel.setUserdistrict(0);
				searchModel.setSize(7);
				tvArea.setText("7km");
				break;
			case R.id.user_search_sex_none:
				searchModel.setUsersex(State.Selected_cancle);
				tvSex.setText(R.string.user_sex);
				setSexState(v);
				break;
			case R.id.user_search_sex_man:
				searchModel.setUsersex(State.Sex_Man);
				tvSex.setText(R.string.user_sex_man);
				setSexState(v);
				break;
			case R.id.user_search_sex_woman:
				searchModel.setUsersex(State.Sex_Woman);
				tvSex.setText(R.string.user_sex_woman);
				setSexState(v);
				break;
			case R.id.user_search_age_0:
				searchModel.setUserage(0);
				tvAge.setText("年龄");
				setAgeState(v);
				break;
			case R.id.user_search_age_0_18:
				searchModel.setUserage(1);
				tvAge.setText(R.string.user_age_0_18);
				setAgeState(v);
				break;
			case R.id.user_search_age_18_25:
				searchModel.setUserage(2);
				tvAge.setText(R.string.user_age_18_25);
				setAgeState(v);
				break;
			case R.id.user_search_age_25_28:
				searchModel.setUserage(3);
				tvAge.setText(R.string.user_age_25_28);
				setAgeState(v);
				break;
			case R.id.user_search_age_28_32:
				searchModel.setUserage(4);
				tvAge.setText(R.string.user_age_28_32);
				setAgeState(v);
				break;
			case R.id.user_search_age_32_35:
				searchModel.setUserage(5);
				tvAge.setText(R.string.user_age_32_35);
				setAgeState(v);
				break;
			case R.id.user_search_age_35_38:
				searchModel.setUserage(6);
				tvAge.setText(R.string.user_age_35_38);
				setAgeState(v);
				break;
			case R.id.user_search_age_38_45:
				searchModel.setUserage(7);
				tvAge.setText(R.string.user_age_38_45);
				setAgeState(v);
				break;
			case R.id.user_search_age_45_100:
				searchModel.setUserage(8);
				tvAge.setText(R.string.user_age_45_100);
				setAgeState(v);
				break;
			default:
				break;
			}
			if (UserSettingManager.getManager().isShowPoint()) {
				search();
			} else {
				MessageDialog dialog = new MessageDialog(context);
				dialog.setTitle("操作提示");
				dialog.setMessage("使用此功能将开启“设置>安全和隐私>让附近的人看见我”功能，确定继续？");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						UserSettingManager manager = UserSettingManager
								.getManager();
						manager.setUserSafeSetting(true, manager.isShowEvent());
						search();
					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();

			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			etSearch.setSelection(etSearch.getText().length());
			ivClear.setVisibility(View.VISIBLE);
			tvCancel.setVisibility(View.VISIBLE);
		} else {

		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode
				&& KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etSearch.getText())) {
				Toast.show("请输入要搜索的关键字");
			} else {
				searchModel.setKeyword(etSearch.getText().toString());
				search();
			}
		}
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (0 == etSearch.getText().length()) {
			ivClear.setVisibility(View.GONE);
		} else {
			ivClear.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected boolean onBack() {
		if (View.VISIBLE == vShadowAll.getVisibility()) {
			hidePop();
			hideShadow();
			clearSelected();
		} else {
			finish();
		}
		return true;
	}

	private void clearSelected() {
		tvArea.setSelected(false);
		tvSex.setSelected(false);
		tvAge.setSelected(false);

		vAreaBack.setSelected(false);
		vSexBack.setSelected(false);
		vAgeBack.setSelected(false);
	}

	private void showShadow() {
		vShadowAll.setVisibility(View.VISIBLE);
	}

	private void hideShadow() {
		vShadowAll.setVisibility(View.GONE);
	}

	private void hidePop() {
		popArea.setVisibility(View.GONE);
		popAge.setVisibility(View.GONE);
		popSex.setVisibility(View.GONE);
	}

	// 修补Filter区域点击效果
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.isSelected()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			((ViewGroup) v).getChildAt(0).setSelected(true);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_OUTSIDE:
			((ViewGroup) v).getChildAt(0).setSelected(false);
			break;
		}
		return false;
	}

	private void search() {
		searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		mPullRefreshListView.setMode(Mode.BOTH);
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content,
							UserListRModel.class).getList();
					mList.clear();
					mList.addAll(list);
					adapter.notifyDataSetInvalidated();
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					if (400 == statusCode) {
						mList.clear();
						adapter.notifyDataSetInvalidated();
					}
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		Net.request(searchModel, WebApi.User.Search, initHandler);
	}

	private void loadmore() {
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content,
							UserListRModel.class).getList();
					mList.addAll(list);
					adapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					if (list.size() < searchModel.getPagesize()) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					mPullRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		Net.request(searchModel, WebApi.User.Search, moreHandler);
		// TODO
		// UserSettingManager.getManager().isShowPoint();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int hearders = ((ListView) parent).getHeaderViewsCount();
		int userID = mList.get(position - hearders).getUserid();
		UserEngine.startUserDetial(userID);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		search();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadmore();
	}

}
