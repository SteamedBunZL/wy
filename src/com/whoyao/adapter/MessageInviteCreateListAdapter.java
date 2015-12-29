package com.whoyao.adapter;

import java.util.List;

import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventCreatListAdapter.Holder;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.InviteListItem;
import com.whoyao.model.PrivateListItem;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.TimeUtils;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MessageInviteCreateListAdapter extends BaseAdapter {
	private List<InviteListItem> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private OnClickListener listener;

	public MessageInviteCreateListAdapter(List<InviteListItem> mList,
			LayoutInflater inflater, OnClickListener listener) {
		super();
		this.mList = mList;
		this.inflater = inflater;
		this.listener = listener;
		loader = SmallImageAsyncLoader.getInstance();
	}

	public void deleteSingleItem(int position) {
		mList.remove(position);
	}

	public void deleteAll() {
		mList.clear();
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
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_my_message_private_content2, null);
			holder = new Holder();
			holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_private_title);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_private_content);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_private_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		InviteListItem item = mList.get(position);
		System.out.println("item"+item);
		holder.tvTime.setText(item.getSendusername());
		holder.tvContent.setText(item.getDescription());
		if (item.getInvitetime() != null) {
			holder.tvTime.setText(TimeUtils.getTime(item.getInvitetime()));
		}
		holder.tvName.setText(item.getSendusername());
		// 判断是否为已读，已读标题字变为黑色
		if (item.getIsread() == 2) {
			holder.tvContent.setTextColor(Color.GRAY);
		}else {
			holder.tvContent.setTextColor(Color.BLACK);
		}
		String pictureID = item.getSenduserface();
		if (!TextUtils.isEmpty(pictureID)) {
			loader.loadBitmap(pictureID, holder.ivHead);
		}
		return convertView;
	}

	/** Holder的持有对象 */
	public static class Holder {
		ImageView ivHead;
		TextView tvName;
		TextView tvContent;
		TextView tvTime;
	}

}
