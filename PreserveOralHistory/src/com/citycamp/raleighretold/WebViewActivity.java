package com.citycamp.raleighretold;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity{
	
	private WebView webView;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		// Load the map from our webserver. XML parsing now takes place on the server side. Yay!
		webView.loadUrl("http://www.raleighretold.org/mapserver/maptest");
 
	}

}
