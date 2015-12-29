package com.whoyao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.model.EventCancelTModel;
import com.whoyao.ui.Toast;

/**
 * 取消参加
 * 
 * @author hyh creat_at：2013-9-17-上午10:40:33
 */
public class EventCancelActivity extends BasicActivity implements OnClickListener, OnItemClickListener {
	private TextView tvReason;
	private EditText etReason;
	private TextView tvNum;
	private BaseAdapter adapter;
	private String[] cancelReasons;
	private ListView lvSpinner;
	private EventCancelTModel model;
	private int dip10;
	private View rlSpinner;
	private int eventId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventId = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == eventId){
			finish();
			return;
		}
		setContentView(R.layout.activity_event_cancel);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.event_cancel_btn_enter).setOnClickListener(this);
		tvReason = (TextView) findViewById(R.id.event_cancel_tv_reason);
		tvReason.setOnClickListener(this);
		etReason = (EditText) findViewById(R.id.event_cancel_et_content);
		tvNum = (TextView) findViewById(R.id.event_cancel_tv_num);
		etReason.addTextChangedListener(new TextNumWatcher(tvNum, 150));
		lvSpinner = (ListView)findViewById(R.id.event_cancel_listview);
		rlSpinner = findViewById(R.id.event_cancel_rl_listview);
		dip10 = (int) getResources().getDimension(R.dimen.dip10);
		cancelReasons = getResources().getStringArray(R.array.cancel_event);
		adapter = new SpinnerAdapter();
		lvSpinner.setAdapter(adapter);
		lvSpinner.setOnItemClickListener(this);
		model = new EventCancelTModel();
		model.setActivityid(getIntent().getIntExtra(Extra.SelectedID, 0));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_cancel_btn_enter:
			model.setActivityid(eventId);
			model.setCancelremark(tvReason.getText().toString());
			model.setCancelremarkcomment(etReason.getText().toString());
			EventEngine.cancel(model, new CallBack<String>(){
				@Override
				public void onCallBack() {
					Toast.show(R.string.warn_event_unjoinsuccess);
					EventDetialManager.getInstance().getDetial(eventId, new CallBack<String>(){
						@Override
						public void onCallBack(boolean isSuccess, String data) {
							finish();
						}
					});
				}
			});
			break;
		case R.id.event_cancel_tv_reason:
			if(View.VISIBLE == rlSpinner.getVisibility()){
				rlSpinner.setVisibility(View.GONE);
			}else{
				rlSpinner.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}

	}


	class SpinnerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return cancelReasons.length;
		}

		@Override
		public Object getItem(int position) {
			return cancelReasons[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(EventCancelActivity.this);
			
			tv.setPadding(dip10, dip10, dip10, dip10);
			if(0 == position){
				tv.setText("请选择");
			}else{
				tv.setText(cancelReasons[position]);
			}
			return tv;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		tvReason.setText(cancelReasons[position]);
		rlSpinner.setVisibility(View.GONE);
	}
}
