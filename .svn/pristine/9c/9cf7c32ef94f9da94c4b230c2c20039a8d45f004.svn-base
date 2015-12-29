package com.whoyao.net;

import com.whoyao.engine.BasicEngine.CallBack;

/**
 * @author hyh 
 * creat_at：2013-8-1-下午1:18:45
 * @param <T>
 */
public class CallBackHandler<T> extends ResponseHandler {

	protected CallBack<T> callback;
	public CallBackHandler() {
		super();
	}

	public CallBackHandler(boolean isShowProgress) {
		super(isShowProgress);
	}
	public CallBackHandler(boolean isShowProgress, CallBack<T> callback) {
		super(isShowProgress);
		this.callback = callback;
	}

	protected void doCallBack(boolean isSuccess,T data){
		if(null != callback){
			callback.onCallBack(isSuccess,data);
		}
	}
}
