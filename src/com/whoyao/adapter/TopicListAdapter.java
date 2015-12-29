package com.whoyao.adapter;

import java.util.List;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.utils.TimeUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicListAdapter extends BaseAdapter implements OnClickListener {
	private List<TopicItemRModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;

	public TopicListAdapter(LayoutInflater inflater, List<TopicItemRModel> list) {
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
		return mList.get(position).getTopicid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_topic_list, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.ivFace = (ImageView) convertView.findViewById(R.id.iv_userface);
			holder.ivFace.setOnClickListener(this);
			holder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
			holder.tvTag.setLines(2);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);

		} else {
			holder = (Holder) convertView.getTag();
		}
		TopicItemRModel item = mList.get(position);
		loader.loadBitmap(item.getUserface(), holder.ivFace);
		holder.ivFace.setTag(R.id.iv_userface, item.getUserid());
		holder.tvTag.setText("[" + item.getTag() + "]" + item.getTitle());
		holder.tvTime.setText(TimeUtils.getTime(item.getTime()));
		holder.tvCount.setText(item.getCount() + "人回复");
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_userface:
			UserEngine.startUserDetial((Integer) v.getTag(R.id.iv_userface));
			break;
		default:
			break;
		}
	}

	class Holder {
		public ImageView ivFace;
		public TextView tvTag;
		public TextView tvTime;
		public TextView tvCount;
	}

}
