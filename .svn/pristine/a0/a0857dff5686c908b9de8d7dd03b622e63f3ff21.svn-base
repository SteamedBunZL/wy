package com.whoyao.activity;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 选择起止时间
 * @author hyh 
 * creat_at：2013-8-16-上午8:37:25
 */
public class SelectThreeNumberActivity extends AbsSelectActivity implements OnClickListener {


	private WheelView wvHundred;
	private WheelView wvTen;
	private WheelView wvYuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_three_number);
		setSelectView(findViewById(R.id.wheel_ll));
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		
		wvHundred = (WheelView)findViewById(R.id.wheel_hundred);
		wvTen = (WheelView)findViewById(R.id.wheel_ten);
		wvYuan = (WheelView)findViewById(R.id.wheel_yuan);
		
		wvHundred.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
		wvTen.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
		wvYuan.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
		
		int defaultNum = getIntent().getIntExtra(Extra.SelectedItem, 0);
		int defaultH = defaultNum /100;
		defaultNum = defaultNum % 100;
		int defaultT = defaultNum /10;
		int defaultY = defaultNum % 10;
		wvHundred.setCurrentItem(defaultH);
		wvTen.setCurrentItem(defaultT);
		wvYuan.setCurrentItem(defaultY);
		show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_cancel:
			dismiss();
			break;
		case R.id.wheel_ok:
			//  返回数据
			Intent data = new Intent();
			int num = wvHundred.getCurrentItem() * 100 + wvTen.getCurrentItem() * 10 + wvYuan.getCurrentItem();
			data.putExtra(Extra.SelectedItem, num);
			setResult(RESULT_OK, data);
			dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "选择活动费用";
	}
	
}
