package com.demo.jiuwo.ui;

import com.demo.core.BaseActivity;
import com.demo.jiuwo.R;
import com.demo.jiuwo.R.anim;
import com.demo.jiuwo.R.id;
import com.demo.jiuwo.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Qidong3Activity extends BaseActivity {
	private boolean isqh=false;//判断是否已经切换
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qidong3);
		final ImageView imageview=(ImageView)findViewById(R.id.qidongimg3);
		//两秒后跳转
		Handler handler = new Handler();  
        handler.postDelayed(new Runnable(){
				        	   public void run() {
				        		  if(!isqh){
				        			  imageview.performClick();
				        		  }
				        	   } 
        								},0);
		imageview.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v) {
			//imageview.setBackgroundResource(R.drawable.bg_720_yellow);
			try{
			isqh=true;
			Intent intent = new Intent();
			intent.setClass(Qidong3Activity.this,MainLayoutActivity.class);
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
