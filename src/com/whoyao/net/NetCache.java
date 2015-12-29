package com.whoyao.net;

import java.io.Serializable;
import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.whoyao.AppContext;
import com.whoyao.utils.JSON;

/**
 * 网络通信的缓存
 * 
 * @author hyh <br>
 *         2013-6-28 下午3:41:09
 *         <p>
 *         dddd
 */
public class NetCache {
	private static String CACHE_XML = "CacheFile";
	private static String CacheVersion = "CacheVersion";
	public static final long VERSION = AppContext.serviceTimeMillis();

	/**
	 * 根据请求地址和请求数据,检查对应的值是否存在,如果存在则返回,不存在着返回null
	 * 
	 * @return
	 */
	public static CacheModel getCache(String requestAddr, String requestStr) {
		String requestName = getRequestName(requestAddr, requestStr);
		SharedPreferences preferences = getCachePreferences();
		String json = preferences.getString(requestName, null);
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		return JSON.getObject(json, CacheModel.class);
	}

	/**
	 * 保存缓存 cache的name是地址?数据(JSON格式)
	 * 
	 * @param requestAddr
	 * @param requestStr
	 * @param statusCode
	 * @param headers
	 * @param responseBody
	 */
	public static void addCache(String requestAddr, String requestStr, int statusCode, Header[] headers,
			String responseBody) {
		String requestName = getRequestName(requestAddr, requestStr);
		if(TextUtils.isEmpty(requestName)){
			return;
		}
		Editor edit = getCachePreferences().edit();
		String json = JSON.toJson(new CacheModel(statusCode, responseBody));
		edit.putString(requestName, json);
		edit.commit();
	}

	/**
	 * 清理缓存
	 */
	public static void clearCaches() {
		Editor edit = getCachePreferences().edit();
		edit.clear();
		edit.putLong(CacheVersion, VERSION);
		edit.commit();
	}

	/**
	 * 获取缓存版本
	 * 
	 * @return
	 */
	public static long getCacheVersion() {
		return getCachePreferences().getLong(CacheVersion, 0);
	}

	/**
	 * 获取缓存Preferences
	 * 
	 * @return
	 */
	private static SharedPreferences getCachePreferences() {
		return AppContext.context.getSharedPreferences(CACHE_XML, Context.MODE_PRIVATE);
	}

	/**
	 * 生成缓存name值
	 * 
	 * @param requestAddr
	 * @param requestStr
	 * @return
	 */
	private static String getRequestName(String requestAddr, String requestStr) {
		if (TextUtils.isEmpty(requestAddr)) {
			return null;// 抛出异常
		}
		if (TextUtils.isEmpty(requestStr)) {
			return requestAddr;
		}
		return requestAddr + "?" + requestStr;
	}

	/**
	 * 缓存数据模型,用于生产缓存String
	 * 
	 * @author hyh creat_at：2013-7-31-下午12:03:05
	 */
	public static class CacheModel implements Serializable {
		private static final long serialVersionUID = 6107583118684562070L;
		public int statusCode;
		public String responseBody;
		
		public CacheModel(int statusCode, String responseBody) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}
	}
}
