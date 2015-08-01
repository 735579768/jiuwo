package com.demo.jiuwo.ui;

import com.demo.core.BaseActivity;
import com.demo.jiuwo.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class Qidong2Activity extends BaseActivity {
	private boolean isqh=false;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qidong2);
		final ImageView imageview=(ImageView)findViewById(R.id.qidongimg2);
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
			//imageview.setBackgroundResource(R.drawable.bg_720_yellow);
			try{
				isqh=true;
			Intent intent = new Intent();
			intent.setClass(Qidong2Activity.this, Qidong3Activity.class);
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
