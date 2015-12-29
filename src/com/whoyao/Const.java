package com.whoyao;

import java.io.File;

import com.whoyao.utils.Utils;

import android.os.Build;
import android.os.Environment;

/**
 * 常量
 * 
 * @author hyh 2013-6-26 上午9:10:27
 */
public class Const {
	// 用于debug测试的log tag
	public static final String ZL = "ZzL";
	public static final String AppName = "互邀";

	public static final int STATUS_TAMPER = -1; // 被篡改
	public static final int STATUS_DEBUG = 1; // 正在开发：{日志:打印, 捕获异常:打印、保存,
												// 未捕获异常:打印、保存}
	public static final int STATUS_RELEASE = 2; // 正式发布：{日志:不打印不保存, 捕获异常:不打印不保存,
												// 未捕获异常:不打印但保存}
	/**
	 * Socket连接状态异常的原因
	 */
	public static final String REASON_READ_EXCEPTION = "read_exception";
	public static final String REASON_NORMA_CONNECTION = "normal_connection";
	public static final String REASON_NET_ERROR = "net_error";
	public static final String REASON_KEY_OVERDUE = "key_overdue";
	public static final String REASON_KICK_OUT = "kick_out";
	public static final String REASON_OVER_TIME = "over_time";
	public static final String REASON_LOGOUT = "logout";

	/** App状态 */
	// public static final int AppSatus = STATUS_TESTING;

	/** 用于2.1.3及以后版本 */
	public static final String MapKey_Debug = "27c63ccfced48f8a435a273c5b533b72",
			MapKey_Release = "7330289457bcebe623aa2c0434fe7f87";
	/** 用于2.1.2及以前版本 */
	public static final String MapKey = "FD0B4D7BFD32C27ABFF7276C73726A795F3AFA4C";

	/** 支付宝身份ID */
	public static final String ALIPAY_PARTNER = "2088501069319626";
	/** 支付宝收款账号 */
	public static final String ALIPAY_SELLER = "wetry@reuniontech.com";
	/** MD5 Key */
	public static final String ALIPAY_MD5 = "b43pgo2v28grdtn0rype5go31c17bz8n";
	/** MD5 Key */
	public static final String ALIPAY_SUBJECT = "场馆预订";
	/** 支付宝公钥 */
	public static final String ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	/** 第三方登录时QQ的AppKey */
	public static final String QQ_APP_KEY = "101040872";
	/** 第三方登录时sina的AppKey */
	public static final String SINA_APP_KEY = "2955216320"; // 应用的APP_KEY
	public static final String SINA_REDIRECT_URL = "http://www.whoyao.com/Login/ReturnValue?type=4";// 应用的回调页
	public static final String SCOPE = // 应用申请的高级权限
	"email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final int ALIYPE_RQF_PAY = 12345;
	public static final int ALIYPE_RQF__LOGIN = 23456;

	public static final String DB_CACHE = "WhoYao.db";
	public static final String DB_AREAE = "Areae.db";
	public static final String DB_CITIES = "Cities.db";
	public static final String CUSTOM_SERVICE_PHONE = "400-666-0626";
	public static final String CUSTOM_SERVICE_WEIBO = "http://e.weibo.com/whoyao";
	public static final String PATH_IMAGE_TEMP = "Images";
	public static final String PATH_IMAGE_SHOT = "Shots";
	public static final String PATH_IMAGE_CAMERA = "Camera";

	public static final int PAGE_SIZE = 15;
	public static final int PAGE_DEFAULT_INDEX = 1;

	public static final int DB_SIZE_AREAE = 200704;
	public static final int DB_SIZE_CITIES = 224256;

	public static final int NetTimeout = 30000;
	public static final String USER_AGENT = "WhoyaoApp/"
			+ AppContext.context.getString(R.string.version_name) + "/"
			+ Utils.getApkVersionCode(AppContext.context) + ";Android/"
			+ Build.VERSION.RELEASE + ";" + Build.MANUFACTURER + "/"
			+ Build.MODEL;

	public static final int CodeLenght_SMS = 5;
	public static final int CodeLength_Image = 4;
	/** 活动人数限制 */
	public static final int Event_minjioner_normall = 2,
			Event_maxjioner_normall = 200, Event_minjioner_immediately = 2,
			Event_maxjioner_immediately = 20;
	/** 推送相关 */
	/** 私信广播 */
	public static final String CHAT_MSG = "com.whoyao.service.chat_msg";
	public static final String CHAT_MSG_WIOUT_NOTIFY = "com.whoyao.service.chat_msg_unnotify";
	// public static final String PATH_LOG = "Log";
	// public static final String DIR_SDcard =
	// Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhoYao/";

	/* SD卡根路径 */
	// public static final File SDCARD_ROOT_DIR =
	// Environment.getExternalStorageDirectory();
	/** SD卡中自己拍照照片的存储路径 */
	public static final File SD_CAMERA_DIR = new File(
			Environment.getExternalStorageDirectory(), "DCIM/Camera");
	/** SD卡中用于缓存路径 */
	public static final File SD_CACHE_DIR = AppContext.context
			.getExternalCacheDir();
	public static final File CACHE_DIR = AppContext.context.getCacheDir();
	public static final File SD_DOWNLOADS_DIR = AppContext.context
			.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
	public static final File ErrorLog_DIR = AppContext.context.getFilesDir();
	public static final File SD_ErrorLog_DIR = AppContext.context
			.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
			.getParentFile();

	// public static final String SD_ErrorLog_DIR =
	// "/storage/emulated/0/Android/data/com.whoyao/files";
	/** SD卡中自己拍照照片截图的存储路径 */

	/**
	 * 配置文件相关常量
	 * 
	 * @author hyh 2013-6-26 上午9:12:07
	 */
	public static class Config {
		public static final String IS_OVERDUE = "isoverdue";
		public static final String PUSH_KEY = "pushkey";
		public static final String XML_NAME = "WhoYao";
		public static final String USER_XML_NAME = "User-";
		public static final String Account = "account";
		public static final String Picurl = "picurl";
		public static final String Openid = "openid";
		public static final String Accesstoken = "accesstoken";
		public static final String PassWord = "password";
		/** 是否是第一次使用客户端,决定是否显示引导页 */
		public static final String isFirstTime = "isFirstTime";
		public static final String ignoreVersion = "ignore_version";
		public static final String UpdateInfo = "upadat_info";

		public static final String Setting_Sound = "SoundSetting";
		public static final String Setting_Vibrato = "VibratoSetting";
		public static final String Setting_Message = "MessageSetting";
		public static final boolean Setting_Sound_Default = true;
		public static final boolean Setting_Vibrato_Default = false;
		public static final boolean Setting_Message_Default = true;
		public static final String ShowMyPoint = "ShowMyPoint";
		public static final String ShowMyEvent = "ShowMyEvent";
		/** 是否点击下次不再提醒注销 */
		public static final String isLogoutChecked = "isLogoutChecked";
		public static final String UserInfo = "CurrentUserInfo";
		public static final String TimeDeviation = "TIME_DEVIATION";
		public static final String User_Push_Key = "User_Key";
	}

	/**
	 * Intent传递数据是的 Extra Name
	 * 
	 * @author HYH 2013-5-8 下午12:52:17
	 */
	public static class Extra {

		public static final String isAvailable = "isAvailable";
		public static final String PHONE_NUM = "phoneNumber";
		public static final String HAS_TO_CUT = "hasToCut";
		public static final String IMAGE_PATH = "imagePath";

		public static final String ProvinceCode = "provinceCode";
		public static final String ProvinceName = "provinceName";
		public static final String CityCode = "cityCode";
		public static final String CityName = "cityName";
		public static final String DistrictCode = "districtCode";
		public static final String DistrictName = "districtName";
		public static final String AddrModel = "addr";

		public static final String Position = "position";
		public static final String SelectedID = "selected_id";
		public static final String SelectedItem = "selected_item";
		public static final String SelectedItemStr = "selected_item_string";
		public static final String Data = "data";
		public static final String Button0_Text = "button_0_text";
		public static final String Button1_Text = "button_1_text";
		public static final String ButtonCancel_Text = "button_cancle_text";

		public static final String MyContent = "MyContent";
		public static final String DefaultTimeMillis = "default_time_millis";
		public static final String SelectedTime = "selected_time_millis";
		public static final String SelectedTimeStr = "selected_time_String";
		public static final String ArrayRes = "ArrayRes";
		public static final String ArrayStr = "StringArray";
		public static final String Time_Earliest = "earliest_time";
		public static final String Time_Latest = "latest_time";
		public static final String Time_EarliestStr = "earliest_time_string";
		public static final String Time_LatestStr = "latest_time_string";
		public static final String Feedback = "content";
		public static final String LatitudeE6 = "LatitudeE6";
		public static final String LongitudeE6 = "LongitudeE6";
		public static final String Title = "Title";
		public static final String Snippet = "Snippet";
		public static final String Description = "description";
		public static final String Photos = "photos";
		public static final String Max = "max";
		public static final String Min = "min";
		public static final String Sex = "sex";
		public static final String ASPECT_X = "aspectX";
		public static final String ASPECT_Y = "aspectY";
		public static final String State = "state";
		public static final String VerifyState = "VerifyState";
		public static final String Search_Keyword = "keyword";
		public static final String Search_List = "searchList";
		// 我的消息数量
		public static int MESSAGE_NUM = 0;
		// 我的邀请数量
		public static int INVITE_NUM = 0;

	}

	/**
	 * 加密相关常量
	 * 
	 * @author hyh 2013-6-26 上午9:11:53
	 */
	public static class Encryption {
		public final static String DES_KEY = "9b2648fcdfbad80f";
	}

	/**
	 * 网络请求的KEY
	 * 
	 * @author hyh creat_at：2013-8-6-上午9:39:52
	 */
	public static class KEY {

		public static final String Account = "account";
		public static final String Password = "userpassword";
		public static final String ResetPassword = "password";
		public static final String NewPassword = "passWord";
		public static final String User_ID = "userid";
		public static final String FriendID = "frienduserid";
		public static final String Type = "type";
		public static final String Image = "image";
		public static final String Width = "width";
		public static final String Height = "height";
		public static final String PhotoSubject = "photosubject";
		public static final String PhotoDescription = "photoDescription";
		public static final String Email = "useremail";
		public static final String Phone = "userphone";
		public static final String VerifyCode = "verifycode";
		public static final String TagIDs = "tagid";
		public static final String TagNames = "tagname";
		public static final String Index = "index";
		public static final String Size = "size";
		public static final String RealName = "userrelname";
		public static final String EventID = "activityid";
		public static final String VenueID = "venueid";
		public static final String PHOTO_SUBJECT = "photosubject";
		public static final String Result = "result";
		public static final String Key = "key";
		public static final String Value = "value";
		public static final String Loginuserid = "loginuserid"; // 接口返回ID
		public static final String Loginid = "loginid"; // 接口返回ID
		public static final String Picurl = "picurl"; // 头像地址
		public static final String Accesstoken = "accesstoken"; // Token值
		public static final String Usernickname = "usernickname"; // 用户昵称
		public static final String Logintype = "logintype"; // 登陆类型
		public static final String Userprovince = "userprovince";
		public static final String Usercity = "usercity";
		public static final String Userdistrict = "userdistrict";
		public static final String Usersex = "usersex";
		public static final String Userbirthday = "userbirthday";
	}

	public static class Type {
		/** 用户详细信息 */
		public static final String Detial_my = "1", Detial_other = "2";
		/** 短信验证码 */
		public static final String VCode_Register = "0", VCode_RetPwd = "1",
				VCode_Verify = "2";
		/** 验证邮箱 */
		public static final String Email_RetPwd = "0", Email_Verify = "1";
		/** 活动类别（是否即时活动） */
		public static final int Category_Immediately = 1, Category_Normal = 2;
		/** 我的活动（参加的 or 发起的） */
		public static final int Creater = 0, Joiner = 1;
		/** 朋友操作(添加 or 删除) */
		public static final int User_Add = 1, User_Del = 2;
	}

	public static class State {
		/** 实名认证状态 */
		public static final int Honesty_none = 0, Honesty_ok = 1,
				Honesty_auditing = 2, Honesty_reject = 3;
		/** 手机认证状态 */
		public static final int Mobile_none = 0, Mobile_unvalid = 1,
				Mobile_ok = 2;
		/** 邮箱认证状态 */
		public static final int Email_none = 0, Email_unvalid = 1,
				Email_ok = 2;
		public static final int Verify_EventCreater = 1,
				Verify_EventJoiner = 2, Verify_InviteCreater = 3;
		/** 按键选择界面返回值 */
		public static final int Button_0 = 0, Button_1 = 1, Selected_enter = 2,
				Selected_cancle = -1;
		/** 按键选择界面返回值 */
		public static final int Sex_None = 0, Sex_Man = 1, Sex_Woman = 2;
		/** 搜索 条件类型:关键字,坐标 */
		public static final int Search_Str = 0, Search_Loc = 1;
		/** 活动搜索 条件类型:热门区域,热门标签 */
		public static final int Search_Area = 2, Search_Type = 3;
		/** 朋友搜索 条件类型:性别,年龄 */
		public static final int Search_Sex = 4, Search_Age = 5;
		/** 朋友操作 条件类型:无,同意,拒绝,忽略 */
		public static final int None = 0, Ignore = 2, Agree = 1, DisAgree = 3;

	}

}
