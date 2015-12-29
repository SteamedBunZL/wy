package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.RequestParams;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.AppContext;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventListOtherAdapter;
import com.whoyao.model.EventListItem;
import com.whoyao.model.EventListOtherTModel;
import com.whoyao.model.EventMyListRModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;  
/**
 * 我的活动列表
 * @author hyh 
 * creat_at：2013-9-13-上午9:43:24
 */
public class EventOther2ListActivity extends BasicActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private PullToRefreshListView mListView;
	private EventListOtherAdapter creatAdapter;
	private EventListOtherAdapter joinAdapter;
	private List<EventListItem> creatList = new ArrayList<EventListItem>();
	private List<EventListItem> joinList = new ArrayList<EventListItem>();
	private EventListOtherTModel model = null;
	private Button btnCreat;
	private Button btnJoin;
	private ResponseHandler initHandler;
	private boolean isRefresh;
	private ResponseHandler moreHandler;
	private int userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_other_list);
		initView();
		userID = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 != userID){
			model = new EventListOtherTModel();
			model.setType(Type.Creater);
			model.setUserId(userID);
		}else{
			finish();
			return;
		}
		initData();
	}
	private void initView() {
		btnCreat = (Button)findViewById(R.id.event_ta_btn_creat);
		btnCreat.setSelected(true);
		btnJoin = (Button)findViewById(R.id.event_ta_btn_join);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		
		
		btnCreat.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		mListView = (PullToRefreshListView)findViewById(R.id.event_lv);
		mListView.getRefreshableView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT ));
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		creatAdapter = new EventListOtherAdapter(creatList,LayoutInflater.from(this));
		joinAdapter = new EventListOtherAdapter(joinList,LayoutInflater.from(this));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		
		case R.id.event_ta_btn_creat:
			if(btnCreat.isSelected()){
				return;
			}
			model.setType(Type.Creater);
			btnCreat.setSelected(true);
			btnJoin.setSelected(false);
			initData();
			break;
		case R.id.event_ta_btn_join:
			if(btnJoin.isSelected()){
				return;
			}
			model.setType(Type.Joiner);
			btnCreat.setSelected(false);
			btnJoin.setSelected(true);
			initData();
			break;
		case R.id.item_event_tv_joiner_mgr:
			Intent joinIntent = new Intent(this, EventJoinerMgrActivity.class);
			joinIntent.putExtra(Extra.SelectedID, getCurrentList().get((Integer) v.getTag()).getActivityid());
			startActivityForResult(joinIntent,R.id.item_event_tv_joiner_mgr);
			break;
		case R.id.item_event_tv_hide:
			RequestParams params = new RequestParams();
			EventListItem item = getCurrentList().get((Integer) v.getTag());
			params.put(KEY.EventID, item.getActivityid()+"");
			String hidestatus = item.getActivityhidestatus() == 0?"1":"0";
			params.put("hidestatus", hidestatus);
			Net.request(params , WebApi.Event.HideEvent, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					initData();
				}
			});
			break;
		case R.id.item_event_tv_upload_photo:
			Intent photoIntent = new Intent(this, EventPhotoAddActivity.class);
			photoIntent.putExtra(Extra.SelectedID, getCurrentList().get((Integer) v.getTag()).getActivityid());
			startActivityForResult(photoIntent,R.id.item_event_tv_upload_photo);
			break;
		case R.id.item_event_tv_valu:
			EventListItem item2 = getCurrentList().get((Integer) v.getTag());
			Intent valuIntent = new Intent();
			valuIntent.putExtra(Extra.SelectedID, item2.getActivityid());
			if(0 ==item2.getActivityevaluationvalue()){// TODO 判断是否已评过分
				valuIntent.setClass(this, EventValuationActivity.class);
				startActivityForResult(valuIntent,R.id.item_event_tv_valu);
			}else{
				valuIntent.setClass(this, EventUserValuActivity.class);
				startActivity(valuIntent);
			}
			break;
		default:
			break;
		}
		
	}

	
	private List<EventListItem> getCurrentList(){
		if(Type.Creater == model.getType()){
			return creatList;
		}else{
			return joinList;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int hearders = ((ListView)parent).getHeaderViewsCount();
		Intent intent = new Intent(this,EventDetialActivity.class);
		intent.putExtra(Extra.SelectedID, getCurrentList().get(position-hearders).getActivityid());
		startActivity(intent);
	}
	
	private void initData(){
		if(null == initHandler){
			initHandler = new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					mListView.onRefreshComplete();
					isRefresh = false;
					ArrayList<EventListItem> list = JSON.getObject(content, EventMyListRModel.class).MyActivityListItem;
					if(null != list){
						if(Type.Creater == model.getType()){
							mListView.setAdapter(creatAdapter);
							creatList.clear();
							creatList.addAll(list);
							creatAdapter.notifyDataSetChanged();
						}else{
							mListView.setAdapter(joinAdapter);
							joinList.clear();
							joinList.addAll(list);
							joinAdapter.notifyDataSetChanged();
						}
						mListView.setMode(Mode.BOTH);
					}else{
						mListView.setMode(Mode.PULL_FROM_START);
					}
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					mListView.onRefreshComplete();
					isRefresh = false;
					if(Type.Creater == model.getType()){
						mListView.setAdapter(creatAdapter);
						creatList.clear();
						creatAdapter.notifyDataSetChanged();
					}else{
						mListView.setAdapter(joinAdapter);
						joinList.clear();
						joinAdapter.notifyDataSetChanged();
					}
					mListView.setMode(Mode.PULL_FROM_START);
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.Event.GetOtherEvent,initHandler);
	}
	private void loadMore(){
		if(null == moreHandler){
			moreHandler = new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					isRefresh = false;
					ArrayList<EventListItem> list = JSON.getObject(content, EventMyListRModel.class).MyActivityListItem;
					if(null != list){
						if(Type.Creater == model.getType()){
							creatList.addAll(list);
							creatAdapter.notifyDataSetChanged();
						}else{
							joinList.addAll(list);
							joinAdapter.notifyDataSetChanged();
						}
						mListView.onRefreshComplete();
						if(model.getPagesize() > list.size()){
							mListView.setMode(Mode.PULL_FROM_START);
						}
					}else{
						mListView.onRefreshComplete();
						mListView.setMode(Mode.PULL_FROM_START);
					}
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					mListView.onRefreshComplete();
					isRefresh = false;
					if(400 == statusCode){
						mListView.setMode(Mode.PULL_FROM_START);
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Event.GetOtherEvent,moreHandler);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case R.id.item_event_tv_joiner_mgr:
		case R.id.item_event_tv_hide:
		case R.id.item_event_tv_upload_photo:
		case R.id.item_event_tv_valu:
			NetCache.clearCaches();
			initData();
			break;
		default:
			break;
		}
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(CalendarUtils.formatDate(AppContext.serviceTimeMillis()));
		isRefresh = true;
		NetCache.clearCaches();
		initData();
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadMore();
	}
 
}
