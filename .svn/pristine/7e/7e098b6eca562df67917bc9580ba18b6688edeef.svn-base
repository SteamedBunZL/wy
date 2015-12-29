package com.whoyao.engine.client;

import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppContext.TimeDeviationReckon;
import com.whoyao.Const;
import com.whoyao.Const.Config;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.TimeDeviationReceiver;
import com.whoyao.engine.BasicEngine;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.model.UpdateModel;
import com.whoyao.model.UpdateTModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.utils.Utils;

/**
 * @author hyh
 * @CreatTime 2013-7-1 上午10:23:21
 *            <p>
 *            客户端相关业务功能
 */
public class ClientEngine extends BasicEngine {

	public static boolean soundSetting;// 声音设置
	public static boolean vibratoSetting;// 震动设置
	public static boolean messageSetting;// 接收消息设置(是否保留后台服务)
	private static boolean isInit;
	private static String socketKey;

	// private static AsyncHttpResponseHandler initMobileHandler = new
	// AsyncHttpResponseHandler(){
	// private EngineCallBack callback;
	// public void setCallback(EngineCallBack callback){
	// this.callback = callback;
	// }
	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// super.onFailure(error, content);
	// if(null != callback){
	// callback.onFailure(error, content);
	// }
	//
	//
	//
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, Header[] headers, String content) {
	// super.onSuccess(statusCode, headers, content);
	// UpdateModel updateInfo = JSON.getObject(content, UpdateModel.class);
	// LOG.w("WhoYao", updateInfo.path);
	// Toast.makeText(Application.context, updateInfo.path,
	// Toast.LENGTH_LONG).show();
	// if(null != callback){
	// callback.onSuccess();
	// }
	// }
	//
	// };
	/** 显示呼叫客服的对话框 */
	public static void showCallServiceDialog(Activity context) {
		MessageDialog dialog = new MessageDialog(context);
		dialog.setTitle("客服电话");
		dialog.setMessage("互邀客服电话：" + Const.CUSTOM_SERVICE_PHONE);
		dialog.setLeftButton("呼叫", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClientEngine.callService();
			}
		});
		dialog.setRightButton("取消", null);
		dialog.show();
	}

	/** 拨打客服电话 */
	public static void callService() {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ Const.CUSTOM_SERVICE_PHONE));
		AppContext.startAct(intent);
	}

	/** 打开官方微博 */
	public static void openWeibo() {
		Utils.openURL(AppContext.curActivity, Const.CUSTOM_SERVICE_WEIBO);
		// Intent intent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse(Const.CUSTOM_SERVICE_WEIBO));
		// AppContext.startAct(intent);
	}

	/**
	 * 获取声音设置
	 * 
	 * @return
	 */
	public static boolean initSoundSeting() {
		return MyinfoManager.getUserConfigFile().getBoolean(
				Config.Setting_Sound, Config.Setting_Sound_Default);
	}

	/**
	 * 获取震动设置
	 * 
	 * @return
	 */
	public static boolean initVibratoSeting() {
		return MyinfoManager.getUserConfigFile().getBoolean(
				Config.Setting_Vibrato, Config.Setting_Vibrato_Default);
	}

	/**
	 * 获取消息设置
	 * 
	 * @return
	 */
	public static boolean initMessageSeting() {
		return MyinfoManager.getUserConfigFile().getBoolean(
				Config.Setting_Message, Config.Setting_Message_Default);
	}

	/**
	 * 获取推送Key
	 * 
	 * @return
	 */
	public static String initSocketKey() {
		return MyinfoManager.getUserConfigFile().getString(Config.PUSH_KEY,
				null);
	}

	/**
	 * 下载推送Key
	 */
	public static String downAndGetSocketKey() {
		RequestParams params = new RequestParams();
		params.put("mobilenum", ClientEngine.getMobileInfo().getMobileNum());

		Net.request(params, WebApi.User.GetPushKey, new ResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				JSONObject json;
				try {
					json = new JSONObject(content);
					socketKey = (String) json.get("Result");
					L.i(Const.ZL, "pushKey-" + socketKey);
					storeSocketKey(socketKey);
					int userId = MyinfoManager.getUserInfo().getUserID();
					String pushCode = "{\"u\":" + userId + "," + "\"k\":" + "\"" + socketKey
							+ "\"" + "\"n\":" + "\""
							+ ClientEngine.getMobileInfo().getMobileNum() + "\"" + "}";
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
			}

		});
		return socketKey;
	}

	public static void storeSocketKey(String pushKey) {
		socketKey = pushKey;
		MyinfoManager.getUserConfigFile().edit()
				.putString(Config.PUSH_KEY, pushKey).commit();
	}

	/**
	 * 保存声音设置
	 * 
	 * @return
	 */
	public static void storeSoundSeting(boolean setting) {
		soundSetting = setting;
		MyinfoManager.getUserConfigFile().edit()
				.putBoolean(Config.Setting_Sound, setting).commit();
	}

	/**
	 * 保存震动设置
	 * 
	 * @return
	 */
	public static void storeVibratoSeting(boolean setting) {
		vibratoSetting = setting;
		MyinfoManager.getUserConfigFile().edit()
				.putBoolean(Config.Setting_Vibrato, setting).commit();
	}

	/**
	 * 保存消息设置
	 * 
	 * @return
	 */
	public static void storeMessageSeting(boolean setting) {
		messageSetting = setting;
		MyinfoManager.getUserConfigFile().edit()
				.putBoolean(Config.Setting_Message, setting).commit();
	}

	/** 初始化客户端设置 */
	public static void initClientSetting() {
		soundSetting = ClientEngine.initSoundSeting();
		vibratoSetting = ClientEngine.initVibratoSeting();
		messageSetting = ClientEngine.initMessageSeting();
		socketKey = ClientEngine.initSocketKey();
	}

	/**
	 * 提醒
	 */
	public static void remind() {
		if (isInit) {
			initClientSetting();
		}
		if (vibratoSetting) {// 振动提醒
			Vibrator vib = (Vibrator) AppContext.context
					.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(400); // 震动时长
		}
		if (soundSetting) {// 声音提醒
			MediaPlayer mediaPlayer = MediaPlayer.create(AppContext.context,
					R.raw.msg);
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void feedback(String contentStr, CallBack callback) {
		if (5 > contentStr.length()) {
			Toast.show("内容不得少于5个字");
			return;
		}
		if (140 < contentStr.length()) {
			contentStr = contentStr.substring(0, 140);
		}
		Net.request(Extra.Feedback, contentStr, WebApi.User.Feedback,
				new CallBackHandler(true, callback) {
					@Override
					public void onSuccess(String content) {
						Toast.show("发送成功");
						doCallBack(true, null);
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
						if (400 == statusCode) {
							Toast.show("发送反馈意见失败");
						}
					}
				});
	}

	/**
	 * 获取客户端相关信息
	 * 
	 * @return
	 */
	public static MobileInfoModel getMobileInfo() {
		if (null == AppContext.mobileInfo) {
			MobileInfoModel mobileInfo = new MobileInfoModel();
			Context context = AppContext.context;
			// 设备型号
			mobileInfo.setPhoneModel(android.os.Build.MODEL);
			// 系统版本
			mobileInfo.setSystemVerison(android.os.Build.VERSION.RELEASE);
			// 屏幕尺寸
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			mobileInfo.setVgaWidth(metrics.widthPixels);
			mobileInfo.setVgaHeight(metrics.heightPixels);
			// 网络信息
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
			if (null != netInfo && netInfo.isAvailable()) {
				mobileInfo.setNetWork(netInfo.getTypeName());
			} else {// LogUtil.i("TAG",
					// "网络链接异常！");//mHandler.sendEmptyMessage(TOAST_NOT_CONN);
				mobileInfo.setNetWork(null);
			}
			// IMEI
			TelephonyManager tMgr = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			mobileInfo.setMobileNum(tMgr.getDeviceId());
			// 客户端版本号
			int apkVersionCode = Utils.getApkVersionCode(context);
			mobileInfo.setClientVersion(apkVersionCode);

			AppContext.mobileInfo = mobileInfo;
		}
		return AppContext.mobileInfo;
	}

	/**
	 * 检查更新
	 */
	public static void checkUpdate(boolean isShowDialog,
			CallBack<?> noNewVersion) {
		if (!isShowDialog && !AppContext.isNetAvailable()) {
			noNewVersion.onCallBack(false, null);
			return;
		}
		Net.request(new UpdateTModel(), WebApi.User.CheckUpdate,
				new CheckUpdateHandler(isShowDialog, noNewVersion));
	}

	public static void showUpdateDialog(Activity activity) {
		LayoutInflater inflater = LayoutInflater.from(AppContext.context);
		View view = inflater.inflate(R.layout.dialog_check_update, null);
		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setView(view);
		dialog.show();

	}

	/**
	 * 安装一个apk文件
	 * 
	 * @param file
	 */
	public static void installApk(File file) {
		/*
		 * <action android:name="android.intent.action.VIEW" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="content" /> <data android:scheme="file" /> <data
		 * android:mimeType="application/vnd.android.package-archive" />
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		// intent.setType("application/vnd.android.package-archive");
		// intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		AppContext.context.startActivity(intent);
		// finish();
	}

	public static class CheckUpdateHandler extends ResponseHandler {
		private CallBack call;
		private TimeDeviationReckon reckon;

		public CheckUpdateHandler(boolean isShowDialog, CallBack noNewVersion) {
			super();
			call = noNewVersion;
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO 检查更新失败
			// UpdateManager.getUpdateManager(null).showLatestOrFailDialog(FAILURE_MESSAGE);
			// super.onFailure(error, content);
			if (null != call) {
				call.onCallBack(false, null);
			}
		}

		@Override
		public void onStart() {
			super.onStart();
			reckon = new AppContext.TimeDeviationReckon();
		}

		@Override
		public void onSuccess(String content) {
			UpdateModel update = JSON.getObject(content, UpdateModel.class);
			reckon.refreshTimeDeviation(update.nowtime);
			int latestVersion = update.getLatestversion();
			int currentVersion = getMobileInfo().getClientVersion();
			int ignoreVersion = AppContext.getConfigFile().getInt(
					Config.ignoreVersion, currentVersion);
			if (currentVersion < latestVersion) {
				// 提示更新
				Editor edit = AppContext.getConfigFile().edit();
				edit.putString(Config.UpdateInfo, content);
				edit.commit();
				if (latestVersion != ignoreVersion) {
					UpdateManager.getUpdateManager(update).showNoticeDialog();
				} else {
					if (null != call) {
						call.onCallBack(true, null);
					}
				}
			} else {
				// 无更新
				if (null != call) {
					call.onCallBack(true, null);
				}
			}
			super.onSuccess(content);
		}
	}

	/**
	 * 判断是否是第一次使用
	 * 
	 * @return
	 */
	public static boolean isFirstTime() {
		boolean isFirst = AppContext.getConfigFile().getBoolean(
				Config.isFirstTime, true);
		if (isFirst) {
			Editor edit = AppContext.getConfigFile().edit();
			edit.putBoolean(Config.isFirstTime, false);
			edit.commit();
		}
		return isFirst;
	}

}
