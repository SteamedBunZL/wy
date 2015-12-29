package com.whoyao.common;

import com.whoyao.AppContext;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author hyh creat_at：2013-11-25-下午4:34:42
 */
public abstract class NetStateReceiver extends BroadcastReceiver {

	private IntentFilter mFilter;
	private Context context;
	private boolean isRegistered;

	public NetStateReceiver(Context context) {
		super();
		this.context = context;
		mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {// 网络状态已经改变
			if (AppContext.isNetAvailable()) {
				// String name = info.getTypeName();
				// Log.d("mark", "当前网络名称：" + name);
				onReceive(AppContext.getNetworkInfo());
			} else {// 没有可用网络
				onReceive(null);
			}
		}

	}
	public abstract void onReceive(NetworkInfo info); 
	public NetStateReceiver register() {
		if (!isRegistered) {
			context.registerReceiver(this, mFilter);
			isRegistered = true;
		}
		return this;
	}

	public NetStateReceiver unRegister() {
		if (isRegistered) {
			context.unregisterReceiver(this);
			isRegistered = false;
		}
		return this;
	}
}
