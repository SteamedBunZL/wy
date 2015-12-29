package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.model.EventJoinerMgrModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.Utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 意见反馈
 * @author hyh 
 * creat_at：2013-8-12-下午5:30:02
 */
public class EventJoinerRemarkActivity extends BasicActivity implements OnClickListener {
	private EditText etRemark;
	private TextView tvNum;
	private EventJoinerMgrModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_joiner_remark);
		etRemark = (EditText)findViewById(R.id.joiner_remark_et_content);
		tvNum = (TextView)findViewById(R.id.joiner_remark_tv_num);
		etRemark.addTextChangedListener(new TextNumWatcher(tvNum, 50));
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.joiner_remark_btn_enter).setOnClickListener(this);
		model = (EventJoinerMgrModel) getIntent().getSerializableExtra(Extra.SelectedItem);
		if(null == model){
			finish();
		}else{
			etRemark.setText(model.getOwnerremark());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.joiner_remark_btn_enter:
			String contentStr = etRemark.getText().toString();
			if(TextUtils.isEmpty(contentStr)){
				Toast.show("请填写备注");
				return;
			}
			if(50<Utils.getStringLength(contentStr)){
				Toast.show("备注最多50字");
				return;
			}
			model.setOwnerremark(contentStr);
			Net.request(model , WebApi.Event.MgrJoiner, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					setResult(RESULT_OK);
					finish();
				}
			});
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "EventJoinerMgr";
//		return "活动人员管理-备注";
	}
}
