package com.whoyao.activity;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.engine.client.ClientEngine;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 帮助
 * @author hyh creat_at：2013-8-12-上午10:31:02
 */
public class SettingHelpActivity extends BasicActivity implements OnClickListener {
	private TextView functionTv;
	private TextView functionCon;
	private TextView chargeTv;
	private TextView chargeCon;
	private TextView howtoTv;
	private TextView howtoCon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_help);
		initView();
	}

	private void initView() {
		functionTv = (TextView)findViewById(R.id.help_tv_function);
		functionCon = (TextView)findViewById(R.id.help_content_function);
		chargeTv = (TextView)findViewById(R.id.help_tv_charge);
		chargeCon = (TextView)findViewById(R.id.help_content_charge);
		howtoTv = (TextView)findViewById(R.id.help_tv_howto);
		howtoCon = (TextView)findViewById(R.id.help_content_howto);
		functionTv.setOnClickListener(this);
		chargeTv.setOnClickListener(this);
		howtoTv.setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.help_tv_feedback).setOnClickListener(this);
		findViewById(R.id.help_tv_service_phone).setOnClickListener(this);
		findViewById(R.id.help_tv_service_weibo).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.help_tv_function:
			if(View.VISIBLE == functionCon.getVisibility()){
				functionCon.setVisibility(View.GONE);
				functionTv.setSelected(false);
			}else{
				functionCon.setVisibility(View.VISIBLE);
				functionTv.setSelected(true);
			}
			break;
		case R.id.help_tv_charge:
			if(View.VISIBLE == chargeCon.getVisibility()){
				chargeCon.setVisibility(View.GONE);
				chargeTv.setSelected(false);
			}else{
				chargeCon.setVisibility(View.VISIBLE);
				chargeTv.setSelected(true);
			}
			break;
		case R.id.help_tv_howto:
			if(View.VISIBLE == howtoCon.getVisibility()){
				howtoCon.setVisibility(View.GONE);
				howtoTv.setSelected(false);
			}else{
				howtoCon.setVisibility(View.VISIBLE);
				howtoTv.setSelected(true);
			}
			break;
		case R.id.help_tv_feedback:
			AppContext.startAct(SettingFeedbackActivity.class);
			break;
		case R.id.help_tv_service_phone:
			ClientEngine.showCallServiceDialog(this);
			break;
		case R.id.help_tv_service_weibo:
			ClientEngine.openWeibo();
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "帮助";
	}
}
