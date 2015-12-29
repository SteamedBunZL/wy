package com.whoyao.map;



import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.whoyao.AppContext;
import com.whoyao.Const;

/**
 * @author hyh 
 * creat_at：2013-8-13-下午1:55:36
 */
public class LocManager {

	private static LocManager mLocation = null;
	
	private LocationClient mLocClient = null;
	
	public static synchronized LocManager getInstance(){
		if(null == mLocation){
			mLocation = new LocManager();
		}
		return mLocation;
	}
	
	private LocManager() {
		mLocClient = new LocationClient(AppContext.context);
		BDLocationListener myListener = new LocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll");//设置坐标类型
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}
	
	public void refresh(){
		mLocClient.start();
		mLocClient.requestLocation();
	}
	
	/**
     * 定位SDK监听函数
     */
    public class LocationListenner implements BDLocationListener {
    	
        @Override
        public void onReceiveLocation(BDLocation location) {
            Intent intent = new Intent();
			if (null != location){
				AppContext.location = location;
            	intent.setAction(LocReceiver.LOC_SUCCESS);
            }else{
            	intent.setAction(LocReceiver.LOC_FAIL);
            }
			mLocClient.stop();
			AppContext.context.sendBroadcast(intent);         
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){

                return ;
            }
        }
    }
}
