package com.whoyao.activity;

import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.adapter.IntWheelAdapter;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.L;
import com.whoyao.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 选择年月日
 * @author HYH create at：2013-4-1 上午11:00:48
 */
public class SelectDateActivity extends AbsSelectActivity implements OnClickListener, OnWheelChangedListener, OnWheelScrollListener {

	private WheelView yearWheel;
	private WheelView monthWheel;
	private WheelView dayWheel;

	private final int VISIBLE_ITEMS = 5;
	private int currentYearIndex = 0;
	private int currentMonthIndex = 0;
	private int currentDayIndex = 0;
	private int[] years;
	private int[] months;
	private int[] days;
//	private String birthdayStr;

	private int currentDay;
	private int currentMonth;
	private int currentYear;
	private boolean isTimeChange;
	private Context context;
	private Button wheelCancle;
	private Button wheelOK;
	private long defaultTimeMillis;
	private boolean isScrolling;
	private View chooseLl;
	private int[] currentMonths;
	private int[] currentdays;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_date);
		context = this;
		initDate();
		init();
	}

	private void init() {
		
		chooseLl = findViewById(R.id.wheel_ll);
		setSelectView(chooseLl);
		show();
		
		wheelCancle = (Button) findViewById(R.id.wheel_cancel);
		wheelOK = (Button) findViewById(R.id.wheel_ok);

		wheelCancle.setOnClickListener(this);
		wheelOK.setOnClickListener(this);

		yearWheel = (WheelView) findViewById(R.id.wheel_date_year);
		monthWheel = (WheelView) findViewById(R.id.wheel_date_month);
		dayWheel = (WheelView) findViewById(R.id.wheel_date_day);

		// 需要先初始化当前年月日
		yearWheel.setCurrentItem(currentYearIndex);// 第0条15岁,默认20岁
		monthWheel.setCurrentItem(currentMonthIndex);
		dayWheel.setCurrentItem(currentDayIndex);
		yearWheel.setViewAdapter(new IntWheelAdapter(context, years, " 年"));
		monthWheel.setViewAdapter(new IntWheelAdapter(context, months, " 月"));

		// ArrayWheelAdapter<Integer> arrayWheelAdapter = new
		// ArrayWheelAdapter<Integer>(context, years);

		dayWheel.setViewAdapter(new IntWheelAdapter(context, getDayOfMonth(), " 日"));
		yearWheel.addChangingListener(this);
		monthWheel.addChangingListener(this);
		dayWheel.addChangingListener(this);
		yearWheel.addScrollingListener(this);
		yearWheel.setCurrentItem(20);
		monthWheel.setCurrentItem(currentMonth);
		dayWheel.setCurrentItem(currentDay - 1);
		yearWheel.setVisibleItems(VISIBLE_ITEMS);
		monthWheel.setVisibleItems(VISIBLE_ITEMS);
		dayWheel.setVisibleItems(VISIBLE_ITEMS);

//		try {
//			
//			birthdayMillis = CalendarUtils.parseDate(MyinfoManager.userInfo.getUserBirthday());
//		} catch (Exception e) {
//			AppException.handle(e);
//			birthdayMillis = 0;
//		}
		defaultTimeMillis = getIntent().getLongExtra(Extra.DefaultTimeMillis, 0);
		if (0 != defaultTimeMillis && -1 != defaultTimeMillis) {

			Calendar day = CalendarUtils.getNewCalendar();
			day.setTimeInMillis(defaultTimeMillis);
			int yearIndex = years[0] - day.get(Calendar.YEAR);
			int monthIndex = day.get(Calendar.MONTH);
			int dayIndex = day.get(Calendar.DAY_OF_MONTH) - 1;
			yearWheel.setCurrentItem(yearIndex);
			monthWheel.setCurrentItem(monthIndex);
			dayWheel.setCurrentItem(dayIndex);
			isTimeChange = false;

		}
		monthWheel.setCyclic(true);
		dayWheel.setCyclic(true);
	}

	/**
	 * 初始化日期相关数据
	 */
	private void initDate() {
		Calendar cal = CalendarUtils.getCalendar();
		currentDay = cal.get(Calendar.DATE);
		currentMonth = cal.get(Calendar.MONTH);
		currentYear = cal.get(Calendar.YEAR);

		years = new int[currentYear - 1900 +1];
		int lastYear = currentYear;
		for (int i = 0; i < years.length; i++) {
			years[i] = lastYear--;
		}
		months = Utils.getRiseArray(1, 12);
		currentMonths = Utils.getRiseArray(1, currentMonth + 1);
		currentdays = Utils.getRiseArray(1, currentDay);
	}

	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		L.i("WhoYao", "wheel state = "+isScrolling);
		isTimeChange = true;
		switch (wheel.getId()) {
		case R.id.wheel_date_year:
			currentYearIndex = newValue;
			if(oldValue != 0 && newValue == 0){//月改为当前,日需要判断
				monthWheel.setViewAdapter(new IntWheelAdapter(context, currentMonths, " 月"));
				if (currentMonthIndex >= currentMonths.length) {
					currentMonthIndex = currentMonths.length - 1;
					monthWheel.setCurrentItem(currentMonthIndex);
				}
				if(currentMonths.length < VISIBLE_ITEMS){
					monthWheel.setCyclic(false);
				}
				onChanged(monthWheel, currentMonthIndex, currentMonthIndex);
				
			}else if(oldValue == 0 && newValue != 0){//月改为12,日改为普通
				monthWheel.setViewAdapter(new IntWheelAdapter(context, months, " 月"));
				if (currentMonthIndex >= months.length) {
					currentMonthIndex = months.length - 1;
					monthWheel.setCurrentItem(currentMonthIndex);
					monthWheel.setCyclic(true);
				}
				dayWheel.setViewAdapter(new IntWheelAdapter(context, getDayOfMonth(), " 日"));
				if (currentDayIndex >= days.length) {
					currentDayIndex = days.length - 1;
					dayWheel.setCurrentItem(currentDayIndex);
				}
			}else{
				monthWheel.setCyclic(true);
				dayWheel.setViewAdapter(new IntWheelAdapter(context, getDayOfMonth(), " 日"));
				if (currentDayIndex >= days.length) {
					currentDayIndex = days.length - 1;
					dayWheel.setCurrentItem(currentDayIndex);
				}
			}

			break;
		case R.id.wheel_date_month://是当前年,当前有,日要改
			currentMonthIndex = newValue;
			if(currentYearIndex == 0 && currentMonthIndex == currentMonths.length - 1){
				dayWheel.setViewAdapter(new IntWheelAdapter(context, currentdays, " 日"));
				if (currentDayIndex >= currentdays.length) {
					currentDayIndex = currentdays.length - 1;
					dayWheel.setCurrentItem(currentDayIndex);
				}
				if(currentdays.length < VISIBLE_ITEMS){
					dayWheel.setCyclic(false);
				}
			}else{
				dayWheel.setViewAdapter(new IntWheelAdapter(context, getDayOfMonth(), " 日"));
				if (currentDayIndex >= days.length) {
					currentDayIndex = days.length - 1;
					dayWheel.setCurrentItem(currentDayIndex);
				}
				dayWheel.setCyclic(true);
			}
			
			
//			birthdayTV.setText(getDateStr());// 日期
//			constellationTV.setText(getConsStr());// 星座
			break;
		case R.id.wheel_date_day:
			currentDayIndex = newValue;
//			birthdayTV.setText(getDateStr());// 日期
//			constellationTV.setText(getConsStr());// 星座
			break;
		}
	}

	private int[] getDayOfMonth() {
		days = CalendarUtils.getDaysArray(years[currentYearIndex], months[currentMonthIndex]);			
		return days;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_cancel:
			dismiss();
			break;
		case R.id.wheel_ok:
			// BirthStr需要格式为:"yyyy-MM-dd HH:mm:ss"
			if (isTimeChange) {
				@SuppressWarnings("deprecation")
				Date date = new Date(years[currentYearIndex], months[currentMonthIndex],  days[currentDayIndex]);
				Intent data = new Intent();
				data.putExtra(Extra.SelectedTime, date.getTime());
				data.putExtra(Extra.SelectedTimeStr, getDateStr());
				setResult(RESULT_OK, data );
			}
			dismiss();
			break;
		}

	}

	private String getDateStr() {
		String monthStr = null;
		String dayStr = null;
		if(10>months[currentMonthIndex]){
			monthStr = "0"+months[currentMonthIndex];
		}else{
			monthStr = ""+months[currentMonthIndex];
		}
		if(10>days[currentDayIndex]){
			dayStr = "0"+days[currentDayIndex];
		}else{
			dayStr = ""+days[currentDayIndex];
		}
		return years[currentYearIndex] + "-" + monthStr + "-" + dayStr + " 00:00:00";
		//return years[currentYearIndex] + "年" + months[currentMonthIndex] + "月" + days[currentDayIndex] + "日";
	}
//
//	private String getConsStr() {
//		int month = currentMonthIndex + 1;
//		int day = days[currentDayIndex];
//		String constellation = CalendarUtils.getConstellation(month, day);
//		return constellation;
//	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();		
		}
		return true;
	}
	
	@Override
	public void onScrollingStarted(WheelView wheel) {
		isScrolling = true;
	}
	
	@Override
	public void onScrollingFinished(WheelView wheel) {
		isScrolling = false;
	}

	@Override
	public String toString() {
		return "选择年月日";
	}
}
