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

import com.demo.adapter.CartListViewAdapter;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.core.LoginVerifyActivity;
import com.demo.core.LoginVerifyFragment;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartActivity extends LoginVerifyActivity implements OnPullListener{
	final static int UPDATE_CART = 1;
	final static int RESET_HEADER = 2;
	final static int RESET_FOOTER = 3;
	private PullRefreshScrollView mPullRefresh;
	protected MyListView cartlistview;
	protected String uri=GLOBAL.PRO_INFO;
	protected ImageView goback;
	private TextView tvTopTitle;
	 private CartListViewAdapter cartadapter;   //菜单适配器
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_cart_list);
			mPullRefresh=(PullRefreshScrollView)findViewById(R.id.scrollid);
			mPullRefresh.setfooterEnabled(false);
			mPullRefresh.setOnPullListener(this);
			goback= (ImageView)findViewById(R.id.goback);
			tvTopTitle= (TextView)findViewById(R.id.tv_top_title);
			tvTopTitle.setText("购物车");
			goback.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}});
		
			cartlistview= (MyListView) findViewById(R.id.cartlist);
			cartadapter = new CartListViewAdapter(this); //创建适配器 
			cartlistview.setAdapter(cartadapter);
			loaddata();
		}
/*	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view= inflater.inflate(R.layout.activity_cart_list,
				container, false);
		mPullRefresh=(PullRefreshScrollView) view.findViewById(R.id.scrollid);
		mPullRefresh.setfooterEnabled(false);
		mPullRefresh.setOnPullListener(this);
		//取要显示到下拉容器中的内容视图
		LinearLayout cl= (LinearLayout)mPullRefresh.addBodyLayoutFile(getActivity(),R.layout.list_cart);
	
		cartlistview= (MyListView) cl.findViewById(R.id.cartlist);
		cartadapter = new CartListViewAdapter(getActivity()); //创建适配器 
		cartlistview.setAdapter(cartadapter);
		loaddata();
		return view;
	}*/
	private void loaddata(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count=cartadapter.getCount();
				 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
				  params.add(new BasicNameValuePair("userinfo", userinfo));
				  params.add(new BasicNameValuePair("num", count+""));
				String jsonstr=GLOBAL.postUrl(GLOBAL.CART_INDEX_LIST,params);
				try {
				JSONArray jarr = JSONDecode.getInstance(jsonstr).toJSONArray();		
					//选购分类
					//JSONArray jarr1=JSONDecode.getInstance(((JSONObject)jsonarr.opt(0)).getString("child")).toJSONArray();
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
						cartadapter.addItem(map);
					}
	                // 发送消息  
	                Message msg=handler.obtainMessage();
	                msg.what=UPDATE_CART;
	                handler.sendMessage(msg);
	                
					} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}
			
		}).start();


	}

	private Handler handler=new Handler(){
	    public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case UPDATE_CART:
        		cartadapter.notifyDataSetChanged();
        		mPullRefresh.setheaderViewReset();
        		mPullRefresh.setfooterViewReset();
        		if(cartadapter.getCount()>=5){
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
        	default:
        		break;
        	}
        	super.handleMessage(msg);  
        };  
	};
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		cartadapter.removeAllItem();
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
