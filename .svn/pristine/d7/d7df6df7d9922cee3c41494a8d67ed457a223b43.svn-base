package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Type;
import com.whoyao.activity.EventMyListActivity.PopOnCOnClickListener;
import com.whoyao.adapter.EventCreatListAdapter;
import com.whoyao.adapter.InviteMyReceiveAdapter;
import com.whoyao.adapter.InviteMySendAdapter;
import com.whoyao.model.EventListItem;
import com.whoyao.model.EventMyListRModel;
import com.whoyao.model.EventMyListTModel;
import com.whoyao.model.InviteDetailListItem;
import com.whoyao.model.InviteOperateTModel;
import com.whoyao.model.MyInviteRModel;
import com.whoyao.model.MyInviteTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyInviteActivity extends BasicActivity implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView>,
		OnCheckedChangeListener {
	private RadioGroup rgInviteGroup;
	private Button btnBack;
	private MyInviteTModel myInviteTModel;
	private TextView tvOrder;
	private TextView tvState;
	private View popOrder;
	private View popState;
	private View shadow1;
	private View shadow0;
	private boolean isRefresh;
	private ResponseHandler initHandler;
	private ResponseHandler moreHandler;
	private View llOrder;
	private View llState;
	private PullToRefreshListView mListView;
	private InviteMyReceiveAdapter receiveAdapter;
	private InviteMySendAdapter sendAdapter;
	private int type = 0;
	private List<InviteDetailListItem> receiveList = new ArrayList<InviteDetailListItem>();
	private List<InviteDetailListItem> sendList = new ArrayList<InviteDetailListItem>();
	private static int GET_OPERATE = 1;
	private InviteOperateTModel model;
	private TextView emptyView;
	private int state = 0;
	private List<Integer> views = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_invite);
		// myInviteTModel = new MyInviteTModel();
		// myInviteTModel.setInvitetype(1);

		// myInviteTModel.setPageindex(1);
		// myInviteTModel.setPagesize(20);

		// Net.request(myInviteTModel, WebApi.Invite.MyInviteList,
		// new ResponseHandler());
		initView();
		initPop();
		myInviteTModel = new MyInviteTModel();
		myInviteTModel.setInvitetype(1);
		myInviteTModel.setUserid(MyMessageActivity.USER_ID);
		myInviteTModel.setInviteOrder(1);
		myInviteTModel.setInviteState(-1);
		model = new InviteOperateTModel();
		initData();
	}

	public void initView() {
		rgInviteGroup = (RadioGroup) findViewById(R.id.rg_invite_title);
		rgInviteGroup.setOnCheckedChangeListener(this);
		emptyView = (TextView) findViewById(R.id.empty_view);
		mListView = (PullToRefreshListView) findViewById(R.id.my_invite_lv);
		mListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		receiveAdapter = new InviteMyReceiveAdapter(receiveList,
				LayoutInflater.from(this), this);
		sendAdapter = new InviteMySendAdapter(sendList,
				LayoutInflater.from(this), this);
		findViewById(R.id.my_invite_btn_back).setOnClickListener(this);
	}

	public void initData() {

		initHandler = new ResponseHandler(true) {
			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<InviteDetailListItem> list = JSON.getObject(content,
						MyInviteRModel.class).InviteDetailListItem;
				System.out.println("***list.size**" + list.size());
				if (null != list) {
					emptyView.setVisibility(View.GONE);
					if (myInviteTModel.getInvitetype() == 1) {
						mListView.setAdapter(receiveAdapter);
						receiveList.clear();
						receiveList.addAll(list);
						receiveAdapter.notifyDataSetChanged();
					} else {
						mListView.setAdapter(sendAdapter);
						sendList.clear();
						sendList.addAll(list);
						sendAdapter.notifyDataSetChanged();
					}
					mListView.setMode(Mode.BOTH);
				} else {
					receiveList.clear();
					receiveAdapter.notifyDataSetChanged();
					emptyView.setVisibility(View.VISIBLE);
					// if (myInviteTModel.getInvitetype() == 1) {
					// emptyView.setText("您还没有收到过邀请哦！");
					// } else {
					// emptyView.setText("您还没有发起过邀请哦！");
					// }
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				mListView.onRefreshComplete();
				isRefresh = false;
				if (400 == statusCode) {
					receiveList.clear();
					sendList.clear();
					receiveAdapter.notifyDataSetChanged();
					sendAdapter.notifyDataSetChanged();
					emptyView.setVisibility(View.VISIBLE);
					if (myInviteTModel.getInvitetype() == 1) {
						emptyView.setText("您还没有收到过邀请哦！");
					} else {
						emptyView.setText("您还没有发起过邀请哦！");
					}
					// emptyView.setText("按照您的筛选条件无筛选结果");
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}
		};

		initHandler.setShowProgress(!isRefresh);
		myInviteTModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		myInviteTModel.setPagesize(20);
		Net.cacheRequest(myInviteTModel, WebApi.Invite.MyInviteList,
				initHandler);

	}

	public void initStateData() {

		initHandler = new ResponseHandler(true) {
			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<InviteDetailListItem> list = JSON.getObject(content,
						MyInviteRModel.class).InviteDetailListItem;
				if (null != list) {
					emptyView.setVisibility(View.GONE);
					if (myInviteTModel.getInvitetype() == 1) {
						mListView.setAdapter(receiveAdapter);
						receiveList.clear();
						receiveList.addAll(list);
						receiveAdapter.notifyDataSetChanged();
					} else {
						mListView.setAdapter(sendAdapter);
						sendList.clear();
						sendList.addAll(list);
						sendAdapter.notifyDataSetChanged();
					}
					mListView.setMode(Mode.BOTH);
				} else {
					receiveList.clear();
					receiveAdapter.notifyDataSetChanged();
					emptyView.setVisibility(View.VISIBLE);
					// if (myInviteTModel.getInvitetype() == 1) {
					// emptyView.setText("您还没有收到过邀请哦！");
					// } else {
					// emptyView.setText("您还没有发起过邀请哦！");
					// }
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				mListView.onRefreshComplete();
				isRefresh = false;
				if (400 == statusCode) {
					receiveList.clear();
					sendList.clear();
					receiveAdapter.notifyDataSetChanged();
					sendAdapter.notifyDataSetChanged();
					emptyView.setVisibility(View.VISIBLE);
					// if (myInviteTModel.getInvitetype() == 1) {
					// emptyView.setText("您还没有收到过邀请哦！");
					// } else {
					// emptyView.setText("您还没有发起过邀请哦！");
					// }
					emptyView.setText("按照您的筛选条件无筛选结果");
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}
		};

		initHandler.setShowProgress(!isRefresh);
		myInviteTModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		myInviteTModel.setPagesize(20);
		Net.cacheRequest(myInviteTModel, WebApi.Invite.MyInviteList,
				initHandler);
	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<InviteDetailListItem> list = JSON.getObject(
							content, MyInviteRModel.class).InviteDetailListItem;
					if (null != list) {
						if (myInviteTModel.getInvitetype() == 1) {
							receiveList.addAll(list);
							receiveAdapter.notifyDataSetChanged();
						} else {
							sendList.addAll(list);
							sendAdapter.notifyDataSetChanged();
						}
						mListView.onRefreshComplete();
						if (myInviteTModel.getPagesize() > list.size()) {
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
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		myInviteTModel.setPageindex(myInviteTModel.getPageindex() + 1);
		Net.cacheRequest(myInviteTModel, WebApi.Invite.MyInviteList,
				moreHandler);
	}

	public void initPop() {
		tvState = (TextView) findViewById(R.id.invite_my_tv_state);
		llState = findViewById(R.id.invite_my_ll_state);
		tvOrder = (TextView) findViewById(R.id.invite_my_tv_order);
		llOrder = findViewById(R.id.invite_my_ll_order);
		popOrder = findViewById(R.id.invite_my_pop_order);
		popState = findViewById(R.id.invite_my_pop_state);
		shadow0 = findViewById(R.id.invite_my_shadow_0);
		shadow1 = findViewById(R.id.invite_my_shadow_1);
		shadow0.setOnClickListener(this);
		shadow1.setOnClickListener(this);

		OnTouchListener touchListener = new OnTouchListener() {

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
		};
		llOrder.setOnTouchListener(touchListener);
		llState.setOnTouchListener(touchListener);
		PopOnCOnClickListener listener = new PopOnCOnClickListener();
		llOrder.setOnClickListener(listener);
		llState.setOnClickListener(listener);
		findViewById(R.id.invite_my_order_1).setOnClickListener(listener);
		findViewById(R.id.invite_my_order_2).setOnClickListener(listener);
		findViewById(R.id.invite_my_order_3).setOnClickListener(listener);
		findViewById(R.id.invite_my_state_5).setOnClickListener(listener);
		findViewById(R.id.invite_my_state_2).setOnClickListener(listener);
		findViewById(R.id.invite_my_state_3).setOnClickListener(listener);
		findViewById(R.id.invite_my_state_4).setOnClickListener(listener);
		// findViewById(R.id.invite_my_state_1).setOnClickListener(listener);
		findViewById(R.id.invite_my_state_0).setOnClickListener(listener);

	}

	@Override
	protected boolean onBack() {
		if (View.VISIBLE == shadow0.getVisibility()) {
			hidePop();
			hideShadow();
			clearSelected();
		} else {
			finish();
		}
		return true;
	}

	class PopOnCOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			boolean isSelected = v.isSelected();
			hidePop();
			clearSelected();
			if (isSelected) {
				hideShadow();
				return;
			}
			switch (v.getId()) {
			case R.id.invite_my_ll_order:
				tvOrder.setSelected(true);
				llOrder.setSelected(true);
				showShadow();
				popOrder.setVisibility(View.VISIBLE);
				break;
			case R.id.invite_my_ll_state:
				tvState.setSelected(true);
				llState.setSelected(true);
				showShadow();
				popState.setVisibility(View.VISIBLE);
				break;
			case R.id.invite_my_order_1:
				myInviteTModel.setInviteOrder(1);
				tvOrder.setText("不限");
				whichSelected(R.id.invite_my_ll_order, R.id.invite_my_order_1);
				state = 1;
				hideShadow();
				initStateData();
				break;
			// 由近及远
			case R.id.invite_my_order_3:
				myInviteTModel.setInviteOrder(3);
				tvOrder.setText("按邀请时间由近及远");
				whichSelected(R.id.invite_my_ll_order, R.id.invite_my_order_3);
				state = 1;
				hideShadow();
				initStateData();
				break;
			// 由远及近
			case R.id.invite_my_order_2:
				myInviteTModel.setInviteOrder(2);
				tvOrder.setText("按邀请时间由远及近");
				whichSelected(R.id.invite_my_ll_order, R.id.invite_my_order_2);
				state = 1;
				hideShadow();
				initStateData();
				break;

			// 未处理
			case R.id.invite_my_state_0:
				myInviteTModel.setInviteState(0);
				tvState.setText("未处理");
				whichSelected(R.id.invite_my_ll_state,R.id.invite_my_state_0);
				state = 1;
				hideShadow();
				initStateData();
				break;
			case R.id.invite_my_state_5:
				myInviteTModel.setInviteState(-1);
				whichSelected(R.id.invite_my_ll_state,R.id.invite_my_state_5);
				tvState.setText("不限");
				state = 1;
				hideShadow();
				initStateData();
				break;
			// 邀请中
			// case R.id.invite_my_state_1:
			// myInviteTModel.setInviteState(1);
			// tvState.setText("邀请中");
			// state = 1;
			// hideShadow();
			// initStateData();
			// break;
			// 已拒绝
			case R.id.invite_my_state_2:
				myInviteTModel.setInviteState(2);
				whichSelected(R.id.invite_my_ll_state,R.id.invite_my_state_2);
				tvState.setText("已拒绝");
				state = 1;
				hideShadow();
				initStateData();
				// initData();
				break;
			// 已过期
			case R.id.invite_my_state_3:
				myInviteTModel.setInviteState(3);
				whichSelected(R.id.invite_my_ll_state,R.id.invite_my_state_3);
				tvState.setText("已过期");
				state = 1;
				hideShadow();
				initStateData();
				break;
			// 已接受
			case R.id.invite_my_state_4:
				myInviteTModel.setInviteState(4);
				whichSelected(R.id.invite_my_ll_state,R.id.invite_my_state_4);
				tvState.setText("已授受");
				state = 1;
				hideShadow();
				initStateData();
				break;
			}
		}
	}
	private void showShadow() {
		shadow0.setVisibility(View.VISIBLE);
		shadow1.setVisibility(View.VISIBLE);
	}
	private void hideShadow() {
		shadow0.setVisibility(View.GONE);
		shadow1.setVisibility(View.GONE);
	}
	private void hidePop() {
		popOrder.setVisibility(View.GONE);
		popState.setVisibility(View.GONE);
	}

	protected void clearSelected() {
		tvOrder.setSelected(false);
		tvState.setSelected(false);
		llState.setSelected(false);
		llOrder.setSelected(false);
	}

	public void changeOrderState(int which) {
		findViewById(R.id.invite_my_order_1).setSelected(false);
		findViewById(R.id.invite_my_order_2).setSelected(false);
		findViewById(R.id.invite_my_order_3).setSelected(false);
		for (int i = 0; i < views.size(); i++) {
			if (views.get(i) == which) {
				findViewById(which).setSelected(true);
			}
		}
	}

	public void changeMyState(int which) {
		findViewById(R.id.invite_my_state_0).setSelected(false);
		findViewById(R.id.invite_my_state_2).setSelected(false);
		findViewById(R.id.invite_my_state_3).setSelected(false);
		findViewById(R.id.invite_my_state_4).setSelected(false);
		findViewById(R.id.invite_my_state_5).setSelected(false);
		for (int i = 0; i < views.size(); i++) {
			if (views.get(i) == which) {
				findViewById(which).setSelected(true);
			}
		}
	}

	public void whichSelected(int which, int id) {
		if (which == R.id.invite_my_ll_order) {
			views.add(R.id.invite_my_order_1);
			views.add(R.id.invite_my_order_3);
			views.add(R.id.invite_my_order_2);
			switch (id) {
			case R.id.invite_my_order_1:
				changeOrderState(R.id.invite_my_order_1);
				break;

			case R.id.invite_my_order_3:
				changeOrderState(R.id.invite_my_order_3);
				break;

			case R.id.invite_my_order_2:
				changeOrderState(R.id.invite_my_order_2);
				break;
			}
		} else {
			views.add(R.id.invite_my_state_0);
			views.add(R.id.invite_my_state_2);
			views.add(R.id.invite_my_state_3);
			views.add(R.id.invite_my_state_4);
			views.add(R.id.invite_my_state_5);
			switch (id) {
			case R.id.invite_my_state_0:
				changeMyState(R.id.invite_my_state_0);
				break;
			case R.id.invite_my_state_2:
				changeMyState(R.id.invite_my_state_2);
				break;
			case R.id.invite_my_state_3:
				changeMyState(R.id.invite_my_state_3);
				break;
			case R.id.invite_my_state_4:
				changeMyState(R.id.invite_my_state_4);
				break;
			case R.id.invite_my_state_5:
				changeMyState(R.id.invite_my_state_5);
				break;
			}
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
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
		// TODO Auto-generated method stub
		InviteDetailListItem item = null;
		if (myInviteTModel.getInvitetype() == 1) {
			item = (InviteDetailListItem) receiveAdapter.getItem(position - 1);
		} else {
			item = (InviteDetailListItem) sendAdapter.getItem(position - 1);
		}
		Intent intent = new Intent(context, MyInviteDetailActivity.class);
		intent.putExtra("myInvite",item);
		intent.putExtra("type",myInviteTModel.getInvitetype());
		System.out.println("**type***" + myInviteTModel.getInvitetype());
		intent.putExtra("position", position - 1);
		startActivityForResult(intent, GET_OPERATE);
	}

	// private void clearFilter() {
	// tvOrder.setText("时间排序");
	// tvState.setText("状态");
	// myInviteTModel.setInvitetype(0);
	// myInviteTModel.set
	// model.setActivitystate(0);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_invite_btn_back:
			onBack();
			break;
		case R.id.invite_my_shadow_0:
		case R.id.invite_my_shadow_1:
			hidePop();
			hideShadow();
			clearSelected();
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_OPERATE) {
			if (resultCode == RESULT_CANCELED) {

			} else {
				InviteDetailListItem item = null;
				int position = data.getIntExtra("position", 0);
				if (data.getIntExtra("type", myInviteTModel.getInvitetype()) == 1) {
					item = (InviteDetailListItem) receiveAdapter
							.getItem(position);
					int operate = data.getIntExtra(Extra.State,
							item.getInviteState());
					L.i(Const.AppName, "operate----------" + operate);
					item.setInviteState(operate);
					receiveAdapter.notifyDataSetChanged();
				} else if (data.getIntExtra("type",
						myInviteTModel.getInvitetype()) == 2) {
					item = (InviteDetailListItem) sendAdapter.getItem(position);
					int operate = data.getIntExtra("operate",
							item.getInviteState());
					item.setInviteState(operate);
					sendAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_invite_receive:
			myInviteTModel.setInvitetype(1);
			myInviteTModel.setInviteOrder(1);
			myInviteTModel.setInviteState(-1);
			tvOrder.setText("时间排序");
			tvState.setText("状态");
			if (state == 0) {
				initData();
			} else {
				initStateData();
			}
			break;

		case R.id.rb_invite_send:
			myInviteTModel.setInvitetype(0);
			myInviteTModel.setInviteOrder(1);
			myInviteTModel.setInviteState(-1);
			tvOrder.setText("时间排序");
			tvState.setText("状态");
			if (state == 0) {
				initData();
			} else {
				initStateData();
			}
			break;
		}
	}

}
