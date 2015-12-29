package com.whoyao;

/**
 * WebService接口地址
 * 
 * @author hyh
 */
public class WebApi {
	/** 正式地址 */
	private static final String Api = "http://api.whoyao.com/";
	private static final String Code = "http://whoyao.com/checkcode.ashx?key=android_remark";
	private static final String Image = "http://image.whoyao.net/";
	private static final String Alipay = "http://www.whoyao.com/Venue/AliPay/Mobile_Start?otherpay=";
	/** 即时通讯测试接口地址 */
	public static final String Ims = "ims.d.whoyao.com";
	public static final String Ims1 = "ims1.d.whoyao.com";
	public static final String Ims2 = "ims2.d.whoyao.com";
	public static final String IMSLI = "10.10.1.246";
	public static final String IMSHU = "10.10.1.137";
	public static final int Port = 60090;
	public static final int Port1 = 80;
	public static final int Port2 = 25;
	/** 测试 接口地址 */
	private static final String Api_d = "http://api.d.whoyao.com/";
	private static final String Api_r = "http://api.r.whoyao.com/";
	private static final String Api_h = "http://10.10.1.137:40008/";
	private static final String Api_c = "http://10.10.1.155:82/";
	/** 测试 验证码地址 */
	private static final String Code_d = "http://d.whoyao.com/checkcode.ashx?key=android_remark";
	private static final String Code_r = "http://r.whoyao.com/checkcode.ashx?key=android_remark";
	private static final String Code_h = "http://10.10.1.137/checkcode.ashx?key=android_remark";
	// 211.100.49.56
	/** 测试 图片地址 */
	private static final String Image_d = "http://image.d.whoyao.com/";
	private static final String Image_r = "http://image.r.whoyao.com/";
	/** 支付宝网页支付地址 */
	private static final String Alipay_d = "http://d.whoyao.com/Venue/AliPay/Mobile_Start?otherpay=";
	private static final String Alipay_r = "http://r.whoyao.com/Venue/AliPay/Mobile_Start?otherpay=";

	/** 接口地址 */
	public static final String Api_Addr = Api_d;
	/** 图片地址 */
	public static final String Image_Addr = Image_d;
	/** 验证码图片 */
	public static final String Code_Addr = Code_d;
	/** 支付宝网页支付地址 */
	public static final String Alipay_Addr = Alipay_d;

	/** 图片尺寸 */
	// private static final String ImageDemen_60 = "/60x60.jpg";
	public static final String ImageDemen_100 = "/100x100.jpg";
	public static final String ImageDemen_120_90 = "/120x90.jpg";
	public static final String ImageDemen_240_180 = "/240x180.jpg";
	public static final String ImageDemen_0 = "/0x0.jpg";

	public static final class Test {
		private static final String PartName = "test/";
		public static final String TestHeader = PartName + "getheader";
	}

	public static final class Common {
		private static final String PartName = "common/";
		/** 与系统同步时间 */
		public static final String GetServerTime = PartName + "getsystemtime";
		/** 获取系统标签 */
		public static final String GetSystemTag = PartName + "getsystemtag";
		/** 测试UserAgent */
		public static final String TestUserAgent = PartName + "useragent";
	}

	/**
	 * WebService用户模块
	 * 
	 * @author hyh
	 */
	public static final class User {

		private static final String PartName = "user/";
		/** 1.客户端初始化和检查更新 */
		public static final String CheckUpdate = PartName + "mobileinfo";
		/** 2.登录 */
		public static final String Login = PartName + "login";
		/** 2.登录 */
		public static final String LoginThird = PartName + "loginapi";
		/** 2.登录 */
		public static final String CheckLoginThird = PartName + "checkloginapi";
		/** 3.注册 */
		public static final String Register = PartName + "register";
		/** 4.验证账号是否已注册 */
		public static final String CheckAccount = PartName + "checkaccount";
		/** 5.获取短信验证码 */
		public static final String SendVerifyCode = PartName + "sendverifycode";
		/** 6.校验短信验证码 */
		public static final String CheckVerifyCode = PartName
				+ "checkverifycode";
		/** 8.邮箱找回密码 */
		public static final String ResetPwdEmail = PartName + "uppwdbyemail";
		/** 9.获取用户详细信息 */
		public static final String DetailInfo = PartName + "getmyuserdetail";
		/** 10.编辑个人资料 */
		public static final String UpUserInfo = PartName + "upuserdetail";
		/** 12.修改个人头像 */
		public static final String UpUserFace = PartName + "upuserface";
		/** 13.上传个人照片 */
		public static final String AddUserPhoto = PartName + "adduserphoto";
		/** 14.删除用户照片 */
		public static final String DelUserPhoto = PartName + "deleteuserphoto";
		/** 15.实名认证 */
		public static final String UpHonestyInfo = PartName + "myhonestyinfo";
		/** 16.获取闲人预告 */
		public static final String GetFree = PartName + "getmyfree";
		/** 17.发闲人预告 */
		public static final String AddMyFree = PartName + "addmyfree";
		/** 18.修改密码 */
		public static final String ChangePassword = PartName + "uppassword";
		/** 19.标签换一换 */
		public static final String GetNextTags = PartName + "nexttags";
		/** 20.添加标签 */
		public static final String UpTags = PartName + "addtag";
		/** 21.删除标签 */
		public static final String DeleteTag = PartName + "deleteusertag";
		/** 22.获取隐私设置 */
		public static final String AddUserSafeSetting = PartName
				+ "addusersetsafe";
		/** 23.修改隐私设置 */
		public static final String UpUserSafeSetting = PartName + "usersetsafe";
		/** 24.获取隐私设置 */
		public static final String GetUserSafeSetting = PartName
				+ "getusersetsafe";
		/** 25.修改条件设置 */
		public static final String UpConditionSetting = PartName
				+ "conditionsetting";
		/** 26.获取条件设置 */
		public static final String GetConditionSetting = PartName
				+ "getconditionsetting";
		/** 27.意见反馈 */
		public static final String Feedback = PartName + "addfeedback";
		/** 28.修改时空设置 */
		public static final String UpUserSpacetime = PartName
				+ "adduserspantime";
		/** 29.手机找回密码 */
		public static final String RetrievePwdByPhone = PartName
				+ "uppwdbyuserphone";
		/** 30.获取时空设置 */
		public static final String GetUserSpacetime = PartName
				+ "getuserspacetime";
		/** 31.注销接口,仅ios */
		public static final String Logout = PartName + "logout";
		/** 32.获取实名认证信息 */
		public static final String GetHonestyInfo = PartName + "getuserhonesty";
		/** 33 删除全部标签 */
		public static final String DeleteAllTag = PartName + "deletealltag";
		/*
		 * 用户模块第2部分
		 */
		/** 33:朋友列表 */
		public static final String GetUserList = PartName + "friendlist";
		/** 34:朋友搜索 */
		public static final String Search = PartName + "searchfriend";
		/** 35:我的好友表 */
		public static final String GetFridnds = PartName + "getfriends";
		/** 36:好友管理 */
		public static final String FriendManage = PartName + "friendmanage";
		/** 37:好友请求管理 */
		public static final String FriendRequestManage = PartName
				+ "operationfriend";
		/** 38:黑名单管理 */
		public static final String BlackManage = PartName + "blackmanage";
		/** 39:收藏管理 */
		public static final String FaviconsManage = PartName + "faviconsmanage";
		/** 40:与Ta的关系 */
		public static final String Relation = PartName + "getuserrelation";
		/** 41:验证邮箱 */
		public static final String VerifyEmail = PartName + "checkemail";
		/** 42我的资金 */
		public static final String MyFund = PartName + "getusercapital";
		/** 43我的资金获取手机验证码 */
		public static final String GetPhoneCode = PartName + "phonecode";
		/** 44验证验证码 */
		public static final String CheckPhoneCode = PartName
				+ "checkpayphonecode";
		/** 45设置密码 */
		public static final String SetPassword = PartName + "setpaypassword";
		/** 46获取推送密钥 */
		public static final String GetPushKey = PartName + "registerpushuser";
	}

	/** 活动模块接口 */
	public static final class Event {
		private static final String PartName = "activity/";
		/** 1:完善人员信息 */
		public static final String ValidateCode = PartName + "validatecode";
		/** 2:活动首页的列表 */
		public static final String GetEventList = PartName + "getactivitylist";
		/** 3:活动首页，推荐活动图片 */
		public static final String GetHotEvent = PartName + "gethotactivity";
		/** 4:活动详情 */
		public static final String GetDetail = PartName + "getactivitydetail";
		/** 5:报名活动 */
		public static final String JoinEvent = PartName + "addactivity";
		/** 6:取消活动报名 */
		public static final String CalcelEvent = PartName + "cancelactivity";
		/** 7:上传活动照片 */
		public static final String UpPhoto = PartName + "uploadactivityphoto";
		/** 9:发活动评分(打分) */
		public static final String AddValuation = PartName
				+ "addactivityevaluation";
		/** 12:举报发起人 */
		public static final String Accuse = PartName
				+ "reportactivityapplyuser";
		/** 13:发活动 */
		public static final String AddEvent = PartName + "sendactivity";
		/** 14:对活动感兴趣 */
		public static final String Interest = PartName + "addactivityinterest";
		/** 15:发活动交流(评论) */
		public static final String AddRemark = PartName + "addactivityremark";
		/** 16:我的活动 */
		public static final String GetMyEvent = PartName + "getmyacitivity";
		/** 17:设置展示(开/关) */
		public static final String HideEvent = PartName + "sethidestatus";
		/** 18:人员管理 */
		public static final String MgrJoiner = PartName
				+ "setactivityadminuser";
		/** 19:获取Ta的活动列表 */
		public static final String GetOtherEvent = PartName
				+ "getotheracitivity";
		/** 20:活动初始页面 */
		public static final String GetInitialEvent = PartName + "gethotsearch";
		/** 21:活动搜索 */
		public static final String Search = PartName + "getsearchacitivity";
		/** 22:验证码验证接口 */
		public static final String CheckCode = PartName + "checkcode";
		/** 23:获取活动评论 */
		public static final String GetRemark = PartName + "getactivityremark";
		/** 24:获取最新参加 */
		public static final String GetNewJoin = PartName + "getnewjoinactivity";
		/** 25:活动地图模式 */
		public static final String GetMap = PartName + "getmapactivity";
		/** 26:邀请好友 */
		public static final String InviteFriend = PartName + "invitationfriend";
		/** 43:获取首页数据 */
		public static final String getHomePage = PartName + "getactivityindex";
	}

	public static final class Message {
		private static final String PartName = "message/";
		/** 1:私信页面 */
		public static final String GetPrivate = PartName
				+ "getprivateletterlist";
		/** 2:邀请页面 */
		public static final String GetInviteList = PartName + "getinvitelist";
		/** 3:通知页面 */
		public static final String GetNoticeList = PartName + "getnoticelist";
		/** 4:删除邀请、通知单条 */
		public static final String DeleteINSingleItem = PartName
				+ "deletemessage";
		/** 5:删除全部消息 */
		public static final String DeleteAllMessage = PartName
				+ "deletekindmessage";
		/** 6:获取私信详情页面 */
		public static final String GetPrivateDetailList = PartName
				+ "getprivatedetaillist";
		/** 7:删除单条私信接口 */
		public static final String DeleteOnePrivate = PartName
				+ "deleteprivateletterdialog";
		/** 9:设置已读接口 */
		public static final String IsRead = PartName + "updatemessagereadstate";
		/** 10:发送消息 */
		public static final String SendMessage = PartName
				+ "sendprivateletters";
		/** 11获取未读消息数量 **/
		public static final String GetUnReadMessageNum = PartName
				+ "getunreadcount";
		/** 12 后台获取私信最新消息 */
		public static final String GetPrivateListBackground = PartName
				+ "getnewprivatedetaillist";
	}

	public static final class Invite {
		private static final String PartName = "invite/";
		/** 1:我的邀请界面 */
		public static final String MyInviteList = PartName + "myinvitelist";
		/** 1:发邀请 */
		public static final String SendInvite = PartName + "sendinvite";
		/** 1:邀请处理 */
		public static final String OperateInvite = PartName
				+ "invitehandlelist";
		/** 1:单条邀请 */
		public static final String SingleInvite = PartName + "invitedetails";
		/** 1:判断能否邀请 */
		public static final String Condition = PartName + "condition";
	}

	/** 获取图片的URL地址 */
	public static String getImageUrl(String ImageID, String ImageDimen) {
		return Image_Addr + ImageID + ImageDimen;
	}

	/** 话题模块 */
	public static final class Topic {
		private static final String PartName = "space/";
		/** 1.话题首页 */
		public static final String TopicHome = PartName + "topiclist";
		/** 2.话题搜索 */
		public static final String TopicSearch = PartName + "searchtopic";
		/** 3.用户的话题 */
		public static final String UserTopic = PartName + "usertopic";
		/** 4.话题的回复 */
		public static final String GetRemark = PartName + "topicremarklist";
		/** 5.回复话题 */
		public static final String AddRemark = PartName + "addtopicremark";
		/** 6.发话题 */
		public static final String AddTopic = PartName + "sendtopic";
		/** 7.话题详情 */
		public static final String GetDetial = PartName + "topicdetial";
		/** 8.删除话题 */
		public static final String DeleteTopic = PartName + "deletetopic";

	}

	public static final class Venue {
		private static final String PartName = "venue/";
		/** 1.场馆列表（首页） */
		public static final String VenueHome = PartName + "venuelist";
		/** 2.场馆地图模式 */
		public static final String VenueMap = PartName + "mapvenue";
		/** 4.场馆搜索初始化 */
		public static final String VenueSearchInitial = PartName
				+ "gethotsearch";
		/** 5.场馆搜索 */
		public static final String VenueSearch = PartName + "searchvenue";
		/** 6.场地搜索 */
		public static final String PlaceSearch = PartName + "searchplace";
		/** 7.场馆详情 */
		public static final String GetDetail = PartName + "venuedetail";
		/** 8.获取指定场馆的场地统计信息 */
		public static final String GetVenueReserve = PartName + "venuereserve";
		/** 9.我的订单列表 */
		public static final String MyOrderList = PartName + "getmyorderlist";
		/** 10.订单详情 */
		public static final String OrderDetail = PartName + "myorderdetail";
		/** 11.添加购物车 */
		public static final String AddVenueCat = PartName + "addvenuecart";
		/** 12.删除购物车 */
		public static final String DeleteVenueCat = PartName
				+ "deletevenuecart";
		/** 13.修改购物车 */
		public static final String UpdateVenueCat = PartName
				+ "updatevenuecart";
		/** 14.我的购物车 */
		public static final String GetVenueCat = PartName + "getvenuecart";
		/** 15.创建订单 */
		public static final String CreatOrder = PartName + "addvenueorder";
		/** 16.地图发送短信 */
		public static final String SendMapMessage = PartName
				+ "sendPhoneNumberMessage";
		/** 17.订单详情发送验证码 */
		// 待补
		// public static final String SendOrderMessage = PartName + ;
		/** 18.场馆支付接口 */
		public static final String AppStart = PartName + "appstart";
		/** 19.订单确认 */
		public static final String GetOrderok = PartName + "getorderok";
		/** 20.获取用户可支付资金 */
		public static final String GetUserMoney = PartName + "getusermoney";
		/** 21.支付结果确认 */
		public static final String AppReturn = PartName + "appreturn";
		/** 22.发送手机验证码 */
		public static final String SendPhoneMessage = PartName
				+ "sendordercode";
		/** 23.取消订单 */
		public static final String CancelOrder = PartName + "cancelorder";
		/** 24.网页支付接口 */
		public static final String WebPay = PartName + "Alipay";
		/** 25.校验用户支付密码 */
		public static final String CheckPwd = PartName + "checkpaypwd";
	}

}