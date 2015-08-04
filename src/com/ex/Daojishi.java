package com.ex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class Daojishi {
	private Context context;
	private String daojishiTime;
    private int recLen = 11;  
    private long hours;
    private long minutes;
    private long seconds;
    private TextView txtView;  
	private long diff;
	private long days;
	public Daojishi(Context c,TextView tv,String strTime){
		this.context=c;
		this.txtView=tv;
		this.daojishiTime=strTime;
	    getTime();
        Message message = handler.obtainMessage(1);     // Message  
        handler.sendMessageDelayed(message, 1000);  
	}
	   final Handler handler = new Handler(){  
		   
	        public void handleMessage(Message msg){         // handle message  
	            switch (msg.what) {  
	            case 1:  
					diff = diff - 1000;
					getShowTime();
					if(diff > 0){
						Message message = handler.obtainMessage(1);  
		                handler.sendMessageDelayed(message, 1000);
		                }else{  
		                	//txtView.setVisibility(View.GONE); 
		                	txtView.setText("已结束");
		                	txtView.invalidate();
		                }
					break;
					default:
						break;
	            }
	            super.handleMessage(msg);  
	        }  
	    };  
	    
		/**
		 * 得到时间差
		 */
		private  void getTime(){

	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
	    	String date = sDateFormat.format(new Date());    
	    	System.out.println("现在时间："+date);
	    	try
	    	{
	    		//Date d1 = df.parse("2015-08-05 12:00:00");
	    		Date d1 = df.parse(this.daojishiTime);
	    		Date d2 = df.parse(date);
	    		diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
	    		days = diff / (1000 * 60 * 60 * 24);
	    		hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	    		minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
	    		seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000* 60))/(1000);
	    		txtView.setText(""+days+"天"+hours+"小时"+minutes+"分"+seconds+"秒");
	    		System.out.println("现在时间：diff "+diff);
	    		System.out.println(""+days+"天"+hours+"小时"+minutes+"分"+seconds+"秒");
	    	}
	    	catch (Exception e)
	    	{
	    	}
	    }
		
		/**
		 * 获得要显示的时间
		 */
		private void getShowTime(){
			days = diff / (1000 * 60 * 60 * 24);
			hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
			minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
			seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000* 60))/(1000);
			txtView.setText(""+days+"天"+hours+"小时"+minutes+"分"+seconds+"秒");
		}
}
