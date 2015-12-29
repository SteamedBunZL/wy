package com.whoyao.net;

import org.apache.http.Header;

import com.whoyao.Const;
import com.whoyao.R;
import com.whoyao.engine.user.UserEngine;
import com.whoyao.ui.LoadingDialog;
import com.whoyao.ui.Toast;
import com.whoyao.utils.L;

/**
 * @author hyh 
 * creat_at：2013-7-26-上午9:10:53
 */
public class ResponseHandlerWithoutDialog extends HaveCacheResponseHandler {

	
	protected Object[] params;
	public ResponseHandlerWithoutDialog() {
		this(false);
	}
	public ResponseHandlerWithoutDialog(boolean isShowProgress) {
		super();
	}
	public ResponseHandlerWithoutDialog(boolean isShowProgress,Object... params) {
		super();
		this.params = params;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	

	@Override
	public void onFinish() {
		super.onFinish();
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		L.i(Const.AppName+"-Net","Response: "+content);
		super.onSuccess(statusCode, headers, content);
	}

	@Override
	public void onFailure(Throwable e, int statusCode, String content) {
		L.e(Const.AppName+"-Net","Error: "+e.getClass().getSimpleName()+"  Code="+statusCode+" , Message="+e.getMessage()+"\nResponse:");
		if(null != content){
			L.w(Const.AppName+"-Net",content);
		}
		switch (statusCode) {
		case -1:
			Toast.show(R.string.warn_network_unavailable);
			break;
		case 0:
			Toast.show(R.string.warn_netrequest_failure);
			break;
//		case 400:
//			Toast.show("400");
//			break;
		case 401:
//			Toast.show("请登录!");
			UserEngine.relogin();
			break;
//		case 406:
//			Toast.show("406");
//			break;
		case 500:
			Toast.show(R.string.warn_netrequest_failure);
			break;
		default:
			break;
		}
	}
}