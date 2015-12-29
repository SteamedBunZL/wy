package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AreaWheelAdapter;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.db.AreaListDB;
import com.whoyao.model.AreaData;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 选择地址(滚轴)
 * @author hyh 
 * creat_at：2013-8-16-上午9:58:22
 */
public class SelectAddrWheelActivity extends AbsSelectActivity implements OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
	private WheelView wvProvince;
	private WheelView wvCity;
	private WheelView wvDistrict;
	private AreaListDB db;
	private boolean isScrolling;
	private ArrayList<AreaData> provinces;
	private ArrayList<AreaData> cities;
	private ArrayList<AreaData> districts;
	private EditText etSnippet;
	private Intent workIntent;
	private int currentWorkProviceIndex;
	private int currentWorkCityIndex;
	private int currentWrokDistricIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_addr_wheel);
		setSelectView(findViewById(R.id.wheel_ll));
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		etSnippet = (EditText)findViewById(R.id.wheel_et_snippet);
		wvProvince = (WheelView)findViewById(R.id.wheel_province);
		wvCity =   	 (WheelView)findViewById(R.id.wheel_city);
		wvDistrict = (WheelView)findViewById(R.id.wheel_district);
		workIntent = getIntent();
		db = new AreaListDB();
		provinces = db.getProvinces();
		for (int i = 0; i < provinces.size(); i++) {
			if (provinces.get(i).getCode()==workIntent.getIntExtra(Extra.ProvinceCode, 110000)) {
				currentWorkProviceIndex = i;
			}
		}
		wvProvince.setViewAdapter(new AreaWheelAdapter(this, provinces));
		wvProvince.setCurrentItem(currentWorkProviceIndex);
		cities = db.getChildArea(provinces.get(currentWorkProviceIndex).getCode());
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getCode()==workIntent.getIntExtra(Extra.CityCode, 110000)) {
				currentWorkCityIndex = i;
			}
		}
		wvCity.setViewAdapter(new AreaWheelAdapter(this, cities));
		wvCity.setCurrentItem(currentWorkCityIndex);
		districts = db.getChildArea(cities.get(currentWorkCityIndex).getCode());
		for (int i = 0; i < districts.size(); i++) {
			if (districts.get(i).getCode()==workIntent.getIntExtra(Extra.DistrictCode, 110101)) {
				currentWrokDistricIndex = i;
			}
		}
		wvDistrict.setViewAdapter(new AreaWheelAdapter(this, districts));
		wvDistrict.setCurrentItem(currentWrokDistricIndex);
		wvProvince.addChangingListener(this);
		wvProvince.addScrollingListener(this);
		wvCity.addChangingListener(this);
		wvCity.addScrollingListener(this);
		
		show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_cancel:
			dismiss();
			break;
		case R.id.wheel_ok:
			setResultData();
			dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		isScrolling = true;
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		isScrolling = false;
		switch (wheel.getId()) {
		case R.id.wheel_province:
			updateCities();
			break;
		case R.id.wheel_city:
			updateDistricty();
			break;
		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if(isScrolling){
			return;
		}
		switch (wheel.getId()) {
		case R.id.wheel_province:
			updateCities();
			break;
		case R.id.wheel_city:
			updateDistricty();
			break;
		default:
			break;
		}
	}


	private void updateSubWheel(WheelView parentWheel,List<AreaData> parentData, WheelView subWheel,List<AreaData> subData) {
		int oldIndex = subWheel.getCurrentItem();
		int parentCode = parentData.get(parentWheel.getCurrentItem()).getCode();
		ArrayList<AreaData> temp = null;
		switch (subWheel.getId()) {
		case R.id.wheel_city:
			temp = db.getChildArea(parentCode);
			break;
		case R.id.wheel_district:
			temp = db.getChildArea(parentCode);
			break;
		default:
			break;
		}
		subData.clear();
		if(null != temp){
			subData.addAll(temp);
		}
		subWheel.setViewAdapter(new AreaWheelAdapter(this, subData));
		if(subData.size()<oldIndex){
			subWheel.setCurrentItem(0);
		}else{
			subWheel.setCurrentItem(0,true);
		}
	}
	
	
	private void updateCities(){
		updateSubWheel(wvProvince, provinces, wvCity, cities);
		updateDistricty();
//		ArrayList<AreaData> temp = db.getCities(cities.get(0).getCode());
//		wvDistrict.setCurrentItem(0,true);
//		districts.clear();
//		if(null != temp){
//			districts.addAll(temp);
//		}
//		wvDistrict.setViewAdapter(new AreaWheelAdapter(this, districts));
	}
	private void updateDistricty(){
		updateSubWheel(wvCity, cities, wvDistrict, districts);
	}
	
	private void setResultData() {
		
		Intent data = new Intent();
		AreaData proData = provinces.get(wvProvince.getCurrentItem());
		AreaData cityData = cities.get(wvCity.getCurrentItem());
		int provinceCode = proData.getCode();
		String provinceName =proData.getAreaName();
		int cityCode = cityData.getCode();
		String cityName = cityData.getAreaName();
		int districtCode = 0;
		String districtName = "";
		if(districts.size() > 0){
			AreaData distData = districts.get(wvDistrict.getCurrentItem());
			districtCode = distData.getCode();
			districtName = distData.getAreaName();
		}
		
		if(100001 ==provinceCode  || 100002 ==provinceCode || 100003 ==provinceCode || 100004 ==provinceCode){
			provinceCode = cityCode = cities.get(wvCity.getCurrentItem()).getCode();
		}
		String snippet = etSnippet.getText().toString();
		data.putExtra(Extra.ProvinceCode, provinceCode);
		data.putExtra(Extra.ProvinceName, provinceName);
		data.putExtra(Extra.CityCode, cityCode);
		data.putExtra(Extra.CityName, cityName);
		data.putExtra(Extra.DistrictCode, districtCode);
		data.putExtra(Extra.DistrictName, districtName);
		data.putExtra(Extra.Snippet,snippet );
		setResult(RESULT_OK, data);
		finish();
	}
	
	@Override
	public String toString() {
		return "选择地址(滚轴)";
	}
}
