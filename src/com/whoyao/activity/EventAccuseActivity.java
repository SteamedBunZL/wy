package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.Const.Extra;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.model.EventCancelTModel;
import com.whoyao.ui.Toast;

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

/**
 * 举报发起人
 * 
 * @author hyh creat_at：2013-9-17-上午10:40:33
 */
public class EventAccuseActivity extends BasicActivity implements OnClickListener, OnItemClickListener {
	private TextView tvReason;
	private EditText etReason;
	private TextView tvNum;
	private BaseAdapter adapter;
	private String[] accuseReasons;
	private ListView lvSpinner;
	private EventCancelTModel model;
	private int dip10;
	private View rlSpinner;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_accuse);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.event_accuse_btn_enter).setOnClickListener(this);
		tvReason = (TextView) findViewById(R.id.event_accuse_tv_reason);
		tvReason.setOnClickListener(this);
		etReason = (EditText) findViewById(R.id.event_accuse_et_content);
		tvNum = (TextView) findViewById(R.id.event_accuse_tv_num);
		etReason.addTextChangedListener(new TextNumWatcher(tvNum, 200));
		lvSpinner = (ListView)findViewById(R.id.event_accuse_listview);
		rlSpinner = findViewById(R.id.event_accuse_rl_listview);
		dip10 = (int) getResources().getDimension(R.dimen.dip10);
		accuseReasons = getResources().getStringArray(R.array.accuse_creater);
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
		case R.id.event_accuse_btn_enter:
			model.setCancelremark(tvReason.getText().toString());
			model.setCancelremarkcomment(etReason.getText().toString());
			EventEngine.accuse(model, new CallBack<String>(){
				@Override
				public void onCallBack() {
					Toast.show("举报成功");
					finish();
				}
			});
			break;
		case R.id.event_accuse_tv_reason:
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
			return accuseReasons.length;
		}

		@Override
		public Object getItem(int position) {
			return accuseReasons[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(EventAccuseActivity.this);
			tv.setPadding(dip10, dip10, dip10, dip10);
			if(0 == position){
				tv.setText("请选择");
			}else{
				tv.setText(accuseReasons[position]);
			}
			return tv;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		tvReason.setText(accuseReasons[position]);
		rlSpinner.setVisibility(View.GONE);
	}
}
