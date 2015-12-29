package com.whoyao.engine.user;

import com.whoyao.AppContext;
import com.whoyao.activity.RetPhoneCheckCodeActivity;
import com.whoyao.net.ResponseHandler;
@Deprecated
public class VerifyCodeHandler extends ResponseHandler{

	@Override
	public void onSuccess(int statusCode, String content) {
		AppContext.startAct(RetPhoneCheckCodeActivity.class);
		super.onSuccess(statusCode, content);
	}
	
}