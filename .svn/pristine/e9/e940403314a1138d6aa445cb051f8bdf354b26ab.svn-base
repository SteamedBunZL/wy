package com.whoyao.net;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author hyh creat_at：2013-7-31-上午8:57:24
 */
public class HaveCacheResponseHandler extends AsyncHttpResponseHandler {

	private String requestStr;
	private String webApiName;

	
	
	public HaveCacheResponseHandler() {
		super();
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		super.handleFailureMessage(e, responseBody);// 触发onFailure(e,
													// responseBody);
		if (e instanceof HttpResponseException) {
			int statusCode = ((HttpResponseException) e).getStatusCode();
			onFailure(e, statusCode, responseBody);
		} else {
			onFailure(e, 0, responseBody);
		}
	}

	@Override
	protected void handleSuccessMessage(int statusCode, Header[] headers, String responseBody) {
		NetCache.addCache(webApiName, requestStr, statusCode, headers, responseBody);// 根据requestStr,进行缓存
		super.handleSuccessMessage(statusCode, headers, responseBody);
	}
	/**
	 * 
	 * @param statusCode 不是HttpResponseException时为0
	 * @param content
	 */
	protected void onFailure(Throwable e, int statusCode, String content) {

	}

	public void setRequestInfo(String webApi, String request) {
		requestStr = request;
		webApiName = webApi;
	}
}
