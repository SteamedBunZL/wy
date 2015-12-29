package com.whoyao.activity;

import java.util.ArrayList;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.db.AreaListDB;
import com.whoyao.model.AreaData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 选择地址(分三级列表)
 * 
 * @author hyh creat_at：2013-8-5-上午11:38:21
 */
public class SelectAddrActivity extends BasicActivity implements OnClickListener, OnItemClickListener {
	private static final int STATE_PROVINCE = 0;
	private static final int STATE_CITY = 1;
	private static final int STATE_DISTRICT = 2;
	private ListView mListview;
	private TextView titleTv;
	private int state = 0;
	private AreaListDB db;
	private ArrayList<AreaData> addrList = new ArrayList<AreaData>();

	private int provinceCode;
	private int cityCode;
	private int districtCode;
	private String provinceName = "";
	private String cityName = "";
	private String districtName ="";
	private AddrAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		mListview = (ListView) findViewById(R.id.chaddr_lv);
		titleTv = (TextView) findViewById(R.id.page_tv_title);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		init();
	}

	private void init() {
		db = new AreaListDB();
		adapter = new AddrAdapter();
		mListview.setSelector(android.R.color.transparent);
		mListview.setAdapter(adapter);
		mListview.setOnItemClickListener(this);
		

		setProvinceDate();
	}

	@Override
	public void onClick(View v) {
		switch (state) {
		case STATE_PROVINCE:// 从选择省份->-原页面
			finish();
			break;
		case STATE_CITY:// 从选择城市->-选择省份
			setProvinceDate();	
			break;
		case STATE_DISTRICT:// 从选择取消->-选择城市
			setCityDate();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (state) {
		case STATE_PROVINCE:
			provinceName = addrList.get(position).getAreaName();
			provinceCode = addrList.get(position).getCode();
			if(0 == addrList.get(position).getHaveChild()){
				setResultData();
				return;
			}
			setCityDate();
			break;
		case STATE_CITY:
			cityName = addrList.get(position).getAreaName();
			cityCode = addrList.get(position).getCode();
			if (0 == addrList.get(position).getHaveChild()) {
				setResultData();
				return;
			}
			setDistrictData();
			break;
		case STATE_DISTRICT:
			districtName = addrList.get(position).getAreaName();
			districtCode = addrList.get(position).getCode();
			setResultData();
			break;
		default:
			break;
		}

	}
	/**切换到区县界面*/
	private void setDistrictData() {
		titleTv.setText(cityName + "--选择区县");
		ArrayList<AreaData> districtList = db.getChildArea(cityCode);
		addrList = districtList;
		adapter.notifyDataSetInvalidated();
		state = 2;
	}

	private void setCityDate() {
		ArrayList<AreaData> cityList = db.getChildArea(provinceCode);
		addrList = cityList;
		adapter.notifyDataSetInvalidated();
		titleTv.setText(provinceName + "--选择城市");
		state = STATE_CITY;
	}

	private void setProvinceDate(){
		ArrayList<AreaData> provinces = db.getProvinces();
		addrList = provinces;
		adapter.notifyDataSetInvalidated();
		titleTv.setText("选择省份");
		state = STATE_PROVINCE;
	}
	
	private void setResultData() {
		Intent data = new Intent();
		data.putExtra(Extra.ProvinceCode, provinceCode);
		data.putExtra(Extra.ProvinceName, provinceName);
		data.putExtra(Extra.CityCode, cityCode);
		data.putExtra(Extra.CityName, cityName);
		data.putExtra(Extra.DistrictCode, districtCode);
		data.putExtra(Extra.DistrictName, districtName);
		setResult(RESULT_OK, data);
		finish();
	}

	public class AddrAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return addrList.size();
		}

		@Override
		public Object getItem(int position) {
			return addrList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return addrList.get(position).getCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (null == convertView) {
				convertView = View.inflate(SelectAddrActivity.this, R.layout.item_address, null);
				holder = new Holder();
				holder.textView = (TextView) convertView.findViewById(R.id.itemaddr_tv);
				holder.imageView = (ImageView) convertView.findViewById(R.id.itemaddr_iv);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.textView.setText(addrList.get(position).getAreaName());
			if (2 == state || !addrList.get(position).hasChild()) {// 如果是区县
				holder.imageView.setVisibility(View.INVISIBLE);
			} else{
				holder.imageView.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	public class Holder {
		public TextView textView;
		public ImageView imageView;
	}

	@Override
	public String toString() {
		return "选择地址(分三级列表)";
	}
}
