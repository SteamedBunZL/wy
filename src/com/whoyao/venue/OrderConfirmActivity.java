package com.whoyao.venue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.app.net.HttpClient;
import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.fund.GetPhoneCodeActivity;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;
import com.whoyao.venue.engine.CartEngine;
import com.whoyao.venue.engine.OrderConfirmAdapter;
import com.whoyao.venue.engine.PayEngine;
import com.whoyao.venue.engine.alipay.Result;
import com.whoyao.venue.model.CartRModel;
import com.whoyao.venue.model.OrderInfoModel;
import com.whoyao.venue.model.PayTModel;
import com.whoyao.venue.model.UserMoneyModel;
import com.whoyao.venue.model.VenueCartModel;

/**
 * @author hyh creat_at：2014-2-27-上午9:54:07
 */
public class OrderConfirmActivity extends BasicActivity implements
		OnClickListener {
	private String payID;
	private Intent intent;
	private ListView mListView;
	private CartRModel mCartData;
	private OrderConfirmAdapter mAdapter;
	private ArrayList<VenueCartModel> mList;
	private TextView tvFooterCount;
	private TextView tvFooterAmount;
	private UserMoneyModel mUserMoney;
	private float totalAmount;
	private TextView tvBalance;
	private TextView tvBalanceDetial;
	private View vUseBalance;
	private EditText etBalancePwd;
	private View vBalancePwdLine;
	private TextView tvAlipay;
	private View vClientPay;
	private View vWebPay;
	private float alipayAmount;
	private float balanceAmount;
	private MessageDialog errDialog;
	private RelativeLayout rlBalance;
	private float paketpay;
	private float accountpay;
	private Intent backIntent = new Intent();
	private String resultPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		payID = intent.getStringExtra(Extra.SelectedID);
		if (TextUtils.isEmpty(payID)) {
			payID = "2014030200001";// TODO 测试数据
		}
		if (TextUtils.isEmpty(payID)) {
			finish();
			return;
		}
		setContentView(R.layout.activity_venue_order_confirm);
		initView();
		initUserMoney();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.order_confirm_btn_pay).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.order_confirm_lv);
		rlBalance = (RelativeLayout) findViewById(R.id.footer_order_rl_use_balance);
		View footerTotal = View.inflate(context, R.layout.footer_venue_cart,
				null);
		View footerPaytype = View.inflate(context,
				R.layout.footer_order_paytype, null);
		mListView.addFooterView(footerTotal, null, false);
		mListView.addFooterView(footerPaytype, null, false);
		mListView.addHeaderView(new View(context), null, false);
		mList = new ArrayList<VenueCartModel>();
		mAdapter = new OrderConfirmAdapter(context, mList);
		mListView.setAdapter(mAdapter);
		tvFooterCount = (TextView) footerTotal
				.findViewById(R.id.footer_cart_tv_count);
		tvFooterAmount = (TextView) footerTotal
				.findViewById(R.id.footer_cart_tv_amount);

		tvBalance = (TextView) findViewById(R.id.footer_order_tv_balance);
		tvBalanceDetial = (TextView) findViewById(R.id.footer_order_tv_balance_detial);
		vUseBalance = findViewById(R.id.footer_order_iv_use_balance);
		etBalancePwd = (EditText) findViewById(R.id.footer_order_et_pwd);
		vBalancePwdLine = findViewById(R.id.footer_order_line_pwd);
		tvAlipay = (TextView) findViewById(R.id.footer_order_tv_alipay);
		vClientPay = findViewById(R.id.footer_order_iv_client_alipay);
		vWebPay = findViewById(R.id.footer_order_iv_web_alipay);
		vUseBalance.setOnClickListener(this);
		vClientPay.setOnClickListener(this);
		vWebPay.setOnClickListener(this);

	}

	private void initData() {
		mCartData = (CartRModel) intent.getSerializableExtra(Extra.Data);
		if (mCartData == null) {
			Net.request("payid", payID, WebApi.Venue.GetOrderok,
					new ResponseHandler() {
						@Override
						public void onSuccess(String content) {
							content = content.replaceAll("orderid", "cartid");
							content = content.replaceAll("originalprice",
									"discountprice");
							content = content.replaceAll(
									"VenueOrderDetailList", "CartDetailList");
							content = content.replaceAll("VenueOrderList",
									"VenueCartList");
							CartRModel cartRModel = JSON.getObject(content,
									CartRModel.class);
							CartEngine.initVenueCart(cartRModel);
							CartEngine.countTotalAmount(cartRModel);
							CartEngine.countTotalItem(cartRModel);
							CartEngine.countValidItem(cartRModel);
							mCartData = cartRModel;
							mList.clear();
							mList.addAll(mCartData.getVenueCartList());
							mAdapter.notifyDataSetChanged();
							setData();
						}
					});
		} else {
			setData();
		}

	}

	private void initUserMoney() {
		Net.request(new RequestParams(), WebApi.Venue.GetUserMoney,
				new ResponseHandler() {

					@Override
					public void onSuccess(String content) {
						mUserMoney = JSON.getObject(content,
								UserMoneyModel.class);
						if (mUserMoney.getAmount()
								+ mUserMoney.getPacketBalance() == 0) {
							findViewById(R.id.footer_order_ll_use_balance)
									.setBackgroundResource(
											R.drawable.rectangle_radius_gray);
						}
					}

					@Override
					public void onFinish() {
						initData();
					}
				});
	}

	private void setData() {
		alipayAmount = mCartData.getTotalAmount();
		refreshHeaderAndFooter(mCartData);
		tvAlipay.setText("￥" + mCartData.getTotalAmount());
		// 账户有余额时
		if (mUserMoney != null
				&& (mUserMoney.getAmount() > 0f || mUserMoney
						.getPacketBalance() > 0f)) {
			float balanceAmount = mUserMoney.getAmount();
			float balancePacket = mUserMoney.getPacketBalance();
			float packetCount = mUserMoney.getPacketCount();
			tvBalance.setText("￥" + (balanceAmount + balancePacket));
			tvBalanceDetial.setText("红包" + packetCount + "余额" + balancePacket
					+ "元，充值余额" + balanceAmount + "元");
			findViewById(R.id.footer_order_rl_balance).setVisibility(
					View.VISIBLE);
		} else {
			tvBalance.setText("￥" + "0");
		}
	}

	private void refreshHeaderAndFooter(CartRModel model) {
		totalAmount = CartEngine.countTotalAmount(model);
		int validCount = CartEngine.countValidItem(model);
		tvFooterCount.setText("结算数量：" + validCount + "　总价：");
		tvFooterAmount.setText("￥" + totalAmount);
	}

	private void refreshAlipay() {
		// totalAmount = CartEngine.countTotalAmount(model);
		// int validCount = CartEngine.countValidItem(model);
		// tvFooterCount.setText("结算总是："+ validCount +"　总价：");
		// tvFooterAmount.setText("￥"+totalAmount);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		case R.id.order_confirm_btn_pay:
			if (vUseBalance.isSelected()) {

			} else if (vWebPay.isSelected()) {

			} else if (vClientPay.isSelected()) {

			} else {
				Toast.show("请选择支付方式");
				return;
			}
			if (vUseBalance.isSelected()) {
				if (!MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",
						MyinfoManager.IsSetPass)) {
					Toast.show("请设置支付密码");
					return;
				}
				String balancePwd = etBalancePwd.getText().toString();
				if (balancePwd.equals("") || balancePwd == null) {
					Toast.show("请输入支付密码");
					return;
				}
				Net.request("password", balancePwd, WebApi.Venue.CheckPwd,
						new ResponseHandler() {

							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								JSONObject json;
								try {
									json = new JSONObject(content);
									String result = (String) json.get("Result");
									resultPwd = result;
									if (result.equals("fail")) {
										Toast.show("输入密码格式不正确");
									} else {
										if (balanceAmount < mCartData
												.getTotalAmount()) {
											if (vWebPay.isSelected()) {
											} else if (vClientPay.isSelected()) {
											} else {
												Toast.show("帐户余额不足");
												return;
											}
										}

										if (mCartData != null) {
											paketpay = 0;
											accountpay = 0;
											totalAmount = mCartData
													.getTotalAmount();
											if (mUserMoney != null
													&& vUseBalance.isSelected()) {
												if (balanceAmount > mUserMoney
														.getPacketBalance()) {
													paketpay = mUserMoney
															.getPacketBalance();
													accountpay = balanceAmount
															- paketpay;
												} else {
													paketpay = balanceAmount;
												}
											}
											PayTModel payModel = new PayTModel();
											payModel.setPaketpay(paketpay);// 红包支付
											payModel.setAccountpay(accountpay);// 余额支付
											payModel.setOtherpay(alipayAmount);// 银行支付
											payModel.setPaytotalamount(totalAmount);// 总额
											payModel.setPayid(payID);
											Net.request(payModel,
													WebApi.Venue.AppStart,
													new ResponseHandler() {
														@Override
														public void onSuccess(
																String content) {
															try {
																JSONObject jo = new JSONObject(
																		content);
																final String out_trade_no = jo
																		.getInt("out_trade_no")
																		+ "";
																String sign = jo
																		.getString("sign");
																String alipayMount = jo.getString("total_fee");
																String apipost = jo.getString("apipost");
																if (!TextUtils
																		.isEmpty(out_trade_no)
																		&& TextUtils
																				.isDigitsOnly(out_trade_no)) {
																	Handler handler = new Handler() {
																		public void handleMessage(
																				android.os.Message msg) {

																			Result resultAli = new Result(
																					(String) msg.obj);
																			String resultStatus = resultAli
																					.getResultStatus();
																			if (TextUtils
																					.equals(resultStatus,
																							"9000")) {
																				// 跳支付成功
																				// Net.request(
																				// "serialnumber",
																				// result,
																				// WebApi.Venue.AppReturn,
																				// new
																				// ResponseHandler());
																				AppContext
																						.startAct(PaySuccessActivity.class);
																				finish();
																			} else if (TextUtils
																					.equals(resultStatus,
																							"6001")) {
																				// Toast.show(resultStatus);
																			} else {
																				// 跳支付失败
																				AppContext
																						.startAct(PayFailActivity.class);

																			}

																			// switch
																			// (msg.what)
																			// {
																			// case
																			// Const.ALIYPE_RQF_PAY:
																			// Toast.show(result
																			// .getResult());
																			// break;
																			// case
																			// Const.ALIYPE_RQF__LOGIN:
																			// Toast.show(result.getResult());
																			// break;
																			// }
																		};
																	};
																	if (vClientPay
																			.isSelected()) {
																		OrderInfoModel info = new OrderInfoModel();
																		info.setBody("选择的场馆预订");
																		info.setEncodeNotify_url("http://www.whoyao.com/Venue/AliPay/Mobile_Notify");
																		info.setOut_trade_no(out_trade_no);
																		info.setTotal_fee(alipayMount+"");
																		L.i(Const.ZL,"alipayAmount--"+alipayAmount);
																		PayEngine
																				.startAliPayClient(
																						context,
																						apipost,
																						sign,
																						handler);
//																		PayEngine
//																		.startAliPayClient(
//																				context,
//																				info,
//																				handler);
																		// PayEngine.startAliPayClient2(context,
																		// result,
																		// handler);
																	} else if (vWebPay
																			.isSelected()) {
																		// otherpay=1.0&paytotalamount=1.0&paketpay=0.0&payid=2014031000044&accountpay=0.0

																		String url = WebApi.Alipay_Addr
																				+ alipayAmount
																				+ "&paytotalamount="
																				+ totalAmount
																				+ "&payid="
																				+ payID
																				+ "&accountpay="
																				+ accountpay;
																		L.i(Const.ZL,"Url-"+url);
																		Intent intent = new Intent(
																				context,
																				WebPayAcitivity.class);
																		intent.putExtra(
																				"url",
																				url);
																		startActivity(intent);

																	}
																} else {
																	if (vUseBalance
																			.isSelected()) {

																		AppContext
																				.startAct(PaySuccessActivity.class);
																		finish();

																	}
																}
															} catch (JSONException e) {
																AppException
																		.handle(e);
																// Message
															}
														}
													});
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(Throwable e, int statusCode,
									String content) {
								super.onFailure(e, statusCode, content);
							}
						});
			} else {
				if (mCartData != null) {
					paketpay = 0;
					accountpay = 0;
					totalAmount = mCartData.getTotalAmount();
					if (mUserMoney != null && vUseBalance.isSelected()) {
						if (balanceAmount > mUserMoney.getPacketBalance()) {
							paketpay = mUserMoney.getPacketBalance();
							accountpay = balanceAmount - paketpay;
						} else {
							paketpay = balanceAmount;
						}
					}
					PayTModel payModel = new PayTModel();
					payModel.setPaketpay(paketpay);// 红包支付
					payModel.setAccountpay(accountpay);// 余额支付
					payModel.setOtherpay(alipayAmount);// 银行支付
					payModel.setPaytotalamount(totalAmount);// 总额
					payModel.setPayid(payID);
					Net.request(payModel, WebApi.Venue.AppStart,
							new ResponseHandler() {
								@Override
								public void onSuccess(String content) {
									try {
										JSONObject jo = new JSONObject(content);
										// final String result = jo
										// .getString("out_trade_no");
										final String out_trade_no = jo
												.getInt("out_trade_no") + "";
										String sign = jo.getString("sign");
										String alipayMount = jo.getString("total_fee");
										String apipost = jo.getString("apipost");
										if (!TextUtils.isEmpty(out_trade_no)
												&& TextUtils
														.isDigitsOnly(out_trade_no)) {
											Handler handler = new Handler() {
												public void handleMessage(
														android.os.Message msg) {

													Result resultAli = new Result(
															(String) msg.obj);
													String resultStatus = resultAli
															.getResultStatus();
													if (TextUtils.equals(
															resultStatus,
															"9000")) {
														// 跳支付成功
														// Net.request(
														// "serialnumber",
														// result,
														// WebApi.Venue.AppReturn,
														// new
														// ResponseHandler());
														AppContext
																.startAct(PaySuccessActivity.class);
														finish();
													} else if (TextUtils
															.equals(resultStatus,
																	"6001")) {
														// Toast.show(resultStatus);
													} else {
														// 跳支付失败
														AppContext
																.startAct(PayFailActivity.class);

													}

												};
											};
											if (vClientPay.isSelected()) {
												OrderInfoModel info = new OrderInfoModel();
												info.setBody("选择的场馆预订");
												info.setEncodeNotify_url("http://www.whoyao.com/pay/notify_url");
												// info.setOut_trade_no(1000902+"");
												// info.setTotal_fee(1
												// + "");
												info.setOut_trade_no(out_trade_no);
												info.setTotal_fee(alipayMount+"");
												L.i(Const.ZL,"alipayAmount--"+alipayAmount);
												 PayEngine.startAliPayClient(
												 context, apipost, sign,
												 handler);
//												PayEngine
//												.startAliPayClient(
//														context,
//														info,
//														handler);
											} else if (vWebPay.isSelected()) {
												// otherpay=1.0&paytotalamount=1.0&paketpay=0.0&payid=2014031000044&accountpay=0.0

												String url = WebApi.Alipay_Addr
														+ alipayAmount
														+ "&paytotalamount="
														+ totalAmount
														+ "&payid="
														+ payID
														+ "&accountpay="
														+ accountpay;
												L.i(Const.ZL,"Url-"+url);
												Intent intent = new Intent(
														context,
														WebPayAcitivity.class);
												intent.putExtra("url", url);
												startActivity(intent);

											}
										} else {
											if (vUseBalance.isSelected()) {

												AppContext
														.startAct(PaySuccessActivity.class);
												finish();

											}
										}
									} catch (JSONException e) {
										AppException.handle(e);
										// Message
									}
								}
							});
				}
			}

			break;
		case R.id.footer_order_iv_use_balance:
			if (mUserMoney.getAmount() + mUserMoney.getPacketBalance() == 0) {
				// Toast.show("您没有帐户余额，请充值");
				return;
			}
			vUseBalance.setSelected(!vUseBalance.isSelected());
			if (vUseBalance.isSelected() && mUserMoney != null) {
				// sp.setIsPass(MyinfoManager.IsSetPass);
				if (!MyinfoManager.getUserConfigFile().getBoolean("IsSetPass",
						MyinfoManager.IsSetPass)) {
					Log.i("互邀!!!!@@@@2",
							"IsSetPass["
									+ MyinfoManager.getUserConfigFile()
											.getBoolean("IsSetPass",
													MyinfoManager.IsSetPass)
									+ "]");
					etBalancePwd.setVisibility(View.VISIBLE);
					etBalancePwd
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					etBalancePwd.setText("设置支付密码");
					etBalancePwd.setFocusableInTouchMode(false);
					etBalancePwd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AppContext.startAct(GetPhoneCodeActivity.class);
						}
					});
				}
				etBalancePwd.setVisibility(View.VISIBLE);

				float balance = mUserMoney.getAmount()
						+ mUserMoney.getPacketBalance();
				float totalAmount2 = mCartData.getTotalAmount();
				if (balance > totalAmount2) {
					balanceAmount = totalAmount2;
					// alipayAmount = mCartData.getTotalAmount() - balance;
					alipayAmount = 0;

				} else {
					balanceAmount = balance;
					alipayAmount = mCartData.getTotalAmount() - balance;
					// vWebPay.setSelected();
					// vClientPay.setSelected(true);
				}
				vBalancePwdLine.setVisibility(View.VISIBLE);
				etBalancePwd.setVisibility(View.VISIBLE);
				if (alipayAmount == 0f) {
					vWebPay.setSelected(false);
					vClientPay.setSelected(false);
				}
			} else {
				alipayAmount = mCartData.getTotalAmount();
				vBalancePwdLine.setVisibility(View.GONE);
				etBalancePwd.setVisibility(View.GONE);
			}
			tvAlipay.setText("" + alipayAmount);
			break;
		case R.id.footer_order_iv_client_alipay:
			if (vClientPay.isSelected()) {
				vClientPay.setSelected(false);
			} else {
				vWebPay.setSelected(false);
				if (alipayAmount == 0f) {
					Toast.show("不需要使用支付宝支付");
				} else {
					vClientPay.setSelected(true);
				}
			}
			break;
		case R.id.footer_order_iv_web_alipay:
			if (vWebPay.isSelected()) {
				vWebPay.setSelected(false);
			} else {
				vClientPay.setSelected(false);
				if (alipayAmount == 0f) {
					Toast.show("不需要使用支付宝支付");
				} else {
					vWebPay.setSelected(true);
				}
			}
			break;
		case MessageDialog.ID_LEFT_BUTTON:
			finish();
			break;
		default:
			break;
		}
	}

	private void showErrorDialog() {
		if (null == errDialog) {
			errDialog = new MessageDialog(context);
			errDialog.setMessage("失效场地不参加结算，部分场地数量不足，请修改场地数量后完成预订。");
			errDialog.setLeftButton("确定", this);
			errDialog.setRightButton("取消", null);
		}
	}

	@Override
	protected boolean onBack() {
		setResult(VenueDetialActivity.ORDER_CONFIM_BACK, backIntent);
		finish();
		return true;
	}

}
