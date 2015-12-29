package com.whoyao.venue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.map.BMapUtil;
import com.whoyao.map.MapManager;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;
import com.whoyao.venue.model.SiteVenueItem;

/**
 * 
 * @author huiyh 
 * creat_at：2013-11-28-上午10:02:02
 */
public class VenueAddrMapActivity extends Activity {
	private MapView mMapView = null;
	private MapController mMapController = null;
//	private MyOverlay mOverlay = null;
	private PopupOverlay pop = null;
	private OneVenueOverlay oneVenueOverlay;
//	private TextView popupText = null;
//	private View viewCache = null;
	private OverlayItem item = null;
	private MapManager app = null;
	private String addr = null;
	private ImageButton pageBackButton;
	private double mLon = 116.404;
	private double mLat = 39.945;
	private GeoPoint p = null;
	private MKMapTouchListener mapTouchListener = null;
	private GeoPoint currentPoint = null;
	private int markerHeight;
	private SiteVenueItem item2;
	private TextView tvTitle;
//	private Button popupBtn;
//	private Bitmap drawingCache;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		app = MapManager.getInstance();
		markerHeight = getResources().getDrawable(R.drawable.poi_marker_active).getIntrinsicHeight();
		setContentView(R.layout.activty_event_addr_map);
		tvTitle = (TextView) findViewById(R.id.tv_map_title);
		tvTitle.setText("场馆地图");
		pageBackButton = (ImageButton) findViewById(R.id.page_btn_back);
		pageBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mMapView = (MapView) findViewById(R.id.bmapEventView);
		oneVenueOverlay = new OneVenueOverlay(mMapView);
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(14);
		/**
		 * 显示内置缩放控件
		 */
		mMapView.setBuiltInZoomControls(false);
	
		Intent intent = getIntent();
		item2 = (SiteVenueItem) intent.getSerializableExtra(Extra.SelectedItem);
		if ((int)(item2.getLatitude()*1E6)!=0&&(int)(item2.getLongitude()*1E6)!=0) {
//			// 当用intent参数时，设置中心点为指定点
////			Bundle b = intent.getExtras();
			addr = item2.getAddress();
			p = new GeoPoint((int)(item2.getLatitude()*1E6),(int)(item2.getLongitude()*1E6));
//			L.i(Const.AppName,"latitude"+p.getLatitudeE6()+"longtitude"+p.getLongitudeE6());
//			
		} else {
			// 设置中心点为天安门
			p = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
		}
		currentPoint = p;
		initOverlay();
		mMapController.setCenter(currentPoint);
//		popupText.setText(addr);
		
		
//		Bitmap[] bmps = new Bitmap[3];  
//		bmps[0] = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.indoor_loc_bg_bar));
//		bmps[1] = BMapUtil.getBitmapFromView(popupText);  
//		bmps[2] = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.indoor_loc_bg_bar));  
//		pop.showPopup(bmps, new GeoPoint(currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6()),markerHeight);
		
//		Bitmap[] bitmaps = {BMapUtil.getBitmapFromView(popupText),BMapUtil.getBitmapFromView(popupBtn)};
//		pop.showPopup(bitmaps, new GeoPoint(currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6()),markerHeight);
		mapTouchListener = new MKMapTouchListener() {
			@Override
			public void onMapLongClick(GeoPoint point) { }

			@Override
			public void onMapDoubleClick(GeoPoint point) { }

			@Override
			public void onMapClick(GeoPoint point) { }
		};
		mMapView.regMapTouchListner(mapTouchListener);
	}

	public void upDateMapState() {
		pop.hidePop();
		p = new GeoPoint(currentPoint.getLatitudeE6(),
				currentPoint.getLongitudeE6());
		item.setMarker(getResources().getDrawable(R.drawable.poi_marker));
		item.setGeoPoint(p);
//		mOverlay.updateItem(item);
		mMapView.refresh();
	}

	public void initOverlay() {
		/**
		 * 创建自定义overlay
		 */
//		mOverlay = new MyOverlay(getResources().getDrawable(
//				R.drawable.poi_marker), mMapView);
		
		/**
		 * 准备overlay 数据
		 */
		
		oneVenueOverlay.setData(item2);
//		mMapView.getOverlays().add(mOverlay);
		/**
		 * 刷新地图
		 */
		mMapView.getOverlays().add(oneVenueOverlay);
		mMapView.refresh();
		/**
		 * 向地图添加自定义View.
		 */
//		viewCache = getLayoutInflater().inflate(R.layout.map_popview_venue_location, null);
//		popupText = (TextView) viewCache.findViewById(R.id.popview_address_tv);
//		popupBtn = (Button) viewCache.findViewById(R.id.popview_address_btn);
		
		/**
		 * 创建一个popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) { }
		};
		pop = new PopupOverlay(mMapView, popListener);
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
//		mOverlay.removeAll();
		if (pop != null) {
			pop.hidePop();
		}
		mMapView.refresh();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		// 重新add overlay
		mMapView.refresh();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
	
//	public class MyOverlay extends ItemizedOverlay {
//
//		public MyOverlay(Drawable defaultMarker, MapView mapView) {
//			super(defaultMarker, mapView);
//		}
//
//		@Override
//		public boolean onTap(int index) {
//			item.setMarker(getResources().getDrawable(R.drawable.poi_marker_active));
//			mOverlay.updateItem(item);
//			mMapView.refresh();
//			popupText.setText(addr);
//			
		
			
//			Bitmap[] bmps = new Bitmap[3];  
//			bmps[0] = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.indoor_loc_bg_bar));
//			bmps[1] = BMapUtil.getBitmapFromView(popupText);  
//			bmps[2] = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.indoor_loc_bg_bar));  
//			pop.showPopup(bmps, new GeoPoint(currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6()),markerHeight);
//			BitmapDrawable bd =new BitmapDrawable(BMapUtil.getBitmapFromView(viewCache));
//			Bitmap[] bitmaps = {BMapUtil.getBitmapFromView(popupText),BMapUtil.getBitmapFromView(popupBtn)};
//			pop.showPopup(bitmaps, new GeoPoint(currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6()),markerHeight);
//			pop.showPopup(drawingCache, new GeoPoint(currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6()),markerHeight);
//			return true;
//		}
//
//		@Override
//		public boolean onTap(GeoPoint pt, MapView mMapView) {
//			if (pop != null) {
//				pop.hidePop();
//			}
//			return false;
//		}
//	}
}
