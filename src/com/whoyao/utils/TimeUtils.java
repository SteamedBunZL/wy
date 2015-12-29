package com.whoyao.utils;

/** 时间处理工具类 */
public class TimeUtils {
	public static String getTime(String giveTime) {
		String giveTimeOperation = giveTime.substring(0, 10);
		String currentTime = CalendarUtils.getCurrentTime().substring(0, 10);
		String currentYear = CalendarUtils.getCurrentTime().substring(0, 4);
		String giveYear = giveTime.substring(0, 4);
		if (currentYear.equals(giveYear)) {

			if (giveTimeOperation.equals(currentTime)) {
				return CalendarUtils.formatHM(giveTime);
			} else {
				return giveTime.substring(5, 16).replace("-", ".");
			}
		}else {
			return giveTime.substring(0,11).replace("-", ".");
		}
	}
	public static String getDuringTime(int giveTime){
		String result = null;
		switch (giveTime) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			result ="0"+giveTime+":00"+"-"+"0"+(giveTime+1)+":00";
			break;
		case 9:
			result ="0"+giveTime+":00"+"-"+"10:00";
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
			result = giveTime+":00"+(giveTime+1)+":00";
			break;
		}
		return result;
		
	}
}
