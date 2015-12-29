package com.whoyao.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.BillImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.map.DistanceUtils;
import com.whoyao.model.EventListItem;
import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh 
 * creat_at：2013-9-13-上午10:38:08
 */
public class EventListAdapter extends BaseAdapter{
	
	private List<EventListItem> mList;
	private LayoutInflater inflater;
	private String[] types;
	private ImageAsyncLoader loader;
	private double locLon;
	private double locLat;

	public EventListAdapter(List<EventListItem> list,LayoutInflater inflater) {
		super();
		mList = list;
		this.inflater = inflater;
		types = AppContext.getRes().getStringArray(R.array.event_tags);
		loader = BillImageAsyncLoader.getInstance();
		if(null != AppContext.location){
			locLon = AppContext.location.getLongitude();
			locLat = AppContext.location.getLatitude();
//			loc = Utils.getGeoPoint(AppContext.location.getLatitude(), AppContext.location.getLongitude());
		}
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
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_event_list2, null);
			holder = new Holder();
			holder.ivLabel = (ImageView) convertView.findViewById(R.id.item_event_iv_label);
			holder.ivPill = (ImageView) convertView.findViewById(R.id.item_event_iv_pill);
			holder.tvType = (TextView) convertView.findViewById(R.id.item_event_tv_type);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_event_tv_title);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_event_tv_time);
			holder.tvDistance = (TextView) convertView.findViewById(R.id.item_event_tv_distance);
			holder.tvAddr = (TextView) convertView.findViewById(R.id.item_event_tv_addr);
			holder.tvKeyword = (TextView) convertView.findViewById(R.id.item_event_tv_keyword);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		EventListItem item = mList.get(position);
		holder.tvType.setText("["+types[item.getTypeid()-1]+"]");
		holder.tvTitle.setText(item.getTitle());
		holder.tvTime.setText(CalendarUtils.formatYMDHM(item.getBegintime()) );

		holder.tvAddr.setText(item.getAddress());
		holder.tvKeyword.setText(item.getKeyword());
		String pictureID = item.getActivitypicture();
		if(null != AppContext.location || (int)item.getLongitude()==0 || (int)item.getLatitude()==0){
			holder.tvDistance.setText("");
		}else{
			int distance = (int) DistanceUtils.GetLongDistance(locLon, locLat, item.getLongitude(), item.getLatitude());
			if(1000 <= distance){
				distance /= 10;
				if ((double)(distance/100)>50) {
					holder.tvDistance.setText(">50km");
				}else {
					holder.tvDistance.setText((double)distance/100 + "km");
				}
			}else{
				if(100 > distance){
					distance = 100;
				}
				holder.tvDistance.setText(distance + "m");
			}
		}
		if(!TextUtils.isEmpty(pictureID)){
			String url = WebApi.getImageUrl(pictureID, WebApi.ImageDemen_120_90);
			loader.loadBitmap(url, holder.ivPill);
		}
		if(1 == item.getActivitycategory()){
			holder.ivLabel.setVisibility(View.VISIBLE);
		}else{
			holder.ivLabel.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public static class Holder{
		public ImageView ivLabel;
		public ImageView ivPill;
		public TextView tvType;
		public TextView tvTitle;
		public TextView tvTime;
		public TextView tvDistance;
		public TextView tvAddr;
		public TextView tvKeyword;
	}
	
	
}