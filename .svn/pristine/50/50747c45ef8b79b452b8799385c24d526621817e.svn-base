package com.whoyao.engine.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const.Config;
import com.whoyao.R;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.WebApi;
import com.whoyao.activity.LoginActivity;
import com.whoyao.activity.RetEmailActivity;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.FreeModel;
import com.whoyao.model.HotTagModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.TagItemModel;
import com.whoyao.model.TagModel;
import com.whoyao.model.UpUserDetailModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.model.UserPhotoModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.FormatUtils;
import com.whoyao.utils.GraphicUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

/**
 * 我的资料
 * 
 * @author hyh creat_at：2013-7-25-上午9:21:19
 */
public class MyinfoManager {
	private static MyinfoManager myinfoManager;
	private static UserInfoModel userInfo;
	public static ArrayList<FreeModel> userFree;
	public static ArrayList<TagModel> userTag;
	public static ArrayList<UserPhotoModel> userPhoto;
	private static SharedPreferences userConfigFile;
	private static Editor editor;
	private static UserDetialModel detialInfo;
	public ArrayList<TagItemModel> eventTags;
	public ArrayList<TagItemModel> interestTags;
	public static boolean IsSetPass;
	public static MyinfoManager getManager() {
		if (myinfoManager == null) {
			myinfoManager = new MyinfoManager();
		}
		return myinfoManager;
	}

	public static void setUserInfo(UserInfoModel userInfo) {
		MyinfoManager.userInfo = userInfo;
		if (null == userInfo) {
			userConfigFile = null;
		}
	}

	public static UserInfoModel getUserInfo() {
		if (null == userInfo) {
			String infoStr = getUserConfigFile().getString(Config.UserInfo, null);
			if (!TextUtils.isEmpty(infoStr)) {
				getManager().initMyinfo(infoStr);
			}
		}
		return userInfo;
	}

	/** 获取配置xml文件 */
	public static synchronized SharedPreferences getUserConfigFile() {
		if (null == userConfigFile) {
			userConfigFile = AppContext.context.getSharedPreferences(Config.USER_XML_NAME + AppContext.getConfigFile().getInt(KEY.User_ID, 0),
					Context.MODE_PRIVATE);
			editor = userConfigFile.edit();
		}
		return userConfigFile;
	}
//	public static synchronized SharedPreferences saveStepaypass(){
//		return userConfigFile;
//		
//	}
	public void getMyDetil(boolean isShowProgress, CallBack<UserDetialModel> callback) {
		String userID = getUserInfo().getUserID() + "";
		RequestParams params = new RequestParams();
		params.put(KEY.User_ID, userID);
		params.put(KEY.Type, Type.Detial_my);
		Net.cacheRequest(params, WebApi.User.DetailInfo, new UserDetialHandler(isShowProgress, callback));
	}

	public void addUserPhoto(String imagePath, CallBack<UserPhotoModel> callback) {
		RequestParams params = new RequestParams();
		File file = new File(imagePath);
		if (file.exists()) {
			try {
				params.put(KEY.Image, file);
				Options options = GraphicUtils.getBiemapOptions(file.getParent());
				params.put(KEY.Width, options.outWidth + "");
				params.put(KEY.Height, options.outHeight + "");
				params.put(KEY.PhotoSubject, "");
				params.put(KEY.PhotoDescription, "");
			} catch (FileNotFoundException e) {
				AppException.handle(e);
				return;
			}
			Net.request(params, WebApi.User.AddUserPhoto, new CallBackHandler<UserPhotoModel>(true, callback) {

				@Override
				public void onSuccess(String content) {
					UserPhotoModel photoModel = null;
					try {
						JSONObject o = new JSONObject(content);
						photoModel = new UserPhotoModel();
						photoModel.setPhotoID(o.getInt("Id"));
						photoModel.setPhotoPath(o.getString("UserPhotoUrl"));
					} catch (JSONException e) {
						AppException.handle(e);
					}
					doCallBack(true, photoModel);
				}

			});
		}
	}

	public void upUserFace(String imagePath, CallBack<String> callback) {
		RequestParams params = new RequestParams();
		File file = new File(imagePath);
		if (file.exists()) {
			try {
				params.put(KEY.Image, file);
			} catch (FileNotFoundException e) {
				AppException.handle(e);
				return;
			}
			Net.request(params, WebApi.User.UpUserFace, new CallBackHandler<String>(true, callback) {

				@Override
				public void onSuccess(String content) {
					doCallBack(true, content);
				}

			});
		}
	}

	public void upUserInfo(UpUserDetailModel model) {
		try {
			model.setUserbirthday(MyinfoManager.userInfo.getUserBirthday());
		} catch (Exception e) {
			AppException.handle(e);
		}
		Net.request(model, WebApi.User.UpUserInfo, new ResponseHandler(false) {
			@Override
			public void onSuccess(String content) {
				NetCache.clearCaches();
				super.onSuccess(content);
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				if (400 == statusCode) {
					// Toast.show("保存修改失败");
				}
				super.onFailure(e, statusCode, content);
			}
		});
	}
	public void addUserInfo(UserInfoModel model) {
		try {
			L.i("互邀===","addUserInfo"+model.toString());
			model.setUserBirthday(MyinfoManager.userInfo.getUserBirthday());
		} catch (Exception e) {
			AppException.handle(e);
		}
		Net.request(model, WebApi.User.LoginThird, new ResponseHandler(false) {
			@Override
			public void onSuccess(String content) {
				NetCache.clearCaches();
				super.onSuccess(content);
			}
			
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				if (400 == statusCode) {
					// Toast.show("保存修改失败");
				}
				super.onFailure(e, statusCode, content);
			}
		});
	}

	public void upUserInfo(RequestParams params) {
		// try {
		// model.setUserbirthday(MyinfoManager.userInfo.getUserBirthday());
		// } catch (Exception e) {
		// AppException.handle(e);
		// }
		params.put("userbirthday", MyinfoManager.userInfo.getUserBirthday());
		Net.request(params, WebApi.User.UpUserInfo, new ResponseHandler() {
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				if (400 == statusCode) {
					// Toast.show("保存修改失败");
				}
				super.onFailure(e, statusCode, content);
			}
		});
	}
	public void changePassword(String oldPwd, String newPwd) {
		if (TextUtils.isEmpty(oldPwd)) {
			Toast.show(R.string.warn_pwd_old_empty);
			return;
		}
		if (TextUtils.isEmpty(newPwd)) {
			Toast.show(R.string.warn_pwd_new_empty);
			return;
		}
//		if (!FormatUtils.isPwdComplex(oldPwd)) {
//			Toast.show(R.string.warn_pwd_formatwrong2);
//			return;
//		}
		String storedPwd = AppContext.getConfigFile().getString(Config.PassWord, null);
		if (!TextUtils.isEmpty(storedPwd) && !oldPwd.equals(storedPwd)) {
			Toast.show(R.string.warn_pwd_old_wrong);
			return;
		}
		if (!FormatUtils.isPwdComplex(newPwd)) {
			Toast.show(R.string.warn_pwd_new_formatwrong);
			return;
		}
		// if(TextUtils.equals(oldPwd, newPwd)){
		// Toast.show("密码修改成功");
		// return;
		// }
		Net.request(KEY.NewPassword, newPwd, WebApi.User.ChangePassword, new ResponseHandler(true) {
			@Override
			public void onSuccess(String content) {
				UserEngine.logout();
				Toast.show("密码修改成功，请重新登录");
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				if (400 == statusCode || 406 == statusCode) {
					Toast.show(R.string.warn_pwd_old_wrong);
				}
			}
		});
	}

	public class UserDetialHandler extends CallBackHandler<UserDetialModel> {

		public UserDetialHandler(boolean isShowProgress, CallBack<UserDetialModel> callback) {
			super(isShowProgress, callback);
		}

		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			getUserConfigFile().edit().putString(Config.UserInfo, content).commit();
			initMyinfo(content);
			doCallBack(true, detialInfo);
		}

		@Override
		public void onFailure(Throwable e, int statusCode, String content) {
			doCallBack(false, null);
		}
	}

	private void initMyinfo(String myInfoJson){
		detialInfo = JSON.getObject(myInfoJson, UserDetialModel.class);
		MyinfoManager.userInfo = detialInfo.getUserList();
		MyinfoManager.userTag = detialInfo.getUserTag();
		MyinfoManager.userPhoto = detialInfo.getUserPhoto();
		MyinfoManager.userFree = detialInfo.getUserMyFree();
		MyinfoManager.IsSetPass = detialInfo.isIsSetPass();
		editor.putBoolean("IsSetPass", MyinfoManager.IsSetPass).commit();
		initTag();
	}
	
	/** 分离活动标签和兴趣标签 */
	public void initTag() {

		eventTags = new ArrayList<TagItemModel>();
		interestTags = new ArrayList<TagItemModel>();
		// 从资源文件中加载活动标签list
		String[] array = AppContext.getRes().getStringArray(R.array.event_tags);
		for (int i = 0; i < array.length; i++) {
			eventTags.add(new TagItemModel(i + 1, array[i]));
		}
		if (null != userTag) {
			for (TagModel tag : userTag) {
				int id = tag.getTagId();
				if (19 > id) {// 设置活动标签状态
					eventTags.get(tag.getTagId() - 1).setSelected(true);
				} else {// 设置兴趣标签状态
					interestTags.add(new TagItemModel(tag, true));
				}
			}
		}
	}

	public void getNextTags(int index, CallBack<HotTagModel> callback) {
		RequestParams params = new RequestParams();
		params.put(KEY.Index, index + "");
		params.put(KEY.Size, "9");
		Net.cacheRequest(params, WebApi.User.GetNextTags, new HotTagHandler(true, callback));
	}

	/** 获取选择的标签的ID,用逗号分隔 */
	public String getTagIds() {
		String tagIds = "";
		
			for (TagItemModel item : MyinfoManager.getManager().eventTags) {
				if (item.isSelected()) {
					tagIds = tagIds + item.getTagId() + ",";
				}
			}
			for (TagItemModel item : MyinfoManager.getManager().interestTags) {
				if (item.isSelected()) {
					tagIds = tagIds + item.getTagId() + ",";
				}
			}
			if(tagIds.endsWith(",")){
				tagIds = tagIds.substring(0, tagIds.length() - 1);
			}
			return tagIds;

		
	}

	/** 获取选择的标签的Name,用逗号分隔 */
	public String getTagNames() {
		String tagNames = "";
		for (TagItemModel item : MyinfoManager.getManager().eventTags) {
			if (item.isSelected()) {
				tagNames = tagNames + item.getTagName() + ",";
			}
		}
		for (TagItemModel item : MyinfoManager.getManager().interestTags) {
			if (item.isSelected()) {
				tagNames = tagNames + item.getTagName() + ",";
			}
		}
		if(tagNames.endsWith(",")){
			tagNames = tagNames.substring(0, tagNames.length() - 1);
		}
		return tagNames;

	}

	public List<FreeModel> getFree() {
		if (null != detialInfo) {
			return detialInfo.getUserMyFree();
		} else {
			return new ArrayList<FreeModel>();
		}
	}

	/** 验证邮箱 */
	public void verifyEmail(String email) {
		if (UserEngine.checkEmailFormat(email)) {
			RequestParams params = new RequestParams();
			params.put(KEY.Email, email);
			params.put(KEY.Type, Type.Email_Verify);
			Net.request(params, WebApi.User.ResetPwdEmail, new VerifyEmailHandler(email));
		}
	}

	public class VerifyEmailHandler extends ResponseHandler {
		String email;
		private String emailUrl;
		private MessageDialog dialog;

		public VerifyEmailHandler(String email) {
			super(true);
			this.email = email;
		}

		@Override
		public void onFinish() {
			super.onFinish();
			onSuccess("");
		}

		@Override
		public void onSuccess(String content) {
			ResultModel result = JSON.getObject(content, ResultModel.class);
			if (null != result) {
				switch (result.getResult()) {
				case 1:// 成功
					String domain = email.substring(email.indexOf("@") + 1);
					if ("gmail.com".equals(domain)) {
						emailUrl = "http://www.gmail.com";
					} else {
						emailUrl = "http://mail." + domain + "/";
					}
					OnClickListener listener = new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发Intent打开连接
							dialog.dismiss();
							if(AppContext.curActivity instanceof RetEmailActivity){
								AppContext.startAct(LoginActivity.class, true);
							}else{
								AppContext.curActivity.finish();
							}
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(emailUrl));
							AppContext.startActivityInNewTask(intent);
						}
					};
					dialog = new MessageDialog(AppContext.curActivity);
					dialog.setTitle("验证邮箱");
					dialog.setMessage("验证链接已发送到您的邮箱，请进入邮箱点击链接；重新登录后验证生效。");
					dialog.setLeftButton("确定", listener);
					dialog.show();
					break;
				case 2:// 超5次
					Toast.show(R.string.warn_email_over5times);
					break;
				case 3:// 未注册
					Toast.show(R.string.warn_email_registered);
					break;
				default:
					break;
				}
			}

		}

		@Override
		public void onFailure(Throwable e, int statusCode, String content) {
			if (400 == statusCode) {
				Toast.show("您的邮箱未注册，请检查是否拼写错误，如需注册，请点击注册");
			}
			super.onFailure(e, statusCode, content);
		}
	}

	public class HotTagHandler extends CallBackHandler<HotTagModel> {
		public HotTagHandler(boolean isShowProgress, CallBack<HotTagModel> callback) {
			super(isShowProgress, callback);
		}

		@Override
		public void onSuccess(String content) {
			HotTagModel tags = JSON.getObject(content, HotTagModel.class);
			doCallBack(true, tags);
		}
	}

}
