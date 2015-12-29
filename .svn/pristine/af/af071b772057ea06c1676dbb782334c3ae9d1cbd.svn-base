package com.whoyao.adapter;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.EventPhotoModel;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author hyh 
 * creat_at：2013-8-20-下午5:08:18
 */
public class EventPhotoGridAdapter extends BaseAdapter {

	
	
	private List<EventPhotoModel> list;
	private ImageAsyncLoader loader;
	private LayoutInflater inflater;
	private Bitmap defaultBm;
	private boolean addAvailable;

	public EventPhotoGridAdapter(List<EventPhotoModel> list, boolean addAvailable) {
		this.list = list;
		this.addAvailable = addAvailable;
		loader = SmallImageAsyncLoader.getInstance();
		inflater = LayoutInflater.from(AppContext.context);
		defaultBm = BitmapFactory.decodeResource(AppContext.getRes(), R.drawable.person);
	}

	@Override
	public int getCount() {
//		if(addAble){
//			return list.size() +1;
//		}else{
//			return list.size();
//		}
		int count = list.size();
		if(addAvailable){
			++count;
		}
		if(4<count){
			return 4;
		}else{
			return count;
		}
	}

	@Override
	public Object getItem(int position) {
		if(addAvailable && 0 == position){
			return null;
		}else if(addAvailable){
			return list.get(position - 1);
		}else{
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		if(0 == position){
			return 0;
		}else{
			if(addAvailable){
				return list.get(position -1).getPhotoid();
			}else{
				return list.get(position).getPhotoid();
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_imagegrid, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.iv = (ImageView) convertView.findViewById(R.id.item_iv_photo);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(addAvailable && 0 == position){
			holder.iv.setImageResource(R.drawable.selector_button_add);
			return convertView;
		}		
		holder.iv.setImageBitmap(defaultBm);
		
		try {
			if(addAvailable){
				String url = WebApi.getImageUrl(list.get(position-1).getPhotopath(), WebApi.ImageDemen_100);
				loader.loadBitmap(url, holder.iv);
			}else{
				String url = WebApi.getImageUrl(list.get(position).getPhotopath(), WebApi.ImageDemen_100);
				loader.loadBitmap(url, holder.iv);
			}
		} catch (Exception e) {
			AppException.handle(e);
		}
		return convertView;
	}

	public class Holder{
		public ImageView iv;
	}
}
