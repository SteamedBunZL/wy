package com.whoyao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.db.AreaListDB;
import com.whoyao.map.AddrInfoModel;
import com.whoyao.model.EventAddModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.FormatUtils;
import com.whoyao.widget.CustomRelativeLayout;
import com.whoyao.widget.CustomRelativeLayout.KeyboardChangeListener;

/**
 * @author hyh creat_at：2013-9-10-下午1:30:52
 */
public class EventAddActivity extends BasicActivity implements OnClickListener, OnCheckedChangeListener, OnFocusChangeListener {
	private int step = 0;
	private static final int REQUEST_CODE_TYPE = 0;
	private static final int REQUEST_CODE_begintime = 1;
	private static final int REQUEST_CODE_endtime = 2;
	
	private static final int REQUEST_CODE_ADDR = 4;
	private static final int REQUEST_CODE_NUM = 5;
	private EventAddModel model = new EventAddModel();
	private Button btnImmed;
	private Button btnNormal;
	private TextView tvType;
	private TextView tvBeginTime;
	private TextView tvEndTime;
	private TextView tvAddr;
	private TextView tvNum;
	private TextView tvPaytype;
	private TextView tvKeyword;
	private TextView tvDesc;
	private TextView tvSex;
	private TextView tvAge;
	private EditText etTitle;
	private EditText etPrice;

	private View llStep0;

	private View llStep1;

	private Button btnEnter;

	private Animation showAnim;

	private Animation hideAnim;

	private CheckBox cbConvert;

	private View llBill;
	private long beginTime;
	private long endTime;
	private Calendar cal = null;
	private ImageAsyncLoader loader;
	private ImageView ivBill;
	private MessageDialog immDialog;
	private MessageDialog norDialog;
	private File billImage;
	private View vPayType;
	private View vPrice;
	private View vCategory;
	private AddrInfoModel addrInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_add);
		initView();
		model.activitycategory = getIntent().getIntExtra(Extra.SelectedItem, Type.Category_Normal);
		setCategoryButton();
		loader = OriginalImageAsyncLoader.getInstance();
	}

	private void initView() {
		btnImmed = (Button) findViewById(R.id.event_add_btn_immediately);
		btnNormal = (Button) findViewById(R.id.event_add_btn_normal);
		btnEnter = (Button) findViewById(R.id.event_add_btn_enter);
		cbConvert = (CheckBox)findViewById(R.id.event_add_cb_convert);
		vCategory = findViewById(R.id.event_add_category_ll);
		btnImmed.setOnClickListener(this);
		btnNormal.setOnClickListener(this);
		btnEnter.setOnClickListener(this);
		cbConvert.setOnCheckedChangeListener(this);
		
		etTitle = (EditText) findViewById(R.id.event_add_et_title);
		etPrice = (EditText) findViewById(R.id.event_add_et_price);
		tvType = (TextView) findViewById(R.id.event_add_tv_type);
		tvBeginTime = (TextView) findViewById(R.id.event_add_tv_begintime);
		tvEndTime = (TextView) findViewById(R.id.event_add_tv_endtime);
		tvAddr = (TextView) findViewById(R.id.event_add_tv_addr);
		tvNum = (TextView) findViewById(R.id.event_add_tv_num);
		
		tvPaytype = (TextView) findViewById(R.id.event_add_tv_paytype);
		tvKeyword = (TextView) findViewById(R.id.event_add_tv_keyword);
		tvDesc = (TextView) findViewById(R.id.event_add_tv_desc);
		tvSex = (TextView) findViewById(R.id.event_add_tv_sex);
		tvAge = (TextView) findViewById(R.id.event_add_tv_age);
		ivBill = (ImageView)findViewById(R.id.event_add_iv_bill);
		llStep0 = findViewById(R.id.event_add_ll_info0);
		llStep1 = findViewById(R.id.event_add_ll_info1);

		vPrice = findViewById(R.id.event_add_ll_price);
		vPayType = findViewById(R.id.event_add_ll_paytype);
		vPayType.setOnClickListener(this);		
		findViewById(R.id.event_add_ll_type).setOnClickListener(this);
		findViewById(R.id.event_add_ll_title).setOnClickListener(this);
		findViewById(R.id.event_add_ll_begintime).setOnClickListener(this);
		findViewById(R.id.event_add_ll_endtime).setOnClickListener(this);
		findViewById(R.id.event_add_ll_addr).setOnClickListener(this);
		findViewById(R.id.event_add_ll_num).setOnClickListener(this);
		findViewById(R.id.event_add_ll_keywork).setOnClickListener(this);
		findViewById(R.id.event_add_ll_desc).setOnClickListener(this);
		findViewById(R.id.event_add_ll_sex).setOnClickListener(this);
		findViewById(R.id.event_add_ll_age).setOnClickListener(this);
		llBill = findViewById(R.id.event_add_ll_bill);
		llBill.setOnClickListener(this);
		
		findViewById(R.id.page_btn_back).setOnClickListener(this);

		showAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_show);
		hideAnim = AnimationUtils.loadAnimation(this, R.anim.plugin_hide);

		immDialog = new MessageDialog(this);
		immDialog.setTitle("操作提示");
		immDialog.setMessage("确定将活动类别修改为“即时活动”");
		immDialog.setRightButton("取消", null);
		immDialog.setLeftButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				model.activitycategory = Type.Category_Immediately;
				setCategoryButton();
			}
		});
		norDialog = new MessageDialog(this);
		norDialog.setTitle("操作提示");
		norDialog.setMessage("确定将活动类别修改为“普通活动”");
		norDialog.setRightButton("取消", null);
		norDialog.setLeftButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				model.activitycategory = Type.Category_Normal;
				setCategoryButton();
			}
		});
		CustomRelativeLayout rl = (CustomRelativeLayout) findViewById(R.id.rete_rl);
		rl.setOnKeyboardChangeListener(new KeyboardChangeListener() {
			@Override
			public void onKeyboardChange(int w, int h, int oldw, int oldh) {
				String priceStr = etPrice.getText().toString();
				if(!TextUtils.isEmpty(priceStr)){
					try{
						float price = Float.parseFloat(priceStr);
						if(999<price || 0 > price){
							Toast.show(R.string.warn_event_price_formatewrong);
							return;
						}else{
//							model.setPredictmaxconsume((int)price); // 网站目前是0
							model.setPredictminconsume((int)price);
							if(0 == price){
								vPrice.setBackgroundResource(R.drawable.selector_radius_bottom);
								vPayType.setVisibility(View.INVISIBLE);
								model.paytype = 0;
							}else{
								vPrice.setBackgroundResource(R.drawable.selector_radius_middle);
								vPayType.setVisibility(View.VISIBLE);
							}
						}
					}catch(Exception e){
						Toast.show(R.string.warn_event_desc_formatewrong);
						return;
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.event_add_btn_immediately:
			if(!v.isSelected() && step == 0){
				immDialog.show();
			}
			break;
		case R.id.event_add_btn_normal:
			if(!v.isSelected() && step == 0){
				norDialog.show();
			}
			break;
		case R.id.event_add_ll_type://类型
			Intent typeData = new Intent(this, SelectSingleWheelActivity.class);
			typeData.putExtra(Extra.ArrayRes, R.array.event_tags);
			startActivityForResult(typeData, REQUEST_CODE_TYPE);
			break;
		case R.id.event_add_ll_begintime:
			cal =CalendarUtils.getNewCalendar();
			Intent beginIntent = new Intent(this,SelectTimeHalfHourActivity.class);
			if(model.isImmediately()){
				cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+40);//及时活动，40分钟后
			}else{
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);//普通活动，1小时候
			}
			cal.setTimeInMillis(cal.getTimeInMillis());
			beginIntent.putExtra(Extra.Time_Earliest, cal.getTimeInMillis());
			
			if(model.isImmediately()){
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);//24小时内
			}else{
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+15);//15天内
			}
			beginIntent.putExtra(Extra.Time_Latest, cal.getTimeInMillis());
			startActivityForResult(beginIntent, REQUEST_CODE_begintime);
			break;
		case R.id.event_add_ll_endtime:
			if(null == cal || State.Selected_cancle == beginTime){
				Toast.show(R.string.warn_event_begintime_empty);
				return;
			}
			Intent endIntent = new Intent(this,SelectTimeHalfHourActivity.class);
			cal.setTimeInMillis(beginTime);
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
			cal.setTimeInMillis(cal.getTimeInMillis());
			endIntent.putExtra(Extra.Time_Earliest, cal.getTimeInMillis());
			if(model.isImmediately()){
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+2);//24小时内
			}else{
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+7);//15天内
			}
			endIntent.putExtra(Extra.Time_Latest, cal.getTimeInMillis());
			startActivityForResult(endIntent, REQUEST_CODE_endtime);
			break;
		case R.id.event_add_ll_addr:// 地址
			Intent workLocIntent = new Intent(this, SelectMapPointActivity.class);
			if(null != addrInfo){
				workLocIntent.putExtra(Extra.AddrModel, addrInfo);
			}
//			workLocIntent.putExtra(Extra.Snippet, "活动地址");
			startActivityForResult(workLocIntent, REQUEST_CODE_ADDR);
			break;
		case R.id.event_add_ll_num:
			Intent numData = new Intent(this, SelectDoubleNumberActivity.class);
			if(Type.Category_Immediately == model.getActivitycategory()){
				numData.putExtra(Extra.Max, Const.Event_maxjioner_immediately);
				numData.putExtra(Extra.Min, Const.Event_minjioner_immediately);
			}else{
				numData.putExtra(Extra.Max, Const.Event_maxjioner_normall);
				numData.putExtra(Extra.Min, Const.Event_minjioner_normall);
			}
			startActivityForResult(numData, REQUEST_CODE_NUM);
			break;
//		case R.id.event_add_ll_price:
//			Intent priceData = new Intent(this, SelectThreeNumberActivity.class);
//			priceData.putExtra(Extra.SelectedItem, model.getPredictmaxconsume());
//			startActivityForResult(priceData, REQUEST_CODE_PRICE);
//			break;
		case R.id.event_add_ll_paytype:
			Intent payData = new Intent(this, SelectSingleWheelActivity.class);
			payData.putExtra(Extra.ArrayRes, R.array.pay_type);
			startActivityForResult(payData, R.id.event_add_ll_paytype);
			break;
		case R.id.event_add_ll_keywork:
			Intent keyData = new Intent(this, EventAddKeywordActivity.class);
			keyData.putExtra(Extra.Snippet, model.getKeyword());
			startActivityForResult(keyData, R.id.event_add_ll_keywork);
			break;
		case R.id.event_add_ll_desc:
			Intent descData = new Intent(this, EventAddDescActivity.class);
			descData.putExtra(Extra.Snippet, model.getActivitydescription());
			startActivityForResult(descData, R.id.event_add_ll_desc);
			break;
		case R.id.event_add_ll_bill://海报
			Intent picIntent = new Intent(AppContext.context, SelectPhotoActivity.class);
			picIntent.putExtra(Extra.HAS_TO_CUT, true);
			picIntent.putExtra(Extra.ASPECT_X, 4);
			picIntent.putExtra(Extra.ASPECT_Y, 3);
			startActivityForResult(picIntent, R.id.event_add_ll_bill);
			break;
		case R.id.event_add_ll_sex:
			Intent sexIntent = new Intent(this, SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			sexIntent.putExtra(Extra.ButtonCancel_Text, "不限");
			startActivityForResult(sexIntent, R.id.event_add_ll_sex);
			break;
		case R.id.event_add_ll_age:
			Intent ageIntent = new Intent(this, SelectSingleWheelActivity.class);
			ageIntent.putExtra(Extra.ArrayRes, R.array.age_group);
			startActivityForResult(ageIntent, R.id.event_add_ll_age);
			break;
		case R.id.event_add_btn_enter:
			if(0 == step){ // 检查数据
				//是否为空
				if(0 == model.getTypeid()){
					Toast.show(R.string.warn_event_type_empty);
					return;
				}
				String title = etTitle.getText().toString();
				if(TextUtils.isEmpty(title)){
					Toast.show(R.string.warn_event_title_empty);
					return;
				}
				if(0 == beginTime){
					Toast.show(R.string.warn_event_begintime_empty);
					return;
				}
				if(0 == endTime){
					Toast.show(R.string.warn_event_endtime_empty);
					return;
				}
				if(0 == model.getLatitude()){
					Toast.show(R.string.warn_event_addr_empty);
					return;
				}
				if(0 == model.getMaxnum()){
					Toast.show(R.string.warn_event_num_empty);
					return;
				}
				String priceStr = etPrice.getText().toString();
				if(TextUtils.isEmpty(priceStr)){
					Toast.show(R.string.warn_event_price_empty);
					return;
				}else{
					try{
						float price = Float.parseFloat(priceStr);
						model.setPredictmaxconsume((int)price);
						model.setPredictminconsume((int)price);
					}catch(Exception e){
						model.setPredictmaxconsume(-1);
						model.setPredictminconsume(-1);
//						Toast.show(R.string.warn_event_price_formatewrong);
						return;
					}
				}
				if(0 != model.getPredictmaxconsume() && TextUtils.isEmpty(tvPaytype.getText())){
					Toast.show(R.string.warn_event_paytype_empty);
					return;
				}
				// 检查格式
				if(2>FormatUtils.getStringLengthEn1Cn1(title) || 20<FormatUtils.getStringLengthEn1Cn1(title)){
					Toast.show(R.string.warn_event_title_formatewrong);
					return;
				}
				model.setTitle(title);
				
				if(0>model.getPredictmaxconsume() || 999<model.getPredictmaxconsume()){
					Toast.show(R.string.warn_event_price_formatewrong);
					model.setPredictmaxconsume(-1);
					model.setPredictminconsume(-1);
					return;
				}
				setStep1();
			}else{
				if(TextUtils.isEmpty(model.getKeyword())){
					Toast.show(R.string.warn_event_keyword_empty);
					return;
				}
				if(TextUtils.isEmpty(model.getActivitydescription())){
					Toast.show(R.string.warn_event_desc_empty);
					return;
				}
				if(null == billImage || !billImage.exists()){
					Toast.show(R.string.warn_event_bill_empty);
					return;
				}
				if(5*1024*1024 < billImage.length()){
					Toast.show(R.string.warn_event_bill_toobig);
					return;
				}
				//  提交 先提交图片
				try {
						RequestParams params = new RequestParams();
						params.put(KEY.EventID, "0");
						params.put(KEY.PHOTO_SUBJECT, "Bill");
						params.put(KEY.Image, billImage);
						params.put("istemp", "1");
						Net.request(params, WebApi.Event.UpPhoto, new ResponseHandler(true){
							@Override
							public void onSuccess(String content) {
								try {
									JSONObject jObject = new JSONObject(content);
									String pill = (String) jObject.get("PhotoPath");
									if(null != pill){
										model.activitypicture = pill;
										Net.request(model, WebApi.Event.AddEvent, new ResponseHandler(true){
											@Override
											public void onSuccess(String content) {
												try {
													JSONObject jObject = new JSONObject(content);
													int result = (Integer) jObject.get(KEY.Result);
													if(2 == result){
														Toast.show(R.string.warn_event_busy);
													}else{
														Toast.show(R.string.warn_event_add_success);
														Intent intent = new Intent(context, EventDetialActivity.class);
														intent.putExtra(Extra.SelectedID, result);
														startActivity(intent);
														finish();
													}
												} catch (JSONException e) {
													AppException.handle(e);
												}
											}
										});
									}else{
										Toast.show(R.string.warn_netrequest_failure);
									}
								} catch (JSONException e) {
									AppException.handle(e);
								}
							}
						});
					} catch (FileNotFoundException e) {
						AppException.handle(e);
					}
			}
			break;
		case R.id.page_btn_back:
			onBack();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected boolean onBack() {
		if(1 == step){
			setStep0();
			return true;
		}else{
			MessageDialog dialog = new MessageDialog(this);
			dialog.setTitle("操作提示");
			dialog.setMessage("确定要退出本次发活动吗？");
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_TYPE:// 活动类型
			if(RESULT_OK == resultCode && null != data){
				model.setTypeid(data.getIntExtra(Extra.SelectedItem, State.Selected_cancle) + 1);
				tvType.setText(data.getStringExtra(Extra.SelectedItemStr));
			}
			break;
		case REQUEST_CODE_begintime://开始时间
			if(RESULT_OK == resultCode && null != data){
				beginTime = data.getLongExtra(Extra.SelectedTime, State.Selected_cancle);
//				String beginTimeStr = data.getStringExtra(Extra.SelectedTimeStr);
				String beginTimeStr = CalendarUtils.formatServer(beginTime);
				model.setBegintime(beginTimeStr);
				tvBeginTime.setText(CalendarUtils.formatYMDHM(beginTimeStr));
				if(0!=endTime && beginTime>(endTime+1000*60*60)){
					tvEndTime.setText("");
					model.setEndtime("");
					endTime = 0;
				}
			}
			break;
		case REQUEST_CODE_endtime://结束时间
			if(RESULT_OK == resultCode && null != data){
				endTime = data.getLongExtra(Extra.SelectedTime, State.Selected_cancle);
				String endTimeStr = CalendarUtils.formatServer(endTime);
				model.setEndtime(endTimeStr);
				tvEndTime.setText(CalendarUtils.formatYMDHM(endTimeStr));
			}
			break;
		case REQUEST_CODE_ADDR://活动地址
			if (RESULT_OK == resultCode && null != data) {
				addrInfo = (AddrInfoModel) data.getSerializableExtra(Extra.AddrModel);
				model.latitude = (float) (addrInfo.getLatitudeE6()/1E6);
				model.longitude = (float) (addrInfo.getLongitudeE6()/1E6);
				model.address = addrInfo.getStrAddr() + " " +addrInfo.getDesc();
				String provinceName = addrInfo.getProvince();
				String cityName = addrInfo.getCity();
				String districtName = addrInfo.getDistrict();
				
				
				AreaListDB db = new AreaListDB();
				if(!TextUtils.isEmpty(districtName)){
					model.userprovince = db.getAreaByName(provinceName).getCode();
				}
				if(!TextUtils.isEmpty(cityName)){
					model.usercity = db.getAreaByName(cityName).getCode();
				}
				if(!TextUtils.isEmpty(districtName)){
					model.userdistrict = db.getAreaByName(districtName).getCode();
				}
				tvAddr.setText(model.address);
			}
			break;
		case REQUEST_CODE_NUM:// 人数
			if(RESULT_OK == resultCode && null != data){
				model.setMinnum(data.getIntExtra(Extra.Min, 0));
				model.setMaxnum(data.getIntExtra(Extra.Max, 0));
				if(0 != model.getMaxnum() && 0 != model.getMinnum()){
					if(model.getMaxnum() == model.getMinnum()){
						tvNum.setText(model.getMinnum()+"人");
					}else{
						tvNum.setText(model.getMinnum()+" - "+model.getMaxnum()+"人");
					}
				}
			}
			break;
//		case REQUEST_CODE_PRICE://费用
//			if(RESULT_OK == resultCode && null != data){
//				model.setPredictmaxconsume(data.getIntExtra(Extra.SelectedItem, 0));
//				model.setPredictminconsume(data.getIntExtra(Extra.SelectedItem, 0));
//				if(0 != model.getPredictmaxconsume()){
//					etPrice.setText("人均消费约"+model.getPredictmaxconsume()+"元");
//				}else{
//					etPrice.setText("免费");
//				}
//			}
//			break;
		case R.id.event_add_ll_paytype://付款方式
			if(RESULT_OK == resultCode && null != data){
				model.setPaytype(data.getIntExtra(Extra.SelectedItem, State.Selected_cancle) + 1);
				tvPaytype.setText(data.getStringExtra(Extra.SelectedItemStr));
			}
			break;
		case R.id.event_add_ll_bill://海报
			if (RESULT_OK == resultCode && null != data) {
				String path = data.getStringExtra(Extra.IMAGE_PATH);
				if (!TextUtils.isEmpty(path)) {
					File file = new File(path);
					if(file.exists()){
						billImage = file;
						ivBill.setVisibility(View.VISIBLE);
						loader.loadBitmap(new File(path), ivBill);
					}
				}
			}
			break;
		case R.id.event_add_ll_keywork:
			if(RESULT_OK == resultCode && null != data){
				model.setKeyword(data.getStringExtra(Extra.Snippet));
				tvKeyword.setText(model.getKeyword());
			}
			break;
		case R.id.event_add_ll_desc:
			if(RESULT_OK == resultCode && null != data){
				model.setActivitydescription(data.getStringExtra(Extra.Snippet));
				tvDesc.setText(model.getActivitydescription());
			}
			break;
		case R.id.event_add_ll_sex:
			if (RESULT_OK == resultCode) {
				model.setSex(1 + data.getIntExtra(Extra.SelectedItem, State.Selected_cancle));
				tvSex.setText(data.getStringExtra(Extra.SelectedItemStr));
			}
			break;
		case R.id.event_add_ll_age:
			if (RESULT_OK == resultCode) {
				tvAge.setText(data.getStringExtra(Extra.SelectedItemStr));
				switch (data.getIntExtra(Extra.SelectedItem, State.Selected_cancle)) {
				case 0:
					model.setMinage(0);
					model.setMaxage(0);
					break;
				case 1:
					model.setMinage(0);
					model.setMaxage(18);
					break;
				case 2:
					model.setMinage(18);
					model.setMaxage(25);
					break;
				case 3:
					model.setMinage(25);
					model.setMaxage(28);
					break;
				case 4:
					model.setMinage(28);
					model.setMaxage(32);
					break;
				case 5:
					model.setMinage(32);
					model.setMaxage(35);
					break;
				case 6:
					model.setMinage(35);
					model.setMaxage(38);
					break;
				case 7:
					model.setMinage(38);
					model.setMaxage(45);
					break;
				case 8:
					model.setMinage(45);
					model.setMaxage(100);
					break;
				default:
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	
	/** 设置CategoryButton状态 */
	private void setCategoryButton() {
		switch (model.activitycategory) {
		case Type.Category_Immediately:
			btnImmed.setSelected(true);
			btnNormal.setSelected(false);
			cbConvert.setVisibility(View.VISIBLE);
			llBill.setBackgroundResource(R.drawable.selector_radius_middle);
			Calendar cal = CalendarUtils.getNewCalendar();
			cal.setTimeInMillis(beginTime);
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
			cal.setTimeInMillis(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+2);//48小时内
			cal.getTimeInMillis();
			if(cal.getTimeInMillis()<endTime){
				endTime = 0;
				model.setEndtime("");
				tvEndTime.setText("");
				if(1 == step){
					setStep0();
				}
			}
			break;
		case Type.Category_Normal:
			btnImmed.setSelected(false);
			btnNormal.setSelected(true);
			cbConvert.setVisibility(View.GONE);
			llBill.setBackgroundResource(R.drawable.selector_radius_bottom);
			Calendar calendar = CalendarUtils.getNewCalendar();
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+1);
			if(0 != beginTime && beginTime < calendar.getTimeInMillis()){
				beginTime = 0;
				endTime = 0;
				model.setBegintime("");
				model.setEndtime("");
				tvBeginTime.setText("");
				tvEndTime.setText("");
				if(1 == step){
					setStep0();
				}
			}
			break;
		default:
			break;
		}
	}

	private void setStep0(){
		step = 0;
		vCategory.setVisibility(View.VISIBLE);
		llStep0.setVisibility(View.VISIBLE);
		llStep1.setVisibility(View.GONE);
		llStep1.startAnimation(hideAnim);
		btnEnter.setText(R.string.next);
	}
	private void setStep1(){
		step = 1;
		vCategory.setVisibility(View.GONE);
		llStep0.setVisibility(View.GONE);
		llStep1.setVisibility(View.VISIBLE);
		llStep1.startAnimation(showAnim);
		btnEnter.setText(R.string.event_add_title);
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		model.setConvertstate(isChecked);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
	}
}
