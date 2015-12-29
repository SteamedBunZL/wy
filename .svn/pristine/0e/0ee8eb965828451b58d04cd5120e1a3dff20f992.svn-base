package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.adapter.ViewPagerAdapter;
import com.whoyao.common.AnimPost;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.model.MobileInfoModel;
import com.whoyao.utils.CalendarUtils;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 活动照片
 * @author hyh 
 * creat_at：2013-9-26-上午10:03:16
 * @param <item>
 */
public class EventPhotoActivity<item> extends BasicActivity implements OnClickListener {
	private TextView tvTitle;
	//private Gallery mGallery;
	private TextView tvPhotoTitle;
	private TextView tvPhotoDesc;
	private ViewPager mViewPager;
	private String imageDimen;
	private OriginalImageAsyncLoader loader;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobileInfoModel info = ClientEngine.getMobileInfo();
		loader = OriginalImageAsyncLoader.getInstance();
		imageDimen = "/"+info.getVgaWidth()+"x"+info.getVgaHeight()+".jpg";
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.activity_event_photo);
		initView();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		tvTitle = (TextView)findViewById(R.id.page_tv_title);
		//mGallery = (Gallery)findViewById(R.id.event_photo_gl);
		tvPhotoTitle = (TextView)findViewById(R.id.event_photo_tv_title);
		tvPhotoDesc = (TextView)findViewById(R.id.event_photo_tv_desc);
//		mGallery.setOnItemSelectedListener(new GalleryListener());
//		mGallery.setAdapter(new EventPhotoAdapter(EventDetialManager.photoList));
//		mGallery.setSelection(getIntent().getIntExtra(Extra.SelectedItem, 0));
		
		mViewPager = (ViewPager)findViewById(R.id.event_photo_vp);
		mViewPager.setAdapter(new ViewPagerAdatper());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				setPhotoInfo(position);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
			
			@Override
			public void onPageScrollStateChanged(int state) { }
		});
		int position = getIntent().getIntExtra(Extra.SelectedItem, 0);
		mViewPager.setCurrentItem(position);
		setPhotoInfo(position);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
	class GalleryListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			tvTitle.setText((position +1) +" of " + EventDetialManager.photoList.size());
			EventPhotoModel item = EventDetialManager.photoList.get(position);
			tvPhotoTitle.setText(item.getPhotosubject());
			tvPhotoDesc.setText(item.getUserid()+"上传与"+item.getUploadtime());
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	}
	
	class ViewPagerAdatper extends ViewPagerAdapter{
	
		@Override
		public int getCount() {
			return EventDetialManager.photoList.size();
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
			}else{
				handler = (Handler) convertView.getTag();
				handler.anim.stop();
			}			
			handler.iv.setImageDrawable(handler.anim);
			handler.iv.post(new AnimPost(handler.anim));		
			String url = WebApi.getImageUrl(EventDetialManager.photoList.get(position).getPhotopath(), imageDimen) ;
			loader.loadBitmap(url, handler.iv);
			return convertView;
		}
	}
	public class Handler{
		public ImageView iv;
		public AnimationDrawable anim;
	}
	private void setPhotoInfo(int position){
		tvTitle.setText((position +1) +" of " + EventDetialManager.photoList.size());
		EventPhotoModel item = EventDetialManager.photoList.get(position);
		tvPhotoTitle.setText(item.getPhotosubject());
		String timeStr = CalendarUtils.formatChinese(item.getUploadtime());
		if(null != timeStr){
			tvPhotoDesc.setText(findNick(item.getUserid())+"上传与"+timeStr);
		}
	}
	private String findNick(int id){
		for(JoinerModel item:EventDetialManager.joinerList){
			if(item.getUserid() == id)
				return item.getUsername();
		}
		return "";
	}
}
