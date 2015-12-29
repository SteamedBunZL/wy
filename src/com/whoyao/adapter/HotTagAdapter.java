package com.whoyao.adapter;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.TagItemModel;
import com.whoyao.ui.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2013-8-19-上午11:30:58
 */
public class HotTagAdapter extends BaseAdapter implements OnCheckedChangeListener, OnClickListener {

	
	
	private List<TagItemModel> list;
	private LayoutInflater inflater;
	private MyinfoManager manager;


	public HotTagAdapter(List<TagItemModel> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(AppContext.context);
		manager = MyinfoManager.getManager();
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
		Holder holder = null;
		if(null == convertView){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_taglist, null);
			convertView.setTag(holder);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb_tag);
			holder.cb.setTag(position);
			holder.cb.setOnCheckedChangeListener(this);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv_tag);
			holder.tv.setTag(holder.cb);
			holder.tv.setOnClickListener(this);
		}else{
			holder = (Holder) convertView.getTag();
			holder.cb.setTag(position);
		}

		TagItemModel tag = list.get(position);
		holder.tv.setText(tag.getTagName());
		holder.cb.setChecked(tag.isSelected());
		return convertView;
	}
	class Holder{
		public TextView tv;
		public CheckBox cb;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int tag = (Integer) buttonView.getTag();
		TagItemModel item = list.get(tag);
		item.setSelected(isChecked);
		if(isChecked && -1 == manager.interestTags.indexOf(item)){
			if(manager.interestTags.size()<10){
				manager.interestTags.add(item);
			}else{
				buttonView.setChecked(false);
				Toast.show(R.string.warn_tag_full);
			}
		}else{
			if(-1 != manager.interestTags.indexOf(item)){
				manager.interestTags.remove(item);
			}
		}
	}
	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		if(null != tag && tag instanceof CheckBox){
			CheckBox cb = (CheckBox) tag;
			cb.setChecked(!cb.isChecked());
		}
	}
}
