package com.whoyao.service;

import java.util.HashMap;
import java.util.Map;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Config;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.activity.MyFundActivity;
import com.whoyao.activity.MyMessageActivity;
import com.whoyao.activity.PrivateDetailActivity;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.utils.L;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;

public class BaseService extends Service {
	private static final String APP_NAME = "whoyao";
	private static final String TAG = "BaseService";
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Intent mNotificationIntent;
	// 手机振动器
	private Vibrator mVibrator;
	protected WakeLock mWakeLock;// 锁
	private Map<String, Integer> mNotificationCount = new HashMap<String, Integer>(
			2);
	private Map<String, Integer> mNotificationId = new HashMap<String, Integer>(
			2);
	private int mLastNotificationId = 2;

	@Override
	public IBinder onBind(Intent intent) {
		L.i(TAG, "called onBind()");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		L.i(TAG, "called onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		L.i(TAG, "called onCreate()");
		super.onCreate();
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);

	}

	@Override
	public void onDestroy() {
		L.i(TAG, "called onDestroy()");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.i(TAG, "called onStartCommand()");
		return START_STICKY;
	}

	private void addNotificationMGR(int type) {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// TODO 启动哪个Activity
		Class<?> cls = null;
		switch (type) {
		case 0:
			cls = PrivateDetailActivity.class;
			break;
		case 1:
			cls = MyMessageActivity.class;
			break;
		case 2:
			cls = MyMessageActivity.class;
			break;
		}
		if (cls != null) {
			mNotificationIntent = new Intent(this, cls);
			mNotificationIntent.putExtra("type", type);
		}
	}

	protected void notifyClient(int type, String fromJid, String fromUserName,
			String message, boolean showNotification, boolean showSoundVibra) {
		addNotificationMGR(type);
		if (!showNotification) {
			return;
		}
		mWakeLock.acquire();
		// mNotificationManager.cancelAll();
		setNotification(type,fromJid, fromUserName, message);

		int notifyId = 0;
		if (mNotificationId.containsKey(fromJid)) {
			notifyId = mNotificationId.get(fromJid);
		} else {
			mLastNotificationId++;
			notifyId = mLastNotificationId;
			mNotificationId.put(fromJid, Integer.valueOf(notifyId));
		}
		// If vibration is set to true, add the vibration flag to
		// the notification and let the system decide.
		// boolean vibraNotify = PreferenceUtils.getPrefBoolean(this,
		// PreferenceConstants.VIBRATIONNOTIFY, true);
		if (showSoundVibra) {
			boolean sound = ClientEngine.soundSetting;
			boolean vibraNotify = ClientEngine.vibratoSetting;
			if (sound) {
				MediaPlayer.create(this, R.raw.music).start();
			}
			if (vibraNotify) {
				mVibrator.vibrate(400);
			}
		}
		mNotificationManager.notify(notifyId, mNotification);

		mWakeLock.release();
	}

	private void setNotification(int type,String fromJid, String fromUserId,
			String message) {

		int mNotificationCounter = 0;
		if (mNotificationCount.containsKey(fromJid)) {
			mNotificationCounter = mNotificationCount.get(fromJid);
		}
		mNotificationCounter++;
		mNotificationCount.put(fromJid, mNotificationCounter);
		String author;
		// if (null == fromUserId || fromUserId.length() == 0) {
		// author = fromJid;
		// } else {
		if (type!=2) {
			if (fromJid.equals(0)) {
				fromUserId = "系统通知";
			}
			author = fromUserId + ":" + message;
		}else {
			author = fromUserId+"邀请你参加"+message;
		}
		// }
		String title = author;
		String ticker;
		// boolean isTicker = PreferenceUtils.getPrefBoolean(this,
		// // PreferenceConstants.TICKER, true);
		// if (isTicker) {
		// int newline = message.indexOf('\n');
		// int limit = 0;
		// String messageSummary = message;
		// if (newline >= 0)
		// limit = newline;
		// // if (limit > MAX_TICKER_MSG_LEN
		// // || message.length() > MAX_TICKER_MSG_LEN)
		// // limit = MAX_TICKER_MSG_LEN;
		// if (limit > 0)
		// messageSummary = message.substring(0, limit) + " [...]";
		// ticker = title + ":\n" + messageSummary;
		// } else
		ticker = author;
		mNotification = new Notification(R.drawable.ic_launcher_a, ticker,
				System.currentTimeMillis());
		// Uri userNameUri = Uri.parse(fromJid);
		L.i(Const.ZL, "notificationid : " + fromJid);
		mNotificationIntent.putExtra(Extra.SelectedID,
				Integer.parseInt(fromJid));
		// mNotificationIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME,
		// fromUserId);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// need to set flag FLAG_UPDATE_CURRENT to get extras transferred
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mNotification.setLatestEventInfo(this, title, null, pendingIntent);
		if (mNotificationCounter > 1)
			mNotification.number = mNotificationCounter;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	// private void setLEDNotification() {
	// boolean isLEDNotify = PreferenceUtils.getPrefBoolean(this,
	// PreferenceConstants.LEDNOTIFY, true);
	// if (isLEDNotify) {
	// mNotification.ledARGB = Color.MAGENTA;
	// mNotification.ledOnMS = 300;
	// mNotification.ledOffMS = 1000;
	// mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
	// }
	// }

	public void resetNotificationCounter(String userJid) {
		mNotificationCount.remove(userJid);
	}

	public void clearNotification() {
		int notifyId = 0;
		// if (mNotificationId.containsKey(Jid)) {
		// notifyId = mNotificationId.get(Jid);
		// mNotificationManager.cancel(notifyId);
		// }
		for (int i = 0; i < mNotificationId.size(); i++) {
			mNotificationManager.cancel(i);
		}
	}

}
