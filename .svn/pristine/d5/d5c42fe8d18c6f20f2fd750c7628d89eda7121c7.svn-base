package com.whoyao.utils;

import java.util.ArrayList;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonParseException;
import com.whoyao.AppException;
/**
 * Json相关工具类
 * @author hyh
 */
public class JSON {

	private static Gson gson = new Gson();

	/**
	 * 将对象转换为Json String
	 * @author HYH
	 * @param t
	 * @return
	 */
	public static <T> String toJson(T t) {
		return gson.toJson(t);
	}

	/**
	 * 将json解析为(clasz)Object
	 * @author HYH
	 * @param json
	 * @param clasz
	 * @return
	 */
	public static <T> T getObject(String json, Class<T> clasz) {
		try {
			return gson.fromJson(json, clasz);
		} catch (JsonParseException e) {
			AppException.handle(e);
		}
		return null;
	}

	/**
	 * 将json中的list解析为ArrayList<itemClass>
	 * @author HYH
	 * @param json
	 * @param itemClass
	 * @return
	 */
	public static <T> ArrayList<T> getList(String json, Class<T> itemClass) {
		JSONArray jsonArray;
		ArrayList<T> list = new ArrayList<T>();
		String jsonItem;
		T obj;
		
		try {
			jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonItem = jsonArray.getString(i);
				obj = gson.fromJson(jsonItem, itemClass);
				list.add(obj);
			}
		} catch (JSONException e) {
			AppException.handle(e);
		}
		return list;
	}

}
