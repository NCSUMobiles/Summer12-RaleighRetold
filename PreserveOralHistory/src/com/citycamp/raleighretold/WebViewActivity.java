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
		//webView.loadUrl("http://www.raleighnc.gov/content/PlanCurrent/Documents/DevelopmentPlansReview/PlansInReview/2011/PlansSubmittalMapsByType/SitePlan/SP-001-11.pdf");
		webView.loadUrl("http://onlinedevcenter.raleighnc.gov/DevServices/planning/DevPlansDetails.aspx?FileNum=S-8-2012");
 
	}

}
