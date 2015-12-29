package com.whoyao;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.whoyao.Const.Config;
import com.whoyao.common.TimeDeviationReceiver;
import com.whoyao.map.LocManager;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.L;

public class AppContext extends android.app.Application {

	public static Context context;
	public static MobileInfoModel mobileInfo;
	public static Activity curActivity;
	public static long loginTime;
//	public static boolean soundSetting;// 声音设置
//	public static boolean vibratoSetting;// 震动设置
//	public static boolean messageSetting;// 接收消息设置(是否保留后台服务)
	public static BDLocation location;// 当前位置信息
	private static SharedPreferences configFile;
	private static final int debug = 1626397258;
	private static final int release = -573765261;
	public static int AppState = Const.STATUS_RELEASE;
	private static ConnectivityManager connectivityManager;
	private static NetworkInfo netState;
	private static long TIME_DEVIATION = 0;
	private static ActivityManager mActivityManager;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		// 未捕获异常处理
		Thread.setDefaultUncaughtExceptionHandler(new AppException());
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		initAppState();
		initMap();
		loadReceiver();
	}
	/**
	 * 加载全局广播接收者
	 */
	private void loadReceiver() {
		TimeDeviationReceiver timeReceiver = new TimeDeviationReceiver();
		timeReceiver.register(context);
	}
	/**
	 * 初始化地图和定位
	 */
	private void initMap() {
		try {
			LocManager.getInstance().refresh();
			// MapManager.getInstance();
		} catch (Exception e) {
			AppException.handle(e);
		}
	}

	/**初始化App状态:开发or发布or被篡改*/
	private void initAppState() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_SIGNATURES);
			Signature sigs = packageInfo.signatures[0];
			switch (sigs.hashCode()) {
			case debug:
				AppState = Const.STATUS_DEBUG;
				break;
			case release:
				AppState = Const.STATUS_RELEASE;
				break;
			default:
				AppState = Const.STATUS_TAMPER;
				System.exit(0);
				return;
			}
		} catch (Exception e) {
			AppException.handle(e);
		}
	}
	public static NetworkInfo getNetworkInfo() {
		if (null == connectivityManager) {
			connectivityManager = (ConnectivityManager) AppContext.context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		netState = connectivityManager.getActiveNetworkInfo();
		return netState;
	}
	public static boolean isNetAvailable(){
		NetworkInfo info = getNetworkInfo();
		return (null != info && info.isAvailable());
	}
	public static boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(context.getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

	/**
	 * Context.getResources()
	 * 
	 * @return
	 */
	public static Resources getRes() {
		return context.getResources();
	}

	/** 获取配置xml文件 */
	public static synchronized SharedPreferences getConfigFile() {
		if (null == configFile) {
			 configFile = context.getSharedPreferences(Config.XML_NAME, Context.MODE_PRIVATE);
//			configFile = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return configFile;
	}

	/**
	 * Context.startActivity(intent);
	 * 
	 * @param intent
	 */
	public static void startAct(Class<? extends Activity> activity) {
		Intent intent = new Intent(curActivity, activity);
		curActivity.startActivity(intent);
	}

	public static void startAct(Class<? extends Activity> activity, boolean isFinishCurrent) {
		Intent intent = new Intent(curActivity, activity);
		startAct(intent, isFinishCurrent);
	}

	public static void startActivityInNewTask(Class<? extends Activity> activity) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	public static void startActivityInNewTask(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void startAct(Intent intent) {
		if(null != curActivity){
			curActivity.startActivity(intent);
		}
	}
	public static void startAct(Intent intent, boolean isFinishCurrent) {
		Activity lastAct = curActivity;
		curActivity.startActivity(intent);
		if (isFinishCurrent) {
			lastAct.finish();
		}
	}



	public static synchronized long serviceTimeMillis() {
		if(TIME_DEVIATION == 0){
			TIME_DEVIATION = getConfigFile().getLong(Config.TimeDeviation, 0);
		}
		long clientTime = System.currentTimeMillis();
		L.i("ClientMils: "  + CalendarUtils.formatDate(clientTime) +"    "+ clientTime);
		L.i("Deviation: " + TIME_DEVIATION);
		long svrTime = clientTime + TIME_DEVIATION;
		L.i("SvrMils: "  + CalendarUtils.formatDate(svrTime) +"    "+ svrTime);
		return clientTime + TIME_DEVIATION;
	}

	public static synchronized void releaseCurAct(Activity acitvity) {
		if (acitvity.equals(AppContext.curActivity)) {
			AppContext.curActivity = null;
			L.i(acitvity.toString() + "---Release");
		}
	}

	public static synchronized void refreshCurAct(Activity acitvity) {
		if (null != acitvity) {
			AppContext.curActivity = acitvity;
			L.i(acitvity.toString() + "---Refresh");
		}
	}

	
	public static class TimeDeviationReckon {
		private long startMills;
		private long endMills;
		
		public TimeDeviationReckon() {
			super();
			setStartMills();
		}
		
		/**
		 * <font color="red" size="4">必须是System.currentTimeMillis()</font>
		 */
		private void setStartMills(){
			startMills = System.currentTimeMillis();
		}
		/**
		 * <font color="red" size="4">必须是System.currentTimeMillis()</font>
		 */
		private void setEndMills(){
			endMills = System.currentTimeMillis();
		}
		public void refreshTimeDeviation(String serviceTimeString) {
			setEndMills();
			serviceTimeString = serviceTimeString.replace("\"", "").replace("/", "-");
			L.i("SvrTime: " + serviceTimeString);
			L.i("start: "+CalendarUtils.formatDate(startMills) +"    "+startMills);
			L.i("end: "+CalendarUtils.formatDate(endMills) +"    "+endMills);
			long svrTimeMills = CalendarUtils.parseDate(serviceTimeString);
			L.i("Svr: "+CalendarUtils.formatDate(svrTimeMills)+"    "+svrTimeMills);
			if(-1 != svrTimeMills){
				
				TIME_DEVIATION = svrTimeMills - (startMills/2) - (endMills/2);
				Editor edit = getConfigFile().edit();
				edit.putLong(Config.TimeDeviation, TIME_DEVIATION);
				edit.commit();
				L.i("deviation: " + TIME_DEVIATION);
				L.i("User: "+ CalendarUtils.formatDate(startMills + TIME_DEVIATION));
			}
		}
	}
	
}

// VMRuntime.getRuntime().setMinimumHeapSize(CWJ_HEAP_SIZE);
// System.gc();
// VMRuntime.getRuntime().runFinalizationSync();
// VMRuntime.getRuntime().getExternalBytesAllocated();
// VMRuntime.getRuntime().setTargetHeapUtilization(newTarget);