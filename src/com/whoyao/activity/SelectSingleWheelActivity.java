package com.whoyao.activity;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import com.whoyao.Const.Extra;
import com.whoyao.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 选择界面(一个滚轴)
 * @author hyh creat_at：2013-8-6-下午4:10:54
 */
public class SelectSingleWheelActivity extends AbsSelectActivity implements OnClickListener {
	private WheelView wheelView;
	private View selectLayout;
	private String[] dataArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_singlewheel);
		initView();
		show();
	}

	private void initView() {
		wheelView = (WheelView) findViewById(R.id.wheel_view);
		selectLayout = findViewById(R.id.wheel_ll);
		setSelectView(selectLayout);
		findViewById(R.id.wheel_ok).setOnClickListener(this);
		findViewById(R.id.wheel_cancel).setOnClickListener(this);
		
		getData();
		wheelView.setViewAdapter(new ArrayWheelAdapter<String>(this, dataArray));
		if(5>dataArray.length){
			wheelView.setCurrentItem(dataArray.length/2);
		}else{
			wheelView.setCurrentItem(2);
		}
	}

	private void getData() {
		Intent intent = getIntent();
		int arrayRes = intent.getIntExtra(Extra.ArrayRes, 0);
		if(0 != arrayRes){
			dataArray = getResources().getStringArray(arrayRes);
			if(0 == dataArray.length){
				dataArray = null;
			}
			return;
		}
		dataArray = intent.getStringArrayExtra(Extra.ArrayStr);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_ok:
			Intent data = new Intent();
			data.putExtra(Extra.SelectedItem, wheelView.getCurrentItem());
			data.putExtra(Extra.SelectedItemStr, dataArray[wheelView.getCurrentItem()]);
			setResult(RESULT_OK, data);
			dismiss();
			break;
		case R.id.wheel_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "选择界面(一个滚轴)";
	}
}
