package com.whoyao.topic;

import java.util.ArrayList;
import java.util.List;

import org.taptwo.widget.CircleFlowIndicator;
import org.taptwo.widget.ViewFlow;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import handmark.pulltorefresh.library.PullToRefreshListView;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.adapter.TopicRemarkAdapter;
import com.whoyao.common.BillImageAsyncLoader;
import com.whoyao.common.ImageAsyncLoader;
import com.whoyao.common.OriginalImageAsyncLoader;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.EventPhotoModel;
import com.whoyao.model.TopicDetialRModel;
import com.whoyao.model.TopicDetialTModel;
import com.whoyao.model.TopicRemarkModel;
import com.whoyao.model.TopicRemarkRModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.JSON;
import com.whoyao.utils.TimeUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class TopicDetialActivity extends BasicActivity implements OnClickListener, OnRefreshListener<ListView> {

	private PullToRefreshListView mListView;
	private BaseAdapter mAdapter;
	private ResponseHandler remarkHandler;
	private ResponseHandler handler;
	private TopicDetialTModel model;
	private List<TopicRemarkModel> mListRemark = new ArrayList<TopicRemarkModel>();
	protected ArrayList<EventPhotoModel> mListImage;
	private View vHeader;
	private View vHeaderContent;
	private ImageView ivFace;
	private TextView tvTitle;
	private TextView tvNick;
	private TextView tvTime;
	private TextView tvContent;
	private ImageAsyncLoader loader;
	private Intent remarkIntent;
	private ResponseHandler refreshHandler;
	protected int userID;
	private View rlImage;
	private ViewFlow vfImage;
	private CircleFlowIndicator ciImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detial);
		Intent intent = getIntent();
		int id = intent.getIntExtra(Extra.SelectedID, 0);
		if (id == 0) {
			finish();
			return;
		}
		remarkIntent = new Intent(this, TopicRemarkAddActivity.class);
		remarkIntent.putExtra(Extra.SelectedID, id);
		model = new TopicDetialTModel(id);
		loader = SmallImageAsyncLoader.getInstance();
		mAdapter = new TopicRemarkAdapter(getLayoutInflater(), mListRemark);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.page_btn_right).setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.topic_detial_lv);
		initHeader();
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(this);
		mListView.setAdapter(mAdapter);
	}

	private void initHeader() {
		vHeader = View.inflate(context, R.layout.header_topic_detial, null);
		vHeaderContent = vHeader.findViewById(R.id.topic_detial_header);
		vHeaderContent.setVisibility(View.GONE);
		ivFace = (ImageView) vHeader.findViewById(R.id.topic_detial_iv_face);
		ivFace.setOnClickListener(this);
		tvTitle = (TextView) vHeader.findViewById(R.id.topic_detial_tv_title);
		tvNick = (TextView) vHeader.findViewById(R.id.topic_detial_tv_nick);
		tvNick.setOnClickListener(this);
		tvTime = (TextView) vHeader.findViewById(R.id.topic_detial_tv_time);
		tvContent = (TextView) vHeader.findViewById(R.id.topic_detial_tv_content);
		rlImage = vHeader.findViewById(R.id.topic_detial_rl_image);
		vfImage = (ViewFlow)vHeader.findViewById(R.id.topic_detial_vf_image);
		ciImage = (CircleFlowIndicator)vHeader.findViewById(R.id.topic_detial_ci_image);
		mListView.getRefreshableView().addHeaderView(vHeader);
	}

	private void initData() {
		if (null == handler) {
			handler = new ResponseHandler() {
				@Override
				public void onSuccess(String content) {
					TopicDetialRModel rModel = JSON.getObject(content, TopicDetialRModel.class);
					if (rModel != null) {
						userID = rModel.getUserid();
						loader.loadBitmap(rModel.getUserface(), ivFace);
						String title = "[" + rModel.getTag() + "]" + rModel.getTitle();
						tvTitle.setText(title);
						remarkIntent.putExtra(Extra.Title, title);
						tvNick.setText(rModel.getUsername());
						tvTime.setText(TimeUtils.getTime(rModel.getTime()));
						tvContent.setText(rModel.getContent());
						vHeaderContent.setVisibility(View.VISIBLE);
						mListImage = rModel.getImage();
						if(mListImage != null && !mListImage.isEmpty()){
							
							rlImage.setVisibility(View.VISIBLE);
							vfImage.setAdapter(new BaseAdapter() {
								LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
								ImageAsyncLoader imageLoader = OriginalImageAsyncLoader.getInstance();
								
								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									ImageView iv = null;
									if(null == convertView){
										iv = new ImageView(context);
										iv.setLayoutParams(lp);
									}else{
										iv = (ImageView) convertView;
									}
									imageLoader.loadBitmapByID(mListImage.get(position).getPhotopath(), iv);
//									iv.setImageResource(helpImages[position]);
//									if(position+1 == helpImages.length){
//										iv.setOnClickListener(GuideActivity.this);
//									}else{
//										iv.setOnClickListener(null);
//									}
									return iv;
								}
								@Override
								public long getItemId(int position) {
									return position;
								}
								@Override
								public Object getItem(int position) {
									return mListImage.get(position);
								}
								@Override
								public int getCount() {
									return mListImage.size();
								}
							});
							ciImage.setViewFlow(vfImage);
							vfImage.setFlowIndicator(ciImage);
						}else{
							rlImage.setVisibility(View.GONE);
						}
						
						if(rModel.getRemark() != null){
							mListRemark.addAll(rModel.getRemark());
							mAdapter.notifyDataSetChanged();
						}
						
						
					}
				}
			};
		}
		Net.cacheRequest(model, WebApi.Topic.GetDetial, handler);
	}

	private void loadMore() {
		if (null == remarkHandler) {
			remarkHandler = new ResponseHandler() {
				@Override
				public void onSuccess(String content) {
					TopicRemarkRModel remarks = JSON.getObject(content, TopicRemarkRModel.class);
					if (remarks != null && remarks.topicitem != null) {
						mListRemark.addAll(remarks.topicitem);
						mAdapter.notifyDataSetChanged();
						if(remarks.topicitem.isEmpty()){
							model.setPageindex(model.getPageindex() - 1);
						}
					}
					
					mListView.onRefreshComplete();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					model.setPageindex(model.getPageindex() - 1);
					mListView.onRefreshComplete();
				}
			};
		}
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Topic.GetRemark, remarkHandler);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.page_btn_right:
			if (resultCode == RESULT_OK) {
				if (refreshHandler == null) {
					refreshHandler = new ResponseHandler() {
						@Override
						public void onSuccess(String content) {
							TopicRemarkRModel remarks = JSON.getObject(content, TopicRemarkRModel.class);
							if (remarks != null && remarks.topicitem != null) {
								mListRemark.clear();
								mListRemark.addAll(remarks.topicitem);
								mAdapter.notifyDataSetChanged();
								if(mListRemark.size() > 1){
									mListView.getRefreshableView().setSelection(1);
								}
							}
						}
					};
				}
				NetCache.clearCaches();
				model.setPageindex(Const.PAGE_DEFAULT_INDEX);
				Net.cacheRequest(model, WebApi.Topic.GetRemark, refreshHandler);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.page_btn_right:
			startActivityForResult(remarkIntent, R.id.page_btn_right);
			break;
		case R.id.topic_detial_tv_nick:
		case R.id.topic_detial_iv_face:
			UserEngine.startUserDetial(userID);
		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}
}
