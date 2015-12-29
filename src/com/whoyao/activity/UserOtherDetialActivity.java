package com.whoyao.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.State;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.adapter.UserOtherPhotoGridAdapter;
import com.whoyao.common.FaceImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.FreeModel;
import com.whoyao.model.FriendRequestRModel;
import com.whoyao.model.RelationManageRModel;
import com.whoyao.model.RelationManageTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.model.TagModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.topic.TopicUserListActivity;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.AddressUtil;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;
import com.whoyao.widget.ElasticScrollView;
import com.whoyao.widget.HorizontalListView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Ta的资料
 * 
 * @author hyh Creat_at：2013-11-07-上午10:00:54
 */
public class UserOtherDetialActivity extends BasicActivity implements
		OnClickListener, OnItemClickListener {
	private static final int ID_AGREE = 0;// 同意
	@SuppressWarnings("unused")
	private static final int ID_DISAGREE = 1;// 拒绝
	private static final int ID_IGNORE = 2;// 忽略

	private static final int ID_ADDFRIEND = 3;// 加好友
	private static final int ID_DELFRIEND = 4;// 删好友

	private static final int ID_FAVORITE = 5;// 收藏
	private static final int ID_UNFAVORITE = 6;// 取消收藏

	private static final int ID_BLACK = 7;// 加黑名单
	private static final int ID_UNBLACK = 8;// 移出黑名单

	private static final int ID_LETTER = 9;// 发私信
	private static final int ID_INVERT = 10;// 发邀请
	private static final int RELATION_NONE = 0;//
	private static final int RELATION_FRIDEND = 1;//
	private static final int RELATION_FAVORITE = 2;//
	private static final int RELATION_BLACK = 3;//
	private static final int RELATION_REQUEST = 4;//
	private static final int RELATION_IGNORE = 5;//
	private RelationManageRModel relation;

	private TextView tvContent;
	private TextView tvNick;
	private ImageView ivFace;
	private TextView tvCity;
	private TextView consTv;
	private TextView heightTv;
	private TextView waistTv;
	private TextView tvBlood;
	private TextView ageTv;
	private TextView tvEvent;
	private TextView tvTopic;
	private ImageView mobileRbn;
	private ImageView honestyeRbn;
	private TextView tvFree;
	// private GridView tagGv;
	private LinearLayout tagLl;
	private ImageAsyncLoader loader;
	// private UserInfoModel info;
	private Random random;
	private int[] colorArray;
	private int width;
	private int lineNum;
	private static int[] paddings = { 15, 10, 20, 15, 20, 10, 25 };
	private int friendUserID;
	private int msgID;
	private UserDetialModel detialModel;
	private TextView tvTitle;
	private LinearLayout vFreeList;
	private LinearLayout vFree;
	private LinearLayout vBottom;
	private LinearLayout.LayoutParams bottomParams;
	private int dip10;
	private int position;
	private HorizontalListView hlPhoto;
	private ElasticScrollView sv;
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		friendUserID = intent.getIntExtra(Extra.SelectedID, 0);
		if (0 == friendUserID) {
			finish();
			return;
		}
		msgID = intent.getIntExtra(Extra.SelectedItem, 0);
		position = intent.getIntExtra(Extra.Position, -1);
		loader = FaceImageAsyncLoader.getInstance();
		random = new Random();
		colorArray = AppContext.getRes().getIntArray(R.array.tag_color);
		width = ClientEngine.getMobileInfo().getVgaWidth()
				- Utils.dip2px(this, 20);
		bottomParams = new LinearLayout.LayoutParams(0, (int) getResources()
				.getDimension(R.dimen.item_height), 1);
		dip10 = (int) getResources().getDimension(R.dimen.dip10);

		setContentView(R.layout.activity_user_other_detial);
		initView();
		requestData();// 异步加载:头像,背景,照片,闲人预告,标签
	}

	private void initView() {
		sv = (ElasticScrollView) findViewById(R.id.user_other_scrollview);
		// 顶部内容
		tvTitle = (TextView) findViewById(R.id.page_tv_title);
		tvNick = (TextView) findViewById(R.id.user_other_nickname);
		ivFace = (ImageView) findViewById(R.id.user_other_iv_face);
		ivBack = (ImageView) findViewById(R.id.user_other_iv_background);
		sv.setElasticView(ivBack);
		tvCity = (TextView) findViewById(R.id.user_other_city);
		consTv = (TextView) findViewById(R.id.user_other_constellation);
		ageTv = (TextView) findViewById(R.id.user_other_age);
		heightTv = (TextView) findViewById(R.id.user_other_height);
		waistTv = (TextView) findViewById(R.id.user_other_waist);
		tvBlood = (TextView) findViewById(R.id.user_other_blood);
		tvEvent = (TextView) findViewById(R.id.user_other_event_count);
		tvTopic = (TextView) findViewById(R.id.user_other_topic_count);
		mobileRbn = (ImageView) findViewById(R.id.user_other_mobile_state);
		honestyeRbn = (ImageView) findViewById(R.id.user_other_honestye_state);
		vBottom = (LinearLayout) findViewById(R.id.user_other_ll_bottom);
		// 中部内容
		tvFree = (TextView) findViewById(R.id.user_other_tv_free);
		vFree = (LinearLayout) findViewById(R.id.user_other_ll_free);
		vFreeList = (LinearLayout) findViewById(R.id.user_other_ll_freelist);
		hlPhoto = (HorizontalListView) findViewById(R.id.user_other_hl_photo);
		
		// tagGv = (GridView) findViewById(R.id.user_other_gv_tag);
		tagLl = (LinearLayout) findViewById(R.id.user_other_ll_tags);
		tvContent = (TextView) findViewById(R.id.user_other_set_content);

		hlPhoto.setOnItemClickListener(this);

		tvEvent.setOnClickListener(this);
		tvTopic.setOnClickListener(this);
		ivFace.setOnClickListener(this);
		findViewById(R.id.page_btn_back).setOnClickListener(this);

		findViewById(R.id.user_other_ll_envet).setOnClickListener(this);
		findViewById(R.id.user_other_ll_topic).setOnClickListener(this);
		findViewById(R.id.user_other_ll_content).setOnClickListener(this);
		/* findViewById(R.id.user_other_set_tag).setOnClickListener(this); */
		findViewById(R.id.user_other_iv_photo).setOnClickListener(this);

	}

	/**
	 * 异步加载部分信息 头像图片,标签,闲人预告,照片
	 */
	private void requestData() {
		Net.request(KEY.FriendID, friendUserID + "", WebApi.User.Relation,
				new ResponseHandler(true) {
					@Override
					public void onSuccess(String content) {
						relation = JSON.getObject(content,
								RelationManageRModel.class);
						setRelationState();
					}

					@Override
					public void onFailure(Throwable e, int statusCode,
							String content) {
						setRelationState();
						super.onFailure(e, statusCode, content);
					}
				});
		RequestParams params = new RequestParams();
		params.put(KEY.User_ID, friendUserID + "");
		params.put(KEY.Type, Type.Detial_other);
		Net.cacheRequest(params, WebApi.User.DetailInfo, new ResponseHandler(
				true) {
			@Override
			public void onSuccess(String content) {
				detialModel = JSON.getObject(content, UserDetialModel.class);
				setDetialData();
			}
		});
	}

	private void setDetialData() {
		UserInfoModel userinfo = detialModel.getUserList();
		tvNick.setText(userinfo.getUserName());
		tvTitle.setText(userinfo.getUserName());
		// setBithdayData();
		if (!"0001-01-01 00:00:00".equals(userinfo.getUserBirthday())) {
			consTv.setText(userinfo.getUserConstellation()); // 星座
			ageTv.setText(userinfo.getUserAge()); // ageTv年龄
		}
		// setSexData();
		int[] colors = { R.drawable.rectangle_stroke_gray,
				R.drawable.rectangle_stroke_blue,
				R.drawable.rectangle_stroke_carmine };
		ivFace.setBackgroundResource(colors[userinfo.getUserSex()]);
		loader.loadBitmap(userinfo.getUserFace(), ivFace);
		// setHeightData();
		if (0 != userinfo.getUserHeight()) {
			heightTv.setText(userinfo.getUserHeight() + "cm");
		} else {
			heightTv.setVisibility(View.GONE);
		}
		// setWaistData();
		if (0 != userinfo.getUserWaist()) {
			waistTv.setText(userinfo.getUserWaist() + "kg");
		} else {
			waistTv.setVisibility(View.GONE);
		}
		tvBlood.setText(userinfo.getBloodStr());// 血型
		tvEvent.setText(userinfo.getActivityCount() + "");// 活动数
		tvTopic.setText(userinfo.getTopicCount() + "");// 话题数
		mobileRbn.setSelected(userinfo.isMobileV());// 认证
		honestyeRbn.setSelected(userinfo.isHonestyV());// 认证
		tvContent.setText(userinfo.getUserContent());// 签名

		// 城市setAddrData();
		String cityName = AddressUtil.getCityName(userinfo.getUserCity());
		tvCity.setText(cityName);
		// 闲人预告
		ArrayList<FreeModel> freeList = detialModel.getUserMyFree();
		if (null == freeList || freeList.isEmpty()) {
			vFree.setVisibility(View.GONE);
			vFreeList.setVisibility(View.GONE);
		} else {
			FreeModel freeModel = freeList.get(0);
			String freeTime = CalendarUtils.formatYMDHM(freeModel
					.getFreeTimeStart())
					+ " ~ "
					+ CalendarUtils.formatYMDHM(freeModel.getFreeTimeEnd());
			tvFree.setText(freeTime);
			findViewById(R.id.user_other_ll_free).setOnClickListener(this);
			random = new Random();
			for (FreeModel item : freeList) {
				View itemView = View.inflate(this, R.layout.item_free, null);
				TextView tvTime = (TextView) itemView
						.findViewById(R.id.itemfree_tv_time);
				TextView tvFreeContent = (TextView) itemView
						.findViewById(R.id.itemfree_tv_content);
				View vColor = itemView.findViewById(R.id.itemfree_color);
				String timeStr = CalendarUtils.formatYMDHM(item
						.getFreeTimeStart())
						+ " ~ "
						+ CalendarUtils.formatYMDHM(item.getFreeTimeEnd());
				tvTime.setText(timeStr);
				tvFreeContent.setText(item.getFreeTimeContent());
				vColor.setBackgroundColor(colorArray[random
						.nextInt(colorArray.length)]);
				vFreeList.addView(itemView);
			}

		}
		// 照片
		// photoHlv.setAdapter(new PhotoListAdapter(MyinfoManager.userPhoto));
		if (null == detialModel.getUserPhoto()
				|| detialModel.getUserPhoto().isEmpty()) {
			findViewById(R.id.user_other_ll_photo).setVisibility(View.GONE);
		} else {
			hlPhoto.setAdapter(new UserOtherPhotoGridAdapter(detialModel.getUserPhoto()));
		}
		// 标签
		tagLl.removeAllViews();
		initTag(tagLl);

	}

	/**
	 * 设置生日相关显示 生日,星座,年龄
	 */

	/**
	 * 设置性别相关显示 性别文字,头像边框
	 */

	/**
	 * 设置城市相关显示 城市,所在地
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		Intent photoViewIntent = new Intent(context,
				UserPhotoActivity.class);
		photoViewIntent.putExtra(Extra.Photos,
				detialModel.getUserPhoto());
		photoViewIntent.putExtra(Extra.SelectedItem, position);
		startActivity(photoViewIntent);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.user_other_ll_free:/* 没有闲人预告时,点击发,有时,点击查看列表 */
			if (View.GONE == vFreeList.getVisibility()) {
				tvFree.setSelected(true);
				vFree.setBackgroundResource(R.drawable.selector_radius_top);
				vFreeList.setVisibility(View.VISIBLE);
			} else {
				tvFree.setSelected(false);
				vFree.setBackgroundResource(R.drawable.selector_radius_white_gary);
				vFreeList.setVisibility(View.GONE);
			}
			break;
		case R.id.user_other_ll_envet:// 查看活动
			Intent intent = new Intent(this, EventOther2ListActivity.class);
			intent.putExtra(Extra.SelectedID, friendUserID);
			startActivity(intent);
			break;
		case R.id.user_other_ll_topic:// 查看话题
			Intent topicIntent = new Intent(context,TopicUserListActivity.class);
			topicIntent.putExtra(Extra.SelectedID, friendUserID);
			startActivity(topicIntent);
			break;
		case R.id.user_other_iv_photo:// 查看照片
			if (null != detialModel && null != detialModel.getUserPhoto()
					&& !detialModel.getUserPhoto().isEmpty()) {
				Intent photoViewIntent = new Intent(AppContext.context,
						UserPhotoActivity.class);
				photoViewIntent.putExtra(Extra.Photos,
						detialModel.getUserPhoto());
				photoViewIntent.putExtra(Extra.SelectedItem, 0);
				startActivity(photoViewIntent);
			}
			break;
		case ID_AGREE:
			FriendRequestRModel agreeModel = new FriendRequestRModel(
					friendUserID, 1, msgID);
			Net.request(agreeModel, WebApi.User.FriendRequestManage,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {
							getRelation().setFriend(true);
							setRelationState();
							Intent agreeIntent = new Intent();
							agreeIntent.putExtra(Extra.Position, position);
							agreeIntent.putExtra(Extra.State, State.Agree);
							setResult(RESULT_OK, agreeIntent);
							Toast.show("添加好友成功，现在可以给TA发私信了");
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
							if (417 == statusCode) {
								setBottomBtn(RELATION_FRIDEND);
								Intent agreeIntent = new Intent();
								agreeIntent.putExtra(Extra.Position, position);
								agreeIntent.putExtra(Extra.State, State.Agree);
								setResult(RESULT_OK, agreeIntent);
							}
						}
					});
			break;
		case ID_IGNORE:
			FriendRequestRModel ignoreModel = new FriendRequestRModel(
					friendUserID, 0, msgID);
			Net.request(ignoreModel, WebApi.User.FriendRequestManage,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {
							setBottomBtn(RELATION_IGNORE);
							Intent ignoreIntent = new Intent();
							ignoreIntent.putExtra(Extra.Position, position);
							ignoreIntent.putExtra(Extra.State, State.Ignore);
							setResult(RESULT_OK, ignoreIntent);
						}
					});
			break;
		case ID_ADDFRIEND:
			Intent addFriendIntent = new Intent(this,
					UserAddFriendActivity.class);
			addFriendIntent.putExtra(Extra.SelectedID, friendUserID);
			startActivity(addFriendIntent);
			break;
		case ID_DELFRIEND:
			final RelationManageTModel delFriendModel = new RelationManageTModel(
					friendUserID, Type.User_Del);
			MessageDialog deleteDialog = new MessageDialog(this);
			deleteDialog.setTitle("操作提示");
			deleteDialog.setMessage("确定要删除" +  tvTitle.getText() + "？");
			deleteDialog.setLeftButton("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Net.request(delFriendModel, WebApi.User.FriendManage,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									ResultModel result = JSON.getObject(
											content, ResultModel.class);
									if (result != null && result.isSuccess()) {
										getRelation().setFriend(false);
										setRelationState();
										Toast.show("删除成功");
									} else {
										Toast.show("请求失败");
									}
								}
							});
				}
			});
			deleteDialog.setRightButton("取消", null);
			deleteDialog.show();

			break;
		case ID_FAVORITE:
			RelationManageTModel addFavorityModel = new RelationManageTModel(
					friendUserID, Type.User_Add);
			Net.request(addFavorityModel, WebApi.User.FaviconsManage,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {
							ResultModel result = JSON.getObject(content,
									ResultModel.class);
							if (result != null && result.isSuccess()) {
								getRelation().setFavicons(true);
								setRelationState();
								Toast.show("收藏成功");
							} else {
								Toast.show("请求失败");
							}
						}
					});
			break;
		case ID_UNFAVORITE:
			RelationManageTModel unFavorityModel = new RelationManageTModel(
					friendUserID, Type.User_Del);
			Net.request(unFavorityModel, WebApi.User.FaviconsManage,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(String content) {
							ResultModel result = JSON.getObject(content,
									ResultModel.class);
							if (result != null && result.isSuccess()) {
								getRelation().setFavicons(false);
								setRelationState();
								Toast.show("已取消收藏");
							} else {
								Toast.show("请求失败");
							}
						}
					});
			break;
		case ID_BLACK:
			final RelationManageTModel addBlackModel = new RelationManageTModel(
					friendUserID, Type.User_Add);
			MessageDialog blackDialog = new MessageDialog(this);
			blackDialog.setTitle("操作提示");
			blackDialog.setMessage("确定要将" +  tvTitle.getText() + "加入黑名单？");
			blackDialog.setLeftButton("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Net.request(addBlackModel, WebApi.User.BlackManage,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									ResultModel result = JSON.getObject(
											content, ResultModel.class);
									if (result != null && result.isSuccess()) {
										getRelation().setBlacklist(true);
										setRelationState();
										Toast.show("操作成功");
									} else {
										Toast.show("请求失败");
									}
								}
							});
				}
			});
			blackDialog.setRightButton("取消", null);
			blackDialog.show();
			break;
		case ID_UNBLACK:
			final RelationManageTModel unBlackModel = new RelationManageTModel(
					friendUserID, Type.User_Del);
			MessageDialog unBlackDialog = new MessageDialog(this);
			unBlackDialog.setTitle("操作提示");
			unBlackDialog.setMessage("确定要将" + tvTitle.getText() + "移出黑名单？");
			unBlackDialog.setLeftButton("确定", new OnClickListener() {

				@Override
				public void onClick(View v) {
					Net.request(unBlackModel, WebApi.User.BlackManage,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									ResultModel result = JSON.getObject(
											content, ResultModel.class);
									if (result != null && result.isSuccess()) {
										getRelation().setBlacklist(false);
										setRelationState();
										Toast.show("已从黑名单中删除");
									} else {
										Toast.show("请求失败");
									}
								}
							});
				}
			});
			unBlackDialog.setRightButton("取消", null);
			unBlackDialog.show();

			break;
		case ID_INVERT:
			UserEngine.sendInvert(friendUserID, detialModel.getUserList()
					.getUserName());
			break;
		case ID_LETTER:
			Intent letterIntent = new Intent(this, PrivateDetailActivity.class);
			letterIntent.putExtra(Extra.SelectedID, friendUserID);
			letterIntent.putExtra(Extra.SelectedItemStr, detialModel
					.getUserList().getUserName());
			startActivity(letterIntent);
			break;

		default:
			break;
		}

	}

	private void initTag(LinearLayout parent) {
		LinearLayout line = getLine();
		int lineWidth = 0;
		parent.addView(line);
		View title = getTagItem("标签", Color.TRANSPARENT);
		TextView tv = (TextView) title.findViewById(R.id.itemtag_tv_tag_tag);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tv.setGravity(Gravity.LEFT);
		line.addView(title);
		if (detialModel.getUserTag() == null
				|| detialModel.getUserTag().isEmpty()) {
			TextView tvTag = (TextView) findViewById(R.id.other_set_tag);
			tvTag.setHint("TA还没有添加标签");
			return;
		}
		lineWidth += title.getMeasuredWidth();
		ArrayList<String> tagList = new ArrayList<String>();
		for (TagModel tag : detialModel.getUserTag()) {
			tagList.add(tag.getTagName());
		}

		// int divide = getDivide(tagList);

		for (int i = 0; i < tagList.size(); i++) {
			String text = tagList.get(i);
			int drawable[] = { R.drawable.rectangle_radius_tag_red,
					R.drawable.rectangle_radius_tag_blue,
					R.drawable.rectangle_radius_tag_green };
			View view = getItem(text, drawable[i % 3]);
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

	private int getDivide(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			if (4 < list.get(i).length()) {
				return i;
			}
		}
		return -1;
	}

	/* 生成底部按键 */
	private TextView addBottomBtn(int id, String text) {

		TextView tv = new TextView(context);
		tv.setLayoutParams(bottomParams);
		tv.setBackgroundResource(R.drawable.selector_button_tran_red);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, dip10, 0, dip10);
		tv.setTextColor(Color.WHITE);
		tv.setId(id);
		tv.setText(text);
		vBottom.addView(tv);
		return tv;
	}

	private RelationManageRModel getRelation() {
		if (null == relation) {
			relation = new RelationManageRModel();
		}
		return relation;
	}

	private void setRelationState() {
		if (0 != msgID) {
			if (null != relation && relation.isFriend()) {
				setBottomBtn(RELATION_FRIDEND);
				Intent agreeIntent = new Intent();
				agreeIntent.putExtra(Extra.Position, position);
				agreeIntent.putExtra(Extra.State, State.Agree);
				setResult(RESULT_OK, agreeIntent);
				return;
			} else {
				setBottomBtn(RELATION_REQUEST);
				return;
			}
		}
		if (null == relation) {
			setBottomBtn(RELATION_NONE);
			return;
		}
		if (relation.isFriend()) {
			setBottomBtn(RELATION_FRIDEND);
			return;
		}
		if (relation.isBlacklist()) {
			setBottomBtn(RELATION_BLACK);
			return;
		}
		if (relation.isFavicons()) {
			setBottomBtn(RELATION_FAVORITE);
			return;
		}
		setBottomBtn(RELATION_NONE);
	}

	private void setBottomBtn(int relation) {
		vBottom.removeAllViews();
		switch (relation) {
		case RELATION_NONE:// 没有关系
			addBottomBtn(ID_ADDFRIEND, "加好友").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_INVERT, "发邀请").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_FAVORITE, "收藏TA").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_BLACK, "加黑名单").setOnClickListener(
					UserOtherDetialActivity.this);
			break;
		case RELATION_FRIDEND:// 是好友
			addBottomBtn(ID_LETTER, "发私信").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_INVERT, "发邀请").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_DELFRIEND, "删除好友").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_BLACK, "加黑名单").setOnClickListener(
					UserOtherDetialActivity.this);
			break;
		case RELATION_FAVORITE:// 已收藏
			addBottomBtn(ID_ADDFRIEND, "加好友").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_INVERT, "发邀请").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_UNFAVORITE, "取消收藏").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_BLACK, "加黑名单").setOnClickListener(
					UserOtherDetialActivity.this);
			break;
		case RELATION_BLACK:// 黑名单
			addBottomBtn(ID_ADDFRIEND, "加好友").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_UNBLACK, "移出黑名单").setOnClickListener(
					UserOtherDetialActivity.this);
			break;
		case RELATION_REQUEST:// 好友请求
			addBottomBtn(ID_AGREE, "同意加好友").setOnClickListener(
					UserOtherDetialActivity.this);
			addBottomBtn(ID_IGNORE, "忽略该申请").setOnClickListener(
					UserOtherDetialActivity.this);
			break;
		case RELATION_IGNORE:// 已忽略
			addBottomBtn(ID_IGNORE, "已忽略");
			break;
		default:
			break;
		}
	}

}
