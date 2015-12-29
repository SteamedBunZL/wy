package com.whoyao.engine.user;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Set;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.Const.Config;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.InvitePubliseActivity;
import com.whoyao.activity.LoginActivity;
import com.whoyao.activity.MainActivity;
import com.whoyao.activity.UserOtherDetialActivity;
import com.whoyao.activity.UserSelfDetialActivity;
import com.whoyao.activity.VerifyNameAndPhoneActivity;
import com.whoyao.engine.BasicEngine;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.model.CheckAccountTModel;
import com.whoyao.model.RelationManageTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.model.UserSafeSettingRModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.service.WYService;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 用户相关业务逻辑
 * 
 * @author HYH create at：2013-5-6 下午01:00:39
 */
public class UserEngine extends BasicEngine {

	private static UserEngine userEngine;
	private static String account;
	private static String password;
	public static boolean isFirstLogin = false;
	// 推送密钥
	private static String pushKey;
	// 密钥是否过期
	private static String isKeyOverdue;
	private static SharedPreferences pushKeyConfigFile;
	private static Editor editor;

	@Deprecated
	public static UserEngine getInstance() {
		synchronized (UserEngine.class) {
			if (null == userEngine) {
				userEngine = new UserEngine();
			}
		}
		return userEngine;
	}

	public static class LoginHandler extends CallBackHandler<Object> {

		public LoginHandler(boolean isShowProgress) {
			super(isShowProgress);
		}

		public LoginHandler(boolean isShowProgress, CallBack<Object> callback) {
			super(isShowProgress, callback);
		}

		@Override
		public void onFailure(Throwable error, int statusCode, String content) {
			super.onFailure(error, statusCode, content);
			if (400 == statusCode) {// 400 密码错误
				Toast.show(R.string.warn_login_wrong);
			}
			if (406 == statusCode) {// 406账号不存在
				if (FormatUtils.isEmailAddr(account)) {
					Toast.show(R.string.warn_login_email_unregister);
				} else if (FormatUtils.isPhoneNumber(account)) {
					Toast.show(R.string.warn_login_phone_unregister);
				} else {
					Toast.show(R.string.warn_account_unregister);
				}
			}
			doCallBack(false, null);
			if (!(AppContext.curActivity instanceof LoginActivity)) {
				AppContext.startAct(LoginActivity.class, true);
			}
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String content) {
			super.onSuccess(statusCode, headers, content);
			UserInfoModel info = JSON.getObject(content, UserInfoModel.class);
			MyinfoManager.setUserInfo(info);
			AppContext.loginTime = AppContext.serviceTimeMillis();
			MyinfoManager.getUserInfo().setUserPassWord(password);
			Editor edit = AppContext.getConfigFile().edit();
			edit.putString(Config.Account, account);
			edit.putString(Config.PassWord, password);
			edit.putInt(KEY.User_ID, info.getUserID());
			edit.commit();
			// 登录成功，获取密钥，判断密钥是否过期，开启服务
			// 1.获取pushKey;
			// String pushKey = getPushKey();
			// 2.通过pushKey,组合连接请求数据;
			// String pushCode = "{u:" + "\"" + info.getUserID() + "\"" + ","
			// + "k:" + "\"" + pushKey + "\"" + "}";
			// L.i(Const.ZL, "pushCode-" + pushCode);
			// 3.把推送数据转化为byte[]
			// byte[] body;
			// int length;
			// byte[] bodyLength;
			// try {
			// body = pushCode.getBytes("UTF-8");
			// length = pushCode.length();
			// bodyLength = integerToBytes(length, 4);
			// } catch (UnsupportedEncodingException e1) {
			// e1.printStackTrace();
			// }
			// Socket client = new Socket();
			// try {
			// client.connect(new InetSocketAddress(WebApi.Ims,
			// WebApi.Port),3000);
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// new Thread(){
			// Socket client;
			// client = new Socket();
			// try {
			// client.connect(new InetSocketAddress(WebApi.Ims,
			// WebApi.Port),3000);
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			//
			// }
			// try {
			// Socket client = new Socket(WebApi.Ims, WebApi.Port);

			// } catch (UnknownHostException e1) {
			// Toast.show("socket连接失败");
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }

			// 连接请求结束
			MyinfoManager.getManager().getMyDetil(false,
					new CallBack<UserDetialModel>());

			// TODO 获取隐私设置,条件是设置并存储
			Net.request(KEY.User_ID, MyinfoManager.getUserInfo().getUserID()
					+ "", WebApi.User.GetUserSafeSetting, new ResponseHandler(
					false) {
				public void onSuccess(String content) {
					UserSafeSettingRModel safeModel = JSON.getObject(content,
							UserSafeSettingRModel.class);
					UserSettingManager.getManager().setUserSafeSetting(
							safeModel.isSeeMe(), safeModel.isSeeJoinActicity());
				};

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					UserSettingManager.getManager().initUserSafeSetting();
				}
			});
			doCallBack(true, null);
		}
	};

	/**
	 * 把Int转为byte[]
	 * 
	 * @param integer
	 * @param len
	 * @return
	 */
	public static byte[] integerToBytes(int integer, int len) {
		// if (integer < 0) { throw new
		// IllegalArgumentException("Can not cast negative to bytes : " +
		// integer); }
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		for (int i = 0; i < len; i++) {
			bo.write(integer);
			integer = integer >> 8;
		}
		return bo.toByteArray();
	}

	/**
	 * 获取推送密钥
	 * 
	 * @return
	 */
	// public static synchronized SharedPreferences getPushKeyConfigFile(){
	// if (null == pushKeyConfigFile) {
	// pushKeyConfigFile =
	// AppContext.context.getSharedPreferences(Config.PUSH_KEY+AppContext.getConfigFile().getInt(KEY.User_ID,
	// 0), Context.MODE_PRIVATE);
	// editor = pushKeyConfigFile.edit();
	// }
	// return pushKeyConfigFile;
	//
	// }
	public static String getPushKey() {
		isKeyOverdue = AppContext.getConfigFile().getString(
				AppContext.getConfigFile().getInt(KEY.User_ID, 0)
						+ Config.IS_OVERDUE, null);
		if (isKeyOverdue != null && TextUtils.equals(isKeyOverdue, "no")) {
			// pushKey = AppContext.getConfigFile().getString(Config.PUSH_KEY,
			// null);
			pushKey = AppContext.getConfigFile().getString(
					AppContext.getConfigFile().getInt(KEY.User_ID, 0)
							+ Config.PUSH_KEY, null);
		} else {
			Net.request(null, WebApi.User.GetPushKey, new ResponseHandler() {

				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					JSONObject json;
					try {
						json = new JSONObject(content);
						pushKey = (String) json.get("Result");
						L.i(Const.ZL, "pushKey-" + pushKey);
						Editor edit = AppContext.getConfigFile().edit();
						edit.putString(
								AppContext.getConfigFile().getInt(KEY.User_ID,
										0)
										+ Config.IS_OVERDUE, "no");
						edit.putString(
								AppContext.getConfigFile().getInt(KEY.User_ID,
										0)
										+ Config.PUSH_KEY, pushKey);
						edit.commit();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
				}

			});
		}
		// TODO 获取pushKey失败
		if (null == pushKey) {

		}
		return pushKey;

	}

	/**
	 * 登录
	 * <p>
	 * 从配置文件获取用户名和密码进行登录
	 */
	public static void login() {
		String userName = AppContext.getConfigFile().getString(Config.Account,
				null);
		String password = AppContext.getConfigFile().getString(Config.PassWord,
				null);
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			AppContext.startAct(LoginActivity.class, true);
			if (!AppContext.isNetAvailable()) {
				Toast.show(R.string.warn_network_unavailable);
			}
			return;
		} else {
			// password = DES.decrypt(password, Encryption.DES_KEY);
			login(userName, password, false, new CallBack<Object>() {
				@Override
				public void onCallBack(boolean isSuccess) {
					if (isSuccess) {
						AppContext.startAct(MainActivity.class, true);
					}
				}
			});
		}
	}

	/**
	 * 登录
	 * <p>
	 * 从配置文件获取用户名和密码进行登录
	 */
	public static void relogin() {
		String userName = AppContext.getConfigFile().getString(Config.Account,
				null);
		String password = AppContext.getConfigFile().getString(Config.PassWord,
				null);
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			AppContext.startAct(LoginActivity.class, true);
			if (!AppContext.isNetAvailable()) {
				Toast.show(R.string.warn_network_unavailable);
			}
			return;
		} else {
			// password = DES.decrypt(password, Encryption.DES_KEY);
			login(userName, password, false, new CallBack<Object>() {
				@Override
				public void onCallBack(boolean isSuccess) {
					// TODO,重新请求
				}
			});
		}
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 * @param password
	 */
	public static void login(String account, String password,
			boolean isShowLoadding, CallBack<Object> callBack) {
		NetCache.clearCaches();
		if (TextUtils.isEmpty(account)) {
			Toast.show(R.string.warn_account_empty);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.show(R.string.warn_pwd_empty);
			return;
		}
		if (!FormatUtils.isPhoneNumber(account)
				&& !FormatUtils.isEmailAddr(account)) {
			Toast.show(R.string.warn_account_formatwrong);
			return;
		}
		/* 检查密码复杂度 */
		// if (!FormatUtils.isPwdComplex(password)) {
		// Toast.show(R.string.warn_login_wrong);
		// return;
		// }
		if (password.length() < 6) {
			Toast.show(R.string.warn_login_wrong);
			return;
		}
		UserEngine.account = account;
		UserEngine.password = password;
		RequestParams params = Net.getRequestParamsShowNull(ClientEngine
				.getMobileInfo());
		params.put(KEY.Account, account);
		params.put(KEY.Password, password);
		params.put(KEY.User_ID, "");
		if (null != AppContext.location) {
			params.put("longitude", (float) AppContext.location.getLongitude()
					+ "");
			params.put("latitude", (float) AppContext.location.getLatitude()
					+ "");
			L.i("互邀", "Latitude[" + AppContext.location.getLatitude()
					+ "]Longitude[" + AppContext.location.getLongitude() + "]");
		} else {
			params.put("longitude", 0 + "");
			params.put("latitude", 0 + "");
		}
		L.i("互邀", "登录传的值:{" + params);
		Net.request(params, WebApi.User.Login, new LoginHandler(isShowLoadding,
				callBack));
//		Net.request(params, WebApi.Test.TestHeader, new LoginHandler(
//				isShowLoadding, callBack));
	}

	/**
	 * 注销
	 */
	public void signout(Context context) {
		// // 注销OpenFire服务
		// if (null == intentService) {
		// intentService = new Intent();
		// intentService.setClass(Constant.context, OpenFireService.class);
		// //intentService.putExtra(OpenFireService.OF_LOGIN_INFO, new String[]
		// { Constant.USER.getOfUserName(), Constant.OF_PASSWORD });
		// }
		// // OpenFireConnection openFireCon = OpenFireConnection.getInstance();
		// // openFireCon.closeConnection();
		// Application.context.stopService(intentService);
		// // 关闭所有BasicAcitvity
		//
		// // 清除用户数据
		// Constant.USER = null;
		// Constant.OF_PASSWORD = "";
		// // 删除XML中的密码
		// //sp = context.getSharedPreferences(Constant.CONFIG_FILE,
		// Activity.MODE_PRIVATE);
		// Editor editor = configFile.edit();// 注销时,将保存的密码置为空
		// editor.putString(Extra.XML_PASSWORD, "");
		// editor.commit();
		// // 进入LandActivity
		// Intent logoutIntent = new Intent();
		// logoutIntent.setClass(context, SignActivity.class);
		// context.startActivity(logoutIntent);
		//
		// //清除cookie数据
		// cookieStore.clear();
	}

	/**
	 * 注册
	 */
	// @Deprecated
	// public static void register(RegisterModel registInfo) {
	// Net.request(registInfo, WebApi.User.Register, new VerifyCodeHandler());
	// }

	public static void checkAccount(String phone, String email,
			String nickname, CallBack<CheckAccountResponseModel> callback) {
		CheckAccountTModel accounts = new CheckAccountTModel(phone, email,
				nickname);
		Net.request(accounts, WebApi.User.CheckAccount,
				new CallBackHandler<CheckAccountResponseModel>(true, callback) {
					@Override
					public void onSuccess(String content) {
						CheckAccountResponseModel model = JSON.getObject(
								content, CheckAccountResponseModel.class);
						doCallBack(true, model);
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						doCallBack(false, null);
					}
				});
	}

	public static void startUserDetial(int userID) {
		UserInfoModel info = MyinfoManager.getUserInfo();
		if (info != null && (0 == userID || userID == info.getUserID())) {
			AppContext.startAct(UserSelfDetialActivity.class);
		} else {
			Intent intent = new Intent(AppContext.curActivity,
					UserOtherDetialActivity.class);
			intent.putExtra(Extra.SelectedID, userID);
			AppContext.startAct(intent);
		}
	}

	/**
	 * 获取短信验证码
	 * 
	 * @param phoneNum
	 * @param type
	 */
	public static void SendVerifyCode(String phoneNum, String type,
			ResponseHandler handler) {
		if (!checkPhoneFormat(phoneNum)) {
			return;
		}

		RequestParams params = new RequestParams();
		params.put("userphone", phoneNum);
		params.put(KEY.Type, type);

		Net.request(params, WebApi.User.SendVerifyCode, handler);
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param phoneNum
	 * @param verifyCode
	 * @param type
	 */
	public static void CheckVerifyCode(String phoneNum, String verifyCode,
			String type, ResponseHandler handler) {

		RequestParams params = new RequestParams();
		params.put("userphone", phoneNum);
		params.put("verifycode", verifyCode);
		params.put(KEY.Type, type + "");
		if (null != MyinfoManager.getUserInfo()) {
			params.put(KEY.User_ID, MyinfoManager.getUserInfo().getUserID()
					+ ""); // 添加当前用户ID
		}

		Net.request(params, WebApi.User.CheckVerifyCode, handler);
	}

	/**
	 * 检查手机号码格式
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static boolean checkPhoneFormat(String phoneNum) {
		if (TextUtils.isEmpty(phoneNum)) {
			Toast.show(R.string.hint_input_phone);
			return false;
		}
		if (!FormatUtils.isPhoneNumber(phoneNum)) {
			Toast.show(R.string.warn_phone_formatwrong);
			return false;
		}
		return true;
	}

	public static boolean checkSmsCodeFormat(String code) {
		if (TextUtils.isEmpty(code)) {
			Toast.show(R.string.warn_code_empty);
			return false;
		}
		if (code.length() != Const.CodeLenght_SMS
				|| !TextUtils.isDigitsOnly(code)) {
			Toast.show(R.string.warn_code_formatwrong);
			return false;
		}
		return true;
	}

	public static boolean checkEmailFormat(String email) {
		if (TextUtils.isEmpty(email)) {
			Toast.show(R.string.warn_email_empty);
			return false;
		}
		if (!FormatUtils.isEmailAddr(email)) {
			Toast.show(R.string.warn_email_formatwrong);
			return false;
		}
		return true;

	}

	public static boolean checkNickFormat(String nickName) {
		if (TextUtils.isEmpty(nickName)) {
			Toast.show(R.string.warn_nick_empty);
			return false;
		}
		int length = FormatUtils.getStringLengthEn1Cn2(nickName);
		/*
		 * if (4 > length) { Toast.show(R.string.warn_nick_toshort); return
		 * false; } if (20 < length) { Toast.show(R.string.warn_nick_tolong);
		 * return false; }
		 */
		if (4 > length || 20 < length || !FormatUtils.isNickname(nickName)) {
			Toast.show(R.string.warn_nick_formatwrong);
			return false;
		}
		return true;
	}

	/**
	 * 检查输入的用户名,密码是否符合规范
	 */
	public static boolean checkAccountFormat(String account) {
		if (TextUtils.isEmpty(account)) {
			Toast.show(R.string.warn_account_empty);
			return false;
		}

		if (!FormatUtils.isPhoneNumber(account)
				&& !FormatUtils.isEmailAddr(account)) {
			Toast.show(R.string.warn_account_formatwrong);
			return false;
		}
		return true;
	}

	/**
	 * 检查密码格式
	 * 
	 * @param password
	 * @return
	 */
	public static boolean checkPasswordFormat(String password) {

		if (TextUtils.isEmpty(password)) {
			Toast.show(R.string.warn_pwd_empty);
			return false;
		}
		if (!FormatUtils.isPwdComplex(password)) {
			Toast.show(R.string.warn_pwd_formatwrong);
			return false;
		}
		return true;
	}

	/**
	 * 显示上一次登录的账号
	 * 
	 * @param accountTv
	 */
	@Deprecated
	public static void setLastAccoutn(TextView accountTv) {
		String lastAccount = AppContext.getConfigFile().getString(
				Config.Account, "");
		accountTv.setText(lastAccount);
	}

	public static String getLastAccount() {
		return AppContext.getConfigFile().getString(Config.Account, "");
	}

	/**
	 * 注销
	 */
	public static void logout() {
		Set<WeakReference<Activity>> actSet = BasicActivity.wActivitySet;
		for (WeakReference<Activity> weak : actSet) {
			Activity act = weak.get();
			if (null != act) {
				act.finish();
			}
		}
		MyinfoManager.setUserInfo(null);
		AppContext.loginTime = 0;
		Editor edit = AppContext.getConfigFile().edit();
		edit.putString(Config.Accesstoken, null);
		edit.putString(Config.PassWord, null);
		edit.putString(Config.Openid, null);
		edit.commit();
		AppContext.curActivity.stopService(new Intent(AppContext.curActivity,
				WYService.class));
		AppContext.startActivityInNewTask(LoginActivity.class);
		Net.request(null, WebApi.User.Logout, new ResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				Log.i("互邀logout", "onSuccess");
				super.onSuccess(statusCode, headers, content);
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				Log.i("互邀logout", "onFailure");
				super.onFailure(e, statusCode, content);
			}
		});
	}

	public static void sendInvert(final int receiveUserID, final String userNick) {
		Net.request(KEY.User_ID, receiveUserID + "", WebApi.Invite.Condition,
				new ResponseHandler(true) {
					@Override
					public void onSuccess(String content) {
						int result = -1;
						try {
							result = Integer.parseInt(content);
						} catch (Exception e) {
						}
						MessageDialog dialog = new MessageDialog(
								AppContext.curActivity);
						dialog.setRightButton("取消", null);
						switch (result) {
						case -1:// 解析错误
							Toast.show(R.string.warn_netrequest_failure);
							break;
						case 0:// 可以发
							UserInfoModel info = MyinfoManager.getUserInfo();
							Intent intent = new Intent();
							intent.putExtra(Extra.SelectedID, receiveUserID);
							intent.putExtra(Extra.SelectedItemStr, userNick);
							if (!info.isMobileV()
									|| TextUtils.isEmpty(info.getUserRelName())) {// 要求手机认证/填写真实姓名
								intent.setClass(AppContext.curActivity,
										VerifyNameAndPhoneActivity.class);
								intent.putExtra(Extra.VerifyState,
										State.Verify_InviteCreater);
							} else {
								intent.setClass(AppContext.curActivity,
										InvitePubliseActivity.class);
							}
							AppContext.startAct(intent);
							break;
						case 1:// 对方不接受
						case 5:// 在对方黑名单
							dialog.setLeftButton("确定", null);
							dialog.setMessage("很抱歉！TA不愿意接受邀请");
							dialog.show();
							break;
						case 2:// 性别不符合
							dialog.setLeftButton("确定", null);
							dialog.setMessage("很抱歉！您的性别不符合TA的邀请条件");
							dialog.show();
							break;
						case 3:// 年龄不符合
							dialog.setLeftButton("确定", null);
							dialog.setMessage("很抱歉！您的年龄不符合TA的邀请条件");
							dialog.show();
							break;
						case 4:// 对方要求实名认证
							dialog.setLeftButton("确定", null);
							dialog.setMessage("很抱歉！对方要求实名认证");
							dialog.show();
							break;
						case 6:// 在我黑名单
							dialog.setLeftButton("确定", new OnClickListener() {
								@Override
								public void onClick(View v) {
									RelationManageTModel unBlackModel = new RelationManageTModel(
											receiveUserID, Type.User_Del);
									Net.request(unBlackModel,
											WebApi.User.BlackManage,
											new ResponseHandler(true) {
												@Override
												public void onSuccess(
														String content) {
													ResultModel result = JSON
															.getObject(
																	content,
																	ResultModel.class);
													if (result != null
															&& result
																	.isSuccess()) {
														Intent intent = new Intent(
																AppContext.curActivity,
																InvitePubliseActivity.class);
														intent.putExtra(
																Extra.SelectedID,
																receiveUserID);
														intent.putExtra(
																Extra.SelectedItemStr,
																userNick);
														AppContext
																.startAct(intent);
													} else {
														Toast.show(R.string.warn_netrequest_failure);
													}
												}
											});
								}
							});// TODO 把对方移出黑名单,进入邀请界面
							dialog.setMessage("执行此操作需先将TA移出黑名单，确定将TA移出黑名单吗？");
							dialog.show();
							break;
						default:
							break;
						}
					}
				});
	}
}
