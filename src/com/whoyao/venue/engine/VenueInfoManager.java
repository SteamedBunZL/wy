package com.whoyao.venue.engine;

import java.util.HashMap;

import com.loopj.android.http.RequestParams;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;
import com.whoyao.venue.model.VenueDetialModel;
import com.whoyao.venue.model.VenueItemModel;

/**
 * @author hyh 
 * creat_at：2014-2-20-上午10:45:36
 */
public class VenueInfoManager {
	private static ResponseHandler mResHandler;
	private static RequestParams mParams;
	private static HashMap<Integer, VenueDetialModel> mDataMap;

	public static void addOnDataChangedListener(int id,CallBack<VenueDetialModel> listener){
		RequestParams params = getParams(id);
		ResponseHandler handler = getResponseHandler(id);
		Net.request(params, WebApi.Venue.GetDetail, handler);
	}

	private static ResponseHandler getResponseHandler(final int id) {
		if(mResHandler == null){
			mResHandler = new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					VenueDetialModel model = JSON.getObject(content, VenueDetialModel.class);
					
				}
			};
		}
		return null;
	}

	private static RequestParams getParams(int id){
		if(mParams == null){
			mParams = new RequestParams();
		}
		mParams.put("venueid", id+"");
		return mParams;
	}
}
