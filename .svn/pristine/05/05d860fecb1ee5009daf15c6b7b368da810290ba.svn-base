package com.whoyao.engine.event;

import android.content.Intent;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.activity.EventAddGuideActivity;
import com.whoyao.activity.VerifyNameAndPhoneActivity;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.EventCancelTModel;
import com.whoyao.model.EventRemarkTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

/**
 * @author hyh creat_at：2013-12-30-下午1:39:11
 */
public class EventEngine {
	/**发布活动*/
	public static void publishEvent(){
		UserInfoModel info = MyinfoManager.getUserInfo();
		if(info.isMobileV()){
			AppContext.startAct(EventAddGuideActivity.class);
		}else{
			Intent intent = new Intent(AppContext.curActivity, VerifyNameAndPhoneActivity.class);
			intent.putExtra(Extra.VerifyState, State.Verify_EventCreater);
			AppContext.startAct(intent);
		}
	}
	
	
	/** 参加活动 */
	public static void join(int eventID, CallBack<String> callback) {

		CallBackHandler<String> handler = new CallBackHandler<String>(true, callback) {
			@Override
			public void onSuccess(String content) {
				ResultModel result = JSON.getObject(content, ResultModel.class);

				switch (result.getResult()) {// 报名的结果
				// 1、报名成功
				case 1:
					NetCache.clearCaches();
					doCallBack(true, content);
					break;
				// 2、报名失败！活动开始前一小时内不能报名！
				case 2:
					Toast.show(R.string.warn_event_join_unavailable);
					break;
				// 3、报名失败！同一用户在同一活动中取消2次将再无报名机会！
				case 3:
					Toast.show(R.string.warn_event_join_over2times);
					break;
				// 4、不能报名哦！您参与的活动间隔需大于一小时！
				case 4:
					Toast.show(R.string.warn_event_join_time_conflict);
					break;
				// 5、报名失败！参加人数已满！
				case 5:
					Toast.show(R.string.warn_event_join_full);
					break;
				default:
					break;
				}
			}
		};
		// HideStatus
		RequestParams params = new RequestParams();
		params.put(KEY.EventID, eventID + "");
		params.put("HideStatus", "1");
		Net.request(params, WebApi.Event.JoinEvent, handler);
	}

	/** 取消参加活动 */
	public static void cancel(EventCancelTModel model, CallBack<String> callback) {
		if (TextUtils.isEmpty(model.getCancelremark()) && TextUtils.isEmpty(model.getCancelremarkcomment())) {
			Toast.show("请填写取消原因");
			return;
		}
		if (150 < Utils.getStringLength(model.getCancelremarkcomment())) {
			Toast.show("字数在150个字以内");
		}
		CallBackHandler<String> handler = new CallBackHandler<String>(true, callback) {
			@Override
			public void onSuccess(String content) {
				NetCache.clearCaches();
				doCallBack(true, content);
			}
		};
		Net.request(model, WebApi.Event.CalcelEvent, handler);
	}
	
	/**举报发起人*/
	public static void accuse(EventCancelTModel model,CallBack<String> callback){
		if(0 == model.getActivityid()){
			throw new NullPointerException("null event id");
		}
		if(TextUtils.isEmpty(model.getCancelremark()) && TextUtils.isEmpty(model.getCancelremarkcomment())){
			Toast.show("请填写举报原因");
			return;
		}
		if(200< Utils.getStringLength(model.getCancelremarkcomment())){
			Toast.show("字数在200个字以内");
		}
		CallBackHandler<String> handler = new CallBackHandler<String>(true, callback) {
			@Override
			public void onSuccess(String content) {
				NetCache.clearCaches();
				doCallBack(true,content);
			}
		};
		Net.request(model, WebApi.Event.Accuse, handler);
	}
	
	/**添加活动评论*/
	public static void addRemark(EventRemarkTModel model, CallBack<ResultModel> callback){
		if (Utils.getStringLength(model.getCommentcontent())==0) {
			Toast.show("请输入内容");
			return;
		}
		if(2 > Utils.getStringLength(model.getCommentcontent()) || 200 < Utils.getStringLength(model.getCommentcontent())){
			Toast.show("字数在2-200个字");
			return;
		}
		if(!TextUtils.isEmpty(model.getCodekey())){
			if(TextUtils.isEmpty(model.getCodevalue())){
				Toast.show(R.string.warn_code_empty);
				return;
			}
			if(Const.CodeLength_Image != model.getCodevalue().length()){
				Toast.show(R.string.warn_code_formatwrong);
				return;
			}
		}
		if(model.getActivityid() == 0){
			throw new NullPointerException("null event id");
		}
		RequestParams params = Net.getRequestParamsWithoutNull(model);
		
		Net.request(params, WebApi.Event.AddRemark, new CallBackHandler<ResultModel>(true,callback){
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				ResultModel result = JSON.getObject(content, ResultModel.class);
				doCallBack(true,result);
			}
		});
	}
	
	/**氛围描述*/
	public static  String getAtmosphereDesc(float value) {
		if(8 < value){
			return "氛围非常好";
		}
		if(6 < value){
			return "氛围不错";
		}
		if(4 < value){
			return "氛围还行";
		}
		if(2 < value){
			return "氛围凑合";
		}
		if(0 == value){
			return "";
		}
		return "氛围差";
	}
	
	/**人员出席分值描述*/
	public static  String getAttendedDesc(float value) {
		if(8 < value){
			return "到齐且准时";
		}
		if(6 < value){
			return "到齐迟到少";
		}
		if(4 < value){
			return "到齐迟到多";
		}
		if(2 < value){
			return "没到齐且迟到少";
		}
		if(0 == value){
			return "";
		}
		return "没到齐且迟到多";
	}
	public static  String getPriceDesc(float value) {
		if(8 < value){
			return "性价比非常好";
		}
		if(6 < value){
			return "性价比不错";
		}
		if(4 < value){
			return "性价比还行";
		}
		if(2 < value){
			return "性价比比较低";
		}
		if(0 == value){
			return "";
		}
		return "性价比很低";
	}
	public static  String getAddressDesc(float value) {
		if(8 < value){
			return "环境非常好";
		}
		if(6 < value){
			return "环境不错";
		}
		if(4 < value){
			return "环境还行";
		}
		if(2 < value){
			return "环境凑合";
		}
		if(0 == value){
			return "";
		}
		return "环境差";
	}
}
