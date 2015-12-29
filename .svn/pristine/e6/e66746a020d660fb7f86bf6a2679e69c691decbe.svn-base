package com.whoyao.utils;

import com.whoyao.AppContext;
import com.whoyao.db.AreaListDB;
import com.whoyao.model.AreaData;



/**
 * 地址工具
 * @author HYH
 * create at：2013-3-28 下午01:14:39
 */
public class AddressUtil {
	// TODO 应该把这个方法放在AreaData类中
	/**
	 * 根据父地址拼接出地址字符串
	 * @param Code
	 * @return
	 */
	public static String getFullAddr(int Code){
		String addrStr = "";
		AreaListDB db = new AreaListDB();
		AreaData area = db.getAreaByCode(Code);
		if(null != area){
			addrStr = area.getAreaName();
			if(!area.isProvince()){
				addrStr = getFullAddr(area.getParentCode()) +" "+ addrStr;
			}
		}
		return addrStr.trim();
	}
	public static String getSimpleAddr(int Code){
		if(0 == Code || -1 == Code)
			return "";
		AreaListDB db = new AreaListDB();
		AreaData area = db.getAreaByCode(Code);
		return area.getAreaName();
	}
	@Deprecated
	private static String getCityName(int provinceCode ,int citycode){
		switch (provinceCode) {
		case 110000://如果是直辖市
			return "北京";
		case 120000:
			return "天津";
		case 310000:
			return "上海";
		case 500000:
			return "重庆";
		case 810000:
			return "香港";
		case 820000:
			return "澳门";
		default://不是直辖市
			return AddressUtil.getSimpleAddr(citycode);
		}
	}
	public static String getCityName(int citycode){
		return AddressUtil.getSimpleAddr(citycode);
	}
	
	/**
	 * 根据父地址拼接出地址字符串
	 * @param Code
	 * @return
	 */
	public static String getAddress(int Code){
		String addrStr = "";
		AreaListDB db = new AreaListDB();
		AreaData area = db.getAreaByCode(Code);
		addrStr = area.getAreaName();
		
		L.i("AddressUtil", "addrStr = " + addrStr);
		
		if(!area.isProvince()){
			addrStr = getAddress(area.getParentCode()) +" "+ addrStr;
		}
		return addrStr.trim();
	}
	
	/**更加定位坐标获取AreaData(具体到City)*/
	public static AreaData getLocArea(){
		if(null == AppContext.location){
			return null;
		}else{
			AreaData area = new AreaListDB().getAreaByName(AppContext.location.getCity());
			return area;
		}
	}
}
