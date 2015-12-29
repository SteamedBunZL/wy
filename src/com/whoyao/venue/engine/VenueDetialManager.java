package com.whoyao.venue.engine;

import java.util.ArrayList;

import android.content.SharedPreferences.Editor;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.KEY;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.EventCommentItem;
import com.whoyao.model.EventInfoModel;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.utils.JSON;
import com.whoyao.venue.model.VenueDetialModel;

/**
 * @author hyh creat_at：2013-9-6-上午8:44:15
 */
public class VenueDetialManager {
	private static VenueDetialManager manager = null;
	private static int currentID ;
	public static EventInfoModel eventInfo;
	public static ArrayList<JoinerModel> joinerList;
	public static ArrayList<EventPhotoModel> photoList;
	public static ArrayList<EventCommentItem> commentList;
	private VenueDetialModel model;
	
	public static synchronized VenueDetialManager getInstance() {
		if (null == manager) {
			manager = new VenueDetialManager();
		}
		return manager;
	}
	private VenueDetialManager() {
		super();
	}

	public void recycle() {
		manager = null;
	}
	
	/**当前Manager是否存储当前活动的数据*/
	public boolean isCurrentManager(int venueID){
		return currentID == venueID;
	}
	public static int getCurrentID(){
		return currentID;
	}
	public void getDetial(int venueID, CallBack<String> callback){
		currentID = venueID;
		Net.cacheRequest(KEY.VenueID, venueID + "", WebApi.Venue.GetDetail, new CallBackHandler<String>(false,callback) {
			

			@Override
			public void onSuccess(String content) {
				model = JSON.getObject(content, VenueDetialModel.class);
				Editor edit = AppContext.getConfigFile().edit();
				edit.putString("Venue_"+model.getVenueid(), content);
				edit.commit();
				doCallBack(true,null);
			}
		});
	}
	public void getDetail2(int VenueId,int VenueType,CallBack<String> callback){
		currentID = VenueId;
		RequestParams params = new RequestParams();
		params.put("venueid", VenueId+"");
		params.put("venuetype", VenueType+"");
		Net.cacheRequest(params, WebApi.Venue.GetDetail, new CallBackHandler<String>(false,callback) {
			

			@Override
			public void onSuccess(String content) {
				model = JSON.getObject(content, VenueDetialModel.class);
				Editor edit = AppContext.getConfigFile().edit();
				edit.putString("Venue_"+model.getVenueid(), content);
				edit.commit();
				doCallBack(true,null);
			}
		});
	}

	public VenueDetialModel getData(){
		if(model == null){
			model = JSON.getObject(AppContext.getConfigFile().getString("Venue_"+ currentID, ""), VenueDetialModel.class);
		}
		return  model;
	}
}
