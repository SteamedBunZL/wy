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
public class SelectDoubleNumberActivity extends AbsSelectActivity implements OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
	private WheelView wvLeft;
	private WheelView wvRight;
	private boolean isScrolling;
	private int max;
	private int min;
	private int rightMin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_double_number);
		setSelectView(findViewById(R.id.wheel_ll));
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		wvLeft = (WheelView)findViewById(R.id.wheel_left);
		wvRight = (WheelView)findViewById(R.id.wheel_right);
		
		Intent data = getIntent();
		max = data.getIntExtra(Extra.Max, 200);
		min = data.getIntExtra(Extra.Min, 1);
		rightMin = min;
		wvLeft.setViewAdapter(new NumericWheelAdapter(this, min, max));
		wvRight.setViewAdapter(new NumericWheelAdapter(this, min, max));
		
		wvLeft.addChangingListener(this);
		wvRight.addChangingListener(this);
		wvLeft.addScrollingListener(this);
		wvRight.addScrollingListener(this);

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
			data.putExtra(Extra.Min, wvLeft.getCurrentItem() + min);
			data.putExtra(Extra.Max, wvRight.getCurrentItem() +rightMin);
			setResult(RESULT_OK, data);
			dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if(!isScrolling){
			switch (wheel.getId()) {
			case R.id.wheel_left:
				refreshRight();
				break;
			default:
				break;
			}
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
		case R.id.wheel_left:
			refreshRight();
			break;
		default:
			break;
		}
	}
	
	private void refreshRight(){
		int temp = wvLeft.getCurrentItem() + min;
		if((rightMin + wvRight.getCurrentItem()) > temp){
			wvRight.setCurrentItem(rightMin + wvRight.getCurrentItem() - temp);
		}else{
			wvRight.setCurrentItem(0);
		}
		rightMin = temp;
		wvRight.setViewAdapter(new NumericWheelAdapter(this, rightMin, max));
	}
	
	@Override
	public String toString() {
		return "选择活动人数";
	}
	
}
