package com.whoyao.activity;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 选择起止时间
 * @author hyh 
 * creat_at：2013-8-16-上午8:37:25
 */
public class SelectDoubleHourActivity extends AbsSelectActivity implements OnClickListener, OnWheelChangedListener {
	private WheelView wvBegHour;
	private WheelView wvBegMinute;
	private WheelView wvEndHour;
	private WheelView wvEndMinute;
	private String[] minutes;
	private boolean isSettingChanged;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_double_hour);
		setSelectView(findViewById(R.id.wheel_ll));
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		wvBegHour = (WheelView)findViewById(R.id.wheel_begin_hour);
		wvBegMinute = (WheelView)findViewById(R.id.wheel_begin_minute);
		wvEndHour = (WheelView)findViewById(R.id.wheel_end_hour);
		wvEndMinute = (WheelView)findViewById(R.id.wheel_end_minute);
//		minutes = new String[]{"00","30"};
		minutes = CalendarUtils.getMinutes();
		wvBegHour.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
		wvEndHour.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
		wvBegMinute.setViewAdapter(new ArrayWheelAdapter<String>(this, minutes));
		wvEndMinute.setViewAdapter(new ArrayWheelAdapter<String>(this, minutes));
		
		wvBegHour.addChangingListener(this);
		wvEndHour.addChangingListener(this);
		wvBegMinute.addChangingListener(this);
		wvEndMinute.addChangingListener(this);
		wvBegHour.setCyclic(true);
		wvEndHour.setCyclic(true);
		show();
		int currentHour = CalendarUtils.getNewCalendar().get(Calendar.HOUR_OF_DAY);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		if (type.equals("workTime")) {
			wvBegHour.setCurrentItem(8);
			wvEndHour.setCurrentItem(20);
		}
		else if (type.equals("homeTime")) {
			wvBegHour.setCurrentItem(22);
			wvEndHour.setCurrentItem(5);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_cancel:
			dismiss();
			break;
		case R.id.wheel_ok:
			//  返回数据
			if(isSettingChanged){
				String beginStr = wvBegHour.getCurrentItem()+":"+minutes[wvBegMinute.getCurrentItem()];
				String endStr = wvEndHour.getCurrentItem()+":"+minutes[wvEndMinute.getCurrentItem()];
				int beginMinute = wvBegHour.getCurrentItem() * 60 + wvBegMinute.getCurrentItem();
				int endMinute = wvEndHour.getCurrentItem() * 60 + wvEndMinute.getCurrentItem();
				int duringMinute = endMinute - beginMinute;
				if (duringMinute>=0&&duringMinute<60) {
					if (type.equals("workTime")) {
						Toast.show("工作时间需大于1小时");
					}
					else if (type.equals("homeTime")) {
						Toast.show("休息时间需大于1小时");
					}
					return;
				}
				Intent data = new Intent();
				data.putExtra(Extra.Time_EarliestStr, beginStr);
				data.putExtra(Extra.Time_LatestStr, endStr);
				data.putExtra(Extra.Time_Earliest, beginMinute);
				data.putExtra(Extra.Time_Latest, endMinute);
				setResult(RESULT_OK, data);
			}
			dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		isSettingChanged = true;
	}
	
	@Override
	public String toString() {
		return "选择起止时间";
	}
}
