package com.whoyao.activity;

import java.util.ArrayList;

import com.whoyao.AppContext;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.adapter.ViewPagerAdapter;
import com.whoyao.common.AnimPost;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.model.UserPhotoModel;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 活动照片
 * 
 * @author hyh creat_at：2013-9-26-上午10:03:16
 * @param <item>
 */
public class UserPhotoActivity<item> extends BasicActivity implements OnClickListener {
	private TextView tvTitle;
	private ViewPager mViewPager;
	private String imageDimen;
	private OriginalImageAsyncLoader loader;
	private LayoutInflater inflater;
	private ArrayList<UserPhotoModel> photoList;
	private int position;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		photoList = (ArrayList<UserPhotoModel>)intent.getSerializableExtra(Extra.Photos);
		if(null == photoList || photoList.isEmpty()){
			finish();
			return;
		}
		position = intent.getIntExtra(Extra.SelectedItem, 0);
		MobileInfoModel info = ClientEngine.getMobileInfo();
		loader = OriginalImageAsyncLoader.getInstance();
		imageDimen = "/" + info.getVgaWidth() + "x" + info.getVgaHeight() + ".jpg";
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.activity_user_photo);
		initView();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.page_tv_title);

		mViewPager = (ViewPager) findViewById(R.id.user_photo_vp);
		mViewPager.setAdapter(new PhotoAdatper());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setPhotoInfo(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		mViewPager.setCurrentItem(position);
		setPhotoInfo(position);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	class PhotoAdatper extends ViewPagerAdapter {

		@Override
		public int getCount() {
			return photoList.size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Handler handler = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_view_flow, null);
				handler = new Handler();
				handler.iv = (ImageView) convertView.findViewById(R.id.item_iv_viewflow);
				handler.anim = (AnimationDrawable) AppContext.getRes().getDrawable(R.drawable.anim_loading_white);
				convertView.setTag(handler);
			} else {
				handler = (Handler) convertView.getTag();
				handler.anim.stop();
			}
			handler.iv.setImageDrawable(handler.anim);
			handler.iv.post(new AnimPost(handler.anim));
			String url = WebApi.getImageUrl(photoList.get(position).getPhotoPath(), imageDimen);
			loader.loadBitmap(url, handler.iv);
			return convertView;
		}
	}

	public class Handler {
		public ImageView iv;
		public AnimationDrawable anim;
	}

	private void setPhotoInfo(int position) {
		tvTitle.setText((position + 1) + " of " + photoList.size());
	}
}
