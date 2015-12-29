package com.whoyao.fragment;

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
import com.whoyao.activity.UserSearchInitialActivity;
import com.whoyao.activity.UserSelfDetialActivity;
import com.whoyao.adapter.UserListAdapter;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.model.UserListTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author hyh 
 * creat_at：2013-11-11-上午9:42:41
 */
public class UserListFragment extends Fragment implements OnClickListener, OnCheckedChangeListener, OnItemClickListener, OnRefreshListener2<ListView>{
	private View contentView;
	//	private RadioButton rbInterest;
//	private RadioButton rbDoyen;
	private PullToRefreshListView mRefreshListView;
	private UserListTModel model;
	private UserListAdapter mAdapter;
	private List<UserListItemModel> mList;
	private RadioGroup rgType;
	private ResponseHandler moreHandler;
	private View vEmpty;
	private View vAddTag;
	private View vSameTag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null == contentView) {
			contentView = inflater.inflate(R.layout.frag_userlist, container, false);
			initView();
		} else {
			((ViewGroup) contentView.getParent()).removeView(contentView);
		}
		if(null == model){
			model = new UserListTModel();
			if(null != AppContext.location){
				model.setLatitude(AppContext.location.getLatitude());
				model.setLongitude(AppContext.location.getLongitude());
			}
		}
		initData();

		return contentView;	
	}



	private void initView() {
		contentView.findViewById(R.id.userlist_tv_search).setOnClickListener(this);
		rgType = (RadioGroup)contentView.findViewById(R.id.userlist_rg_type);
		rgType.setOnCheckedChangeListener(this);
		mRefreshListView = (PullToRefreshListView)contentView.findViewById(R.id.userlist_lv);
		mRefreshListView.setOnRefreshListener(this);
		mRefreshListView.setMode(Mode.BOTH);
		mRefreshListView.getRefreshableView().setSelector(android.R.color.transparent);
		mRefreshListView.getRefreshableView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		vEmpty = View.inflate(getActivity(), R.layout.empty_user_sametag, null);
		vSameTag = vEmpty.findViewById(R.id.empty_user_sametag);
		vAddTag = vEmpty.findViewById(R.id.empty_user_sametag_add);
		if(MyinfoManager.userTag == null || MyinfoManager.userTag.isEmpty()){
			vAddTag.setOnClickListener(this);
			mRefreshListView.getRefreshableView().setEmptyView(vEmpty);
		}
		mRefreshListView.setOnItemClickListener(this);
		mList = new ArrayList<UserListItemModel>();
		mAdapter = new UserListAdapter(LayoutInflater.from(getActivity()), mList);
		mRefreshListView.setAdapter(mAdapter);
	}
	
	private void initData() {
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.User.GetUserList, new ResponseHandler(true){
			@Override
			public void onSuccess(String content) {
				ArrayList<UserListItemModel> list = JSON.getObject(content, UserListRModel.class).getList();
				mList.clear();
				mList.addAll(list);
				mAdapter.notifyDataSetInvalidated();
				mRefreshListView.onRefreshComplete();
				if(Const.PAGE_SIZE > list.size()){
					mRefreshListView.setMode(Mode.PULL_FROM_START);
				}else{
					mRefreshListView.setMode(Mode.BOTH);
				}
			}
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
//				if(400 == statusCode){
//				}
				mList.clear();
				mAdapter.notifyDataSetInvalidated();
				mRefreshListView.onRefreshComplete();
				mRefreshListView.setMode(Mode.PULL_FROM_START);
				super.onFailure(e, statusCode, content);
			}
		});
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userlist_tv_search:
			AppContext.startAct(UserSearchInitialActivity.class);
			break;
		case R.id.empty_user_sametag_add:
			AppContext.startAct(UserSelfDetialActivity.class);
			break;
		default:
			break;
		}
	}



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.userlist_rb_interest:
			model.setType(0);
			vSameTag.setVisibility(View.VISIBLE);
			initData();
			mRefreshListView.setMode(Mode.BOTH);
			break;
		case R.id.userlist_rb_doyen:
			model.setType(1);
			vSameTag.setVisibility(View.GONE);
			initData();
			mRefreshListView.setMode(Mode.BOTH);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		position = position - mRefreshListView.getRefreshableView().getHeaderViewsCount();
		if(position > -1){
			int userid = mList.get(position).getUserid();
			UserEngine.startUserDetial(userid);
		}
		
	}

	private void loadMore(){
		if(null == moreHandler){
			moreHandler = new ResponseHandler(){
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content, UserListRModel.class).getList();
					mRefreshListView.onRefreshComplete();
					if(null != list){
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
						if(model.getPagesize() > list.size()){
							mRefreshListView.setMode(Mode.PULL_FROM_START);
						}
					}else{
						mRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					mRefreshListView.onRefreshComplete();
					if(400 == statusCode){
						mRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				}
			};
		}
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.User.GetUserList, moreHandler);	
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		NetCache.clearCaches();
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}
}
