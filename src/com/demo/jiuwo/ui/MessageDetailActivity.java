package com.demo.jiuwo.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.adapter.MessageListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;
import com.ex.TextViewHtmlParser;

public class MessageDetailActivity extends BaseActivity {
	final static int SET_CONTENT = 2;
	private TextView titletv,contenttv;
	private String message_id,jsonstr;
	private Context c;

	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_message_detail);
			titletv=(TextView)findViewById(R.id.title);
			contenttv=(TextView)findViewById(R.id.content);
			message_id=this.getIntent().getExtras().getString("message_id");
			loaddata();
			this.c=this;
			
	}
	private void loaddata(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				jsonstr=GLOBAL.getUrlPage(GLOBAL.MESSAGE_DETAIL_URL+"message_id/"+message_id+"/");
				sendMessage(SET_CONTENT);
			}
			
		}).start();
	}
		public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
				finish();
				inleft();
	                   return true;       
	        }       
	        return super.onKeyDown(keyCode, event);       
	    } 

		private void sendMessage(int MESSAGE_TYPE){
			// 发送消息  
	        Message msg=handler.obtainMessage();
	        msg.what=MESSAGE_TYPE;
	        handler.sendMessage(msg);
		}
	    private Handler handler = new Handler() {  
	        public void handleMessage(Message msg) { 
	        	switch(msg.what){
	        	case SET_CONTENT:
	                //通知listview更新界面
					String title;
					try {
						title = JSONDecode.getInstance(jsonstr).toJSONObject().getString("title").toString();
						String content=JSONDecode.getInstance(jsonstr).toJSONObject().getString("content").toString();
						titletv.setText(title);
				        // 生成一个支持HTML格式的文本
							//content=new String(content.getBytes("ISO-8859-1"),"UTF-8");
							new TextViewHtmlParser(c)
							.setUrlPrefix("http://www.0yuanwang.com")
							.setTextViewHtml(contenttv,content);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	        		break;
	        	default:
	        		break;
	        	}
	        	//super.handleMessage(msg);  
	        };  
	    };  	


}
