package com.whoyao.venue;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity;
import com.whoyao.map.EventOverlay;
import com.whoyao.map.LocManager;
import com.whoyao.map.LocReceiver;
import com.whoyao.map.MapManager;
import com.whoyao.model.AreaData;
import com.whoyao.model.EventListItem;
import com.whoyao.model.EventMapRModel;
import com.whoyao.model.EventMapTModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;
import com.whoyao.venue.model.VenueItemModel;

/**
 * 
 * @author whoyao 场馆首页地图
 */

public class VenueMapModeActivity extends BasicActivity implements
		OnClickListener {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 * 用MapController完成地图控制
	 */
	private MapController mMapController = null;
	// 定位广播
	private LocReceiver locReceiver;
	protected BDLocation location;
	// 位置数据
	private LocationData locData;
	// 图层
	private MyLocationOverlay myLoc;
	// MapActivity 模型设置
	private EventMapTModel mapModel = new EventMapTModel();
	private ResponseHandler initMapHandler;
	// 图层
	private VenueOverlay venueOverlay;
	private TextView tvCity;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// locReceiver.register();
		// mMapView.refresh();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		// 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		MapManager.getInstance();

		// 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		setContentView(R.layout.activity_venue_map_mode);
		initMapView();
		initView();
	}

	private void initView() {
//		tvCity = (TextView) findViewById(R.id.event_tv_city);
//		tvCity.setVisibility(View.GONE);
		findViewById(R.id.event_iv_list).setOnClickListener(this);
//		findViewById(R.id.event_iv_search).setVisibility(View.GONE);
		findViewById(R.id.event_mapmode_iv_loc).setOnClickListener(this);
	}

	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.bmapEventView);
		venueOverlay = new VenueOverlay(mMapView);
		mMapController = mMapView.getController();// 获取地图控制器
		mMapController.enableClick(true);// 设置地图是否响应点击事件 .
		mMapController.setZoom(14);// 设置地图缩放级别
		mMapView.setBuiltInZoomControls(false);// 显示内置缩放控件
		locReceiver = new LocReceiver(LocReceiver.Success_And_Fail) {

			@Override
			public void onReceive(Context context, Intent intent) {

				location = AppContext.location;
				if (null != location) {
					if (null == myLoc) {
						myLoc = new MyLocationOverlay(mMapView);
					} else {
						mMapView.getOverlays().remove(myLoc);
					}
					locData = new LocationData();
					locData.latitude = location.getLatitude();
					locData.longitude = location.getLongitude();
					// 如果不显示定位精度圈，将accuracy赋值为0即可
					locData.accuracy = location.getRadius();
					locData.direction = location.getDerect();
					myLoc.setData(locData);
					// myLoc.setMarker(AppContext.getRes().getDrawable(R.drawable.btn_footbar_mid));
					mMapView.getOverlays().add(myLoc);
					myLoc.enableCompass();
					mMapView.refresh();
					mMapView.getController().animateTo(
							new GeoPoint((int) (locData.latitude * 1e6),
									(int) (locData.longitude * 1e6)));
					mapModel.setLatitude(location.getLatitude());
					mapModel.setLongitude(location.getLongitude());
					Net.cacheRequest(mapModel, WebApi.Venue.VenueMap,
							initMapHandler());
					// Net.cacheRequest(mapModel, WebApi.Venue.VenueMap,
					// new ResponseHandler());
					this.unRegister();
				}
			}
		}.register();
		LocManager.getInstance().refresh();
	}

	private ResponseHandler initMapHandler() {
		if (initMapHandler == null) {
			initMapHandler = new ResponseHandler(true) {

				@Override
				public void onSuccess(String content) {
					ArrayList<VenueItemModel> list = JSON.getList(content,
							VenueItemModel.class);
					int indexOf = mMapView.getOverlays().indexOf(venueOverlay);
					if (-1 != indexOf) {
						mMapView.getOverlays().remove(indexOf);
					}
					venueOverlay.setVenueData(list);
					mMapView.getOverlays().add(venueOverlay);
					mMapView.refresh();
				}

				@Override
				public void onFailure(Throwable e, int statusCode,
						String content) {
					super.onFailure(e, statusCode, content);
				}

			};
		}
		return initMapHandler;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.event_tv_city:
		// Intent data = new Intent(this, SelectCityActivity.class);
		// startActivityForResult(data, R.id.event_tv_city);
		// break;
		case R.id.event_iv_list:
			finish();
			break;
		// case R.id.event_iv_search:
		// AppContext.startAct(EventSearchInitialActivity.class);
		// break;
		case R.id.event_mapmode_iv_loc:
			if (null != locReceiver) {
				locReceiver.register();
				LocManager.getInstance().refresh();
			}
			break;
		default:
			break;
		}
	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// switch (requestCode) {
	// case R.id.event_tv_city:
	// if (Activity.RESULT_OK == resultCode && null != data) {
	// AreaData area = (AreaData) data
	// .getSerializableExtra(Extra.SelectedItem);
	// if (null != area) {
	// tvCity.setText(area.getAreaName());
	// }
	// }
	// break;
	// default:
	// break;
	// }
	// }

	@Override
	protected void onStop() {
		super.onStop();
		if (null != locReceiver) {
			locReceiver.unRegister();
		}
		 if (null != mMapView) {
		 mMapView.getOverlays().clear();
		 mMapView.refresh();
		 }
	}
}
