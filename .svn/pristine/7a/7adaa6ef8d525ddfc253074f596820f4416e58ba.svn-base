package com.whoyao.venue.engine;

import java.util.ArrayList;
import java.util.LinkedList;

import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.utils.L;
import com.whoyao.venue.model.PlaceStatisticModel;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author hyh creat_at：2014-2-20-下午2:53:18
 */
public class VenuePlaceAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<PlaceStatisticModel> mListContent;
	private ArrayList<Integer> mListIndex;
	private LayoutInflater mInflater;
	private LinkedList<View> mSubViewCache;

	public VenuePlaceAdapter(Activity context,
			ArrayList<PlaceStatisticModel> list) {
		this.context = context;
		mListContent = list;
		mInflater = LayoutInflater.from(context);
		mSubViewCache = new LinkedList<View>();
		mListIndex = new ArrayList<Integer>();
	}

	@Override
	public int getCount() {
//		if (mListContent.size() == 0) {
//			return 1;
//		} else {
			return mListIndex.size();
//		}
	}

	@Override
	public Object getItem(int position) {
//		if (mListContent.size() == 0) {
//			return null;
//		} else {
		return mListIndex.get(position);
//		}
	}

	@Override
	public long getItemId(int position) {
//		if (mListContent.size() == 0) {
//			return position;
//		} else {
		return mListIndex.get(position);
//		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		if (mListContent.size() == 0) {
//			convertView = mInflater.inflate(R.layout.sub_empty_view, null);
//		} else {
			if (null == convertView) {
				convertView = mInflater
						.inflate(R.layout.item_venue_place, null);
				convertView.setTag(convertView.findViewById(R.id.ll));
			} else {
				recycleSubView((ViewGroup) convertView.getTag());
			}
			ViewGroup vg = (ViewGroup) convertView.getTag();
			int currentIndex = mListIndex.get(position);
			int nextIndex = mListContent.size();
			if (position + 1 < mListIndex.size()) {
				nextIndex = mListIndex.get(position + 1);
			}
			// L.i(Const.ZL,"list-"+mListContent.size());
			// View view = mInflater.inflate(R.layout.sub_empty_view, null);
			// vg.addView(view);
			for (int i = currentIndex; i < nextIndex; i++) {
				PlaceStatisticModel item = mListContent.get(i);
				View subView = getSubView();
				Holder tag = (Holder) subView.getTag();
				tag.tvTime
						.setText(VenueEngine.getPlaceTime(item.getPlacetime()));
				tag.tvType.setText(VenueEngine.getPlaceTypeDetial(item
						.getPlacetypedetail()));
				tag.tvPrice.setText("￥" + (int) item.getPrice());
				if (item.getPrice() != item.getOriginalprice()) {
					tag.tvOPrice.setText("￥" + (int) item.getOriginalprice());
				} else {
					tag.tvOPrice.setText("");
				}
				tag.tvCount.setText("剩" + item.getRemainder() + "块");
				// tag.btnOrder.setTag(item.getPlacestatisticid());
				if (item.getRemainder() == 0) {
					tag.btnOrder.setEnabled(false);
				} else {
					tag.btnOrder.setTag(i);
					if (i == currentIndex) {
						tag.tvTime.setVisibility(View.VISIBLE);
					} else {
						tag.tvTime.setVisibility(View.INVISIBLE);
					}
					tag.btnOrder.setEnabled(!item.isAdded());
				}
				vg.addView(subView);
			}

//		}

		return convertView;
	}

	private View getSubView() {
		View v = getRecycledSubView();
		if (v == null) {
			Holder tag = new Holder();
			v = mInflater.inflate(R.layout.item_venue_place_sub, null);
			tag.tvTime = (TextView) v.findViewById(R.id.place_sub_tv_time);
			tag.tvType = (TextView) v.findViewById(R.id.place_sub_tv_type);
			tag.tvPrice = (TextView) v.findViewById(R.id.place_sub_tv_price);
			tag.tvOPrice = (TextView) v.findViewById(R.id.place_sub_tv_oprice);
			tag.tvCount = (TextView) v.findViewById(R.id.place_sub_tv_count);
			tag.btnOrder = (TextView) v.findViewById(R.id.place_sub_btn_order);
			tag.tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			tag.btnOrder.setOnClickListener((OnClickListener) context);
			v.setTag(tag);
		}
		return v;
	}

	private void recycleSubView(ViewGroup parent) {
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			mSubViewCache.offer(parent.getChildAt(i));
		}
		parent.removeAllViews();
	}

	private View getRecycledSubView() {
		return mSubViewCache.poll();
	}

	@Override
	public void notifyDataSetChanged() {
		initIndex();
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		initIndex();
		super.notifyDataSetInvalidated();
	}

	private void initIndex() {
		mListIndex.clear();
		int lastTime = -1;
		int size = mListContent.size();
		for (int i = 0; i < size; i++) {
			int placeTime = mListContent.get(i).getPlacetime();
			if (placeTime != lastTime) {
				lastTime = placeTime;
				mListIndex.add(i);
			}
		}
	}

	public class Holder {
		public TextView tvTime;
		public TextView tvType;
		public TextView tvPrice;
		public TextView tvOPrice;
		public TextView tvCount;
		public TextView btnOrder;
	}

}
