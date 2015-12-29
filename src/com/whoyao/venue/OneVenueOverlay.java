package com.whoyao.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.activity.EventDetialActivity;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.EventListItem;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.venue.model.SendMapMessageTModel;
import com.whoyao.venue.model.SiteVenueItem;
import com.whoyao.venue.model.VenueItemModel;

/**
 * @author hyh creat_at：2013-8-13-下午7:58:38
 */
public class OneVenueOverlay extends ItemizedOverlay<OverlayItem> {

	// private ArrayList<OverlayItem> mOverlayItemList;
	private OverlayItem actItem = null;
	private View popView;
	private Context context;
	// private ImageView btnPop;
	// private TextView tvPopTitle;
	// private TextView tvPopType;
	private TextView tvPopAddress;
	private double mLon = 116.404;
	private double mLat = 39.945;
	// private ImageView ivPopPill;
	private OverlayItem touchItem;
	private static Drawable markerDr = AppContext.getRes().getDrawable(
			R.drawable.poi_marker);
	private static Drawable actMarkerDr = AppContext.getRes().getDrawable(
			R.drawable.poi_marker_active);
	private LayoutParams popParams;
	private OnPopClickListener popClickListener;
	private Button sendButton;
	// private String[] tags;
	// private HashMap<String, EventListItem> mEventMap = new HashMap<String,
	// EventListItem>();
	private HashMap<String, SiteVenueItem> mVenueMap = new HashMap<String, SiteVenueItem>();

	// private ImageAsyncLoader loader;
	// private Drawable defaultDrawable;

	public OneVenueOverlay(MapView mapView) {
		super(markerDr, mapView);
		context = mapView.getContext();
		// tags =
		// AppContext.getRes().getStringArray(R.array.venue_service_type);
		// defaultDrawable = AppContext.getRes().getDrawable(
		// R.drawable.default_user_face);
		// loader = SmallImageAsyncLoader.getInstance();

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
	public void setData(SiteVenueItem item) {
		SiteVenueItem item2 = item;
		addItem(new OverlayItem(new GeoPoint((int) (item2.getLatitude() * 1E6),
				(int) (item2.getLongitude() * 1E6)), item2.getAddress(),
				item2.getVenueid() + ""));
		mVenueMap.put(item2.getVenueid() + "", item2);
		// addItem(new OverlayItem( new GeoPoint((int) (mLat * 1E6), (int) (mLon
		// * 1E6)), "", ""));
	}

	// 加场馆
	// public void setVenueData(ArrayList<VenueItemModel> venueList) {
	// mOverlayItemList = new ArrayList<OverlayItem>();
	//
	// for (VenueItemModel info : venueList) {
	// mOverlayItemList.add(new OverlayItem(new GeoPoint((int) (info
	// .getLatitude() * 1E6), (int) (info.getLongitude() * 1E6)),
	// info.getFullname(), info.getVenueid() + ""));
	// // mVenueMap.put(info.getVenueid() + "", info);
	// }
	// addItem(mOverlayItemList);
	// }
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
		final SiteVenueItem venue = mVenueMap.get(item.getSnippet());
		if (null == popView || null == popParams) {
			popView = LayoutInflater.from(AppContext.context).inflate(
					R.layout.map_popview_venue_item, null);
			// tvPopTitle = (TextView) popView
			// .findViewById(R.id.popview_event_tv_title);
			// 类型
			// tvPopType = (TextView) popView
			// .findViewById(R.id.popview_event_tv_time);
			// 地址
			tvPopAddress = (TextView) popView
					.findViewById(R.id.popview_event_tv_num);
			sendButton = (Button) popView.findViewById(R.id.btn_send_message);
			// ivPopPill = (ImageView) popView
			// .findViewById(R.id.popview_event_iv_pill);
			// btnPop = (ImageView) popView
			// .findViewById(R.id.popview_event_iv_detial);

			sendButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Intent intent = new Intent(AppContext.curActivity,
					// EventDetialActivity.class);
					// intent.putExtra(
					// Extra.SelectedID,
					// mEventMap.get(
					// ((OverlayItem) popView.getTag())
					// .getSnippet()).getActivityid());
					// AppContext.startAct(intent);
					final MessageDialog showMessage = new MessageDialog(context);
					showMessage.setTitle("请输入手机号码");
					showMessage.setEditText();
//					showMessage.getPhoneNumber();
					showMessage.setLeftButton("取消", null);
					showMessage.setRightButton("确定", new OnClickListener() {

						@Override
						public void onClick(View v) {
							String phone_number = showMessage.getPhoneNumber();
							//创建电话管理
//							TelephonyManager tm = (TelephonyManager)
//							//与手机建立连接
//							mMapView.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//							//获取手机号码
//							
//							String phoneId = tm.getLine1Number();
//							Toast.show(phoneId);
							if (phone_number.equals("")) {
								Toast.show("号码不能为空");
							} else {
								// SmsManager smsManager =
								// SmsManager.getDefault();
								// if (venue.getAddress().length() > 70) {
								// List<String> contents = smsManager
								// .divideMessage(venue.getAddress());
								// for (String sms : contents) {
								// smsManager.sendTextMessage(
								// phone_number, null, sms, null,
								// null);
								// }
								// } else {
								// smsManager.sendTextMessage(phone_number,
								// null, venue.getAddress(), null,
								// null);
								// }
								SendMapMessageTModel tModel = new SendMapMessageTModel();
								tModel.setVenueid(mVenueMap.get(
										((OverlayItem) popView.getTag())
												.getSnippet()).getVenueid());
								tModel.setPhonenumber(phone_number);
								Net.request(tModel, WebApi.Venue.SendMapMessage,new ResponseHandler(){

									@Override
									public void onSuccess(String content) {
										super.onSuccess(content);
										Toast.show("发送成功");
									}

									@Override
									public void onFailure(Throwable e,
											int statusCode, String content) {
										super.onFailure(e, statusCode, content);
										Toast.show("发送失败");
									}
									
								});
							}
						}
					});
					showMessage.show();
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

		if (null != venue) {
			// tvPopTitle.setText(venue.getFullname());
			// tvPopType.setText(tags[venue.getServicetype()[0] - 1] + " "
			// + tags[venue.getServicetype()[1] - 1]);
			tvPopAddress.setText(venue.getAddress());
			// ivPopPill.setImageDrawable(defaultDrawable);
			// loader.loadBitmap(venue.getLogourl(), ivPopPill);
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
