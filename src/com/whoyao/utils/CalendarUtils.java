package com.whoyao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;

/**
 * 日期相关工具类
 * 
 * @author HYH create at：2013-3-28 上午09:06:30
 */
public class CalendarUtils {
	/** 服务器的日期格式 */
	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat formatter0 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	public static final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy年M月d日 HH:mm");
	public static final SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy年M月d日");
	/** 只有小时和分钟 */
	public static final SimpleDateFormat formatter4 = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat formatter6 = new SimpleDateFormat("MM.dd（EEEE）");
	// public static final SimpleDateFormat formatter_en = new
	// SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	public static final SimpleDateFormat formatter_en = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
	private static int[] days28;
	private static int[] days29;
	private static int[] days30;
	private static int[] days31;
	private static String[] halfHours;
	private static String[] minutes;
	private static Date date = new Date();
	private static Calendar cal = Calendar.getInstance();

	public static Calendar getCalendar() {
		cal.setTimeInMillis(AppContext.serviceTimeMillis());
		return cal;
	}

	public static Calendar getNewCalendar() {
		Calendar newCal = Calendar.getInstance();
		newCal.setTimeInMillis(AppContext.serviceTimeMillis());
		return newCal;
	}

	/** 将服务端时间字符串转化为MM.dd HH:mm格式 */
	public static String formatMDHM(String timeStr) {
		return timeStr.substring(5, 16).replace("-", ".");
	}

	/** 将服务端时间字符串转化为YYYY.MM.dd HH:mm格式(UI统一格式) */
	public static String formatYMDHM(String timeStr) {
		return timeStr.substring(0, 16).replace("-", ".");
	}

	/** 将服务端时间字符串转化为YYYY.MM.dd格式 */
	public static String formatYMD(String timeStr) {
		if(timeStr == null){
			return "";
		}
		return timeStr.substring(0, 10).replace("-", ".");
	}
	/** 日期转化为YYYY.MM.dd格式 */
	public static String formatYMD(Date date) {
		return formatter5.format(date);
	}

	/** 将服务端时间字符串转化为YYYY年MM月dd HH:mm格式 */
	public static String formatChinese(String timeStr) {
		long mills = parseDate(timeStr);
		if (-1 == mills) {
			return null;
		}
		Date time = new Date(mills);
		return formatter1.format(time);
	}

	/** 将服务端 时间字符串转化为HH:mm格式 */
	public static String formatHM(String timeStr) {
		if(TextUtils.isEmpty(timeStr)){
			return "";
		}
		return timeStr.substring(11, 16);

	}

	// Mon Oct 14 15:28:03 GMT+08:00 2013
	public static String farmatEn() {
		Locale default1 = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		date.setTime(AppContext.serviceTimeMillis());
		String format = date.toString();
		Locale.setDefault(default1);
		return format;
	}

	/**
	 * 通过开始&结束时间判断活动在今天、明天、已结束、进行中...
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static String parseDateState(String beginTime, String endTime) throws ParseException {
		String ret = "";

		Calendar now = getNewCalendar();
		Calendar begin = getNewCalendar();
		Calendar end = getNewCalendar();

		Date beginDate = formatter.parse(beginTime);
		begin.setTime(beginDate);
		Date endDate = formatter.parse(endTime);
		end.setTime(endDate);

		if (now.after(end)) {
			ret = "已经结束";
		} else if (now.before(end)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate1 = format.parse(beginTime);
			Date dateNow = new Date();
			int daysBetween = (int) Math.ceil((double) (beginDate1.getTime() - dateNow.getTime())
					/ (1000 * 60 * 60 * 24));
			if (daysBetween == 0) {
				if (now.before(end) && now.after(begin)) {
					ret = "活动进行中";
				} else {
					ret = "今天 " + beginTime.substring(11, 16);
				}
			} else if (daysBetween == 1) {
				ret = "明天" + beginTime.substring(11, 16);
			} else if (daysBetween == 2) {
				L.i(Const.AppName, "differ: " + now.compareTo(begin));
				ret = "2天后";
			} else if (daysBetween == 3) {
				L.i(Const.AppName, "differ: " + now.compareTo(begin));
				ret = "3天后";
			} else if (daysBetween == 4) {
				L.i(Const.AppName, "differ: " + now.compareTo(begin));
				ret = "4天后";
			} else if (daysBetween == 5) {
				L.i(Const.AppName, "differ: " + now.compareTo(begin));
				ret = "5天后";
			} else if (daysBetween == 6) {
				L.i(Const.AppName, "differ: " + now.compareTo(begin));
				ret = "6天后";
			} else if (daysBetween >= 7) {
				ret = beginTime.substring(5, 10) + "开始";
			}
		}
		return ret;
	}

	/**
	 * format决定"今天"返回格式: 0=HH:mm, 1=HH:mm:ss, 2=yyyy-MM-dd,3 =yyyy.MM.dd
	 * HH:mm:ss, other=今天
	 * 
	 * @param createTime
	 * @param format
	 * @return
	 */
	public static String parseDate(String createTime, int format) {
		try {
			// LogUtil.i("Utils", "" + format);
			// LogUtil.i("Utils", "createTime" + createTime);
			String ret = null;
			String[] formats = { "HH:mm", "HH:mm:ss", "yyyy-MM-dd" };
			SimpleDateFormat sdf = formatter;
			Date createDate = sdf.parse(createTime);
			long create = createDate.getTime();
			Calendar now = getNewCalendar();
			// 小时 + 分钟 + 秒 = 今天已过去的时间
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now
					.get(Calendar.SECOND));
			long ms_now = now.getTimeInMillis();
			// LogUtil.i("Utils", "ms_now:  " + ms_now);
			if (ms_now - create <= ms) {
				// 如果是今天,则返回时间 HH:mm:ss
				if (0 <= format && 3 > format) {
					// LogUtil.i("Utils", "format:  " + format);
					sdf = new SimpleDateFormat(formats[format]);
					// LogUtil.i("Utils", "format:  " + format);
					ret = sdf.format(createDate);
				} else if (3 == format) {
					ret = createTime;
				} else {
					ret = "今天" + createTime.substring(10, 16);
					// LogUtil.i("Utils", "ret1= " + ret);
				}
			} else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
				ret = "昨天" + createTime.substring(10, 16);
			} else if (ms_now - create < (ms + 24 * 3600 * 1000 * 2)) {
				ret = "前天" + createTime.substring(10, 16);
			} else {
				// ret = "更早";
				ret = createTime.substring(5, 16);
			}
			// LogUtil.i("Utils", "ret= " + ret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * format决定"今天"返回格式: 0=HH:mm, 1=HH:mm:ss, 2=yyyy-MM-dd,3 =yyyy.MM.dd
	 * HH:mm:ss, other=今天
	 * 
	 * @param createTime
	 * @param format
	 * @return
	 */
	@Deprecated
	public static String parseMsgTime(String createTime) {
		try {
			String ret = null;
			Date createDate = formatter.parse(createTime);
			long create = createDate.getTime();
			Calendar now = getNewCalendar();
			// 小时 + 分钟 + 秒 = 今天已过去的时间
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now
					.get(Calendar.SECOND));
			long ms_now = now.getTimeInMillis();
			if (ms_now - create <= ms) {
				// 如果是今天,则返回时间 HH:mm:ss
				ret = "今天" + createTime.substring(10, 19);
				// LogUtil.i("Utils", "ret1= " + ret);

			} else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
				ret = "昨天" + createTime.substring(10, 19);
			} else if (ms_now - create < (ms + 24 * 3600 * 1000 * 2)) {
				ret = "前天" + createTime.substring(10, 19);
			} else {// ret = "更早";
				ret = createTime.substring(5, 16);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 当前时间 格式为 yyyy.MM.dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return formatter.format(getCalendar().getTime());
	}

	/**
	 * 根据日期字符串,解析出毫秒值
	 * 
	 * @param dateStr
	 * @return
	 */
	public static long parseDate(String dateStr) {

		try {
			return formatter.parse(dateStr).getTime();
		} catch (ParseException e) {
			AppException.handle(e);
		}
		try {
			return formatter2.parse(dateStr).getTime();
		} catch (ParseException e) {
			AppException.handle(e);
		}
		return -1;
	}

	/** 将毫秒值格式化为yyyy年MM月dd日 */
	public static String formatDateYMD(long milliseconds) {
		Date time = new Date(milliseconds);
		return formatter3.format(time);
	}

	/** 将毫秒值格式化为 服务端接受的格式 */
	public static String formatServer(long milliseconds) {
		Date time = getDate();
		time.setTime(milliseconds);
		return formatter.format(time);
	}

	/** 将毫秒值格式化为 yyyy.mm.dd hh:mm(UI统一格式) */
	public static String formatDate(long milliseconds) {
		Date time = getDate();
		time.setTime(milliseconds);
		return formatter0.format(time);
	}

	/**
	 * 判断是否是同一天
	 * 
	 * @param timeStr
	 * @return
	 */
	public static boolean isToday(String timeStr) {
		try {
			Date time = formatter.parse(timeStr);
			return isToday(time);
		} catch (ParseException e) {
			AppException.handle(e);
		}
		return false;
	}

	/**
	 * 判断是否是同一天
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static boolean isToday(long timeMillis) {
		Date time = new Date(timeMillis);
		return isToday(time);
	}

	/**
	 * 判断是否是同一天
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isToday(Date time) {
		Calendar svrCal = getCalendar();
		Calendar comCal = getNewCalendar();
		comCal.setTime(time);
		if (svrCal.get(Calendar.YEAR) != comCal.get(Calendar.YEAR)) {
			return false;
		}
		if (svrCal.get(Calendar.MONTH) != comCal.get(Calendar.MONTH)) {
			return false;
		}
		if (svrCal.get(Calendar.DAY_OF_MONTH) != comCal.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是同一年
	 * 
	 * @param timeStr
	 * @return
	 */
	public static boolean isThisYear(String timeStr) {
		try {
			Date time = formatter.parse(timeStr);
			return isThisYear(time);
		} catch (ParseException e) {
			AppException.handle(e);
		}
		return false;
	}

	/**
	 * 判断是否是同一年
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static boolean isThisYear(long timeMillis) {
		Date time = new Date(timeMillis);
		return isThisYear(time);
	}

	/**
	 * 判断是否是同一年
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isThisYear(Date time) {
		Calendar svrCal = getCalendar();
		Calendar comCal = getNewCalendar();
		comCal.setTime(time);
		if (svrCal.get(Calendar.YEAR) != comCal.get(Calendar.YEAR)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断两个世界是否在同一时间段 -1 未定义 0 同一毫秒 1 同一秒 2 同一分钟 3 同一小时 4 同一天 5 同一月 6 同一年 7 同一世纪
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int compareDate(Calendar time1, Calendar time2) {
		int result = -1;
		if (time1.getTimeInMillis() == time2.getTimeInMillis()) {
			result = 0;
		} else if (time1.get(Calendar.YEAR) == time2.get(Calendar.YEAR)) {
			result = 6;
			if (time1.get(Calendar.MONTH) == time2.get(Calendar.MONDAY)) {
				result = 5;
				if (time1.get(Calendar.DAY_OF_MONTH) == time2.get(Calendar.DAY_OF_MONTH)) {
					result = 4;
					if (time1.get(Calendar.HOUR_OF_DAY) == time2.get(Calendar.HOUR_OF_DAY)) {
						result = 3;
						if (time1.get(Calendar.MINUTE) == time2.get(Calendar.MINUTE)) {
							result = 2;
							if (time1.get(Calendar.SECOND) == time2.get(Calendar.SECOND)) {
								result = 1;
							}
						}
					}
				}
			}
		} else if (time1.get(Calendar.YEAR) / 100 == time2.get(Calendar.YEAR) / 100) {
			result = 7;
		}
		return result;
	}

	public static int[] getDaysArray(int year, int month) {
		return getDaysArray(getDaysOfMonth(year, month));
	}

	public static int[] getDaysArray(int daysOfMonth) {
		switch (daysOfMonth) {
		case 28:// 非闰年
			return getDays28();
		case 29:// 闰年
			return getDays29();
		case 30:
			return getDays30();
		case 31:
			return getDays31();
		}
		return null;
	}

	/**
	 * 判断一个月有多少天,月份从1~~12.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (0 == year % 4) {
				return 29;// 闰年
			} else {
				return 28;// 非闰年
			}
		default:
			return 0;
		}
	}

	public static int[] getDays28() {
		if (null == days28) {
			days28 = new int[28];
			for (int i = 0; i < 28; i++) {
				days28[i] = i + 1;
			}
		}
		return days28;
	}

	public static int[] getDays29() {
		if (null == days29) {
			days29 = new int[29];
			for (int i = 0; i < 29; i++) {
				days29[i] = i + 1;
			}
		}
		return days29;
	}

	public static int[] getDays30() {
		if (null == days30) {
			days30 = new int[30];
			for (int i = 0; i < 30; i++) {
				days30[i] = i + 1;
			}
		}
		return days30;
	}

	public static int[] getDays31() {
		if (null == days31) {
			days31 = new int[31];
			for (int i = 0; i < 31; i++) {
				days31[i] = i + 1;
			}
		}
		return days31;
	}

	/** 获取一小时内的分钟:00-59 */
	public static String[] getMinutes() {
		if (null == minutes) {
			minutes = new String[60];
			for (int i = 0; i < 10; i++) {
				minutes[i] = "0" + i;
			}
			for (int i = 10; i < 60; i++) {
				minutes[i] = "" + i;
			}
		}
		return minutes;
	}

	public static String[] getHalfHourArray() {
		if (null == halfHours) {
			halfHours = new String[48];
			for (int i = 0; i < 24; i++) {
				halfHours[2 * i] = i + ":00";
				halfHours[2 * i + 1] = i + ":30";
			}
		}
		return halfHours;
	}

	public static String[] getEndHalfHourArray(int endHour, int endMinute) {
		String[] array = null;
		int length = endHour * 2;
		if (30 <= endMinute) {
			++length;
		}
		array = new String[length];
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) {
				array[i] = i / 2 + ":00";
			} else {
				array[i] = i / 2 + ":30";
			}
		}
		return array;
	}

	public static String[] getBeginHalfHourArray(int beginHour, int beginMinute) {
		String[] array = null;
		int length = (24 - beginHour) * 2;
		if (30 < beginMinute) {
			beginHour++;
			length -= 2;
			array = new String[length];
			for (int i = 0; beginHour + i < 24; i++) {
				array[2 * i] = (beginHour + i) + ":00";
				array[2 * i + 1] = (beginHour + i) + ":30";
			}
		} else {
			length--;
			array = new String[length];
			for (int i = 0; i < length; i++) {
				if (i % 2 == 0) {
					array[i] = beginHour + i / 2 + ":30";
				} else {
					array[i] = beginHour + 1 + i / 2 + ":00";
				}
			}
		}
		return array;
	}

	/**
	 * 根据生日获取星座
	 * 
	 * @param birthdayStr
	 * @return
	 */
	public static String getConstellation(String birthdayStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date birth = formatter.parse(birthdayStr);
			return getConstellation(birth.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 根据生日获取星座
	 * 
	 * @param birthdayMills
	 * @return
	 */
	public static String getConstellation(long birthdayMills) {

		Calendar birthdayCalendar = getNewCalendar();
		birthdayCalendar.setTimeInMillis(birthdayMills);
		int month = birthdayCalendar.get(Calendar.MONTH) + 1;
		int day = birthdayCalendar.get(Calendar.DATE);

		return getConstellation(month, day);
	}

	/**
	 * 通过日期判断星座
	 * 
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @return
	 */
	public static String getConstellation(int monthOfYear, int dayOfMonth) {
		int total = monthOfYear * 31 + dayOfMonth;
		String chinaNm = "";
		if (total >= 32 && total <= 50 || total >= 394 && total <= 403) {
			chinaNm = "魔羯座";
		} else if (total >= 51 && total <= 80) {
			chinaNm = "水瓶座";
		} else if (total >= 81 && total <= 113) {
			chinaNm = "双鱼座";
		} else if (total >= 114 && total <= 144) {
			chinaNm = "白羊座";
		} else if (total >= 145 && total <= 175) {
			chinaNm = "金牛座";
		} else if (total >= 176 && total <= 207) {
			chinaNm = "双子座";
		} else if (total >= 208 && total <= 239) {
			chinaNm = "巨蟹座";
		} else if (total >= 240 && total <= 270) {
			chinaNm = "狮子座";
		} else if (total >= 271 && total <= 301) {
			chinaNm = "处女座";
		} else if (total >= 302 && total <= 333) {
			chinaNm = "天秤座";
		} else if (total >= 334 && total <= 362) {
			chinaNm = "天蝎座";
		} else if (total >= 363 && total <= 393) {
			chinaNm = "射手座";
		}
		return chinaNm;
	}

	/**
	 * 获取日期 格式为 yy年某某月
	 * 
	 * @param time
	 * @return
	 */
	public static String getYearMonth(String time) {
		String yearMonth = null;
		if (time != null && time.length() > 0) {
			Date date = null;
			try {
				date = formatter.parse(time);
				Calendar calendar = getNewCalendar();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				String m = "";
				switch (month) {
				case 1:
					m = "一";
					break;

				case 2:
					m = "二";
					break;

				case 3:
					m = "三";
					break;

				case 4:
					m = "四";
					break;

				case 5:
					m = "五";
					break;

				case 6:
					m = "六";
					break;

				case 7:
					m = "七";
					break;

				case 8:
					m = "八";
					break;

				case 9:
					m = "九";
					break;

				case 10:
					m = "十";
					break;

				case 11:
					m = "十一";
					break;

				case 12:
					m = "十二";
					break;
				}
				yearMonth = year + "年" + m + "月";
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		return yearMonth;
	}

	/**
	 * 根据出生日期,计算生日
	 * 
	 * @param birthdayStr
	 * @return
	 */
	public static int getAge(String birthdayStr) {

		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date birthday = null;
		try {
			birthday = formatter5.parse(birthdayStr);
			return getAge(birthday.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据出生日期,计算生日
	 * 
	 * @param birthdayMill
	 * @return
	 */
	public static int getAge(long birthdayMill) {
		int startYear = 1970;
		Calendar cal = getNewCalendar();
		long ageMill = cal.getTimeInMillis() - birthdayMill;
		cal.setTimeInMillis(ageMill);
		int ageYear = cal.get(Calendar.YEAR) - startYear;
		if (0 > ageYear) {
			ageYear = 0;
		}
		return ageYear;
	}
	/**
	 * 
	 * 
	 * @param birthdayMill
	 * @return
	 */
	public static String[] getSevenDays() {
		String[] days = new String[7];
		days[0] = "今天";
		Calendar cal = getNewCalendar();
		for(int i = 1 ; i<7 ; i++){
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			days[i] = formatter6.format(cal.getTime());
		}
		return days;
	}

	/** 将分钟数转换为 小时:分钟 */
	public static String getTimeFormMinute(int minutes) {
		int hour = minutes / 60;
		int minute = minutes % 60;
		// if(0 == hour && 0 == minute){
		// return "";
		// }
		return hour + ":" + minute;
	}

	private static Date getDate() {
		if (null == date) {
			date = new Date();
		}
		return date;
	}
}
