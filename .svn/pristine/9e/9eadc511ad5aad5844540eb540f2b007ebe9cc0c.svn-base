package com.whoyao.adapter;

import java.util.List;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.engine.user.UserSettingManager;
import com.whoyao.model.UserListItemModel;
import com.whoyao.utils.CalendarUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-11-12-下午3:20:56
 */
public class UserFriendListAdapter extends BaseAdapter {

	private List<UserListItemModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private UserSettingManager manager = UserSettingManager.getManager();
	private boolean showPoint;

	public UserFriendListAdapter(LayoutInflater inflater,
			List<UserListItemModel> list) {
		super();
		mList = list;
		this.inflater = inflater;
		loader = SmallImageAsyncLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (null == mList) {
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
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_user_friendlist, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.ivFace = (ImageView) convertView
					.findViewById(R.id.user_iv_face);
			holder.ivFree = (ImageView) convertView
					.findViewById(R.id.user_iv_free);
			holder.ivMobile = (ImageView) convertView
					.findViewById(R.id.user_iv_mobile);
			holder.ivHonesty = (ImageView) convertView
					.findViewById(R.id.user_iv_honesty);
			holder.tvNick = (TextView) convertView
					.findViewById(R.id.user_tv_nick);
			holder.tvAgeSex = (TextView) convertView
					.findViewById(R.id.user_tv_age_sex);
			holder.tvTag = (TextView) convertView
					.findViewById(R.id.user_tv_tag);
			holder.tvDistance = (TextView) convertView
					.findViewById(R.id.user_tv_distance);
		} else {
			holder = (Holder) convertView.getTag();
		}
		UserListItemModel item = mList.get(position);
		loader.loadBitmap(item.getUserface(), holder.ivFace);
		holder.tvNick.setText(item.getUsername());
		holder.tvAgeSex.setText(CalendarUtils.getAge(item.getUserbirthday())
				+ "");
		if (1 == item.getUsersex()) {
			holder.tvAgeSex.setBackgroundResource(R.drawable.sex_man);
		} else {
			holder.tvAgeSex.setBackgroundResource(R.drawable.sex_woman);
		}

		holder.tvTag.setText(item.getUsertag().replace(",", " ").trim());

		holder.ivFree.setSelected(item.isFreetime());
		holder.ivMobile.setSelected(item.isMobileV());
		holder.ivHonesty.setSelected(item.isHonestyV());
		if (showPoint) {
			if (0.1 > item.getSize()) {
				holder.tvDistance.setText("100m");
			} else if (1 > item.getSize()) {
				holder.tvDistance.setText((int) (item.getSize() * 1000) + "m");
			} else {
				holder.tvDistance.setText(((int) (item.getSize() * 100)) / 100
						+ "km");
			}
		} else {
			holder.tvDistance.setText("");
		}
		return convertView;
	}

	class Holder {
		public ImageView ivHonesty;
		public ImageView ivMobile;
		public ImageView ivFree;
		public ImageView ivFace;
		public TextView tvNick;
		public TextView tvAgeSex;
		public TextView tvTag;
		public TextView tvDistance;
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
