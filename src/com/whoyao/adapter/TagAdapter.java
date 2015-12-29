package com.whoyao.adapter;

import java.util.List;
import java.util.Random;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.model.FreeModel;
import com.whoyao.model.TagModel;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2013-7-30-下午12:00:01
 */
public class TagAdapter extends BaseAdapter {

	
	private List<TagModel> list;
	private LayoutInflater inflater;
	private Random random;
	private int[] colorArray;
	
	public TagAdapter(List<TagModel> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(AppContext.curActivity);
		random = new Random();
		colorArray = AppContext.getRes().getIntArray(R.array.tag_color);
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return list.get(position).getTagId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder ;
		if(null == convertView){
			convertView = inflater.inflate(R.layout.item_tag, null);
			holder = new Holder();
			holder.tag = (TextView) convertView.findViewById(R.id.itemtag_tv_tag);
			holder.color = convertView.findViewById(R.id.itemtag_ll);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		String tag = list.get(position).getTagName();
		holder.tag.setText(tag);
		holder.color.setBackgroundColor(colorArray[random.nextInt(colorArray.length)]);
		
		return convertView;
	}

	class Holder{
		public TextView tag;
		public View color;
	}
}
