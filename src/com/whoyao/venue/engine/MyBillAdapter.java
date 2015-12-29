package com.whoyao.venue.engine;

import java.util.List;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.R.color;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.model.PrivateListItem;
import com.whoyao.utils.TimeUtils;
import com.whoyao.venue.model.MyBillItem;

public class MyBillAdapter extends BaseAdapter {
	private List<MyBillItem> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;
	private OnClickListener listener;

	public MyBillAdapter(List<MyBillItem> mList, LayoutInflater inflater,
			OnClickListener listener) {
		super();
		this.mList = mList;
		this.inflater = inflater;
		this.listener = listener;
		loader = SmallImageAsyncLoader.getInstance();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_mybill_list, null);
			holder = new Holder();
			holder.tvVenueName = (TextView) convertView
					.findViewById(R.id.mybill_venuename_tv);
			holder.tvMoney = (TextView) convertView
					.findViewById(R.id.mybill_ordermoney_tv);
			holder.tvOrderNumber = (TextView) convertView
					.findViewById(R.id.mybill_ordernumber_tv);
			holder.tvOrderTime = (TextView) convertView
					.findViewById(R.id.mybill_ordertime_tv);
			holder.tvOrderState = (TextView) convertView
					.findViewById(R.id.mybill_orderstate_tv);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		MyBillItem item = mList.get(position);
		holder.tvVenueName.setText(item.getVenuename());
		holder.tvMoney.setText((int)item.getMoney() + "元");
		holder.tvOrderNumber.setText(item.getOrdernumber() + "");
		holder.tvOrderTime.setText(item.getOrdertime().replace("-", "."));
		switch (item.getOrderstate()) {
		case 0:
			holder.tvOrderState.setText("");
			break;
		case 2:
			holder.tvOrderState.setText("待支付");
			holder.tvOrderState.setTextColor(AppContext.getRes().getColor(R.color.orange_button));
			break;
		case 4:
			holder.tvOrderState.setText("已取消");
			// holder.tvOrderState.setTextColor(R.color.);
			holder.tvOrderState.setTextColor(AppContext.getRes().getColor(R.color.gray_text));
			break;
		case 8:
			holder.tvOrderState.setText("已过期");
			holder.tvOrderState.setTextColor(AppContext.getRes().getColor(R.color.gray_text));
			break;
		case 16:
//			holder.tvOrderState.setText("退款申请中");
			break;
		case 32:
//			holder.tvOrderState.setText("已退款");
			break;
		case 64:
			holder.tvOrderState.setText("已成功");
			holder.tvOrderState.setTextColor(AppContext.getRes().getColor(R.color.green_text));
			break;
		}
		return convertView;
	}

	public static class Holder {
		TextView tvVenueName;
		TextView tvMoney;
		TextView tvOrderNumber;
		TextView tvOrderTime;
		TextView tvOrderState;
	}
}
