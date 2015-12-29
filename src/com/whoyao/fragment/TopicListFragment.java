package com.whoyao.fragment;

import java.util.ArrayList;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.adapter.TopicListAdapter;
import com.whoyao.adapter.UserListAdapter;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.model.TopicHomeTModel;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.model.UserListTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.topic.TopicDetialActivity;
import com.whoyao.topic.TopicSearchIntialActivity;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TopicListFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView> {
	private View contentView;
	private PullToRefreshListView mRefreshListView;
	private ResponseHandler moreHandler;
	private TopicHomeTModel model;
	private ArrayList<TopicItemRModel> mList;
	private TopicListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == contentView) {
			contentView = inflater.inflate(R.layout.frag_topiclist, container,
					false);
			initView();
		} else {
			((ViewGroup) contentView.getParent()).removeView(contentView);
		}
		if (null == model) {
			model = new TopicHomeTModel();
			// if(null != AppContext.location){
			// model.setLatitude(AppContext.location.getLatitude());
			// model.setLongitude(AppContext.location.getLongitude());
			// }
			model.setPageindex(Const.PAGE_DEFAULT_INDEX);
			model.setPagesize(Const.PAGE_SIZE);
		}
		initData();

		return contentView;
	}

	public void initData() {
		Net.cacheRequest(model, WebApi.Topic.TopicHome, new ResponseHandler(
				true) {
			@Override
			public void onSuccess(String content) {
				ArrayList<TopicItemRModel> list = JSON.getList(content,
						TopicItemRModel.class);
				// System.out.println(list);
				mList.clear();
				mList.addAll(list);
				mAdapter.notifyDataSetInvalidated();
				mRefreshListView.onRefreshComplete();
				if (Const.PAGE_SIZE > list.size()) {
					mRefreshListView.setMode(Mode.PULL_FROM_START);
				} else {
					mRefreshListView.setMode(Mode.BOTH);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				if (400 == statusCode) {
				}
				mList.clear();
				mAdapter.notifyDataSetInvalidated();
				mRefreshListView.onRefreshComplete();
				mRefreshListView.setMode(Mode.PULL_FROM_START);
				super.onFailure(e, statusCode, content);
			}
		});
	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler() {
				@Override
				public void onSuccess(String content) {
					ArrayList<TopicItemRModel> list = JSON.getList(content,
							TopicItemRModel.class);
					mRefreshListView.onRefreshComplete();
					if (null != list) {
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
						if (model.getPagesize() > list.size()) {
							mRefreshListView.setMode(Mode.PULL_FROM_START);
						}
					} else {
						mRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					mRefreshListView.onRefreshComplete();
					if (400 == statusCode) {
						mRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}
			};
		}
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Topic.TopicHome, moreHandler);
	}

	public void initView() {
		contentView.findViewById(R.id.topiclist_tv_search).setOnClickListener(this);
		mRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.topiclist_lv_1);
		mRefreshListView.setOnRefreshListener(this);
		mRefreshListView.setMode(Mode.BOTH);
		mRefreshListView.getRefreshableView().setSelector(
				android.R.color.transparent);
		mRefreshListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mRefreshListView.setOnItemClickListener(this);
		mList = new ArrayList<TopicItemRModel>();
		mAdapter = new TopicListAdapter(LayoutInflater.from(getActivity()),
				mList);
		mRefreshListView.setAdapter(mAdapter);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		NetCache.clearCaches();
		initData();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position -= mRefreshListView.getRefreshableView().getHeaderViewsCount();
		TopicItemRModel item = (TopicItemRModel) mAdapter.getItem(position);
		Intent intent = new Intent(getActivity(),TopicDetialActivity.class);
		intent.putExtra(Extra.SelectedID, item.getTopicid());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topiclist_tv_search:
			AppContext.startAct(TopicSearchIntialActivity.class);
			break;

		
		}

	}

}
