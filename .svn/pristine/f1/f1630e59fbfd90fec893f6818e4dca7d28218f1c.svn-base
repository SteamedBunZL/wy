package com.whoyao.venue.engine;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.activity.GuideActivity;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.utils.L;
import com.whoyao.venue.VenuPhotoCancelActivity;
import com.whoyao.venue.VenueMoeInfoActivity;
import com.whoyao.venue.model.VenuePhotoModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


/**
 * @author hyh 
 * creat_at：2014-2-23-下午12:25:34
 */
public class VenuePhotosAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<VenuePhotoModel> mListContent;
	private OriginalImageAsyncLoader loader;

	public VenuePhotosAdapter(Context context,List<VenuePhotoModel> photos) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		mListContent = photos;
		loader = OriginalImageAsyncLoader.getInstance();
	}

	@Override
	public int getCount() {
		return mListContent.size();
	}

	@Override
	public Object getItem(int position) {
		return mListContent.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mListContent.get(position).getPhotoid();
	}
	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final VenuePhotoModel item = mListContent.get(position);
		ImageView iv = null;
		if(null == convertView){
			iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setScaleType(ScaleType.FIT_XY);
		}else{
			iv = (ImageView) convertView;
		}
		iv.setTag(position);
		loader.loadBitmapByID(item.getPhotopath(), iv);
		L.i("VenPhoto",item.getPhotopath());
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent jump = new Intent(context,VenuPhotoCancelActivity.class);
					int position = (Integer) v.getTag();
					jump.putExtra(Extra.SelectedItem,position);
					L.i("互邀++________","Extra.SelectedItem"+item.getPhotopath());
					AppContext.curActivity.startActivity(jump);
			}
		});
		return iv;
	}
}
