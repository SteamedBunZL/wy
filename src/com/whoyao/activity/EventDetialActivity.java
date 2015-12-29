package com.whoyao.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventPhotoGridAdapter;
import com.whoyao.adapter.Joiner4GridAdapter;
import com.whoyao.common.BillImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.EventInfoModel;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.model.MyFriendTModel;
import com.whoyao.model.UserListItemModel;
import com.whoyao.model.UserListRModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.widget.ElasticScrollView;

/**
 * 活动详情
 * 
 * @author hyh creat_at：2013-9-9-上午9:51:28
 */
public class EventDetialActivity extends BasicActivity implements
		OnClickListener {
	private View tvRemark;
	private View tvJoin;
	private View tvInvite;
	private TextView tvInterest;
	private TextView tvType;
	private TextView tvTitle;
	private TextView tvTime;
	private TextView tvInfo00;
	private TextView tvInfo01;
	private TextView tvInfo02;
	private TextView tvKeyword;
	private TextView tvAddr;
	private TextView tvDesc;
	private TextView tvJoinnum;
	private TextView tvMaxnum;
	private TextView tvCommentCount;
	private TextView tvAtmosphereValue;
	private TextView tvAtmosphereDesc;
	private TextView tvAttendedDesc;
	private TextView tvPriceValue;
	private TextView tvAttendedValue;
	private TextView tvPriceDesc;
	private TextView tvAddressValue;
	private TextView tvAddressDesc;
	private ImageView ivPicture;
	// 活动交流
	private TextView tvEventRemarkTitleOne;
	private TextView tvEventRemarkTitleTwo;
	private TextView tvEventRemarkContentOne;
	private TextView tvEventRemarkContentTwo;
	private TextView tvEventRemarkTimeOne;
	private TextView tvEventRemarkTimeTwo;
	private TextView tvNoneValue;
	private TextView tvValue;
	private ElasticScrollView scroll;
	private MyFriendTModel searchModel = new MyFriendTModel();
	private ImageAsyncLoader pillLoader;
	
	private boolean isCreater;
	private boolean isJoiner;
	private GridView gvPhoto;
	private View tvUnJoin;
	private View tvAccuse;
	private View tvSetValue;
	private int eventId;
	private GridView gvJoiner;
	private ImageView ivPhoto;
	private ImageView ivJoiner;
	private TextView tvInfo10;
	private TextView tvInfo11;
	private boolean hasFriend=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detial);
		initView();
		SmallImageAsyncLoader.getInstance();
		pillLoader = BillImageAsyncLoader.getInstance();
		eventId = getIntent().getIntExtra(Extra.SelectedID, 0);
		if (0 == eventId) {
			finish();
			return;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		EventDetialManager.getInstance().getDetial(eventId,
				new CallBack<String>() {
					@Override
					public void onCallBack() {
						setBottomButtonState();
						setData();
					}
				});
	}

	@SuppressLint("NewApi")
	// 已经判断了
	private void initView() {
		tvValue = (TextView) findViewById(R.id.event_tv_value);
		tvNoneValue = (TextView) findViewById(R.id.event_detial_tv_novalue);
		tvType = (TextView) findViewById(R.id.event_detial_type);
		tvTitle = (TextView) findViewById(R.id.event_detial_title);
		tvTime = (TextView) findViewById(R.id.event_detial_time);
		tvInfo00 = (TextView) findViewById(R.id.event_detial_0_0);
		tvInfo01 = (TextView) findViewById(R.id.event_detial_0_1);
		tvInfo02 = (TextView) findViewById(R.id.event_detial_0_2);
		tvInfo10 = (TextView) findViewById(R.id.event_detial_1_0);
		tvInfo11 = (TextView) findViewById(R.id.event_detial_1_1);
		tvKeyword = (TextView) findViewById(R.id.event_detial_tv_keyword);
		tvAddr = (TextView) findViewById(R.id.event_detial_addr);
		tvDesc = (TextView) findViewById(R.id.event_detial_desc);
		tvJoinnum = (TextView) findViewById(R.id.event_detial_tv_joinnum);
		tvMaxnum = (TextView) findViewById(R.id.event_detial_tv_maxnum);
		tvCommentCount = (TextView) findViewById(R.id.event_detial_tv_comment_count);
		// 活动交流初始化
		tvEventRemarkTitleOne = (TextView) findViewById(R.id.event_remark_one_title);
		tvEventRemarkTitleTwo = (TextView) findViewById(R.id.event_remark_two_title);
		tvEventRemarkContentOne = (TextView) findViewById(R.id.event_detial_remark_one_content);
		tvEventRemarkContentTwo = (TextView) findViewById(R.id.event_detial_remark_two_content);
		tvEventRemarkTimeOne = (TextView) findViewById(R.id.event_detail_remark_one_ll_time);
		tvEventRemarkTimeTwo = (TextView) findViewById(R.id.event_detail_remark_two_time);
		tvAtmosphereValue = (TextView) findViewById(R.id.event_detial_tv_atmosphere_value);
		tvAtmosphereDesc = (TextView) findViewById(R.id.event_detial_tv_atmosphere_desc);
		tvAttendedValue = (TextView) findViewById(R.id.event_detial_tv_attended_value);
		tvAttendedDesc = (TextView) findViewById(R.id.event_detial_tv_attended_desc);
		tvPriceValue = (TextView) findViewById(R.id.event_detial_tv_price_value);
		tvPriceDesc = (TextView) findViewById(R.id.event_detial_tv_price_desc);
		tvAddressValue = (TextView) findViewById(R.id.event_detial_tv_address_value);
		tvAddressDesc = (TextView) findViewById(R.id.event_detial_tv_address_desc);
		scroll = (ElasticScrollView) findViewById(R.id.scrollview);
		findViewById(R.id.tv_remark_none).setOnClickListener(this);
		tvNoneValue.setOnClickListener(this);
		ivPicture = (ImageView) findViewById(R.id.event_detial_icon);

		gvPhoto = (GridView) findViewById(R.id.event_detial_gv_photo);

		ivPhoto = (ImageView) findViewById(R.id.event_detial_iv_photo);
		gvJoiner = (GridView) findViewById(R.id.event_detial_gv_joiner);

		int version = android.os.Build.VERSION.SDK_INT;
		if (version > 8) {
			gvPhoto.setOverScrollMode(View.OVER_SCROLL_NEVER);
			gvJoiner.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		ivJoiner = (ImageView) findViewById(R.id.event_detial_iv_joiner);

		tvJoin = findViewById(R.id.event_detial_tv_join);// 参加
		tvUnJoin = findViewById(R.id.event_detial_tv_unjoin);// 取消
		tvAccuse = findViewById(R.id.event_detial_tv_accuse);// 举报
		tvSetValue = findViewById(R.id.event_detial_tv_value);// 评分
		tvInvite = findViewById(R.id.event_detial_tv_invite);// 要请
		tvRemark = findViewById(R.id.event_detial_tv_remark);// 评论
		tvInterest = (TextView) findViewById(R.id.event_detial_tv_interest);// 感兴趣
		tvAddr.setOnClickListener(this);
		ivPhoto.setOnClickListener(this);
		ivJoiner.setOnClickListener(this);
		tvJoin.setOnClickListener(this);
		tvUnJoin.setOnClickListener(this);
		tvAccuse.setOnClickListener(this);
		tvSetValue.setOnClickListener(this);
		tvJoin.setOnClickListener(this);
		tvInvite.setOnClickListener(this);
		tvInterest.setOnClickListener(this);
		tvRemark.setOnClickListener(this);
		findViewById(R.id.rl_remark_one).setOnClickListener(this);
		findViewById(R.id.rl_remark).setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);

		gvPhoto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if ((isCreater || isJoiner) && 0 == position) { // 选择照片(不含裁剪)
					Intent intent = new Intent(context,
							EventPhotoAddActivity.class);
					intent.putExtra(Extra.SelectedID, eventId);
					startActivity(intent);
				} else {
					// user 0 代表既不是发起人也不是参与人 1代表是两者之中的一个
					Intent ablumIntent = new Intent(context,
							EventAlbumActivity.class);
					ablumIntent.putExtra(Extra.isAvailable, isCreater
							|| isJoiner ? 1 : 0);
					startActivity(ablumIntent);
				}
			}
		});
		gvJoiner.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppContext.startAct(EventJoinerActivity.class);

			}
		});
	}

	public void CheckRemarkNum() {
		if (EventDetialManager.commentList == null
				|| EventDetialManager.commentList.size() == 0) {
			findViewById(R.id.tv_remark_none).setVisibility(View.VISIBLE);
			findViewById(R.id.rl_remark).setVisibility(View.GONE);
			findViewById(R.id.rl_remark_one).setVisibility(View.GONE);
		} else {
			int listNum = EventDetialManager.commentList.size();
			if (listNum == 1) {
				findViewById(R.id.tv_remark_none).setVisibility(View.GONE);
				findViewById(R.id.rl_remark_one).setVisibility(View.VISIBLE);
				findViewById(R.id.rl_remark).setVisibility(View.GONE);
				TextView tvOneTitle = (TextView) findViewById(R.id.event_remark_one_title_rl_one);
				tvOneTitle.setText(EventDetialManager.commentList.get(0)
						.getUsername());
				TextView tvOneContent = (TextView) findViewById(R.id.event_detial_remark_one_content_rl_one);
				tvOneContent.setText(EventDetialManager.commentList.get(0)
						.getCommentcontent());
				TextView tvOneTime = (TextView) findViewById(R.id.event_detail_remark_one_time_rl_one);
				tvOneTime.setText(CalendarUtils
						.formatYMDHM(EventDetialManager.commentList.get(0)
								.getCreatetime()));

			} else if (listNum >= 2) {
				findViewById(R.id.rl_remark_one).setVisibility(View.GONE);
				findViewById(R.id.tv_remark_none).setVisibility(View.GONE);
				tvEventRemarkTitleOne.setText(EventDetialManager.commentList
						.get(0).getUsername());
				tvEventRemarkContentOne.setText(EventDetialManager.commentList
						.get(0).getCommentcontent());
				tvEventRemarkTimeOne.setText(CalendarUtils
						.formatYMDHM(EventDetialManager.commentList.get(0)
								.getCreatetime()));
				tvEventRemarkTitleTwo.setText(EventDetialManager.commentList
						.get(1).getUsername());
				tvEventRemarkContentTwo.setText(EventDetialManager.commentList
						.get(1).getCommentcontent());
				tvEventRemarkTimeTwo.setText(CalendarUtils
						.formatYMDHM(EventDetialManager.commentList.get(1)
								.getCreatetime()));
				findViewById(R.id.rl_remark).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		CheckRemarkNum();
	}

	/** 从网络请求数据并更新界面 */
	private void setData() {
		EventInfoModel eventInfo = EventDetialManager.eventInfo;
		// 为活动交流添加数据
		tvType.setText("["+eventInfo.getType()+"]");
		tvTitle.setText(eventInfo.getTitle());
		tvTime.setText(CalendarUtils.formatYMDHM(eventInfo.getBegintime())
				+ " ~ " + CalendarUtils.formatYMDHM(eventInfo.getEndtime()));
		if (eventInfo.getMinnum() == eventInfo.getMaxnum()) {
			tvInfo00.setText(eventInfo.getMinnum() + "人");
		} else {
			tvInfo00.setText(eventInfo.getMinnum() + "-"
					+ eventInfo.getMaxnum() + "人");
		}
		int sex = eventInfo.getSex();
		int maxage = eventInfo.getMaxage();
		int minage = eventInfo.getMinage();
		if (sex == 0 && maxage == 0) {
			if (0 == eventInfo.getPredictminconsume()) {
				tvInfo01.setText("免费");
				tvInfo02.setText("");
			} else {
				tvInfo01.setText("人均约" + eventInfo.getPredictminconsume() + "元");
				tvInfo02.setText(eventInfo.getPaytypeDesc());
			}
			tvInfo10.setVisibility(View.GONE);
			tvInfo11.setVisibility(View.GONE);
		} else {
			if (sex == 0) {
				tvInfo01.setVisibility(View.GONE);
			} else {
				String[] sexs = getResources().getStringArray(R.array.sex_info);
				tvInfo01.setText(sexs[sex]);
			}
			if (minage == 0 && maxage == 0) {
				tvInfo02.setText("");
			} else {
				if (minage == 0) {
					tvInfo02.setText("18岁以下");
				} else if (maxage > 45) {
					tvInfo02.setText("45岁以上");
				} else {
					tvInfo02.setText(minage + "-" + maxage + "岁");
				}
			}
			if (0 == eventInfo.getPredictminconsume()) {
				tvInfo10.setText("免费");
				tvInfo11.setText("");
			} else {
				tvInfo10.setText("人均约" + eventInfo.getPredictminconsume() + "元");
				tvInfo11.setText(eventInfo.getPaytypeDesc());
			}
		}

		tvAddr.setText(eventInfo.getAddress());
		if((int)eventInfo.getLatitude() ==0 || (int)eventInfo.getLatitude() == 0){
			tvAddr.setCompoundDrawables(null, null, null, null);
		}
		tvDesc.setText(eventInfo.getActivitydescription());
		tvJoinnum.setText(eventInfo.getJoinnum() + "");
		tvMaxnum.setText(eventInfo.getMaxnum() + "");
		tvCommentCount.setText(eventInfo.getCommentcount() + "");
		scroll.setVisibility(View.VISIBLE);
		// 刚兴趣
		if (eventInfo.isInterest()) {
			tvInterest.setText("感兴趣(" + eventInfo.getInterestcount() + ")");
		}

		// 评分
		// if (eventInfo.getActivitystate() == 1
		// && eventInfo.getActivityprogressstate() == 3
		// && eventInfo.getAtmosphereavgvalue()==0&&
		// eventInfo.getAttendedavgvalue()==0&&eventInfo.getPriceavgvalue()==0&&eventInfo.getAddressavgvalue()==0)
		// {
		// tvNoneValue.setVisibility(View.VISIBLE);
		// // tvValue.setVisibility(View.VISIBLE);
		// } else {
		// System.out.println("执行");
		// tvNoneValue.setVisibility(View.GONE);
		// // tvValue.setVisibility(View.GONE);
		// }
		if (0 != eventInfo.getAtmosphereavgvalue()
				|| 0 != eventInfo.getAttendedavgvalue()
				|| 0 != eventInfo.getPriceavgvalue()
				|| 0 != eventInfo.getAddressavgvalue()) {
			tvValue.setVisibility(View.VISIBLE);
			tvNoneValue.setVisibility(View.GONE);

		} else {
			if (eventInfo.getActivitystate() == 2
					&& eventInfo.getActivityprogressstate() == 3) {
				tvValue.setVisibility(View.VISIBLE);
				tvNoneValue.setVisibility(View.VISIBLE);
			} else {
				tvValue.setVisibility(View.GONE);
				tvNoneValue.setVisibility(View.GONE);
			}
		}
		// if (eventInfo.getActivitystate() != 1
		// || eventInfo.getActivityprogressstate() != 3
		// &&( eventInfo.getAtmosphereavgvalue()==0&&
		// eventInfo.getAttendedavgvalue()==0&&eventInfo.getPriceavgvalue()==0&&eventInfo.getAddressavgvalue()==0))
		// {
		// tvNoneValue.setVisibility(View.GONE);
		// }
		// else
		// if(eventInfo.getActivitystate()!=1||eventInfo.getActivityprogressstate()!=3){
		// tvValue.setVisibility(View.GONE);
		// tvNoneValue.setVisibility(View.VISIBLE);
		// }
		if (0 != eventInfo.getAtmosphereavgvalue()
				|| 0 != eventInfo.getAttendedavgvalue()
				|| 0 != eventInfo.getPriceavgvalue()
				|| 0 != eventInfo.getAddressavgvalue()) {
			// findViewById(R.id.event_detial_tv_novalue).setVisibility(View.GONE);
			findViewById(R.id.event_detial_rl_value)
					.setVisibility(View.VISIBLE);
			tvAtmosphereValue.setText(eventInfo.getAtmosphereavgvalue() + "");
			tvAtmosphereDesc.setText(eventInfo.getAtmosphereDesc());
			tvAttendedValue.setText(eventInfo.getAttendedavgvalue() + "");
			tvAttendedDesc.setText(eventInfo.getAttendedDesc());
			tvPriceValue.setText(eventInfo.getPriceavgvalue() + "");
			tvPriceDesc.setText(eventInfo.getPriceDesc());
			tvAddressValue.setText(eventInfo.getAddressavgvalue() + "");
			tvAddressDesc.setText(eventInfo.getAddressDesc());
		} else {
			// findViewById(R.id.event_detial_tv_novalue).setVisibility(
			// View.VISIBLE);
			findViewById(R.id.event_detial_rl_value).setVisibility(View.GONE);
		}
		// 关键字
		tvKeyword.setText(eventInfo.getKeyword());
		// String[] keywords = eventInfo.getKeyword().split(" ");
		// if (keywords.length > 0) {
		// Random random = new Random();
		// int[] colorArray =
		// AppContext.getRes().getIntArray(R.array.tag_color);
		// for (String key : keywords) {
		// if (!TextUtils.isEmpty(key.trim())) {
		// View item = View.inflate(EventDetialActivity.this,
		// R.layout.item_tag2, null);
		// int color = colorArray[random.nextInt(colorArray.length)];
		// item.findViewById(R.id.itemtag_ll).setBackgroundColor(color);
		// TextView tv = (TextView) item.findViewById(R.id.itemtag_tv_tag);
		// tv.setText(key);
		// flKeyword.addView(item);
		// }
		// }
		// }
		// 评论
		CheckRemarkNum();

		// 海报
		if (!TextUtils.isEmpty(eventInfo.getActivitypicture())) {
			String url = WebApi.getImageUrl(eventInfo.getActivitypicture(),
					WebApi.ImageDemen_240_180);
			pillLoader.loadBitmap(url, ivPicture);
		}
		// 照片
		ArrayList<EventPhotoModel> photoList = EventDetialManager.photoList;
		BaseAdapter photoAdapter = null;
		if (isCreater || isJoiner || 0 < photoList.size()) {
			photoAdapter = new EventPhotoGridAdapter(photoList,
					(isCreater || isJoiner));
			// gvPhoto.setData(photoList, photoAdapter);
			gvPhoto.setAdapter(photoAdapter);
		} else {
			findViewById(R.id.event_detial_ll_photo_bg)
					.setVisibility(View.GONE);
			findViewById(R.id.event_detial_tv_photo_empty).setVisibility(
					View.VISIBLE);
		}
		ArrayList<JoinerModel> joinerList = EventDetialManager.joinerList;
		// gvUsers.setData(joinerList, new JoinerGridAdapter(joinerList));
		gvJoiner.setAdapter(new Joiner4GridAdapter(joinerList));
	}

	@Override
	protected void onStop() {
		super.onStop();
		findViewById(R.id.tv_remark_none).setVisibility(View.GONE);
		findViewById(R.id.rl_remark_one).setVisibility(View.GONE);
		findViewById(R.id.rl_remark).setVisibility(View.GONE);
	}

	protected void setBottomButtonState() {
		EventInfoModel eventInfo = EventDetialManager.eventInfo;
		ArrayList<JoinerModel> joinerList = EventDetialManager.joinerList;
		/* 活动状态为1:未达成or4:已关闭 时,隐藏底部按键 */
		if (null != eventInfo
				&& (1 == eventInfo.getActivitystate() || 4 == eventInfo
						.getActivitystate())) {
			return;
		}
		/* 活动状态或活动进度为空时,隐藏底部. */
		if (0 == eventInfo.getActivityprogressstate()
				|| 0 == eventInfo.getActivitystate()) {
			return;
		}

		int userID = 0;
		if (MyinfoManager.getUserInfo() != null) {
			userID = MyinfoManager.getUserInfo().getUserID();
		}

		isCreater = userID == eventInfo.getUserid();// 判断是否是创建者,参与者
		// isJoiner = isCreater;
		if (!isCreater && null != joinerList) {
			for (JoinerModel item : joinerList) {
				if (userID == item.getUserid()) {
					isJoiner = true;
					break;
				}
			}
		}

		switch (eventInfo.getActivityprogressstate()) {
		case 1:// 即将开始
		case 2:// 进行中 //隐藏邀请,评分
			tvInvite.setVisibility(View.GONE);
			tvSetValue.setVisibility(View.GONE);

			tvJoin.setVisibility(View.GONE);
			tvUnJoin.setVisibility(View.GONE);
			tvAccuse.setVisibility(View.GONE);

			break;
		case 3:// 已结束// 显示评分 投诉,隐藏邀请
			if (isJoiner) {
				tvAccuse.setVisibility(View.VISIBLE);
			}
			tvSetValue.setVisibility(View.VISIBLE);
			tvInvite.setVisibility(View.GONE);
			break;
		case 4:// 报名中
				// 显示邀请,隐藏评分
			if (isJoiner) {
				tvUnJoin.setVisibility(View.VISIBLE);
				tvJoin.setVisibility(View.GONE);
			} else if (!isCreater) {
				tvJoin.setVisibility(View.VISIBLE);
				tvUnJoin.setVisibility(View.GONE);
			}
			tvInvite.setVisibility(View.VISIBLE);
			tvAccuse.setVisibility(View.GONE);
			tvSetValue.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	public void initFriendData() {
		// 请求我的好友数据
		searchModel.setFriendtype(1);
		searchModel.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.request(searchModel, WebApi.User.GetFridnds, new ResponseHandler(
				true) {
			@Override
			public void onSuccess(String content) {
				ArrayList<UserListItemModel> list = JSON.getObject(content,
						UserListRModel.class).getList();
				System.out.println(list);
				if (list.size()==0) {
					MessageDialog dialog = new MessageDialog(context);
					dialog.setTitle("操作提示");
					dialog.setMessage("您还没有好友哦，现在去加好友？");
					dialog.setLeftButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							AppContext.startAct(UserSearchInitialActivity.class);
						}
					});
					dialog.setRightButton("取消", null);
					dialog.show();
				}else {
					Intent intentInvite = new Intent(context,InviteFriendActivity.class);
					intentInvite.putExtra(Extra.SelectedID, eventId);
					startActivity(intentInvite);
				}

			}

			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				super.onFailure(e, statusCode, content);
				if (statusCode==400) {
					MessageDialog dialog = new MessageDialog(context);
					dialog.setTitle("操作提示");
					dialog.setMessage("您还没有好友哦，现在去加好友？");
					dialog.setLeftButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							AppContext.startAct(UserSearchInitialActivity.class);
						}
					});
					dialog.setRightButton("取消", null);
					dialog.show();
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		EventInfoModel eventInfo = EventDetialManager.eventInfo;
		switch (v.getId()) {
		case R.id.event_detial_tv_novalue:
			if (isCreater || isJoiner) {
				AppContext.startAct(EventValuationActivity.class);
			}
			break;
		case R.id.tv_remark_none:
			AppContext.startAct(EventRemarkActivity.class);
			break;
		case R.id.event_detial_addr:
			if((int)eventInfo.getLatitude() == 0 || (int)eventInfo.getLongitude() == 0){
				break;
			}
			Intent intent = new Intent(this, EventAddrMapActivity.class);
			intent.putExtra(Const.Extra.Snippet, tvAddr.getText());
			intent.putExtra(Const.Extra.LatitudeE6, eventInfo.getLatitude());
			intent.putExtra(Const.Extra.LongitudeE6, eventInfo.getLongitude());
			startActivity(intent);
			break;
		case R.id.page_btn_back:
			finish();
			break;
		// case R.id.event_detial_remark_one:
		// AppContext.startAct(EventRemarkActivity.class);
		// break;
		case R.id.event_detial_iv_photo:
			AppContext.startAct(EventAlbumActivity.class);
			break;
		case R.id.event_detial_iv_joiner:
			// 参与人员列表
			AppContext.startAct(EventJoinerActivity.class);
			break;
		case R.id.event_detial_tv_join:// 报名
			// 判断是否需要完善资料
			if (!MyinfoManager.getUserInfo().isMobileV()) {
				Intent joinIntent = new Intent(context,
						VerifyNameAndPhoneActivity.class);
				joinIntent
						.putExtra(Extra.VerifyState, State.Verify_EventJoiner);
				joinIntent.putExtra(Extra.SelectedID, eventId);
				startActivity(joinIntent);
			} else {
				EventEngine.join(eventId, new CallBack<String>() {
					@Override
					public void onCallBack() {
						tvJoin.setVisibility(View.GONE);
						tvUnJoin.setVisibility(View.VISIBLE);
					}
				});
			}

			break;
		case R.id.event_detial_tv_unjoin:// 取消报名
			Intent cancelIntent = new Intent(context, EventCancelActivity.class);
			cancelIntent.putExtra(Extra.SelectedID, eventId);
			startActivity(cancelIntent);
			break;
		case R.id.event_detial_tv_accuse:// 举报发起人
			AppContext.startAct(EventAccuseActivity.class);
			break;
		// case R.id.event_detial_tv_upphoto://上传照片
		// AppContext.startAct(EventPhotoAddActivity.class);
		// break;
		case R.id.event_detial_tv_value:// 评分
			EventInfoModel info = EventDetialManager.eventInfo;
			if (0 != info.getActivityatmospherevalue()
					|| 0 != info.getActivityattendedvalue()
					|| 0 != info.getActivitypricevalue()
					|| 0 != info.getActivityaddressvalue()) {
				AppContext.startAct(EventUserValuActivity.class);
			} else {
				AppContext.startAct(EventValuationActivity.class);
			}
			break;
		case R.id.event_detial_tv_invite:// 邀请
			initFriendData();
			System.out.println(hasFriend);
			if (hasFriend) {
				
			}else {
//				MessageDialog dialog = new MessageDialog(this);
//				dialog.setTitle("操作提示");
//				dialog.setMessage("您还没有好友哦，现在去加好友？");
//				dialog.setLeftButton("确定", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						AppContext.startAct(UserSearchInitialActivity.class);
//					}
//				});
//				dialog.setRightButton("取消", null);
//				dialog.show();
			}
			break;
		case R.id.rl_remark_one:
		case R.id.rl_remark:
			System.out.println("**rl_remark**");
			AppContext.startAct(EventRemarkActivity.class);
			break;
		case R.id.event_detial_tv_remark:// 评论
			AppContext.startAct(EventRemarkActivity.class);
			break;
		case R.id.event_detial_tv_interest:// 感兴趣
			if (EventDetialManager.eventInfo.isInterest()) {
				Toast.show(R.string.warn_event_interested);
			} else {
				Net.request(KEY.EventID, eventId + "", WebApi.Event.Interest,
						new ResponseHandler() {
							@Override
							public void onSuccess(String content) {
								EventInfoModel info = EventDetialManager.eventInfo;
								info.setInterestcount(info.getInterestcount() + 1);
								tvInterest.setText("感兴趣("
										+ info.getInterestcount() + ")");
								NetCache.clearCaches();
							}
						});
			}
			break;
		default:
			break;
		}
	}
}
