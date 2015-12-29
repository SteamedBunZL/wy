package com.whoyao.adapter;

import java.util.List;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.PrivateListItem;
import com.whoyao.utils.TimeUtils;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessagePrivateCreateListAdapter extends BaseAdapter {
	private List<PrivateListItem> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private OnClickListener listener;

	public MessagePrivateCreateListAdapter(List<PrivateListItem> mList,
			LayoutInflater inflater, OnClickListener listener) {
		super();
		this.mList = mList;
		this.inflater = inflater;
		this.listener = listener;
		loader = SmallImageAsyncLoader.getInstance();
	}
	public void deleteSingleItem(int position){
		mList.remove(position);
	}
	public void deleteAll(){
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
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_private_title);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_private_content);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_private_time);
			holder.tvNum = (TextView) convertView
					.findViewById(R.id.tv_message_num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		PrivateListItem item = mList.get(position);
		System.out.println("**item*"+item);
		holder.tvTitle.setText(item.getUserName());
		holder.tvContent.setText(item.getContent());
		if (item.getSendTime()!=null) {
			holder.tvTime.setText(TimeUtils.getTime(item.getSendTime()));
		}
		if (item.getUnReadCount()>0) {
			holder.tvNum.setVisibility(View.VISIBLE);
			if(item.getUnReadCount()<100){
				holder.tvNum.setText(item.getUnReadCount()+"");
			}else{
				holder.tvNum.setText("99+");
			}
		}else {
			holder.tvNum.setVisibility(View.INVISIBLE);
		}
		String pictureID = item.getHeadPic();
		if (!TextUtils.isEmpty(pictureID)) {
			loader.loadBitmap(pictureID, holder.ivHead);
		}
		return convertView;
	}

	/** Holder的持有对象 */
	public static class Holder {
		ImageView ivHead;
		TextView tvTitle;
		TextView tvContent;
		TextView tvTime;
		TextView tvNum;
	}

}
