package com.whoyao.adapter;

import java.io.File;
import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.common.SmallImageAsyncLoader;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author hyh 
 * creat_at：2013-9-16-下午2:27:31
 */
public class TopicPhotoAdapter extends BaseAdapter {
	
		
	
	private List<File> mList;
	private SmallImageAsyncLoader loader;
	private LayoutInflater inflater;
	public TopicPhotoAdapter(List<File> list) {
		super();
		mList = list;
		loader = SmallImageAsyncLoader.getInstance();
		inflater = LayoutInflater.from(AppContext.context);
		BitmapFactory.decodeResource(AppContext.getRes(), R.drawable.selector_button_add);
	}

	@Override
	public int getCount() {
		return mList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			View v = inflater.inflate(R.layout.item_imagegrid, null);
			ImageView iv = (ImageView) v.findViewById(R.id.item_iv_photo);
			if(position < mList.size()){
				loader.loadBitmap(mList.get(position), iv);
			}else if(position == mList.size()){
				iv.setImageResource(R.drawable.selector_button_add);
			}
			return v;
		}
}
