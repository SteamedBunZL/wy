package com.whoyao.activity;

import com.whoyao.Const.Extra;
import com.whoyao.R;
import com.whoyao.WebApi;
import com.whoyao.Const.Type;
import com.whoyao.common.TextNumWatcher;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.client.ClientEngine;
import com.whoyao.model.RelationManageTModel;
import com.whoyao.model.ResultModel;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;
import com.whoyao.ui.Toast;
import com.whoyao.utils.JSON;
import com.whoyao.utils.Utils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 加好友
 * @author hyh 
 * creat_at：2013-8-12-下午5:30:02
 */
public class UserAddFriendActivity extends BasicActivity implements OnClickListener {
	private EditText etContent;
	private TextView tvNum;
	private int friendUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friendUserID = getIntent().getIntExtra(Extra.SelectedID, 0);
		if(0 == friendUserID){
			finish();
			return;
		}
		setContentView(R.layout.activity_user_add_friend);
		etContent = (EditText)findViewById(R.id.user_addfriend_et_content);
		tvNum = (TextView)findViewById(R.id.user_addfriend_tv_num);
		etContent.addTextChangedListener(new TextNumWatcher(tvNum, 50));
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.user_addfriend_btn_enter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.user_addfriend_btn_enter:
			String contentStr = etContent.getText().toString();
			if(50 <Utils.getStringLength(contentStr)){
				Toast.show("字数在50个字以内");
				return;
			}
			RelationManageTModel addFriendModel = new RelationManageTModel(friendUserID, Type.User_Add);
			addFriendModel.setRemarkcontent(contentStr);
			Net.request(addFriendModel, WebApi.User.FriendManage, new ResponseHandler(true){
				@Override
				public void onSuccess(String content) {
					ResultModel result = JSON.getObject(content, ResultModel.class);
					if(result != null && result.isSuccess()){
						Toast.show("验证发送成功");
						finish();
					}else{
						Toast.show("请求失败");
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "加好友";
	}
}
