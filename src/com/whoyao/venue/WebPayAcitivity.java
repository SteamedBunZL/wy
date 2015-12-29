package com.whoyao.venue;

import org.apache.http.Header;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whoyao.AppContext;
import com.whoyao.R;
import com.whoyao.activity.BasicActivity;
import com.whoyao.net.Net;
import com.whoyao.net.ResponseHandler;

public class WebPayAcitivity extends BasicActivity {

	private WebView webView;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_webpay);
		mHandler = new Handler();
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		System.out.println(url);
		webView = (WebView) findViewById(R.id.wb_venue_webpay);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// @Override
			// public void onPageFinished(WebView view, String url) {
			// String taskUrl =
			// "http://d.whoyao.com/Venue/AliPay/Mobile_Return";
			// if (TextUtils.equals(url, taskUrl)) {
			// Net.request2(null, taskUrl, new ResponseHandler() {
			//
			// @Override
			// public void onSuccess(String content) {
			// if (TextUtils.equals(content, "success")) {
			// // 跳转到支付成功页面
			// AppContext.startAct(PaySuccessActivity.class);
			// finish();
			// } else if (TextUtils.equals(content, "fail")) {
			// // 跳转到支付失败页面
			// AppContext.startAct(PayFailActivity.class);
			// }
			//
			// }
			//
			// @Override
			// public void onFailure(Throwable e, int statusCode,
			// String content) {
			// super.onFailure(e, statusCode, content);
			// }
			//
			// }
			// );
			// }
			// }

		});
		webView.loadUrl(url);

		webView.addJavascriptInterface(new Object() {
			public void Success() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						finish();
						AppContext.startAct(PaySuccessActivity.class);
					}
				});
			}

			public void Fail() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						finish();
						AppContext.startAct(PayFailActivity.class);
					}
				});
			}
		}, "OrderResult");

		// webView.addJavascriptInterface(new Object(){
		// public void clickOnAndroid(){
		// mHandler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// finish();
		// AppContext.startAct(PaySuccessActivity.class);
		// }
		// });
		// }
		// }, "venueordersuccess");
		// webView.addJavascriptInterface(new Object(){
		// public void clickOnAndroid(){
		// mHandler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// finish();
		// AppContext.startAct(PayFailActivity.class);
		// }
		// });
		// }
		// }, "venueorderfail");
	}

	@Override
	protected boolean onBack() {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
