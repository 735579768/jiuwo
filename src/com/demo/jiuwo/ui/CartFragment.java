package com.demo.jiuwo.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.demo.adapter.GoodsListViewAdapter;
import com.demo.adapter.MemListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.BaseFragment;
import com.demo.core.MyThread;
import com.demo.jiuwo.R;
import com.demo.jsobject.jshome;
import com.ex.UpdateVersion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class CartFragment extends BaseFragment{
	protected WebView webview;
	protected  ProgressBar  progressBar;
	protected ListView listview;
	protected ListView goodslistview;
	private TextView login_btn,register_btn;
	protected String uri="http://app.0yuanwang.com/Member/info";
	private Integer[] imageIDs={R.drawable.rico,R.drawable.rico,R.drawable.rico,R.drawable.rico,R.drawable.rico};
	String [] menutitle={"我的全部订单","未付款订单","已付款订单","检查更新","退出"};
	 private List<Map<String, Object>> listItems; //菜单列表
	 private MemListViewAdapter memviewadapter;   //菜单适配器
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.activity_cart,
				container, false);
		//setContentView(R.layout.activity_proinfo);
		listview= (ListView) view.findViewById(R.id.menulist);	
		login_btn= (TextView) view.findViewById(R.id.login_btn);	
		register_btn= (TextView) view.findViewById(R.id.register_btn);	
		//列表元素添加
		listItems=getListItems();
		memviewadapter = new MemListViewAdapter(getActivity(), listItems); //创建适配器   
	    listview.setAdapter(memviewadapter);  
	    
		//设置点击
	    listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			//arg0:就是你的listview   arg2:点击的item的位置。和你的数组的下标相等。arg3:被电击view的id
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	               // TODO Auto-generated method stub
				//String titleitem=listItems.get(arg2).get("title").toString();	
				Intent intent=new Intent();
				switch(arg2){
				case 0://全部订单

					break;
				case 1://未付订单
					break;
				case 2://已付订单
					break;
				case 3://更新
					new UpdateVersion(getActivity()).checkVersion("http://app.0yuanwang.com/version.xml",false);
					break;
				case 4://退出
					intent.setClass(getActivity(),MainLayoutActivity.class);
					startActivity(intent);
					getActivity().finish();
					inright();					
					break;
				}
			}
             
        });
		return view;
	}
	   /** 
     * 初始化列表信息
     */  
    private List<Map<String, Object>> getListItems() {  
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();  
        for(int i = 0; i < menutitle.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();               //图片资源   
            map.put("image", imageIDs[i]); 
            map.put("title", menutitle[i]);              //物品标题   
            listItems.add(map);  
        }     
        return listItems;  
    }  
    /**
     * 初始化监听器
     * */
    private void initListeren(){
    	login_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	register_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    }
	private Handler handler=new Handler(){
	    public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case 1:
        		break;
        	default:
        		break;
        	}
        	super.handleMessage(msg);  
        };  
	};
	

}
