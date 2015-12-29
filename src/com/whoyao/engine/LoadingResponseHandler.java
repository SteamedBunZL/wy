package com.whoyao.engine;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.whoyao.AppContext;
import com.whoyao.ui.LoadingDialog;

/**
 * @author hyh 
 * creat_at：2013-7-19-下午12:30:23
 */
@Deprecated
public class LoadingResponseHandler extends AsyncHttpResponseHandler {

	protected String message;
	private LoadingDialog dialog;

	public LoadingResponseHandler() {

	}
	public LoadingResponseHandler(String message) {
		this.message = message;
	}
	public LoadingResponseHandler(int message) {
		this.message = AppContext.getRes().getString(message);
	}

	@Override
	public void onFinish() {
		dialog.dismiss();
		super.onFinish();
	}

	@Override
	public void onStart() {
		dialog = LoadingDialog.getDialog();
		dialog.setMessage(message);
		dialog.show();
		super.onStart();
	}

}
