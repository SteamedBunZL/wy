package com.whoyao.topic;

import java.util.ArrayList;
import java.util.List;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import handmark.pulltorefresh.library.PullToRefreshListView;

import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.activity.BasicActivity;
import com.whoyao.adapter.TopicListAdapter;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.TopicItemRModel;
import com.whoyao.model.TopicSelfTModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.MessageDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author hyh creat_at：2014-1-22-上午10:15:17
 */
public class TopicUserListActivity extends BasicActivity implements OnClickListener, OnCheckedChangeListener, OnRefreshListener2<ListView>, OnItemClickListener, OnItemLongClickListener {
	private RadioButton rbCreat;
	private RadioButton rbJoin;
	private PullToRefreshListView mListView;
	private List<TopicItemRModel> mList = new ArrayList<TopicItemRModel>();
	private TopicListAdapter mAdapter;
	private TopicSelfTModel model = new TopicSelfTModel();
	private ResponseHandler handler;
	private ResponseHandler moreHandler;
	private MessageDialog dialog;
	private Intent detialIntent;
	private int selectItemID;
	private int userid;
	private TextView emptyView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_selflist);
		userid = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(userid == MyinfoManager.getUserInfo().getUserID()){
			userid = 0;
		}
		if(userid > 0){
			model.setUserid(userid);
		}
		detialIntent = new Intent(this, TopicDetialActivity.class);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.page_btn_right).setOnClickListener(this);
		rbCreat = (RadioButton) findViewById(R.id.topic_self_btn_creat);
		rbJoin = (RadioButton) findViewById(R.id.topic_self_btn_join);
		rbCreat.setOnCheckedChangeListener(this);
		rbJoin.setOnCheckedChangeListener(this);
		if(userid > 0){
			((TextView)findViewById(R.id.page_tv_title)).setText("TA的话题");
			rbCreat.setText("TA发布的");
			rbJoin.setText("TA回复的");
		}
		mListView = (PullToRefreshListView) findViewById(R.id.topic_self_lv);
		emptyView = (TextView)findViewById(R.id.topic_user_empty);
		mListView.getRefreshableView().setEmptyView(emptyView);
		mAdapter = new TopicListAdapter(getLayoutInflater(), mList);
		mListView.setMode(Mode.BOTH);
		mListView.getRefreshableView().setSelector(android.R.color.transparent);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		mListView.getRefreshableView().setOnItemLongClickListener(this);
		mListView.setAdapter(mAdapter);
	}
	 
	private void initData() {
		if (handler == null) {
			handler = new ResponseHandler(true) {
				@Override
				public void onSuccess(String content) {
					ArrayList<TopicItemRModel> list = JSON.getList(content, TopicItemRModel.class);
					mList.clear();
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				}
				@Override
				public void onFailure(Throwable error, String content) {
					mList.clear();
					mAdapter.notifyDataSetChanged();
				}
				@Override
				public void onFinish() {
					super.onFinish();
					if(userid > 0){
						emptyView.setText("TA还没有参与任何话题");
					}else if(model.getType() == 0){
						emptyView.setText("您还没有发布任何话题");
					}else{
						emptyView.setText("您还没有回复任何话题");
					}
					mListView.onRefreshComplete();
					
				}
			};
		}
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.Topic.UserTopic, handler);
	}

	private void loadMore() {
		if (moreHandler == null) {
			moreHandler = new ResponseHandler() {
				@Override
				public void onSuccess(String content) {
					ArrayList<TopicItemRModel> list = JSON.getList(content, TopicItemRModel.class);
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				}
				@Override
				public void onFinish() {
					super.onFinish();
					mListView.onRefreshComplete();
				}
			};
		}
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Topic.UserTopic, moreHandler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.page_btn_right:
			AppContext.startAct(TopicAddActivity.class);
			break;
		case MessageDialog.ID_LEFT_BUTTON:
			if(selectItemID != 0){
				Net.request("topicid", selectItemID + "", WebApi.Topic.DeleteTopic, new ResponseHandler(true){
					@Override
					public void onSuccess(String content) {
						NetCache.clearCaches();
						initData();
					}
					@Override
					public void onFailure(Throwable e, int statusCode, String content) {
						super.onFailure(e, statusCode, content);
						if(417 == statusCode){
							Toast.show("删除失败");
						}
					}
				});
			}
			break;
		default:
			break;
		}
	}
	private MessageDialog initDialog(){
		if(dialog == null){
			dialog = new MessageDialog(context);
			dialog.setMessage("确定要删除该话题吗?");
			dialog.setLeftButton("确定", this);
			dialog.setRightButton("取消", null);
		}
		return dialog;
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.topic_self_btn_creat:
				model.setType(0);
				break;
			case R.id.topic_self_btn_join:
				model.setType(1);
				break;
			default:
				break;
			}
			initData();
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		NetCache.clearCaches();
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(id > 0){
			detialIntent.putExtra(Extra.SelectedID, (int)id);
			startActivity(detialIntent);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if(userid == 0 && model.getType() == 0){
			selectItemID = (int)id;
			initDialog().show();
			return true;
		}else{
			return false;
		}
	}
}
