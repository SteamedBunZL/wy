package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.adapter.ChatAdapter;
import com.whoyao.db.ChatDBHelper;
//import com.whoyao.db.ChatProvider;
//import com.whoyao.db.ChatProvider.ChatConstants;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.InviteNoticeDeleteSingleItemModel;
import com.whoyao.model.MessagePrivateDetailRModel;
import com.whoyao.model.MessagePrivateDetailTModel;
import com.whoyao.model.PraviteDetailSendMessageTModel;
import com.whoyao.model.PrivateDetailListItem;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.net.ResponseHandlerWithoutDialog;
import com.whoyao.service.PushPrivateModel;
import com.whoyao.service.WYService;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.R.integer;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PrivateDetailActivity extends BasicActivity implements
		OnClickListener, OnRefreshListener2<ListView>, OnFocusChangeListener,
		OnItemLongClickListener {
	// 声明带下拉刷新的ListView
	private PullToRefreshListView mListView;
	// 声明发送人的名字
	private TextView tvSenderName;
	public static final String INTENT_EXTRA_USERNAME = PrivateDetailActivity.class
			.getName() + ".username";// 昵称对应的key
	private Button btnPageBack;
	private MessagePrivateDetailTModel model;
	private ResponseHandler initHandler;
	private boolean isRefresh;
	private ChatAdapter chatAdapter;
	private List<PrivateDetailListItem> createList = new ArrayList<PrivateDetailListItem>();
	private ResponseHandler moreHandler;
	private Button mBtnSend;
	private EditText mEditTextContent;
	private int receverId;
	private String name;
	private PraviteDetailSendMessageTModel sendModel;
	private InviteNoticeDeleteSingleItemModel deletInviteNoticeSingleModel;
	private Intent intent;
	private Intent contentIntent;
	private WYService mService;
	private Handler mainHandler = new Handler();
	private String currentHeadpic;
	private List<Integer> readMessage = new ArrayList<Integer>();
	private ChatDBHelper helper;
	private ArrayList<PrivateDetailListItem> dbList;
	private int host;
	private int chatuser;
	private String head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_private_detail);
		model = new MessagePrivateDetailTModel();
		sendModel = new PraviteDetailSendMessageTModel();
		deletInviteNoticeSingleModel = new InviteNoticeDeleteSingleItemModel();

		// TODO 每次进入都要先读数据库加载数据

		// initData();
		// setChatWindowAdapter();// 初始化对话数据
	}

	private BroadcastReceiver chatReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (TextUtils.equals(action, Const.CHAT_MSG)) {
				PushPrivateModel privateModel = (PushPrivateModel) intent
						.getSerializableExtra(Extra.SelectedItem);
				PrivateDetailListItem item = new PrivateDetailListItem();
				item.setIsowner(false);
				item.setContent(privateModel.getCon());
				item.setSenderuserid(privateModel.getSid());
				item.setEntertime((CalendarUtils.getCurrentTime()));
				item.setMsgcontentid(privateModel.getCid());
				L.i(Const.ZL, "MessageId : " + privateModel.getCid());
				item.setHeadpicture(chatAdapter.getHead());
				addDBbyItem(item);
				// item.setHeadpicture(headMap.get(receverId, null));
				readMessage.add(privateModel.getCid());
				chatAdapter.addSingleItem(item);
				chatAdapter.notifyDataSetChanged();
				L.i(Const.ZL, "Head:" + item.getHeadpicture());
				// mListView.getRefreshableView().setSelection(
				// chatAdapter.getCount() - 1);
				// mListView.getRefreshableView().setSelection(
				// chatAdapter.getCount() - 1);
				mListView.getRefreshableView().smoothScrollToPosition(
						mListView.getRefreshableView().getCount() - 1);
			} else if (TextUtils.equals(action, Const.CHAT_MSG_WIOUT_NOTIFY)) {
				PushPrivateModel privateModel = (PushPrivateModel) intent
						.getSerializableExtra(Extra.SelectedItem);
				PrivateDetailListItem item = new PrivateDetailListItem();
				item.setIsowner(false);
				item.setContent(privateModel.getCon());
				item.setSenderuserid(privateModel.getSid());
				item.setEntertime((CalendarUtils.getCurrentTime()));
				item.setMsgcontentid(privateModel.getCid());
				L.i(Const.ZL, "MessageId : " + privateModel.getCid());
				item.setHeadpicture(chatAdapter.getHead());
				addDBbyItem(item);
			}

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStart() {
		super.onStart();
		intent = getIntent();
		receverId = intent.getIntExtra(Extra.SelectedID, 0);
		L.i(Const.ZL,"RECEIVEID:"+receverId);
		int position = intent.getIntExtra(Extra.Position, -1);
		contentIntent = new Intent();
		contentIntent.putExtra(Extra.Position, position);
		chatuser = receverId;
		host = MyinfoManager.getUserInfo().getUserID();
		helper = new ChatDBHelper(this, "chat.db", null, 1);
		initView();
		dbList = (ArrayList<PrivateDetailListItem>) queryDBNewListData(host,
				chatuser);
		createList.addAll(dbList);
		L.i(Const.ZL, "dbList : " + dbList);
		chatAdapter.notifyDataSetChanged();
		mListView.getRefreshableView().setSelection(chatAdapter.getCount() - 1);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Const.CHAT_MSG);
		registerReceiver(chatReceiver, intentFilter);
		// 访问是否有最新数据接口,后台线程下载新接口数据
		L.i(Const.ZL, "后台申请数据");
		int messageId = 0;
		if (dbList == null || dbList.size() == 0) {
			// 访问getprivatedetail接口，初始化最新的15条数据
			L.i(Const.ZL, "重新申请数据");
			messageId = 0;
		} else {
			messageId = queryLastSQLId(host, chatuser);
		}
		backgroundData(messageId);

	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(chatReceiver);
	}

	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((WYService.WYBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// 窗口获取到焦点时绑定服务，失去焦点将解绑
		if (hasFocus)
			bindWYService();
		else
			unbindWYService();
	}

	/**
	 * 绑定服务
	 */
	private void bindWYService() {
		Intent mServiceIntent = new Intent(this, WYService.class);
		Uri chatURI = Uri.parse(receverId + "");
		mServiceIntent.setData(chatURI);
		bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	/**
	 * 解绑服务
	 */
	private void unbindWYService() {
		try {
			unbindService(mServiceConnection);
		} catch (IllegalArgumentException e) {
			L.e("Service wasn't bound!");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 把已读的ITEM告知网络，并清空readMessage
		if (readMessage.size() > 0) {
			String messageIds = "";
			for (int i = 0; i < readMessage.size(); i++) {
				messageIds += readMessage.get(i) + ",";
			}
			messageIds = messageIds.substring(0, messageIds.length() - 1);
			Net.request("messagecontentid", messageIds, WebApi.Message.IsRead,
					new ResponseHandler());
		}
	}

	private void initView() {
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		tvSenderName = (TextView) findViewById(R.id.private_detail_title);
		name = intent.getStringExtra(Extra.SelectedItemStr);
		tvSenderName.setText(name);
		mEditTextContent.setOnFocusChangeListener(this);
		chatAdapter = new ChatAdapter(createList, LayoutInflater.from(this),
				this);
		mListView = (PullToRefreshListView) findViewById(R.id.lv_private_detail);
		mListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setOnRefreshListener(this);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		findViewById(R.id.btn_send_private).setOnClickListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
		findViewById(R.id.private_detail_back).setOnClickListener(this);
		mListView.setAdapter(chatAdapter);
	}

	private void initData() {
		initHandler = new ResponseHandler() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<PrivateDetailListItem> list = JSON.getObject(content,
						MessagePrivateDetailRModel.class).PrivatDetailListItem;
				if (list != null) {
					// 5.2为listview绑定上adapter 和 4.1重复
					mListView.setAdapter(chatAdapter);
					// 5.3将list添加到createlist中去 此时createlist中有从网上下载的Lis和本地数据库两组数据
					createList.addAll(list);
					// head = createList.get(0).getHeadpicture();
					// headMap.put(receverId, head);
					addDBbyList(list);
					// 5.4将createlist按id和时间进行排序
					// timeOrder();
					// 5.5更新adpater 刷新一次列表
					chatAdapter.notifyDataSetChanged();
					// mListView.getRefreshableView().setSelection(
					// chatAdapter.getCount() - 1);
					mListView.getRefreshableView().setSelection(
							mListView.getRefreshableView().getCount() - 1);

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
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}
		};
		// 5.6 设置请求的历史数据为10条
		model.setPagesize(-15);
		model.setFriendsuserid(receverId);
		// 5.7设置请求历史数据为messageid前的10条
		model.setMessageid(0);
		// 5.8执行网络请求
		Net.request(model, WebApi.Message.GetPrivateDetailList, initHandler);
	}

	private void backgroundData(final int messagId) {
		initHandler = new ResponseHandler() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<PrivateDetailListItem> list = JSON.getObject(content,
						MessagePrivateDetailRModel.class).PrivatDetailListItem;
				if (list != null) {
					// 5.3将list添加到createlist中去 此时createlist中有从网上下载的Lis和本地数据库两组数据
					// if (list.get(0)) {
					//
					// }
					if ((list.get(0).getMsgcontentid() - messagId) > 15) {
						createList.clear();
					}
					createList.addAll(list);
					// head = createList.get(0).getHeadpicture();
					// headMap.put(receverId, head);
					addDBbyList(list);
					// 5.4将createlist按id和时间进行排序
					// timeOrder();
					// 5.5更新adpater 刷新一次列表
					chatAdapter.notifyDataSetChanged();
					// mListView.getRefreshableView().setSelection(
					// chatAdapter.getCount());
					mListView.getRefreshableView().setSelection(
							mListView.getRefreshableView().getCount() - 1);
					// mListView.getRefreshableView().smoothScrollToPosition(
					// chatAdapter.getCount() - 1);
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
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}
		};
		// 5.6 设置请求的历史数据为10条
		model.setPagesize(15);
		model.setFriendsuserid(receverId);
		// 5.7设置请求历史数据为messageid前的10条
		model.setMessageid(messagId);
		// 5.8执行网络请求
		Net.request(model, WebApi.Message.GetPrivateListBackground, initHandler);
	}

	public void timeOrder() {
		Comparator<PrivateDetailListItem> comparator = new Comparator<PrivateDetailListItem>() {

			@Override
			public int compare(PrivateDetailListItem o1,
					PrivateDetailListItem o2) {
				if (o1.getMsgcontentid() != 0 && o2.getMsgcontentid() != 0) {
					int m1 = o1.getMsgcontentid();
					int m2 = o2.getMsgcontentid();
					if (m1 > m2) {
						return 1;
					} else if (m1 < m2) {
						return -1;
					} else {
						return 0;
					}
				} else {
					long l1 = CalendarUtils.parseDate(o1.getEntertime());
					long l2 = CalendarUtils.parseDate(o2.getEntertime());
					if (l1 > l2) {
						return 1;
					} else if (l1 < l2) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		};
		Collections.sort(createList, comparator);
	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					// 重新请求网络数据在List中
					ArrayList<PrivateDetailListItem> list = JSON.getObject(
							content, MessagePrivateDetailRModel.class).PrivatDetailListItem;
					if (null != list) {
						int currentItemPosition = chatAdapter.getCount();
						// 把新请求的List加入到createlist中现有的之前
						createList.addAll(0, list);
						// 先不排序 timeOrder();
						// 刷新界面，有新数据
						chatAdapter.notifyDataSetChanged();
						mListView.getRefreshableView().setSelection(
								currentItemPosition);
						mListView.onRefreshComplete();
						if (model.getPagesize() > list.size()) {
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
		model.setPagesize(-10);
		model.setFriendsuserid(receverId);
		model.setMessageid(getLastId());
		Net.cacheRequest(model, WebApi.Message.GetPrivateDetailList,
				moreHandler);
	}

	public int getNewestId() {
		if (createList.size() > 0) {
			return createList.get(createList.size() - 1).getMsgcontentid();
		} else {
			return 0;
		}
	}

	public int getLastId() {
		L.i(Const.ZL, createList.toString());
		return createList.get(0).getMsgcontentid();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击头像进入个人主页
		case R.id.iv_userhead:
			PrivateDetailListItem item = (PrivateDetailListItem) createList
					.get((Integer) v.getTag(R.id.iv_userhead));
			int currentId = item.getSenderuserid();
			L.i(Const.AppName, currentId + "");
			if (currentId == MyinfoManager.getUserInfo().getUserID()) {
				Intent intent = new Intent(context,
						UserSelfDetialActivity.class);
				startActivity(intent);
			} else {
				int userID = item.getSenderuserid();
				UserEngine.startUserDetial(userID);
			}
			break;
		// 发送按扭的点击事件
		case R.id.btn_send_private:
			String content = mEditTextContent.getText().toString();
			if (content.length() == 0) {
				Toast.show("请输入私信内容");
			} else {
				if (content.length() > 200) {
					Toast.show("私信字数在200个字以内");
				} else {
					sendModel.setSubject(null);
					// 发送用户id
					sendModel.setFriendUserId(receverId);
					// 发送edittext中的内容
					sendModel.setContent(mEditTextContent.getText().toString());
					Net.request(sendModel, WebApi.Message.SendMessage,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									// 请求成功 生成一个新的newitem 用于保存发送数据
									PrivateDetailListItem newItem = new PrivateDetailListItem();
									newItem.setIsowner(true);
									// 加入内容
									newItem.setContent(mEditTextContent
											.getText().toString());
									// 设置头像为自己
									newItem.setHeadpicture(MyinfoManager
											.getUserInfo().getUserFace());
									// 把当前时间转为系统时间
									newItem.setEntertime((CalendarUtils
											.getCurrentTime()));
									// 把当前数据库最后一条id设置给新条目
									newItem.setSenderuserid(MyinfoManager
											.getUserInfo().getUserID());
									// newItem.setIsSuccess(0);
									// 给发送成功的条目设上messageid
									newItem.setMsgcontentid(Integer
											.parseInt(content));
									// 发送成功不能往数据库里加
									chatAdapter.addSingleItem(newItem);
									addDBbyItem(newItem);
									// 不用排序
									// timeOrder();
									// 更新adapter 刷新界面
									chatAdapter.notifyDataSetChanged();
									mListView.getRefreshableView()
											.setSelection(
													chatAdapter.getCount());
									mEditTextContent.setText("");
								}

								@Override
								public void onFailure(Throwable e,
										int statusCode, String content) {
									super.onFailure(e, statusCode, content);
									// 失败时也新生成一个newItem 用于保存数据
									PrivateDetailListItem newItem = new PrivateDetailListItem();
									newItem.setIsowner(true);
									newItem.setContent(mEditTextContent
											.getText().toString());
									newItem.setHeadpicture(MyinfoManager
											.getUserInfo().getUserFace());
									// 把当前时间转为系统时间
									newItem.setEntertime((CalendarUtils
											.getCurrentTime()));
									newItem.setSenderuserid(MyinfoManager
											.getUserInfo().getUserID());
									// 把发送失败的加入数据库
									// System.out.println("newItem+++++++++++++"
									// + newItem);
									chatAdapter.addSingleItem(newItem);
									// System.out.println("adapter-----"
									// + createList);
									chatAdapter.notifyDataSetChanged();
									mListView.getRefreshableView()
											.setSelection(
													chatAdapter.getCount());
									// 不用排序
									// timeOrder();
									mEditTextContent.setText("");
								}
							});
				}

			}

			break;

		case R.id.private_detail_back:
			onBack();
			break;
		}

	}

	@Override
	protected boolean onBack() {
		PrivateDetailListItem callItem = (PrivateDetailListItem) createList
				.get(createList.size() - 1);
		L.i(Const.AppName, callItem.getContent());
		contentIntent.putExtra(Extra.Snippet, callItem.getContent());
		contentIntent.putExtra(Extra.Time_Latest, callItem.getEntertime());
		// System.out.println("time*****" + callItem.getEntertime());
		setResult(RESULT_OK, contentIntent);
		finish();
		return true;

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 下拉时加载历史
		loadMore();
		// mListView.setMode(Mode.BOTH);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = false;
		initHandler.setShowProgress(isRefresh);
		mListView.setMode(Mode.PULL_FROM_END);

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			mListView.getRefreshableView().setSelection(chatAdapter.getCount());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final int realPosition = position - 1;
		final PrivateDetailListItem item = (PrivateDetailListItem) chatAdapter
				.getItem(realPosition);
		// System.out.println("item---------------------" + item);
		// if (item.getIsSuccess() == 0) {
		if (item.getMsgcontentid() != 0) {
			AlertDialog deleDialog = new AlertDialog.Builder(this)
					.setTitle("操作")
					.setItems(new String[] { "删除该条消息" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									deletInviteNoticeSingleModel.setType(1);
									deletInviteNoticeSingleModel
											.setMessagecountid(item
													.getMsgcontentid());
									Net.request(deletInviteNoticeSingleModel,
											WebApi.Message.DeleteINSingleItem,
											new ResponseHandler(true) {

												@Override
												public void onSuccess(
														String content) {
													chatAdapter
															.deleteSingleItem(realPosition);
													timeOrder();
													chatAdapter
															.notifyDataSetChanged();
													deleteSingleDB(item
															.getMsgcontentid());
												}

												@Override
												public void onFailure(
														Throwable e,
														int statusCode,
														String content) {
													super.onFailure(e,
															statusCode, content);
													Toast.show("删除失败，请重试！");
												}
											});
								}
							}).create();
			deleDialog.setCanceledOnTouchOutside(true);
			deleDialog.show();

		}

		// else {
		// // 发送失败时条目的处理
		// AlertDialog doubleDialog = new AlertDialog.Builder(this)
		// .setTitle("操作")
		// .setItems(new String[] { "删除该条消息", "重新发送" },
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// switch (which) {
		// case 0:
		// // 删除，得在界面和数据库中同时删除 先做重发
		// int thatId = item.getId();
		// chatAdapter
		// .deleteSingleItem(realPosition);
		// chatAdapter.notifyDataSetChanged();
		// // deletSingleSQL(thatId);
		//
		// break;
		//
		// case 1:
		// // 发送失败时重新发送功能 先实现能重发功能，但重发时旧的先不删
		// // 获取当前条目的id 用于到数据库里去查询
		// int thisId = item.getId();
		// // System.out.println("item++++++++"
		// // + item);
		// final int deleteId = thisId;
		// // System.out
		// // .println("thisId当前条目与数据库的id----"
		// // + thisId);
		// sendModel.setSubject(null);
		// Net.cacheRequest(sendModel,
		// WebApi.Message.SendMessage,
		// new ResponseHandler(true) {
		//
		// @Override
		// public void onSuccess(
		// String content) {
		// // 重发成功
		// // 新生成一个newitem和发送成功保持一致
		// PrivateDetailListItem newItem = new PrivateDetailListItem();
		// newItem.setIsowner(true);
		// newItem.setHeadpicture(MyinfoManager
		// .getUserInfo()
		// .getUserFace());
		// // 把当前时间转为系统时间
		// newItem.setEntertime((CalendarUtils
		// .getCurrentTime()));
		// newItem.setMsgcontentid(Integer
		// .parseInt(content));
		// chatAdapter
		// .addSingleItem(newItem);
		// int position = intent
		// .getIntExtra(
		// Extra.Position,
		// -1);
		// Intent contentIntent = new Intent();
		// contentIntent
		// .putExtra(
		// Extra.Snippet,
		// mEditTextContent
		// .getText()
		// .toString());
		// contentIntent.putExtra(
		// Extra.Position,
		// position);
		// setResult(RESULT_OK,
		// contentIntent);
		// timeOrder();
		// // deletSingleSQL(deleteId);
		// chatAdapter
		// .deleteSingleItem(realPosition);
		// // _id--;
		// chatAdapter
		// .notifyDataSetChanged();
		// mListView
		// .getRefreshableView()
		// .setSelection(
		// chatAdapter
		// .getCount());
		// mEditTextContent
		// .setText("");
		//
		// }
		//
		// @Override
		// public void onFailure(
		// Throwable e,
		// int statusCode,
		// String content) {
		// super.onFailure(e,
		// statusCode,
		// content);
		//
		// }
		// });
		// break;
		//
		// }
		//
		// }
		// }).create();
		// doubleDialog.setCanceledOnTouchOutside(true);
		// doubleDialog.show();
		// }

		return false;
	}

	private void addDBbyList(List<PrivateDetailListItem> newList) {
		// private int senderuserid;
		// private String content;
		// private String entertime;
		// private boolean isowner;
		// private String headpicture;
		// private int msgcontentid;
		// private int isSuccess = 0;
		// private int id;
		SQLiteDatabase db = helper.getWritableDatabase();
		for (int i = 0; i < newList.size(); i++) {
			PrivateDetailListItem item = newList.get(i);
			ContentValues values = new ContentValues();
			values.put(ChatDBHelper.CHAT_CONTENT, item.getContent());
			// 1 owner 2 to
			values.put(ChatDBHelper.CHAT_FROM, item.isIsowner() ? 1 : 2);
			values.put(ChatDBHelper.CHAT_TIME, item.getEntertime());
			values.put(ChatDBHelper.CHAT_USER, receverId);
			values.put(ChatDBHelper.CHAT_HOST, host);
			values.put(ChatDBHelper.CHAT_HEAD, item.getHeadpicture());
			values.put(ChatDBHelper.CHAT_MESSAGE_ID, item.getMsgcontentid());
			db.insert("chat", null, values);
			L.i(Const.AppName, "插入成功");
		}
		db.close();
	}

	private void addDBbyItem(PrivateDetailListItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatDBHelper.CHAT_CONTENT, item.getContent());
		// 1 owner 2 to
		values.put(ChatDBHelper.CHAT_FROM, item.isIsowner() ? 1 : 2);
		values.put(ChatDBHelper.CHAT_TIME, item.getEntertime());
		values.put(ChatDBHelper.CHAT_USER, receverId);
		values.put(ChatDBHelper.CHAT_HOST, host);
		values.put(ChatDBHelper.CHAT_HEAD, item.getHeadpicture());
		L.i(Const.ZL, "additem&messageid:" + item.getMsgcontentid());
		values.put(ChatDBHelper.CHAT_MESSAGE_ID, item.getMsgcontentid());
		db.insert("chat", null, values);
		db.close();
	}

	private List<PrivateDetailListItem> queryDBNewListData(int mhost, int user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<PrivateDetailListItem> newList = new ArrayList<PrivateDetailListItem>();
		Cursor cursor = db.query("chat", new String[] { "_id",
				ChatDBHelper.CHAT_CONTENT, ChatDBHelper.CHAT_TIME,
				ChatDBHelper.CHAT_USER, ChatDBHelper.CHAT_FROM,
				ChatDBHelper.CHAT_HEAD, ChatDBHelper.CHAT_MESSAGE_ID },
				"host =" + mhost + " and chatuser = " + user, null, null, null,
				null);
		int count = cursor.getCount();
		if (count > 15) {
			cursor.moveToPosition(count - 15);
		}
		while (cursor.moveToNext()) {
			PrivateDetailListItem dbItem = new PrivateDetailListItem();
			String chattime = cursor.getString(cursor
					.getColumnIndex(ChatDBHelper.CHAT_TIME));
			String content = cursor.getString(cursor
					.getColumnIndex(ChatDBHelper.CHAT_CONTENT));
			int chatid = cursor.getInt(cursor
					.getColumnIndex(ChatDBHelper.CHAT_USER));
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			// 1 from 2 to
			int chatfrom = cursor.getInt(cursor
					.getColumnIndex(ChatDBHelper.CHAT_FROM));
			int messageid = cursor.getInt(cursor
					.getColumnIndex(ChatDBHelper.CHAT_MESSAGE_ID));
			String userhead = cursor.getString(cursor
					.getColumnIndex(ChatDBHelper.CHAT_HEAD));
			dbItem.setContent(content);
			dbItem.setSenderuserid(chatid);
			dbItem.setIsowner(chatfrom == 1 ? true : false);
			dbItem.setHeadpicture(chatfrom == 1 ? MyinfoManager.getUserInfo()
					.getUserFace() : userhead);
			// head = userhead;
			dbItem.setEntertime(chattime);
			dbItem.setMsgcontentid(messageid);
			dbItem.setId(id);
			newList.add(dbItem);
		}
		// headMap.put(receverId, head);
		if (count > 500) {
			deleteDB(db, cursor, mhost, user);
		}
		cursor.close();
		db.close();
		return newList;

	}

	public int queryLastSQLId(int mhost, int user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("chat", new String[] { "_id", "messageid" },
				"host = " + mhost + " and chatuser = " + user, null, null,
				null, null);
		int id = 1;
		if (cursor.moveToLast()) {
			id = cursor.getInt(cursor.getColumnIndex("messageid"));
		}
		cursor.close();
		db.close();
		return id;
	}

	private void deleteDB(SQLiteDatabase db, Cursor cursor, int mhost, int user) {
		// db.execSQL("delete chat where _id in ( select top 20 UserID from
		// UserList order by userid asc)
		// ");
		db.delete("chat", "host =" + mhost + " and chatuser = " + user, null);

	}

	private void deleteSingleDB(int messageid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("chat", "messageid = " + messageid, null);
		db.close();
	}
}
