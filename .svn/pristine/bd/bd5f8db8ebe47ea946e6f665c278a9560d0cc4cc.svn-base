package com.whoyao.map;

import com.whoyao.AppContext;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author hyh creat_at：2013-8-13-下午2:23:49
 */
public abstract class LocReceiver extends BroadcastReceiver {

	public static final int Success_Only = 0;
	public static final int Fail_Only = 1;
	public static final int Success_And_Fail = 2;
	/**定位服务发出广播的意图*/
	public static final String LOC_SUCCESS = "com.whoyao.loc.success",LOC_FAIL = "com.whoyao.loc.fail";
	private boolean isRegistered = false;
	private IntentFilter filter;
	private Context context;

	public LocReceiver(int receiveType) {
		super();
		context = AppContext.context;
		filter = new IntentFilter();
		switch (receiveType) {
		case 0:
			filter.addAction(LOC_SUCCESS);
			break;
		case 1:
			filter.addAction(LOC_FAIL);
			break;
		case 2:
			filter.addAction(LOC_SUCCESS);
			filter.addAction(LOC_FAIL);
			break;
		default:
			break;
		}
	}

	public LocReceiver register() {
		if (!isRegistered) {
			context.registerReceiver(this, filter);
			isRegistered = true;
		}
		return this;
	}

	public LocReceiver unRegister() {
		if (isRegistered) {
			context.unregisterReceiver(this);
			isRegistered = false;
		}
		return this;
	}
	/**
	 * Intent action中包含定位成功/失败的信息
	 */
	@Override
	abstract public void onReceive(Context context, Intent intent);

}
