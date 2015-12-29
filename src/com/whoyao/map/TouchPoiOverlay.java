package com.whoyao.map;

import android.graphics.drawable.Drawable;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.AppContext;
import com.whoyao.R;

/**
 * @author hyh 
 * creat_at：2013-8-13-下午7:58:38
 */
public class TouchPoiOverlay extends ItemizedOverlay<OverlayItem> {

	private OverlayItem touchItem;
	private static Drawable markerDr = AppContext.getRes().getDrawable(R.drawable.poi_marker);
	private static Drawable actMarkerDr = AppContext.getRes().getDrawable(R.drawable.poi_marker_active);
	public TouchPoiOverlay(MapView mapView) {
		super(actMarkerDr, mapView);
	}

	@Override
	protected boolean onTap(int index) {
		return super.onTap(index);
	}
	public void setTouchPoint(GeoPoint point,String title,String snippet){//"正在加载地址..."
		if(null == touchItem){
			touchItem = new OverlayItem(point, title, snippet);
//			touchItem.setMarker(markerDr);
		}else{
			removeItem(touchItem);
			touchItem.setGeoPoint(point);
			touchItem.setTitle(title);
			touchItem.setSnippet(snippet);
		}
		addItem(touchItem);
		mMapView.refresh();
	}
	
}
