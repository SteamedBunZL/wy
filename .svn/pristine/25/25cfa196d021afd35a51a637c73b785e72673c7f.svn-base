package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.model.EventInfoModel;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 用户对活动的评分
 * @author hyh 
 * creat_at：2013-10-9-上午9:46:30
 */
public class EventUserValuActivity extends BasicActivity implements OnClickListener {
	private RatingBar rbAtmo;
	private TextView tvAtmo;
	private RatingBar rbAttend;
	private TextView tvAttend;
	private RatingBar rbPrice;
	private TextView tvPrice;
	private RatingBar rbAddr;
	private TextView tvAddr;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_user_valuation);
		initView();
		initData();
	}



	private void initView() {
		rbAtmo = (RatingBar)findViewById(R.id.event_valu_rb_atmo);
		tvAtmo = (TextView)findViewById(R.id.event_valu_tv_atmo);
		rbAttend = (RatingBar)findViewById(R.id.event_valu_rb_attend);
		tvAttend = (TextView)findViewById(R.id.event_valu_tv_attend);
		rbPrice = (RatingBar)findViewById(R.id.event_valu_rb_price);
		tvPrice = (TextView)findViewById(R.id.event_valu_tv_price);
		rbAddr = (RatingBar)findViewById(R.id.event_valu_rb_addr);
		tvAddr = (TextView)findViewById(R.id.event_valu_tv_addr);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
	}
	private void initData() {
		id = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == id){
			id = EventDetialManager.getCurrentID();
		}
		EventDetialManager.getInstance().getDetial(id, new CallBack<String>(){
			@Override
			public void onCallBack() {
				EventInfoModel info = EventDetialManager.eventInfo;
				if(null == info){
					return;
				}
				float atmospherevalue = info.getActivityatmospherevalue();
				rbAtmo.setRating(atmospherevalue/2);
				tvAtmo.setText(String.valueOf((int)atmospherevalue));
				float attendedvalue = info.getActivityattendedvalue();
				rbAttend.setRating(attendedvalue/2);
				tvAttend.setText(String.valueOf((int)attendedvalue));
				float pricevalue = info.getActivitypricevalue();
				rbPrice.setRating(pricevalue/2);
				tvPrice.setText(String.valueOf((int)pricevalue));
				float addressvalue = info.getActivityaddressvalue();
				rbAddr.setRating(addressvalue/2);
				tvAddr.setText(String.valueOf((int)addressvalue));
			}
		});
		
		
		
	}
	@Override
	public void onClick(View v) {
		finish();
	}
}
