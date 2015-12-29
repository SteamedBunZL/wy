package com.whoyao.adapter;

import java.util.Comparator;
import java.util.List;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.PrivateDetailListItem;
import com.whoyao.model.PrivateListItem;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.TimeUtils;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	// public ChatAdapter(Context context, int layout,
	// Cursor c, String[] from, int[] to) {
	// super(context, layout, c, from, to);
	// }

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<PrivateDetailListItem> mList;
	private LayoutInflater inflater;
	public static int TIME = 5 * 60 * 1000;
	// 图像加载
	private ImageAsyncLoader loader;
	private OnClickListener listener;
	private String head;

	public ChatAdapter(List<PrivateDetailListItem> mList,
			LayoutInflater inflater, OnClickListener listener) {
		super();
		this.mList = mList;
		this.inflater = inflater;
		this.listener = listener;
		loader = SmallImageAsyncLoader.getInstance();

	}
	public String getHead(){
		return head;
		
	}

	public void deleteSingleItem(int position) {
		mList.remove(position);
	}

	public void addSingleItem(PrivateDetailListItem item) {
		mList.add(item);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getItemViewType(int position) {
		PrivateDetailListItem item = mList.get(position);

		if (item.isIsowner()) {
			return IMsgViewType.IMVT_TO_MSG;
		} else {
			return IMsgViewType.IMVT_COM_MSG;
		}

	}

	public void addMessage(PrivateDetailListItem item) {
		mList.add(item);
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO
		Holder holder = null;
		PrivateDetailListItem item = mList.get(position);
		if (!item.isIsowner()) {
			head = item.getHeadpicture();
		}
		boolean isComMsg = !item.isIsowner();

		if (null == convertView) {
			if (isComMsg) {
				convertView = inflater.inflate(R.layout.item_chat_from_left,
						null);
			} else {
				convertView = inflater.inflate(R.layout.item_chat_to_right,
						null);
			}
			holder = new Holder();
			holder.tvFailSend = (TextView) convertView
					.findViewById(R.id.tv_fail_send);
			holder.tvFailSend.setTag(position);
			holder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			holder.ivHead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			holder.isComMsg = isComMsg;
			holder.ivHead.setTag(R.id.iv_userhead, position);
			if (null != listener) {
				holder.ivHead.setOnClickListener(listener);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// TODO
		String pictureID = item.getHeadpicture();
		if (!TextUtils.isEmpty(pictureID)) {
			loader.loadBitmap(pictureID, holder.ivHead);
		}
		holder.tvSendTime.setText(TimeUtils.getTime(item.getEntertime()));
		// Comparator<PrivateDetailListItem> comparator = new
		// Comparator<PrivateDetailListItem>() {
		//
		// @Override
		// public int compare(PrivateDetailListItem o1,
		// PrivateDetailListItem o2) {
		// // TODO Auto-generated method stub
		// return (int) (CalendarUtils.parseDate(o1.getEntertime())-
		// CalendarUtils.parseDate(o2.getEntertime()));
		// }
		// };
		if (position > 0) {
			PrivateDetailListItem lastItem = mList.get(position - 1);
			if ((CalendarUtils.parseDate(item.getEntertime()) - CalendarUtils
					.parseDate(lastItem.getEntertime())) < TIME) {
				holder.tvSendTime.setVisibility(View.GONE);
			} else {
				holder.tvSendTime.setVisibility(View.VISIBLE);
			}
		}
		// if (item.getIsSuccess()==0) {
		holder.tvFailSend.setVisibility(View.INVISIBLE);
		// }else {
		// holder.tvFailSend.setVisibility(View.VISIBLE);
		// }
		holder.tvContent.setText(item.getContent());
		holder.tvContent
				.setMaxWidth(ClientEngine.getMobileInfo().getVgaWidth() * 1 / 2);
		return convertView;
	}

	/** Holder的持有对象 */
	public static class Holder {
		public ImageView ivHead;
		public TextView tvSendTime;
		public TextView tvContent;
		public TextView tvTitle;
		public boolean isComMsg = true;
		public TextView tvFailSend;
	}

}
