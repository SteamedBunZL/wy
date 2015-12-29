package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.Const;
import com.whoyao.Const.State;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.adapter.EventListAdapter;
import com.whoyao.db.AreaListDB;
import com.whoyao.model.AreaData;
import com.whoyao.model.EventListItem;
import com.whoyao.model.EventListRModel;
import com.whoyao.model.EventMapRModel;
import com.whoyao.model.EventMapTModel;
import com.whoyao.model.EventSearchTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
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
public class EventSearchActivity extends BasicActivity implements OnClickListener, OnFocusChangeListener, TextWatcher,
		OnKeyListener, OnTouchListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private EditText etSearch;
	private View ivClear;
	private TextView tvCancel;
	private TextView tvArea;
	private TextView tvType;
	private TextView tvTime;
	private TextView tvProgress;
	private TextView tvPrice;
	private TextView tvJoinable;
	private View btnBack;
	private View vShadowAll;
	private View vShadow0;
	private View vShadow1;
	private View popArea;
	private View vAreaBack;
	private View vTypeBack;
	private View vTimeBack;
	private View vProgressBack;
	private View vPriceBack;
	private View popTime;
	private View popState;
	private View popPrice;
	private GridView popType;
	private String[] tags;

	private BaseAdapter adapter;
	private TextView tv_empty;
	private List<EventListItem> mList = new ArrayList<EventListItem>();
	private EventSearchTModel searchModel = new EventSearchTModel();

	private ListView lvArea;
	private GridView gvArea;
	private BaseAdapter lvAdapter;
	private TextView lvSelectedView;
	private ArrayList<AreaData> cityList;
	private BaseAdapter gvAdapter;
	private ArrayList<AreaData> districtList;
	protected int selectedCityId = 0;
	protected int selectedDistrictPosition = -1;
	private AreaListDB db;
	private PullToRefreshListView mPullRefreshListView;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private AreaData selectArea;
	private OnItemClickListener lvItemClick;
	private ResponseHandler initMapHandler;
	private EventMapTModel mapModel;
	private boolean isMapModel;
	private ResponseHandler moreMapHandler;
	private TextView tvAllArea;
	private View vAllArea;
	protected int selectedCityPosition;
	private AreaData cityAreaData;
	protected int selectType;
	private View vTime0;
	private View vTime1;
	private View vTime2;
	private View vTime3;
	private View vTime4;
	private View vProgress0;
	private View vProgress1;
	private View vProgress2;
	private View vProgress3;
	private View vProgress4;
	private View vPrice0;
	private View vPrice1;
	private View vPrice2;
	/** 北京 */
	private static final int DEFAULT_CITY_CODE = 110000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_search);
		tags = getResources().getStringArray(R.array.event_type);
		db = new AreaListDB();
		initView();
		initFilter();
		initState();
		initPop();
	}

	private void initState() {
		Intent intent = getIntent();
		// searchModel.setTimespan(5);
		int state = intent.getIntExtra(Extra.State, State.Selected_cancle);
		cityAreaData = (AreaData) intent.getSerializableExtra(Extra.AddrModel);
		if (cityAreaData == null) {// 默认为北京
			cityAreaData = new AreaData();
			cityAreaData.setCode(DEFAULT_CITY_CODE);
			cityAreaData.setParentCode(DEFAULT_CITY_CODE);
		}
		searchModel.setUsercity(cityAreaData.getCode());

		switch (state) {
		case State.Search_Str:
			String keyword = intent.getStringExtra(Extra.Search_Keyword);
			etSearch.setText(keyword);
			searchModel.setKeyword(keyword);
			search();
			break;
		case State.Search_Loc:
			isMapModel = true;
			mapModel = new EventMapTModel();
			mapModel.setLatitude(AppContext.location.getLatitude());
			mapModel.setLongitude(AppContext.location.getLongitude());
			searchMap();
			// TODO 开始搜索,需要增加根据坐标搜索
			break;
		case State.Search_Area:
			int areaID = intent.getIntExtra(Extra.SelectedID, State.Selected_cancle);
			searchModel.setAreaid(areaID);
			tvArea.setText(intent.getStringExtra(Extra.SelectedItemStr));
			selectArea = db.getAreaByCode(areaID);
			AreaData districtArea = db.getAreaByCode(selectArea.getParentCode());
			// vAllArea.setVisibility(View.GONE);
			// gvArea.setVisibility(View.VISIBLE);
			// Toast.show("SearchArea : "+ areaID);
			// TODO 开始搜索,搜索初始化界面,需要提供有效数据
			search();

			break;
		case State.Search_Type:
			selectType = intent.getIntExtra(Extra.SelectedID, State.Selected_cancle);
			if (0 < selectType || selectType <18) {
				tvType.setText(tags[selectType]);
				searchModel.setTypeid(selectType);
				search();
			}
			break;
		default:
			break;
		}
	}

	private void initView() {
		btnBack = findViewById(R.id.page_btn_back);
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		etSearch = (EditText) findViewById(R.id.event_search_et);
		tvCancel = (TextView) findViewById(R.id.event_search_tv_cancel);
		ivClear = findViewById(R.id.event_search_iv_clear);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.event_search_lv);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);

		vShadowAll = findViewById(R.id.event_search_shadow_all);
		vShadow0 = findViewById(R.id.event_search_shadow_0);
		vShadow1 = findViewById(R.id.event_search_shadow_1);

		btnBack.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		tvCancel.setOnClickListener(this);

		vShadow0.setOnClickListener(this);
		vShadow1.setOnClickListener(this);
		vShadowAll.setOnClickListener(this);

		etSearch.setOnFocusChangeListener(this);
		etSearch.addTextChangedListener(this);
		etSearch.setOnKeyListener(this);
		adapter = new EventListAdapter(mList, LayoutInflater.from(this));
		mPullRefreshListView.getRefreshableView().setEmptyView(tv_empty);
		mPullRefreshListView.setAdapter(adapter);
	}

	private void initFilter() {
		tvArea = (TextView) findViewById(R.id.event_search_tv_area);
		tvType = (TextView) findViewById(R.id.event_search_tv_type);
		tvTime = (TextView) findViewById(R.id.event_search_tv_time);
		tvProgress = (TextView) findViewById(R.id.event_search_tv_progress);
		tvPrice = (TextView) findViewById(R.id.event_search_tv_price);
		tvJoinable = (TextView) findViewById(R.id.event_search_tv_joinable);

		vAreaBack = findViewById(R.id.event_search_ll_area);
		vTypeBack = findViewById(R.id.event_search_ll_type);
		vTimeBack = findViewById(R.id.event_search_ll_time);
		vProgressBack = findViewById(R.id.event_search_ll_progress);
		vPriceBack = findViewById(R.id.event_search_ll_price);

		FilterClickListener listener = new FilterClickListener();
		vAreaBack.setOnClickListener(listener);
		vTypeBack.setOnClickListener(listener);
		vTimeBack.setOnClickListener(listener);
		vProgressBack.setOnClickListener(listener);
		vPriceBack.setOnClickListener(listener);

		vAreaBack.setOnTouchListener(this);
		vTypeBack.setOnTouchListener(this);
		vTimeBack.setOnTouchListener(this);
		vProgressBack.setOnTouchListener(this);
		vPriceBack.setOnTouchListener(this);

		tvJoinable.setOnClickListener(this);
	}

	private void initPop() {
		popArea = findViewById(R.id.event_search_pop_area);
		lvArea = (ListView) findViewById(R.id.event_search_lv_area);
		gvArea = (GridView) findViewById(R.id.event_search_gv_area);
		vAllArea = findViewById(R.id.event_search_ll_area_all);
		tvAllArea = (TextView) findViewById(R.id.event_search_tv_area_all);
		tvAllArea.setSelected(true);

		popType = (GridView) findViewById(R.id.event_search_gv_type);
		popTime = findViewById(R.id.event_search_pop_time);
		popState = findViewById(R.id.event_search_pop_state);
		popPrice = findViewById(R.id.event_search_pop_price);
		PopClickListener listener = new PopClickListener();
		tvAllArea.setOnClickListener(listener);
		vTime0 = findViewById(R.id.event_search_time_0);
		vTime1 = findViewById(R.id.event_search_time_1);
		vTime2 = findViewById(R.id.event_search_time_2);
		vTime3 = findViewById(R.id.event_search_time_3);
		vTime4 = findViewById(R.id.event_search_time_4);
		vTime0.setOnClickListener(listener);
		vTime1.setOnClickListener(listener);
		vTime2.setOnClickListener(listener);
		vTime3.setOnClickListener(listener);
		vTime4.setOnClickListener(listener);
		vTime0.setSelected(true);
		vProgress0 = findViewById(R.id.event_search_prog_0);
		vProgress1 = findViewById(R.id.event_search_prog_1);
		vProgress2 = findViewById(R.id.event_search_prog_2);
		vProgress3 = findViewById(R.id.event_search_prog_3);
		vProgress4 = findViewById(R.id.event_search_prog_4);
		vProgress0.setOnClickListener(listener);
		vProgress1.setOnClickListener(listener);
		vProgress2.setOnClickListener(listener);
		vProgress3.setOnClickListener(listener);
		vProgress4.setOnClickListener(listener);
		vProgress0.setSelected(true);
		vPrice0 = findViewById(R.id.event_search_price_0);
		vPrice1 = findViewById(R.id.event_search_price_1);
		vPrice2 = findViewById(R.id.event_search_price_2);
		vPrice0.setOnClickListener(listener);
		vPrice1.setOnClickListener(listener);
		vPrice2.setOnClickListener(listener);
		vPrice0.setSelected(true);
		districtList = new ArrayList<AreaData>();

		if (cityAreaData.getCode() == DEFAULT_CITY_CODE) {
			cityList = db.getChildArea(DEFAULT_CITY_CODE);
		} else {
			cityList = new ArrayList<AreaData>();
//			cityAreaData.setAreaName("全城");
			cityList.add(cityAreaData);
			vAllArea.setVisibility(View.GONE);
			gvArea.setVisibility(View.VISIBLE);
			ArrayList<AreaData> cities = db.getChildArea(cityAreaData.getCode());
			districtList.addAll(cities);
			// cityList = db.getChildArea(cityAreaData.getParentCode());
			
			
			ArrayList<AreaData> districts = db.getChildArea(cityAreaData.getCode());
			districtList.clear();
			if (districts != null) {
				districtList.addAll(districts);
			}
			if (View.VISIBLE != gvArea.getVisibility()) {
				vAllArea.setVisibility(View.GONE);
				gvArea.setVisibility(View.VISIBLE);
			}
		}
		if (null != selectArea) {
			vAllArea.setVisibility(View.GONE);
			gvArea.setVisibility(View.VISIBLE);
			for (int i = 0; i < cityList.size(); i++) {
				if (cityList.get(i).getCode() == selectArea.getParentCode()) {
					selectedCityPosition = i + 1;// TODO 需要初始化 selectDistrict
					selectedCityId = selectArea.getParentCode();
				}
			}
			if (selectedCityId != 0) {
				ArrayList<AreaData> cities = db.getChildArea(selectArea.getParentCode());
				districtList.addAll(cities);
				for (int i = 0; i < districtList.size(); i++) {
					if (districtList.get(i).getCode() == selectArea.getCode()) {
						selectedDistrictPosition = i + 1;// TODO 需要初始化 selectDistrict
					}
				}
			}
		}
		lvAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = null;
				if (null == convertView) {
					convertView = View.inflate(context, R.layout.item_event_pop_lv_area, null);
					tv = (TextView) convertView.findViewById(R.id.item_pop_tv);
					convertView.setTag(tv);
				} else {
					tv = (TextView) convertView.getTag();
				}

				if (cityAreaData.getCode() == DEFAULT_CITY_CODE) {
					if (0 == position) {
						tv.setText("不限");
					} else {
						AreaData areaData = cityList.get(position - 1);
						tv.setText(areaData.getAreaName());
					}
					if (position == selectedCityPosition) {
						tv.setSelected(true);
					} else {
						tv.setSelected(false);
					}
				} else {
					AreaData areaData = cityList.get(position);
					tv.setText("全城");
					tv.setSelected(true);
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
				if (cityAreaData.getCode() == DEFAULT_CITY_CODE) {
					return cityList.size() + 1;
				} else {
					return cityList.size();
				}
			}
		};
		lvArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lvArea.setAdapter(lvAdapter);
		lvItemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// if(view.getTag() == lvSelectedView){
				// return;
				// }
				// if (null != lvSelectedView) {
				// lvSelectedView.setSelected(false);
				// (lvSelectedView).setTextColor(Color.BLUE);
				// }
				// lvSelectedView = (TextView) view.getTag();
				// lvSelectedView.setSelected(true);
				// ((TextView)lvSelectedView).setTextColor(Color.RED);
				selectedCityPosition = position;
				if (position > 0) {
					selectedCityId = cityList.get(position - 1).getCode();
				} else {
					selectedCityId = 0;
				}
				selectedDistrictPosition = -1;
				lvAdapter.notifyDataSetChanged();
				if (0 == position) {

					tvAllArea.setSelected(false);
					gvArea.setVisibility(View.GONE);
					vAllArea.setVisibility(View.VISIBLE);
					// tvArea.setText("区域");
					// districtList.clear();
					// gvAdapter.notifyDataSetInvalidated();
					// hidePop();
					// hideShadow();
					// clearSelected();
					// searchModel.setAreaid(0);// TODO ,这里应该是provinceCode
					// search();
				} else {

					AreaData areaData = cityList.get(position - 1);
					// tvArea.setText(areaData.getAreaName());
					ArrayList<AreaData> cities = db.getChildArea(areaData.getCode());
					districtList.clear();
					if (cities != null) {
						districtList.addAll(cities);
					}
					gvAdapter.notifyDataSetInvalidated();
					if (View.VISIBLE != gvArea.getVisibility()) {
						vAllArea.setVisibility(View.GONE);
						gvArea.setVisibility(View.VISIBLE);
					}
				}
			}
		};
		if (cityAreaData.getCode() == DEFAULT_CITY_CODE) {
			lvArea.setOnItemClickListener(lvItemClick);
		}
		gvAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = null;
				if (null == convertView) {
					convertView = View.inflate(context, R.layout.item_event_pop_grid, null);
					tv = (TextView) convertView.findViewById(R.id.item_pop_tv);
					convertView.setTag(tv);
				} else {
					tv = (TextView) convertView.getTag();
				}
				if (0 == position && 0 < districtList.size()) {
					tv.setText("不限");
				} else {
					tv.setText(districtList.get(position - 1).getAreaName());
				}
				L.i("position: " + position + " select: " + selectedDistrictPosition);
				if (position == selectedDistrictPosition) {
					tv.setSelected(true);
				} else {
					tv.setSelected(false);
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
				int count = districtList.size();
				if (0 < count) {
					++count;
				}
				return count;
			}
		};
		gvArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvArea.setAdapter(gvAdapter);
		gvArea.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				hidePop();
				hideShadow();
				clearSelected();

				selectedDistrictPosition = position;
				gvAdapter.notifyDataSetChanged();
				if (0 == position) {
					searchModel.setAreaid(0);
					searchModel.setUsercity(cityAreaData.getCode());
					if(cityAreaData.getCode() == DEFAULT_CITY_CODE){
						tvArea.setText(cityList.get(selectedCityPosition - 1).getAreaName());
					}else{
						tvArea.setText("区域");
					}
				} else {
					searchModel.setUsercity(0);
					searchModel.setAreaid(districtList.get(position - 1).getCode());
					tvArea.setText(districtList.get(position - 1).getAreaName());
				}
				search();
			}
		});

		popType.setSelector(new ColorDrawable(Color.TRANSPARENT));
		popType.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = View.inflate(context, R.layout.item_event_pop_grid, null);
				TextView tv = (TextView) v.findViewById(R.id.item_pop_tv);
				if(position == selectType){
					tv.setSelected(true);
				}else{
					tv.setSelected(false);
				}
				tv.setText(tags[position]);
				if (tags.length % 3 >= 19 - position) {
					v.findViewById(R.id.item_pop_line_h).setVisibility(View.GONE);
				}
				if (2 == position % 3) {
					v.findViewById(R.id.item_pop_line_v).setVisibility(View.GONE);
				}
				return v;
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
				return tags.length;
			}
		});
		popType.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectType = position;
				((BaseAdapter)popType.getAdapter()).notifyDataSetChanged();
				hidePop();
				hideShadow();
				clearSelected();
				searchModel.setTypeid(position);
				if (0 == position) {
					tvType.setText("类型");
				} else {
					tvType.setText(tags[position]);
				}
				searchModel.setTypeid(position);
				search();
			}
		});
		if (gvArea.getVisibility() == View.VISIBLE) {
			gvAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.event_search_iv_clear:
			etSearch.setText("");
			break;
		case R.id.event_search_tv_cancel:
			etSearch.setText("");
			etSearch.clearFocus();
			Utils.hideSoftKeyboard(context);
			ivClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.event_search_tv_joinable:
			if (v.isSelected()) {// TODO 设置 搜索条件 暂时停用该Button
				v.setSelected(false);
				searchModel.setActivityprogress(0);
			} else {
				v.setSelected(true);
				searchModel.setActivityprogress(4);
			}
			break;
		case R.id.event_search_shadow_all:
		case R.id.event_search_shadow_0:
		case R.id.event_search_shadow_1:
			hidePop();
			hideShadow();
			clearSelected();
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
				showShadow(v.getId());
			}
			// 每个按键的响应事件
			switch (v.getId()) {
			case R.id.event_search_ll_area:
				tvArea.setSelected(v.isSelected());
				if (v.isSelected()) {
					popArea.setBackgroundResource(R.drawable.main_dropdown_list_bkg_left);
					popArea.setVisibility(View.VISIBLE);
				} else {
					popArea.setVisibility(View.GONE);
				}
				if (selectedCityPosition > -1) {
					lvArea.setSelection(selectedCityPosition);
				}
				if (selectedDistrictPosition > -1) {
					gvArea.setSelection(selectedDistrictPosition);
				}
				break;
			case R.id.event_search_ll_type:
				tvType.setSelected(v.isSelected());
				if (v.isSelected()) {
					popType.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.event_search_ll_time:
				tvTime.setSelected(v.isSelected());
				if (v.isSelected()) {
					popTime.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.event_search_ll_progress:
				tvProgress.setSelected(v.isSelected());
				if (v.isSelected()) {
					popState.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.event_search_ll_price:
				tvPrice.setSelected(v.isSelected());
				if (v.isSelected()) {
					popPrice.setVisibility(View.VISIBLE);
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
			case R.id.event_search_tv_area_all:
				tvAllArea.setSelected(true);
				tvArea.setText("区域");
				districtList.clear();
				searchModel.setAreaid(0);// TODO ,这里应该是provinceCode
				searchModel.setUsercity(cityAreaData.getCode());
				break;
			case R.id.event_search_time_0:
				searchModel.setTimespan(0);
				tvTime.setText("时间");
				setTimeState(v);
				break;
			case R.id.event_search_time_1:
				searchModel.setTimespan(1);
				tvTime.setText("今天");
				setTimeState(v);
				break;
			case R.id.event_search_time_2:
				searchModel.setTimespan(3);
				tvTime.setText("三天内");
				setTimeState(v);
				break;
			case R.id.event_search_time_3:
				searchModel.setTimespan(4);
				tvTime.setText("一周内");
				setTimeState(v);
				break;
			case R.id.event_search_time_4:
				searchModel.setTimespan(5);
				tvTime.setText("一月内");
				setTimeState(v);
				break;
			case R.id.event_search_prog_0:
				searchModel.setActivityprogress(0);
				tvProgress.setText("状态");
				setProgressState(v);
				break;
			case R.id.event_search_prog_1:
				searchModel.setActivityprogress(4);
				tvProgress.setText("正在报名");
				setProgressState(v);
				break;
			case R.id.event_search_prog_2:
				searchModel.setActivityprogress(1);
				tvProgress.setText("即将开始");
				setProgressState(v);
				break;
			case R.id.event_search_prog_3:
				searchModel.setActivityprogress(2);
				tvProgress.setText("进行中");
				setProgressState(v);
				break;
			case R.id.event_search_prog_4:
				searchModel.setActivityprogress(3);
				tvProgress.setText("已结束");
				setProgressState(v);
				break;
			case R.id.event_search_price_0:
				searchModel.setOrder(-1);
				tvPrice.setText("费用");
				setPriceState(v);
				break;
			case R.id.event_search_price_1:
				searchModel.setOrder(5);
				tvPrice.setText("人均由少到多");
				setPriceState(v);
				break;
			case R.id.event_search_price_2:
				searchModel.setOrder(4);
				tvPrice.setText("人均由多到少");
				setPriceState(v);
				break;
			default:
				break;
			}
			search();
		}
	}

	private void setTimeState(View view){
		vTime0.setSelected(false);
		vTime1.setSelected(false);
		vTime2.setSelected(false);
		vTime3.setSelected(false);
		vTime4.setSelected(false);
		view.setSelected(true);
	}
	
	private void setProgressState(View view){
		vProgress0.setSelected(false);
		vProgress1.setSelected(false);
		vProgress2.setSelected(false);
		vProgress3.setSelected(false);
		vProgress4.setSelected(false);
		view.setSelected(true);
	}
	private void setPriceState(View view){
		vPrice0.setSelected(false);
		vPrice1.setSelected(false);
		vPrice2.setSelected(false);
		view.setSelected(true);
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
		if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etSearch.getText())) {
				Toast.show("请输入要搜索的关键字");
			} else {
				searchModel.setKeyword(etSearch.getText().toString());

				searchModel.setKeyword(etSearch.getText().toString());
				searchModel.setAreaid(0);
				tvArea.setText("区域");
				selectedCityId = 0;
				searchModel.setTypeid(0);
				tvType.setText("类型");
				searchModel.setTimespan(0);
				tvTime.setText("时间");
				searchModel.setActivityprogress(0);
				tvProgress.setText("状态");
				searchModel.setOrder(-1);
				tvPrice.setText("费用");
				searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);

				search();
			}
		}
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
		tvType.setSelected(false);
		tvTime.setSelected(false);
		tvProgress.setSelected(false);
		tvPrice.setSelected(false);

		vAreaBack.setSelected(false);
		vTypeBack.setSelected(false);
		vTimeBack.setSelected(false);
		vProgressBack.setSelected(false);
		vPriceBack.setSelected(false);
	}

	private void showShadow(int viewId) {
		switch (viewId) {
		case R.id.event_search_tv_area:
		case R.id.event_search_tv_type:
		case R.id.event_search_tv_time:
		case R.id.event_search_ll_area:
		case R.id.event_search_ll_type:
		case R.id.event_search_ll_time:
			vShadow0.setVisibility(View.GONE);
			vShadow1.setVisibility(View.VISIBLE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;
		case R.id.event_search_tv_progress:
		case R.id.event_search_tv_price:
		case R.id.event_search_ll_progress:
		case R.id.event_search_ll_price:
			vShadow0.setVisibility(View.VISIBLE);
			vShadow1.setVisibility(View.GONE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void hideShadow() {
		vShadow0.setVisibility(View.GONE);
		vShadow1.setVisibility(View.GONE);
		vShadowAll.setVisibility(View.GONE);
	}

	private void hidePop() {
		popArea.setVisibility(View.GONE);
		popType.setVisibility(View.GONE);
		popTime.setVisibility(View.GONE);
		popState.setVisibility(View.GONE);
		popPrice.setVisibility(View.GONE);
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
					ArrayList<EventListItem> list = JSON.getObject(content, EventListRModel.class).ActivityListItem;
					mList.clear();
					mList.addAll(list);
					adapter.notifyDataSetInvalidated();
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
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
		isMapModel = false;
		Net.request(searchModel, WebApi.Event.Search, initHandler);
	}

	private void searchMap() {
		if (initMapHandler == null) {
			initMapHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<EventListItem> list = JSON.getObject(content, EventMapRModel.class).getActivityListItem();
					mList.clear();
					mList.addAll(list);
					adapter.notifyDataSetInvalidated();
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
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

		isMapModel = true;
		mPullRefreshListView.setMode(Mode.BOTH);
		// Net.request(searchModel, WebApi.Event.Search, initHandler);
		Net.cacheRequest(mapModel, WebApi.Event.GetMap, initMapHandler);
	}

	private void loadmore() {
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<EventListItem> list = JSON.getObject(content, EventMapRModel.class).getActivityListItem();
					mList.addAll(list);
					adapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					if (list.size() < searchModel.getPagesize()) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					mPullRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		Net.request(searchModel, WebApi.Event.Search, moreHandler);
	}

	private void loadmoreMap() {
		mapModel.setTop(mapModel.getTop() + Const.PAGE_SIZE);
		if (null == moreMapHandler) {
			moreMapHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<EventListItem> list = JSON.getObject(content, EventListRModel.class).ActivityListItem;
					if (null != list && !list.isEmpty()) {
						mList.clear();
					}
					mList.addAll(list);
					adapter.notifyDataSetChanged();
					SystemClock.sleep(500);
					mPullRefreshListView.onRefreshComplete();
					if (list.size() < mapModel.getSize()) {
						// mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					mPullRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		// Net.request(searchModel, WebApi.Event.Search, moreHandler);
		Net.cacheRequest(mapModel, WebApi.Event.GetMap, initMapHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int hearders = ((ListView) parent).getHeaderViewsCount();
		Intent intent = new Intent(this, EventDetialActivity.class);
		intent.putExtra(Extra.SelectedID, mList.get(position - hearders).getActivityid());
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		if (isMapModel) {
			searchMap();
		} else {
			search();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (isMapModel) {
			loadmoreMap();
		} else {
			loadmore();
		}
	}

}
