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

import com.demo.adapter.MiaoshaListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.core.MSG_TYPE;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class MiaoshaActivity extends BaseActivity implements OnPullListener{
	final static int RESET_HEADER = 2;
	final static int RESET_FOOTER = 3;
	private PullRefreshScrollView mPullRefresh;
	protected MyListView miaoshalistview;
	protected String uri=GLOBAL.PRO_INFO;

	 private List<Map<String, Object>> listItems; //菜单列表
	 private MiaoshaListViewAdapter miaoshaadapter;   //菜单适配器
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_miaosha_list);
			mPullRefresh=(PullRefreshScrollView)findViewById(R.id.scrollid);
			mPullRefresh.setfooterEnabled(false);
			mPullRefresh.setOnPullListener(this);
			//取要显示到下拉容器中的内容视图
			LinearLayout cl= (LinearLayout)mPullRefresh.addBodyLayoutFile(this,R.layout.list_miaosha);
		
			miaoshalistview= (MyListView) cl.findViewById(R.id.miaoshalist);
			miaoshaadapter = new MiaoshaListViewAdapter(this); //创建适配器 
			miaoshalistview.setAdapter(miaoshaadapter);
			loaddata();
		}
	private void loaddata(){
	//	GLSurfaceView.queueEvent(new Runnable(){});
		//GLSurfaceView.queueEvent(Runnable r);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count=miaoshaadapter.getCount();
				 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
				  //params.add(new BasicNameValuePair("userinfo", userinfo));
				  params.add(new BasicNameValuePair("num", count+""));
				String jsonstr=GLOBAL.postUrl(GLOBAL.GOODS_MIAOSHA_LISTS_URL,params);
				try {
				JSONArray jarr = JSONDecode.getInstance(jsonstr).toJSONArray();		
					//选购分类
					//JSONArray jarr1=JSONDecode.getInstance(((JSONObject)jsonarr.opt(0)).getString("child")).toJSONArray();
				if(!TextUtils.isEmpty(jsonstr)){
				for(int i=0;i<jarr.length();i++){
						JSONObject obj=(JSONObject)jarr.opt(i);
						HashMap<String,Object> map = new HashMap<String,Object>();
						String title=obj.getString("title");
						title=title.replace(" ",""); 
						map.put("pic", obj.getString("pic"));
						map.put("price", obj.getString("price"));
						map.put("title", title);
						map.put("category_id",obj.getString("category_id"));
						map.put("goods_id",obj.getString("goods_id"));
						miaoshaadapter.addItem(map);
					}
	                // 发送消息  
	                sendMessage(MSG_TYPE.MSG_UPDATE_MIAOSHA);
				}else{
					sendMessage(MSG_TYPE.MSG_LOADOVER_MIAOSHA);
				}
	                
					} catch (JSONException e) {
					// TODO Auto-generated catch block
						sendMessage(MSG_TYPE.MSG_LOADOVER_MIAOSHA);
					e.printStackTrace();
				}					
			}
			
		}).start();


	}
	private void sendMessage(int msg_type){
        Message msg=handler.obtainMessage();
        msg.what=msg_type;
        handler.sendMessage(msg);
	}
	private Handler handler=new Handler(){
	    public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case MSG_TYPE.MSG_UPDATE_MIAOSHA:
        		miaoshaadapter.notifyDataSetChanged();
        		mPullRefresh.setheaderViewReset();
        		mPullRefresh.setfooterViewReset();
        		if(miaoshaadapter.getCount()>=5){
        			mPullRefresh.setfooterEnabled(true);
        		}else{
        			mPullRefresh.setfooterEnabled(false);
        		}
        		break;
        	case RESET_HEADER:
        		mPullRefresh.setheaderViewReset();
        		break;
        	case RESET_FOOTER:
        		mPullRefresh.setfooterViewReset();
        		break;
        	case MSG_TYPE.MSG_LOADOVER_MIAOSHA :
        		mPullRefresh.setfooterLoadOverText("加载完毕,共"+miaoshaadapter.getCount()+"个");
        	default:
        		break;
        	}
        	super.handleMessage(msg);  
        };  
	};
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		miaoshaadapter.removeAllItem();
		loaddata();
	}
	@Override
	public void loadMore() {
		loaddata();
		// TODO Auto-generated method stub
		
	} 
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
			finish();
			inleft();
            return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }  
}
