package com.whoyao.activity;

import java.io.File;
import java.util.Calendar;

import org.apache.http.Header;

import com.baidu.platform.comapi.map.l;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.db.AreaListDB;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.map.AddrInfoModel;
import com.whoyao.model.InviteDetailListItem;
import com.whoyao.model.InviteSendRModel;
import com.whoyao.model.InviteSendTModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.Utils;
import com.whoyao.widget.CustomRelativeLayout;
import com.whoyao.widget.CustomRelativeLayout.KeyboardChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class InvitePubliseActivity extends BasicActivity implements
		OnClickListener, OnCheckedChangeListener, OnFocusChangeListener,
		TextWatcher {
	private EditText etName;
	private EditText etMoney;
	private TextView etDescription;
	private Button btnSend;
	private TextView tvBeginTime;
	private TextView tvAddr;
	private TextView tvPayType;
	private CheckBox isSendMessage;
	private long beginTime;
	private Calendar cal = null;
	private static final int REQUEST_CODE_TYPE = 0;
	private static final int REQUEST_CODE_begintime = 1;
	private static final int REQUEST_CODE_ADDR = 4;
	private static final int REQUEST_CODE_PRICE = 6;
	private InviteSendTModel model;
	private InviteSendRModel rModel;
	private InviteDetailListItem item = new InviteDetailListItem();
	private String name;
	private LinearLayout llPaytype;
	private AddrInfoModel addrInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_add);
		model = new InviteSendTModel();
		rModel = new InviteSendRModel();
		model.setTomessage(0);
		Intent intent = getIntent();
		int userId = intent.getIntExtra(Extra.SelectedID, 0);
		if (userId == 0) {
			userId = 8047;
		}
		name = intent.getStringExtra(Extra.SelectedItemStr);
		model.setUserid(userId);
		initView();
		Calendar calendar = CalendarUtils.getNewCalendar();
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.get(Calendar.HOUR_OF_DAY) + 1);
		if (0 != beginTime && beginTime < calendar.getTimeInMillis()) {
			beginTime = 0;
			// TODO
			model.setTime("");
			tvBeginTime.setText("");
		}
	}

	public void initView() {
		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setText("向" + name + "发邀请");
		findViewById(R.id.invite_btn_back).setOnClickListener(this);
		// 名称
		findViewById(R.id.invite_add_ll_title).setOnClickListener(this);
		etName = (EditText) findViewById(R.id.invite_add_et_title);
		// 费用
		etMoney = (EditText) findViewById(R.id.invite_add_et_price);
		etMoney.addTextChangedListener(this);
		findViewById(R.id.invite_add_ll_price).setOnClickListener(this);
		// 描述
		findViewById(R.id.invite_add_ll_description).setOnClickListener(this);
		etDescription = (TextView) findViewById(R.id.invite_add_et_description);
		// 发送
		btnSend = (Button) findViewById(R.id.invite_add_btn_enter);
		btnSend.setOnClickListener(this);
		// 开始
		findViewById(R.id.invite_add_ll_begintime).setOnClickListener(this);
		tvBeginTime = (TextView) findViewById(R.id.invite_add_tv_begintime);
		// 址址
		findViewById(R.id.invite_add_ll_addr).setOnClickListener(this);
		tvAddr = (TextView) findViewById(R.id.invite_add_tv_addr);
		llPaytype = (LinearLayout) findViewById(R.id.invite_add_ll_paytype);
		llPaytype.setOnClickListener(this);
		tvPayType = (TextView) findViewById(R.id.invite_add_tv_paytype);
		// 短信
		// isSendMessage = (CheckBox)
		// findViewById(R.id.setcon_cb_real_attestation);
		// isSendMessage.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// if (isChecked) {
		// model.setTomessage(1);
		// } else {
		// model.setTomessage(0);
		// }
		// }
		// });

		// CustomRelativeLayout rlInvite = (CustomRelativeLayout)
		// findViewById(R.id.invite_rl);
		// rlInvite.setOnKeyboardChangeListener(new KeyboardChangeListener() {
		//
		// @Override
		// public void onKeyboardChange(int w, int h, int oldw, int oldh) {
		//
		// String title = etName.getText().toString();
		// if (TextUtils.isEmpty(title)) {
		// return;
		// }
		// if (2 > Utils.getStringLength(title)
		// || 20 < Utils.getStringLength(title)) {
		// Toast.show("邀请主题在2-20个字之内");
		// return;
		// }
		//
		// String priceStr = etMoney.getText().toString();
		// System.out.println("priiceStr****"+priceStr);
		// System.out.println("*******");
		// if (!TextUtils.isEmpty(priceStr)) {
		// try {
		// float price = Float.parseFloat(priceStr);
		// if (999 < price) {
		// Toast.show("有效范围0-999，0元为免费");
		// return;
		// } else {
		// if (price == 0) {
		// // TODO
		// llPaytype.setVisibility(View.INVISIBLE);
		// } else {
		// llPaytype.setVisibility(View.VISIBLE);
		// }
		// }
		// } catch (Exception e) {
		// Toast.show("请输入有效的金额");
		// return;
		// }
		// }
		// String Description = etDescription.getText().toString();
		// if (TextUtils.isEmpty(Description)) {
		// return;
		// }
		// if (2 > Utils.getStringLength(Description)
		// || 20 < Utils.getStringLength(Description)) {
		// Toast.show("邀请名称在2-20个字之内");
		// return;
		// }
		// }
		// });

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 检查数据
		case R.id.invite_add_ll_description:
			Intent descIntent = new Intent(context, InviteDescActivity.class);
			if (etDescription != null) {
				descIntent.putExtra("content", etDescription.getText()
						.toString());
			}
			startActivityForResult(descIntent, R.id.invite_add_ll_description);
			break;
		case R.id.invite_btn_back:
			onBack();
			// finish();
			break;
		case R.id.invite_add_btn_enter:
			boolean checkedago = true;
			boolean checkedlate = false;
			if (checkedago) {
				String title = etName.getText().toString();
				if (TextUtils.isEmpty(title)) {
					Toast.show("请输入邀请主题");
					return;
				}
				if (0 == beginTime) {
					Toast.show("请选择邀请时间");
					return;
				}
				if (model.getAddress() == null) {
					Toast.show("请输入邀请地点");
					return;
				}
				String priceStr = etMoney.getText().toString();
				if (TextUtils.isEmpty(priceStr)) {
					Toast.show("请输入人均费用");
					return;
				}
				if (Integer.parseInt(priceStr) != 0) {
					if (model.getPaytype() == null) {
						Toast.show("请选择支付方式");
						return;
					}

				}
				String description = etDescription.getText().toString();
				if (TextUtils.isEmpty(description)) {
					Toast.show("请输入邀请描述");
					return;
				}
				if (2 > Utils.getStringLength(title)
						|| 20 < Utils.getStringLength(title)) {
					Toast.show("邀请主题在2-20个字之内");
					return;
				}
				model.setTitle(title);
				model.setPredictConsume(priceStr);
				if (Integer.parseInt(etMoney.getText().toString()) == 0) {
					model.setPaytype(1 + "");
				}
				 if (priceStr.length()>3) {
				 Toast.show("人均费用范围在0-999元");
				 return;
				 }
				item.setPredictconsume(Integer.parseInt(priceStr));
				if (description.length() < 15 || description.length() > 500) {
					Toast.show("邀请描述为15-500个字");
				} else {
					model.setDescription(description);
				}
				item.setDescription(description);
				item.setSenduserid(MyinfoManager.getUserInfo().getUserID());
				checkedlate = true;
			}
			if (checkedlate) {
				Net.request(model, WebApi.Invite.SendInvite,
						new ResponseHandler(true) {

							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								Intent intent = new Intent(context,
										MyInviteDetailActivity.class);
								intent.putExtra("inviteid",
										Integer.parseInt(content));
								intent.putExtra("invitetype", 1);
								startActivity(intent);
								finish();
							}

							@Override
							public void onFailure(Throwable e, int statusCode,
									String content) {
								super.onFailure(e, statusCode, content);
								Toast.show("发布失败，请重试");
							}
						});
			}

			// if(0 == Integer.parseInt(model.getPredictConsume())){
			// if(!TextUtils.isEmpty(priceStr)){
			// try{
			// float price = Float.parseFloat(priceStr);
			// if(999<price){
			// Toast.show("有效范围0-999，0元为免费");
			// return;
			// }else{
			// model.setPredictConsume(priceStr);
			//
			// }
			// }catch(Exception e){
			// Toast.show("请输入有效的金额");
			// return;
			// }
			// }else{
			// Toast.show("请输入人均费用");
			// return;
			// }
			// }
			break;
		case R.id.invite_add_ll_paytype:// 类型
			etMoney.clearFocus();
			Intent typeData = new Intent(this, SelectSingleWheelActivity.class);
			typeData.putExtra(Extra.ArrayRes, R.array.pay_type);
			startActivityForResult(typeData, REQUEST_CODE_TYPE);
			break;
		case R.id.invite_add_ll_begintime:
			cal = CalendarUtils.getNewCalendar();
			Intent beginIntent = new Intent(this,
					SelectTimeMinuteActivity.class);
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);// 普通活动，1小时候
			cal.setTimeInMillis(cal.getTimeInMillis());
			beginIntent.putExtra(Extra.Time_Earliest, cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 15);// 15天内
			beginIntent.putExtra(Extra.Time_Latest, cal.getTimeInMillis());
			startActivityForResult(beginIntent, REQUEST_CODE_begintime);
			break;
		case R.id.invite_add_ll_addr:// 地址
			Intent workLocIntent = new Intent(this, SelectMapPointActivity.class);
//			workLocIntent.putExtra(Extra.Snippet, "活动地址");
			if(null != addrInfo){
				workLocIntent.putExtra(Extra.AddrModel, addrInfo);
			}
			startActivityForResult(workLocIntent, REQUEST_CODE_ADDR);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.invite_add_ll_description:
			if (resultCode == RESULT_OK && data != null) {
				etDescription.setText(data.getStringExtra("content"));
			}
			break;
		case REQUEST_CODE_TYPE:// 支付方式
			if (RESULT_OK == resultCode && null != data) {

				model.setPaytype((data.getIntExtra(Extra.SelectedItem,
						State.Selected_cancle) + 1) + "");

				item.setPaytype((data.getIntExtra(Extra.SelectedItem,
						State.Selected_cancle) + 1));
				tvPayType.setText(data.getStringExtra(Extra.SelectedItemStr));
			}
			break;
		case REQUEST_CODE_begintime:// 开始时间
			if (RESULT_OK == resultCode && null != data) {
				beginTime = data.getLongExtra(Extra.SelectedTime,
						State.Selected_cancle);
				// String beginTimeStr =
				// data.getStringExtra(Extra.SelectedTimeStr);

				String beginTimeStr = CalendarUtils.formatServer(beginTime);
				model.setTime(beginTimeStr);
				item.setTime(beginTimeStr);
				System.out.println(beginTimeStr);
				tvBeginTime.setText(CalendarUtils.formatYMDHM(beginTimeStr));
			}
			break;

		case REQUEST_CODE_ADDR:// 活动地址
			if (RESULT_OK == resultCode && null != data) {
				addrInfo = (AddrInfoModel) data.getSerializableExtra(Extra.AddrModel);
				model.setLatitude(addrInfo.getLatitudeE6() / 1E6 + "");
				item.setLatitude((float) (addrInfo.getLatitudeE6() / 1E6));
				model.setLongitude((float) (addrInfo.getLongitudeE6() / 1E6) + "");
				item.setLongitude((float) (addrInfo.getLongitudeE6() / 1E6));
				model.setAddress(addrInfo.getStrAddr() + " " + addrInfo.getDesc());
				item.setAddress(addrInfo.getStrAddr() + " " + addrInfo.getDesc());
//				String provinceName = addrInfo.getProvince();
//				String cityName = addrInfo.getCity();
//				String districtName = addrInfo.getDistrict();
//				AreaListDB db = new AreaListDB();
				tvAddr.setText(model.getAddress());
			}
			break;

		case REQUEST_CODE_PRICE:// 费用
			if (0 != Integer.parseInt(model.getPredictConsume())) {
				etMoney.setText("人均消费约" + model.getPredictConsume() + "元");

			} else {
				etMoney.setText("免费");
			}
			break;
		}
	}

	@Override
	protected boolean onBack() {
		MessageDialog dialog = new MessageDialog(this);
		dialog.setTitle("操作提示");
		dialog.setMessage("确定要退出本次发邀请吗？");
		dialog.setLeftButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		dialog.setRightButton("取消", null);
		dialog.show();
		return true;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etMoney.getText().toString().equals("0") || etMoney == null
				|| etMoney.getText().toString().equals("")) {
			llPaytype.setVisibility(View.GONE);
		} else {
			llPaytype.setVisibility(View.VISIBLE);
			// if (etMoney.getText().length()>3&&start<4) {
			// Toast.show("人均费用范围在0-999元");
			// }
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
