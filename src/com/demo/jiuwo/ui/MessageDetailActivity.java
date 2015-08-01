package com.demo.jiuwo.ui;

import java.net.URL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.KeyEvent;
import android.webkit.WebView;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.jiuwo.R;

public class MessageDetailActivity extends BaseActivity {
	final static int SET_CONTENT = 2;
	private WebView contenttv;
	private String message_id,jsonstr;
	private Context c;

	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_message_detail);
			//titletv=(TextView)findViewById(R.id.title);
			contenttv=(WebView)findViewById(R.id.content);
			message_id=this.getIntent().getExtras().getString("message_id");
			contenttv.loadUrl("http://app.0yuanwang.com/Api/getMessageDetail/message_id/"+message_id+"/");
			//loaddata();
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

/*						title = JSONDecode.getInstance(jsonstr).toJSONObject().getString("title").toString();
						String content=JSONDecode.getInstance(jsonstr).toJSONObject().getString("content").toString();
						titletv.setText(title);*/
				        // 生成一个支持HTML格式的文本
							//content=new String(content.getBytes("ISO-8859-1"),"UTF-8");
						
						/*							contenttv.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动  
						contenttv.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页  
						contenttv.setText(Html.fromHtml(content, imgGetter, null));  
						new TextViewHtmlParser(c)
							.setUrlPrefix("http://www.0yuanwang.com")
							.setTextViewHtml(contenttv,content);*/



	        		break;
	        	default:
	        		break;
	        	}
	        	//super.handleMessage(msg);  
	        };  
	    };  	
	    //这里面的resource就是fromhtml函数的第一个参数里面的含有的url  
	    ImageGetter imgGetter = new Html.ImageGetter() {  
	        public Drawable getDrawable(String source) {  
	           // Log.i("RG", "source---?>>>" + source);  
	            Drawable drawable = null;  
	            URL url;  
	            try {  
	                url = new URL("http://app.0yuanwang.com"+source);  
	                //Log.i("RG", "url---?>>>" + url);  
	                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	                return null;  
	            }  
	            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
	                    drawable.getIntrinsicHeight());  
	           // Log.i("RG", "url---?>>>" + url);  
	            return drawable;  
	        }  
	    };  

}
