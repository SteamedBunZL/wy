package com.whoyao.topic;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.activity.BasicActivity;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.model.TopicRemarkAddTModel;
import com.whoyao.model.TopicRemarkModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.CalendarUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 话题回复
 * @author hyh 
 * creat_at：2013-8-12-下午5:30:02
 */
public class TopicRemarkAddActivity extends BasicActivity implements OnClickListener {
	private EditText etCcontent;
	private TextView tvNum;
	private int id;
	private static final int ContentMaxLength = 250;
	private TopicRemarkAddTModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getIntExtra(Extra.SelectedID, 0);
		String title = intent.getStringExtra(Extra.Title);
		if(id == 0 || TextUtils.isEmpty(title)){
			finish();
			return;
		}
		model = new TopicRemarkAddTModel();
		model.setId(id);
		setContentView(R.layout.activity_topic_remark_add);
		TextView tvTitle = (TextView)findViewById(R.id.page_tv_title);
		tvTitle.setText("回复："+title);
		etCcontent = (EditText)findViewById(R.id.topic_remark_et_content);
		tvNum = (TextView)findViewById(R.id.topic_remark_tv_num);
		etCcontent.addTextChangedListener(new TextNumWatcher(tvNum, ContentMaxLength));
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.topic_remark_btn_enter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.topic_remark_btn_enter:
			String contentStr = etCcontent.getText().toString().trim();
			if(TextUtils.isEmpty(contentStr)){
				Toast.show("请输入内容");
			}
			if (contentStr.length()>ContentMaxLength) {
				Toast.show("回复内容在250个字以内");
			}else {
				model.setContent(contentStr);
				Net.request(Net.getRequestParamsWithoutNull(model), WebApi.Topic.AddRemark, new ResponseHandler(true){
					@Override
					public void onSuccess(String content) {
						setResult(RESULT_OK, null);
						finish();
					}
				});
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "话题回复";
	}
}
