package com.whoyao.engine.event;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.RequestParams;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.model.EventListRModel;
import com.whoyao.model.EventListItem;
import com.whoyao.net.CallBackHandler;
import com.whoyao.net.Net;
import com.whoyao.utils.JSON;

/**
 * @author hyh creat_at：2013-9-6-上午8:44:15
 */
public class EventListManager {
	private static EventListManager manager = null;
	

	public static synchronized EventListManager getInstance() {
		if (null == manager) {
			manager = new EventListManager();
		}
		return manager;
	}

	private EventListManager() {
		super();
	}

	public void recycle() {
		manager = null;
	}

	public void getHotEvent(CallBack<List<EventListItem>> callback) {
		RequestParams params = new RequestParams();
		Net.cacheRequest(params, WebApi.Event.GetHotEvent, new CallBackHandler<List<EventListItem>>(true, callback) {
			@Override
			public void onSuccess(String content) {
				ArrayList<EventListItem> list = JSON.getObject(content, EventListRModel.class).ActivityListItem;
				doCallBack(true,list);
			}
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				doCallBack(false,null);
			}
		});

	}


//	public static class InitialEventListHandler extends ResponseHandler{
//		private CallBack<List<ini>> callback;
//
//		public EventMyListHandler(boolean isShowProgress, CallBack<List<EventListItem>> callback) {
//			super(isShowProgress);
//			this.callback = callback;
//		}
//		@Override
//		public void onSuccess(String content) {
//			ArrayList<InitialEventHotAreaListItem> hotAreaList = JSON.getObject(content, EventInitialRModel.class).getHotAreaListItem();
//			
//		}
//		
//
//	}
//	public static class InitialEventActivityListHandler extends ResponseHandler{
//
//		@Override
//		public void onSuccess(String content) {
//			ArrayList<InitialEventHotActivityListItem> hotActivityList=JSON.getObject(content, EventInitialRModel.class).getHotActivityListItem();
//		}
//		
//	}


//	public void getInitialEventAreaList()
}
