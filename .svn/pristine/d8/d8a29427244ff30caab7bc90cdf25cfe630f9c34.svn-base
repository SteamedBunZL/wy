package com.whoyao.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.JoinerGridAdapter;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.EventInfoModel;
import com.whoyao.model.JoinerModel;
import com.whoyao.model.UserDetialModel;
import com.whoyao.model.UserInfoModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.AddressUtil;
import com.whoyao.utils.JSON;

/**
 * @author hyh 
 * creat_at：2013-9-22-下午2:06:29
 */
public class EventJoinerActivity extends BasicActivity implements OnClickListener {
	private ImageView ivCreaterFace;
	private TextView tvCreaterNick;
	private TextView tvCreaterSex;
	private TextView tvCreaterAddr;
	private TextView tvJoinnum;
	private TextView tvMaxnum;
	private GridView gvJoiner;
	private SmallImageAsyncLoader loader;
	private ArrayList<JoinerModel> mList;
	private int userId;
	private UserDetialModel detialModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_joiner);
		loader = SmallImageAsyncLoader.getInstance();
		initView();
		initData();
	}
	private void initData(){
		RequestParams params = new RequestParams();
		params.put(KEY.User_ID, userId + "");
		params.put(KEY.Type, Type.Detial_other);
		Net.cacheRequest(params, WebApi.User.DetailInfo, new ResponseHandler(
				true) {
			@Override
			public void onSuccess(String content) {
				detialModel = JSON.getObject(content, UserDetialModel.class);
				UserInfoModel userinfo = detialModel.getUserList();
				tvCreaterSex.setText(userinfo.getSexStr());
				tvCreaterAddr.setText(AddressUtil.getFullAddr(userinfo.getUserDistrict()));
			}
		});
	}

	private void initView() {
		ivCreaterFace = (ImageView)findViewById(R.id.event_joiner_iv_face);
		ivCreaterFace.setOnClickListener(this);
		tvCreaterNick = (TextView)findViewById(R.id.event_joiner_tv_nick);
		tvCreaterSex = (TextView)findViewById(R.id.event_joiner_tv_sex);
		tvCreaterAddr = (TextView)findViewById(R.id.event_joiner_tv_addr);
		tvJoinnum = (TextView)findViewById(R.id.event_joiner_tv_joinnum);
		tvMaxnum = (TextView)findViewById(R.id.event_joiner_tv_maxnum);
		gvJoiner = (GridView)findViewById(R.id.event_joiner_gv);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		ivCreaterFace.setOnClickListener(this);
		mList = EventDetialManager.joinerList;
		
		
		EventInfoModel info = EventDetialManager.eventInfo;
		info.getMaxnum();
		info.getJoinnum();
		tvJoinnum.setText(info.getJoinnum() +"");
		tvMaxnum.setText(info.getMaxnum() +"");
		tvCreaterNick.setText(mList.get(0).getUsername());
		userId = mList.get(0).getUserid();
		loader.loadBitmap(mList.get(0).getUserface(), ivCreaterFace);
		gvJoiner.setAdapter(new JoinerGridAdapter(mList));
		gvJoiner.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserEngine.startUserDetial(mList.get(position +1).getUserid());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_joiner_iv_face:
			UserEngine.startUserDetial(userId);
			break;
		default:
			break;
		}
		
	}
	
}
