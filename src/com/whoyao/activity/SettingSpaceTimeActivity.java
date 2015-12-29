package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.db.AreaListDB;
import com.whoyao.model.UserSpacetimeRModel;
import com.whoyao.model.UserSpacetimeTModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 时空设置
 * 
 * @author hyh creat_at：2013-8-12-上午10:27:20
 */
public class SettingSpaceTimeActivity extends BasicActivity implements OnClickListener {
	private TextView tvWorkLoc;
	private TextView tvHomeLoc;
	private TextView tvHomeTime;
	private TextView tvWorkTime;
	
	private String workProvinceName;
	private String workCityName;
	private String workDistrictName;
	private String homeProvinceName;
	private String homeCityName;
	private String homeDistrictName;
	private UserSpacetimeTModel workModel = new UserSpacetimeTModel(1);
	private UserSpacetimeTModel homeModel = new UserSpacetimeTModel(2);
	//
	private int workProvinceIndex = 0;
	private int workCityIndex = 0;
	private int workDistrictIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_spacetime);
		initView();
		requestData();
	}

	private void initView() {
		tvWorkLoc = (TextView)findViewById(R.id.set_tv_work_location);
		tvHomeLoc = (TextView)findViewById(R.id.set_tv_home_location);
		tvWorkTime = (TextView)findViewById(R.id.set_tv_work_time);
		tvHomeTime = (TextView)findViewById(R.id.set_tv_home_time);
		tvWorkLoc.setOnClickListener(this);
		tvHomeLoc.setOnClickListener(this);
		tvWorkTime.setOnClickListener(this);
		tvHomeTime.setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.set_iv_work_location).setOnClickListener(this);
		findViewById(R.id.set_iv_home_location).setOnClickListener(this);
		findViewById(R.id.set_btn_work_enter).setOnClickListener(this);
		findViewById(R.id.set_btn_home_enter).setOnClickListener(this);
	}

	private void requestData() {
		Net.request(null, WebApi.User.GetUserSpacetime, new ResponseHandler(true){
			@Override
			public void onSuccess(String content) {
				UserSpacetimeRModel model = JSON.getObject(content, UserSpacetimeRModel.class);
				if(2 == model.UserAddress.size()){
					workModel = new UserSpacetimeTModel(model.UserAddress.get(0));
					homeModel = new UserSpacetimeTModel(model.UserAddress.get(1));
				}
				tvWorkLoc.setText(workModel.userfulladdress);
				tvHomeLoc.setText(homeModel.userfulladdress);
				if(workModel.beginminute != 0 || workModel.endminute != 0){
					tvWorkTime.setText(CalendarUtils.getTimeFormMinute(workModel.beginminute) +" ~ "+ CalendarUtils.getTimeFormMinute(workModel.endminute));
				}
				if(homeModel.beginminute != 0 || homeModel.endminute != 0){
					tvHomeTime.setText(CalendarUtils.getTimeFormMinute(homeModel.beginminute) +" ~ "+ CalendarUtils.getTimeFormMinute(homeModel.endminute));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.set_tv_work_location:
			Intent workIntent = new Intent(this, SelectAddrWheelActivity.class);
			workIntent.putExtra(Extra.ProvinceCode, workModel.userprovince);
			workIntent.putExtra(Extra.CityCode, workModel.usercity);
			workIntent.putExtra(Extra.DistrictCode, workModel.userdistrict);
			startActivityForResult(workIntent, R.id.set_tv_work_location);
			break;
		case R.id.set_tv_home_location:
			Intent homeIntent = new Intent(this, SelectAddrWheelActivity.class);
			homeIntent.putExtra(Extra.ProvinceCode, homeModel.userprovince);
			homeIntent.putExtra(Extra.CityCode, homeModel.usercity);
			homeIntent.putExtra(Extra.DistrictCode, homeModel.userdistrict);
			startActivityForResult(homeIntent, R.id.set_tv_home_location);
			break;
		case R.id.set_iv_work_location:
			Intent workLocIntent = new Intent(this, SettingSpaceTimeMapActivity.class);
			workLocIntent.putExtra(Extra.Snippet, "工作地");
			startActivityForResult(workLocIntent, R.id.set_iv_work_location);
			break;
		case R.id.set_iv_home_location:
			Intent homeLocIntent = new Intent(this, EventAreaSetActivity.class);
			homeLocIntent.putExtra(Extra.Snippet, "居住地");
			startActivityForResult(homeLocIntent, R.id.set_iv_home_location);
			break;
		case R.id.set_tv_work_time:
			Intent workTimeIntent = new Intent(this, SelectDoubleHourActivity.class);
			workTimeIntent.putExtra("type", "workTime");
			startActivityForResult(workTimeIntent, R.id.set_tv_work_time);
			break;
		case R.id.set_tv_home_time:
			Intent homeTimeIntent = new Intent(this, SelectDoubleHourActivity.class);
			homeTimeIntent.putExtra("type", "homeTime");
			startActivityForResult(homeTimeIntent, R.id.set_tv_home_time);
			break;
		case R.id.set_btn_work_enter:
			if(TextUtils.isEmpty(tvWorkLoc.getText())){
				Toast.show(getResources().getString(R.string.setting_addr_work_input));
				return;
			}
			if(TextUtils.isEmpty(tvWorkTime.getText())){
				Toast.show(getResources().getString(R.string.setting_time_work_input));
				return;
			}
			Net.request(workModel, WebApi.User.UpUserSpacetime, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					Toast.show("保存成功");
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					if(400 == statusCode || 406 == statusCode){
						Toast.show("保存失败");
					}
					super.onFailure(e, statusCode, content);
				}
			});
			break;
		case R.id.set_btn_home_enter:
			if(TextUtils.isEmpty(tvHomeLoc.getText())){
				Toast.show(getResources().getString(R.string.setting_addr_home_input));
				return;
			}
			if(TextUtils.isEmpty(tvHomeTime.getText())){
				Toast.show(getResources().getString(R.string.setting_time_home_input));
				return;
			}
			Net.request(homeModel, WebApi.User.UpUserSpacetime, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					Toast.show("保存成功");
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					if(400 == statusCode || 406 == statusCode){
						Toast.show("保存失败");
					}
					super.onFailure(e, statusCode, content);
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case R.id.set_iv_work_location:
			if (RESULT_OK == resultCode && null != data) {
				workModel.userspacelatitude = (float) (data.getIntExtra(Extra.LatitudeE6, 0)/1E6);
				workModel.userspacelongitude = (float) (data.getIntExtra(Extra.LongitudeE6, 0)/1E6);
				workModel.userfulladdress = data.getStringExtra(Extra.Snippet);
				workProvinceName = data.getStringExtra(Extra.ProvinceName);
				workCityName = data.getStringExtra(Extra.CityName);
				workDistrictName = data.getStringExtra(Extra.DistrictName);
				//workModel.userprovince
				AreaListDB db = new AreaListDB();
				if(!TextUtils.isEmpty(workDistrictName)){
					workModel.userprovince = db.getAreaByName(workProvinceName).getCode();
				}
				if(!TextUtils.isEmpty(workCityName)){
					workModel.usercity = db.getAreaByName(workCityName).getCode();
				}
				if(!TextUtils.isEmpty(workDistrictName)){
					workModel.userdistrict = db.getAreaByName(workDistrictName).getCode();
				}
				tvWorkLoc.setText(workModel.userfulladdress);
			}
			break;
		case R.id.set_iv_home_location:
			if (RESULT_OK == resultCode && null != data) {
				homeModel.userspacelatitude = (float) (data.getIntExtra(Extra.LatitudeE6, 0)/1E6);
				homeModel.userspacelongitude = (float) (data.getIntExtra(Extra.LongitudeE6, 0)/1E6);
				homeModel.userfulladdress = data.getStringExtra(Extra.Snippet);
				homeProvinceName = data.getStringExtra(Extra.ProvinceName);
				homeCityName = data.getStringExtra(Extra.CityName);
				homeDistrictName = data.getStringExtra(Extra.DistrictName);
				AreaListDB db = new AreaListDB();
				if(!TextUtils.isEmpty(homeDistrictName)){
					homeModel.userprovince = db.getAreaByName(homeProvinceName).getCode();
				}
				if(!TextUtils.isEmpty(homeCityName)){
					homeModel.usercity = db.getAreaByName(homeCityName).getCode();
				}
				if(!TextUtils.isEmpty(homeDistrictName)){
					homeModel.userdistrict = db.getAreaByName(homeDistrictName).getCode();
				}
				tvHomeLoc.setText(homeModel.userfulladdress);
			}
			break;
		case R.id.set_tv_work_location:
			if (RESULT_OK == resultCode && null != data) {
				workModel.userprovince = data.getIntExtra(Extra.ProvinceCode, 0);
				workProvinceIndex = data.getIntExtra("provinceIndex", 0);
				L.i(Const.AppName,"workProvinceIndex---"+workProvinceIndex);
				workModel.usercity = data.getIntExtra(Extra.CityCode, 0);
				workCityIndex = data.getIntExtra("cityIndex", 0);
				workModel.userdistrict = data.getIntExtra(Extra.DistrictCode, 0);
				workDistrictIndex = data.getIntExtra("districIndex", 0);
				workProvinceName = data.getStringExtra(Extra.ProvinceName);
				workCityName = data.getStringExtra(Extra.CityName);
				workDistrictName = data.getStringExtra(Extra.DistrictName);
				workModel.userfulladdress = workProvinceName+ workCityName+workDistrictName+data.getStringExtra(Extra.Snippet);
				tvWorkLoc.setText(workModel.userfulladdress);
			}
			break;
		case R.id.set_tv_home_location:
			if (RESULT_OK == resultCode && null != data) {
				homeModel.userprovince = data.getIntExtra(Extra.ProvinceCode, 0);
				homeModel.usercity = data.getIntExtra(Extra.CityCode, 0);
				homeModel.userdistrict = data.getIntExtra(Extra.DistrictCode, 0);
				homeProvinceName = data.getStringExtra(Extra.ProvinceName);
				homeCityName = data.getStringExtra(Extra.CityName);
				homeDistrictName = data.getStringExtra(Extra.DistrictName);
				data.getStringExtra(Extra.Snippet);
				homeModel.userfulladdress = homeProvinceName + homeCityName + homeDistrictName + data.getStringExtra(Extra.Snippet);
				tvHomeLoc.setText(homeModel.userfulladdress);
			}
			break;
		case R.id.set_tv_work_time:
			if (RESULT_OK == resultCode && null != data) {

				workModel.beginminute = data.getIntExtra(Extra.Time_Earliest,0);
				workModel.endminute = data.getIntExtra(Extra.Time_Latest,0);
				String beg = data.getStringExtra(Extra.Time_EarliestStr);
				String end = data.getStringExtra(Extra.Time_LatestStr);
				tvWorkTime.setText(beg+" - "+end);
			}
			break;
		case R.id.set_tv_home_time:
			if (RESULT_OK == resultCode && null != data) {
				homeModel.beginminute = data.getIntExtra(Extra.Time_Earliest,0);
				homeModel.endminute = data.getIntExtra(Extra.Time_Latest,0);
				String beg = data.getStringExtra(Extra.Time_EarliestStr);
				String end = data.getStringExtra(Extra.Time_LatestStr);
				tvHomeTime.setText(beg+" - "+end);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public String toString() {
		return "时空设置";
	}
}
