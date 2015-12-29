package com.whoyao.venue.engine;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.venue.model.CartAddItemTModel;

/**
 * @author hyh creat_at：2014-2-20-下午4:31:44
 */
public class VenueEngine {
	private static String[] serviceTypes = AppContext.getRes().getStringArray(
			R.array.venue_service_type);
	private static String[] placeTypes = AppContext.getRes().getStringArray(
			R.array.venue_place_type);
	private static ResponseHandler mAddHandler;

	/**
	 * 更加id经名称拼接成字符串,已" "分割
	 * 
	 * @param typeids
	 * @return
	 */
	public static String getServiceType(int[] typeids) {
		if (null == typeids) {
			return "";
		}
		int length = typeids.length;
		String typeStr = "";
		for (int i = 0; i < length; i++) {
			typeStr += serviceTypes[typeids[i] - 1] + " ";
		}
		return typeStr.trim();
	}

	/**
	 * 返回id对应的服务类型名称
	 * 
	 * @param typeid
	 * @return
	 */
	public static String getPlaceType(int typeid) {
		if (0 == typeid) {
			return "";
		}
		return placeTypes[typeid - 1];
	}

	/**
	 * 根据id数组返回名称数组
	 * 
	 * @param typeids
	 * @return
	 */
	public static String[] getPlaceTypes(int[] typeids) {
		if (null == typeids) {
			return null;
		}
		int length = typeids.length;
		String[] types = new String[length];
		for (int i = 0; i < length; i++) {
			types[i] = placeTypes[typeids[i] - 1];
		}
		return types;
	}

	public static String getPlaceTime(int time) {
		if (time > 9) {
			if (time == 23) {
				return "23:00-00:00";
			}
			return time + ":00-" + (time + 1) + ":00";
		} else if (time == 9) {
			return "09:00-10:00";
		} else {
			return "0" + time + ":00-0" + (time + 1) + ":00";
		}
	}

	public static void addToCart(final int value,
			final CartAddItemTModel cartItem) {

		mAddHandler = new ResponseHandler() {
			@Override
			public void onSuccess(String content) {
				cartItem.setCount(cartItem.getCount()+value);
			}
		};

		RequestParams params = Net.getRequestParamsWithNull(cartItem);
//		params.put("count", (value - cartItem.getCount()) + "");
		params.put("count", value+"");
		Net.request(params, WebApi.Venue.AddVenueCat, mAddHandler);
	}

	public static String getPlaceTypeDetial(int typedetial) {
		switch (typedetial) {
		case 1:
			return " 室内 ";
		case 2:
			return " 室外 ";
		default:
			return " 　　 ";
		}
	}
}
