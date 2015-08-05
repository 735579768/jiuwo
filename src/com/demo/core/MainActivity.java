package com.demo.core;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.demo.jiuwo.R;
import com.demo.jiuwo.ui.Qidong1Activity;
import com.demo.jiuwo.ui.Qidong3Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,"TfscjiUBW9nvrMr0Xr9mQpSz");
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
