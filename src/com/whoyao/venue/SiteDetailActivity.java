package com.whoyao.venue;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.map.l;
import com.whoyao.Const;
import com.whoyao.Const.State;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.EventAddrMapActivity;
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
import com.whoyao.venue.engine.SiteListAdapter;
import com.whoyao.venue.model.AreaList;
import com.whoyao.venue.model.ConditionList;
import com.whoyao.venue.model.MyBillItem;
import com.whoyao.venue.model.PlaceDataList;
import com.whoyao.venue.model.PlaceList;
import com.whoyao.venue.model.PlaceTimeList;
import com.whoyao.venue.model.PriceList;
import com.whoyao.venue.model.SiteRModel;
import com.whoyao.venue.model.SiteSearchTModel;
import com.whoyao.venue.model.SiteVenueItem;
import com.whoyao.venue.model.VenueItemModel;
import com.whoyao.venue.model.VenueSearchTModel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-4-下午2:26:40
 */
public class SiteDetailActivity extends BasicActivity implements
		OnClickListener, OnFocusChangeListener, OnTouchListener,
		OnItemClickListener, OnRefreshListener2<ListView> {
	private TextView tvArea;
	// filter 价格
	private TextView tvMoney;
	private TextView tvDate;
	private TextView tvTime;
	private View btnBack;
	private View vShadowAll;
	private View vShadow0;
	private View vShadow1;
	private View popType;
	private View popArea;
	private View vAreaBack;
	private View vMoney;
	private View vDate;
	private View vTime;
	private LinearLayout popDate;
	private View popTime;
	private LinearLayout popMoney;
	// 时段里的ListView
	private ListView timeListView;
	private TimeAdapter mTimeAdapter;
	private AreaAdapter mAreaAdapter;
	private String[] money = {};
	private String[] times = {};
	private String[] areas = {};
	private String[] dates = {};
	private ListView areaListView;

	private SiteListAdapter adapter;
	private TextView tv_empty;
	private List<SiteVenueItem> mList = new ArrayList<SiteVenueItem>();
	// private List<EventListItem> mList = new ArrayList<EventListItem>();
	// private List<ConditionList> condtionList = new
	// ArrayList<ConditionList>();
	private ConditionList conditionList;
	private SiteSearchTModel searchModel = new SiteSearchTModel();
	private List<PriceList> priceList = new ArrayList<PriceList>();
	private List<PlaceDataList> placeDataList = new ArrayList<PlaceDataList>();
	private List<PlaceList> placeList = new ArrayList<PlaceList>();
	private List<PlaceTimeList> placeTimeList = new ArrayList<PlaceTimeList>();
	private TextView lvSelectedView;
	protected int selectedCityId = 0;
	protected int selectedDistrictPosition = -1;
	private PullToRefreshListView mPullRefreshListView;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private OnItemClickListener lvItemClick;
	private ResponseHandler initMapHandler;
	private EventMapTModel mapModel;
	private boolean isMapModel;
	private ResponseHandler moreMapHandler;
	private TextView tvType;
	private View vShadow;
	private int venuetype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_list);
		initView();
		initFilter();
		initState();
		initPop();
	}

	private void initState() {
		Intent intent = getIntent();
		// searchModel.setTimespan(5);
		int state = intent.getIntExtra(Extra.State, State.Selected_cancle);

		switch (state) {
		// case State.Search_Str:
		// String keyword = intent.getStringExtra(Extra.Search_Keyword);
		// searchModel.setKeyword(keyword);
		// search();
		// break;
		// case State.Search_Loc:
		// isMapModel = true;
		// mapModel = new EventMapTModel();
		// mapModel.setLatitude(AppContext.location.getLatitude());
		// mapModel.setLongitude(AppContext.location.getLongitude());
		// searchMap();
		// break;
		// case State.Search_Area:
		// int areaID = intent.getIntExtra(Extra.SelectedID,
		// State.Selected_cancle);
		// searchModel.setAreaid(areaID);
		// tvArea.setText(intent.getStringExtra(Extra.SelectedItemStr));
		// // vAllArea.setVisibility(View.GONE);
		// // gvArea.setVisibility(View.VISIBLE);
		// // Toast.show("SearchArea : "+ areaID);
		// search();
		//
		// break;
		case State.Search_Type:
			venuetype = intent.getIntExtra(Extra.SelectedID, State.Selected_cancle);
			searchModel.setPlacetype(intent.getIntExtra(Extra.SelectedID,
					State.Selected_cancle));
			tvType.setText(intent.getStringExtra(Extra.SelectedItemStr));
			// tvItem.setText(intent.getStringExtra(Extra.Snippet));
			searchModel.setFlag(1);
			searchModel.setPlacetime(-1 + "");
			search();
			break;
		default:
			break;
		}
	}

	private void initView() {
		tvType = (TextView) findViewById(R.id.page_tv_title);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		vShadow = findViewById(R.id.v_shadow);
		vShadow.setOnClickListener(this);
		findViewById(R.id.site_type_badminton).setOnClickListener(this);
		findViewById(R.id.site_type_basketball).setOnClickListener(this);
		findViewById(R.id.site_type_football).setOnClickListener(this);
		findViewById(R.id.site_type_pingpong).setOnClickListener(this);
		findViewById(R.id.site_type_tennis).setOnClickListener(this);
		findViewById(R.id.site_iv_map).setOnClickListener(this);
		mTimeAdapter = new TimeAdapter();
		mAreaAdapter = new AreaAdapter();
		timeListView = (ListView) findViewById(R.id.site_list_list_time);
		areaListView = (ListView) findViewById(R.id.site_list_list_area);
		timeListView.setSelector(android.R.color.transparent);
		areaListView.setSelector(android.R.color.transparent);
		timeListView.setOnItemClickListener(this);
		areaListView.setOnItemClickListener(this);
		timeListView.setAdapter(mTimeAdapter);
		areaListView.setAdapter(mAreaAdapter);
		Utils.setListViewHeightForeachChildren(timeListView);
		Utils.setListViewHeightForeachChildren(areaListView);
		tv_empty = (TextView) findViewById(R.id.tv_empty);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.event_search_lv);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);

		vShadowAll = findViewById(R.id.event_search_shadow_all);
		vShadow0 = findViewById(R.id.event_search_shadow_0);
		vShadow1 = findViewById(R.id.event_search_shadow_1);
		vShadow0.setOnClickListener(this);
		vShadow1.setOnClickListener(this);
		vShadowAll.setOnClickListener(this);

		adapter = new SiteListAdapter(mList, LayoutInflater.from(this), this,
				this);
		mPullRefreshListView.getRefreshableView().setEmptyView(tv_empty);
		mPullRefreshListView.setAdapter(adapter);
	}

	public void getListView(boolean isFixed, ListView whichListView) {

		if (isFixed) {
			ViewGroup.LayoutParams params = whichListView.getLayoutParams();
			int totalHeight = 5 * Dp2Px(context, 40);
			params.height = totalHeight;
			whichListView.setLayoutParams(params);

			((BaseAdapter) whichListView.getAdapter()).notifyDataSetChanged();

		} else {
			((BaseAdapter) whichListView.getAdapter()).notifyDataSetChanged();
			whichListView.setVisibility(View.VISIBLE);
			Utils.setListViewHeightForeachChildren(whichListView);
		}

	}

	// event_search_ll_type----->site_list_ll_money tvType-->tvMoney
	private void initFilter() {
		// event_search_tv_area
		tvArea = (TextView) findViewById(R.id.site_list_tv_area);
		tvMoney = (TextView) findViewById(R.id.site_list_tv_money);
		// tvProgress&tvDate event_search_tv_progress&site_list_tv_date
		tvDate = (TextView) findViewById(R.id.site_list_tv_date);
		// 时段 tvPrice event_search_tv_price
		tvTime = (TextView) findViewById(R.id.site_list_tv_time);
		// 区域 vAreaBack
		vAreaBack = findViewById(R.id.site_list_ll_area);
		// 价格vTypeBack
		vMoney = findViewById(R.id.site_list_ll_money);
		// 日期 vProgressBack event_search_ll_progress
		vDate = findViewById(R.id.site_list_ll_date);
		// 时段vPriceBack event_search_ll_price
		vTime = findViewById(R.id.site_list_ll_time);

		FilterClickListener listener = new FilterClickListener();
		vAreaBack.setOnClickListener(listener);
		vMoney.setOnClickListener(listener);
		vDate.setOnClickListener(listener);
		vTime.setOnClickListener(listener);
		tvType.setOnClickListener(listener);
		vAreaBack.setOnTouchListener(this);
		vMoney.setOnTouchListener(this);
		vDate.setOnTouchListener(this);
		vTime.setOnTouchListener(this);
		// tvType.setOnTouchListener(this);

	}

	// event_search_pop_area&site_list_pop_area
	private void initPop() {
		popType = findViewById(R.id.site_pop_type);
		// 区域pop
		popArea = findViewById(R.id.site_list_pop_area);
		// 价格 popType event_search_gv_type
		popMoney = (LinearLayout) findViewById(R.id.site_list_pop_money);
		// 日期date event_search_pop_state popState
		popDate = (LinearLayout) findViewById(R.id.site_list_pop_date);
		// 时段popPrice event_search_pop_price
		popTime = findViewById(R.id.site_list_pop_time);

		lvItemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
				selectedDistrictPosition = -1;
				if (0 == position) {

					// tvArea.setText("区域");
					// districtList.clear();
					// gvAdapter.notifyDataSetInvalidated();
					// hidePop();
					// hideShadow();
					// clearSelected();
					// searchModel.setAreaid(0);//,这里应该是provinceCode
					// search();
				} else {

				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.v_shadow:
			popType.setVisibility(View.GONE);
			vShadow.setVisibility(View.GONE);
			hidePop();
			hideShadow();
			break;
		case R.id.event_search_iv_clear:
			break;
		case R.id.event_search_tv_cancel:
			Utils.hideSoftKeyboard(context);
			break;
		case R.id.site_iv_map:
			Intent intent = new Intent(context, SiteMapModeActivity.class);
			intent.putExtra(Extra.SelectedItem, searchModel);
			startActivity(intent);
			break;
		// case R.id.event_search_tv_joinable:
		// if (v.isSelected()) {// 设置 搜索条件 暂时停用该Button
		// v.setSelected(false);
		// searchModel.setActivityprogress(0);
		// } else {
		// v.setSelected(true);
		// searchModel.setActivityprogress(4);
		// }
		// break;
		case R.id.event_search_shadow_all:
		case R.id.event_search_shadow_0:
		case R.id.event_search_shadow_1:
			hidePop();
			hideShadow();
			clearSelected();
			break;
		case R.id.site_type_badminton:
			clearSearchMode();
			searchModel.setPlacetype(1);
			popType.setVisibility(View.GONE);
			tvType.setText("羽毛球");
			hidePop();
			hideShadow();
			clearSelected();
			search();
			break;
		case R.id.site_type_basketball:
			clearSearchMode();
			searchModel.setPlacetype(4);
			popType.setVisibility(View.GONE);
			tvType.setText("篮球");
			hidePop();
			hideShadow();
			clearSelected();
			search();
			break;
		case R.id.site_type_football:
			clearSearchMode();
			searchModel.setPlacetype(5);
			popType.setVisibility(View.GONE);
			tvType.setText("足球");
			hidePop();
			hideShadow();
			clearSelected();
			search();
			break;
		case R.id.site_type_pingpong:
			clearSearchMode();
			searchModel.setPlacetype(3);
			popType.setVisibility(View.GONE);
			tvType.setText("乒乓球");
			hidePop();
			hideShadow();
			clearSelected();
			search();
			break;
		case R.id.site_type_tennis:
			clearSearchMode();
			searchModel.setPlacetype(2);
			popType.setVisibility(View.GONE);
			tvType.setText("网球");
			hidePop();
			hideShadow();
			clearSelected();
			search();
			break;
		// case R.id.item_tv_site_address:
		// SiteVenueItem item = (SiteVenueItem) adapter.getItem((Integer) v
		// .getTag());
		// if ((int) item.getLatitude() == 0 || (int) item.getLongitude() == 0)
		// {
		// break;
		// }
		// Intent intentAddr = new Intent(this, VenueAddrMapActivity.class);
		// intentAddr.putExtra(Extra.SelectedItem, item);
		// startActivity(intentAddr);
		// break;

		// case R.id.rl_show_more:
		// SiteVenueItem item2 = (SiteVenueItem) adapter.getItem((Integer) v
		// .getTag());
		//
		// Intent intentMore = new Intent(this, VenueDetialActivity.class);
		// intentMore.putExtra(Extra.SelectedID,item2.getVenueid() );
		// // Toast.show(item2.getVenueid()+"");
		// startActivity(intentMore);
		// break;
		}

	}

	/**
	 * 清除搜索条件
	 */
	private void clearSearchMode() {
		tvType.setSelected(false);
		searchModel.setDistrictid(0);
		tvArea.setText("区域");
		searchModel.setPlacedate("");
		tvDate.setText("日期");
		searchModel.setPrice(0);
		tvMoney.setText("价格");
		searchModel.setPlacetime(-1 + "");
		tvTime.setText("时段");
	}

	// TODO fliter的点击事件
	class FilterClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			boolean state = v.isSelected();
			// 复位
			// clearSelected();
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
			// event_search_ll_area&site_list_ll_area
			case R.id.site_list_ll_area:
				tvArea.setSelected(v.isSelected());
				if (v.isSelected()) {
					popArea.setBackgroundResource(R.drawable.main_dropdown_list_bkg_center);
					if (mAreaAdapter.getCount() > 5) {
						getListView(true, areaListView);
					} else {
						getListView(false, areaListView);
					}
					popArea.setVisibility(View.VISIBLE);
				} else {
					popArea.setVisibility(View.GONE);
				}

				break;
			case R.id.site_list_ll_money:
				tvMoney.setSelected(v.isSelected());
				if (v.isSelected()) {
					// addChildView(money,true);
					hidePop();
					addChildView(priceList, true);
					popMoney.setVisibility(View.VISIBLE);
				}
				break;

			case R.id.site_list_ll_date:
				tvDate.setSelected(v.isSelected());
				if (v.isSelected()) {
					// addChildView(dates,false);
					hidePop();
					addChildView2(placeDataList, false);
					popDate.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.site_list_ll_time:
				tvTime.setSelected(v.isSelected());
				if (v.isSelected()) {
					if (mTimeAdapter.getCount() > 5) {
						getListView(true, timeListView);
					} else {
						getListView(false, timeListView);
					}
					popTime.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.page_tv_title:
				if (popType.getVisibility() == View.GONE) {
					popType.setVisibility(View.VISIBLE);
					tvType.setSelected(true);
					popType.setVisibility(View.VISIBLE);
					vShadow.setVisibility(View.VISIBLE);
				} else {
					tvType.setSelected(false);
					popType.setVisibility(View.GONE);
					vShadow.setVisibility(View.GONE);
				}
			default:
				break;
			}

		}

	}

	public void addChildView(final List<PriceList> list, boolean isMoney) {
		// TODO addChildView
		if (list.size()==0) {
			return;
		}
		if (!TextUtils.equals(list.get(0).getName(), "全部")) {
			PriceList allPrice = new PriceList();
			allPrice.setName("全部");
			allPrice.setValue("0");
			list.add(0, allPrice);
		}
		for (int i = 0; i < list.size(); i++) {
			TextView tv = new TextView(context);
			tv.setText(list.get(i).getName());
			tv.setTag(i);
			tv.setPadding(1, 10, 1, 10);
			tv.setBackgroundResource(R.drawable.selector_button_tran_gray);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.show(list.get((Integer) v.getTag()).getValue());
					searchModel.setPrice(Integer.parseInt(list.get(
							(Integer) v.getTag()).getValue()));
					if (TextUtils.equals(list.get((Integer) v.getTag())
							.getName(), "全部")) {
						tvMoney.setText("价格");
					} else {
						tvMoney.setText(list.get((Integer) v.getTag())
								.getName());

					}
					clearSelected();
					hidePop();
					hideShadow();
					search();

				}
			});
			if (isMoney) {
				popMoney.addView(tv);
			} else {
				popDate.addView(tv);
			}
			TextView view = new TextView(context);
			view.setBackgroundResource(R.color.gray_stroke);
			view.setHeight(1);
			if (isMoney) {
				popMoney.addView(view);
			} else {
				popDate.addView(view);
			}

		}

	}

	public void addChildView2(final List<PlaceDataList> list, boolean isMoney) {
		// TODO addChildView
		if (list.size()==0) {
			return;
		}
		if (!TextUtils.equals(list.get(0).getName(), "全部")) {
			PlaceDataList allDate = new PlaceDataList();
			allDate.setName("全部");
			allDate.setValue("");
			list.add(0, allDate);
		}
		for (int i = 0; i < list.size(); i++) {
			TextView tv = new TextView(context);
			String date = list.get(i).getName();
			if (i > 0) {
				if (date.substring(5, date.length()).equals("(今天)")) {
					tv.setText("今天");
				} else {
					tv.setText(list.get(i).getName());
				}
			} else {
				tv.setText(list.get(i).getName());
			}
			tv.setTag(i);
			tv.setPadding(1, 10, 1, 10);
			tv.setBackgroundResource(R.drawable.selector_button_tran_gray);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.show(list.get((Integer) v.getTag()).getValue());
					searchModel.setPlacedate(list.get((Integer) v.getTag())
							.getValue());
					if (TextUtils.equals(list.get((Integer) v.getTag())
							.getName(), "全部")) {
						tvDate.setText("日期");
					} else {
						tvDate.setText(list.get((Integer) v.getTag()).getName());
					}
					clearSelected();
					hidePop();
					hideShadow();
					search();
				}
			});
			if (isMoney) {
				popMoney.addView(tv);
			} else {
				popDate.addView(tv);
			}
			TextView view = new TextView(context);
			view.setBackgroundResource(R.color.gray_stroke);
			view.setHeight(1);
			if (isMoney) {
				popMoney.addView(view);
			} else {
				popDate.addView(view);
			}

		}

	}

	private void setTimeState(View view) {

		view.setSelected(true);
	}

	private void setProgressState(View view) {

		view.setSelected(true);
	}

	private void setPriceState(View view) {

		view.setSelected(true);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
		} else {

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
		tvMoney.setSelected(false);
		tvDate.setSelected(false);
		tvTime.setSelected(false);

		vAreaBack.setSelected(false);
		vMoney.setSelected(false);
		vDate.setSelected(false);
		vTime.setSelected(false);
	}

	private void showShadow(int viewId) {
		switch (viewId) {
		case R.id.site_list_tv_area:
		case R.id.event_search_tv_type:
		case R.id.event_search_tv_time:
		case R.id.site_list_ll_area:
		case R.id.site_list_ll_money:
		case R.id.event_search_ll_time:
			vShadow0.setVisibility(View.GONE);
			vShadow1.setVisibility(View.VISIBLE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;
		case R.id.site_list_tv_date:
		case R.id.site_list_tv_time:
		case R.id.site_list_ll_date:
		case R.id.site_list_ll_time:
			vShadow0.setVisibility(View.VISIBLE);
			vShadow1.setVisibility(View.GONE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void hideShadow() {
		vShadow.setVisibility(View.GONE);
		vShadow0.setVisibility(View.GONE);
		vShadow1.setVisibility(View.GONE);
		vShadowAll.setVisibility(View.GONE);
	}

	private void hidePop() {
		popArea.setVisibility(View.GONE);
		popMoney.setVisibility(View.GONE);
		popMoney.removeAllViews();
		popDate.setVisibility(View.GONE);
		popDate.removeAllViews();
		popTime.setVisibility(View.GONE);
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
		// TODO search
		searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		mPullRefreshListView.setMode(Mode.BOTH);
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ConditionList conditionList = JSON.getObject(content,
							SiteRModel.class).getConditionList();
					ArrayList<SiteVenueItem> venueList = JSON.getObject(
							content, SiteRModel.class).getVenueList();
					priceList.clear();
					placeDataList.clear();
					priceList.addAll(conditionList.getPrice());
					if (mTimeAdapter.getCount() == 0) {
						mTimeAdapter.addTime(conditionList.getPlacetime());
					}
					if (mAreaAdapter.getCount() == 0) {
						mAreaAdapter.addTime(conditionList.getArea());
					}
					if (conditionList.getPlacetime().size() == 0
							|| conditionList.getPlacetime() == null) {
						vTime.setEnabled(false);
					}
					if (conditionList.getArea().size() == 0
							|| conditionList.getArea() == null) {
						vAreaBack.setEnabled(false);
					}
					placeDataList.addAll(conditionList.getPlacedate());
					if (conditionList.getPlacedate().size() == 0
							|| conditionList.getPlacedate() == null) {
						vDate.setEnabled(false);
					}
					if (conditionList.getPrice().size() == 0
							|| conditionList.getPrice() == null) {
						vMoney.setEnabled(false);
					}

					mList.clear();
					mList.addAll(venueList);
					// conditionList = mList.
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
		isMapModel = false;
		Net.request(searchModel, WebApi.Venue.PlaceSearch, initHandler);
	}

	// private void searchMap() {
	// if (initMapHandler == null) {
	// initMapHandler = new ResponseHandler(true) {
	// @Override
	// public void onSuccess(String content) {
	// ArrayList<EventListItem> list = JSON.getObject(content,
	// EventMapRModel.class).getActivityListItem();
	// mList.clear();
	// mList.addAll(list);
	// adapter.notifyDataSetInvalidated();
	// if (isRefresh) {
	// mPullRefreshListView.onRefreshComplete();
	// isRefresh = false;
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable e, int statusCode,
	// String content) {
	// if (400 == statusCode) {
	// mList.clear();
	// adapter.notifyDataSetInvalidated();
	// }
	// if (isRefresh) {
	// mPullRefreshListView.onRefreshComplete();
	// isRefresh = false;
	// }
	// super.onFailure(e, statusCode, content);
	// }
	// };
	// }
	//
	// isMapModel = true;
	// mPullRefreshListView.setMode(Mode.BOTH);
	// // Net.request(searchModel, WebApi.Event.Search, initHandler);
	// Net.cacheRequest(mapModel, WebApi.Event.GetMap, initMapHandler);
	// }

	private void loadmore() {
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<SiteVenueItem> venueList = JSON.getObject(
							content, SiteRModel.class).getVenueList();
					if (null != venueList) {
						mList.addAll(venueList);
						adapter.notifyDataSetChanged();
						mPullRefreshListView.onRefreshComplete();
						if (searchModel.getPagesize() > venueList.size()) {
							mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						}
					} else {
						mPullRefreshListView.onRefreshComplete();
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					mPullRefreshListView.onRefreshComplete();
					isRefresh = false;
					if (400 == statusCode) {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						mPullRefreshListView.setMode(Mode.BOTH);
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		// tModel.setPageindex(tModel.getPageindex() + 1);
		Net.request(searchModel, WebApi.Venue.PlaceSearch, moreHandler);
	}

	// private void loadmoreMap() {
	// mapModel.setTop(mapModel.getTop() + Const.PAGE_SIZE);
	// if (null == moreMapHandler) {
	// moreMapHandler = new ResponseHandler(true) {
	// @Override
	// public void onSuccess(String content) {
	// ArrayList<EventListItem> list = JSON.getObject(content,
	// EventListRModel.class).ActivityListItem;
	// if (null != list && !list.isEmpty()) {
	// mList.clear();
	// }
	// mList.addAll(list);
	// adapter.notifyDataSetChanged();
	// SystemClock.sleep(500);
	// mPullRefreshListView.onRefreshComplete();
	// if (list.size() < mapModel.getSize()) {
	// // mPullRefreshListView.setMode(Mode.PULL_FROM_START);
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable e, int statusCode,
	// String content) {
	// mPullRefreshListView.onRefreshComplete();
	// if (400 == statusCode) {
	// mPullRefreshListView.setMode(Mode.PULL_FROM_START);
	// }
	// super.onFailure(e, statusCode, content);
	// }
	// };
	// }
	// // Net.request(searchModel, WebApi.Event.Search, moreHandler);
	// Net.cacheRequest(mapModel, WebApi.Event.GetMap, initMapHandler);
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int hearders = ((ListView) parent).getHeaderViewsCount();
		// Intent intent = new Intent(this, EventDetialActivity.class);
		// intent.putExtra(Extra.SelectedID, mList.get(position -
		// hearders).getActivityid());
		// startActivity(intent);
		switch (parent.getId()) {
		case R.id.site_list_list_area:

			AreaList item = (AreaList) mAreaAdapter
					.getItem(position - hearders);
			searchModel.setDistrictid(Integer.parseInt(item.getValue()));
			if (TextUtils.equals(item.getName(), "全部")) {
				tvArea.setText("区域");
			} else {
				tvArea.setText(item.getName());
			}
			clearSelected();
			hidePop();
			hideShadow();
			search();
			break;
		case R.id.site_list_list_time:
			PlaceTimeList timeItem = (PlaceTimeList) mTimeAdapter
					.getItem(position - hearders);
			searchModel.setPlacetime(timeItem.getValue());
			if (TextUtils.equals(timeItem.getName(), "全部")) {
				tvTime.setText("时段");
			} else {
				tvTime.setText(timeItem.getName());
			}
			clearSelected();
			hidePop();
			hideShadow();
			search();
			break;
		default:
			SiteVenueItem item2 = (SiteVenueItem) adapter.getItem(position
					- hearders);

			Intent intentMore = new Intent(this, VenueDetialActivity.class);
			intentMore.putExtra(Extra.SelectedID, item2.getVenueid());
			intentMore.putExtra("venuetype", venuetype);
			// Toast.show(item2.getVenueid()+"");
			startActivity(intentMore);
			break;

		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		// if (isMapModel) {
		// searchMap();
		// } else {
		search();
		// }
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// if (isMapModel) {
		// loadmoreMap();
		// } else {
		loadmore();
		// }
	}

	public class TimeAdapter extends BaseAdapter {
		private List<PlaceTimeList> mList = new ArrayList<PlaceTimeList>();

		public void addTime(List<PlaceTimeList> times) {
			PlaceTimeList all = new PlaceTimeList();
			all.setName("全部");
			all.setValue("-1");
			this.mList.add(0, all);
			this.mList.addAll(times);
		}

		public void removeTime() {
			this.mList.clear();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_list_site_time, null);
			TextView tvTime = (TextView) view.findViewById(R.id.tv_list_time);
			tvTime.setText(mList.get(position).getName());
			return view;
		}

	}

	public class AreaAdapter extends BaseAdapter {
		private List<AreaList> mList = new ArrayList<AreaList>();

		public void addTime(List<AreaList> times) {
			AreaList all = new AreaList();
			all.setName("全部");
			all.setValue("0");
			this.mList.add(0, all);
			this.mList.addAll(times);
		}

		public void removeTime() {
			this.mList.clear();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_list_site_time, null);
			TextView tvTime = (TextView) view.findViewById(R.id.tv_list_time);
			tvTime.setText(mList.get(position).getName());
			return view;
		}

	}

	public int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
