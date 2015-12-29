package com.whoyao.engine.user;

import com.whoyao.AppContext;
import com.whoyao.Const.Config;
import com.whoyao.Const.KEY;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.UserConditionSettingRModel;
import com.whoyao.model.UserSafeSettingTModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;

/**
 * @author hyh 
 * creat_at：2013-8-15-下午12:32:52
 */
public class UserSettingManager {
	public static UserSettingManager manager;
	public static UserSettingManager getManager(){
		if(null == manager){
			manager = new UserSettingManager();
		}
		return manager;
	}
	public boolean isShowPoint(){
		return MyinfoManager.getUserConfigFile().getBoolean(Config.ShowMyPoint, true);
	}
	public boolean isShowEvent(){
		return MyinfoManager.getUserConfigFile().getBoolean(Config.ShowMyEvent, false);
	}
	public void storeShowPoint(boolean selected){
//		int setting = 0;
//		if(selected){
//			setting = 1;
//		}
		MyinfoManager.getUserConfigFile().edit().putBoolean(Config.ShowMyPoint, selected).commit();
	}
	public void storeShowEvent(boolean selected){
//		int setting = 0;
//		if(selected){
//			setting = 1;
//		}
		MyinfoManager.getUserConfigFile().edit().putBoolean(Config.ShowMyEvent, selected).commit();
	}
	public void setUserSafeSetting(boolean isShowPoint,boolean isShowEvent){
		UserSafeSettingTModel model = new UserSafeSettingTModel();
		model.setSeeme(isShowPoint);
		model.setSeejoinacticity(isShowEvent);
		if(null != AppContext.location){
			model.setLatitude((float)AppContext.location.getLatitude());
			model.setLongitude((float)AppContext.location.getLongitude());
		}else{
			model.setLatitude(0);
			model.setLongitude(0);
		}
		Net.request(model, WebApi.User.UpUserSafeSetting, new ResponseHandler());
		storeShowPoint(isShowPoint);
		storeShowEvent(isShowEvent);
	}
	

	/**初始化安全设置*/
	public void initUserSafeSetting(){
		UserSafeSettingTModel model = new UserSafeSettingTModel();
		if(null != AppContext.location){
			model.setLatitude((float)AppContext.location.getLatitude());
			model.setLongitude((float)AppContext.location.getLongitude());
		}else{
			model.setLatitude(0);
			model.setLongitude(0);
		}
		model.setSeeme(isShowPoint());
		model.setSeejoinacticity(isShowEvent());
		Net.request(model , WebApi.User.AddUserSafeSetting, new ResponseHandler());
	}
	
	public void getUserConditionSetting(int userID,CallBack<UserConditionSettingRModel> callBack){
		
		Net.cacheRequest(KEY.User_ID, MyinfoManager.getUserInfo().getUserID()+"", WebApi.User.GetConditionSetting, new CallBackHandler<UserConditionSettingRModel>(true,callBack) {

			@Override
			public void onSuccess(String content) {
				UserConditionSettingRModel model = JSON.getObject(content, UserConditionSettingRModel.class);
				doCallBack(true,model);
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				doCallBack(400 == statusCode,null);
				super.onFailure(e, statusCode, content);
			}
		});
	}
}
