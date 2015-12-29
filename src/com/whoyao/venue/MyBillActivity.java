package com.whoyao.venue;

import java.util.ArrayList;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.adapter.MessagePrivateCreateListAdapter;
import com.whoyao.model.MessagePrivateRModel;
import com.whoyao.model.PrivateListItem;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;
import com.whoyao.venue.engine.MyBillAdapter;
import com.whoyao.venue.model.MyBillItem;
import com.whoyao.venue.model.MyBillListTModel;

public class MyBillActivity extends BasicActivity implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView> {
	private PullToRefreshListView mListView;
	private MyBillListTModel tModel;
	private ResponseHandler initHandler;
	private ResponseHandler moreHandler;
	private boolean isRefresh;
	private ArrayList<MyBillItem> createList = new ArrayList<MyBillItem>();
	private MyBillAdapter billAdapter;
	private TextView tvType;
	private View popType;
	private TextView tv_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_bill);
		tModel = new MyBillListTModel();
		tModel.setOrdertype(0);
		initView();
		initData();
	}

	
	private void initData() {
		initHandler = new ResponseHandler(true) {

			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<MyBillItem> list = JSON.getList(content,
						MyBillItem.class);
				if (list != null) {
					createList.clear();
					createList.addAll(list);
					billAdapter.notifyDataSetChanged();
				} else {
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				mListView.onRefreshComplete();
				isRefresh = false;
				if (400 == statusCode) {
					createList.clear();
					billAdapter.notifyDataSetInvalidated();
				}
				if (isRefresh) {
					mListView.onRefreshComplete();
					isRefresh = false;
				}
				super.onFailure(e, statusCode, content);
			}

		};

		initHandler.setShowProgress(!isRefresh);
		Net.request(tModel, WebApi.Venue.MyOrderList, initHandler);

	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<MyBillItem> list = JSON.getList(content,
							MyBillItem.class);
					if (null != list) {
						createList.addAll(list);
						billAdapter.notifyDataSetChanged();
						mListView.onRefreshComplete();
						if (tModel.getPagesize() > list.size()) {
							mListView.setMode(Mode.PULL_FROM_START);
						}
					} else {
						mListView.onRefreshComplete();
						mListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					mListView.onRefreshComplete();
					isRefresh = false;
					if (400 == statusCode) {
						mListView.setMode(Mode.PULL_FROM_START);
						mListView.setMode(Mode.BOTH);
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		tModel.setPageindex(tModel.getPageindex() + 1);
		// tModel.setPageindex(tModel.getPageindex() + 1);
		Net.request(tModel, WebApi.Venue.MyOrderList, moreHandler);
	}

	private void initView() {
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		tv_empty.setText("您还没有预订场馆");
		tvType = (TextView) findViewById(R.id.page_tv_title);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		popType = findViewById(R.id.site_pop_type);
		findViewById(R.id.mybill_tv).setOnClickListener(this);
		findViewById(R.id.waitpay_tv).setOnClickListener(this);
		findViewById(R.id.closed_tv).setOnClickListener(this);
		findViewById(R.id.overdate_tv).setOnClickListener(this);
		// findViewById(R.id.appling_tv).setOnClickListener(this);
		// findViewById(R.id.getpay_tv).setOnClickListener(this);
		findViewById(R.id.success_tv).setOnClickListener(this);
		tvType.setOnClickListener(this);
		billAdapter = new MyBillAdapter(createList, LayoutInflater.from(this),
				this);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_my_bill_list);
		mListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		mListView.getRefreshableView().setEmptyView(tv_empty);
		mListView.setAdapter(billAdapter);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		NetCache.clearCaches();
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int hearders = ((ListView) parent).getHeaderViewsCount();
		MyBillItem item = (MyBillItem) billAdapter.getItem(position - hearders);
		Intent intent = new Intent(context, BillDetailActivity.class);
		intent.putExtra("orderid", item.getOrderid());
		intent.putExtra("position", position - 1);
		// if (item.getPayid()!=null&&item.getPayid().length!=0) {
		intent.putExtra("payid", item.getPayid()[0] + "");
		Log.e("whoyao",item.getPayid()[0]+"");
		// }
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_CANCELED) {

			} else {
				MyBillItem item = null;
				int position = data.getIntExtra("position", 0);
				switch (data.getIntExtra("type", 0)) {
				case 4:
					item = (MyBillItem) billAdapter.getItem(position);
					item.setOrderstate(4);
					billAdapter.notifyDataSetChanged();
					break;

				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_tv_title:
			if (popType.getVisibility() == View.GONE) {
				popType.setVisibility(View.VISIBLE);
				tvType.setSelected(true);
				popType.setVisibility(View.VISIBLE);
			} else {
				tvType.setSelected(false);
				popType.setVisibility(View.GONE);
			}
			break;
		case R.id.mybill_tv:
			tv_empty.setText("您还没有预订场馆");
			popType.setVisibility(View.GONE);
			tvType.setText("我的订单");
			tModel.setOrdertype(0);
			tvType.setSelected(false);
			initData();
			break;
		case R.id.waitpay_tv:
			tv_empty.setText("很抱歉，没有您想要的结果");
			popType.setVisibility(View.GONE);
			tvType.setText("待支付");
			tModel.setOrdertype(2);
			tvType.setSelected(false);
			initData();
			break;
		case R.id.closed_tv:
			tv_empty.setText("很抱歉，没有您想要的结果");
			popType.setVisibility(View.GONE);
			tvType.setText("已取消");
			tModel.setOrdertype(4);
			tvType.setSelected(false);
			initData();
			break;
		case R.id.overdate_tv:
			tv_empty.setText("很抱歉，没有您想要的结果");
			popType.setVisibility(View.GONE);
			tvType.setText("已过期");
			tModel.setOrdertype(8);
			tvType.setSelected(false);
			initData();
			break;
		// case R.id.getpay_tv:
		// popType.setVisibility(View.GONE);
		// tvType.setText("已退款");
		// tModel.setOrdertype(32);
		// tvType.setSelected(false);
		// initData();
		// break;
		case R.id.success_tv:
			tv_empty.setText("很抱歉，没有您想要的结果");
			popType.setVisibility(View.GONE);
			tvType.setText("已成功");
			tModel.setOrdertype(64);
			tvType.setSelected(false);
			initData();
			break;
		// case R.id.appling_tv:
		// popType.setVisibility(View.GONE);
		// tvType.setText("退款申请中");
		// tModel.setOrdertype(16);
		// tvType.setSelected(false);
		// initData();
		// break;
		case R.id.page_btn_back:
			finish();
			break;

		}

	}

}
