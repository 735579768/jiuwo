package com.demo.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.demo.jiuwo.R;
import com.demo.jiuwo.R.anim;
import com.demo.jiuwo.R.id;
import com.demo.jiuwo.R.layout;
import com.demo.jiuwo.ui.Qidong1Activity;
import com.demo.jiuwo.ui.Qidong3Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends BaseActivity {
	private boolean isqh=false;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//取用户是否有登陆信息
/*		 SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
	     String userinfo=sharedPre.getString("userinfo", "");*/
		super.onCreate(savedInstanceState);
		isFirst();
		init();
		}
	private void isFirst(){
		//判断是不是第一次登陆
		SharedPreferences setting = getSharedPreferences("www.59vip.cn", 0);
		Boolean user_first = setting.getBoolean("FIRST",true);
		if(user_first){//第一次
			setting.edit().putBoolean("FIRST", false).commit();
			setContentView(R.layout.activity_qidong1);
			final ImageView imageview=(ImageView)findViewById(R.id.qidongimg1);
			//两秒后跳转
			Handler handler = new Handler();  
	        handler.postDelayed(new Runnable(){
					        	   public void run() {
						        		  if(!isqh){
						        			  imageview.performClick();
						        		  }
					        	   } 
	        								},animsj);
			imageview.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				//handler.removeCallbacks(runable);
				//imageview.setBackgroundResource(R.drawable.bg_720_yellow);
				try{
				isqh=true;
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Qidong1Activity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.show,R.anim.hidden);    
				finish();
				}catch(Exception e){
					Log.v("mylog",e.getCause().toString());
				}
			}
		});

		 }else{
			 	this.animsj=1000;
				
				setContentView(R.layout.activity_qidong3);
				final ImageView imageview=(ImageView)findViewById(R.id.qidongimg3);
				//两秒后跳转
				Handler handler = new Handler();  
		        handler.postDelayed(new Runnable(){
						        	   public void run() {
						        		imageview.performClick();
						        	   } 
		        								},animsj);
				imageview.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					//handler.removeCallbacks(runable);
					//imageview.setBackgroundResource(R.drawable.bg_720_yellow);
					try{
					Intent intent = new Intent();
					//intent.setClass(MainActivity.this, Qidong3Activity.class);
					intent.setClass(MainActivity.this, Qidong3Activity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.show,R.anim.hidden);    
					finish();
					}catch(Exception e){
						Log.v("mylog",e.getCause().toString());
					}
				}
			});

		}
	}
	/**
	 * 记录设置信息
	 * */
	private void init(){

	}

}
