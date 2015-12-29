package com.whoyao.topic;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.Const.Extra;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.adapter.TopicListAdapter;
import com.whoyao.model.SystemTagItem;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.model.TopicSearchTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicSearchActivity extends BasicActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher, OnKeyListener,
		OnTouchListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private Intent intent;
	private int tagId = 0;
	private String tagString = "";
	private TopicSearchTModel model;
	private EditText etSearch;
	private View ivClear;
	private TextView tvCancel;
	private TextView tvType;
	private TextView tvPrice;
	private View vShadowAll;
	// private View vShadow0;
	// private View vShadow1;
	private View vTypeBack;
	private View vPriceBack;
	private GridView popType;
	private View popPrice;
	private String[] tags;
	private List<TopicItemRModel> mList = new ArrayList<TopicItemRModel>();
	private PullToRefreshListView mPullRefreshListView;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private View vPrice0;
	private View vPrice1;
	private int order;

	private BaseAdapter adapter;
	private TextView tv_empty;
	private List<SystemTagItem> systemlist;
	private SystemTagItem selectedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_search);
		tags = getResources().getStringArray(R.array.event_type);
		intent = getIntent();
		systemlist = (List<SystemTagItem>) intent
				.getSerializableExtra(Extra.Search_List);
		selectedItem = (SystemTagItem) intent.getSerializableExtra(Extra.SelectedItem);
		SystemTagItem newItem = new SystemTagItem();
		newItem.setTagid(0);
		newItem.setTag("不限");
		systemlist.add(0, newItem);
		tagId = intent.getIntExtra(Extra.SelectedID, 0);
		tagString = intent.getStringExtra(Extra.Search_Keyword);
		if (tagString == null) {
			tagString = "";
		}
		System.out.println(tagId + "****" + tagString);
		model = new TopicSearchTModel();
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		model.setPagesize(Const.PAGE_SIZE);
		order=1;
		model.setOrder(order);
		model.setKeyword(tagString);
		model.setTagid(tagId);
		// Net.request(model, WebApi.Topic.TopicSearch,new ResponseHandler());
		initView();
		search();
		initFilter();
		initPop();
	}
	
	public void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		etSearch = (EditText) findViewById(R.id.topic_search_et_2);
		etSearch.setText(tagString);
		tvCancel = (TextView) findViewById(R.id.topic_search_tv_cancel);
		ivClear = findViewById(R.id.topic_search_iv_clear);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.topic_search_lv_2);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);
		vShadowAll = findViewById(R.id.topic_search_shadow_all);
		// vShadow0 = findViewById(R.id.topic_search_shadow_0);
		// vShadow1 = findViewById(R.id.topic_search_shadow_1);
		ivClear.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		vShadowAll.setOnClickListener(this);
		// vShadow0.setOnClickListener(this);
		// vShadow1.setOnClickListener(this);
		etSearch.setOnFocusChangeListener(this);
		etSearch.addTextChangedListener(this);
		etSearch.setOnKeyListener(this);
		adapter = new TopicListAdapter(LayoutInflater.from(this), mList);
		mPullRefreshListView.setAdapter(adapter);

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

	public void initPop() {
		PopClickListener listener = new PopClickListener();
		popType = (GridView) findViewById(R.id.topic_search_gv_type);
		popPrice = findViewById(R.id.topic_search_pop_price);
		vPrice0 = findViewById(R.id.topic_search_price_1);
		vPrice1 = findViewById(R.id.topic_search_price_2);
		vPrice0.setOnClickListener(listener);
		vPrice1.setOnClickListener(listener);
		popType.setSelector(new ColorDrawable(Color.TRANSPARENT));
		popType.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = View.inflate(context, R.layout.item_event_pop_grid,
						null);
				TextView tv = (TextView) v.findViewById(R.id.item_pop_tv);
				// if(position == selectType){
				// tv.setSelected(true);
				// }else{
				// tv.setSelected(false);
				// }

				SystemTagItem item = systemlist.get(position);
				String tag = item.getTag();
				tv.setText(tag);
				if (systemlist.size() % 3 >= 19 - position) {
					v.findViewById(R.id.item_pop_line_h).setVisibility(
							View.GONE);
				}
				if (2 == position % 3) {
					v.findViewById(R.id.item_pop_line_v).setVisibility(
							View.GONE);
				}
				return v;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return systemlist.get(position);
			}

			@Override
			public int getCount() {
				return systemlist.size();
			}
		});
		popType.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// selectType = position;
				((BaseAdapter) popType.getAdapter()).notifyDataSetChanged();
				hidePop();
				hideShadow();
				clearSelected();
				SystemTagItem item = systemlist.get(position);
				tagId = item.getTagid();
				model.setTagid(tagId);
				model.setOrder(order);
				// if (0 == position) {
				// tvType.setText("类型");
				// } else {
				if (position==0) {
					tvType.setText("标签");
				}else {
					tvType.setText(item.getTag());
				}
				// }
				// model.setOrder(position);
				search();
			}
		});

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

			case R.id.topic_search_ll_type:
//				tvType.setSelected(v.isSelected());
				if (v.isSelected()) {
					popType.setVisibility(View.VISIBLE);
				}
				break;

			case R.id.topic_search_ll_price:
//				tvPrice.setSelected(v.isSelected());
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
			case R.id.topic_search_price_1:
				model.setOrder(0);
				model.setTagid(tagId);
				tvPrice.setText("最新发布");
//				setPriceState(v);
				break;
			case R.id.topic_search_price_2:
				model.setOrder(1);
				model.setTagid(tagId);
				tvPrice.setText("最新回复");
//				setPriceState(v);
				break;
			default:
				break;
			}
			search();
		}
	}

	private void initFilter() {
		tvType = (TextView) findViewById(R.id.topic_search_tv_type);
		if (tagId!=0&&selectedItem!=null) {
			tvType.setText(selectedItem.getTag());
		}
//			else {
//			tvType.setText("不限");
//		}
		tvPrice = (TextView) findViewById(R.id.topic_search_tv_price);
		vTypeBack = findViewById(R.id.topic_search_ll_type);
		vPriceBack = findViewById(R.id.topic_search_ll_price);
		FilterClickListener listener = new FilterClickListener();
		vTypeBack.setOnClickListener(listener);
		vPriceBack.setOnClickListener(listener);
		vTypeBack.setOnTouchListener(this);
		vPriceBack.setOnTouchListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		// if (isMapModel) {
		// loadmoreMap();
		// } else {
		loadmore();
		// }

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position-=mPullRefreshListView.getRefreshableView().getHeaderViewsCount();
		TopicItemRModel item = (TopicItemRModel) adapter.getItem(position);
		Intent intent = new Intent(context,TopicDetialActivity.class);
		intent.putExtra(Extra.SelectedID, item.getTopicid());
		startActivity(intent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (0 == etSearch.getText().length()) {
			ivClear.setVisibility(View.GONE);
		} else {
			ivClear.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.topic_search_iv_clear:
			etSearch.setText("");
			break;
		case R.id.topic_search_tv_cancel:
			etSearch.setText("");
			etSearch.clearFocus();
			Utils.hideSoftKeyboard(context);
			ivClear.setVisibility(View.INVISIBLE);
			tvCancel.setVisibility(View.GONE);
			break;
		case R.id.topic_search_shadow_all:
			// case R.id.topic_search_shadow_0:
			// case R.id.topic_search_shadow_1:
			hidePop();
			hideShadow();
			clearSelected();
			break;
		case R.id.page_btn_back:
			finish();
			break;
		}
	}

	private void setPriceState(View view) {
		vPrice0.setSelected(false);
		vPrice1.setSelected(false);
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
		if (KeyEvent.KEYCODE_ENTER == keyCode
				&& KeyEvent.ACTION_UP == event.getAction()) {
			if (TextUtils.isEmpty(etSearch.getText())) {
				Toast.show("请输入要搜索的关键字");
			} else {
				//点击搜索把条件还原
				tagId = 0;
				order = 1;
				model.setTagid(tagId);
				model.setOrder(order);
				tvType.setText("标签");
				tvPrice.setText("最新回复");
				model.setPageindex(Const.PAGE_DEFAULT_INDEX);
				model.setPagesize(Const.PAGE_SIZE);
				search();
			}
		}
		return false;
	}

	private void clearSelected() {
		tvType.setSelected(false);
		tvPrice.setSelected(false);

		vTypeBack.setSelected(false);
		vPriceBack.setSelected(false);
	}

	private void showShadow(int viewId) {
		switch (viewId) {

		case R.id.topic_search_tv_type:

		case R.id.topic_search_ll_type:

			// vShadow0.setVisibility(View.GONE);
			// vShadow1.setVisibility(View.VISIBLE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;

		case R.id.topic_search_tv_price:

		case R.id.topic_search_ll_price:
			// vShadow0.setVisibility(View.VISIBLE);
			// vShadow1.setVisibility(View.GONE);
			vShadowAll.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void hideShadow() {
		// vShadow0.setVisibility(View.GONE);
		// vShadow1.setVisibility(View.GONE);
		vShadowAll.setVisibility(View.GONE);
	}

	private void hidePop() {
		popType.setVisibility(View.GONE);
		popPrice.setVisibility(View.GONE);
	}

	private void search() {
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		tagString = etSearch.getText().toString();
		model.setKeyword(tagString);
		mPullRefreshListView.setMode(Mode.BOTH);
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<TopicItemRModel> list = JSON.getList(content,
							TopicItemRModel.class);
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
				@Override
				public void onFinish() {
					super.onFinish();
					tv_empty.setText("很抱歉，没有您想要的结果");
				}
			};
		}
		Net.request(model, WebApi.Topic.TopicSearch, initHandler);
		mPullRefreshListView.getRefreshableView().setEmptyView(tv_empty);
	}

	private void loadmore() {
		model.setPageindex(model.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<TopicItemRModel> list = JSON.getList(content,
							TopicItemRModel.class);
					mList.addAll(list);
					adapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
					if (list.size() < model.getPagesize()) {
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
		Net.request(model, WebApi.Topic.TopicSearch, moreHandler);
	}

}
