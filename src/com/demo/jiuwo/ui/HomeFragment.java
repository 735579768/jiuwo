package com.demo.jiuwo.ui;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.adapter.GridViewAdapter;
import com.demo.adapter.HomeGoodsListViewAdapter;
import com.demo.core.BaseFragment;
import com.demo.core.JSONDecode;
import com.demo.core.ScanCodeActivity;
import com.demo.core.jiekou.MyProgressBar;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends BaseFragment implements MyProgressBar,OnPullListener {
	final static int SCANNIN_GREQUEST_CODE = 1;
	final static int UPDATE_GOODSLISTVIEW = 12;
	final static int UPDATE_GRIDVIEW_A = 2;
	final static int UPDATE_GRIDVIEW_B = 11;
	final static int UPDATE_A_GOODS = 3;
	final static int SHOW_LOADING = 4;
	final static int GONE_LOADING = 5;
	private PullRefreshScrollView	mpullScrollView;
	private LinearLayout	contentLayout;
	final static int SET_GOODSLIST_ADAPTER=10;//设置产品列表适配器
	private ProgressBar mProgressbar;
	private CustomProgressDialog progressDialog;
	private int threadnum=0;//进入的线程计数 

	//商品选购和礼品赠送区地址
	private String uri_a="http://app.0yuanwang.com/Api/getXuangouList/";
	private String uri_b="http://app.0yuanwang.com/Api/getLipinList/";
	
	private boolean okload=true;//标记是否可以加载
	private boolean loadover=false;//全部数据加载完成
	protected ListView goodslistview;
	protected TextView loading,searchbtn;
	private ImageView scancode,iv_search_btn;//,image_a1,image_a2,image_a3,image_a4;
	private GridView gridview_a,gridview_b;
	private GridViewAdapter gridviewadapter_a,gridviewadapter_b; 
	protected  ArrayList<Map<String, Object>> goodslistItems; //产品列表
	private HomeGoodsListViewAdapter goodslistviewadapter;   //产品适配器
	protected String uri="http://app.0yuanwang.com/";
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view= inflater.inflate(R.layout.container_pullscrollview,
				container, false);
		//取下拉刷新对象
		mpullScrollView= (PullRefreshScrollView) view.findViewById(R.id.scrollid);
		 //slidingLayout.setScrollEvent(mpullScrollView);
		 
		searchbtn = (TextView) view.findViewById(R.id.tv_top_title);
		scancode = (ImageView) view.findViewById(R.id.scancode);
		iv_search_btn = (ImageView) view.findViewById(R.id.iv_search_btn);
		mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
		loading= (TextView)view.findViewById(R.id.loading);
	
		//取要显示的内容视图
		contentLayout= (LinearLayout)mpullScrollView.addBodyLayoutFile(getActivity(),R.layout.activity_home);
		//mpullScrollView.setFooterShow(false);
		goodslistview = (ListView) contentLayout.findViewById(R.id.goodslist);
		gridview_a= (GridView) contentLayout.findViewById(R.id.gridview_a);
		gridview_b= (GridView) contentLayout.findViewById(R.id.gridview_b);
		loading= (TextView) contentLayout.findViewById(R.id.loading);
		
		
		//取要显示的内容视图

		//mpullScrollView.addBodyView(contentLayout);
		mpullScrollView.setOnPullListener(this);
		//设置gridview适配器
		gridviewadapter_a=new GridViewAdapter(getActivity());
		gridview_a.setAdapter(gridviewadapter_a);
		gridviewadapter_b=new GridViewAdapter(getActivity());
		gridview_b.setAdapter(gridviewadapter_b);

		initListener();
	    //后台请求产品列表
		initgrid_a();
		initgrid_b();
		//loadagoods();
		loaddata();
		return view;
	}

	private void loaddata(){
		if(!loadover){
		new Thread(new Runnable() {
			public void run() {
				 //showProgressBar();
	                // 发送消息  
	                Message msg=handler.obtainMessage();
	                msg.what=SHOW_LOADING;
	                handler.sendMessage(msg);
				try{
					boolean firstload=false;
					int count=0;
					if(goodslistviewadapter == null){
						firstload=true;
						 goodslistviewadapter = new HomeGoodsListViewAdapter(getActivity()); //创建适配器 
					}else{
						 count = goodslistviewadapter.getCount(); 
					}
				    //后台请求产品列表
						// TODO Auto-generated method stub
				       
			           	String jsonstr=getUrlPage("http://app.0yuanwang.com/Api/getgoodslist/page/"+count+"/num/6");
			           //	Log.v("jsonstr","data-->"+jsonstr);
			           	if(jsonstr!=null && !TextUtils.isEmpty(jsonstr)){
			            try{
			            	
			        		JSONArray jsonarr= new JSONObject(jsonstr).getJSONArray("goods_list");   
			                for(int i = 0; i < jsonarr.length() ; i++){ 
			                    JSONObject jsonObj = ((JSONObject)jsonarr.opt(i)); 
			                    int id = jsonObj.getInt("id"); 
			                    String pic = jsonObj.getString("pic"); 
			                    String title = jsonObj.getString("title");
			                    String price = jsonObj.getString("price");
			                    Map<String, Object> map = new HashMap<String, Object>(); 
			                    map.put("pic", pic); 
			                    map.put("title", title);  
			                    map.put("price", price);  
			                    map.put("id", id);    
			                    if(!map.isEmpty()){
			    					 goodslistviewadapter.addItem(map);
			    				}
			                   
			                }
			                if(firstload){
			                	Message msg2=handler.obtainMessage();;
			                	msg2.what=SET_GOODSLIST_ADAPTER;
			                	handler.sendMessage(msg2);
			                }else{
			                	
				                // 发送消息  
				                Message msg1=handler.obtainMessage();;
				                msg1.what=UPDATE_GOODSLISTVIEW;
				                handler.sendMessage(msg1);			  
				                }

			            }catch(JSONException e){
			            	Log.v("json_err",e.getMessage());
			            }
			           	}else{
			           		loadover=true;
			           	 hideProgressBar();
					    	Toast.makeText(getActivity(), "已经没有内容啦！",
										Toast.LENGTH_SHORT).show();
			           	}
					}catch(Exception e){
						Log.v("json_err",e.getMessage());
					}	
	            Message msg1=handler.obtainMessage();
	            msg1.what=GONE_LOADING;
	            handler.sendMessage(msg1);
	            okload=true;
			}

		}).start();
		}else{
			mpullScrollView.setfooterLoadOverText(null);
			loading.setVisibility(View.VISIBLE);
			loading.setText("加载完成,共"+goodslistviewadapter.getCount()+"个");
		}

	}
	
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
	    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
	    listView.setLayoutParams(params); 
    }  
	/**
	 * 初始化事件绑定
	 **/
	private void initListener(){
		gridview_a.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				try{
					Map<String, Object> map=(Map<String, Object>) gridviewadapter_a.getItem(arg2);
					String titleitem=map.get("id").toString();	
						Intent intent=new Intent();
/*						Bundle bundle=new Bundle();
						bundle.putString("goodsid",titleitem);
						intent.putExtras(bundle);*/
						intent.putExtra("goods_id", titleitem);
						intent.setClass(getActivity(),GoodsActivity.class);
						startActivity(intent);
						inright();
					}catch(Exception e){
						e.printStackTrace();
					}	
			}
			
		});
		gridview_b.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				try{
					Map<String, Object> map=(Map<String, Object>) gridviewadapter_b.getItem(arg2);
					String titleitem=map.get("id").toString();	
						Intent intent=new Intent();
/*						Bundle bundle=new Bundle();
						bundle.putString("goodsid",titleitem);
						intent.putExtras(bundle);*/
						intent.putExtra("goods_id", titleitem);
						intent.setClass(getActivity(),GoodsActivity.class);
						startActivity(intent);
						inright();
					}catch(Exception e){
						e.printStackTrace();
					}	
			}
			
		});
		iv_search_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(), GoodsListActivity.class);
				startActivity(intent);
				inright();
			}
			
		});
		searchbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(), GoodsListActivity.class);
				startActivity(intent);
				inright();
			}
			
		});
		scancode.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(),ScanCodeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
			
		});
	    goodslistview.setOnItemClickListener(new OnItemClickListener(){
			@SuppressWarnings("unchecked")
			@Override
			//arg0:就是你的listview   arg2:点击的item的位置。和你的数组的下标相等。arg3:被电击view的id
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	               // TODO Auto-generated method stub
				try{
				Map<String, Object> map=(Map<String, Object>) goodslistviewadapter.getItem(arg2);
				String titleitem=map.get("id").toString();	
				

					Intent intent=new Intent();
/*					Bundle bundle=new Bundle();
					bundle.putString("goodsid",titleitem);
					intent.putExtras(bundle);*/
					intent.putExtra("goods_id", titleitem);
					intent.setClass(getActivity(),GoodswebActivity.class);
					startActivity(intent);
					inright();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
             
        }); 
	}
    private Handler handler = new Handler() {  
        public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case UPDATE_GOODSLISTVIEW:
                //通知listview更新界面
        		if(goodslistview.getAdapter()==null){
        			goodslistview.setAdapter(goodslistviewadapter);
        		}
                goodslistviewadapter.notifyDataSetChanged();
            	loading.setVisibility(View.GONE);
            	setListViewHeightBasedOnChildren(goodslistview);
            	mpullScrollView.setfooterViewReset();
            	okload=true;//可以再次加载
        		break;       		
        	case UPDATE_GRIDVIEW_A:
                if (progressDialog != null){  
                    progressDialog.dismiss();  
                    progressDialog = null;  
                } 
                //通知listview更新界面
               gridviewadapter_a.notifyDataSetChanged();
            	loading.setVisibility(View.GONE);
            	setListViewHeightBasedOnChildren(gridview_a);
            	mpullScrollView.setheaderViewReset();
            	okload=true;//可以再次加载
        		break;
        	case UPDATE_GRIDVIEW_B:
                //通知listview更新界面
                gridviewadapter_b.notifyDataSetChanged();
            	loading.setVisibility(View.GONE);
            	setListViewHeightBasedOnChildren(gridview_b);
            	mpullScrollView.setheaderViewReset();
            	okload=true;//可以再次加载
        		break;
        	case UPDATE_A_GOODS:
        		
        		break;
        	case SHOW_LOADING:
        		loading.setVisibility(View.VISIBLE);
        		break;
        	case GONE_LOADING:
        		if(threadnum==0){
        		loading.setVisibility(View.GONE);
        		mpullScrollView.setheaderViewReset();
        		}
        		break;
        	case SHOW_PROGRESSBAR:
        		threadnum++;
        		mProgressbar.setVisibility(View.VISIBLE);
        		break;
        	case HIDE_PROGRESSBAR:
        		//threadnum--;
        		if(--threadnum<=0){
        		mProgressbar.setVisibility(View.GONE);
        		}
        		break;
        	case SET_GOODSLIST_ADAPTER:
        		goodslistview.setAdapter(goodslistviewadapter);
        		setListViewHeightBasedOnChildren(goodslistview);
        		break;
        	default:
        		break;
        	}
        	super.handleMessage(msg);  
        };  
    };  
    @Override
	
    //扫描完成后
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == -1){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				String str=bundle.getString("result");
/*				DecodeCodeActivity decode=new DecodeCodeActivity(getActivity(),str);
				decode.toActivity();*/
				//调用外部浏览器
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(str));
				startActivity(intent);
				//显示
				//mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		}
    }	
    private void initgrid_a(){
    	new Thread(new Runnable() {

			@Override
			public void run() {
                // 发送消息  
              showProgressBar();
				// TODO Auto-generated method stub
		      	String jsonstr=getUrlPage(uri_a);
		       	Log.v("jsonstr","data-->"+jsonstr);
		       	if(jsonstr!=null && !TextUtils.isEmpty(jsonstr)){
		        try{
		        	
		    		JSONArray jsonarr=JSONDecode.getInstance(jsonstr)
							    				.getString("goods_list")
							    				.toJSONArray();   
		            for(int i = 0; i < jsonarr.length() ; i++){ 
		                JSONObject jsonObj = JSONDecode.getInstance(jsonarr.opt(i).toString())
		                							.toJSONObject(); 
		                int id = jsonObj.getInt("id"); 
		                String pic = jsonObj.getString("pic"); 
		                String title = jsonObj.getString("title");
		                String price = jsonObj.getString("price");
		                Map<String, Object> map = new HashMap<String, Object>(); 
		                map.put("pic", pic); 
		                map.put("title", title);  
		                map.put("price", price);  
		                map.put("id", id);    
		                if(!map.isEmpty()){
							 gridviewadapter_a.addItem(map);
						}
		               
		            }
		                // 发送消息  
		                Message msg3=handler.obtainMessage();;
		                msg3.what=UPDATE_GRIDVIEW_A;
		                handler.sendMessage(msg3);	
		                
		        }catch(JSONException e){
		        	Log.v("json_err",e.getMessage());
		        }
		       	}
/*		       	setListViewHeightBasedOnChildren(gridview_a);
		       	gridviewadapter_a.notifyDataSetChanged();*/		
		        hideProgressBar();
			}
    		
    	}).start();
 
    }
    private void initgrid_b(){
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			 showProgressBar();
    			// TODO Auto-generated method stub
       	String jsonstr=getUrlPage(uri_b);
       	Log.v("jsonstr","data-->"+jsonstr);
       	if(jsonstr!=null && !TextUtils.isEmpty(jsonstr)){
        try{
        	
    		JSONArray jsonarr= new JSONObject(jsonstr).getJSONArray("goods_list");   
            for(int i = 0; i < jsonarr.length() ; i++){ 
                JSONObject jsonObj = ((JSONObject)jsonarr.opt(i)); 
                int id = jsonObj.getInt("id"); 
                String pic = jsonObj.getString("pic"); 
                String title = jsonObj.getString("title");
                String price = jsonObj.getString("price");
                Map<String, Object> map = new HashMap<String, Object>(); 
                map.put("pic", pic); 
                map.put("title", title);  
                map.put("price", price);  
                map.put("id", id);    
                if(!map.isEmpty()){
					 gridviewadapter_b.addItem(map);
				}
               
            }

            	
                // 发送消息  
                Message msg2=handler.obtainMessage();;
                msg2.what=UPDATE_GRIDVIEW_B;
                handler.sendMessage(msg2);	
                

        }catch(JSONException e){
        	Log.v("json_err",e.getMessage());
        }
       	}
/*       	setListViewHeightBasedOnChildren(gridview_b);
       	gridviewadapter_b.notifyDataSetChanged();*/
        hideProgressBar();
		}
		}).start();
    }
    public static void setListViewHeightBasedOnChildren(GridView listView) {  
        // 获取listview的adapter  
           ListAdapter listAdapter = listView.getAdapter();  
           if (listAdapter == null) {  
               return;  
           }  
           // 固定列宽，有多少列  
           int col = 2;// listView.getNumColumns();  
           int totalHeight = 0;  
           // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，  
           // listAdapter.getCount()小于等于8时计算两次高度相加  
           for (int i = 0; i < listAdapter.getCount(); i += col) {  
            // 获取listview的每一个item  
               View listItem = listAdapter.getView(i, null, listView);  
               listItem.measure(0, 0);  
               // 获取item的高度和  
               totalHeight += listItem.getMeasuredHeight()+40;  //添加40顶边距等
           }  
      
           // 获取listview的布局参数  
           ViewGroup.LayoutParams params = listView.getLayoutParams();  
           // 设置高度  
           params.height = totalHeight;  
           // 设置margin  
           ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);  
           // 设置参数  
           listView.setLayoutParams(params);  
       } 
    //显示进度条
    @Override
    public void showProgressBar(){
        Message msg1=handler.obtainMessage();;
        msg1.what=SHOW_PROGRESSBAR;
        handler.sendMessage(msg1);	
    }
    //隐藏进度条
    @Override
    public void hideProgressBar(){
        Message msg1=handler.obtainMessage();;
        msg1.what=HIDE_PROGRESSBAR;
        handler.sendMessage(msg1);	
    }
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
     	initgrid_a();
    	initgrid_b();
    	gridviewadapter_a.removeAllItem();
    	gridviewadapter_b.removeAllItem();
    	goodslistviewadapter.removeAllItem();
    	loaddata();
	}
	@Override
	public void loadMore() {
		loaddata();
		// TODO Auto-generated method stub
		
	}
}

