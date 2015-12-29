package com.whoyao.venue.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventListAdapter.Holder;
import com.whoyao.common.BillImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.map.DistanceUtils;
import com.whoyao.model.EventListItem;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.TimeUtils;
import com.whoyao.venue.VenueDetialActivity;
import com.whoyao.venue.model.PlaceList;
import com.whoyao.venue.model.SiteVenueItem;

/**
 * @author hyh creat_at：2013-9-13-上午10:38:08
 */
public class SiteListAdapter extends BaseAdapter {

	private List<SiteVenueItem> mList;
	private LayoutInflater inflater;
	private boolean isShowMore = false;
	private OnClickListener listener;
	private Context context;

	public SiteListAdapter(List<SiteVenueItem> list, LayoutInflater inflater,
			OnClickListener listener,Context context) {
		super();
		mList = list;
		this.inflater = inflater;
		this.listener = listener;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		int itemNum = 0;
		// LinearLayout ll;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_site_item, null);
			holder = new Holder();
			holder.tvVenueName = (TextView) convertView
					.findViewById(R.id.item_tv_site_venuename);
			holder.tvDistance = (TextView) convertView
					.findViewById(R.id.item_tv_site_distances);
			holder.tvAddr = (TextView) convertView
					.findViewById(R.id.item_tv_site_address);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.item_tv_site_price);
			holder.llContent = (LinearLayout) convertView
					.findViewById(R.id.ll_convertview);
			if (null != listener) {
				// holder.ivInformHead.setOnClickListener(listener);
				holder.tvAddr.setOnClickListener(listener);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tvAddr.setTag(position);
		final SiteVenueItem item = mList.get(position);
		itemNum = item.getPlacelist().size();
		float distance = item.getDistance();
		holder.tvVenueName.setText(item.getFullname());
		holder.tvPrice.setText("￥" + item.getPlaceprice() + "/时");
		holder.tvAddr.setText(item.getAddress());
		holder.tvDistance.setText(getDistance(item.getDistance()));
		
		// if (null != AppContext.location || (int) item.getLongitude() == 0
		// || (int) item.getLatitude() == 0) {
		// holder.tvDistance.setText("");
		// } else {
		// if (1000 <= distance) {
		// distance /= 10;
		// if ((double) (distance / 100) > 50) {
		// holder.tvDistance.setText(">50km");
		// } else {
		// holder.tvDistance.setText((double) distance / 100 + "km");
		// }
		// } else {
		// if (100 > distance) {
		// distance = 100;
		// }
		// holder.tvDistance.setText(distance + "m");
		// }
		// }
		if (itemNum > 5) {
			isShowMore = true;
			itemNum = 5;
		} else {
			itemNum = item.getPlacelist().size();
			isShowMore = false;
		}
		holder.llContent.removeAllViews();
		ArrayList<PlaceList> placeList = item.getPlacelist();
		for (int i = 0; i < itemNum; i++) {
			RelativeLayout rl = null;
			rl = (RelativeLayout) inflater.inflate(
					R.layout.item_rl_site_single, null);
			TextView tvDate = (TextView) rl.findViewById(R.id.tv_left_1);
			tvDate.setText(CalendarUtils.formatYMD(item.getPlacelist().get(i)
					.getPlacedate()));
			TextView tvTime = (TextView) rl.findViewById(R.id.tv_time);
			int placeTime = item.getPlacelist().get(i).getPlacetime();
//			String newTime = "0" + placeTime;
//			String newTiem2 = "0" + (placeTime + 1);
//			if (placeTime < 23) {
//				if (newTime.length() > 3) {
//					newTime.substring(1, newTime.length());
//				} else if (newTiem2.length() > 3) {
//					newTiem2 = newTiem2.substring(1, newTiem2.length());
//
//				}
//				tvTime.setText(newTime + ":00" + "-" + newTiem2 + ":00");
//			} else {
//
//			}
			tvTime.setText(VenueEngine.getPlaceTime(placeTime));
			if (i > 0) {
				if (placeList.get(i).getPlacedate()
						.equals(placeList.get(i - 1).getPlacedate())) {
					tvDate.setVisibility(View.INVISIBLE);
				} else {
					tvDate.setVisibility(View.VISIBLE);
				}
			}
			TextView tvCount = (TextView) rl.findViewById(R.id.tv_right_1);
			tvCount.setText("空闲" + item.getPlacelist().get(i).getCount() + "场");
			rl.setVisibility(View.VISIBLE);
			View view = rl.findViewById(R.id.line_1);
			if (i < itemNum - 1) {
				if (placeList.get(i).getPlacedate()
						.equals(placeList.get(i + 1).getPlacedate())) {
					if (i == 4) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.INVISIBLE);
					}
				} else {
					view.setVisibility(View.VISIBLE);
				}
			} else if (isShowMore) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.INVISIBLE);
			}
			holder.llContent.addView(rl);
			if (i == 4 && isShowMore) {
				RelativeLayout rlShow = null;
				rlShow = (RelativeLayout) inflater.inflate(
						R.layout.item_is_show_more, null);
				RelativeLayout rl2 = (RelativeLayout) rlShow
						.findViewById(R.id.rl_show_more);
//				rl2.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						int venueid = item.getVenueid();
//						Intent intent = new Intent(context,
//								VenueDetialActivity.class);
//						context.startActivity(intent);
//					}
//				});
				rl2.setTag(position);
				rl2.setOnClickListener(listener);
				holder.llContent.addView(rlShow);
			}
		}

		return convertView;
	}
	private String getDistance(float distance) {
		String distanceStr = "";
		if (0 != distance) {
			if (0.1 > distance) {
				distanceStr = "<100m";
			} else if (1 > distance) {
				distanceStr = (int) (distance * 1000) + "m";
			} else if (distance > 50) {
				distanceStr = ">50km";
			} else {
				distanceStr = ((int) (distance * 100)) / 100.0f + "km";
			}
		}
		return distanceStr;
	}


	public static class Holder {
		public TextView tvVenueName;
		public TextView tvPrice;
		public TextView tvDistance;
		public TextView tvAddr;
		public LinearLayout llContent;
	}

}