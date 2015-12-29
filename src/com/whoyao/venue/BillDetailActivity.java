package com.whoyao.venue;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;
import com.whoyao.venue.engine.VenueEngine;
import com.whoyao.venue.model.BillDetailTModel;
import com.whoyao.venue.model.BillInformation;
import com.whoyao.venue.model.BillItemModel;
import com.whoyao.venue.model.BillVenueItemModel;

public class BillDetailActivity extends BasicActivity {
	private BillDetailTModel tModel;
	private TextView tvOrderNumber;
	private TextView tvVenueName;
	private TextView tvMoney;
	private TextView tvTime;
	private TextView tvPayState;
	private LinearLayout venueNumLayout;
	private BillItemModel billItem;
	private RelativeLayout waitPayRl;
	private TextView tvSend;
	private RelativeLayout rlSendMessage;
	private String[] placeTypeDetailsArray = { "室内", "室外" };
	private String[] placeTypeArray = { "羽毛球场", "网球场", "乒乓球场", "篮球场", "足球场" };
	private String[] orderPayStateArray = { "未支付", "已支付", "已退款" };
	private int orderPayState;
	private int position;
	private String payId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_bill_detial);
		tModel = new BillDetailTModel();
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		payId = intent.getStringExtra("payid");
		long orderId = intent.getLongExtra("orderid", 0);
		tModel.setOrderid(orderId);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		rlSendMessage = (RelativeLayout) findViewById(R.id.sendmessage_rl);
		waitPayRl = (RelativeLayout) findViewById(R.id.billdetail_waitpay_ll);
		tvSend = (TextView) findViewById(R.id.sendmessage_tv);
		tvOrderNumber = (TextView) findViewById(R.id.billdetail_ordernumber_tv);
		tvVenueName = (TextView) findViewById(R.id.billdetail_venuename_tv);
		tvMoney = (TextView) findViewById(R.id.billdetail_money_tv);
		tvTime = (TextView) findViewById(R.id.billdetail_time_tv);
		tvPayState = (TextView) findViewById(R.id.billdetail_paystate_tv);
		venueNumLayout = (LinearLayout) findViewById(R.id.sub_venue_num);
	}

	public void addView() {
		LinearLayout ll = null;
		ll = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.item_bill_single, null);
		TextView tvBill = (TextView) ll.findViewById(R.id.bill_tv);
		tvBill.setText(billItem.getInformation().getTime().substring(0, 10)
				+ placeTypeArray[billItem.getPlacetype() - 1]);
		tvBill.setTextColor(getResources().getColor(R.color.blue_bar));
		venueNumLayout.addView(ll);
		List<BillVenueItemModel> details = billItem.getInformation()
				.getDetail();
		for (int i = 0; i < details.size(); i++) {
			ll = (LinearLayout) LayoutInflater.from(this).inflate(
					R.layout.item_billl_sing_num, null);
			TextView tvBillNum = (TextView) ll.findViewById(R.id.bill_tv_num);
			BillVenueItemModel billVenueItemModel = billItem.getInformation()
					.getDetail().get(i);
			tvBillNum.setText(VenueEngine.getPlaceTime(billVenueItemModel
					.getTime())
					+ " "
					+ placeTypeDetailsArray[billItem.getPlacetypedetail() - 1]
					+ " "
					+ billVenueItemModel.getMoney()
					+ " "
					+ billVenueItemModel.getRemainder() + "块");
			venueNumLayout.addView(ll);

		}

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		NetCache.clearCaches();
		initData();
	}


	private void initData() {
		Net.request(tModel, WebApi.Venue.OrderDetail, new ResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				billItem = JSON.getObject(content, BillItemModel.class);

				tvOrderNumber.setText(billItem.getOrdernumber() + "");
				tvVenueName.setText(billItem.getVenuename());
				tvMoney.setText(billItem.getMoney() + "元");
				tvTime.setText(billItem.getOrdertime().replace("-", "."));
				int state = billItem.getOrderstate();
				orderPayState = billItem.getOrderpaystate();
				switch (state) {
				case 0:
					tvPayState.setText("");
					break;
				case 2:
					if (orderPayState == 0) {
						tvPayState.setText("未支付");
						tvPayState.setTextColor(getResources().getColor(
								R.color.orange_button));
						waitPayRl.setVisibility(View.VISIBLE);
						findViewById(R.id.cancelorder_tv).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										MessageDialog dialog = new MessageDialog(
												context);
										dialog.setTitle("操作提示");
										dialog.setMessage("确认取消该场馆订单吗？");
										dialog.setLeftButton("确定",
												new OnClickListener() {

													private Intent intent;

													@Override
													public void onClick(View v) {
														Net.request(
																"orderid",
																billItem.getOrdernumber()
																		+ "",
																WebApi.Venue.CancelOrder,
																new ResponseHandler() {

																	@Override
																	public void onSuccess(
																			String content) {
																		super.onSuccess(content);
																		tvPayState.setText("未支付");
																		tvPayState.setTextColor(getResources().getColor(
																				R.color.orange_button));
																		waitPayRl.setVisibility(View.GONE);
																		rlSendMessage.setVisibility(View.VISIBLE);
																		tvSend.setText("订单已取消");
																		intent = new Intent();
																		intent.putExtra(
																				"position",
																				position);
																		intent.putExtra("type",
																				4);
																		setResult(RESULT_OK,
																				intent);
																		
																	}

																	@Override
																	public void onFailure(
																			Throwable e,
																			int statusCode,
																			String content) {
																		super.onFailure(
																				e,
																				statusCode,
																				content);
																	}

																});
														
//														tvPayState
//																.setText("已取消");
//														tvPayState
//																.setTextColor(getResources()
//																		.getColor(
//																				R.color.gray_text));
													}
												});
										dialog.setRightButton("取消", null);
										dialog.show();

									}
								});
						findViewById(R.id.payorder_tv).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent payIntent = new Intent(context,OrderConfirmActivity.class);
										payIntent.putExtra(Extra.SelectedID,payId);
										startActivity(payIntent);
									}
								});
						tvSend.setVisibility(View.GONE);
					}

					break;
				case 4:
					if (orderPayState == 0) {
						tvPayState.setText("未支付");
						tvPayState.setTextColor(getResources().getColor(
								R.color.orange_button));
						waitPayRl.setVisibility(View.GONE);
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("订单已取消");

					}
					break;
				case 8:
					if (orderPayState == 0) {
						tvPayState.setText("未支付");
						waitPayRl.setVisibility(View.GONE);
						tvPayState.setTextColor(getResources().getColor(
								R.color.orange_button));
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("订单已过期");
					}
					break;
				case 16:
					if (orderPayState == 1) {
						tvPayState.setText("已支付");
						tvPayState.setTextColor(getResources().getColor(
								R.color.green_text));
						waitPayRl.setVisibility(View.GONE);
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("退款申请中");
					}
					break;
				case 32:
					if (orderPayState == 1) {
						tvPayState.setText("已支付");
						tvPayState.setTextColor(getResources().getColor(
								R.color.green_text));
						waitPayRl.setVisibility(View.GONE);
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("订单已退款");
					} else if (orderPayState == 2) {
						tvPayState.setText("已退款");
						waitPayRl.setVisibility(View.GONE);
						tvPayState.setTextColor(getResources().getColor(
								R.color.green_text));
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("订单已退款");
					}
					break;
				case 64:
					if (orderPayState == 1) {
						tvPayState.setText("已支付");
						tvPayState.setTextColor(getResources().getColor(
								R.color.green_text));
						waitPayRl.setVisibility(View.GONE);
						rlSendMessage.setVisibility(View.VISIBLE);
						tvSend.setText("发送手机验证码");
						tvSend.setBackgroundResource(R.drawable.rectangle_radius_blue);
						tvSend.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO
								Net.request("orderid",
										billItem.getOrdernumber() + "",
										WebApi.Venue.SendPhoneMessage,
										new ResponseHandler() {

											@Override
											public void onSuccess(String content) {
												super.onSuccess(content);
												Toast.show("发送成功");
											}

											@Override
											public void onFailure(Throwable e,
													int statusCode,
													String content) {
												super.onFailure(e, statusCode,
														content);
												Toast.show("发送失败");
											}

										});
							}
						});
					}

					break;
				}
				addView();

			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
			}

		});
	}

}
