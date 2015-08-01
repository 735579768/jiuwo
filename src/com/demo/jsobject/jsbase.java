package com.demo.jsobject;

import com.demo.jiuwo.ui.GoodsListActivity;
import com.demo.jiuwo.ui.LoadurlActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class jsbase{
	private Context mContext;
	public jsbase(Context c){
		this.mContext=c;
	}
	//提示信息
	public void toast(String str){
		Toast.makeText(this.mContext.getApplicationContext(), str,
				Toast.LENGTH_SHORT).show();			
	}

	//产品详情页
	public void loadurl(String str){
		try{
		Intent intent=new Intent();
		intent.putExtra("url", str);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setClass(this.mContext,LoadurlActivity.class);
		this.mContext.startActivity(intent);
		((Activity) mContext).finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//产品详情页
	public void proinfo(){
		try{
		Intent intent=new Intent();
		//intent.putExtra("url", str);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setClass(this.mContext,GoodsListActivity.class);
		this.mContext.startActivity(intent);
		//this.mContext.finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
