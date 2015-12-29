package com.whoyao.venue.engine;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.whoyao.R;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.venue.model.VenueItemModel;

/**
 * @author hyh creat_at：2013-11-12-下午3:20:56
 */
public class VenueListAdapter extends BaseAdapter {

	private List<VenueItemModel> mList;
	private LayoutInflater inflater;
	private ImageAsyncLoader loader;

	public VenueListAdapter(Context context, List<VenueItemModel> list) {
		super();
		mList = list;
		inflater = LayoutInflater.from(context);
		loader = SmallImageAsyncLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (null == mList) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList.get(position).getVenueid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_venue_homelist, null);
			holder = new Holder();
			convertView.setTag(holder);
			holder.ivLogo = (ImageView) convertView.findViewById(R.id.venue_iv_logo);
			holder.tvName = (TextView) convertView.findViewById(R.id.venue_tv_name);
			holder.tvAddr = (TextView) convertView.findViewById(R.id.venue_tv_addr);
			holder.tvDistance = (TextView) convertView.findViewById(R.id.venue_tv_distance);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.venue_tv_price);
			holder.tvMarkValue = (TextView) convertView.findViewById(R.id.venue_tv_markvalue);
			holder.rbMarkValue = (RatingBar) convertView.findViewById(R.id.venue_rb_markvalue);
			holder.tvServiceType = (TextView) convertView.findViewById(R.id.venue_tv_service_type);
		} else {
			holder = (Holder) convertView.getTag();
		}
		VenueItemModel item = mList.get(position);
		loader.loadBitmap(item.getLogourl(), holder.ivLogo);
		holder.tvName.setText(item.getFullname());
		holder.tvAddr.setText(item.getAddress());
		if(!(item.getMarkvalue()==0.0))
		holder.tvMarkValue.setText(item.getMarkvalue() + "");
		holder.rbMarkValue.setMax(100);
		holder.rbMarkValue.setProgress((int) (item.getMarkvalue() * 10));
		holder.tvServiceType.setText(VenueEngine.getServiceType(item.getServicetype()));
		holder.tvDistance.setText(getDistance(item.getDistance()));
		if(item.getLowestprice() > 0){
			holder.tvPrice.setText("");
			holder.tvPrice.append(Html.fromHtml("￥<font color=red>" + (int) item.getLowestprice() + "</font> 起"));
		}else{
			holder.tvPrice.setText("");
		}
		return convertView;
	}


	private String getDistance(float distance) {
		String distanceStr = "";
		if (0 != distance) {
			if (0.1 > distance) {
				distanceStr = "<100m";
			} else if (1 > distance) {
				distanceStr = (int) (distance * 1000) + "m";
			} else if (distance > 50) {
				distanceStr = ">50km";
			} else {
				distanceStr = ((int) (distance * 100)) / 100.0f + "km";
			}
		}
		return distanceStr;
	}

	class Holder {
		public RatingBar rbMarkValue;
		public TextView tvMarkValue;
		public ImageView ivLogo;
		public TextView tvName;
		public TextView tvAddr;
		public TextView tvServiceType;
		public TextView tvDistance;
		public TextView tvPrice;
	}
}
