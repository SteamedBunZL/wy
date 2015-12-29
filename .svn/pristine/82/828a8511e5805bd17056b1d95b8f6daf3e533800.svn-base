package com.whoyao.activity;

import java.io.File;
import java.util.ArrayList;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Extra;
import com.whoyao.Const.KEY;
import com.whoyao.common.SmallImageAsyncLoader;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.event.EventDetialManager;
import com.whoyao.engine.event.EventEngine;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.EventCommentItem;
import com.whoyao.model.EventRemarkRModel;
import com.whoyao.model.EventRemarkTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.net.CodeResponseHandler;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.JSON;
import com.whoyao.utils.TimeUtils;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author hyh 
 * creat_at：2013-9-24-上午10:20:56
 */
public class EventRemarkActivity extends BasicActivity implements OnClickListener {
	private ListView mListView;
	private EditText etContent;
	private EditText etCode;
	private Button btnEnter0;
	private View llCode;
	private ImageView ivCode;
	private SmallImageAsyncLoader loader;
	private View footer;
	private View footer1;
	private RemarkAdapter mAdapter;
	private String randomCode;
	private int id;
	private ArrayList<EventCommentItem> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loader = SmallImageAsyncLoader.getInstance();
		id = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == id){
			id = EventDetialManager.getCurrentID();
		}
		setContentView(R.layout.activity_event_remark);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.event_comment_btn_enter1).setOnClickListener(this);
		etContent = (EditText)findViewById(R.id.event_comment_et_content);
		etCode = (EditText) findViewById(R.id.event_comment_et_code);
		
		btnEnter0 = (Button)findViewById(R.id.event_comment_btn_enter0);
		btnEnter0.setOnClickListener(this);
		llCode = findViewById(R.id.event_comment_ll_code);
		ivCode = (ImageView)findViewById(R.id.event_comment_iv_code);
		ivCode.setOnClickListener(this);
		mListView = (ListView)findViewById(R.id.event_comment_lv);
		mListView.setFooterDividersEnabled(false);
		
		footer = View.inflate(this, R.layout.footer_event_comment, null);
		footer1 = footer.findViewById(R.id.footer_comment_ll_1);
		mListView.addFooterView(footer, null, false);
		//mListView.addFooterView(footer);
		mList = new ArrayList<EventCommentItem>();
		mAdapter = new RemarkAdapter();
		mListView.setAdapter(mAdapter);
	}

	private void initData(){
		
		Net.request(KEY.EventID, id+"", WebApi.Event.GetRemark, new ResponseHandler(true){
			@Override
			public void onSuccess(String content) {
				ArrayList<EventCommentItem> list = ((EventRemarkRModel)JSON.getObject(content, EventRemarkRModel.class)).ActivityComment;
				mList.clear();
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
				
			}
			@Override
			public void onFailure(Throwable e, int statusCode, String content) {
				mList.clear();
				mAdapter.notifyDataSetInvalidated();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.event_comment_btn_enter0:
//			if (etContent.getText().toString()==null||etContent.getText().toString()=="") {
//				Toast.show("请输入内容");
//				return;
//			}
			EventRemarkTModel model = new EventRemarkTModel(id);
			model.setCommentcontent(etContent.getText().toString());
			EventEngine.addRemark(model, new CallBack<ResultModel>() {

				@Override
				public void onCallBack(ResultModel data) {
					switch (data.getResult()) {
					case 0:
						Toast.show("因您多次重复输入相同内容，再次发布请先输入验证码");
						showCodeView(true);
						randomCode = "&"+AppContext.serviceTimeMillis();
						ivCode.setImageBitmap(null);
						Net.request(WebApi.Code_Addr + "&id="+MyinfoManager.getUserInfo().getSessionId()+randomCode,new CodeResponseHandler(new CallBack<File>(){
							@Override
							public void onCallBack(File data) {
								ivCode.setImageBitmap(BitmapFactory.decodeFile(data.getPath()));
							}
						}));
						//loader.loadBitmap(, ivCode);
						break;
					case 1:
						// TODO 接口要更换
						etContent.setText("");
						ivCode.setImageBitmap(null);
						showCodeView(false);
						NetCache.clearCaches();
						initData();
//						EventDetialManager.getInstance().getDetial(EventDetialManager.getCurrentID(),
//								new CallBack<String>() {
//									public void onCallBack() {
//										mAdapter.notifyDataSetChanged();
//									};
//								});
						break;
					case 2:
						
						break;
					default:
						break;
					}
				}
			});

			break;
		case R.id.event_comment_btn_enter1:
			if (TextUtils.isEmpty(etContent.getText())) {
				Toast.show(R.string.warn_code_empty);
				return;
			}
			EventRemarkTModel model1 = new EventRemarkTModel(id);
			model1.setCommentcontent(etContent.getText().toString());
			model1.setCodekey("android_remark");
			model1.setCodevalue(etCode.getText().toString());
			EventEngine.addRemark(model1, new CallBack<ResultModel>() {

				@Override
				public void onCallBack(ResultModel data) {
					if (2 == data.getResult()) {// 需要验证
						Toast.show(R.string.warn_code_wrong);
					} else {
						// TODO 接口要更换
						etContent.setText("");
						randomCode = null;
						ivCode.setImageBitmap(null);
						showCodeView(false);
						NetCache.clearCaches();
						initData();
//						EventDetialManager.getInstance().getDetial(EventDetialManager.getCurrentID(),
//								new CallBack<String>() {
//									public void onCallBack() {
//										mAdapter.notifyDataSetChanged();
//									};
//								});
					}
				}
			});
			break;
		case R.id.event_comment_iv_code:
			// TODO 刷新验证码
			randomCode = "&"+AppContext.serviceTimeMillis();
			ivCode.setImageBitmap(null);// TODO
			Net.request(WebApi.Code_Addr + "&id="+MyinfoManager.getUserInfo().getSessionId() + randomCode,new CodeResponseHandler(new CallBack<File>(){
				@Override
				public void onCallBack(File data) {
					ivCode.setImageBitmap(BitmapFactory.decodeFile(data.getPath()));
				}
			}));
			break;
		default:
			break;
		}
	}
	
	private void showCodeView(boolean showCode){
		if(showCode){
			btnEnter0.setVisibility(View.GONE);
			llCode.setVisibility(View.VISIBLE);
			footer1.setVisibility(View.VISIBLE);
		}else {
			btnEnter0.setVisibility(View.VISIBLE);
			llCode.setVisibility(View.GONE);
			footer1.setVisibility(View.GONE);
		}
	}
	
	class RemarkAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
				return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mList.get(position).getCommentid();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder h = null;
			if(null == convertView){
				convertView = View.inflate(getApplicationContext(), R.layout.item_event_remark, null);
				h = new Holder();
				convertView.setTag(h);
				h.ivFace = (ImageView) convertView.findViewById(R.id.item_joiner_iv_face);
				h.tvNick = (TextView) convertView.findViewById(R.id.item_event_tv_nick);
				h.tvTime = (TextView) convertView.findViewById(R.id.item_event_tv_time);
				h.tvContent = (TextView) convertView.findViewById(R.id.item_event_tv_content);
			}else{
				h = (Holder) convertView.getTag();
			}
			EventCommentItem item = mList.get(position);
//			String url = WebApi.getImageUrl(item.get, WebApi.ImageDemen_100);
			loader.loadBitmap(item.getUserurl(), h.ivFace);
			h.tvNick.setText(item.getUsername());
			h.tvTime.setText(TimeUtils.getTime(item.getCreatetime()));
			
//			if(CalendarUtils.isToday(item.getCreatetime())){
//				h.tvTime.setText(CalendarUtils.formatHM(item.getCreatetime()));
//			}else if(CalendarUtils.isThisYear(item.getCreatetime())){
//				h.tvTime.setText(CalendarUtils.formatMDHM(item.getCreatetime()));
//			}else{
//				h.tvTime.setText(CalendarUtils.formatYMDHM(item.getCreatetime()));
//			}
			h.tvContent.setText(item.getCommentcontent());
			return convertView;
		}
		
		class Holder{
			public ImageView ivFace;
			public TextView tvNick;
			public TextView tvTime;
			public TextView tvContent;
		}
	}
}
