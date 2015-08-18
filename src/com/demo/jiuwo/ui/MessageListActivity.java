package com.demo.jiuwo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.adapter.MessageListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.core.MSG_TYPE;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

public class MessageListActivity extends BaseActivity implements OnPullListener {
	final static int SCANNIN_GREQUEST_CODE = 1;
	final static int UPDATE_MESSAGELISTVIEW = 2;
	final static int RESET_PULLREFRESH = 3;
	
	protected ImageView goback;
	private ProgressBar progressBar;
	private TextView tvTopTitle;
	private MyListView messagelist;
	private PullRefreshScrollView	mpullScrollView;//下拉刷新
	private MessageListViewAdapter mMessageadapter;   //产品适配器

	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_message_list);
			
			progressBar=(ProgressBar)findViewById(R.id.progressBar);
			//取下拉刷新对象
			mpullScrollView= (PullRefreshScrollView)findViewById(R.id.scrollid);
			//mpullScrollView.setfooterEnabled(false);
			mpullScrollView.setOnPullListener(this);
			//取要显示到下拉容器中的内容视图
			//LinearLayout cl= (LinearLayout)mpullScrollView.addBodyLayoutFile(this,R.layout.list_message);
			messagelist= (MyListView)findViewById(R.id.messagelist);
			

			mMessageadapter=new MessageListViewAdapter(this);
			messagelist.setAdapter(mMessageadapter);
			
			goback= (ImageView)findViewById(R.id.goback);
			tvTopTitle= (TextView)findViewById(R.id.tv_top_title);
			tvTopTitle.setText("九沃动态");
			goback.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}});
			initListener();
			loaddata();
	}
		public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
				finish();
				inleft();
	                   return true;       
	        }       
	        return super.onKeyDown(keyCode, event);       
	    } 
		private void loaddata(){
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendMessage(MSG_TYPE.MSG_SHOW_PROGRESSBAR);
					String strUri=GLOBAL.MESSAGE_URL;
					 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
					 params.add(new BasicNameValuePair("num", mMessageadapter.getCount()+""));
					String jsonstr=GLOBAL.postUrl(strUri,params);
					if(!TextUtils.isEmpty(jsonstr)){
						JSONArray jarr;
						try {
							jarr = JSONDecode.getInstance(jsonstr).toJSONArray();
		
						//选购分类
						//JSONArray jarr1=JSONDecode.getInstance(((JSONObject)jsonarr.opt(0)).getString("child")).toJSONArray();
						for(int i=0;i<jarr.length();i++){
							JSONObject obj=(JSONObject)jarr.opt(i);
							HashMap<String,Object> map = new HashMap<String,Object>();
							String title=obj.getString("title");
							title=title.replace(" ",""); 
							map.put("update_time", obj.getString("update_time"));
							map.put("pic", obj.getString("pic"));
							map.put("descr", obj.getString("descr"));
							map.put("title", title);
							map.put("message_id",obj.getString("message_id"));
							mMessageadapter.addItem(map);
						}
						sendMessage(UPDATE_MESSAGELISTVIEW);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}else{
						sendMessage(MSG_TYPE.MSG_DATA_LOADOVER);
					}
				}
				
			}).start();
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
	        	case UPDATE_MESSAGELISTVIEW:
	                //通知listview更新界面
	                mMessageadapter.notifyDataSetChanged();
	    			mpullScrollView.setheaderViewReset();//重置头部刷新
	    			mpullScrollView.setfooterViewReset();
	            	int count=mMessageadapter.getCount();
	            	if(count>=2){
	            		mpullScrollView.setfooterEnabled(true);
	            		
	            	}else if(count==0){
	            		mpullScrollView.setfooterLoadOverText("请下拉刷新");
	            	}else{
	            		mpullScrollView.setfooterEnabled(false);
	            	}
	            	progressBar.setVisibility(View.GONE);
	        		break;
	        	case MSG_TYPE.MSG_SHOW_PROGRESSBAR:
	        		progressBar.setVisibility(View.VISIBLE);
	        	case MSG_TYPE.MSG_DATA_LOADOVER:
	        		 mpullScrollView.setfooterLoadOverText("加载完毕，共"+mMessageadapter.getCount()+"个");
	        	default:
	        		break;
	        	}
	        	//super.handleMessage(msg);  
	        };  
	    };  	
	    //重置头部下拉刷新
	    public void resetRefresh(){
	        Message msg1=handler.obtainMessage();;
	        msg1.what=RESET_PULLREFRESH;
	        handler.sendMessage(msg1);		    	
	    }

	    /**
	     * 初始化监听器
	     * */
		@Override
		public void refresh() {
			// TODO Auto-generated method stub
			mpullScrollView.setfooterEnabled(false);
			mMessageadapter.removeAllItem();
			mMessageadapter.notifyDataSetChanged();
			loaddata();
		}
		@Override
		public void loadMore() {
			// TODO Auto-generated method stub
			loaddata();
		}
		private void initListener(){
			messagelist.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Map<String,Object> obj=(Map<String,Object>)mMessageadapter.getItem(arg2);
					Intent intent=new Intent();
					intent.putExtra("message_id",obj.get("message_id").toString());
					intent.setClass(MessageListActivity.this,MessageDetailActivity.class);
					startActivity(intent);
				}
				
			});
		}
}
