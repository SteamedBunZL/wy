package com.whoyao.adapter;

import java.util.List;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.TopicRemarkModel;
import com.whoyao.model.TopicRemarkRModel;
import com.whoyao.utils.TimeUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicRemarkAdapter extends BaseAdapter implements OnClickListener {
	private List<TopicRemarkModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private static final int TagKey = 1000;
	public TopicRemarkAdapter(LayoutInflater inflater, List<TopicRemarkModel> list) {
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
		return mList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_topic_remark_list, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.ivFace = (ImageView) convertView.findViewById(R.id.item_topic_remark_iv_face);
			holder.tvNick = (TextView) convertView.findViewById(R.id.item_topic_remark_tv_nick);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_topic_remark_tv_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.item_topic_remark_tv_content);
			holder.ivFace.setOnClickListener(this);
			holder.tvNick.setOnClickListener(this);

		} else {
			holder = (Holder) convertView.getTag();
		}
		TopicRemarkModel item = mList.get(position);
		holder.ivFace.setTag(R.id.item_topic_remark_iv_face, item.getUserid());
		loader.loadBitmap(item.getUserface(), holder.ivFace);
		holder.tvNick.setText(item.getUsername());
		holder.tvTime.setText(TimeUtils.getTime(item.getTime()));
		holder.tvContent.setText(item.getContent());
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_topic_remark_tv_nick:
		case R.id.item_topic_remark_iv_face:
			UserEngine.startUserDetial((Integer) v.getTag(R.id.item_topic_remark_iv_face));
			break;
		default:
			break;
		}
	}

	class Holder {
		public ImageView ivFace;
		public TextView tvNick;
		public TextView tvTime;
		public TextView tvContent;
	}

}
