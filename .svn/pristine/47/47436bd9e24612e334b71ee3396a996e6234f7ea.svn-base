package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Network;
import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.model.AreaData;
import com.whoyao.model.EventInitialRModel;
import com.whoyao.model.InitialEventHotActivityListItem;
import com.whoyao.model.InitialEventHotAreaListItem;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventSearchInitialActivity extends BasicActivity implements
		OnClickListener, OnItemClickListener, OnFocusChangeListener,
		OnKeyListener, TextWatcher {
	private ImageButton imbPageBack;
	private TextView tvClear;
	private TextView tvCancel;
	private EditText etEventSearch;
	private LinearLayout llEventNearby;
//	private InputMethodManager imm;
	private GridView gvHotArea;
	private Intent intent;
	private GridView gvHotEvent;
	private HotAreaAdapter adtHotArea;
	private HotActivityAdapter adtHotEvent;
	private List<InitialEventHotAreaListItem> hotAreaList = new ArrayList<InitialEventHotAreaListItem>();
	private List<InitialEventHotActivityListItem> hotActivityList = new ArrayList<InitialEventHotActivityListItem>();
	private Intent cityIntent;
	private AreaData areaData;
	private LinearLayout llHotArea;
	private LinearLayout llHotEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!AppContext.isNetAvailable()) {
			finish();
		}
		setContentView(R.layout.activity_event_search_intial);
		cityIntent = getIntent();
		areaData = (AreaData) cityIntent.getSerializableExtra(Extra.SelectedItem);
//		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		intent = new Intent(this, EventSearchActivity.class);
		if(areaData != null){
			intent.putExtra(Extra.AddrModel, areaData);
		}
		adtHotArea = new HotAreaAdapter();
		adtHotEvent = new HotActivityAdapter();
		initView();
		gvHotArea.setAdapter(adtHotArea);
		gvHotEvent.setAdapter(adtHotEvent);
		requestData();
		gvHotEvent.setOnItemClickListener(this);
		gvHotArea.setOnItemClickListener(this);
		llEventNearby.setOnClickListener(this);
	}

	/**
	 * 初始化界面控件
	 */
	@SuppressLint("NewApi")
	private void initView() {
		llHotArea = (LinearLayout) findViewById(R.id.event_search_ll_hot_area);
		llHotEvent = (LinearLayout) findViewById(R.id.event_search_ll_event_type);
		imbPageBack = (ImageButton) findViewById(R.id.page_btn_back_initial);
		imbPageBack.setOnClickListener(this);
		tvClear = (TextView) findViewById(R.id.event_search_iv_clear_initial);
		tvClear.setOnClickListener(this);
		tvCancel = (TextView) findViewById(R.id.event_search_tv_cancel);
		tvCancel.setOnClickListener(this);
		etEventSearch = (EditText) findViewById(R.id.event_search_et_initial);
		etEventSearch.setOnFocusChangeListener(this);
		etEventSearch.addTextChangedListener(this);
		etEventSearch.setOnKeyListener(this);
		llEventNearby = (LinearLayout) findViewById(R.id.event_search_ll_nearby);
		gvHotArea = (GridView) findViewById(R.id.event_search_gv_hot_area);
		gvHotEvent = (GridView) findViewById(R.id.event_search_gv_activity_type);
		int version = android.os.Build.VERSION.SDK_INT;
		if(version > 8){
			gvHotArea.setOverScrollMode(View.OVER_SCROLL_NEVER);
			gvHotEvent.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		// 避免出现gridview点击陷进去
		gvHotEvent.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvHotArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back_initial:
			finish();
			break;
		case R.id.event_search_iv_clear_initial:
			etEventSearch.setText("");
			break;
		case R.id.event_search_tv_cancel:
			etEventSearch.setText("");
			etEventSearch.clearFocus();
//			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
			Utils.hideSoftKeyboard(context);
			tvClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.event_search_ll_nearby:// 附近的活动
			if (AppContext.location != null) {
				intent.putExtra(Extra.State, State.Search_Loc);
				// Toast.show(AppContext.location.getAddrStr());
				// TODO 搜索附件的活动
			} else {
				Toast.show("网络定位失败,请检查网络情况");
			}
			if (AppContext.isNetAvailable()) {
				startActivity(intent);
			} else {
				Toast.show(R.string.warn_network_unavailable);
			}
		}
	}

	/**
	 * 从网络上下载数据
	 */
	private void requestData() {
		Net.cacheRequest(new RequestParams(), WebApi.Event.GetInitialEvent,
				new ResponseHandler(true) {
					@Override
					public void onSuccess(String content) {
						EventInitialRModel model = JSON.getObject(content,EventInitialRModel.class);
						hotAreaList = model.getHotAreaListItem();
						hotActivityList = model.getHotActivityListItem();
						if(hotAreaList != null && !hotAreaList.isEmpty()){
							llHotArea.setVisibility(View.VISIBLE);
						}
						if(hotActivityList != null && !hotActivityList.isEmpty()){
							llHotEvent.setVisibility(View.VISIBLE);
						}
						if (areaData!=null && !(areaData.getParentCode() == 110000 || areaData.getCode() == 110000)) {
							llHotArea.setVisibility(View.GONE);
						}else {
							adtHotArea.addAll(hotAreaList);
						}
						adtHotEvent.addAll(hotActivityList);
						adtHotArea.notifyDataSetChanged();
						adtHotEvent.notifyDataSetChanged();
					}
				});

	}

	/**
	 * 活动类型的适配器
	 * 
	 * @author zl
	 * 
	 */
	private class HotActivityAdapter extends BaseAdapter {
		List<InitialEventHotActivityListItem> listHotActivity;

		public HotActivityAdapter() {
			listHotActivity = new ArrayList<InitialEventHotActivityListItem>();
		}

		public void addAll(List<InitialEventHotActivityListItem> data) {
			for (int i = 0; i < 8; i++) {
				listHotActivity.add(data.get(i));
			}
		}

		@Override
		public int getCount() {
			return listHotActivity.size();
		}

		@Override
		public Object getItem(int position) {
			return listHotActivity.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_event_search_initial, null);
			TextView tv_event_initial_area = (TextView) view
					.findViewById(R.id.tv_event_initial);
			tv_event_initial_area.setText(listHotActivity.get(position)
					.getTagname());
			return view;
		}

	}

	/**
	 * 热闹区域的适配器
	 * 
	 * @author zl
	 * 
	 */
	private class HotAreaAdapter extends BaseAdapter {
		ArrayList<InitialEventHotAreaListItem> listHotArea;

		public HotAreaAdapter() {
			listHotArea = new ArrayList<InitialEventHotAreaListItem>();
		}

		public void addAll(List<InitialEventHotAreaListItem> hotAreaList) {
			listHotArea.addAll(hotAreaList);
		}

		public void clearAll() {
			listHotArea.clear();
		}

		@Override
		public int getCount() {
			return listHotArea.size();
		}

		@Override
		public Object getItem(int position) {
			return listHotArea.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_event_search_initial, null);
			TextView tv_event_initial_area = (TextView) view
					.findViewById(R.id.tv_event_initial);
			tv_event_initial_area.setText(listHotArea.get(position)
					.getAreaname());
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.event_search_gv_hot_area:
			intent.putExtra(Extra.State, State.Search_Area);
			InitialEventHotAreaListItem item = adtHotArea.listHotArea
					.get(position);
			int areaId = item.getAreaid();
			intent.putExtra(Extra.SelectedID, areaId);
			intent.putExtra(Extra.SelectedItemStr, item.getAreaname());
			break;
		case R.id.event_search_gv_activity_type:
			intent.putExtra(Extra.State, State.Search_Type);
			int tagId = adtHotEvent.listHotActivity.get(position).getTagid();
//			if (tagId<1||tagId>18) {
//				return;
//			}
			intent.putExtra(Extra.SelectedID, tagId);
			break;
		default:
			break;
		}
		if (AppContext.isNetAvailable()) {
			startActivity(intent);
		} else {
			Toast.show(R.string.warn_network_unavailable);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {// 关键字搜索
		if (KeyEvent.KEYCODE_ENTER == keyCode
				&& KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etEventSearch.getText())) {
				Toast.show("请输入搜索内容");
				return false;
			}
			intent.putExtra(Extra.State, State.Search_Str);
			intent.putExtra(Extra.Search_Keyword, etEventSearch.getText()
					.toString());
			startActivity(intent);
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			etEventSearch.setSelection(etEventSearch.getText().length());
			tvClear.setVisibility(View.VISIBLE);
			tvCancel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etEventSearch.getText().length() == 0) {
			tvClear.setVisibility(View.GONE);
		} else {
			tvClear.setVisibility(View.VISIBLE);
		}
	}

}
