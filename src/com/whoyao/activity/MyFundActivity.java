package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.fund.GetPhoneCodeActivity;
import com.whoyao.model.MyFundRModel;
import com.whoyao.model.MyFundRedList;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.DoubleFormat;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFundActivity extends BasicActivity implements OnClickListener {
	private TextView tvAccountMoney;
	private TextView tvRedMoney;
	private TextView tvRechargeMoney;
	private TextView tvArrow;
	private TextView tvRedNum;
	private LinearLayout mSetorchanngeLin;
	private LinearLayout rlRedList;
	private RelativeLayout rlFundRed;
	private TextView tvSetOrChangePassword;
	private List<MyFundRedList> redLists = new ArrayList<MyFundRedList>();
	private boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fund_my);
		initView();
		initData();
	}

	public void initView() {
		tvSetOrChangePassword = (TextView) findViewById(R.id.tv_changeorset_password);
		mSetorchanngeLin = (LinearLayout) findViewById(R.id.ll_setorchange_password);
//		tvSetOrChangePassword.setOnClickListener(this);
		mSetorchanngeLin.setOnClickListener(this);
		tvAccountMoney = (TextView) findViewById(R.id.account_money_1);
		tvRechargeMoney = (TextView) findViewById(R.id.recharge_money_1);
		tvRedMoney = (TextView) findViewById(R.id.red_money_1);
		tvArrow = (TextView) findViewById(R.id.red_arrow);
		tvRedNum = (TextView) findViewById(R.id.red_num_1);
		rlRedList = (LinearLayout) findViewById(R.id.ll_red_list_1);
		rlFundRed = (RelativeLayout) findViewById(R.id.my_fund_red);
		findViewById(R.id.my_fund_red).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
	}

	public void initData() {
		Net.request(null, WebApi.User.MyFund, new ResponseHandler(true) {

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				MyFundRModel myFundRModel = JSON.getObject(content,
						MyFundRModel.class);
				redLists = myFundRModel.getUserPacketItem();
				double accountAll = myFundRModel.getAmount()
						+ myFundRModel.getPacketamount();
				L.i(Const.ZL,"TrueOrFalse---"+MyinfoManager.getUserConfigFile().getBoolean("IsSetPass", MyinfoManager.IsSetPass));
				tvSetOrChangePassword.setText(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass)?"修改支付密码":"设置支付密码");
//				sp.setIsPass(MyinfoManager.IsSetPass);
				MyinfoManager.getUserConfigFile().edit().putBoolean("IsSetPass", true);
				Log.i("互邀+++!!!!!@@",(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass) ? "修改支付密码":"设置支付密码").toString());
				tvAccountMoney.setText(DoubleFormat.format(accountAll) + "元");
				tvRechargeMoney.setText(DoubleFormat.format(myFundRModel
						.getAmount()) + "元");
				tvRedMoney.setText(DoubleFormat.format(myFundRModel
						.getPacketamount()) + "元");
//				tvRedMoney.setText(DoubleFormat.format(12.23) + "元");
				tvRedNum.setText(myFundRModel.getPacketcount() + "个");
			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onFailure(e, statusCode, content);
				if (statusCode == 400) {
					tvAccountMoney.setText("0.00元");
					tvRechargeMoney.setText("0.00元");
					tvRedMoney.setText("0.00元");
					tvRedNum.setText("0个");
					tvSetOrChangePassword.setText(MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",MyinfoManager.IsSetPass)?"修改支付密码":"设置支付密码");
				}else if(statusCode == 0){
					finish();
				}
			}

		});
		// addChildView(1, 2);
		// addChildView(2, 3);
		// addChildView(3, 4);
	}

	public void addMiddleView(long num, double numMoney, String type) {
		if (type.equals("middle")) {
			LinearLayout llLayout = null;
			LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.item_fund_red, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources()
							.getDimension(R.dimen.item_height));
			lp.setMargins((int) getResources().getDimension(R.dimen.dip10), -1,
					(int) getResources().getDimension(R.dimen.dip10), -1);
			ll.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selector_radius_middle));
			TextView tvNum = (TextView) ll.findViewById(R.id.num_2);
			TextView tvNumMoney = (TextView) ll.findViewById(R.id.num_money_2);
			tvNum.setText("编号：" + num + "");
			tvNumMoney.setText(DoubleFormat.format(numMoney) + "元");
			rlRedList.addView(ll, lp);
		} else if (type.equals("last")) {
			LinearLayout llLayout = null;
			LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.item_fund_red, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources()
							.getDimension(R.dimen.item_height));
			lp.setMargins((int) getResources().getDimension(R.dimen.dip10), -1,
					(int) getResources().getDimension(R.dimen.dip10), -1);
			ll.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.selector_radius_bottom));
			TextView tvNum = (TextView) ll.findViewById(R.id.num_2);
			TextView tvNumMoney = (TextView) ll.findViewById(R.id.num_money_2);
			tvNum.setText("编号：" + num + "");
			tvNumMoney.setText(DoubleFormat.format(numMoney) + "元");
			rlRedList.addView(ll, lp);
		}

	}

	public void removeView() {
		rlRedList.removeAllViews();
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.ll_setorchange_password:
			AppContext.startAct(GetPhoneCodeActivity.class);
			break;
		case R.id.my_fund_red:
			if (isOpen == false) {
				isOpen = true;
				if (redLists.size() > 0) {
					rlFundRed.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.selector_radius_middle));
					tvArrow.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.arrow_up));
					for (int i = 0; i < redLists.size() - 1; i++) {
						long packetNum = redLists.get(i).getPacketid();
						double packetMoney = redLists.get(i).getPacketmoney();
						addMiddleView(packetNum, packetMoney, "middle");
					}
					addMiddleView(redLists.get(redLists.size() - 1)
							.getPacketid(), redLists.get(redLists.size() - 1)
							.getPacketmoney(), "last");
				}

			} else {
				isOpen = false;
				rlFundRed.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.selector_radius_bottom));
				tvArrow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.arrow_down));
				removeView();
			}
			break;

		}

	}

}
