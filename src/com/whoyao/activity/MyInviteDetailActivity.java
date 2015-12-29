package com.whoyao.activity;

import org.apache.http.Header;

import com.whoyao.Const.Extra;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.InviteDetailListItem;
import com.whoyao.model.InviteOperateTModel;
import com.whoyao.model.InviteSingleTModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.L;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyInviteDetailActivity extends BasicActivity implements
		OnClickListener {
	private TextView tvTitle;
	private TextView tvOperator;
	private TextView tvName;
	private TextView tvAttendancerate;
	private TextView tvAttestationtel;
	private TextView tvTime;
	private TextView payType;
	private TextView tvAddr;
	private TextView tvDescription;
	private LinearLayout llOperate;
	private TextView tvAgree;
	private TextView tvDisagee;
	private TextView tvOperateResult;
	private InviteDetailListItem item;
	private int type;
	private ImageAsyncLoader loader;
	private ImageView ivHead;
	private InviteOperateTModel model;
	private int position;
	private Intent intent;
	private Intent intentResult;
	private InviteSingleTModel singleModel;
	private View viewLine;
	private int inviteid;
	private int userId;
	private int invitetype = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_invite_detail);
		model = new InviteOperateTModel();
		singleModel = new InviteSingleTModel();
		Intent intentSend = getIntent();
		inviteid = intentSend.getIntExtra("inviteid", -1);
		invitetype = intentSend.getIntExtra("invitetype", 0);
		Intent testIntent = new Intent();
		intentResult = new Intent();
		L.i(Const.AppName, inviteid + "");
		if (inviteid != -1) {
			singleModel.setInviteid(inviteid);
			Net.request(singleModel, WebApi.Invite.SingleInvite,
					new ResponseHandler(true) {

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							item = JSON.getObject(content,
									InviteDetailListItem.class);
							System.out.println("item******" + item);
							type = 0;
							initView();
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
						}

					});
		} else {
			intent = getIntent();
			item = (InviteDetailListItem) intent
					.getSerializableExtra("myInvite");
			type = intent.getIntExtra("type", 0);
			position = intent.getIntExtra("position", 0);
			intentResult.putExtra("position", position);
			intentResult.putExtra("type", type);
			initView();
		}

	}

	public void initView() {
		if (item == null) {
			finish();
			return;
		}
		loader = SmallImageAsyncLoader.getInstance();
		viewLine = findViewById(R.id.line_gray);
		ivHead = (ImageView) findViewById(R.id.invite_detail_picture);
		ivHead.setOnClickListener(this);
		tvName = (TextView) findViewById(R.id.invite_detail_name);
		findViewById(R.id.invite_detail_btn_back).setOnClickListener(this);
		tvAttendancerate = (TextView) findViewById(R.id.invite_detail_attendancerate);
		tvAttestationtel = (TextView) findViewById(R.id.invite_detail_attestationtel);
		String pictureID = null;
		tvOperator = (TextView) findViewById(R.id.tv_invite_detail_send_person);
		if (type == 1) {
			tvOperator.setText("发起人");
		} else {
			tvOperator.setText("受邀人");
		}
		if (type == 1 && inviteid == -1) {
			pictureID = item.getSenduserheadimage();
			tvName.setText(item.getSendusername());
			tvAttendancerate.setText("出勤率" + item.getAttendancerate());
			if (item.getAttestationtel() == 0) {
				tvAttestationtel.setText("未填写手机认证");
			} else if (item.getAttestationtel() == 1) {
				tvAttestationtel.setText("还未通过手机验证");
			} else {
				tvAttestationtel.setText("已通过手机认证");
			}
			userId = item.getSenduserid();
		} else if (type == 0 && inviteid != -1 && invitetype == 0) {
			pictureID = item.getSenduserheadimage();
			tvName.setText(item.getSendusername());
			System.out.println("**recevie**" + item.getReciveUsreName());
			tvAttendancerate.setText("出勤率" + item.getAttendancerate());
			if (item.getAttestationtel() == 0) {
				tvAttestationtel.setText("未填写手机认证");
			} else if (item.getAttestationtel() == 1) {
				tvAttestationtel.setText("还未通过手机验证");
			} else {
				tvAttestationtel.setText("已通过手机认证");
			}
			userId = item.getSenduserid();
			tvOperator.setText("发起人");
			type=1;
		} else if (type == 0 && inviteid != -1 && invitetype == 1) {
			pictureID = item.getReciveHeadPic();
			tvName.setText(item.getReciveUsreName());
			System.out.println("**recevie**" + item.getReciveUsreName());
			tvAttendancerate.setText("出勤率" + item.getReciveAttendancerate());
			if (item.getReciveAttestationtel() == 0) {
				tvAttestationtel.setText("未填写手机认证");
			} else if (item.getReciveAttestationtel() == 1) {
				tvAttestationtel.setText("还未通过手机验证");
			} else {
				tvAttestationtel.setText("已通过手机认证");
			}
			userId = item.getSenduserid();
		} else if (type == 0 && inviteid == -1) {
			pictureID = item.getReciveHeadPic();
			tvName.setText(item.getReciveUsreName());
			System.out.println("**recevie**" + item.getReciveUsreName());
			tvAttendancerate.setText("出勤率" + item.getReciveAttendancerate());
			if (item.getReciveAttestationtel() == 0) {
				tvAttestationtel.setText("未填写手机认证");
			} else if (item.getReciveAttestationtel() == 1) {
				tvAttestationtel.setText("还未通过手机验证");
			} else {
				tvAttestationtel.setText("已通过手机认证");
			}
			userId = item.getUserid();
		}
		if (!TextUtils.isEmpty(pictureID)) {
			loader.loadBitmap(pictureID, ivHead);
		}
		tvTitle = (TextView) findViewById(R.id.event_detial_title);
		tvTitle.setText(item.getTitle());

		tvTime = (TextView) findViewById(R.id.invite_detail_time);
		tvTime.setText(CalendarUtils.formatYMDHM(item.getTime()));
		payType = (TextView) findViewById(R.id.invite_detail_paytype);
		if (item.getPaytype() == 1) {
			payType.setText("人均约" + item.getPredictconsume() + "元"
					+ "          " + " AA制");
		} else if (item.getPaytype() == 2) {
			payType.setText("人均约" + item.getPredictconsume() + "元"
					+ "          " + "男生付款");
		} else if (item.getPaytype() == 3) {
			payType.setText("人均约" + item.getPredictconsume() + "元"
					+ "          " + "女生付款");
		} else if (item.getPaytype() == 4) {
			payType.setText("人均约" + item.getPredictconsume() + "元"
					+ "          " + "输家付费");
		}
		if (item.getPredictconsume() == 0) {
			payType.setVisibility(View.GONE);
			viewLine.setVisibility(View.GONE);
		}
		tvAddr = (TextView) findViewById(R.id.event_detial_addr);
		tvAddr.setText(item.getAddress());
		tvAddr.setOnClickListener(this);

		tvDescription = (TextView) findViewById(R.id.invite_detail_detail);
		tvDescription.setText(item.getDescription());
		tvAgree = (TextView) findViewById(R.id.invite_detial_tv_agree);
		tvAgree.setOnClickListener(this);
		tvDisagee = (TextView) findViewById(R.id.invite_detial_tv_disagree);
		tvDisagee.setOnClickListener(this);
		tvOperateResult = (TextView) findViewById(R.id.invite_detail_operatestate);
		if (item.getInviteState() == 3) {
			tvAgree.setVisibility(View.GONE);
			tvDisagee.setVisibility(View.GONE);
			tvOperateResult.setVisibility(View.VISIBLE);
			tvOperateResult.setText("已过期");
		} else {
			// "未处理","邀请中","已拒绝","已过期","已接受"
			System.out.println(type + "******");
			System.out.println(item.getInviteid() + "*****");
			if (type == 1) {
				// 未处理
				if (item.getInviteState() == 0) {
					tvAgree.setVisibility(View.VISIBLE);
					tvDisagee.setVisibility(View.VISIBLE);
					tvOperateResult.setVisibility(View.GONE);
				} else if (item.getInviteState() == 4) {
					tvAgree.setVisibility(View.GONE);
					tvDisagee.setVisibility(View.GONE);
					tvOperateResult.setVisibility(View.VISIBLE);
					tvOperateResult.setText("已授受");
				} else if (item.getInviteState() == 2) {
					tvAgree.setVisibility(View.GONE);
					tvDisagee.setVisibility(View.GONE);
					tvOperateResult.setVisibility(View.VISIBLE);
					tvOperateResult.setText("已拒绝");
				} else {
					tvAgree.setVisibility(View.VISIBLE);
					tvDisagee.setVisibility(View.VISIBLE);
					tvOperateResult.setVisibility(View.GONE);
				}
			} else {
				if (item.getInviteState() == 4) {
					tvAgree.setVisibility(View.GONE);
					tvDisagee.setVisibility(View.GONE);
					tvOperateResult.setVisibility(View.VISIBLE);
					tvOperateResult.setText("已接受");
					System.out.println(tvOperateResult.getText() + "****");
				} else if (item.getInviteState() == 2) {
					tvAgree.setVisibility(View.GONE);
					tvDisagee.setVisibility(View.GONE);
					tvOperateResult.setVisibility(View.VISIBLE);
					tvOperateResult.setText("已拒绝");
				} else {
					tvAgree.setVisibility(View.GONE);
					tvDisagee.setVisibility(View.GONE);
					tvOperateResult.setVisibility(View.VISIBLE);
					tvOperateResult.setText("未处理");
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invite_detail_picture:
			UserEngine.startUserDetial(userId);
			break;
		case R.id.event_detial_addr:
			Intent intent = new Intent(this, InviteDetailMapAcitivity.class);
			intent.putExtra(Const.Extra.Snippet, tvAddr.getText());
			intent.putExtra(Const.Extra.LatitudeE6, item.getLatitude());
			intent.putExtra(Const.Extra.LongitudeE6, item.getLongitude());
			startActivity(intent);
			break;
		case R.id.invite_detial_tv_agree:
			model.setInviteid(item.getInviteid());
			model.setInvitestate(4);
			Net.request(model, WebApi.Invite.OperateInvite,
					new ResponseHandler(true) {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String content) {
							super.onSuccess(statusCode, headers, content);
							tvAgree.setVisibility(View.GONE);
							tvDisagee.setVisibility(View.GONE);
							tvOperateResult.setVisibility(View.VISIBLE);
							tvOperateResult.setText("已授受");
							intentResult.putExtra(Extra.State,model.getInvitestate());
							setResult(RESULT_OK, intentResult);
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
							Toast.show("提交失败，请重试");
						}
					});
			break;
		case R.id.invite_detial_tv_disagree:
			model.setInviteid(item.getInviteid());
			model.setInvitestate(2);
			Net.request(model, WebApi.Invite.OperateInvite,
					new ResponseHandler(true) {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String content) {
							super.onSuccess(statusCode, headers, content);
							tvAgree.setVisibility(View.GONE);
							tvDisagee.setVisibility(View.GONE);
							tvOperateResult.setVisibility(View.VISIBLE);
							tvOperateResult.setText("已拒绝");
							intentResult.putExtra(Extra.State,model.getInvitestate());
							setResult(RESULT_OK, intentResult);
						}

						@Override
						public void onFailure(Throwable e, int statusCode,
								String content) {
							super.onFailure(e, statusCode, content);
							Toast.show("提交失败，请重试");
						}
					});
			break;
		case R.id.invite_detail_btn_back:
			finish();
			break;
		}
	}

}
