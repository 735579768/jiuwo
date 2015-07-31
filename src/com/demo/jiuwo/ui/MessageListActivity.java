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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.demo.adapter.MessageListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

public class MessageListActivity extends BaseActivity implements OnPullListener {
	final static int SCANNIN_GREQUEST_CODE = 1;
	final static int UPDATE_MESSAGELISTVIEW = 2;
	final static int RESET_PULLREFRESH = 3;

	private MyListView messagelist;
	private PullRefreshScrollView	mpullScrollView;//下拉刷新
	private MessageListViewAdapter mMessageadapter;   //产品适配器

	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_message_list);
			
			//取下拉刷新对象
			mpullScrollView= (PullRefreshScrollView)findViewById(R.id.scrollid);
			mpullScrollView.setfooterEnabled(false);
			mpullScrollView.setOnPullListener(this);
			//取要显示到下拉容器中的内容视图
			LinearLayout cl= (LinearLayout)mpullScrollView.addBodyLayoutFile(this,R.layout.list_message);
			messagelist= (MyListView)cl.findViewById(R.id.messagelist);
			mMessageadapter=new MessageListViewAdapter(this);
			messagelist.setAdapter(mMessageadapter);
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
					String strUri=GLOBAL.MESSAGE_URL;
					 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
					 params.add(new BasicNameValuePair("num", mMessageadapter.getCount()+""));
					String jsonstr=GLOBAL.postUrl(strUri,params);
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
	            	//setListViewHeightBasedOnChildren(messagelist);
	    			mpullScrollView.setheaderViewReset();//重置头部刷新
	    			mpullScrollView.setfooterViewReset();
	    			//mpullScrollView.fullScroll(ScrollView.FOCUS_DOWN);
	            	int count=mMessageadapter.getCount();
	            	if(count>=5){
	            		mpullScrollView.setfooterEnabled(true);
	            		
	            	}else if(count==0){
	            		mpullScrollView.setfooterLoadOverText("没有数据");
	            	}else{
	            		mpullScrollView.setfooterEnabled(false);
	            	}
	        		break;
	        	case RESET_PULLREFRESH:
	        		mpullScrollView.setheaderViewReset();
	        		mpullScrollView.setfooterViewReset();
	        		mpullScrollView.setfooterEnabled(true);
	        		break;
	        	default:
	        		break;
	        	}
	        	//super.handleMessage(msg);  
	        };  
	    };  	
		//动态设置listview的高度
		public void setListViewHeightBasedOnChildren(ListView listView) {     
	        // 获取ListView对应的Adapter     
			ListAdapter listAdapter = listView.getAdapter();  
		    if (listAdapter == null) { 
		        return; 
		    } 
		    int totalHeight = 0; 
		    for (int i = 0; i < listAdapter.getCount(); i++) { 
		        View listItem = listAdapter.getView(i, null, listView); 
		        listItem.measure(0, 0); 
		        totalHeight += listItem.getMeasuredHeight(); 
		    } 

		    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1)); 
		    params.height=params.height+listView.getPaddingTop()+listView.getPaddingBottom()+300;
		    //((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
		    listView.setLayoutParams(params); 
	    }  

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
			mMessageadapter.removeAllItem();
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
