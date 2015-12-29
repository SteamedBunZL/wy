package com.whoyao.service;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity.BackPressHandler;
import com.whoyao.activity.BasicFragmentActivity;
import com.whoyao.activity.MainActivity;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.utils.NeedToConnect;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.utils.SocketClient;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

public class WYService extends BaseService implements BackPressHandler {
	private static final String TAG = "WYService";
	private IBinder mBinder = new WYBinder();
	private Handler mMainHandler = new Handler();
	protected static int SERVICE_NOTIFICATION = 1;
	private HashSet<String> mIsBoundTo = new HashSet<String>();
	// private Socket client;
	private NetChangeBroadCastReceiver netChangeBroadCastReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		L.i(TAG, "[SERVICE] onBind");
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.remove(chatPartner);
		}
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		BasicFragmentActivity.mListeners.add(this);
		// client = new Socket();
		netChangeBroadCastReceiver = new NetChangeBroadCastReceiver(
				WYService.this);

		L.i(Const.ZL, "create");

		// 判断网络状态，是否连接socket
		NeedToConnect.needToReConnect(true);
		registerNetworkReceiver();
		// SocketUtils.createAndConnectSocket(client, WYService.this);
		ClientEngine.initClientSetting();

	}

	private void registerNetworkReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(netChangeBroadCastReceiver, filter);
	}

	private void unRegisterNetworkReceiver() {
		this.unregisterReceiver(netChangeBroadCastReceiver);
	}

	public class NetChangeBroadCastReceiver extends BroadcastReceiver {
		private WYService service;

		public NetChangeBroadCastReceiver(WYService service) {
			this.service = service;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			State wifiState = null;
			State mobileState = null;
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			// 手机没有任何网络
			if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				L.i(Const.ZL, "手机无网络状态，中断socket");
				ConnectStatus.setStatus(false);
				NeedToConnect.needToReConnect(false);
				ConnectBreakReason.setReason(Const.REASON_NET_ERROR);
			} else {
				// 手机有网络状态
				L.i(Const.ZL, "手机有网络状态，连接socket");
				SocketClient.getInstance().connectSocket(WYService.this);
			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BasicFragmentActivity.mListeners.remove(this);
		ConnectBreakReason.setReason(Const.REASON_LOGOUT);
		unRegisterNetworkReceiver();
		ConnectStatus.setStatus(false);
		NeedToConnect.needToReConnect(false);
		clearNotifications();
		if (SocketClient.getInstance().getSocketList() != null
				&& SocketClient.getInstance().getSocketList().size() > 0) {
			Socket client = SocketClient.getInstance().getSocketList().get(0);
			SocketClient.getInstance().closeSocket(client);
		}

		L.i(Const.ZL, "服务停止运行");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mMainHandler.removeCallbacks(monitorStatus);
		mMainHandler.postDelayed(monitorStatus, 1000L);// 检查应用是否在后台运行线程
		return START_STICKY;
	}

	// 判断程序是否在后台运行的任务
	Runnable monitorStatus = new Runnable() {
		public void run() {
			try {
				mMainHandler.removeCallbacks(monitorStatus);
				// 如果在后台运行并且连接上了
				// L.i(Const.ZL,"apponforeground----"+isAppOnForeground());
				if (!AppContext.isAppOnForeground()) {
					// if (isAuthenticated())
					L.i(Const.ZL, "后台运行");
					// updateServiceNotification("Whoyao正在后台运行");
					return;
				} else {
					L.i(Const.ZL, "前台运行");
					stopForeground(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// 网络状态监听广播

	Runnable socketNetListener = new Runnable() {

		@Override
		public void run() {
			if (AppContext.isNetAvailable()) {
				// if (!GetConnectStatus.getInstance()) {
				// SocketUtils.connectSocket(client, WYService.this);
				// }
				if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_KEY_OVERDUE)) {
					// 密钥过期需要重新更新

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_NET_ERROR)) {
					// 网络连接异常需要重新连接socket
					// NeedToConnect.setStatus(false);
					SocketClient.getInstance().connectSocket(WYService.this);

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_KICK_OUT)) {
					// 被其它客户端踢除，不做处理

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_NORMA_CONNECTION)) {
					// 正常连接，不做处理

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_OVER_TIME)) {
					// 超时，需要重新连接

				} else if (TextUtils.equals(ConnectBreakReason.getInstance(),
						Const.REASON_READ_EXCEPTION)) {
					// 服务端异常，需要重新连接
					// NeedToConnect.setStatus(false);
					SocketClient.getInstance().connectSocket(WYService.this);

				}
			} else {
				// NeedToConnect.setStatus(false);
				ConnectBreakReason.setReason(Const.REASON_NET_ERROR);
			}
		}
	};

	/**
	 * 新消息处理
	 * 
	 * @param from
	 * @param userName
	 * @param message
	 */
	public void newMessage(final String message) {
		mMainHandler.post(new Runnable() {
			public void run() {
				L.i(Const.ZL, "newMessage");
				// 调用父类的通知提醒，要判断是否提醒1，是否和这个人在聊天的activity，如果不是的话就不响不振，只做通知处理。2，是否在后台运行
				String type = message.substring(0, 1);
				// if (!ClientEngine.messageSetting) {
				// return;
				// }
				switch (Integer.parseInt(type)) {
				case 0:
					// 私信
					String privateMesssage = message.substring(1,
							message.length());
					PushPrivateModel privateModel = JSON.getObject(
							privateMesssage, PushPrivateModel.class);
					L.i(Const.ZL, privateModel.toString());
					String from = privateModel.getSid() + "";
					String userName = privateModel.getSna();
					String newMessage = privateModel.getCon();
					Intent msg = new Intent();
					if (!AppContext.isAppOnForeground()) {
						// 根据设置，是否提醒及声音和振动是否开启
						// clearNotification(1+"");
						if (!ClientEngine.messageSetting) {
							return;
						}
						notifyClient(0, from, userName, newMessage, true, true);
						msg.putExtra(Extra.SelectedItem, privateModel);
						msg.setAction(Const.CHAT_MSG_WIOUT_NOTIFY);
						sendBroadcast(msg);
					} else {
						// 是否和当前用户聊天中，如果是不通知但要发广播更新聊天界面，如果不是那么通知
						if (mIsBoundTo.contains(from)) {
							// notifyClient(from, userName, message, false);
							msg.putExtra(Extra.SelectedItem, privateModel);
							msg.setAction(Const.CHAT_MSG);
							sendBroadcast(msg);
						} else {
							// 但在应用中，此时只提醒，但声音和振动不开
							notifyClient(0, from, userName, newMessage, true,
									true);
							msg.putExtra(Extra.SelectedItem, privateModel);
							msg.setAction(Const.CHAT_MSG_WIOUT_NOTIFY);
							sendBroadcast(msg);
						}
					}

					break;
				case 1:
					String noticeMessage = message.substring(1,
							message.length());
					PushPrivateModel privateModel1 = JSON.getObject(
							noticeMessage, PushPrivateModel.class);
					String from1 = privateModel1.getSid() + "";
					String userName1 = privateModel1.getSna();
					String message1 = privateModel1.getCon();
					if (!AppContext.isAppOnForeground()) {
						notifyClient(1, from1, userName1, message1, true, true);
					} else {
						notifyClient(1, from1, userName1, message1, true, false);
					}
					break;
				case 2:
					// 邀请
					String inviteMessage = message.substring(1,
							message.length());
					PushPrivateModel privateModel2 = JSON.getObject(
							inviteMessage, PushPrivateModel.class);
					String from2 = privateModel2.getSid() + "";
					String userName2 = privateModel2.getSna();
					String message2 = privateModel2.getCon();
					if (!AppContext.isAppOnForeground()) {
						// clearNotification(1+"");
						notifyClient(2, from2, userName2, message2, true, true);
					} else {
						notifyClient(2, from2, userName2, message2, true, false);
					}
					break;
				}

			}

		});
	}

	/**
	 * 更新通知栏
	 * 
	 * @param message
	 */
	public void updateServiceNotification(String message) {
		// if (!PreferenceUtils.getPrefBoolean(this,
		// PreferenceConstants.FOREGROUND, true))
		// return;
		String title = "互邀";
		Notification n = new Notification(com.whoyao.R.drawable.icon, title,
				System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		n.contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(this, title, null, n.contentIntent);
		startForeground(SERVICE_NOTIFICATION, n);
	}

	public class WYBinder extends Binder {
		public WYService getService() {
			return WYService.this;

		}
	}

	// 清除通知栏
	public void clearNotifications() {
		clearNotification();
	}

	@Override
	public void activityOnResume() {
		mMainHandler.post(monitorStatus);
	}

	@Override
	public void activityOnPause() {
		mMainHandler.postDelayed(monitorStatus, 1000L);
	}

}
