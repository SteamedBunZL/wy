package com.whoyao.activity;

import java.util.Calendar;

import com.whoyao.AppException;
import com.whoyao.Const.Extra;
import com.whoyao.Const.State;
import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.model.AddMyFreeModel;
import com.whoyao.net.Net;
import com.whoyao.net.NetCache;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;
import com.whoyao.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 发闲人预告
 * @author hyh 
 * creat_at：2013-7-29-下午7:39:58
 */
public class MyFreeAddActivity extends BasicActivity implements OnClickListener {
	private static final int REQUEST_CODE_BEGINTIME = 0;
	private static final int REQUEST_CODE_ENDTIME = 1;
	
	
	private TextView beginTv;
	private TextView endTv;
	private TextView numTv;
	private EditText contentEt;
	private Handler handler;
	private Calendar cal = null;
	private long beginTime = -1;
	private long endTime = -1;
//	private String beginTimeStr = null;
//	private String endTimeStr = null;
//	private String contentStr;
	private AddMyFreeModel model = new AddMyFreeModel();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_free_add);
		
		beginTv = (TextView)findViewById(R.id.myaf_tv_begin);
		endTv = (TextView)findViewById(R.id.myaf_tv_end);
		numTv = (TextView)findViewById(R.id.event_desc_tv_num);
		contentEt = (EditText)findViewById(R.id.event_desc_et_content);

		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.myaf_ll_begin).setOnClickListener(this);
		findViewById(R.id.myaf_ll_end).setOnClickListener(this);
		findViewById(R.id.event_desc_btn_enter).setOnClickListener(this);
		contentEt.addTextChangedListener(new TextNumWatcher(numTv, 50));
		handler = new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				AppContext.startAct(MyFreeActivity.class,true);
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.myaf_ll_begin:
			cal =CalendarUtils.getNewCalendar();
			Intent beginIntent = new Intent(this,SelectTimeHalfHourActivity.class);
			beginIntent.putExtra(Extra.Time_Earliest, cal.getTimeInMillis());
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+3);
			beginIntent.putExtra(Extra.Time_Latest, cal.getTimeInMillis());
			startActivityForResult(beginIntent, REQUEST_CODE_BEGINTIME);
			break;
		case R.id.myaf_ll_end:
			if(null == cal || State.Selected_cancle == beginTime){
				Toast.show("请选择开始时间");
				return;
			}
			Intent endIntent = new Intent(this,SelectTimeHalfHourActivity.class);
			cal.setTimeInMillis(beginTime);
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
			endIntent.putExtra(Extra.Time_Earliest, cal.getTimeInMillis());
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
			endIntent.putExtra(Extra.Time_Latest, cal.getTimeInMillis());
			startActivityForResult(endIntent, REQUEST_CODE_ENDTIME);
			break;
		case R.id.event_desc_btn_enter:
			if(State.Selected_cancle == beginTime){
				Toast.show("请输入开始时间");
				return;
			}
			if(State.Selected_cancle == endTime){
				Toast.show("请输入结束时间");
				return;
			}
			model.setFreetimecontent(contentEt.getText().toString());
			if(TextUtils.isEmpty(model.getFreetimecontent())){
				Toast.show("请输入闲人宣言，2-50个字");
				return;
			}
			if(2>Utils.getStringLength(model.getFreetimecontent())){
				Toast.show("闲人宣言为2-50个字");
				return;
			}
			if(50<Utils.getStringLength(model.getFreetimecontent())){
				Toast.show("闲人宣言为2-50个字");
				return;
			}
			Net.request(model, WebApi.User.AddMyFree, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					Toast.show("发布成功");
					NetCache.clearCaches();
					new Thread(){
						public void run() {
							try {
								sleep(1500);
							} catch (InterruptedException e) {
								AppException.handle(e);
							}finally{
								handler.sendEmptyMessage(0);
							}
						};
					}.start();
				}
				@Override
				public void onFailure(Throwable e, int statusCode, String content) {
					super.onFailure(e, statusCode, content);
					if(400 == statusCode){
						Toast.show("发布闲人预告失败");
					}
				}
			});
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_BEGINTIME://开始时间
			if(RESULT_OK == resultCode && null != data){
				beginTime = data.getLongExtra(Extra.SelectedTime, State.Selected_cancle);
				String beginTimeStr = data.getStringExtra(Extra.SelectedTimeStr);
				model.setFreetimestart(beginTimeStr);
				System.out.println("beginTime**"+beginTimeStr);
				beginTv.setText(CalendarUtils.formatYMDHM(beginTimeStr));
			}
			break;
		case REQUEST_CODE_ENDTIME://结束时间
			if(RESULT_OK == resultCode && null != data){
				endTime = data.getLongExtra(Extra.SelectedTime, State.Selected_cancle);
				String endTimeStr = data.getStringExtra(Extra.SelectedTimeStr);
				model.setFreetimeend(endTimeStr);
				endTv.setText(CalendarUtils.formatYMDHM(endTimeStr));
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "发闲人预告";
	}
}
