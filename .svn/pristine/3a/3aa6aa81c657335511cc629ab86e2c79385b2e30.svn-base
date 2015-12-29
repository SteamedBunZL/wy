package com.whoyao.activity;

import com.whoyao.R;
import com.whoyao.engine.BasicEngine.CallBack;
import com.whoyao.engine.user.MyinfoManager;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.model.CheckAccountResponseModel;
import com.whoyao.ui.Toast;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 验证邮箱
 * @author hyh creat_at：2013-8-5-下午6:05:57
 */
public class MyVerifyEmailActivity extends BasicActivity implements OnClickListener {
	private EditText emailEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_verify_email);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		findViewById(R.id.verifyemail_btn_sendemail).setOnClickListener(this);
		emailEt = (EditText) findViewById(R.id.verifyemail_et_email);
		if(MyinfoManager.getUserInfo().isEmailV()){
			((TextView)findViewById(R.id.page_tv_title)).setText("修改邮箱");
		}else if(!TextUtils.isEmpty(MyinfoManager.getUserInfo().getUserEmail())){
			emailEt.setText(MyinfoManager.getUserInfo().getUserEmail());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_btn_back:
			finish();
			break;
		case R.id.verifyemail_btn_sendemail:
			String email = emailEt.getText().toString();
			if (UserEngine.checkEmailFormat(email)) {
				if(email.equals(MyinfoManager.getUserInfo().getUserEmail())){
					if(MyinfoManager.getUserInfo().isEmailV()){
						Toast.show(R.string.warn_email_duplicate);
					}else{
						MyinfoManager.getManager().verifyEmail(email);
					}
				}else{
					UserEngine.checkAccount(null, email, null, new CallBack<CheckAccountResponseModel>(email){
						@Override
						public void onCallBack(boolean isSuccess, CheckAccountResponseModel data) {
							if(isSuccess && null != data){
								if(data.isEmailRegistered()){
									Toast.show(R.string.warn_email_registered);
								}else{
									MyinfoManager.getManager().verifyEmail((String)params[0]);
								}
							}
						}
					});
//			CheckAccountTModel checkAccountModel = new CheckAccountTModel(null, email, null);
//			Net.request(checkAccountModel, WebApi.User.CheckAccount, new ResponseHandler(true,email){
//				@Override
//				public void onSuccess(String content) {
//					CheckAccountResponseModel model = JSON.getObject(content, CheckAccountResponseModel.class);
//					if(!model.isEmailRegistered()){
//					}else{
//						Toast.show("该邮箱已与其他账号绑定!");
//					}
//				}
//			});
					
				}
			}
			break;
		default:
			break;
		}
	}
		
	@Override
	public String toString() {
		return "验证邮箱";
	}
}
