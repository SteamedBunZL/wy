package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.UserFriendListAdapter;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.MyFriendTModel;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-4-下午2:26:40
 */
public class MyFriendActivity extends BasicActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {




	private BaseAdapter mAdapter;
	private List<UserListItemModel> mList = new ArrayList<UserListItemModel>();
	private MyFriendTModel searchModel = new MyFriendTModel();

	protected int selectedCity = -1;
	private PullToRefreshListView mPullRefreshListView;
	private View popType;
	protected boolean isRefresh;
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;

	private TextView tvTitle;
	private View vShadow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friend);
		initView();
		initData();
	}


	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.page_btn_right).setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.page_tv_title);
		tvTitle.setOnClickListener(this);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.my_friend_lv);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT ));
		mAdapter = new UserFriendListAdapter(LayoutInflater.from(this), mList);
		mPullRefreshListView.setAdapter(mAdapter);
		popType = findViewById(R.id.my_friend_pop_type);
		vShadow = findViewById(R.id.my_friend_shadow);
		vShadow.setOnClickListener(this);
		PopClickListener listener = new PopClickListener();
		findViewById(R.id.my_friend_type_friend).setOnClickListener(listener);
		findViewById(R.id.my_friend_type_free).setOnClickListener(listener);
		findViewById(R.id.my_friend_type_event).setOnClickListener(listener);
		findViewById(R.id.my_friend_type_favorite).setOnClickListener(listener);
		findViewById(R.id.my_friend_type_black).setOnClickListener(listener);
		findViewById(R.id.my_friend_type_tag).setOnClickListener(listener);
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.my_friend_shadow:
			popType.setVisibility(View.GONE);
			vShadow.setVisibility(View.GONE);
			break;
		case R.id.page_btn_right:
			AppContext.startAct(UserSearchInitialActivity.class);
			break;
		case R.id.page_tv_title:
			if(View.GONE == popType.getVisibility()){
				tvTitle.setSelected(true);
				popType.setVisibility(View.VISIBLE);
				vShadow.setVisibility(View.VISIBLE);
			}else{
				tvTitle.setSelected(false);
				popType.setVisibility(View.GONE);
				vShadow.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}


	class PopClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.my_friend_type_friend:
				tvTitle.setText("我的好友");
				searchModel.setFriendtype(1);
				break;
			case R.id.my_friend_type_free:
				tvTitle.setText("空闲好友");
				searchModel.setFriendtype(2);
				break;
			case R.id.my_friend_type_event:
				tvTitle.setText("活动过的人");
				searchModel.setFriendtype(3);
				break;
			case R.id.my_friend_type_favorite:
				tvTitle.setText("收藏的人");
				searchModel.setFriendtype(4);
				break;
			case R.id.my_friend_type_black:
				tvTitle.setText("黑名单");
				searchModel.setFriendtype(5);
				break;
			case R.id.my_friend_type_tag:
				tvTitle.setText("共同标签");
				searchModel.setFriendtype(6);
				break;

			default:
				break;
			}
			popType.setVisibility(View.GONE);
			vShadow.setVisibility(View.GONE);
			tvTitle.setSelected(false);
			initData();
		}
	}



	private void initData() {
		searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		mPullRefreshListView.setMode(Mode.BOTH);
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content, UserListRModel.class).getList();
					mList.clear();
					mList.addAll(list);
					mAdapter.notifyDataSetInvalidated();
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					if (400 == statusCode) {
						mList.clear();
						mAdapter.notifyDataSetInvalidated();
					}
					if (isRefresh) {
						mPullRefreshListView.onRefreshComplete();
						isRefresh = false;
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		Net.request(searchModel, WebApi.User.GetFridnds, initHandler);
	}

	private void loadmore() {
		searchModel.setPageindex(searchModel.getPageindex() + 1);
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content, UserListRModel.class).getList();
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
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
		Net.request(searchModel, WebApi.User.GetFridnds, moreHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int hearders = ((ListView) parent).getHeaderViewsCount();
		int userID = mList.get(position - hearders).getUserid();
		UserEngine.startUserDetial(userID);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadmore();
	}

}
