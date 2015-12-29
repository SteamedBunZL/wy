package com.whoyao.activity;

import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.adapter.IntWheelAdapter;
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
public class SelectTimeHalfHourActivity extends AbsSelectActivity implements OnClickListener, OnWheelChangedListener {

	public static final String BEGINTIME_TAG = "BeginTime";
	public static final String ENDTIME_TAG = "EndTime";
	public static final long Mills_of_One_Month = 2592000000L;
	private WheelView monthWheel;
	private WheelView dayWheel;
	private WheelView hourWheel;
	private TextView yearTv;
	
	private long earliest;
	private long latest;
	private Calendar earlyCal;
	private Calendar lastCal;
	private int monthNum;
	private int yearNum;
	private int[] years;
	private int[] months;
	private int[] days;
	private int[] firstDays;
	private int[] lastDays;
	private String[] hours;
	private String[] firstHours;
	private String[] lastHours;

	private int mounthState;
	private String[] allHalfHour;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_time);
		Intent intent = getIntent();
		earliest = intent.getLongExtra(Extra.Time_Earliest, -1) - 1;
		latest = intent.getLongExtra(Extra.Time_Latest, -1) - 1;
		
		if (-1 == earliest || -1 == latest) {
			// throw new NullPointerException("SelectTime:未设置初始值or初始时间错误");
			earliest = CalendarUtils.getNewCalendar().getTimeInMillis();
			latest = earliest + Mills_of_One_Month;//一个月
		}
		earlyCal = CalendarUtils.getNewCalendar();
		earlyCal.setTime(new Date(earliest));
		lastCal = CalendarUtils.getNewCalendar();
		lastCal.setTime(new Date(latest));


		initDate();
		initView();
	}

	private void initView() {

		setSelectView(findViewById(R.id.wheel_layout));
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		findViewById(R.id.wheel_cancel).setOnClickListener(this);

		yearTv = (TextView) findViewById(R.id.wheel_tv_year);
		monthWheel = (WheelView) findViewById(R.id.wheel_view_month);
		dayWheel = (WheelView) findViewById(R.id.wheel_view_day);
		hourWheel = (WheelView) findViewById(R.id.wheel_view_hour);
		
		
		if (0 ==yearNum) {// 同一年,不显示年份
			//yearTv.setVisibility(View.INVISIBLE);
		}else{
			yearTv.setVisibility(View.VISIBLE);
		}

		monthWheel.setViewAdapter(new IntWheelAdapter(this, months, " 月"));
		dayWheel.setViewAdapter(new IntWheelAdapter(this, days, " 日"));
		hourWheel.setViewAdapter(new ArrayWheelAdapter<String>(this, hours));

		monthWheel.addChangingListener(this);
		dayWheel.addChangingListener(this);
		hourWheel.addChangingListener(this);
		show();
	}

	/**
	 * 初始化日期相关数据
	 */
	private void initDate() {
		// 年月
		yearNum = lastCal.get(Calendar.YEAR) - earlyCal.get(Calendar.YEAR);
		int e = earlyCal.get(Calendar.MONTH);
		int l = lastCal.get(Calendar.MONTH);
		monthNum = 12*yearNum - e + l +1;
		
		years = new int[monthNum];
		months = new int[monthNum];
		int startMonth = earlyCal.get(Calendar.MONTH);
		int startYear = earlyCal.get(Calendar.YEAR);

		for(int i = 0;i<monthNum;i++){
			startMonth +=1;
			if(startMonth == 13){
				startMonth = 1;
				startYear +=1;
			}
			months[i] = startMonth;
			years[i]=startYear;
		}

		//日
		earlyCal.setTimeInMillis(earliest);
		if(1 != earlyCal.get(Calendar.DAY_OF_MONTH)){//如果是X月的第一天
			firstDays = Utils.getRiseArray(earlyCal.get(Calendar.DAY_OF_MONTH), CalendarUtils.getDaysOfMonth(years[0], months[0]));
		}else{
			firstDays = CalendarUtils.getDaysArray(years[0], months[0]);
		}
		if(28 > lastCal.get(Calendar.DAY_OF_MONTH)){
			lastDays = Utils.getRiseArray(1, lastCal.get(Calendar.DAY_OF_MONTH));
		}else{
			lastDays = CalendarUtils.getDaysArray(lastCal.get(Calendar.DAY_OF_MONTH));
		}
		if(1 == months.length){
			firstDays = lastDays = Utils.getRiseArray(earlyCal.get(Calendar.DAY_OF_MONTH), lastCal.get(Calendar.DAY_OF_MONTH));
		}
		days = firstDays;
		
		//时
		if(1 == months.length && 1 == days.length){//如果只有一个月,且这个月只有1天
			firstHours = lastHours = CalendarUtils.getBeginHalfHourArray(earlyCal.get(Calendar.HOUR_OF_DAY), earlyCal.get(Calendar.MINUTE));
		}else{
			firstHours = CalendarUtils.getBeginHalfHourArray(earlyCal.get(Calendar.HOUR_OF_DAY), earlyCal.get(Calendar.MINUTE));
			lastHours = CalendarUtils.getEndHalfHourArray(lastCal.get(Calendar.HOUR_OF_DAY), lastCal.get(Calendar.MINUTE));
		}
		hours = firstHours;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_ok:
			Intent data = new Intent();
			String timeString = null;
			String timeYear =years[monthWheel.getCurrentItem()]+"-";
			String timeMonth = months[monthWheel.getCurrentItem()]+"-";
			String timeDay = days[dayWheel.getCurrentItem()]+" ";
			String timeHour = hours[hourWheel.getCurrentItem()] +":00";
			if (timeMonth.length()==2) {
				if (timeDay.length()==2) {
					timeMonth = "0"+timeMonth;
					timeDay = "0" + timeDay;
				}else {
					timeMonth = "0"+timeMonth;
				}
			}else {
				if (timeDay.length()==2) {
					timeDay = "0"+timeDay;
				}
			}
			if (timeHour.length()==7) {
				timeHour = "0"+timeHour;
			}
			timeString = timeYear+timeMonth+timeDay+timeHour;
			long timeMills = CalendarUtils.parseDate(timeString);
			data.putExtra(Extra.SelectedTimeStr, timeString);
			data.putExtra(Extra.SelectedTime, timeMills);
			setResult(RESULT_OK, data);
			dismiss();
			break;
		case R.id.wheel_cancel:
			dismiss();
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		switch (wheel.getId()) {
		case R.id.wheel_view_month:
			mounthState = 0;
			System.out.println("*oldValue***"+oldValue+"*newValue*****"+newValue);
			days = CalendarUtils.getDaysArray(years[newValue], months[newValue]);
			if(0 == newValue){//第一个月
				mounthState = 1;
				days = firstDays;
			}else if(months.length-1 == newValue){//最后一个月
				mounthState = -1;
				days = lastDays;
			}
			yearTv.setText(years[newValue]+"年");
			dayWheel.setViewAdapter(new IntWheelAdapter(this, days, " 日"));
			onChanged(dayWheel, 0, dayWheel.getCurrentItem());
			break;
		case R.id.wheel_view_day:
			hours = getHalfHour();
			if(1 == months.length){
				if(0 == newValue){
					hours = firstHours;
				}else if(days.length == newValue+1){
					hours = lastHours;
				}
			}else{
				if(1 == mounthState && 0 == newValue){
					hours = firstHours;
				}else if(-1 == mounthState && days.length == newValue+1){
					hours = lastHours;
				}
			}
			hourWheel.setViewAdapter(new ArrayWheelAdapter<String>(this, hours));
			hourWheel.setCurrentItem(0);
			break;
		case R.id.wheel_view_hour:
			
			break;
		default:
			break;
		}
	}

	private String[] getHalfHour(){
		if(null == allHalfHour){
			allHalfHour = CalendarUtils.getHalfHourArray();
		}
		return allHalfHour;
	}
	
	@Override
	public String toString() {
		return "设置时间(月日时,需指定范围)";
	}
}
