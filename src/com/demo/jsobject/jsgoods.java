package com.demo.jsobject;

import com.demo.jiuwo.ui.GoodsActivity;
import com.demo.jiuwo.ui.LoadurlActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class jsgoods extends jsbase {
	private Context mContext;
	public jsgoods(Context c) {
		super(c);
		this.mContext=c;
		// TODO Auto-generated constructor stub
	}
	//产品详情页
	public void loadurl(String str){
		try{
		Intent intent=new Intent();
		intent.putExtra("url", str);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setClass(this.mContext,GoodsActivity.class);
		this.mContext.startActivity(intent);
		//this.mContext.finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//提示信息
	public void goback(){
/*		Toast.makeText(this.mContext.getApplicationContext(), str,
				Toast.LENGTH_SHORT).show();*/	
		((Activity) mContext).finish();
	}
}
