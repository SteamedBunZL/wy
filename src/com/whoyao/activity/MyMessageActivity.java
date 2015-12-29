package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Header;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.MessageInviteCreateListAdapter;
import com.whoyao.adapter.MessageNoticeAdapter;
import com.whoyao.adapter.MessagePrivateCreateListAdapter;
import com.whoyao.db.ChatDBHelper;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.DeleteOneUserPrivateTModel;
import com.whoyao.model.EventInfoModel;
import com.whoyao.model.FriendAgreeOrDisAgreeTModel;
import com.whoyao.model.InviteData;
import com.whoyao.model.InviteListItem;
import com.whoyao.model.InviteNoticeDeleteSingleItemModel;
import com.whoyao.model.MessageDeleteTModel;
import com.whoyao.model.MessageInviteRModel;
import com.whoyao.model.MessageInviteTModel;
import com.whoyao.model.MessageIsReadTModel;
import com.whoyao.model.MessageNoticeRModel;
import com.whoyao.model.MessageNoticeTModel;
import com.whoyao.model.MessagePrivateRModel;
import com.whoyao.model.MessagePrivateTModel;
import com.whoyao.model.NoticeData;
import com.whoyao.model.NoticeListItem;
import com.whoyao.model.PrivateListItem;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/** 我的私信 */
public class MyMessageActivity extends BasicActivity implements
		OnCheckedChangeListener, OnClickListener, OnItemClickListener,
		OnRefreshListener2<ListView>, OnItemLongClickListener {
	// 声明标题切换的RadioGroup
	private RadioGroup rgTabChange;
	// 声明后退键的Button
	private Button backButton;
	// 声明带下拉刷新的ListView
	private PullToRefreshListView mListView;
	// 发送模型
	private MessagePrivateTModel model;
	private MessageInviteTModel inviteModel;
	private MessageNoticeTModel noticeModel;
	private InviteNoticeDeleteSingleItemModel deletInviteNoticeSingleModel;
	private DeleteOneUserPrivateTModel deleteOneUserPrivateTModel;
	private MessageIsReadTModel isReadTModel;
	private MessageDeleteTModel deletAllModel;
	private ResponseHandler initHandler;
	private ResponseHandler initInviteHandler;
	private ResponseHandler initNoticeHandler;
	private boolean isRefresh;
	private MessagePrivateCreateListAdapter privateLetterAdapter;
	private MessageInviteCreateListAdapter inviteCreateListAdapter;
	private MessageNoticeAdapter noticeCreateListAdapter;
	private List<PrivateListItem> createList = new ArrayList<PrivateListItem>();
	private List<InviteListItem> inviteCreateList = new ArrayList<InviteListItem>();
	private List<NoticeListItem> noticeCreateList = new ArrayList<NoticeListItem>();
	private ResponseHandler moreHandler;
	private ResponseHandler inviteMoreHandler;
	private ResponseHandler noticeMoreHandler;
	private int type = 0;
	private static int PRIVATE_TYPE = 0;
	private static int INVITE_TYPE = 1;
	private static int INFORM_TYPE = 2;
	public static int USER_ID = MyinfoManager.getUserInfo().getUserID();
	private FriendAgreeOrDisAgreeTModel friendModel;
	private static final int REQUEST_CODE = 1;
	private TextView tvDelete;
	private RadioButton privateButton;
	private RadioButton noticeButton;
	private RadioButton inviteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_my);
		model = new MessagePrivateTModel();
		inviteModel = new MessageInviteTModel();
		noticeModel = new MessageNoticeTModel();
		friendModel = new FriendAgreeOrDisAgreeTModel();
		deletInviteNoticeSingleModel = new InviteNoticeDeleteSingleItemModel();
		deleteOneUserPrivateTModel = new DeleteOneUserPrivateTModel();
		deletAllModel = new MessageDeleteTModel();
		isReadTModel = new MessageIsReadTModel();
		rgTabChange = (RadioGroup) findViewById(R.id.rg_message_title);
		rgTabChange.setOnCheckedChangeListener(this);
		backButton = (Button) findViewById(R.id.page_btn_back);
		findViewById(R.id.private_delete).setOnClickListener(this);
		backButton.setOnClickListener(this);
		// 加载视图
		initView();
		// TODO 加载什么视图，应该判断？？？？？？？？？？？？？
		// initPrivateData();
		// 绑定私信adapter
		// mListView.setAdapter(privateLetterAdapter);
		Intent notificationIntent = getIntent();
		int chooseType = notificationIntent.getIntExtra("type", 0);
		switch (chooseType) {
		case 0:
			// 私信
			type = PRIVATE_TYPE;
			privateButton.setChecked(true);
			initPrivateData();
			mListView.setAdapter(privateLetterAdapter);
			break;
		case 1:
			// 通知
			type = INFORM_TYPE;
			noticeButton.setChecked(true);
			initNoticeData();
			mListView.setAdapter(noticeCreateListAdapter);
			break;
		case 2:
			// 邀请
			type = INVITE_TYPE;
			inviteButton.setChecked(true);
			initInviteData();
			mListView.setAdapter(inviteCreateListAdapter);
			break;
		}

	}

	/** 初始化视图 */
	private void initView() {
		privateButton = (RadioButton) findViewById(R.id.rb_message_title_private);
		noticeButton = (RadioButton) findViewById(R.id.rb_message_title_inform);
		inviteButton = (RadioButton) findViewById(R.id.rb_message_title_invite);

		tvDelete = (TextView) findViewById(R.id.private_delete);
		if (type == PRIVATE_TYPE) {
			tvDelete.setVisibility(View.GONE);
		} else {
			tvDelete.setVisibility(View.VISIBLE);
		}
		privateLetterAdapter = new MessagePrivateCreateListAdapter(createList,
				LayoutInflater.from(this), this);
		// 设置RadioGroup的点击事件
		inviteCreateListAdapter = new MessageInviteCreateListAdapter(
				inviteCreateList, LayoutInflater.from(this), this);
		noticeCreateListAdapter = new MessageNoticeAdapter(noticeCreateList,
				LayoutInflater.from(this), this);
		mListView = (PullToRefreshListView) findViewById(R.id.message_lv);
		mListView.getRefreshableView().setScrollBarStyle(
				View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));

	}

	/** 初始化私信界面数据 */
	private void initPrivateData() {

		initHandler = new ResponseHandler(true) {

			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<PrivateListItem> list = JSON.getObject(content,
						MessagePrivateRModel.class).PrivateListItem;
				if (list != null) {
					createList.clear();
					createList.addAll(list);
					privateLetterAdapter.notifyDataSetChanged();
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

		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.Message.GetPrivate, initHandler);
	}

	private void loadMore() {
		if (null == moreHandler) {
			moreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<PrivateListItem> list = JSON.getObject(content,
							MessagePrivateRModel.class).PrivateListItem;
					if (null != list) {
						createList.addAll(list);
						privateLetterAdapter.notifyDataSetChanged();
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
						mListView.setMode(Mode.BOTH);
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Message.GetPrivate, moreHandler);
	}
	

	/** 初始化邀请界面数据 */
	private void initInviteData() {

		initInviteHandler = new ResponseHandler(true) {

			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				ArrayList<InviteListItem> list = null;
				try {
					list = JSON.getObject(content, MessageInviteRModel.class).InviteListItem;
				} catch (Exception e) {
					if (list == null) {
						tvDelete.setVisibility(View.INVISIBLE);
						return;
					}
				}
				if (list != null) {
					tvDelete.setVisibility(View.VISIBLE);
					inviteCreateList.clear();
					inviteCreateList.addAll(list);
					mListView.setAdapter(inviteCreateListAdapter);
					inviteCreateListAdapter.notifyDataSetChanged();
				} else {
					tvDelete.setVisibility(View.INVISIBLE);
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				mListView.onRefreshComplete();
				isRefresh = false;
				if (400 == statusCode) {
					tvDelete.setVisibility(View.INVISIBLE);
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

		};

		initInviteHandler.setShowProgress(!isRefresh);
		inviteModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(inviteModel, WebApi.Message.GetInviteList,
				initInviteHandler);
	}

	private void loadInviteMore() {
		if (null == inviteMoreHandler) {
			inviteMoreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<InviteListItem> list = JSON.getObject(content,
							MessageInviteRModel.class).InviteListItem;
					if (null != list) {
						tvDelete.setVisibility(View.VISIBLE);
						inviteCreateList.addAll(list);
						inviteCreateListAdapter.notifyDataSetChanged();
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
						tvDelete.setVisibility(View.INVISIBLE);
						mListView.setMode(Mode.PULL_FROM_START);
						mListView.setMode(Mode.BOTH);
					}
				}
			};
		}
		initInviteHandler.setShowProgress(!isRefresh);
		inviteModel.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(inviteModel, WebApi.Message.GetInviteList,
				inviteMoreHandler);
	}
	

	@Override
	protected void onStop() {
		super.onStop();
		NetCache.clearCaches();
	}

	/** 初始化通知界面 */
	private void initNoticeData() {

		initNoticeHandler = new ResponseHandler(true) {

			@Override
			public void onSuccess(String content) {
				mListView.onRefreshComplete();
				isRefresh = false;
				MessageNoticeRModel object = null;
				try {
					object = JSON.getObject(content, MessageNoticeRModel.class);
				} catch (Exception e) {

				}
				if (object == null) {
					tvDelete.setVisibility(View.INVISIBLE);
					return;
				}
				ArrayList<NoticeListItem> list = object.NoticeListItem;
				if (list != null) {
					tvDelete.setVisibility(View.VISIBLE);
					noticeCreateList.clear();
					noticeCreateList.addAll(list);
					mListView.setAdapter(noticeCreateListAdapter);
					noticeCreateListAdapter.notifyDataSetChanged();
				} else {
					tvDelete.setVisibility(View.INVISIBLE);
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				mListView.onRefreshComplete();
				isRefresh = false;
				if (400 == statusCode) {
					tvDelete.setVisibility(View.INVISIBLE);
					mListView.setMode(Mode.PULL_FROM_START);
				}
			}

		};

		initNoticeHandler.setShowProgress(!isRefresh);
		noticeModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(noticeModel, WebApi.Message.GetNoticeList,
				initNoticeHandler);
	}

	private void loadNoticeMore() {
		if (null == inviteMoreHandler) {
			noticeMoreHandler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<NoticeListItem> list = JSON.getObject(content,
							MessageNoticeRModel.class).NoticeListItem;
					if (null != list) {
						tvDelete.setVisibility(View.VISIBLE);
						noticeCreateList.addAll(list);
						mListView.onRefreshComplete();
						noticeCreateListAdapter.notifyDataSetChanged();
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
						tvDelete.setVisibility(View.INVISIBLE);
						mListView.setMode(Mode.PULL_FROM_START);
						mListView.setMode(Mode.BOTH);
					}
				}
			};
		}
		initNoticeHandler.setShowProgress(!isRefresh);
		noticeModel.setPageindex(noticeModel.getPageindex() + 1);
		Net.cacheRequest(noticeModel, WebApi.Message.GetNoticeList,
				noticeMoreHandler);
	}

	/** RadioGroup的点击事件 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_message_title_private:
			type = PRIVATE_TYPE;
			tvDelete.setVisibility(View.GONE);
			mListView.setMode(Mode.BOTH);
			NetCache.clearCaches();
			initPrivateData();
			mListView.setAdapter(privateLetterAdapter);
			privateLetterAdapter.notifyDataSetChanged();
			break;
		case R.id.rb_message_title_invite:
			type = INVITE_TYPE;
			tvDelete.setVisibility(View.VISIBLE);
			NetCache.clearCaches();
			initInviteData();
			mListView.setMode(Mode.BOTH);
			mListView.setAdapter(inviteCreateListAdapter);
			inviteCreateListAdapter.notifyDataSetChanged();
			break;
		case R.id.rb_message_title_inform:
			type = INFORM_TYPE;
			tvDelete.setVisibility(View.VISIBLE);
			NetCache.clearCaches();
			initNoticeData();
			mListView.setAdapter(noticeCreateListAdapter);
			noticeCreateListAdapter.notifyDataSetChanged();
			mListView.setMode(Mode.BOTH);
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		if (type == PRIVATE_TYPE) {
			initPrivateData();
		} else if (type == INVITE_TYPE) {
			initInviteData();
		} else if (type == INFORM_TYPE) {
			initNoticeData();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (type == PRIVATE_TYPE) {
			loadMore();
		} else if (type == INVITE_TYPE) {
			loadInviteMore();
		} else if (type == INFORM_TYPE) {
			loadNoticeMore();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.iv_inform_head:
			if (resultCode == RESULT_OK) {
				int state = data.getIntExtra(Extra.State, State.None);
				int position = data.getIntExtra(Extra.Position, -1);
				if (position != -1) {
					NoticeListItem item = (NoticeListItem) noticeCreateListAdapter
							.getItem(position);
					if (state != 0) {
						item.setResult(state);
						noticeCreateListAdapter.notifyDataSetChanged();
					}
				}
			}
			break;

		case REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				int position2 = data.getIntExtra(Extra.Position, -1);
				String content = data.getStringExtra(Extra.Snippet);
				String time = data.getStringExtra(Extra.Time_Latest);
				if (position2 == -1 || position2 > createList.size()) {

				} else {
					if (content != null) {
						PrivateListItem item = (PrivateListItem) privateLetterAdapter
								.getItem(position2);
						item.setContent(content);
						item.setSendTime(time);
						privateLetterAdapter.notifyDataSetChanged();
					}
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// 删除全部按钮及返回
		switch (v.getId()) {

		case R.id.page_btn_back:
			finish();
			break;
		case R.id.private_delete:
			if (type == PRIVATE_TYPE) {
				// 删除全部私信
				deletAllModel.setType(2);
				MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
				dialog.setTitle("提示");
				dialog.setHtmlMessage("确定将全部私信删除么？");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						deletInviteNoticeSingleModel.setType(1);
						Net.request(deletAllModel,
								WebApi.Message.DeleteAllMessage,
								new ResponseHandler(true) {

									@Override
									public void onSuccess(int statusCode,
											Header[] headers, String content) {
										super.onSuccess(statusCode, headers,
												content);
										privateLetterAdapter.deleteAll();
										privateLetterAdapter
												.notifyDataSetChanged();
									}

									@Override
									public void onFailure(Throwable e,
											int statusCode, String content) {
										super.onFailure(e, statusCode, content);
										Toast.show("删除失败，请重试。");
									}

								});

					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
			} else if (type == INVITE_TYPE) {
				// 刪除邀請全部
				deletAllModel.setType(2);
				MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
				dialog.setTitle("提示");
				dialog.setHtmlMessage("确定将全部邀请删除么？");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						deletInviteNoticeSingleModel.setType(2);
						Net.request(deletAllModel,
								WebApi.Message.DeleteAllMessage,
								new ResponseHandler(true) {

									@Override
									public void onSuccess(int statusCode,
											Header[] headers, String content) {
										super.onSuccess(statusCode, headers,
												content);
										inviteCreateListAdapter.deleteAll();
										inviteCreateListAdapter
												.notifyDataSetChanged();
										tvDelete.setVisibility(View.INVISIBLE);
									}

									@Override
									public void onFailure(Throwable e,
											int statusCode, String content) {
										super.onFailure(e, statusCode, content);
										Toast.show("删除失败，请重试。");
									}

								});

					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
			} else if (type == INFORM_TYPE) {
				// 删除全部通知
				deletAllModel.setType(3);
				MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
				dialog.setTitle("提示");
				dialog.setHtmlMessage("确定将全部通知删除么？");
				dialog.setLeftButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						deletInviteNoticeSingleModel.setType(2);
						Net.request(deletAllModel,
								WebApi.Message.DeleteAllMessage,
								new ResponseHandler(true) {

									@Override
									public void onSuccess(int statusCode,
											Header[] headers, String content) {
										super.onSuccess(statusCode, headers,
												content);
										noticeCreateListAdapter.deleteAll();
										noticeCreateListAdapter
												.notifyDataSetChanged();
										tvDelete.setVisibility(View.INVISIBLE);
									}

									@Override
									public void onFailure(Throwable e,
											int statusCode, String content) {
										super.onFailure(e, statusCode, content);
										Toast.show("删除失败，请重试。");
									}
								});
					}
				});
				dialog.setRightButton("取消", null);
				dialog.show();
			}
			break;
		case R.id.btn_inform_agree:
			L.i(Const.AppName, "同意");
//			System.out.println("同意");
			final NoticeListItem item = noticeCreateList.get((Integer) v
					.getTag(R.id.btn_inform_agree));
			item.setIsread(2);
			friendModel.setFrienduserid(item.getSenduserid());
			friendModel.setIsagree(1);
			friendModel.setMesscontentid(item.getMessagecontentid());
			Net.cacheRequest(friendModel, WebApi.User.FriendRequestManage,
					new ResponseHandler(true) {

						@Override
						public void onSuccess(String content) {
							item.setResult(1);
							noticeCreateListAdapter.notifyDataSetChanged();
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
							if (statusCode == 417) {
								onSuccess(content);
							}
						}
					});
			break;
		case R.id.btn_inform_disagree:
			L.i(Const.AppName, "忽略");
			final NoticeListItem item1 = noticeCreateList.get((Integer) v
					.getTag(R.id.btn_inform_disagree));
			item1.setIsread(2);
			friendModel.setFrienduserid(item1.getSenduserid());
			friendModel.setIsagree(0);
			friendModel.setMesscontentid(item1.getMessagecontentid());
			Net.cacheRequest(friendModel, WebApi.User.FriendRequestManage,
					new ResponseHandler(true) {

						@Override
						public void onSuccess(String content) {
							item1.setResult(2);
							noticeCreateListAdapter.notifyDataSetChanged();
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
							Toast.show("网络超时，请重试");
						}
					});
			break;
		case R.id.iv_inform_head:
			int position = (Integer) v.getTag(R.id.iv_inform_head);
			NoticeListItem item2 = noticeCreateList.get(position);
			Intent intent = new Intent(context, UserOtherDetialActivity.class);
			int userId = item2.getSenduserid();
			if (item2.getResult() == 0) {
				int messageId = item2.getMessagecontentid();
				intent.putExtra(Extra.SelectedItem, messageId);
				intent.putExtra(Extra.Position, position);
			}
			L.i(Const.AppName, "postion--" + position + "----item2-----"
					+ item2 + "userid" + userId);
			intent.putExtra(Extra.SelectedID, userId);
			startActivityForResult(intent, R.id.iv_inform_head);
			break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 设置私信为已读，点击后数字消失（本地做），自己发的消息不带数字提示，未读数字和外面界面的关联
		if (type == PRIVATE_TYPE) {
			PrivateListItem item = (PrivateListItem) privateLetterAdapter
					.getItem(position - 1);
			item.setUnReadCount(0);
			privateLetterAdapter.notifyDataSetChanged();
			Intent intent = new Intent(MyMessageActivity.this,
					PrivateDetailActivity.class);
			if (item.getReceiverId() == USER_ID) {
				intent.putExtra(Extra.SelectedID, item.getSenderId());
			} else {
				intent.putExtra(Extra.SelectedID, item.getReceiverId());
			}
			String username = item.getUserName();
			intent.putExtra(Extra.SelectedItemStr, username);
			intent.putExtra(Extra.Position, position - 1);
			startActivityForResult(intent, REQUEST_CODE);
		} else if (type == INVITE_TYPE) {
			// TODO 邀请跳转
			final InviteListItem item = (InviteListItem) inviteCreateListAdapter
					.getItem(position - 1);
			Map<String, String> mapType = new TreeMap<String, String>();
			List<InviteData> dataList = new ArrayList<InviteData>();
			dataList = item.getData();
			String activtyId = null;
			String inviteId = null;
			if (dataList != null && !dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					mapType.put(dataList.get(i).getKey(), dataList.get(i)
							.getValue());
				}
				if (mapType.containsKey("ActivityId")) {
					activtyId = mapType.get("ActivityId");
				}
				if (mapType.containsKey("InviteId")) {
					inviteId = mapType.get("InviteId");
				}
			}
			if (item.getIsread() == 2) {

			} else {
				isReadTModel.setMessagecontentid(item.getMessagecontentid());
				Net.request(isReadTModel, WebApi.Message.IsRead,
						new ResponseHandler(true) {

							@Override
							public void onSuccess(int statusCode,
									Header[] headers, String content) {
								// TODO 邀请界面的跳转
								super.onSuccess(statusCode, headers, content);
								item.setIsread(2);
								inviteCreateListAdapter.notifyDataSetChanged();
							}
						});
			}
			if (item.getContenttype() == 17) {
				if (inviteId != null) {
					Intent inviteIntent = new Intent(context,
							MyInviteDetailActivity.class);
					inviteIntent.putExtra("inviteid",
							Integer.parseInt(inviteId));
					startActivity(inviteIntent);
				}

			} else if (item.getContenttype() == 15) {
				if (activtyId != null) {
					Intent activityInent = new Intent(context,
							EventDetialActivity.class);
					activityInent.putExtra(Extra.SelectedID,
							Integer.parseInt(activtyId));
					startActivity(activityInent);
				}

			}

		} else if (type == INFORM_TYPE) {
			// TODO 通知跳转
			L.i(Const.AppName, position + "");
			NoticeListItem item = (NoticeListItem) noticeCreateListAdapter
					.getItem(position - 1);
			int contentType = item.getContenttype();
			Map<String, String> mapType = new TreeMap<String, String>();
			List<NoticeData> dataList = new ArrayList<NoticeData>();
			dataList = item.getData();
			String activtyId = null;
			String inviteId = null;
			String inviteActivityId = null;
			if (dataList != null) {
				for (int i = 0; i < dataList.size(); i++) {
					mapType.put(dataList.get(i).getKey(), dataList.get(i)
							.getValue());
				}
				if (mapType.containsKey("ActivityId")) {
					activtyId = mapType.get("ActivityId");
				}
				if (mapType.containsKey("InviteId")) {
					inviteId = mapType.get("InviteId");
				}
				if (mapType.containsKey("activityId")) {
					inviteActivityId = mapType.get("activityId");
				}
			}
			item.setIsread(2);
			TextView tvTextView = (TextView) view
					.findViewById(R.id.tv_inform_name);
			tvTextView.setTextColor(getResources().getColor(R.color.gray_text));
			// isReadTModel.setMessagecontentid(item.getMessagecontentid());
			// Net.request(isReadTModel, WebApi.Message.IsRead,
			// new ResponseHandler() {
			// @Override
			// public void onSuccess(int statusCode, Header[] headers,
			// String content) {
			// super.onSuccess(statusCode, headers, content);
			// }
			// });
			switch (contentType) {
			// 跳转到活动详情
			case 5:
			case 6:
			case 7:
			case 8:
			case 15:
			case 16:
				Intent intentActivity = new Intent(context,
						EventDetialActivity.class);
				intentActivity.putExtra(Extra.SelectedID,
						Integer.parseInt(activtyId));
				startActivity(intentActivity);
				break;
			case 24:
				Intent intentAcitivityTwo = new Intent(context,
						EventDetialActivity.class);
				intentAcitivityTwo.putExtra(Extra.SelectedID,
						Integer.parseInt(inviteActivityId));
				startActivity(intentAcitivityTwo);
				break;
			// 跳转到人员管理
			case 10:
			case 11:
			case 12:
				Intent joinIntent = new Intent(this,
						EventJoinerMgrActivity.class);
				joinIntent.putExtra(Extra.SelectedID,
						Integer.parseInt(activtyId));
				startActivity(joinIntent);
				break;
			// 跳转到活动相册
			case 14:
				Intent photoIntent = new Intent(this,
						EventPhotoAddActivity.class);
				photoIntent.putExtra(Extra.SelectedID,
						Integer.parseInt(activtyId));
				startActivity(photoIntent);
				break;
			// TODO 跳转到活动评分
			case 9:
				CallBack<String> callback = new CallBack<String>() {
					@Override
					public void onCallBack(boolean isSuccess, String data) {
						if (isSuccess) {
							EventInfoModel info = EventDetialManager.eventInfo;
							if (0 != info.getActivityatmospherevalue()
									|| 0 != info.getActivityattendedvalue()
									|| 0 != info.getActivitypricevalue()
									|| 0 != info.getActivityaddressvalue()) {
								AppContext
										.startAct(EventUserValuActivity.class);
							} else {
								AppContext
										.startAct(EventValuationActivity.class);
							}
						}
					}
				};
				EventDetialManager.getInstance().getDetial(
						Integer.parseInt(activtyId), callback);
				break;
			// 跳转到邀请详情
			case 17:
				Intent inviteIntent = new Intent(this,
						MyInviteDetailActivity.class);
				inviteIntent.putExtra(Extra.SelectedID,
						Integer.parseInt(inviteId));
				startActivity(inviteIntent);
				break;
			// 跳转到个人详情
			case 18:

				break;
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
				Intent intent = new Intent(context,
						UserOtherDetialActivity.class);
				int userId = item.getSenduserid();
				if (item.getResult() == 0) {
					int messageId = item.getMessagecontentid();
					intent.putExtra(Extra.SelectedItem, messageId);
					intent.putExtra(Extra.Position, position - 1);
				}
				L.i(Const.AppName, "postion--" + position + "----item2-----"
						+ item + "userid" + userId);
				intent.putExtra(Extra.SelectedID, userId);
				startActivityForResult(intent, R.id.iv_inform_head);
				break;

			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 删除邀请单条
		final int realPosition = position - 1;
		if (type == INVITE_TYPE) {
			final InviteListItem item = (InviteListItem) inviteCreateListAdapter
					.getItem(realPosition);
			MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
			dialog.setTitle("提示");
			dialog.setHtmlMessage("确定将这条邀请删除么？");
			dialog.setLeftButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					deletInviteNoticeSingleModel.setType(2);
					deletInviteNoticeSingleModel.setMessagecountid(item
							.getMessagecontentid());
					Net.request(deletInviteNoticeSingleModel,
							WebApi.Message.DeleteINSingleItem,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, String content) {
									super.onSuccess(statusCode, headers,
											content);
									inviteCreateListAdapter
											.deleteSingleItem(realPosition);
									inviteCreateListAdapter
											.notifyDataSetChanged();
									if (inviteCreateList.size() == 0) {
										tvDelete.setVisibility(View.INVISIBLE);
									} else {
										tvDelete.setVisibility(View.VISIBLE);
									}
								}

								@Override
								public void onFailure(Throwable e,
										int statusCode, String content) {
									super.onFailure(e, statusCode, content);
									Toast.show("删除失败，请重试。");
								}

							});
				}
			});
			dialog.setRightButton("取消", null);
			dialog.show();
		} else if (type == INFORM_TYPE) {
			final NoticeListItem item = (NoticeListItem) noticeCreateListAdapter
					.getItem(realPosition);
			MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
			dialog.setTitle("提示");
			dialog.setHtmlMessage("确定将这条通知删除么？");
			dialog.setLeftButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					deletInviteNoticeSingleModel.setType(3);
					deletInviteNoticeSingleModel.setMessagecountid(item
							.getMessagecontentid());
					Net.request(deletInviteNoticeSingleModel,
							WebApi.Message.DeleteINSingleItem,
							new ResponseHandler(true) {

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, String content) {
									super.onSuccess(statusCode, headers,
											content);
									noticeCreateListAdapter
											.deleteSingleItem(realPosition);
									noticeCreateListAdapter
											.notifyDataSetChanged();
									if (noticeCreateList.size() == 0) {
										tvDelete.setVisibility(View.INVISIBLE);
									} else {
										tvDelete.setVisibility(View.VISIBLE);
									}

								}

								@Override
								public void onFailure(Throwable e,
										int statusCode, String content) {
									super.onFailure(e, statusCode, content);
									Toast.show("删除失败，请重试。");
								}

							});

				}
			});
			dialog.setRightButton("取消", null);
			dialog.show();
		} else {
			// 删除单条私信
			final PrivateListItem item = (PrivateListItem) privateLetterAdapter
					.getItem(realPosition);
			MessageDialog dialog = new MessageDialog(MyMessageActivity.this);
			dialog.setTitle("提示");
			dialog.setHtmlMessage("确定将这条私信删除么？");
			dialog.setLeftButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (item.getReceiverId() == USER_ID) {
						deleteOneUserPrivateTModel.setTouserid(item
								.getSenderId());
					} else {
						deleteOneUserPrivateTModel.setTouserid(item
								.getReceiverId());
					}
					Net.request(deleteOneUserPrivateTModel,
							WebApi.Message.DeleteOnePrivate,
							new ResponseHandler(true) {

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, String content) {
									super.onSuccess(statusCode, headers,
											content);
									privateLetterAdapter
											.deleteSingleItem(realPosition);
									privateLetterAdapter.notifyDataSetChanged();
								}

								@Override
								public void onFailure(Throwable e,
										int statusCode, String content) {
									super.onFailure(e, statusCode, content);
									Toast.show("删除失败，请重试。");
								}
							});
				}
			});
			dialog.setRightButton("取消", null);
			dialog.show();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.i(Const.AppName, "onDestroy");
	}

}
