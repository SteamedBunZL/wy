package com.whoyao.activity;

import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.adapter.IntWheelAdapter;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 设置时间(月日时,需指定范围)
 * 
 * @author HYH create at：2013-4-8 下午01:08:40
 */
public class SelectTimeMinuteActivity extends AbsSelectActivity implements
		OnClickListener, OnWheelChangedListener {
	private WheelView monthWheel;
	private WheelView dayWheel;
	private WheelView hourWheel;
	private WheelView minuteWheel;
	private int dayNum;
	private int years[];
	private int months[];
	private int days[];
	private int hours[];
	private int minutes[];
	private int firstDay;
	private int lastDay;
	private int firstMonth;
	private int lastMonth;
	private int firstYear;
	private int lastYear;
	private int firstHour;
	private int lastHour;
	private int firstMinute;
	private int lastMinute;
	private Calendar currentCal;
	private int currentDay = 0;
	private String currentHour;
	private String currentMinute;
	private TextView tvYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_select_time);
		setSelectView(findViewById(R.id.wheel_layout));
		tvYear = (TextView) findViewById(R.id.wheel_tv_year);
		currentCal = CalendarUtils.getNewCalendar();
		monthWheel = (WheelView) findViewById(R.id.wheel_view_month);
		dayWheel = (WheelView) findViewById(R.id.wheel_view_day);
		hourWheel = (WheelView) findViewById(R.id.wheel_view_hour);
		minuteWheel = (WheelView) findViewById(R.id.wheel_view_minute);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		firstMonth = currentCal.get(Calendar.MONTH) + 1;
		if (firstMonth ==12) {
			lastMonth = 1;
		}
		if (currentCal.get(Calendar.MONTH) + 1 == 12
				&& currentCal.get(Calendar.DAY_OF_MONTH) == 31) {
			months = new int[2];
			months[0] = firstMonth;
			months[1] = lastMonth;
			firstYear = currentCal.get(Calendar.YEAR);
			lastYear = firstYear + 1;

		} else {
			months = new int[1];
			months[0] = firstMonth;
			firstYear = currentCal.get(Calendar.YEAR);
		}
		tvYear.setText(firstYear+"");
		initDate();

	}

	private void initDate() {
		// year

		firstDay = currentCal.get(Calendar.DAY_OF_MONTH);
		lastDay = currentCal.get(Calendar.DATE) + 1;
		if (lastDay>currentCal.getMaximum(Calendar.MONTH)) {
			lastDay=1;
		}
		if (lastDay==1) {
			days = new int[1];
			days[0] = firstDay;
		}else {
			days = new int[2];
			days[0] = firstDay;
			days[1] = lastDay;
		}
		firstHour = currentCal.get(Calendar.HOUR_OF_DAY);
		lastHour = 23;
		firstMinute = currentCal.get(Calendar.MINUTE);
		if ((firstMinute + 40) < 60) {
			firstMinute += 40;
			
		} else {
			firstMinute = firstMinute + 40 - 60;
			firstHour+=1;
		}
		

		lastMinute = 59;
		monthWheel.setViewAdapter(new IntWheelAdapter(this, months, " 月"));
		dayWheel.setViewAdapter(new IntWheelAdapter(this, days, "日"));
		
		hourWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstHour, lastHour),
				"时"));
		minuteWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstMinute, lastMinute),
				"分"));
		monthWheel.addChangingListener(this);
		dayWheel.addChangingListener(this);
		hourWheel.addChangingListener(this);
		minuteWheel.addChangingListener(this);
		show();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		switch (wheel.getId()) {
		case R.id.wheel_view_month:
			changeMonth(newValue);
			changeHour(newValue);
			changeMinute(0);
			dayWheel.setViewAdapter(new IntWheelAdapter(this, days, "日"));
			hourWheel.setCurrentItem(0);
			hourWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstHour, lastHour),
					"时"));
			minuteWheel.setCurrentItem(0);
			minuteWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstMinute, lastMinute),
					"分"));

			break;
		case R.id.wheel_view_day:
			changeHour(newValue);
			changeMinute(0);
			hourWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstHour, lastHour),
					"时"));
			hourWheel.setCurrentItem(0);
			minuteWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstMinute, lastMinute),
					"分"));
			break;
		case R.id.wheel_view_hour:
			changeMinute(newValue);
			minuteWheel.setViewAdapter(new IntWheelAdapter(this, Utils.getRiseArray(firstMinute, lastMinute),
					"分"));
			minuteWheel.setCurrentItem(0);
			break;
		case R.id.wheel_view_minute:
			
			break;

		}

	}

	public void newHours(int num, int firsHour) {
		hours = new int[num];
		for (int i = 0; i < num - 1; i++) {
			hours[i] = firsHour;
			firsHour++;
		}

	}

	public void newMinutes(int num, int firstMinute) {
		minutes = new int[num];
		for (int i = 0; i < num; i++) {
			hours[i] = firstMinute;
			firstMinute++;
		}

	}
	public void changeMonth(int newValue){
		if (newValue==1) {
			days[0] = 1;
			firstYear = currentCal.get(Calendar.YEAR)+1;
			tvYear.setText(firstYear+"");
		}else {
			days[0] = firstDay;
			firstYear = currentCal.get(Calendar.YEAR);
			tvYear.setText(firstYear+"");
		}
	}
	public void changeHour(int newValue) {
		if (newValue == 1) {
			lastHour = firstHour;
			firstHour = 0;
			// newHours(lastHour-firstHour+1, firstHour);
			currentDay = 1;
		} else {
			firstHour = currentCal.get(Calendar.HOUR_OF_DAY);
			firstMinute = currentCal.get(Calendar.MINUTE);
			if ((firstMinute + 40) < 60) {
				
			} else {
				firstHour+=1;
			}
			lastHour = 23;
			// newHours(lastHour-firstHour+1, firstHour);
			currentDay = 0;
		}
	}

	public void changeMinute(int newValue) {
		if (currentDay == 0) {
			if (newValue != 0) {
				lastMinute = 59;
				firstMinute = 0;
			} else {
				firstMinute = currentCal.get(Calendar.MINUTE);
				if ((firstMinute + 40) < 60) {
					firstMinute += 40;
					
				} else {
					firstMinute = firstMinute + 40 - 60;
				}
				lastMinute = 59;
			}
			// newMinutes(lastMinute-firstMinute+1, firstMinute);
		} else {
			if (newValue == (currentCal.get(Calendar.HOUR_OF_DAY)+1)) {
				firstMinute = 0;
				lastMinute = currentCal.get(Calendar.MINUTE);
			} else {
				lastMinute = 59;
				firstMinute = 0;
			}
			// newMinutes(lastMinute-firstMinute+1, firstMinute);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_ok:
			currentHour = hourWheel.getCurrentItem() + firstHour + "";
			currentMinute = minuteWheel.getCurrentItem() + firstMinute + "";
			// if ((hourWheel.getCurrentItem()+firstHour+"").length()==1) {
			// currentHour = "0"+hourWheel.getCurrentItem()+firstHour;
			// }
			// if ((minuteWheel.getCurrentItem()+firstMinute+"").length()==1) {
			// currentMinute = "0"+minuteWheel.getCurrentItem()+firstMinute;
			// }
			String time = firstYear + "-" + months[monthWheel.getCurrentItem()]
					+ "-" + days[dayWheel.getCurrentItem()] + " " + currentHour
					+ ":" + currentMinute + ":00";
			long timeMills = CalendarUtils.parseDate(time);
			Intent data = new Intent();
			data.putExtra(Extra.SelectedTime, timeMills);
			setResult(RESULT_OK, data);
			dismiss();
			
			break;
		case R.id.wheel_cancel:
			dismiss();
			break;

		}

	}

}
