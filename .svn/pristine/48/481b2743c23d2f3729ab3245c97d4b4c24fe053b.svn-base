package com.whoyao.utils;

import com.whoyao.AppContext;
import com.whoyao.Const;
/**
 * @author hyh 
 * creat_at：2013-7-2-上午9:41:56 <p>
 * Log统一处理工具类
 */
public class L {
	
	public static void v(String TAG, String msg) {
		if(null == msg){
			msg = "null";
		}
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			android.util.Log.v(TAG, msg);
			break;
		case Const.STATUS_RELEASE:
			break;
		default:
			break;
		}
	}

	public static void d(String TAG, String msg) {
		if(null == msg){
			msg = "null";
		}
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			android.util.Log.d(TAG, msg);
			break;
		case Const.STATUS_RELEASE:
			break;
		default:
			break;
		}
	}
	
	public static void i(String TAG, String msg) {
		if(null == msg){
			msg = "null";
		}
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			android.util.Log.i(TAG, msg);
			break;
		case Const.STATUS_RELEASE:
			break;
		default:
			break;
		}
	}

	public static void w(String TAG, String msg) {
		if(null == msg){
			msg = "null";
		}
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			android.util.Log.w(TAG, msg);
			break;
		case Const.STATUS_RELEASE:
			break;
		default:
			break;
		}
	}

	public static void e(String TAG, String msg) {
		if(null == msg){
			msg = "null";
		}
		switch (AppContext.AppState) {
		case Const.STATUS_DEBUG:
			android.util.Log.e(TAG, msg);
			break;
		case Const.STATUS_RELEASE:
			break;
		default:
			break;
		}
	}
	public static void i(String msg) {
		i(Const.AppName,msg);
	}
	public static void w(String msg) {
		w(Const.AppName,msg);
	}
	public static void e(String msg) {
		e(Const.AppName,msg);
	}

}
