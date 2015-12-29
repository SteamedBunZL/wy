package com.whoyao.common;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppContext.TimeDeviationReckon;
import com.whoyao.WebApi;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * 负责更新时间误差
 * @author hyh creat_at：2013-12-5-上午11:30:43
 */
public class TimeDeviationReceiver extends BroadcastReceiver {
	private RequestParams params;
	private ResponseHandler handler;
	private boolean isRegistered;
	private Context context;
	private IntentFilter filter;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(!AppContext.isNetAvailable()){
			return;
		}
		if (null == params) {
			params = new RequestParams();
		}
		if (null == handler) {
			handler = new ResponseHandler() {
				private long timeStart;
				private TimeDeviationReckon reckon;

				@Override
				public void onStart() {
					super.onStart();
					reckon = new AppContext.TimeDeviationReckon();
				}

				@Override
				public void onSuccess(String content) {
					reckon.refreshTimeDeviation(content);
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) { }
			};
		}
		Net.request(params, WebApi.Common.GetServerTime, handler);
	}

	private IntentFilter getFilter(){
		if(null == filter){
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		}
		return filter;
	}
	
	public TimeDeviationReceiver register(Context context) {
		if (!isRegistered) {
			this.context = context;
			context.registerReceiver(this, getFilter());
			isRegistered = true;
		}
		return this;
	}

	public TimeDeviationReceiver unRegister() {
		if (isRegistered) {
			context.unregisterReceiver(this);
			isRegistered = false;
		}
		return this;
	}
}
