package com.demo.core;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Counter extends CountDownTimer {
	private static final int SECONDS = 60;    //秒数
	private static final int MINUTES = 60 * 60;    //小时
	 
	private long first = 0, twice = 0, third = 0;
	private long mtmp = 0, mtmp2 = 0;
	private TextView tv;
    public Counter(long millisInFuture, long countDownInterval,TextView tv) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated method stub
            this.tv=tv;
    }

    @Override
    public void onFinish() {
            // TODO Auto-generated method stub
             
    }

    @Override
    public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
        //获取当前时间总秒数
        //first = millisUntilFinished / 1000;
        first=millisUntilFinished;
        if(first < SECONDS) {    //小于一分钟 只显示秒
                settext("00:00:" + (first<10?"0"+first:first));
        }else if(first < MINUTES) {    //大于或等于一分钟，但小于一小时，显示分钟
                twice = first % 60;    //将秒转为分钟取余，余数为秒
                mtmp = first/60;    //将秒数转为分钟
                if(twice==0) {
                        settext("00:" + (mtmp<10?"0"+mtmp:mtmp) + ":00");    //只显示分钟
                } else {
                        settext("00:" + (mtmp<10?"0"+mtmp:mtmp) + ":" + (twice<10?"0"+twice:twice));    //显示分钟和秒 
                }
        }else {
                twice = first % 3600;    //twice为余数 如果为0则小时为整数
                mtmp = first / 3600;
                if(twice == 0) {
                        //只剩下小时
                        settext("0" + first/3600 + ":00:00");
                }else {
                        if(twice < SECONDS) {    //twice小于60 为秒
                                settext((mtmp<10?"0"+mtmp:mtmp) + ":00:" + (twice<10?"0"+twice:twice));    //显示小时和秒
                        }else {
                                third = twice%60;    //third为0则剩下分钟 否则还有秒
                                mtmp2 = twice/60;
                                if(third == 0) {
                                        settext((mtmp<10?"0"+mtmp:mtmp) + ":" + (mtmp2<10?"0"+mtmp2:mtmp2) + ":00");
                                }else {
                                        settext((mtmp<10?"0"+mtmp:mtmp) + ":" + (mtmp2<10?"0"+mtmp2:mtmp2) + ":" + third);    //还有秒
                                }
                        }
                }
        }       
    }
    private void settext(String str){
    	tv.setText(str);
    }

}