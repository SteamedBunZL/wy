package com.whoyao.adapter;

import java.util.ArrayList;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.JoinerModel;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 类说明
 * 
 * @author WCT create at：2013-3-20 下午04:53:29
 */
public class Joiner4GridAdapter extends BaseAdapter {

	int count = 0;
	private LayoutInflater inflater;
	private ArrayList<JoinerModel> mList;
	// friendsList
	private ImageAsyncLoader loader;

	public Joiner4GridAdapter(ArrayList<JoinerModel> list) {
		super();
		this.inflater = LayoutInflater.from(AppContext.context);
		//LogUtil.i(TAG, "list size= " + friendsList.size());
		mList = list;
		loader = SmallImageAsyncLoader.getInstance();
	}



	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getCount() {
		if (mList == null)
			return 0;
		int count = mList.size();
		if(4<count){
			return 4;
		}else{
			return count;
		}
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_imagegrid,null);
			holder = new Holder();
			holder.tvName = (TextView) convertView.findViewById(R.id.joinergrid_item_tv_initiator);
			holder.ivFace = (ImageView) convertView.findViewById(R.id.item_iv_photo);
			holder.ivFace.setImageResource(R.drawable.default_user_face);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		JoinerModel item = mList.get(position);
		String imageUrl = item.getUserface();
		loader.loadBitmap(imageUrl, holder.ivFace);

		if(0 == position){
			holder.tvName.setText("发起人");
			holder.tvName.setVisibility(View.VISIBLE);
		}else{
			holder.tvName.setVisibility(View.GONE);
		}
		return convertView;
	}
	class Holder {
		ImageView ivFace, sexImg;
		TextView tvName, friendAgeSex, friendPersonalSig;
	}
}
