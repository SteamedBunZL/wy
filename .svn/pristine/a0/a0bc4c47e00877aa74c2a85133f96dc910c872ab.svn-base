package com.whoyao.venue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.baidu.platform.comapi.map.v;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.activity.BasicActivity;
import com.whoyao.activity.EventSearchActivity;
import com.whoyao.model.AreaData;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.venue.model.VenueDisdrictItem;

/**
 * 场馆搜索初始页
 * 
 * @author whoyao
 * 
 */
public class VenueSearchInitialActivity extends BasicActivity implements
		OnItemClickListener, OnClickListener {
	private LinearLayout llNearby;
	private GridView gvArea, gvItem;
	private List<String> areaList;
	private List<VenueDisdrictItem> venueDisdrictItem;
	private String[] items = { "羽毛球", "网球", "乒乓球", "篮球", "足球" };
	private int[] itemNum = { 1, 5, 2, 3, 4 };
	// 1,5,2,3,4
	//
	private int[] drawables = { R.drawable.syumaoball,
			R.drawable.snetball, R.drawable.spingpangball,
			R.drawable.sbasktball, R.drawable.sfootball };
	private ItemAdapter itemAdapter;
	private AreaAdapter areaAdapter;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_search_initial);
		initView();
		initData();
		gvArea.setOnItemClickListener(this);
		gvItem.setOnItemClickListener(this);
	}

	private void initView() {
		findViewById(R.id.page_btn_back_initial).setOnClickListener(this);
		venueDisdrictItem = new ArrayList<VenueDisdrictItem>();
		intent = new Intent(this, VenueSearchActivity.class);
		llNearby = (LinearLayout) findViewById(R.id.venue_search_ll_nearby);
		llNearby.setOnClickListener(this);
		gvArea = (GridView) findViewById(R.id.venue_search_gv_hot_area);
		gvItem = (GridView) findViewById(R.id.venue_search_gv_activity_type);
		gvArea.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvItem.setSelector(new ColorDrawable(Color.TRANSPARENT));
		areaAdapter = new AreaAdapter();
		itemAdapter = new ItemAdapter(items, drawables);

	}

	private void initData() {
		areaList = new ArrayList<String>();
		Net.request(null, WebApi.Venue.VenueSearchInitial,
				new ResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						ArrayList<VenueDisdrictItem> venueDisdrictItem = JSON
								.getList(content, VenueDisdrictItem.class);
						areaAdapter.addList(venueDisdrictItem);
						gvItem.setAdapter(itemAdapter);
						gvArea.setAdapter(areaAdapter);
						itemAdapter.notifyDataSetChanged();
						areaAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						super.onFailure(e, statusCode, content);
					}

				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		VenueDisdrictItem item = (VenueDisdrictItem) areaAdapter
				.getItem(position);
		switch (parent.getId()) {
		case R.id.venue_search_gv_hot_area:
			intent.putExtra(Extra.State, State.Search_Area);
			intent.putExtra(Extra.SelectedID, item.getCode());
			intent.putExtra(Extra.Snippet, item.getAreaname());
			startActivity(intent);
			break;
		case R.id.venue_search_gv_activity_type:
			intent.putExtra(Extra.State, State.Search_Type);
			intent.putExtra(Extra.SelectedID, itemNum[position]);
			intent.putExtra(Extra.Snippet, items[position]);
			startActivity(intent);
			break;
		}
	}

	public class AreaAdapter extends BaseAdapter {
		List<VenueDisdrictItem> disdrictItem = new ArrayList<VenueDisdrictItem>();

		public void addList(List<VenueDisdrictItem> data) {
			disdrictItem.addAll(data);
		}

		@Override
		public int getCount() {
			return disdrictItem.size();
		}

		@Override
		public Object getItem(int position) {
			return disdrictItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			v = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_search_initial_district, null);
			TextView tvDistrict = (TextView) v.findViewById(R.id.item_pop_tv);
			VenueDisdrictItem item = (VenueDisdrictItem) getItem(position);
			tvDistrict.setText(item.getAreaname());
			if (position > 3) {
				v.findViewById(R.id.item_pop_line_h).setVisibility(View.GONE);
			}
			if (3 == position % 4) {
				v.findViewById(R.id.item_pop_line_v).setVisibility(View.GONE);
			}
			return v;
		}

	}

	public class ItemAdapter extends BaseAdapter {
		private String[] items;
		private int[] drawables;

		public ItemAdapter(String[] items, int[] drawables) {
			this.items = items;
			this.drawables = drawables;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			v = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item_venue_search_initial, null);
			TextView tvItem = (TextView) v.findViewById(R.id.item_pop_tv);
			tvItem.setText(items[position]);
			ImageView ivItem = (ImageView) v.findViewById(R.id.item_pop_img);
			ivItem.setBackgroundResource(drawables[position]);
			if (items.length % 3 < position) {
				v.findViewById(R.id.item_pop_line_h).setVisibility(View.GONE);
			}
			if (2 == position % 3) {
				v.findViewById(R.id.item_pop_line_v).setVisibility(View.GONE);
			}
			return v;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back_initial:
			finish();
			break;
		case R.id.venue_search_ll_nearby:
			if (AppContext.location != null) {
				intent.putExtra(Extra.State, State.Search_Loc);
				// Toast.show(AppContext.location.getAddrStr());
				// TODO 搜索附件的活动
			} else {
				Toast.show("网络定位失败,请检查网络情况");
			}
			if (AppContext.isNetAvailable()) {
				startActivity(intent);
			} else {
				Toast.show(R.string.warn_network_unavailable);
			}
			break;

		}

	}

}
