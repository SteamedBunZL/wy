package com.whoyao.utils;

import java.util.regex.Pattern;

/**
 * 格式检查工具
 * @author hyh 
 * creat_at：2013-8-9-下午7:33:24
 */
public class FormatUtils {
	
	public static final Pattern Chinese_Pattern = Pattern.compile("^[\u4E00-\u9FFF]+$");
	public static final Pattern Password_Pattern = Pattern.compile("^(?!\\D+$)(?![^a-zA-Z]+$).{6,16}$");
	public static final Pattern NickName_Pattern = Pattern.compile("^[0-9a-zA-Z_-—\u0391-\uFFE5\u4e00-\u9fa5]+$");
	public static final Pattern Phone_Pattern = Pattern.compile("^((14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-2,5-9]))\\d{8}$");
	public static final Pattern Email_Pattern = Pattern.compile("^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+.)+[a-zA-Z0-9]{2,4}$");
//	public static final Pattern Email_Pattern = Pattern.compile("^([a-zA-Z0-9]+[\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[\\_|\\.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,3}$");
	public static final Pattern IDCard15_Pattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");//18位身份证
	public static final Pattern IDCard18_Pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");//18位身份证
	public static final Pattern URL_Pattern = Pattern.compile("(http|https|ftp|Http|Https|Ftp|HTTP|HTTPS|FTP):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
	public static final Pattern Http_or_Https_URL_Pattern = Pattern.compile("(http|https|Http|Https|HTTP|HTTPS):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
	public static final Pattern VenueURL_Pattern = Pattern.compile("^http\\:\\/\\/(d|www|r|p)\\.whoyao\\.com\\/venue\\/(\\d+).*");
	public static final Pattern EventURL_Pattern = Pattern.compile("^http\\:\\/\\/(activity|activity\\.r|activity\\.p|activity\\.d)\\.whoyao\\.com\\/(\\d+).*");

	//(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?
	/**
	 * 正则表达式匹配手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		return Phone_Pattern.matcher(phoneNumber).matches();
	}
	
	public static boolean isNickname(String nickname){
		return NickName_Pattern.matcher(nickname).matches();
	}
	/**
	 * 正则表达式匹配邮箱地址
	 * 
	 * @param emailAddr
	 * @return
	 */
	public static boolean isEmailAddr(String emailAddr) {
		return Email_Pattern.matcher(emailAddr).matches();
	}
	/**
	 * 正则表达式匹配http或https地址
	 * 
	 * @param emailAddr
	 * @return
	 */
	public static boolean isHttpAddr(String httpAddr) {
		return Http_or_Https_URL_Pattern.matcher(httpAddr).matches();
	}
	/**
	 * 正则表达式匹配汉字
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isChineseOnly(String string) {
		return Chinese_Pattern.matcher(string).matches();
	}
	/**
	 * 正则表达式匹配汉字
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isIDCard(String string) {
		return IDCard18_Pattern.matcher(string).matches();
	}
	
	/**
	 * 正则表达式检查密码复杂度
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isPwdComplex(String password) {
		return Password_Pattern.matcher(password).matches();
	}
	/**
	 * 判断字符串的长度,每个汉字为1,每个字母为1
	 * 
	 * @param string
	 * @return
	 */
	public static int getStringLengthEn1Cn1(String str) {
		return str.length();
	}
	/**
	 * 判断字符串的长度,每个汉字为1,每个字母为0.5
	 * 
	 * @param string
	 * @return
	 */
	public static int getStringLengthEn0_5Cn1(String str) {
		double num = 0;
		String len;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			len = Integer.toBinaryString(c[i]);
			if (len.length() > 8) {
				num++;
			} else {
				num += 0.5;
			}
		}
		if(0 == num%1){
			return (int)num;
		}else {
			return (int)num + 1;
		}
	}
	/**
	 * 判断字符串的长度,每个汉字为1,每个字母为1
	 * 
	 * @param string
	 * @return
	 */
	public static int getStringLengthEn1Cn2(String str) {
		double num = 0;
		String len;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			len = Integer.toBinaryString(c[i]);
			if (len.length() > 8) {
				num++;
			} else {
				num += 1;
			}
		}
		if(0 == num%1){
			return (int)num;
		}else {
			return (int)num + 2;
		}
	}
	
	public static boolean isVenueUrl(String url){
		return VenueURL_Pattern.matcher(url).matches();
	}
	public static boolean isEventUrl(String url){
		return EventURL_Pattern.matcher(url).matches();
	}
}
