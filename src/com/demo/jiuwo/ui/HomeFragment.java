package com.demo.jiuwo.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.adapter.GridViewAdapter;
import com.demo.adapter.HomeGoodsListViewAdapter;
import com.demo.core.BaseFragment;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.core.MyProgressBar;
import com.demo.core.ScanCodeActivity;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.PullRefreshScrollView.OnPullListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends BaseFragment implements OnPullListener {
	final static int SCANNIN_GREQUEST_CODE = 1;
	final static int UPDATE_GOODSLISTVIEW = 12;
	final static int UPDATE_GRIDVIEW_A = 2;
	final static int UPDATE_GRIDVIEW_B = 11;
	final static int SHOW_PROGRESSBAR=1314;
	final static int HIDE_PROGRESSBAR=1315;
	final static int UPDATE_A_GOODS = 3;
	private PullRefreshScrollView	mpullScrollView;
	private LinearLayout	contentLayout;
	final static int SET_GOODSLIST_ADAPTER=10;//设置产品列表适配器
	private ProgressBar mProgressbar;
	private CustomProgressDialog progressDialog;
	private int threadnum=0;//进入的线程计数 



	
	private boolean okload=true;//标记是否可以加载
	private boolean loadover=false;//全部数据加载完成
	protected MyListView goodslistview;
	protected TextView searchbtn;
	private ImageView scancode,iv_search_btn;//,image_a1,image_a2,image_a3,image_a4;
	private MyGridView gridview_a,gridview_b;
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
		View view= inflater.inflate(R.layout.activity_home,
				container, false);
		//取下拉刷新对象
		mpullScrollView= (PullRefreshScrollView) view.findViewById(R.id.scrollid);
		mpullScrollView.setfooterEnabled(false);
		 
		searchbtn = (TextView) view.findViewById(R.id.tv_top_title);
		scancode = (ImageView) view.findViewById(R.id.scancode);
		iv_search_btn = (ImageView) view.findViewById(R.id.iv_search_btn);
		mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
	
		//取要显示的内容视图
		//contentLayout= (LinearLayout)mpullScrollView.addBodyLayoutFile(getActivity(),R.layout.activity_home);
		//mpullScrollView.setFooterShow(false);
		goodslistview = (MyListView)view.findViewById(R.id.goodslist);
		gridview_a= (MyGridView)view.findViewById(R.id.gridview_a);
		gridview_b= (MyGridView)view.findViewById(R.id.gridview_b);
		initListener();
		
		//取要显示的内容视图

		//mpullScrollView.addBodyView(contentLayout);
		mpullScrollView.setOnPullListener(this);
		//设置gridview适配器
		gridviewadapter_a=new GridViewAdapter(getActivity());
		gridview_a.setAdapter(gridviewadapter_a);
		gridviewadapter_b=new GridViewAdapter(getActivity());
		gridview_b.setAdapter(gridviewadapter_b);
		goodslistviewadapter = new HomeGoodsListViewAdapter(getActivity());
		goodslistview.setAdapter(goodslistviewadapter);

		
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
				sendMessage(SHOW_PROGRESSBAR);
	                
				try{
					//boolean firstload=false;
					int count=goodslistviewadapter.getCount(); ;
				    //后台请求产品列表
						// TODO Auto-generated method stub
				       
			           	String jsonstr=GLOBAL.getUrlPage(GLOBAL.GOODS_LISTS_URL+"page/"+count+"/num/6");
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
			          
			                	 //sendMessage(SET_GOODSLIST_ADAPTER);
		
			                	
				                // 发送消息  
				                sendMessage(UPDATE_GOODSLISTVIEW);
				     

			            }catch(JSONException e){
			            	Log.v("json_err",e.getMessage());
			            }
			           	}else{
			           		loadover=true;
			           		sendMessage(HIDE_PROGRESSBAR);
					    	Toast.makeText(getActivity(), "已经没有内容啦！",
										Toast.LENGTH_SHORT).show();
			           	}
					}catch(Exception e){
						Log.v("json_err",e.getMessage());
					}	
				sendMessage(HIDE_PROGRESSBAR);
	            okload=true;
			}

		}).start();
		}else{
			mpullScrollView.setfooterLoadOverText("加载完成,共"+goodslistviewadapter.getCount()+"个");
		}

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
					intent.setClass(getActivity(),GoodsActivity.class);
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

            	//setListViewHeightBasedOnChildren(goodslistview);
            	mpullScrollView.setfooterViewReset();
            	if(goodslistviewadapter.getCount()>5){
            		mpullScrollView.setfooterEnabled(true);
            	}else{
            		mpullScrollView.setfooterEnabled(false);
            	}
            	okload=true;//可以再次加载
        		break;       		
        	case UPDATE_GRIDVIEW_A:
                if (progressDialog != null){  
                    progressDialog.dismiss();  
                    progressDialog = null;  
                } 
                //通知listview更新界面
               gridviewadapter_a.notifyDataSetChanged();

            	mpullScrollView.setheaderViewReset();
            	okload=true;//可以再次加载
        		break;
        	case UPDATE_GRIDVIEW_B:
                //通知listview更新界面
                gridviewadapter_b.notifyDataSetChanged();
            	mpullScrollView.setheaderViewReset();
            	okload=true;//可以再次加载
        		break;
        	case UPDATE_A_GOODS:
        		
        		break;
/*        	case SHOW_LOADING:
        		loading.setVisibility(View.VISIBLE);
        		break;
        	case GONE_LOADING:
        		if(threadnum==0){
        		loading.setVisibility(View.GONE);
        		mpullScrollView.setheaderViewReset();
        		}
        		break;*/
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
/*        	case SET_GOODSLIST_ADAPTER:
        		goodslistview.setAdapter(goodslistviewadapter);
        		//setListViewHeightBasedOnChildren(goodslistview);
        		break;*/
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
              sendMessage(SHOW_PROGRESSBAR);
				// TODO Auto-generated method stub
		      	String jsonstr=GLOBAL.getUrlPage(GLOBAL.GOODS_A_LISTS_URL);
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
		                sendMessage(UPDATE_GRIDVIEW_A);
		                
		        }catch(JSONException e){
		        	Log.v("json_err",e.getMessage());
		        }
		       	}
/*		       	setListViewHeightBasedOnChildren(gridview_a);
		       	gridviewadapter_a.notifyDataSetChanged();*/		
		       	sendMessage(HIDE_PROGRESSBAR);
			}
    		
    	}).start();
 
    }
    private void initgrid_b(){
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			sendMessage(SHOW_PROGRESSBAR);
    			// TODO Auto-generated method stub
       	String jsonstr=GLOBAL.getUrlPage(GLOBAL.GOODS_B_LISTS_URL);
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
                sendMessage(UPDATE_GRIDVIEW_B);
              	
                

        }catch(JSONException e){
        	Log.v("json_err",e.getMessage());
        }
       	}
/*       	setListViewHeightBasedOnChildren(gridview_b);
       	gridviewadapter_b.notifyDataSetChanged();*/
       	sendMessage(HIDE_PROGRESSBAR);
		}
		}).start();
    }
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
     	initgrid_a();
    	initgrid_b();
    	gridviewadapter_a.removeAllItem();
    	gridviewadapter_a.notifyDataSetChanged();
    	gridviewadapter_b.removeAllItem();
    	gridviewadapter_b.notifyDataSetChanged();
    	goodslistviewadapter.removeAllItem();
    	goodslistviewadapter.notifyDataSetChanged();
    	loaddata();
	}
	@Override
	public void loadMore() {
		loaddata();
		// TODO Auto-generated method stub
		
	}
	public void sendMessage(int msg_type){
        Message msg1=handler.obtainMessage();
        msg1.what=msg_type;
        handler.sendMessage(msg1);
	}
}

