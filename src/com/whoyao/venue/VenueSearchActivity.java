package com.whoyao.venue;

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
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.EventMapModeActivity;
import com.whoyao.adapter.UserListAdapter;
import com.whoyao.db.AreaListDB;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.model.AreaData;
import com.whoyao.model.EventMapTModel;
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
import com.whoyao.venue.engine.VenueListAdapter;
import com.whoyao.venue.model.VenueItemModel;
import com.whoyao.venue.model.VenueSearchTModel;

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
public class VenueSearchActivity extends BasicActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher, OnKeyListener,
		OnTouchListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private InputMethodManager imm;
	private String[] ages;
	private EditText etSearch;
	private View ivClear;
	private TextView tvCancel;

	private View vAreaBack;
	private View vItemBack;
	private TextView tvArea;
	private TextView tvItem;
	private View vShadowAll;

	private View popArea;
	private View popItem;

	private BaseAdapter adapter;
	private List<VenueItemModel> mList = new ArrayList<VenueItemModel>();
	private VenueSearchTModel searchModel = new VenueSearchTModel();

	private GridView gvArea;
	private ArrayList<AreaData> cityList;
	private BaseAdapter gvAdapter;
	protected int selectedCity = -1;
	private AreaListDB db;
	private AreaData cityAreaData;
	private int provinceCode;
	private PullToRefreshListView mPullRefreshListView;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private View tvNearby;
	private View tvCity;
	private View vDistance;
//	private View vClearLoc;
	private AreaData selectArea;
	private MessageDialog clearDialog;
	// ageViews/itemViews
	private View[] itemViews = new View[9];
	private int selectAge;
	/** 北京 */
	private static final int DEFAULT_CITY_CODE = 110000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venu_search);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		ages = getResources().getStringArray(R.array.age_group);
		findViewById(R.id.event_iv_map).setOnClickListener(this);
		db = new AreaListDB();
		provinceCode = 110000;// TODO 目前只有北京
		initView();
		initFilter();
		initState();
		initPop();
	}

	private void initState() {
		Intent intent = getIntent();
		cityAreaData = (AreaData) intent.getSerializableExtra(Extra.AddrModel);
		if (cityAreaData == null) {// 默认为北京
			cityAreaData = new AreaData();
			cityAreaData.setCode(DEFAULT_CITY_CODE);
			cityAreaData.setParentCode(DEFAULT_CITY_CODE);
		}
		searchModel.setCityid(cityAreaData.getCode());
		int state = intent.getIntExtra(Extra.State, State.Selected_cancle);
		switch (state) {
//		case State.Search_Str:
//			String keyword = intent.getStringExtra(Extra.Search_Keyword);
//			etSearch.setText(keyword);
//			searchModel.setKeyword(keyword);
//			search();
//			break;
		case State.Search_Loc:
			searchModel.setSize(7);
			tvArea.setText("7km");
			search();
			break;
		case State.Search_Area:
			int districtId = intent.getIntExtra(Extra.SelectedID, State.Selected_cancle);
			searchModel.setDistrictid(districtId);
			tvArea.setText(intent.getStringExtra(Extra.Snippet));
			selectArea = db.getAreaByCode(districtId);
			AreaData districtArea = db.getAreaByCode(selectArea.getParentCode());
			// vAllArea.setVisibility(View.GONE);
			// gvArea.setVisibility(View.VISIBLE);
			// Toast.show("SearchArea : "+ areaID);
			// TODO 开始搜索,搜索初始化界面,需要提供有效数据
			search();
			break;
		// case State.Search_Age:
		// selectAge = intent.getIntExtra(Extra.SelectedItem, 0);
		// if (0 < selectAge) {
		// tvItem.setText(ages[selectAge]);
		// searchModel.setUserage(selectAge);
		// }
		// search();
		// break;
		case State.Search_Type:
			searchModel.setPlacetype(intent.getIntExtra(Extra.SelectedID, State.Selected_cancle));
			tvItem.setText(intent.getStringExtra(Extra.Snippet));
			search();
		default:
			break;
		}
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
//		vClearLoc = findViewById(R.id.page_btn_right);
//		vClearLoc.setVisibility(View.INVISIBLE);
//		vClearLoc.setOnClickListener(this);
//		vClearLoc.setSelected(UserSettingManager.getManager().isShowPoint());
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
		adapter = new VenueListAdapter(context, mList);
		mPullRefreshListView.setAdapter(adapter);
	}

	private void initFilter() {
		tvArea = (TextView) findViewById(R.id.user_search_tv_area);
		// user_search_tv_age/venue_search_tv_item tvAge/tvItem
		tvItem = (TextView) findViewById(R.id.venue_search_tv_item);

		vAreaBack = findViewById(R.id.user_search_ll_area);
		// TODO user_search_ll_age/venue_search_ll_item vAgeBack/vItemBack
		vItemBack = findViewById(R.id.venue_search_ll_item);

		FilterClickListener listener = new FilterClickListener();
		vAreaBack.setOnClickListener(listener);
		vItemBack.setOnClickListener(listener);

		vAreaBack.setOnTouchListener(this);
		vItemBack.setOnTouchListener(this);
	}

	private void initPop() {
		popArea = findViewById(R.id.user_search_pop_area);
		gvArea = (GridView) findViewById(R.id.user_search_gv_area);
		vDistance = findViewById(R.id.user_search_ll_distance);
		tvNearby = findViewById(R.id.user_search_tv_nearby);
		tvCity = findViewById(R.id.user_search_tv_city);
		tvNearby.setOnClickListener(this);
		tvCity.setOnClickListener(this);
		// user_search_pop_age/venue_search_pop_item popAge/popItem
		popItem = findViewById(R.id.venue_search_pop_item);

		PopClickListener listener = new PopClickListener();

		findViewById(R.id.user_search_tv_distance_500).setOnClickListener(
				listener);
		findViewById(R.id.user_search_tv_distance_1).setOnClickListener(
				listener);
		findViewById(R.id.user_search_tv_distance_2).setOnClickListener(
				listener);
		findViewById(R.id.user_search_tv_distance_5).setOnClickListener(
				listener);
		findViewById(R.id.user_search_tv_distance_7).setOnClickListener(
				listener);

		itemViews[0] = findViewById(R.id.user_search_age_0);
		itemViews[1] = findViewById(R.id.user_search_age_0_18);
		itemViews[2] = findViewById(R.id.user_search_age_18_25);
		itemViews[3] = findViewById(R.id.user_search_age_25_28);
		itemViews[4] = findViewById(R.id.user_search_age_28_32);
//		itemViews[5] = findViewById(R.id.user_search_age_32_35);
//		itemViews[6] = findViewById(R.id.user_search_age_35_38);
//		itemViews[7] = findViewById(R.id.user_search_age_38_45);
//		itemViews[8] = findViewById(R.id.user_search_age_45_100);
		itemViews[0].setOnClickListener(listener);
		itemViews[1].setOnClickListener(listener);
		itemViews[2].setOnClickListener(listener);
		itemViews[3].setOnClickListener(listener);
		itemViews[4].setOnClickListener(listener);
//		itemViews[5].setOnClickListener(listener);
//		itemViews[6].setOnClickListener(listener);
//		itemViews[7].setOnClickListener(listener);
//		itemViews[8].setOnClickListener(listener);

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
					searchModel.setDistrictid(0);
					searchModel.setSize(0);
					tvArea.setText("区域");
				} else {
					searchModel.setDistrictid(cityList.get(position - 1)
							.getCode());
					tvArea.setText(cityList.get(position - 1).getAreaName());
				}
				search();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
//		case R.id.page_btn_right:
//			if (!vClearLoc.isSelected()) {
//				if (clearDialog == null) {
//					clearDialog = new MessageDialog(context);
//					clearDialog.setTitle("清除位置");
//					clearDialog.setMessage("清除位置信息后，别人就没有办法找到你了。确定继续？");
//					clearDialog.setLeftButton("确定", this);
//					clearDialog.setRightButton("取消", null);
//				}
//				clearDialog.show();
//			}
//			break;
		
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
//			vClearLoc.setSelected(true);
			UserSettingManager manager = UserSettingManager.getManager();
			manager.setUserSafeSetting(false, manager.isShowEvent());
			break;
		case R.id.event_iv_map:
			Intent intent = new Intent(context,VenueMapModeActivity.class);
			startActivity(intent);
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
			case R.id.venue_search_ll_item:
				tvItem.setSelected(v.isSelected());
				if (v.isSelected()) {
					popItem.setVisibility(View.VISIBLE);
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
				searchModel.setDistrictid(0);
				searchModel.setSize(0.5f);
				tvArea.setText("500m");
				break;
			case R.id.user_search_tv_distance_1:
				searchModel.setDistrictid(0);
				searchModel.setSize(1);
				tvArea.setText("1km");
				break;
			case R.id.user_search_tv_distance_2:
				searchModel.setDistrictid(0);
				searchModel.setSize(2);
				tvArea.setText("2km");
				break;
			case R.id.user_search_tv_distance_5:
				searchModel.setDistrictid(0);
				searchModel.setSize(5);
				tvArea.setText("5km");
				break;
			case R.id.user_search_tv_distance_7:
				searchModel.setDistrictid(0);
				searchModel.setSize(7);
				tvArea.setText("7km");
				break;
			case R.id.user_search_age_0:
				searchModel.setPlacetype(1);
				tvItem.setText("羽毛球");
				break;
			case R.id.user_search_age_0_18:
				searchModel.setPlacetype(5);
				tvItem.setText("网球");
				break;
			case R.id.user_search_age_18_25:
				searchModel.setPlacetype(2);
				tvItem.setText("乒乓球");
				break;

			case R.id.user_search_age_25_28:
				searchModel.setPlacetype(3);
				tvItem.setText("篮球");
				break;
			case R.id.user_search_age_28_32:
				searchModel.setPlacetype(4);
				tvItem.setText("足球");
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
				searchModel.setSize(0);
				searchModel.setPlacetype(0);
				tvArea.setText("区域");
				tvItem.setText("项目");
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
		tvItem.setSelected(false);

		vAreaBack.setSelected(false);
		vItemBack.setSelected(false);
	}

	private void showShadow() {
		vShadowAll.setVisibility(View.VISIBLE);
	}

	private void hideShadow() {
		vShadowAll.setVisibility(View.GONE);
	}

	private void hidePop() {
		popArea.setVisibility(View.GONE);
		popItem.setVisibility(View.GONE);
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
					ArrayList<VenueItemModel> list = JSON.getList(content,
							VenueItemModel.class);
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
		Net.request(searchModel, WebApi.Venue.VenueSearch, initHandler);
	}

	private void loadmore() {
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<VenueItemModel> list = JSON.getList(content,
							VenueItemModel.class);
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
		Net.request(searchModel,WebApi.Venue.VenueSearch, moreHandler);
		// TODO
		// UserSettingManager.getManager().isShowPoint();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 int hearders = ((ListView) parent).getHeaderViewsCount();
		 VenueItemModel item = (VenueItemModel) adapter.getItem(position-hearders);
		 Intent intent = new Intent(context,VenueDetialActivity.class);
		 intent.putExtra(Extra.SelectedID, item.getVenueid());
		 startActivity(intent);
		// int userID = mList.get(position - hearders).getUserid();
		// UserEngine.startUserDetial(userID);
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
