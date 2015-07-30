package com.demo.jiuwo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.demo.adapter.CategoryListViewAdapter;
import com.demo.adapter.GoodsListViewAdapter;
import com.demo.adapter.HomeGoodsListViewAdapter;
import com.demo.adapter.MemListViewAdapter;
import com.demo.core.BaseActivity;
import com.demo.core.JSONDecode;
import com.demo.core.MyProgressBar;
import com.demo.core.ScanCodeActivity;
import com.demo.jiuwo.R;
import com.ex.PullRefreshScrollView;
import com.ex.SlidingMenu;
import com.ex.PullRefreshScrollView.OnPullListener;

public class GoodsListActivity extends BaseActivity implements MyProgressBar,OnPullListener {
	final static int SCANNIN_GREQUEST_CODE = 1;
	final static int UPDATE_GOODSLISTVIEW = 2;
	final static int UPDATE_CATEGORY_XUANGOU = 21;
	final static int UPDATE_CATEGORY_LIPIN = 22;
	final static int RESET_PULLREFRESH = 23;
	final static int RESET_LOADMORE = 24;
	
	final static int SHOW_LOADING = 4;
	final static int GONE_LOADING = 5;
	
	private int threadnum=0;//进入的线程计数 
	private boolean okload;//标记是否可以加载
	private boolean loadover;//全部数据加载完成
	
	private ProgressBar mProgressbar;
	protected ListView goodslistview,categorylistview_xuangou,categorylistview_lipin;
	protected TextView loading,category_title,clear_category_title;
	protected boolean searching=false;//正在搜索中
	private EditText keywords_edit;
	private SlidingMenu mSlidingmenu;
	private PullRefreshScrollView	mpullScrollView;//下拉刷新
	protected ImageView category_btn,search_btn;
	protected  ArrayList<Map<String, Object>> goodslistItems; //产品列表
	private GoodsListViewAdapter goodslistviewadapter;   //产品适配器
	private CategoryListViewAdapter categorylistviewadapter_xuangou,categorylistviewadapter_lipin;   //分类适配器
	private String searuri="http://app.0yuanwang.com/Api/getgoodslist";//搜索产品地址

	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_goods_list);
			//取下拉刷新对象
			mpullScrollView= (PullRefreshScrollView)findViewById(R.id.scrollid);
			mpullScrollView.setfooterEnabled(false);
			mpullScrollView.setOnPullListener(this);
			//取要显示到下拉容器中的内容视图
			LinearLayout cl= (LinearLayout)mpullScrollView.addBodyLayoutFile(this,R.layout.activity_goods_list_content);
			
			mSlidingmenu=(SlidingMenu)findViewById(R.id.slidingLayout);
			category_title= (TextView) findViewById(R.id.category_title);
			clear_category_title= (TextView) findViewById(R.id.clear_category_title);
			categorylistview_xuangou= (ListView) findViewById(R.id.categorylist_xuangou);
			categorylistview_lipin= (ListView) findViewById(R.id.categorylist_lipin);
			keywords_edit= (EditText)findViewById(R.id.keywords);
			search_btn= (ImageView) findViewById(R.id.search_btn);
			category_btn = (ImageView)findViewById(R.id.category_btn);
			mProgressbar = (ProgressBar)findViewById(R.id.progressBar);
			


			
			goodslistview= (ListView) cl.findViewById(R.id.goodslist);
			loading= (TextView)cl.findViewById(R.id.loading);
			//初始化适配器
			goodslistviewadapter = new GoodsListViewAdapter(this); //创建适配器 
			categorylistviewadapter_xuangou=new CategoryListViewAdapter(this);
			categorylistviewadapter_lipin=new CategoryListViewAdapter(this);
			categorylistview_xuangou.setAdapter(categorylistviewadapter_xuangou);
			categorylistview_lipin.setAdapter(categorylistviewadapter_lipin);	
			goodslistview.setAdapter(goodslistviewadapter);
			this.initCategory();
			this.initListener();
		}
		public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
				finish();
				inleft();
	                   return true;       
	        }       
	        return super.onKeyDown(keyCode, event);       
	    } 
		/**
		 *初始化分类数据 
		 ***/
		private void initCategory(){	
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String jsonstr=getUrlPage("http://app.0yuanwang.com/Api/getcategorylist/");
					try {
					JSONArray jsonarr = JSONDecode.getInstance(jsonstr).toJSONArray();		
						//选购分类
						JSONArray jarr1=JSONDecode.getInstance(((JSONObject)jsonarr.opt(0)).getString("child")).toJSONArray();
						for(int i=0;i<jarr1.length();i++){
							JSONObject obj=(JSONObject)jarr1.opt(i);
							String title=obj.getString("title");
							title=title.replace(" ","");
							String category_id=obj.getString("category_id");
							String child=obj.getString("child");
							HashMap<String,Object> map = new HashMap<String,Object>(); 
							map.put("title", title);
							map.put("category_id",category_id);
							map.put("child",child);
							categorylistviewadapter_xuangou.addItem(map);
						}
		                // 发送消息  
		                Message msg=handler.obtainMessage();
		                msg.what=UPDATE_CATEGORY_XUANGOU;
		                handler.sendMessage(msg);
						
						//礼品分类
						JSONArray jarr2=JSONDecode.getInstance(((JSONObject)jsonarr.opt(1)).getString("child")).toJSONArray();
						for(int i=0;i<jarr2.length();i++){
							JSONObject obj=(JSONObject)jarr2.opt(i);
							String title=obj.getString("title");
							title=title.replace(" ","");
							String category_id=obj.getString("category_id");
							String child=obj.getString("child");
							HashMap<String,Object> map = new HashMap<String,Object>(); 
							map.put("title",title);
							map.put("category_id",category_id);
							map.put("child",child);
							categorylistviewadapter_lipin.addItem(map);
						}
		                // 发送消息  
		                Message msg1=handler.obtainMessage();
		                msg1.what=UPDATE_CATEGORY_LIPIN;
		                handler.sendMessage(msg1);
						} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				
			}).start();


		}
		/**
		 * 加载列表数据
		 * */
		private void loaddata(){
			if(!loadover){
			new Thread(new Runnable() {
				public void run() {
					showProgressBar();
					okload=false;
		                // 发送消息  
		                Message msg=handler.obtainMessage();
		                msg.what=SHOW_LOADING;
		                handler.sendMessage(msg);
					try{
						boolean firstload=false;
						int count = goodslistviewadapter.getCount(); 

						/***搜索数据条件**/
						String baseuri=searuri;
						   String keywords="";
						   String category_id="";
						   category_id=(String)category_title.getTag();
					       keywords=keywords_edit.getText().toString();
					        /*for (int i = count; i < count + 10; i++) { 
					        	goodslistviewadapter.add(String.valueOf(i + 1)); 
					        }	*/
				        String jsonstr="";
				        baseuri+="/page/"+count;
			           	if(!TextUtils.isEmpty(keywords)){
			           		baseuri+="/keywords/"+keywords;
			           	}
			           	if(!TextUtils.isEmpty(category_id)){
			           		baseuri+="/category_id/"+category_id;
			           	}
			           	
			           		jsonstr=getUrlPage(baseuri);
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
				    					 goodslistviewadapter.addItem(map);
				    				}
				                   
				                }
					                // 发送消息  
					                Message msg1=handler.obtainMessage();;
					                msg1.what=UPDATE_GOODSLISTVIEW;
					                handler.sendMessage(msg1);			                	

				            }catch(JSONException e){
				            	Log.v("json_err",e.getMessage());
				            	resetRefresh();
				            }
				           	}else{
				           		loadover=true;
				           		okload=true;
				           		searching=false;
				           		resetRefresh();
						    	Toast.makeText(GoodsListActivity.this, "已经没有内容啦！",
											Toast.LENGTH_SHORT).show();
				           	}

				                
						}catch(Exception e){
							Log.v("json_err",e.getMessage());
							resetRefresh();
						}	
					hideProgressBar();
					resetRefresh();
				}
			}).start();
			}else{
				loading.setVisibility(View.VISIBLE);
				mpullScrollView.setheaderViewReset();//重置头部刷新
				loading.setText("加载完成");
				mpullScrollView.setfooterLoadOverText("加载完成,共"+goodslistviewadapter.getCount()+"个");
			}

		}
	    private Handler handler = new Handler() {  
	        public void handleMessage(Message msg) { 
	        	switch(msg.what){
	        	case UPDATE_GOODSLISTVIEW:
	                //通知listview更新界面
	                goodslistviewadapter.notifyDataSetChanged();
	            	loading.setVisibility(View.GONE);
	            	setListViewHeightBasedOnChildren(goodslistview);
	    			mpullScrollView.setheaderViewReset();//重置头部刷新
	    			mpullScrollView.setfooterViewReset();
	            	okload=true;//可以再次加载
	            	searching=false;
	            	Log.v("statusa",goodslistviewadapter.getCount()+"");
	            	int count=goodslistviewadapter.getCount();
	            	if(count>=5){
	            		mpullScrollView.setfooterEnabled(true);
	            		
	            	}else if(count==0){
	            		mpullScrollView.setfooterLoadOverText("没有数据");
	            	}else{
	            		mpullScrollView.setfooterEnabled(false);
	            	}
	        		break;
	        	case UPDATE_CATEGORY_XUANGOU:
	        		categorylistviewadapter_xuangou.notifyDataSetChanged();
	        		break;
	        	case UPDATE_CATEGORY_LIPIN:
	        		categorylistviewadapter_lipin.notifyDataSetChanged();
	        		break;
	        	case SHOW_LOADING:
	        		loading.setVisibility(View.VISIBLE);
	        		break;
	        	case GONE_LOADING:
	        		loading.setVisibility(View.GONE);
	        		break;
/*	        	case SET_GOODSLIST_ADAPTER:
	        		goodslistview.setAdapter(goodslistviewadapter);
	        		setListViewHeightBasedOnChildren(goodslistview);
	        		break;*/
	        	case SHOW_PROGRESSBAR:
	        		mProgressbar.setVisibility(View.VISIBLE);
	        		break;
	        	case HIDE_PROGRESSBAR:
	        		mProgressbar.setVisibility(View.GONE);
	        		break;
	        	case RESET_PULLREFRESH:
	        		mpullScrollView.setheaderViewReset();
	        		mpullScrollView.setfooterViewReset();
	        		mpullScrollView.setfooterEnabled(true);
	        		break;
	        	case RESET_LOADMORE:
	        		mpullScrollView.setfooterViewReset();
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
		    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
		    listView.setLayoutParams(params); 
	    }  
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
	    //显示进度条
	    public void showProgressBar(){
	    	int co=goodslistviewadapter.getCount();
	    	if(co<=0){
	        Message msg1=handler.obtainMessage();;
	        msg1.what=SHOW_PROGRESSBAR;
	        handler.sendMessage(msg1);	
	    	}
	    }
	    //隐藏进度条
	    public void hideProgressBar(){
	        Message msg1=handler.obtainMessage();;
	        msg1.what=HIDE_PROGRESSBAR;
	        handler.sendMessage(msg1);	
	    }
	    //重置头部下拉刷新
	    public void resetRefresh(){
	        Message msg1=handler.obtainMessage();;
	        msg1.what=RESET_PULLREFRESH;
	        handler.sendMessage(msg1);		    	
	    }
	    //重置底部加载更多
	    public void resetloadmore(){
	        Message msg1=handler.obtainMessage();;
	        msg1.what=RESET_LOADMORE;
	        handler.sendMessage(msg1);		    	
	    }
	    /**
	     * 初始化监听器
	     * */
		private void initListener(){
			clear_category_title.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					category_title.setText("筛选:全部");
					category_title.setTag("");
				}
				
			});
			goodslistview.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					  // 当不滚动时
				     if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				      //判断是否滚动到底部
				      if (view.getLastVisiblePosition() >= view.getCount() - 2) {
				    	  //加载更多数据
				    	  loaddata();
			                
			               // goodsListView.setSelection(visibleLastIndex - visibleItemCount + 1); //设置选中项 
				      }
				     }
				    }
					
				});
			categorylistview_xuangou.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					Map<String, Object> map=(Map<String, Object>) categorylistviewadapter_xuangou.getItem(arg2);
					category_title.setVisibility(View.VISIBLE);
					category_title.setText("筛选:全部>商品选购>"+map.get("title").toString());
					category_title.setTag(map.get("category_id"));
					clear_category_title.setText("清空");
					startSearch();
				}
				
			});
			categorylistview_lipin.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					Map<String, Object> map=(Map<String, Object>) categorylistviewadapter_lipin.getItem(arg2);
					category_title.setVisibility(View.VISIBLE);
					category_title.setText("筛选:全部>礼品赠送>"+map.get("title").toString());
					category_title.setTag(map.get("category_id"));
					clear_category_title.setText("清空");
					startSearch();
				}
				
			});
			//搜索按钮
			search_btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startSearch();
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
						intent.putExtra("goods_id", titleitem);
						intent.setClass(GoodsListActivity.this,GoodsActivity.class);
						startActivity(intent);
						inright();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
	             
	        }); 
			category_btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					mSlidingmenu.toggle();
				}
				
			});
		}
		/*
		 * 根据现有条件进行一次搜索
		 * */
		public void startSearch(){
			if(!searching){
			searching=true;
			loadover=false;//重新开始搜索
			goodslistviewadapter.removeAllItem();
			goodslistviewadapter.notifyDataSetChanged();
			
			/**隐藏软键盘**/
	        View view = getWindow().peekDecorView();
	        if (view != null) {
	            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
	        }
/*			
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); */
			loaddata();
			}
		}
		@Override
		public void refresh() {
			// TODO Auto-generated method stub
			goodslistviewadapter.removeAllItem();
			goodslistviewadapter.notifyDataSetChanged();
			loaddata();
		}
		@Override
		public void loadMore() {
			// TODO Auto-generated method stub
			//loaddata();
			loaddata();
		}
}
