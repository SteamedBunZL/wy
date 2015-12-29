package com.whoyao.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.activity.EventDetialActivity;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.EventListItem;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.venue.model.SiteVenueItem;
import com.whoyao.venue.model.VenueItemModel;

/**
 * @author hyh creat_at：2013-8-13-下午7:58:38
 */
public class SiteOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlayItemList;
	private OverlayItem actItem = null;
	private View popView;
	private ImageView btnPop;
	private TextView tvPopTitle;
	private TextView tvPopType;
	private TextView tvPopAddress;
	private ImageView ivPopPill;
	private OverlayItem touchItem;
	private static Drawable markerDr = AppContext.getRes().getDrawable(
			R.drawable.poi_marker);
	private static Drawable actMarkerDr = AppContext.getRes().getDrawable(
			R.drawable.poi_marker_active);
	private LayoutParams popParams;
	private OnPopClickListener popClickListener;
	private String[] tags;
	private HashMap<String, EventListItem> mEventMap = new HashMap<String, EventListItem>();
	private HashMap<String, VenueItemModel> mVenueMap = new HashMap<String, VenueItemModel>();
	private HashMap<String, SiteVenueItem> mSiteMap = new HashMap<String, SiteVenueItem>();
	private List<Integer> types = new ArrayList<Integer>();

	private ImageAsyncLoader loader;
	private Drawable defaultDrawable;

	public SiteOverlay(MapView mapView) {
		super(markerDr, mapView);
		tags = AppContext.getRes().getStringArray(R.array.venue_service_type);
		defaultDrawable = AppContext.getRes().getDrawable(
				R.drawable.default_user_face);
		loader = SmallImageAsyncLoader.getInstance();

	}

	// public void setData(ArrayList<MKPoiInfo> poiList){
	//
	// mOverlayItemList = new ArrayList<OverlayItem>();
	//
	// for(MKPoiInfo info : poiList){
	// mOverlayItemList.add(new OverlayItem(info.pt , info.name, info.address));
	// }
	// addItem(mOverlayItemList);
	// }
	public void setData(ArrayList<EventListItem> eventList) {
		mOverlayItemList = new ArrayList<OverlayItem>();

		for (EventListItem info : eventList) {
			mOverlayItemList.add(new OverlayItem(new GeoPoint((int) (info
					.getLatitude() * 1E6), (int) (info.getLongitude() * 1E6)),
					info.getTitle(), info.getActivityid() + ""));
			mEventMap.put(info.getActivityid() + "", info);
		}
		addItem(mOverlayItemList);
	}

	// 加场馆
	public void setVenueData(ArrayList<VenueItemModel> venueList) {
		mOverlayItemList = new ArrayList<OverlayItem>();

		for (VenueItemModel info : venueList) {
			mOverlayItemList.add(new OverlayItem(new GeoPoint((int) (info
					.getLatitude() * 1E6), (int) (info.getLongitude() * 1E6)),
					info.getFullname(), info.getVenueid() + ""));
			mVenueMap.put(info.getVenueid() + "", info);
		}
		addItem(mOverlayItemList);
	}

	// 加场地
	public void setSiteData(ArrayList<SiteVenueItem> siteList) {
		mOverlayItemList = new ArrayList<OverlayItem>();

		for (SiteVenueItem info : siteList) {
			mOverlayItemList.add(new OverlayItem(new GeoPoint((int) (info
					.getLatitude() * 1E6), (int) (info.getLongitude() * 1E6)),
					info.getFullname(), info.getVenueid() + ""));
			mSiteMap.put(info.getVenueid() + "", info);
		}
		addItem(mOverlayItemList);
	}

	@Override
	protected boolean onTap(int index) {

		OverlayItem item = getItem(index);
		if (null == actItem) {// 选中
			// Toast.show("选中"+index);

			setItemState(item, true);
			mMapView.refresh();
			actItem = item;
		} else if (item.getSnippet().equals(actItem.getSnippet())) {// 取消选中
			// Toast.show("取消选中"+index);
			setItemState(actItem, false);
			mMapView.refresh();
			actItem = null;
		} else {// 取消选中, 选中
				// TODO 修改pop
				// Toast.show("选中:"+index+" 取消选中:");

			setItemState(actItem, false);
			setItemState(item, true);
			mMapView.refresh();
			actItem = item;
		}
		return super.onTap(index);
	}

	/** 设置OverlayItem是否获取交点 */
	private void setItemState(OverlayItem item, boolean focused) {
		removeItem(item);
		if (focused) {
			item.setMarker(actMarkerDr);
			showPop(item);
		} else {
			item.setMarker(markerDr);
			dismissPop();
		}
		addItem(item);
	}

	public void setOnPopClickListener(OnPopClickListener listener) {
		if (null != listener) {
			popClickListener = listener;
		}
	}

	/** 清除被选中的OverlyItem的选中状态 */
	public void clearFocusedItem() {
		setItemState(actItem, false);
	}

	public void showPop(OverlayItem item) {
		if (null == popView || null == popParams) {
			popView = LayoutInflater.from(AppContext.context).inflate(
					R.layout.map_popview_event_info, null);
			tvPopTitle = (TextView) popView
					.findViewById(R.id.popview_event_tv_title);
			// 类型
			tvPopType = (TextView) popView
					.findViewById(R.id.popview_event_tv_time);
			// 地址
			tvPopAddress = (TextView) popView
					.findViewById(R.id.popview_event_tv_num);
			ivPopPill = (ImageView) popView
					.findViewById(R.id.popview_event_iv_pill);
			btnPop = (ImageView) popView
					.findViewById(R.id.popview_event_iv_detial);
			popView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AppContext.curActivity,
							VenueDetialActivity.class);
					intent.putExtra(
							Extra.SelectedID,
							mSiteMap.get(
									((OverlayItem) popView.getTag())
											.getSnippet()).getVenueid());
					intent.putExtra(
							Extra.SelectedItemStr,
							mSiteMap.get(
									((OverlayItem) popView.getTag())
											.getSnippet()).getFullname());
					AppContext.startAct(intent);
					if (null != popClickListener) {
						popClickListener.onClick((OverlayItem) popView.getTag());
					}
				}
			});
			popParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, item.getPoint(), 0, 0,
					MapView.LayoutParams.BOTTOM_CENTER);
		} else {
			mMapView.removeView(popView);
			popParams.point = item.getPoint();
		}
		popView.setTag(item);
		popParams.y = -item.getMarker().getIntrinsicHeight();
		// EventListItem event = mEventMap.get(item.getSnippet());
		SiteVenueItem site = mSiteMap.get(item.getSnippet());
		if (null != site) {
			tvPopTitle.setText(site.getFullname());
			int length = site.getServicetype().size();
			if (length>=3) {
				types.clear();
				for (int i = 0; i < 3; i++) {
					types.add(site.getServicetype().get(i).getServicetypetype());
				}
				tvPopType.setText(tags[types.get(0)-1] + " "
						+ tags[types.get(1)-1]);
			}else {
				types.clear();
				for (int i = 0; i < length; i++) {
					types.add(site.getServicetype().get(i).getServicetypetype());
				}
				tvPopType.setText(tags[types.get(0)-1] + " ");
			}
			tvPopAddress.setText(site.getAddress());
			ivPopPill.setImageDrawable(defaultDrawable);
			loader.loadBitmap(site.getLogourl(), ivPopPill);
			mMapView.addView(popView, popParams);
		}
	}

	public void dismissPop() {
		if (null != popView) {
			popView.setTag(null);
			mMapView.removeView(popView);
		}
	}

	// public void setTouchPoint(GeoPoint point,String title,String
	// snippet){//"正在加载地址..."
	// if(null == touchItem){
	// touchItem = new OverlayItem(point, title, snippet);
	// touchItem.setMarker(markerDr);
	// }else{
	// removeItem(touchItem);
	// touchItem.setGeoPoint(point);
	// touchItem.setTitle(title);
	// touchItem.setSnippet(snippet);
	// }
	// //addItem(touchItem);
	// setItemState(touchItem, true);
	// actItem = touchItem;
	// mMapView.refresh();
	// }

	public interface OnPopClickListener {
		public void onClick(OverlayItem item);
	}
}
