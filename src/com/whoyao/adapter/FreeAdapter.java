package com.whoyao.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.model.FreeModel;
import com.whoyao.utils.CalendarUtils;

/**
 * @author hyh creat_at：2013-7-30-下午12:00:01
 */
public class FreeAdapter extends BaseAdapter {

	private List<FreeModel> list;
	private LayoutInflater inflater;
	private int[] colorArray;

	public FreeAdapter(List<FreeModel> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(AppContext.curActivity);
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

		return list.get(position).getFreeId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_free, null);
			holder = new Holder();
			holder.time = (TextView) convertView.findViewById(R.id.itemfree_tv_time);
			holder.content = (TextView) convertView.findViewById(R.id.itemfree_tv_content);
			holder.color = convertView.findViewById(R.id.itemfree_color);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		FreeModel item = list.get(position);
		String time = CalendarUtils.formatYMDHM(item.getFreeTimeStart()) + " ~ " + CalendarUtils.formatYMDHM(item.getFreeTimeEnd());
		holder.time.setText(time);
		String content = item.getFreeTimeContent();
		holder.content.setText(content);
		holder.color.setBackgroundColor(colorArray[position%(colorArray.length - 1)]);
		return convertView;
	}

	class Holder {
		public TextView time, content;
		public View color;
	}
}
