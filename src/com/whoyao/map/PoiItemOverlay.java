package com.whoyao.map;

import java.util.ArrayList;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

/**
 * @author hyh 
 * creat_at：2013-8-13-下午7:58:38
 */
public class PoiItemOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlayItemList;
	private OverlayItem actItem = null;
	private View popView;
	private TextView poptext;
	private Button popBtn;
	private OverlayItem touchItem;
	private static Drawable markerDr = AppContext.getRes().getDrawable(R.drawable.poi_marker);
	private static Drawable actMarkerDr = AppContext.getRes().getDrawable(R.drawable.poi_marker_active);
	private LayoutParams popParams;
	private OnPopClickListener popClickListener;
	public PoiItemOverlay(MapView mapView) {
		super(markerDr, mapView);
		
	}

	public void setData(ArrayList<MKPoiInfo> poiList){

		mOverlayItemList = new ArrayList<OverlayItem>();

		for(MKPoiInfo info : poiList){
			mOverlayItemList.add(new OverlayItem(info.pt , info.name, info.address));
		}
		addItem(mOverlayItemList);
	}
	
	@Override
	protected boolean onTap(int index) {
		
		OverlayItem item = getItem(index);
		if(null == actItem){// 选中
			//Toast.show("选中"+index);
			
			setItemState(item, true);
			mMapView.refresh();
			actItem = item;
		}else if(item.equals(actItem)){// 取消选中
			//Toast.show("取消选中"+index);
			
			setItemState(actItem, false);
			mMapView.refresh();
			actItem = null;
		}else{//  取消选中, 选中
			// TODO 修改pop
			//Toast.show("选中:"+index+" 取消选中:");
			
			setItemState(actItem, false);
			setItemState(item, true);
			mMapView.refresh();
			actItem = item;
		}
		return super.onTap(index);
	}
	
	/**设置OverlayItem是否获取交点*/
	private void setItemState(OverlayItem item, boolean focused){
		removeItem(item);
		if(focused){
			item.setMarker(actMarkerDr);
			showPop(item);		
		}else{
			item.setMarker(markerDr);
			dismissPop();
		}
		addItem(item);
	}
	
	public void setOnPopClickListener(OnPopClickListener listener){
		if(null != listener){
			popClickListener = listener;
		}
	}
	/**清除被选中的OverlyItem的选中状态*/
	public void clearFocusedItem(){
		setItemState(actItem, false);
	}
	
	public void showPop(OverlayItem item){
		if(null == popView || null == popParams){
			popView = LayoutInflater.from(AppContext.context).inflate(R.layout.map_popview_poi, null);
			poptext = (TextView)popView.findViewById(R.id.popview_address_textview);
			popBtn = (Button)popView.findViewById(R.id.popview_right_btn);
			popBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(null != popClickListener){
						popClickListener.onClick((OverlayItem) popView.getTag());
					}
				}
			});
			popParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, item.getPoint(), 0, 0,MapView.LayoutParams.BOTTOM_CENTER );
		}else{
			mMapView.removeView(popView);
			popParams.point = item.getPoint();
		}
		popView.setTag(item);
		popParams.y = -item.getMarker().getIntrinsicHeight();
		String snippet = item.getSnippet();
		if(15<snippet.length()){
			snippet = snippet.substring(0, 14)+"...";
		}
		poptext.setText(snippet);
		mMapView.addView(popView, popParams);
	}
	public void dismissPop(){
		if(null != popView){
			popView.setTag(null);
			mMapView.removeView(popView);
		}
	}
	public void setTouchPoint(GeoPoint point,String title,String snippet){//"正在加载地址..."
		if(null == touchItem){
			touchItem = new OverlayItem(point, title, snippet);
			touchItem.setMarker(markerDr);
		}else{
			removeItem(touchItem);
			touchItem.setGeoPoint(point);
			touchItem.setTitle(title);
			touchItem.setSnippet(snippet);
		}
		//addItem(touchItem);
		setItemState(touchItem, true);
		actItem = touchItem;
		mMapView.refresh();
	}
	
	public interface OnPopClickListener {
		public void onClick(OverlayItem item);
	}
}
