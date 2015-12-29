package com.whoyao.venue.engine;

import java.util.LinkedList;
import java.util.List;

import com.whoyao.R;
import com.whoyao.ui.PickerDialog;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.venue.model.CartSubItemModel;
import com.whoyao.venue.model.VenueCartModel;
import com.whoyao.widget.HorizontalPicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2014-2-26-下午3:29:32
 */
public class OrderConfirmAdapter extends BaseAdapter {

	
	
	private List<VenueCartModel> mListContent;
	private Context context;
	private LayoutInflater mInflater;
	private LinkedList<View> mSubViewCache;
	private PickerDialog<CartSubItemModel> pickerDialog;

	public OrderConfirmAdapter(Context context, List<VenueCartModel> list) {
		super();
		mListContent = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mSubViewCache = new LinkedList<View>();
	}

	@Override
	public int getCount() {
		return mListContent.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h = null;
		if(null == convertView){
			convertView = mInflater.inflate(R.layout.item_venue_cart, null);
			h = new Holder();
			h.llSub = (ViewGroup) convertView.findViewById(R.id.ll);
			h.tvName = (TextView) convertView.findViewById(R.id.venue_cart_tv_name);
			h.tvType = (TextView) convertView.findViewById(R.id.venue_cart_tv_place_type);
			convertView.setTag(h);
		}else{
			h = (Holder) convertView.getTag();
			recycleSubView(h.llSub);
		}
		VenueCartModel item = mListContent.get(position);
		h.tvName.setText(item.getVenueName());
		h.tvType.setText(VenueEngine.getPlaceType(item.getPlacetype()));
		List<CartSubItemModel> subList = item.getSubList();
		int size = subList.size();
		for(int i = 0; i<size; i++){
			CartSubItemModel subItem = subList.get(i);
			View subView = getSubView();
			SubHolder tag = (SubHolder) subView.getTag();
			tag.data = subItem;
//			tag.tvTime.setText(CalendarUtils.formatYMD(subItem.getReservetime()) + " " +VenueEngine.getPlaceTime(subItem.getPlacetime()));
			tag.tvTime.setText(CalendarUtils.formatYMD(item.getPlacedate()) + " " +VenueEngine.getPlaceTime(subItem.getPlacetime()));
//			tag.tvType.setText(VenueEngine.getPlaceTypeDetial(subItem.getPlacetypedetail()));
			tag.tvType.setText(VenueEngine.getPlaceTypeDetial(item.getPlacetypedetail()));
			tag.tvTotalPrice.setText("￥" + (int)subItem.getAmount());
			tag.tvPrice.setText(((int)subItem.getDiscountprice()) + "元/时");
			tag.tvCount.setText("数量："+subItem.getReservecount());
//			tag.tvRemaind.setText("剩"+subItem.getReservecount()+"块");
			int state = subItem.getState();
			if(state != 0){
				tag.tvCount.setSelected(true);
				if(state == 1){
					tag.tvCount.setEnabled(false);
				}
			}else{
				tag.tvCount.setSelected(false);
				tag.tvCount.setEnabled(true);
			}
			h.llSub.addView(subView);
		}
		return convertView;
	}
	
	private View getSubView(){
		View v = getRecycledSubView();
		if(v == null){
			SubHolder tag = new SubHolder();
			v = mInflater.inflate(R.layout.item_venue_order_sub, null);
			tag.tvTime = (TextView) v.findViewById(R.id.order_sub_tv_time);
			tag.tvType = (TextView) v.findViewById(R.id.order_sub_tv_type_detial);
			tag.tvTotalPrice = (TextView) v.findViewById(R.id.order_sub_tv_total_price);
			tag.tvPrice = (TextView) v.findViewById(R.id.order_sub_tv_price);
			tag.tvCount = (TextView) v.findViewById(R.id.order_sub_tv_count);
			tag.tvCount.setTag(tag);
			v.setTag(tag);
		}
		return v;
	}
	private void recycleSubView(ViewGroup parent){
		int count = parent.getChildCount();
		for(int i = 0;i<count;i++){
			mSubViewCache.offer(parent.getChildAt(i));
		}
		parent.removeAllViews();
	}
	private View getRecycledSubView(){
		return mSubViewCache.poll();
	}
	
	private void initPicker(CartSubItemModel subItem) {
		if (null == pickerDialog) {
			pickerDialog = new PickerDialog<CartSubItemModel>(context);
			pickerDialog.setRightButton("取消", null);
			pickerDialog.setLeftButton("确定", (OnClickListener) context);
		}
		HorizontalPicker picker = pickerDialog.getPicker();
//		picker.setMaxValue(item.getRemainder());
		picker.setValue(subItem.getReservecount());
		pickerDialog.setTag(subItem);
		pickerDialog.show();
	}
	
	public class Holder{
		public TextView tvName;
		public TextView tvType;
		public ViewGroup llSub;
	}
	public static class SubHolder{
		public CartSubItemModel data;
		public TextView tvTotalPrice;
		public TextView tvTime;
		public TextView tvType;
		public TextView tvPrice;
		public TextView tvCount;
	}

}
