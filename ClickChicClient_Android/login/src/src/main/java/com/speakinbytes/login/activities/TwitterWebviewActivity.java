package com.speakinbytes.login.activities;

import  com.speakinbytes.login.utils.Constants;
import  com.speakinbytes.login.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TwitterWebviewActivity extends Activity {

	Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_twitter_webview);
		mIntent = getIntent();
		String url = (String) mIntent.getExtras().get("URL");
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				if (url.contains(Constants.TWITTER_CALLBACK_URL)) {
					Uri uri = Uri.parse(url);
					String oauthVerifier = uri
							.getQueryParameter("oauth_verifier");
					mIntent.putExtra("oauth_verifier", oauthVerifier);
					setResult(RESULT_OK, mIntent);
					finish();
					return true;
				}
				return false;
			}
		});
		webView.loadUrl(url);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//TrackingHelper.startActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//TrackingHelper.stopActivity();
	}

}