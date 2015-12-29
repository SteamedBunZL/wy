package com.whoyao.venue;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.activity.BasicActivity;
import com.whoyao.model.InitialEventHotAreaListItem;

/**
 * 场地搜索初始页
 * 
 * @author whoyao
 * 
 */
public class SiteSearchInitialActivity extends BasicActivity implements
		OnClickListener, OnItemClickListener {
	private GridView gvSiteType;
	// gridview文字和图片
	private String[] typeName = { "羽毛球", "网球", "乒乓球", "篮球", "足球" };
	private int[] typePicture = { R.drawable.yumaoball, R.drawable.netball,
			R.drawable.pingpangball, R.drawable.basktball, R.drawable.football };
	private SiteTypeAdapter siteTypeAdapter;
	private int[] typeId = { 1, 2, 3, 4, 5 };
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_search_initial);
		intent = new Intent(this, SiteDetailActivity.class);
		initData();
		initView();
	}

	private void initView() {
		// TODO initView
		gvSiteType = (GridView) findViewById(R.id.gv_site_type);
		// gvSiteType.setOverScrollMode(View.OVER_SCROLL_NEVER);
		gvSiteType.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvSiteType.setOnItemClickListener(this);
		gvSiteType.setAdapter(siteTypeAdapter);
		findViewById(R.id.page_btn_back).setOnClickListener(this);

	}

	private void initData() {
		// TODO initData
		siteTypeAdapter = new SiteTypeAdapter(typeName, typePicture);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO the clickevent of the gridview
		intent.putExtra(Extra.State, State.Search_Type);
		intent.putExtra(Extra.SelectedItemStr, typeName[position]);
		intent.putExtra(Extra.SelectedID, typeId[position]);
		startActivity(intent);
	}

	private class SiteTypeAdapter extends BaseAdapter {
		private String[] typeName;
		private int[] typePicture;

		public SiteTypeAdapter(String[] typeName, int[] typePicture) {
			this.typeName = typeName;
			this.typePicture = typePicture;
		}

		@Override
		public int getCount() {
			return typeName.length;
		}

		@Override
		public Object getItem(int position) {
			return typeName[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_grid_site_type, null);
			TextView tvStiteType = (TextView) view
					.findViewById(R.id.tv_site_initial);
			tvStiteType.setCompoundDrawablesWithIntrinsicBounds(
					(typePicture[position]), 0, 0, 0);
			tvStiteType.setText(typeName[position]);
			return view;
		}
	}

}
