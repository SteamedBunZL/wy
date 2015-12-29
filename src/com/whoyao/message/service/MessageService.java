package com.whoyao.message.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.whoyao.Const;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.utils.L;

/**
 * @author hyh 
 * creat_at：2013-12-2-上午8:55:22
 */
public class MessageService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.e(Const.AppName, "AlarmService StartCommand");
		ClientEngine.getMobileInfo();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		L.e(Const.AppName, "AlarmService Start");
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		L.e(Const.AppName, "AlarmService IBinder");
		return null;
	}

}
