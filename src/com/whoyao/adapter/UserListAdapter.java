package com.whoyao.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.model.UserListItemModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh 
 * creat_at：2013-11-12-下午3:20:56
 */
public class UserListAdapter extends BaseAdapter implements OnClickListener {
	
	
	
	private List<UserListItemModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private UserSettingManager manager = UserSettingManager.getManager();
	private boolean showPoint;
	public UserListAdapter(LayoutInflater inflater,List<UserListItemModel> list) {
		super();
		mList = list;
		this.inflater = inflater;
		loader = SmallImageAsyncLoader.getInstance();
	}

	@Override
	public int getCount() {
		if(null == mList){
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList.get(position).getUserid();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_userlist, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.ivFace = (ImageView) convertView.findViewById(R.id.user_iv_face);
			holder.tvNick = (TextView) convertView.findViewById(R.id.user_tv_nick);
			holder.tvAgeSex = (TextView) convertView.findViewById(R.id.user_tv_age_sex);
			holder.tvTag = (TextView) convertView.findViewById(R.id.user_tv_tag);
			holder.tvDistance = (TextView) convertView.findViewById(R.id.user_tv_distance);
			holder.tvInvert = (TextView) convertView.findViewById(R.id.user_tv_invert);
		}else{
			holder = (Holder) convertView.getTag();
		}
		UserListItemModel item = mList.get(position);
		loader.loadBitmap(item.getUserface(), holder.ivFace);
		holder.tvNick.setText(item.getUsername());
		holder.tvInvert.setTag(position);
		holder.tvInvert.setOnClickListener(this);
		holder.tvAgeSex.setText(CalendarUtils.getAge(item.getUserbirthday())+"");
		if(1 == item.getUsersex()){
			holder.tvAgeSex.setBackgroundResource(R.drawable.sex_man);
		}else{
			holder.tvAgeSex.setBackgroundResource(R.drawable.sex_woman);
		}
//		if(TextUtils.isEmpty(item.getUsertag())){
//			holder.tvTag.setText(R.string.hint_other_tag);
//		}else{
//		}
		holder.tvTag.setText(item.getUsertag().replace(",", " ").trim());
		if(showPoint){
			if(0 == item.getSize()){
				holder.tvDistance.setText("");
			}else if(0.1 > item.getSize()){
				holder.tvDistance.setText("<100m");
			}else if(1>item.getSize()){
				holder.tvDistance.setText((int)(item.getSize() * 1000) + "m");
			}else{
				if(item.getSize()>50){
					holder.tvDistance.setText(">50km");
				}else{
					holder.tvDistance.setText(((int)(item.getSize()*100))/100.0f +"km");
				}
			}
		}else{
			holder.tvDistance.setText("");
		}
		return convertView;
	}
	class Holder{
		public ImageView ivFace;
		public TextView tvNick;
		public TextView tvAgeSex;
		public TextView tvTag;
		public TextView tvDistance;
		public TextView tvInvert;
	}
	
	@Override
	public void onClick(View v) {
	
		UserListItemModel itemModel = mList.get((Integer) v.getTag());
		Net.request(KEY.User_ID, itemModel.getUserid() +"", WebApi.Invite.Condition, new ResponseHandler());
		UserEngine.sendInvert(itemModel.getUserid(),itemModel.getUsername());
//		Intent intent = new Intent(AppContext.curActivity, OtherDetialActivity.class);// TODO 打开邀请界面
//		intent.putExtra(Extra.SelectedID, userid);
//		AppContext.startAct(intent);
	}
	
	@Override
	public void notifyDataSetChanged() {
		showPoint = manager.isShowPoint();
		super.notifyDataSetChanged();
	}
	@Override
	public void notifyDataSetInvalidated() {
		showPoint = manager.isShowPoint();
		super.notifyDataSetInvalidated();
	}
}
