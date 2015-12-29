package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

/**
 * @author hyh creat_at：2013-9-24-下午6:34:07
 */
public class EventValuationActivity extends BasicActivity implements OnClickListener {
	private Gallery glAtmo;
	private GalleryAdapter adAtmo;
	private String[] nums = {"0","1","2","3","4","5","6","7","8","9","10"};
	private Gallery glAttend;
	private Gallery glPrice;
	private Gallery glAddr;
	private TextView tvAtmo;
	private TextView tvAttend;
	private TextView tvPrice;
	private TextView tvAddr;
	private GalleryListener listener;
	private View btnEnter;
	private View vTouch;
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_valuation);
		id = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == id){
			id = EventDetialManager.getCurrentID();
		}
		initView();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		
		btnEnter = findViewById(R.id.event_valu_btn_enter);
		btnEnter.setOnClickListener(this);
		tvAtmo = (TextView)findViewById(R.id.event_valu_tv_atmo);
		tvAttend = (TextView)findViewById(R.id.event_valu_tv_attend);
		tvPrice = (TextView)findViewById(R.id.event_valu_tv_price);
		tvAddr = (TextView)findViewById(R.id.event_valu_tv_addr);
		glAtmo = (Gallery) findViewById(R.id.event_valu_gl_atmo);
		glAttend = (Gallery) findViewById(R.id.event_valu_gl_attend);
		glPrice = (Gallery) findViewById(R.id.event_valu_gl_price);
		glAddr = (Gallery) findViewById(R.id.event_valu_gl_addr);
		
		glAtmo.setAdapter(new GalleryAdapter());
		glAttend.setAdapter(new GalleryAdapter());
		glPrice.setAdapter(new GalleryAdapter());
		glAddr.setAdapter(new GalleryAdapter());
		
		listener = new GalleryListener();
		glAtmo.setOnItemSelectedListener(listener);
		glAttend.setOnItemSelectedListener(listener);
		glPrice.setOnItemSelectedListener(listener);
		glAddr.setOnItemSelectedListener(listener);
		
		glAtmo.setSelection(11*1000);
		glAttend.setSelection(11*1000);
		glPrice.setSelection(11*1000);
		glAddr.setSelection(11*1000);
		
		/*vTouch = findViewById(R.id.event_valu_view);
		vTouch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});*/
		
/*		EventInfoModel info = EventDetialManager.eventInfo;
		if(0 != info.getActivityatmospherevalue() ||
			0 != info.getActivityattendedvalue() ||
			0 != info.getActivitypricevalue() ||
			0 != info.getActivityaddressvalue()){
			btnEnter.setVisibility(View.GONE);
			vTouch.setVisibility(View.VISIBLE);
			glAtmo.setSelection((int)info.getActivityatmospherevalue() +12);
			glAttend.setSelection((int)info.getActivityattendedvalue() +12);
			glPrice.setSelection((int)info.getActivitypricevalue() +12);
			glAddr.setSelection((int)info.getActivityaddressvalue() +12);
		 }*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_valu_btn_enter:
			int atmo = glAtmo.getSelectedItemPosition()%nums.length;
			int attend = glAttend.getSelectedItemPosition()%nums.length;
			int price = glPrice.getSelectedItemPosition()%nums.length;
			int addr = glAddr.getSelectedItemPosition()%nums.length;
			if(0 == atmo || 0 == attend || 0 == price || 0 == addr){
				Toast.show("请评分完成后提交！");
				return;
			}
			RequestParams params = new RequestParams();
			params.put(KEY.EventID, id+"");
			params.put("atmospherevalue", atmo+"");
			params.put("attendedvalue", attend+"");
			params.put("pricevalue", price+"");
			params.put("addressvalue", addr+"");
			
			Net.request(params , WebApi.Event.AddValuation, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					EventDetialManager.getInstance().getDetial(id, new CallBack<String>(){
						@Override
						public void onCallBack(boolean isSuccess, String data) {
							finish();
						}
					});
				}
			});
			break;
		default:
			break;
		}

	}

	class GalleryAdapter extends BaseAdapter {
		private int selectItem;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = (TextView) convertView;
			if (null == v) {
				v = (TextView) View.inflate(AppContext.curActivity, R.layout.item_event_valu, null);
			}
			if (position == selectItem) {
				v.setTextColor(Color.WHITE);
			}
			v.setText(nums[position%nums.length]);
			return v;
		}

		public void setSelectItem(int selectItem) {
			if (this.selectItem != selectItem) {
				this.selectItem = selectItem;
				notifyDataSetChanged();
			}
		}

		@Override
		public long getItemId(int position) {
			return position%nums.length;
		}

		@Override
		public Object getItem(int position) {
			return nums[position%nums.length];
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
	}
	
	class GalleryListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			((GalleryAdapter)parent.getAdapter()).setSelectItem(position);
			position = position%nums.length;
			switch (parent.getId()) {
			case R.id.event_valu_gl_atmo:
				tvAtmo.setText(EventEngine.getAtmosphereDesc(position));
				break;
			case R.id.event_valu_gl_attend:
				tvAttend.setText(EventEngine.getAttendedDesc(position));
				break;
			case R.id.event_valu_gl_price:
				tvPrice.setText(EventEngine.getPriceDesc(position));
				break;
			case R.id.event_valu_gl_addr:
				tvAddr.setText(EventEngine.getAddressDesc(position));
				break;
			default:
				break;
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	}
}
