package com.whoyao.activity;

import com.whoyao.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author hyh 
 * creat_at：2013-8-12-下午2:51:09
 */
public class LicenceActivity extends BasicActivity implements OnClickListener {
	private WebView licenceWv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_licence);
		findViewById(R.id.page_btn_back).setOnClickListener(this);
		licenceWv = (WebView)findViewById(R.id.licence_webview);
		WebSettings settings = licenceWv.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		settings.setJavaScriptEnabled(true);
		licenceWv.loadUrl(" file:///android_asset/licence.html "); 
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
