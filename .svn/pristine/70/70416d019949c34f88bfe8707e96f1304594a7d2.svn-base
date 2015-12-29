package com.whoyao.ui;

import java.util.List;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.utils.Utils;

import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author hyh creat_at：2013-8-23-下午1:57:19
 */
public  class  SingleLineGridView<T> {
	int cloumnNums = 0;
	int width = 0;
	int columnWidth = Utils.dip2px(AppContext.context, 60);
	int columnHeigth = Utils.dip2px(AppContext.context, 60);
	GridView grid;
	private LinearLayout parent;
	private List<T> mList;
	private BaseAdapter adapter;
	private OnItemClickListener mListener;

	public SingleLineGridView(LinearLayout parent,OnItemClickListener listener) {
		this.parent = parent;
		mListener = listener;
		grid = new GridView(AppContext.curActivity);
		grid.setVerticalSpacing(0);
		grid.setBackgroundResource(R.color.carmine_button);
//		grid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		grid.setVerticalScrollBarEnabled(false);
	}

	public void setData(List<T> list,BaseAdapter adapter) {
		mList = list;
		cloumnNums = mList.size() +1;
		width = cloumnNums * columnWidth;
		LayoutParams params = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
		Log.i("GridView", "width = " + width);
		grid.setLayoutParams(params);
		grid.setNumColumns(cloumnNums);
		
		this.adapter = adapter;
//		adapter = new ImageAdapter(list);
		grid.setAdapter(adapter);
		parent.removeAllViews();
		parent.addView(grid);
		grid.setOnItemClickListener(mListener);
	}
	
	public void refresh() {
		//int columnWidth = Utils.dip2px(AppContext.context, 60);
		cloumnNums = mList.size() +1;
		int width = cloumnNums * columnWidth;
		LayoutParams params = new LayoutParams(width, columnWidth);
		parent.removeView(grid);
		grid.setNumColumns(cloumnNums);
		grid.setLayoutParams(params);
		parent.addView(grid);
		adapter.notifyDataSetChanged();
	}
}
