package com.whoyao.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.FaceImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.EventJoinerMgrModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;

/**
 * 活动参与人员管理
 * 
 * @author hyh Creat_at：2013-10-11-上午11:20:02
 */
public class EventJoinerMgrActivity extends BasicActivity implements
		OnClickListener{
	private static String[] state = { "待确认", "未参加", "已参加", "已取消" };
	private static int[] colors;
	private ListView mListView;
	private ArrayList<JoinerModel> mList = new ArrayList<JoinerModel>();
	private JoinerMgrAdatper adatper;
	private int id;
	private FaceImageAsyncLoader faceLoader;
	private int selectPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = getIntent().getIntExtra(Extra.SelectedID, 0);
		if (0 == id) {
			finish();
			return;
		}
		initColor();
		faceLoader = FaceImageAsyncLoader.getInstance();
		setContentView(R.layout.activity_event_joiner_mgr);
		mListView = (ListView) findViewById(R.id.joiner_mgr_lv);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		adatper = new JoinerMgrAdatper();
		mListView.setAdapter(adatper);
//		mListView.setOnItemClickListener(this);
		initData();

	}

	private void initColor() {
		if (null == colors) {
			Resources res = getResources();
			int g = res.getColor(R.color.gray_text);
			int b = res.getColor(R.color.blue_text);
			int c = res.getColor(R.color.carmine_text);
			int[] temp = { g, b, b, c };
			colors = temp;
		}
	}

	private void initData() {
		EventDetialManager.getInstance().getDetial(id, new CallBack<String>() {
			@Override
			public void onCallBack() {
				ArrayList<JoinerModel> list = EventDetialManager.joinerList;
				if (null != list) {
					mList.clear();
					mList.addAll(list);
					if (!mList.isEmpty()) {
						mList.remove(0);
					}
					adatper.notifyDataSetChanged();
				}
			}
		});
	}

	private class JoinerMgrAdatper extends BaseAdapter {

		@Override
		public int getCount() {
			if (null == mList) {
				return 0;
			}
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (null == convertView) {
				holder = new Holder();
				convertView = View.inflate(EventJoinerMgrActivity.this,
						R.layout.item_event_joiner_list, null);
				convertView.setTag(holder);
				holder.ivFace = (ImageView) convertView
						.findViewById(R.id.item_joiner_iv_face);
				holder.tvRealname = (TextView) convertView
						.findViewById(R.id.item_event_tv_realname);
				holder.tvPhone = (TextView) convertView
						.findViewById(R.id.item_joiner_tv_phone);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.item_joiner_tv_join_time);
				holder.tvNick = (TextView) convertView
						.findViewById(R.id.item_joiner_tv_nick);
				holder.tvRemark = (TextView) convertView
						.findViewById(R.id.item_joiner_tv_remark);
				holder.tvState = (TextView) convertView
						.findViewById(R.id.item_joiner_tv_state);
				holder.tvRemark.setOnClickListener(EventJoinerMgrActivity.this);
				holder.ivFace.setOnClickListener(EventJoinerMgrActivity.this);
			} else {
				holder = (Holder) convertView.getTag();
			}
			JoinerModel item = mList.get(position);
			holder.tvRemark.setTag(position);
			holder.ivFace.setTag(position);
			holder.tvRealname.setText(item.getUserrelname());
			holder.tvNick.setText(item.getUsername());
			if (item.getUserphone()!=null) {
				String phoneNumber = item.getUserphone().substring(0, 3) + "****"
						+ item.getUserphone().substring(7, 11);
				holder.tvPhone.setText(phoneNumber);
			}else {
				holder.tvPhone.setText("");
			}
			holder.tvTime
					.setText(CalendarUtils.formatYMDHM(item.getApplytime()));
			holder.tvRemark.setText(item.getOwnerremark());
			holder.tvState.setText(state[item.getJoinstate()]);
			holder.tvState.setTextColor(colors[item.getJoinstate()]);
			if (3 == item.getJoinstate()) {
				holder.tvState.setOnClickListener(null);
			} else {
				holder.tvState.setTag(position);
				holder.tvState.setOnClickListener(EventJoinerMgrActivity.this);
			}
			faceLoader.loadBitmap(item.getUserface(), holder.ivFace);
			// TODO
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.item_joiner_tv_state:
			selectPosition = (Integer) v.getTag();
			Intent intent1 = new Intent(this, SelectSingleWheelActivity.class);
			intent1.putExtra(Extra.ArrayRes, R.array.join_state);
			startActivityForResult(intent1, R.id.item_joiner_tv_state);

			break;
		case R.id.item_joiner_iv_face:
			int userID = mList.get((Integer) v.getTag()).getUserid();
			UserEngine.startUserDetial(userID);
			break;
		case R.id.item_joiner_tv_remark:
			// 填写备注
			JoinerModel item2 = mList.get((Integer) v.getTag());
			EventJoinerMgrModel model2 = serializeModel(item2);
			Intent intent2 = new Intent(this, EventJoinerRemarkActivity.class);
			intent2.putExtra(Extra.SelectedItem, model2);
			startActivityForResult(intent2, R.id.item_joiner_tv_remark);

			break;
		default:
			break;
		}
	}

	private EventJoinerMgrModel serializeModel(JoinerModel item) {
		EventJoinerMgrModel model = new EventJoinerMgrModel();
		model.setActivityid(item.getActivityid());
		model.setUserid(item.getUserid());
		model.setJoinstate(item.getJoinstate());
		model.setOwnerremark(item.getOwnerremark());
		return model;
	}

	private class Holder {
		public ImageView ivFace;
		public TextView tvRealname;
		public TextView tvPhone;
		public TextView tvTime;
		public TextView tvNick;
		public TextView tvRemark;
		public TextView tvState;
	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO
//		Toast.show("Item  " + position);
//
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.item_joiner_tv_state:
			if (RESULT_OK == resultCode && null != data) {
				int select = data.getIntExtra(Extra.SelectedItem,
						State.Selected_cancle);
				if (State.Selected_cancle != select) {
					JoinerModel item = mList.get(selectPosition);
					EventJoinerMgrModel model = serializeModel(item);
					if (0 == select) {
						model.setJoinstate(2);
					} else {
						model.setJoinstate(1);
					}
					Net.request(model, WebApi.Event.MgrJoiner,
							new ResponseHandler(true) {
								@Override
								public void onSuccess(String content) {
									NetCache.clearCaches();
									initData();
								}
							});
				}
			}
			break;
		case R.id.item_joiner_tv_remark:
			if (RESULT_OK == resultCode) {
				NetCache.clearCaches();
				initData();
			}
			break;
		default:
			break;
		}

	}
}
