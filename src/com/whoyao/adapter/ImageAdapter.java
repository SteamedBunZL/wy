/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whoyao.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.AnimPost;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.model.UserPhotoModel;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<UserPhotoModel> mList;
	private ImageAsyncLoader loader;
	private String imageDimen;


	public ImageAdapter(List<UserPhotoModel> list) {
		mInflater = (LayoutInflater) AppContext.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		loader = OriginalImageAsyncLoader.getInstance();
		BitmapFactory.decodeResource(AppContext.getRes(), android.R.color.transparent);
		MobileInfoModel info = ClientEngine.getMobileInfo();
		imageDimen = "/"+info.getVgaWidth()+"x"+info.getVgaHeight()+".jpg";
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Handler handler = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_view_flow, null);
			handler = new Handler();
			handler.iv = (ImageView) convertView.findViewById(R.id.item_iv_viewflow);
			handler.anim = (AnimationDrawable) AppContext.getRes().getDrawable(R.drawable.anim_loading_white);
			convertView.setTag(handler);
		}else{
			handler = (Handler) convertView.getTag();
			handler.anim.stop();
		}
		
		//handler.iv.setImageBitmap(defaultBm);
		handler.iv.setImageDrawable(handler.anim);
		//AnimationDrawable anim = (AnimationDrawable)handler.iv.getDrawable();
		handler.iv.post(new AnimPost(handler.anim));
 
		
		
		String url = WebApi.getImageUrl(mList.get(position).getPhotoPath(), imageDimen) ;
		loader.loadBitmap(url, handler.iv);
		return convertView;
	}

	public class Handler{
		public ImageView iv;
		public AnimationDrawable anim;
	}
}
