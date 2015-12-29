package com.whoyao.engine.event;

import java.util.ArrayList;

import com.whoyao.Const.KEY;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.EventCommentItem;
import com.whoyao.model.EventDetialRModel;
import com.whoyao.model.EventInfoModel;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.utils.JSON;

/**
 * @author hyh creat_at：2013-9-6-上午8:44:15
 */
public class EventDetialManager {
	private static EventDetialManager manager = null;
	private static int currentID ;
	public static EventInfoModel eventInfo;
	public static ArrayList<JoinerModel> joinerList;
	public static ArrayList<EventPhotoModel> photoList;
	public static ArrayList<EventCommentItem> commentList;
	
	public static synchronized EventDetialManager getInstance() {
		if (null == manager) {
			manager = new EventDetialManager();
		}
		return manager;
	}
	private EventDetialManager() {
		super();
	}

	public void recycle() {
		manager = null;
	}
	
	/**当前Manager是否存储当前活动的数据*/
	public boolean isCurrentManager(int eventID){
		return currentID == eventID;
	}
	public static int getCurrentID(){
		return currentID;
	}
	public void getDetial(int eventID, CallBack<String> callback){
		currentID = eventID;
		Net.cacheRequest(KEY.EventID, eventID + "", WebApi.Event.GetDetail, new CallBackHandler<String>(true,callback) {
			@Override
			public void onSuccess(String content) {
				EventDetialRModel model = JSON.getObject(content, EventDetialRModel.class);
				eventInfo = model.ActivityItem;
				joinerList = model.JoinerListItem;
				photoList = model.ActivityPhotoItem;
				commentList = model.ActivityComment;
				eventInfo.setJoinnum(joinerList.size() -1);
				doCallBack(true,content);
			}
		});
	}

}
