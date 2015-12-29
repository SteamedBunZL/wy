package com.whoyao.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hyh 
 * creat_at：2013-9-26-下午2:44:28
 * @param <T>
 */
public abstract class ViewPagerAdapter<T> extends PagerAdapter {

	protected Object convertView;
	protected List<T> contentList;
	protected Context context;

	
	
	public ViewPagerAdapter() {
		super();
	}
	public  ViewPagerAdapter(Context context, List<T> list) {
		super();
		this.context = context;
		contentList = list;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = getView(position, (View) convertView, container);
		container.addView(view);
		if(convertView == view){
			convertView = null;
		}
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		convertView = object;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
