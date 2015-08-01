package com.demo.jiuwo.ui;

import com.demo.core.BaseActivity;
import com.demo.jiuwo.R;
import com.demo.jsobject.jsgoods;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class LoadurlActivity extends BaseActivity {
	protected WebView webview;
	protected  ProgressBar  progressBar;
	protected String uri;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		Intent intent=getIntent();
		String goods_id=intent.getStringExtra("goods_id").toString();
		uri="http://app.0yuanwang.com/Api/getGoodsDesc/goods_id/"+goods_id;
		webview = (WebView) findViewById(R.id.webv);
		try {  
		webview.getSettings().setJavaScriptEnabled(true);
		 webview.addJavascriptInterface(new jsgoods(LoadurlActivity.this),"jw");
		 final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		 webview.setWebViewClient(new WebViewClient(){
			 public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                view.loadUrl(url);       
                return true;  
			 }
				//网页加载开始时调用，显示加载提示旋转进度条
	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                // TODO Auto-generated method stub
	                super.onPageStarted(view, url, favicon);
	                progressBar.setVisibility(android.view.View.VISIBLE);
//	                Toast.makeText(ElecHall.this, "onPageStarted", 2).show();
	            }

	//网页加载完成时调用，隐藏加载提示旋转进度条
	            @Override
	            public void onPageFinished(WebView view, String url) {
	                // TODO Auto-generated method stub
	                super.onPageFinished(view, url);
	                progressBar.setVisibility(android.view.View.GONE);
//	                Toast.makeText(ElecHall.this, "onPageFinished", 2).show();
	            }
//	//网页加载失败时调用，隐藏加载提示旋转进度条
	            @Override
	            public void onReceivedError(WebView view, int errorCode,
	                    String description, String failingUrl) {
	                // TODO Auto-generated method stub
	                super.onReceivedError(view, errorCode, description, failingUrl);
	                progressBar.setVisibility(android.view.View.GONE);
	                String str="file:///android_asset/html/err.html";
	                webview.loadUrl(str);
	                webview.setInitialScale(30);
//	                Toast.makeText(ElecHall.this, "onReceivedError", 2).show();
	            }
            });
		 webview.loadUrl(uri);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			inleft();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
