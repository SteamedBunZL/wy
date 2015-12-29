package com.whoyao.adapter;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.UserPhotoModel;

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
public class UserSelfPhotoGridAdapter extends BaseAdapter {

	
	
	private List<UserPhotoModel> list;
	private ImageAsyncLoader loader;
	private LayoutInflater inflater;
	private Bitmap defaultBm;

	public UserSelfPhotoGridAdapter(List<UserPhotoModel> list) {
		this.list = list;
		loader = SmallImageAsyncLoader.getInstance();
		inflater = LayoutInflater.from(AppContext.context);
		defaultBm = BitmapFactory.decodeResource(AppContext.getRes(), R.drawable.person);
	}

	@Override
	public int getCount() {
		return list.size() +1;
	}

	@Override
	public Object getItem(int position) {
		if(0 == position){
			return null;
		}else{
			return list.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		if(0 == position){
			return 0;
		}else{
			return list.get(position -1).getPhotoID();
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
		if(0 == position){
			holder.iv.setImageResource(R.drawable.selector_button_add);
			return convertView;
		}		
		holder.iv.setImageBitmap(defaultBm);
		try {
			String url = WebApi.getImageUrl(list.get(position-1).getPhotoPath(), WebApi.ImageDemen_100);
			loader.loadBitmap(url, holder.iv);
		} catch (Exception e) {
			AppException.handle(e);
		}
		return convertView;
	}

	class Holder{
		public ImageView iv;
	}
}
