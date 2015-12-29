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
import com.whoyao.model.EventListItem;
import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh 
 * creat_at：2013-9-13-上午10:38:08
 */
public class EventListOtherAdapter extends BaseAdapter{
	
	private List<EventListItem> mList;
	private LayoutInflater inflater;
	private String[] types;
	private ImageAsyncLoader loader;
	public EventListOtherAdapter(List<EventListItem> list,LayoutInflater inflater) {
		super();
		mList = list;
		this.inflater = inflater;
		types = AppContext.getRes().getStringArray(R.array.event_tags);
		loader = BillImageAsyncLoader.getInstance();
		if(null != AppContext.location){
			AppContext.location.getLongitude();
			AppContext.location.getLatitude();
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
			convertView = inflater.inflate(R.layout.item_event_list_other, null);
			holder = new Holder();
//			holder.ivLabel = (ImageView) convertView.findViewById(R.id.item_event_iv_label);
			holder.ivPill = (ImageView) convertView.findViewById(R.id.item_event_iv_pill);
			holder.tvType = (TextView) convertView.findViewById(R.id.item_event_tv_type);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_event_tv_title);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_event_tv_time);
			holder.tvJoinNum = (TextView) convertView.findViewById(R.id.item_event_tv_join_num);
			holder.tvPvNum = (TextView) convertView.findViewById(R.id.item_event_tv_pv_num);
			holder.tvRemarkNum = (TextView) convertView.findViewById(R.id.item_event_tv_remark_num);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		EventListItem item = mList.get(position);
		holder.tvType.setText("["+types[item.getTypeid()-1]+"]");
		holder.tvTitle.setText(item.getTitle());
		holder.tvTime.setText(CalendarUtils.formatYMDHM(item.getBegintime()));
		holder.tvJoinNum.setText("参加("+item.getJoinnum()+")");
		holder.tvPvNum.setText("浏览("+item.getPv()+")");
		holder.tvRemarkNum.setText("交流("+item.getCommentnum()+")");

		String pictureID = item.getActivitypicture();
		holder.ivPill.setImageDrawable(null);
		if(!TextUtils.isEmpty(pictureID)){
			String url = WebApi.getImageUrl(pictureID, WebApi.ImageDemen_120_90);
			loader.loadBitmap(url, holder.ivPill);
		}
//		if(0 == position && 4 == item.getActivityprogressstate() && AppContext.serviceTimeMillis() < CalendarUtils.parseDate(item.getBegintime())){
//			holder.ivLabel.setVisibility(View.VISIBLE);
//		}else{
//			holder.ivLabel.setVisibility(View.GONE);
//		}
		return convertView;
	}
	
	public static class Holder{
		public TextView tvRemarkNum;
		public TextView tvPvNum;
		public TextView tvJoinNum;
		public ImageView ivLabel;
		public ImageView ivPill;
		public TextView tvType;
		public TextView tvTitle;
		public TextView tvTime;
	}
	
	
}