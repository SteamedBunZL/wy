package com.whoyao.activity;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.WebApi;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.HonestyModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 实名认证状态
 * @author hyh 
 * creat_at：2013-8-5-上午9:19:13
 */
public class MyHonestyActivity extends BasicActivity implements OnClickListener {
	private View stateLl;
	private View reasonLl;
	private TextView stateTv;
	private TextView reasonTv;
	private Button revalidBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_honesty);
		
		initView();
		
		setHonestyState();
	}

	private void initView() {
		stateLl = findViewById(R.id.myhonesty_ll_state);
		reasonLl = findViewById(R.id.myhonesty_ll_reason);
		stateTv = (TextView)findViewById(R.id.myhonesty_tv_state);
		reasonTv = (TextView)findViewById(R.id.myhonesty_tv_reason);
		revalidBtn = (Button)findViewById(R.id.myhonesty_btn_revalid);
		
		revalidBtn.setOnClickListener(this);
		findViewById(R.id.myhonesty_title_back).setOnClickListener(this);
	}
		
		

	private void setHonestyState() {
		int honestyState = 0;
		if(null != MyinfoManager.getUserInfo()){
			honestyState = MyinfoManager.getUserInfo().getUserHonestyState();
		}
		switch (honestyState) {
		case State.Honesty_auditing:
			stateLl.setBackgroundResource(R.drawable.rectangle_radius_white);
			reasonLl.setVisibility(View.GONE);
			revalidBtn.setVisibility(View.GONE);
			stateTv.setText(R.string.state_checking);
			break;
		case State.Honesty_reject:
			stateTv.setText(R.string.state_check_fail);
			RequestParams params = new RequestParams();
			Net.request(params , WebApi.User.GetHonestyInfo, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					HonestyModel honestyInfo = JSON.getObject(content, HonestyModel.class);
					reasonTv.setText(honestyInfo.AuditComment);
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myhonesty_title_back:
			finish();
			break;
		case R.id.myhonesty_btn_revalid:
			AppContext.startAct(MyVerifyHonestyActivity.class);
			break;
		default:
			break;
		}
	}
	@Override
	public String toString() {
		return "实名认证状态";
	}
}
