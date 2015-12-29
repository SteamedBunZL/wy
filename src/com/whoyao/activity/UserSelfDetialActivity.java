package com.whoyao.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import com.whoyao.AppContext;
import com.whoyao.AppException;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.adapter.UserSelfPhotoGridAdapter;
import com.whoyao.common.FaceImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.FreeModel;
import com.whoyao.model.TagModel;
import com.whoyao.model.UpUserDetailModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.model.UserPhotoModel;
import com.whoyao.net.NetCache;
import com.whoyao.topic.TopicUserListActivity;
import com.whoyao.ui.Toast;
import com.whoyao.utils.AddressUtil;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.Utils;
import com.whoyao.widget.ElasticScrollView;
import com.whoyao.widget.HorizontalListView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 我的资料
 * 
 * @author hyh Creat_at：2013-7-23-上午10:00:54
 */
public class UserSelfDetialActivity extends BasicActivity implements OnClickListener, OnFocusChangeListener,
		TextWatcher, OnItemClickListener {

	// private HorizontalListView photoHlv;
	private TextView setContentTv;
	private TextView tvNick;
	private ImageView ivFace;
	// private ImageView backIv;
	private TextView tvCity;
	private TextView consTv;
	private TextView heightTv;
	private TextView waistTv;
	private TextView bloodTv;
	private TextView ageTv;
	private TextView tvEvent;
	private TextView tvTopic;
	private TextView setNickTv;
	private TextView setCityTv;
	private TextView setBirthTv;
	private TextView setSexTv;
	private EditText etSetHeight;
	private EditText etSetWaist;
	private TextView setBloodTv;
	private ImageView mobileRbn;
	private ImageView honestyeRbn;
	private TextView freeTv;
	// private GridView tagGv;
	private LinearLayout tagLl;
	private ImageAsyncLoader loader;
	// private UserInfoModel info;
	private Random random;
	private int[] colorArray;
	private int width;
	private int lineNum;
	private ElasticScrollView sv;
	private static int[] paddings = { 15, 10, 20, 15, 20, 10, 25 };
	// private static int[] paddings = { 0, 0, 0, 0, 0, 0, 0 };
	private TextView tvHeightEdit;
	private TextView tvWaisttEdit;
	private EditText etCurrent;
	private boolean isSettingChanged;
	private HorizontalListView hlPhoto;
	private UserSelfPhotoGridAdapter photoAdapter;
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_self_detial);
		random = new Random();
		colorArray = AppContext.getRes().getIntArray(R.array.tag_color);
		width = ClientEngine.getMobileInfo().getVgaWidth() - Utils.dip2px(this, 20);
		initView();
		try {
			requestData();// 异步加载:头像,背景,照片,闲人预告,标签
		} catch (Exception e) {
			AppException.handle(e);
		}
		loader = FaceImageAsyncLoader.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// setData();
		setDetialData();
		loader.loadBitmap(MyinfoManager.getUserInfo().getUserFace(), ivFace);
	}

	private void initView() {
//		sv.scrollTo(0, 0);

		hlPhoto = (HorizontalListView) findViewById(R.id.my_hl_photo);

		// 顶部内容
		tvNick = (TextView) findViewById(R.id.my_nickname);
		ivFace = (ImageView) findViewById(R.id.my_iv_face);
		ivBack = (ImageView) findViewById(R.id.my_iv_background);
		sv = (ElasticScrollView) findViewById(R.id.my_scrollview);
		sv.setElasticView(ivBack);
		tvCity = (TextView) findViewById(R.id.my_city);
		consTv = (TextView) findViewById(R.id.my_constellation);
		ageTv = (TextView) findViewById(R.id.my_age);
		heightTv = (TextView) findViewById(R.id.my_height);
		waistTv = (TextView) findViewById(R.id.my_waist);
		bloodTv = (TextView) findViewById(R.id.my_blood);
		tvEvent = (TextView) findViewById(R.id.my_event_count);
		// 让scrollview滚到最顶部，让tvevent获取焦点
		tvEvent.setFocusable(true);
		tvEvent.setFocusableInTouchMode(true);
		tvEvent.requestFocus();

		tvTopic = (TextView) findViewById(R.id.my_topic_count);
		mobileRbn = (ImageView) findViewById(R.id.my_mobile_state);
		honestyeRbn = (ImageView) findViewById(R.id.my_honestye_state);

		// 中部内容
		freeTv = (TextView) findViewById(R.id.my_tv_free);
		// tagGv = (GridView) findViewById(R.id.my_gv_tag);
		tagLl = (LinearLayout) findViewById(R.id.my_ll_tags);
		setContentTv = (TextView) findViewById(R.id.my_set_content);
		setNickTv = (TextView) findViewById(R.id.my_set_nick);
		setCityTv = (TextView) findViewById(R.id.my_set_city);
		setBirthTv = (TextView) findViewById(R.id.my_set_birthday);
		setSexTv = (TextView) findViewById(R.id.my_set_sex);
		setBloodTv = (TextView) findViewById(R.id.my_set_blood);
		etSetHeight = (EditText) findViewById(R.id.my_set_height);
		tvHeightEdit = (TextView) findViewById(R.id.my_set_height_label);
		etSetWaist = (EditText) findViewById(R.id.my_set_waist);
		tvWaisttEdit = (TextView) findViewById(R.id.my_set_waist_label);

		etSetHeight.setOnFocusChangeListener(this);
		etSetWaist.setOnFocusChangeListener(this);
		etSetHeight.addTextChangedListener(this);
		etSetWaist.addTextChangedListener(this);
		// 点击事件
		tvEvent.setOnClickListener(this);
		tvTopic.setOnClickListener(this);
		ivFace.setOnClickListener(this);
		tvHeightEdit.setOnClickListener(this);
		tvWaisttEdit.setOnClickListener(this);
		findViewById(R.id.my_btn_back).setOnClickListener(this);
		// findViewById(R.id.my_btn_add_photo).setOnClickListener(this);
		findViewById(R.id.my_ll_free).setOnClickListener(this);
		findViewById(R.id.my_ll_envet).setOnClickListener(this);
		findViewById(R.id.my_ll_topic).setOnClickListener(this);
		findViewById(R.id.my_ll_content).setOnClickListener(this);
		findViewById(R.id.my_ll_tags).setOnClickListener(this);
		// findViewById(R.id.my_ll_nick).setOnClickListener(this);
		findViewById(R.id.my_iv_photo).setOnClickListener(this);
		findViewById(R.id.my_ll_city).setOnClickListener(this);
		findViewById(R.id.my_ll_birthday).setOnClickListener(this);
		findViewById(R.id.my_ll_sex).setOnClickListener(this);
		findViewById(R.id.my_ll_height).setOnClickListener(this);
		findViewById(R.id.my_ll_waist).setOnClickListener(this);
		findViewById(R.id.my_ll_blood).setOnClickListener(this);
		findViewById(R.id.my_ll_more).setOnClickListener(this);
		findViewById(R.id.my_ll_password).setOnClickListener(this);
	}

	/**
	 * 异步加载部分信息 头像图片,标签,闲人预告,照片
	 */
	private void requestData() {

		MyinfoManager.getManager().getMyDetil(true, new CallBack<UserDetialModel>() {
			@Override
			public void onCallBack(UserDetialModel data) {
				// info = MyinfoManager.userInfo;
				setData();
				setDetialData();
			}
		});

	}

	private void setData() {
		// info = MyinfoManager.userInfo;
		if (null != MyinfoManager.getUserInfo()) {
			tvNick.setText(MyinfoManager.getUserInfo().getUserName());
			setNickTv.setText(MyinfoManager.getUserInfo().getUserName());
			setBithdayData();
			setSexData();
			setHeightData();
			setWaistData();
			bloodTv.setText(MyinfoManager.getUserInfo().getBloodStr());// 血型
			setBloodTv.setText(MyinfoManager.getUserInfo().getBloodStr());
			tvEvent.setText(MyinfoManager.getUserInfo().getActivityCount() + "");// 话题数
			tvTopic.setText(MyinfoManager.getUserInfo().getTopicCount() + "");
			mobileRbn.setSelected(MyinfoManager.getUserInfo().isMobileV());
			honestyeRbn.setSelected(MyinfoManager.getUserInfo().isHonestyV());
		}
	}

	private void setDetialData() {
		if (null == MyinfoManager.userFree) {
			return;
		}
		setContentTv.setText(MyinfoManager.getUserInfo().getUserContent());// 签名
		// 城市
		setAddrData();
		// 闲人预告
		ArrayList<FreeModel> myFree = MyinfoManager.userFree;
		if (0 < myFree.size()) {
			FreeModel freeModel = myFree.get(0);
			String freeTime = CalendarUtils.formatYMDHM(freeModel.getFreeTimeStart()) + " ~ "
					+ CalendarUtils.formatYMDHM(freeModel.getFreeTimeEnd());
			freeTv.setTextSize(13);
			freeTv.setText(freeTime);
		}
		photoAdapter = new UserSelfPhotoGridAdapter(MyinfoManager.userPhoto);
		hlPhoto.setAdapter(photoAdapter);
		hlPhoto.setOnItemClickListener(this);
		// 标签
		tagLl.removeAllViews();
		initTag(tagLl);

	}

	private void setWaistData() {
		if (0 != MyinfoManager.getUserInfo().getUserWaist()) {
			waistTv.setText(MyinfoManager.getUserInfo().getUserWaist() + "kg");
			etSetWaist.setText(MyinfoManager.getUserInfo().getUserWaist() + "");
			tvWaisttEdit.setText("kg");
		} else {
			waistTv.setVisibility(View.GONE);
			tvWaisttEdit.setText("");
		}
	}

	private void setHeightData() {
		if (0 != MyinfoManager.getUserInfo().getUserHeight()) {
			heightTv.setText(MyinfoManager.getUserInfo().getUserHeight() + "cm");
			etSetHeight.setText(MyinfoManager.getUserInfo().getUserHeight() + "");
			tvHeightEdit.setText("cm");
		} else {
			heightTv.setVisibility(View.GONE);
			tvHeightEdit.setText("");
		}
	}

	/**
	 * 设置生日相关显示 生日,星座,年龄
	 */
	private void setBithdayData() {
		if (!"0001-01-01 00:00:00".equals(MyinfoManager.getUserInfo().getUserBirthday())) {
			setBirthTv.setText(CalendarUtils.formatYMD(MyinfoManager.getUserInfo().getUserBirthday()));
			consTv.setText(MyinfoManager.getUserInfo().getUserConstellation());// 星座
			ageTv.setText(MyinfoManager.getUserInfo().getUserAge());// ageTv年龄
		}
	}

	/**
	 * 设置性别相关显示 性别文字,头像边框
	 */
	private void setSexData() {
		isSettingChanged = true;
		setSexTv.setText(MyinfoManager.getUserInfo().getSexStr());
		switch (MyinfoManager.getUserInfo().getUserSex()) {
		case 1:
			ivFace.setBackgroundResource(R.drawable.rectangle_stroke_blue);
			break;
		case 2:
			ivFace.setBackgroundResource(R.drawable.rectangle_stroke_carmine);
			break;
		}
	}

	/**
	 * 设置城市相关显示 城市,所在地
	 */
	private void setAddrData() {
		String cityName = AddressUtil.getCityName(MyinfoManager.getUserInfo().getUserCity());
		tvCity.setText(cityName);
		setCityTv.setText(AddressUtil.getFullAddr(MyinfoManager.getUserInfo().getUserDistrict()));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.my_btn_back:
			onBack();
			// Intent data = new Intent();
			// data.putExtra(Extra.SelectedItem, State.Button_0);
			// onActivityResult(R.id.my_ll_sex, RESULT_OK, data);
			break;
		case R.id.my_iv_face:// 选择新头像(含裁剪)
			Intent faceIntent = new Intent(AppContext.context, SelectPhotoActivity.class);
			faceIntent.putExtra(Extra.HAS_TO_CUT, true);
			startActivityForResult(faceIntent, R.id.my_iv_face);
			break;
		// case R.id.my_btn_add_photo:// 选择照片(不含裁剪)
		// Intent photoIntent = new Intent(AppContext.context,
		// SelectPhotoActivity.class);
		// startActivityForResult(photoIntent, R.id.my_btn_add_photo);
		// break;
		case R.id.my_ll_free:/* 没有闲人预告时,点击发,有时,点击查看列表 */
			if (TextUtils.isEmpty(freeTv.getText())) {
				AppContext.startAct(MyFreeAddActivity.class);
			} else {
				AppContext.startAct(MyFreeActivity.class);
			}
			break;
		case R.id.my_ll_envet:// 查看活动
			AppContext.startAct(EventMyListActivity.class);
			break;
		case R.id.my_ll_topic:// 查看话题
			// TODO
			Intent topicIntent = new Intent(context,TopicUserListActivity.class);
			startActivity(topicIntent);
			break;
		case R.id.my_iv_photo:// 查看照片
			if (null != MyinfoManager.userPhoto && !MyinfoManager.userPhoto.isEmpty()) {
				Intent photoViewIntent = new Intent(AppContext.context, UserPhotoActivity.class);
				photoViewIntent.putExtra(Extra.Photos, MyinfoManager.userPhoto);
				photoViewIntent.putExtra(Extra.SelectedItem, 0);
				startActivity(photoViewIntent);
			}
			break;
		case R.id.my_ll_content:// 修改签名
			Intent contentIntent = new Intent(this, MySetContentActivity.class);
			contentIntent.putExtra(Extra.MyContent, setContentTv.getText().toString());
			startActivityForResult(contentIntent, R.id.my_ll_content);
			break;
		case R.id.my_ll_tags:// 修改标签
			AppContext.startAct(MyTagActivity.class);
			break;
		case R.id.my_ll_nick:// 修改昵称

			break;
		case R.id.my_ll_city:// 修改城市
			Intent addrIntent = new Intent(this, SelectAddrActivity.class);
			startActivityForResult(addrIntent, R.id.my_ll_city);
			break;
		case R.id.my_ll_birthday:// 修改生日
			Intent intent = new Intent(this, SelectDateActivity.class);
			try {
				long timeMills = CalendarUtils.parseDate(MyinfoManager.getUserInfo().getUserBirthday());
				intent.putExtra(Extra.DefaultTimeMillis, timeMills);
			} catch (Exception e) {
				AppException.handle(e);
			}
			startActivityForResult(intent, R.id.my_ll_birthday);
			break;
		case R.id.my_ll_sex:// 修改性别
			Intent sexIntent = new Intent(this, SelectButton3Activity.class);
			sexIntent.putExtra(Extra.Button0_Text, "男");
			sexIntent.putExtra(Extra.Button1_Text, "女");
			startActivityForResult(sexIntent, R.id.my_ll_sex);
			break;
		case R.id.my_set_height_label:
			etSetHeight.requestFocus();
			break;
		case R.id.my_set_waist_label:
			etSetWaist.requestFocus();
			break;
		case R.id.my_ll_blood:
			Intent bloodIntent = new Intent(this, SelectSingleWheelActivity.class);
			bloodIntent.putExtra(Extra.ArrayRes, R.array.blood_group);
			startActivityForResult(bloodIntent, R.id.my_ll_blood);
			break;
		case R.id.my_ll_more:
			AppContext.startAct(MyMoreinfoActivity.class);
			break;
		case R.id.my_ll_password:
			AppContext.startAct(MyChangePwdActivity.class);
			break;
		default:
			break;
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		etCurrent = (EditText) v;
		if (hasFocus) {
			int length = etCurrent.getText().length();
			etCurrent.setSelection(length);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.my_hl_photo:// 照片
			if (RESULT_OK == resultCode) {
				final String path = data.getStringExtra(Extra.IMAGE_PATH);
				if (!TextUtils.isEmpty(path)) {
					MyinfoManager.getManager().addUserPhoto(path, new CallBack<UserPhotoModel>() {
						@Override
						public void onCallBack(UserPhotoModel data) {
							{
								Toast.show("上传成功");
								if (null != data) {
									MyinfoManager.userPhoto.add(0, data);
									photoAdapter.notifyDataSetChanged();
								}
							}
						}
					});
				}
			}
			break;
		case R.id.my_iv_face:// 头像
			if (RESULT_OK == resultCode && null != data) {
				String path = data.getStringExtra(Extra.IMAGE_PATH);
				if (!TextUtils.isEmpty(path)) {
					MyinfoManager.getManager().upUserFace(path, new CallBack<String>(path) {
						@Override
						public void onCallBack(boolean isSuccess, String data) {
							Toast.show("上传成功");
							NetCache.clearCaches();
							loader.loadBitmap(new File((String) params[0]), ivFace);
							try {
								JSONObject object = new JSONObject(data);
								String faceUrl = (String) object.get("UserFaceUrl");
								MyinfoManager.getUserInfo().setUserFace(faceUrl);
							} catch (JSONException e) {
								AppException.handle(e);
							}
						}
					});
				}
			}
			break;
		case R.id.my_ll_content:// 签名
			if (RESULT_OK == resultCode && null != data) {
				MyinfoManager.getUserInfo().setUserContent(data.getStringExtra(Extra.MyContent));
				setContentTv.setText(MyinfoManager.getUserInfo().getUserContent());
				isSettingChanged = true;
			}
			break;
		case R.id.my_ll_city:// 城市
			if (RESULT_OK == resultCode && null != data) {
				MyinfoManager.getUserInfo().setUserProvince(data.getIntExtra(Extra.ProvinceCode, 0));
				MyinfoManager.getUserInfo().setUserCity(data.getIntExtra(Extra.CityCode, 0));
				MyinfoManager.getUserInfo().setUserDistrict(data.getIntExtra(Extra.DistrictCode, 0));
				setAddrData();
				isSettingChanged = true;
			}
			break;
		case R.id.my_ll_birthday:// 生日
			if (RESULT_OK == resultCode && null != data) {
				MyinfoManager.getUserInfo().setUserBirthday(data.getStringExtra(Extra.SelectedTimeStr));
				setBithdayData();
				isSettingChanged = true;
			}
			break;
		case R.id.my_ll_sex:// 性别
			if (RESULT_OK == resultCode && null != data) {
				switch (data.getIntExtra(Extra.SelectedItem, State.Selected_cancle)) {
				case State.Button_0:
					MyinfoManager.getUserInfo().setUserSex(State.Sex_Man);
					setSexData();
					break;
				case State.Button_1:
					MyinfoManager.getUserInfo().setUserSex(State.Sex_Woman);
					setSexData();
					break;
				default:
					break;
				}
				isSettingChanged = true;
			}
			break;
		case R.id.my_ll_blood:// 血型
			if (RESULT_OK == resultCode && null != data) {
				int select = data.getIntExtra(Extra.SelectedItem, State.Selected_cancle);
				if (State.Selected_cancle != select) {
					MyinfoManager.getUserInfo().setUserBloodGroup(select + 1);
					bloodTv.setText(MyinfoManager.getUserInfo().getBloodStr());
					setBloodTv.setText(MyinfoManager.getUserInfo().getBloodStr());
					isSettingChanged = true;
				}
			}
			break;
		default:
			break;
		}
	}


	private void initTag(LinearLayout parent) {
		LinearLayout line = getLine();
		int lineWidth = 0;
		parent.addView(line);
		// View title = getTagItem("标签",Color.TRANSPARENT);
		View title = getTagItem("标签", Color.TRANSPARENT);
		TextView tv = (TextView) title.findViewById(R.id.itemtag_tv_tag_tag);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tv.setGravity(Gravity.LEFT);
		line.addView(title);
		TextView tvTag = (TextView) findViewById(R.id.my_set_tag);
		if (MyinfoManager.userTag == null || MyinfoManager.userTag.isEmpty()) {
			tvTag.setHint("请填写");
			return;
		} else {
			tvTag.setHint("");
		}
		lineWidth += title.getMeasuredWidth();
		ArrayList<String> tagList = new ArrayList<String>();
		for (TagModel tag : MyinfoManager.userTag) {
			tagList.add(tag.getTagName());
		}

		for (int i = 0; i < tagList.size(); i++) {
			String text = tagList.get(i);
			int drawable[] = { R.drawable.rectangle_radius_tag_red, R.drawable.rectangle_radius_tag_blue,
					R.drawable.rectangle_radius_tag_green };
			View view = getItem(text, drawable[i % 3]);
			// View view = getItem(text,
			// colorArray[random.nextInt(colorArray.length)]);
			lineWidth += view.getMeasuredWidth();
			if (width >= lineWidth) {
				line.addView(view);
			} else {
				line = getLine();
				line.setPadding(paddings[++lineNum % 7], 0, 0, 0);
				parent.addView(line);
				line.addView(view);
				lineWidth = view.getMeasuredWidth();
			}
		}
	}

	private LinearLayout getLine() {
		return (LinearLayout) View.inflate(this, R.layout.item_line_tag, null);
	}

	private View getItem(String text, int drawable) {
		View item = View.inflate(this, R.layout.item_tag2, null);
		item.findViewById(R.id.itemtag_ll).setBackgroundResource(drawable);
		TextView tv = (TextView) item.findViewById(R.id.itemtag_tv_tag);
		tv.setText(text);
		if (4 < text.length()) {
			tv.setEms(text.length() / 2 + text.length() % 2);
		}
		Utils.measureView(item);
		return item;
	}

	private View getTagItem(String text, int drawable) {
		View item = View.inflate(this, R.layout.item_tag_tag, null);
		item.findViewById(R.id.itemtag_ll_tag).setBackgroundResource(drawable);
		TextView tv = (TextView) item.findViewById(R.id.itemtag_tv_tag_tag);
		tv.setText(text);
		if (4 < text.length()) {
			tv.setEms(text.length() / 2 + text.length() % 2);
		}
		Utils.measureView(item);
		return item;
	}

	private int getDivide(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			if (4 < list.get(i).length()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	protected boolean onBack() {
		if (isSettingChanged) {
			String heightStr = etSetHeight.getText().toString().trim();
			String waistStr = etSetWaist.getText().toString().trim();
			if (!TextUtils.isEmpty(heightStr) && !TextUtils.isDigitsOnly(heightStr)) {
				Toast.show("请输入正确的身高值\n(50-280cm之间)");
				return true;
			}
			if (!TextUtils.isEmpty(waistStr) && !TextUtils.isDigitsOnly(waistStr)) {
				Toast.show("请输入正确的体重值\n(30-300kg之间)");
				return true;
			}

			int height = 0;
			int waist = 0;
			if (!TextUtils.isEmpty(heightStr)) {
				height = Integer.parseInt(heightStr);
			}
			if (!TextUtils.isEmpty(waistStr)) {
				waist = Integer.parseInt(waistStr);
			}

			if (0 != height && (50 > height || 280 < height)) {
				Toast.show("请输入正确的身高值\n(50-280cm之间)");
				return true;
			}
			if (0 != waist && (30 > waist || 300 < waist)) {
				Toast.show("请输入正确的体重值\n(30-300kg之间)");
				return true;
			}
			MyinfoManager.getUserInfo().setUserHeight(height);
			MyinfoManager.getUserInfo().setUserWaist(waist);
			UpUserDetailModel model = new UpUserDetailModel(MyinfoManager.getUserInfo());
			MyinfoManager.getManager().upUserInfo(model);
		}
		finish();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (0 == position) { // 选择照片(不含裁剪)
			Intent photoIntent = new Intent(AppContext.context, SelectPhotoActivity.class);
			startActivityForResult(photoIntent, R.id.my_hl_photo);
		} else {
			Intent photoViewIntent = new Intent(AppContext.context, UserPhotoActivity.class);
			photoViewIntent.putExtra(Extra.Photos, MyinfoManager.userPhoto);
			photoViewIntent.putExtra(Extra.SelectedItem, position - 1);
			startActivity(photoViewIntent);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (etCurrent != null) {
			if (etCurrent.getId() == R.id.my_set_height) {
				isSettingChanged = true;
				if (TextUtils.isEmpty(s)) {
					tvHeightEdit.setText("");
				} else {
					tvHeightEdit.setText("cm");
				}
			} else if (etCurrent.getId() == R.id.my_set_waist) {
				isSettingChanged = true;
				if (TextUtils.isEmpty(s)) {
					tvWaisttEdit.setText("");
				} else {
					tvWaisttEdit.setText("kg");
				}
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) { }


}
