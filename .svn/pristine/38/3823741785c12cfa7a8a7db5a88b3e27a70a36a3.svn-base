package com.whoyao.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class EventJoinListAdapter extends BaseAdapter {
	
	private List<EventListItem> mList;
	private LayoutInflater inflater;
	private String[] types;
	private String[] states = AppContext.getRes().getStringArray(R.array.event_states_enum);
	private String[] progress = AppContext.getRes().getStringArray(R.array.event_progress_enum);
	private int[] colors;
	private ImageAsyncLoader loader;
	private OnClickListener listener;

	public EventJoinListAdapter(List<EventListItem> list,LayoutInflater inflater,OnClickListener listener) {
		super();
		mList = list;
		this.inflater = inflater;
		this.listener = listener;
		if(null == types){
			types = AppContext.getRes().getStringArray(R.array.event_tags);
		}
		if(null == colors){
			colors = AppContext.getRes().getIntArray(R.array.event_prog_color);
		}
		loader = BillImageAsyncLoader.getInstance();
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
		int btnCount = 3;
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_event_creat_list, null);
			holder = new Holder();
			holder.ivPill = (ImageView) convertView.findViewById(R.id.item_event_iv_pill);
			holder.tvType = (TextView) convertView.findViewById(R.id.item_event_tv_type);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_event_tv_title);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_event_tv_time);
			holder.tvProg = (TextView) convertView.findViewById(R.id.item_event_tv_progress);
			holder.tvCategory = (TextView) convertView.findViewById(R.id.item_event_tv_category);
			holder.tvState = (TextView) convertView.findViewById(R.id.item_event_tv_state);
			holder.tvNum = (TextView) convertView.findViewById(R.id.item_event_tv_num);
			holder.tvJoinerMgr = (TextView) convertView.findViewById(R.id.item_event_tv_joiner_mgr);
			holder.vJoinerL0 = convertView.findViewById(R.id.item_event_v_joiner_0);
			holder.vJoinerL1 = convertView.findViewById(R.id.item_event_v_joiner_1);
			holder.tvHide = (TextView) convertView.findViewById(R.id.item_event_tv_hide);
			holder.vHideL0 = convertView.findViewById(R.id.item_event_v_hide_0);
			holder.vHideL1 = convertView.findViewById(R.id.item_event_v_hide_1);
			holder.vValuL0 = convertView.findViewById(R.id.item_event_v_valu_0);
			holder.vValuL1 = convertView.findViewById(R.id.item_event_v_value_1);
			holder.tvUpPhoto = (TextView) convertView.findViewById(R.id.item_event_tv_upload_photo);
			holder.tvValu = (TextView) convertView.findViewById(R.id.item_event_tv_valu);
			holder.vInfill0 = convertView.findViewById(R.id.item_event_v_infill_0);
			holder.vInfill1 = convertView.findViewById(R.id.item_event_v_infill_1);
			holder.vInfill2 = convertView.findViewById(R.id.item_event_v_infill_2);
			holder.vBtnGroup = convertView.findViewById(R.id.item_event_v_btn_group);
						
			holder.tvJoinerMgr.setVisibility(View.GONE);
			holder.vJoinerL0.setVisibility(View.GONE);
			holder.vJoinerL1.setVisibility(View.GONE);
			holder.vInfill0.setVisibility(View.VISIBLE);
			holder.vValuL0.setVisibility(View.VISIBLE);
			holder.vValuL1.setVisibility(View.VISIBLE);

			if(null != listener){
				holder.tvHide.setOnClickListener(listener);
				holder.tvUpPhoto.setOnClickListener(listener);
				holder.tvValu.setOnClickListener(listener);
			}
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.tvHide.setTag(position);
		holder.tvUpPhoto.setTag(position);
		holder.tvValu.setTag(position);

		EventListItem item = mList.get(position);
		holder.tvType.setText("["+types[item.getTypeid()-1]+"]");
		holder.tvTitle.setText(item.getTitle());
		
		holder.tvTime.setText(CalendarUtils.formatYMDHM(item.getBegintime()));//日期格式
		if(1 == item.getActivitycategory()){
			holder.tvCategory.setText("即时活动");
			holder.tvHide.setVisibility(View.VISIBLE);
			holder.vHideL0.setVisibility(View.VISIBLE);
			holder.vHideL1.setVisibility(View.VISIBLE);
			holder.vInfill1.setVisibility(View.GONE);
			if(0 == item.getActivityhidestatus()){
				holder.tvHide.setText("关闭展示");
			}else{
				holder.tvHide.setText("开启展示");
			}
		}else{
			holder.tvCategory.setText("普通活动");
			holder.tvHide.setVisibility(View.GONE);
			--btnCount;
			holder.vHideL0.setVisibility(View.GONE);
			holder.vHideL1.setVisibility(View.GONE);
			holder.vInfill1.setVisibility(View.VISIBLE);
		}
		int state = item.getActivitystate();
		int prog = item.getActivityprogressstate();
		
		//0：未知 1：未达成2：已达成3：已取消
		holder.tvState.setText(states[state]);
		//0：未知 1：即将开始 2：进行中 3：已结束 4：报名中
		if(1 == state || 3 == state){
			holder.tvProg.setText(progress[0]);
		}else{
			holder.tvProg.setText(progress[prog]);
			holder.tvProg.setTextColor(colors[prog]);
		}
		if(state == 0 && prog == 4){
			holder.tvState.setText(states[4]);
		}
		holder.tvNum.setText(item.getJoinnum()+"人参加　"+item.getPhotonum()+"张照片");
		//加载海报
		String pictureID = item.getActivitypicture();
		if(!TextUtils.isEmpty(pictureID)){
			String url = WebApi.getImageUrl(pictureID, WebApi.ImageDemen_120_90);
			loader.loadBitmap(url, holder.ivPill);
		}
		//活动达成且已结束,可以评分
		if(2 == state && 3 == prog){
			holder.tvValu.setVisibility(View.VISIBLE);
			holder.vInfill2.setVisibility(View.GONE);
			holder.vValuL0.setVisibility(View.VISIBLE);
			holder.vValuL1.setVisibility(View.VISIBLE);
			if(1 == item.getActivityevaluationvalue()){
				holder.tvValu.setText("已评分");
			}else{
				holder.tvValu.setText("评分");
			}
		}else{
			holder.tvValu.setText("");
			--btnCount;
			holder.vValuL0.setVisibility(View.GONE);
			holder.vValuL1.setVisibility(View.GONE);
		}
		//上传照片
		if(2 == state && (2==prog || 3==prog)){
			holder.tvUpPhoto.setVisibility(View.VISIBLE);
			holder.vInfill2.setVisibility(View.GONE);
		}else{
			holder.tvUpPhoto.setVisibility(View.GONE);
			--btnCount;
			holder.vInfill2.setVisibility(View.VISIBLE);
		}
		if(1> btnCount){
			holder.vBtnGroup.setVisibility(View.GONE);
		}else{
			holder.vBtnGroup.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	public static class Holder{
		public View vBtnGroup;
		public View vValuL1;
		public View vValuL0;
		public View vInfill2;
		public TextView tvHide;
		public View vInfill1;
		public View vInfill0;
		public TextView tvValu;
		public TextView tvUpPhoto;
		public View vHideL1;
		public View vHideL0;
		public View vJoinerL1;
		public View vJoinerL0;
		public TextView tvJoinerMgr;
		public TextView tvProg;
		public ImageView ivPill;
		public TextView tvType;
		public TextView tvTitle;
		public TextView tvTime;
		public TextView tvCategory;
		public TextView tvState;
		public TextView tvNum;
	}
	
}