package com.whoyao.activity;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshListView;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.RequestParams;
import com.whoyao.AppContext;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.Const.Type;
import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.adapter.EventCreatListAdapter;
import com.whoyao.adapter.EventJoinListAdapter;
import com.whoyao.model.EventListItem;
import com.whoyao.model.EventMyListRModel;
import com.whoyao.model.EventMyListTModel;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;  
/**
 * 我的活动列表
 * @author hyh 
 * creat_at：2013-9-13-上午9:43:24
 */
public class EventMyListActivity extends BasicActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {
	private PullToRefreshListView mListView;
	private EventCreatListAdapter creatAdapter;
	private List<EventListItem> creatList = new ArrayList<EventListItem>();
	private List<EventListItem> joinList = new ArrayList<EventListItem>();
	private EventMyListTModel model = null;
	private Button btnCreat;
	private Button btnJoin;
	private EventJoinListAdapter joinAdapter;
	private TextView tvType;
	private TextView tvProg;
	private TextView tvState;
	private ResponseHandler initHandler;
	private boolean isRefresh;
	private ResponseHandler moreHandler;
//	private GridView popType;
	private View popCate;
	private View popProg;
	private View popState;
	private View shadow1;
	private View shadow0;
	private View llType;
	private View llProg;
	private View llState;;
	private View[] categoryViews = new View[3];
	private View[] progressViews = new View[5];
	private View[] stateViews = new View[4];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_mylist);
		getResources().getStringArray(R.array.event_type);
		initView();
		initPop();
		model = new EventMyListTModel();
		model.setType(Type.Creater);
		btnCreat.setSelected(true);
		initData();
	}
	private void initView() {
		btnCreat = (Button)findViewById(R.id.event_my_btn_creat);
		btnJoin = (Button)findViewById(R.id.event_my_btn_join);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		
		
		btnCreat.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		mListView = (PullToRefreshListView)findViewById(R.id.event_lv);
		mListView.getRefreshableView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT ));
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		creatAdapter = new EventCreatListAdapter(creatList,LayoutInflater.from(this),this);
		joinAdapter = new EventJoinListAdapter(joinList,LayoutInflater.from(this),this);
	}
	private void initPop() {
		tvType = (TextView)findViewById(R.id.event_my_tv_type);
		llType = findViewById(R.id.event_my_ll_type);
		tvProg = (TextView)findViewById(R.id.event_my_tv_prog);
		llProg = findViewById(R.id.event_my_ll_prog);
		tvState = (TextView)findViewById(R.id.event_my_tv_state);
		llState = findViewById(R.id.event_my_ll_state);
		
//		popType = (GridView)findViewById(R.id.event_my_gv_type);
		popCate = findViewById(R.id.event_my_pop_category);
		popProg = findViewById(R.id.event_my_pop_progress);
		popState = findViewById(R.id.event_my_pop_state);
		shadow0 = findViewById(R.id.event_my_shadow_0);
		shadow1 = findViewById(R.id.event_my_shadow_1);
		popState = findViewById(R.id.event_my_pop_state);
		
		shadow0.setOnClickListener(this);
		shadow1.setOnClickListener(this);
		
		OnTouchListener touchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.isSelected()) {
					return false;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					((ViewGroup)v).getChildAt(0).setSelected(true);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_OUTSIDE:
					((ViewGroup)v).getChildAt(0).setSelected(false);
					break;
				}
				return false;
			}
		};
		llType.setOnTouchListener(touchListener);
		llProg.setOnTouchListener(touchListener);
		llState.setOnTouchListener(touchListener);
	//TODO
		PopOnCOnClickListener listener = new PopOnCOnClickListener();
		llType.setOnClickListener(listener);
		llProg.setOnClickListener(listener);
		llState.setOnClickListener(listener);

		categoryViews[0] = findViewById(R.id.event_my_cate_0);
		categoryViews[1] = findViewById(R.id.event_my_cate_1);
		categoryViews[2] = findViewById(R.id.event_my_cate_2);
		categoryViews[0].setOnClickListener(listener);
		categoryViews[1].setOnClickListener(listener);
		categoryViews[2].setOnClickListener(listener);
		categoryViews[0].setSelected(true);
		progressViews[0] = findViewById(R.id.event_my_prog_0);
		progressViews[1] = findViewById(R.id.event_my_prog_1);
		progressViews[2] = findViewById(R.id.event_my_prog_2);
		progressViews[3] = findViewById(R.id.event_my_prog_3);
		progressViews[4] = findViewById(R.id.event_my_prog_4);
		progressViews[0].setOnClickListener(listener);
		progressViews[1].setOnClickListener(listener);
		progressViews[2].setOnClickListener(listener);
		progressViews[3].setOnClickListener(listener);
		progressViews[4].setOnClickListener(listener);
		progressViews[0].setSelected(true);
		stateViews[0] = findViewById(R.id.event_my_state_0);
		stateViews[1] = findViewById(R.id.event_my_state_1);
		stateViews[2] = findViewById(R.id.event_my_state_2);
		stateViews[3] = findViewById(R.id.event_my_state_3);
		stateViews[0].setOnClickListener(listener);
		stateViews[1].setOnClickListener(listener);
		stateViews[2].setOnClickListener(listener);
		stateViews[3].setOnClickListener(listener);
		stateViews[0].setSelected(true);
		
//		popType.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		popType.setAdapter(new BaseAdapter() {
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				View v = View.inflate(context, R.layout.item_event_pop_grid, null);
//				TextView tv = (TextView) v.findViewById(R.id.item_pop_tv);
//				tv.setText(tags[position]);
//				if (tags.length % 3 >= 19 - position) {
//					v.findViewById(R.id.item_pop_line_h).setVisibility(View.GONE);
//				}
//				if (2 == position % 3) {
//					v.findViewById(R.id.item_pop_line_v).setVisibility(View.GONE);
//				}
//				return v;
//			}
//
//			@Override
//			public long getItemId(int position) {
//				return 0;
//			}
//
//			@Override
//			public Object getItem(int position) {
//				return null;
//			}
//
//			@Override
//			public int getCount() {
//				return tags.length;
//			}
//		});
//		popType.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				hidePop();
//				hideShadow();
//				clearSelected();
//				model.setTypeid(position);
//				if (0 == position) {
//					tvType.setText("活动类型");
//				} else {
//					tvType.setText(tags[position]);
//				}
//				model.setTypeid(position);
//			}
//			// TODO 请求数据
//		});
	}	
	protected void clearSelected() {
		tvType.setSelected(false);
		tvProg.setSelected(false);
		tvState.setSelected(false);
		llType.setSelected(false);
		llProg.setSelected(false);
		llState.setSelected(false);
	}
	private void hidePop() {
		popCate.setVisibility(View.GONE);
		popProg.setVisibility(View.GONE);
		popState.setVisibility(View.GONE);
	}
	private void hideShadow() {
		shadow0.setVisibility(View.GONE);
		shadow1.setVisibility(View.GONE);
	}
	private void showShadow() {
		shadow0.setVisibility(View.VISIBLE);
		shadow1.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			onBack();
			break;
		
		case R.id.event_my_btn_creat:
			if(btnCreat.isSelected()){
				return;
			}
			model.setType(Type.Creater);
			btnCreat.setSelected(true);
			btnJoin.setSelected(false);
			clearFilter();
			initData();
			break;
		case R.id.event_my_btn_join:
			if(btnJoin.isSelected()){
				return;
			}
			model.setType(Type.Joiner);
			btnCreat.setSelected(false);
			btnJoin.setSelected(true);
			clearFilter();
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
		case R.id.event_my_shadow_0:
		case R.id.event_my_shadow_1:
			hidePop();
			hideShadow();
			clearSelected();
			break;
		default:
			break;
		}
		
	}
	
	private void setCategoryState(View v){
		for(View view : categoryViews){
			view.setSelected(false);
		}
		v.setSelected(true);
	}
	private void setProgressState(View v){
		for(View view : progressViews){
			view.setSelected(false);
		}
		v.setSelected(true);
	}
	private void setStateState(View v){
		for(View view : stateViews){
			view.setSelected(false);
		}
		v.setSelected(true);
	}
	
	private void clearFilter() {
		tvType.setText("类型");
		tvProg.setText("进度");
		tvState.setText("状态");
		model.setTypeid(0);
		model.setActivityprogressstate(0);
		model.setActivitystate(0);
	}
	
	private List<EventListItem> getCurrentList(){
		if(Type.Creater == model.getType()){
			return creatList;
		}else{
			return joinList;
		}
	}
	
	@Override
	protected boolean onBack() {
		if(View.VISIBLE == shadow0.getVisibility()){
			hidePop();
			hideShadow();
			clearSelected();
		}else{
			finish();
		}
		return true;
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
						creatList.clear();
						creatAdapter.notifyDataSetChanged();
					}else{
						joinList.clear();
						joinAdapter.notifyDataSetChanged();
					}
					if(400 == statusCode){
						mListView.setMode(Mode.PULL_FROM_START);
					}
				}
			};
		}
		initHandler.setShowProgress(!isRefresh);
		model.setPageindex(Const.PAGE_DEFAULT_INDEX);
		Net.cacheRequest(model, WebApi.Event.GetMyEvent,initHandler);
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
		model.setPageindex(model.getPageindex() + 1);
		Net.cacheRequest(model, WebApi.Event.GetMyEvent,moreHandler);
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
 
	class PopOnCOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			boolean isSelected = v.isSelected();
			hidePop();
			clearSelected();
			if(isSelected){
				hideShadow();
				return;
			}
			switch (v.getId()) {
			case R.id.event_my_ll_type:
				tvType.setSelected(true);
				llType.setSelected(true);
				showShadow();
				popCate.setVisibility(View.VISIBLE);
				break;
			case R.id.event_my_ll_prog:
				tvProg.setSelected(true);
				llProg.setSelected(true);
				showShadow();
				popProg.setVisibility(View.VISIBLE);
				break;
			case R.id.event_my_ll_state:
				tvState.setSelected(true);
				llState.setSelected(true);
				showShadow();
				popState.setVisibility(View.VISIBLE);
				break;
			case R.id.event_my_cate_0:
				model.setActivitycategory(0);
				tvType.setText("类别");
				setCategoryState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_cate_1:
				model.setActivitycategory(2);
				tvType.setText("即时活动");
				setCategoryState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_cate_2:
				model.setActivitycategory(4);
				tvType.setText("普通活动");
				setCategoryState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_prog_0:
				model.setActivityprogressstate(0);
				tvProg.setText("进度");
				setProgressState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_prog_1:
				model.setActivityprogressstate(2);
				tvProg.setText("报名中");
				setProgressState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_prog_2:
				model.setActivityprogressstate(4);
				tvProg.setText("即将开始");
				setProgressState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_prog_3:
				model.setActivityprogressstate(8);
				tvProg.setText("正在进行");
				setProgressState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_prog_4:
				model.setActivityprogressstate(16);
				tvProg.setText("已结束");
				setProgressState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_state_0:
				model.setActivitystate(0);
				tvState.setText("状态");
				setStateState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_state_1:
				model.setActivitystate(2);
				tvState.setText("已达成");
				setStateState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_state_2:
				model.setActivitystate(4);
				tvState.setText("未达成");
				setStateState(v);
				hideShadow();
				initData();
				break;
			case R.id.event_my_state_3:
				model.setActivitystate(8);
				tvState.setText("招募中");
				setStateState(v);
				hideShadow();
				initData();
				break;
			default:
				break;
			}
		}
	}
}
