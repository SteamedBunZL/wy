package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import com.baidu.platform.comapi.map.v;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.InviteFriendListAdapter;
import com.whoyao.adapter.InviteMyReceiveAdapter;
import com.whoyao.adapter.UserFriendListAdapter;
import com.whoyao.model.InviteFriendTModel;
import com.whoyao.model.MyFriendTModel;
import com.whoyao.model.NoticeListItem;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 邀请好友页
 * 
 * @author zl
 * 
 */
public class InviteFriendActivity extends BasicActivity implements
		OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener
		{
	private TextView cbAll;
	private PullToRefreshListView mListView;
	private MyFriendTModel searchModel = new MyFriendTModel();
	private ResponseHandler moreHandler;
	private ResponseHandler initHandler;
	private List<UserListItemModel> mList = new ArrayList<UserListItemModel>();
	private List<UserListItemModel> checkedList = new ArrayList<UserListItemModel>();
	private InviteFriendListAdapter mAdapter;
	protected boolean isRefresh;
	private boolean isAllChecked = false;
	private InviteFriendTModel tModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friend);
		tModel = new InviteFriendTModel();
		mAdapter = new InviteFriendListAdapter(LayoutInflater.from(this),
				mList, this);
		initView();
		initData();
	}

	public void initView() {
		cbAll = (TextView) findViewById(R.id.cb_all);
		cbAll.setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.next).setOnClickListener(this);
		findViewById(R.id.rl_check_all).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.ll_invite_friend);
		mListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		mListView.getRefreshableView().setItemsCanFocus(false);
		mListView.setAdapter(mAdapter);

	}

	public void initData() {
		// 请求我的好友数据
		searchModel.setFriendtype(1);
		searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		mListView.setMode(Mode.PULL_FROM_END);
		if (null == initHandler) {
			initHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<UserListItemModel> list = JSON.getObject(content,
							UserListRModel.class).getList();
					mList.clear();
					mList.addAll(list);
					mAdapter.notifyDataSetInvalidated();
					if (isRefresh) {
						mListView.onRefreshComplete();
						isRefresh = false;
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					if (400 == statusCode) {
						mList.clear();
						mAdapter.notifyDataSetInvalidated();
					}
					if (isRefresh) {
						mListView.onRefreshComplete();
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
					ArrayList<UserListItemModel> list = JSON.getObject(content,
							UserListRModel.class).getList();
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					mListView.onRefreshComplete();
					if (list.size() < searchModel.getPagesize()) {
						mListView.setMode(Mode.DISABLED);
					}
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					mListView.onRefreshComplete();
					if (400 == statusCode) {
						mListView.setMode(Mode.DISABLED);
					}
					super.onFailure(e, statusCode, content);
				}
			};
		}
		Net.request(searchModel, WebApi.User.GetFridnds, moreHandler);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		// mListView.onRefreshComplete();
		NetCache.clearCaches();
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadmore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println(id);
		UserListItemModel item = (UserListItemModel) mAdapter
				.getItem(position - 1);
		item.setChecked(!item.isChecked());
		TextView tvSingle = (TextView) view.getTag(position);
		int wrongNum = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).isChecked() == false) {
				wrongNum++;
			} else {

			}
		}
		if (wrongNum > 0) {
			isAllChecked = false;
			cbAll.setSelected(false);
		} else {
			isAllChecked = true;
			cbAll.setSelected(true);
		}
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_iv_face:
			int position = (Integer) v.getTag(R.id.user_iv_face);
			UserListItemModel item2 = mList.get(position);
			Intent intent = new Intent(context, UserOtherDetialActivity.class);
			int userId = item2.getUserid();
//			if (item2.getResult() == 0) {
//				int messageId = item2.getMessagecontentid();
//				intent.putExtra(Extra.SelectedItem, messageId);
//				intent.putExtra(Extra.Position, position);
//			}
//			L.i(Const.AppName, "postion--" + position + "----item2-----"
//					+ item2 + "userid" + userId);
			intent.putExtra(Extra.SelectedID, userId);
			startActivityForResult(intent, R.id.user_iv_face);
			break;
		case R.id.cb_single:
			UserListItemModel item = mList.get((Integer) v
					.getTag(R.id.cb_single));
			boolean isChecked = item.isChecked();
			System.out.println(isChecked);
			item.setChecked(!isChecked);
			int wrongNum = 0;
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).isChecked() == false) {
					wrongNum++;
				} else {

				}
			}
			if (wrongNum > 0) {
				isAllChecked = false;
				cbAll.setSelected(false);
			} else {
				isAllChecked = true;
				cbAll.setSelected(true);
			}

			// cbAll.setChecked(isAllChecked);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.rl_check_all:
			if (isAllChecked == true) {
				isAllChecked = false;
				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setChecked(isAllChecked);
				}
				cbAll.setSelected(false);
			}

			else {
				isAllChecked = true;
				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setChecked(isAllChecked);
				}
				cbAll.setSelected(true);
			}
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.cb_all:
			if (isAllChecked == true) {
				isAllChecked = false;
				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setChecked(isAllChecked);
				}
				cbAll.setSelected(false);
			}

			else {
				isAllChecked = true;
				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setChecked(isAllChecked);
				}
				cbAll.setSelected(true);
			}
			mAdapter.notifyDataSetChanged();

			break;
		case R.id.next:
			String userIds = "";
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).isChecked() == true) {
					checkedList.add(mList.get(i));
					// userIds.add(mList.get(i).getUserid()+"");
				} else {

				}
			}
			for (int j = 0; j < checkedList.size(); j++) {
				if (j + 1 == checkedList.size()) {
					userIds = userIds + mList.get(j).getUserid();
				} else {
					userIds = userIds + mList.get(j).getUserid() + ",";
				}
			}
			System.out.println(userIds.length());
			if (userIds.length() == 0) {
				Toast.show("请选择好友");
				return;
			}
			tModel.setUserids(userIds);
			Intent inviteIntent = getIntent();
			int inviteId = inviteIntent.getIntExtra(Extra.SelectedID, 0);
			tModel.setActivityid(inviteId);
			Net.request(tModel, WebApi.Event.InviteFriend, new ResponseHandler(
					true) {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String content) {
					super.onSuccess(statusCode, headers, content);
					Toast.show("邀请发送成功");
					finish();
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
					Toast.show("很抱歉，邀请未发送成功");
				}

			});

			// default:
			// break;
		}

	}

	

}
