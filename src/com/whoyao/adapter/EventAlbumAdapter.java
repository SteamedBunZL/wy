package com.whoyao.adapter;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.model.EventPhotoModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2013-9-16-上午11:41:21
 */
public class EventAlbumAdapter extends BaseAdapter {
	
	private List<EventPhotoModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;

	public EventAlbumAdapter(List<EventPhotoModel> list) {
		super();
		mList = list;
		inflater = LayoutInflater.from(AppContext.context);
		loader = OriginalImageAsyncLoader.getInstance();
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
		return mList.get(position).getPhotoid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h = null;
		if(null == convertView){
			h = new Holder();
			convertView = inflater.inflate(R.layout.item_event_album, null);
			convertView.setTag(h);
			h.iv = (ImageView) convertView.findViewById(R.id.item_event_album_iv);
			h.tv = (TextView) convertView.findViewById(R.id.item_event_album_tv);
		}else{
			h = (Holder) convertView.getTag();
		}
		EventPhotoModel item = mList.get(position);
		
		String imageUrl = WebApi.getImageUrl(item.getPhotopath(), WebApi.ImageDemen_240_180);
		loader.loadBitmap(imageUrl , h.iv);
		h.tv.setText(item.getPhotosubject());
		return convertView;
	}
	private class Holder{
		public ImageView iv;
		public TextView tv;
	}
}
