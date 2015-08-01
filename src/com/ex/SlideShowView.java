package com.ex;

import android.widget.FrameLayout;

import java.io.IOException;
import java.util.ArrayList;  
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
  
import android.content.Context;  
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;  
import android.os.AsyncTask;  
import android.os.Handler;  
import android.os.Message;  
import android.os.Parcelable;  
import android.support.v4.view.PagerAdapter;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.util.AttributeSet;  
import android.util.Log;
import android.view.LayoutInflater;  
import android.view.View;   
import android.widget.ImageView;  
import android.widget.ImageView.ScaleType;  
import android.widget.LinearLayout;  
import com.demo.jiuwo.R;
import com.demo.jiuwo.ui.GoodsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader; 
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
public class SlideShowView extends FrameLayout {
	 // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar  
    private ImageLoader imageLoader = ImageLoader.getInstance();  
  
    //轮播图图片数量  
    private final static int IMAGE_COUNT = 5;  
    //自动轮播的时间间隔  
    private final static int TIME_INTERVAL = 5;  
    //自动轮播启用开关  
    private final static boolean isAutoPlay = true;   
      
    //自定义轮播图的资源  
    private String[] imageUrls;  
    //自定义轮播图的id列表
    private String[] ids;  
    //放轮播图片的ImageView 的list  
    private List<ImageView> imageViewsList;  
    //放圆点的View的list  
    private List<View> dotViewsList;  
      
    private ViewPager viewPager;  
    //当前轮播页  
    private int currentItem  = 0;  
    //定时任务  
    private ScheduledExecutorService scheduledExecutorService;  
      
    private Context context;  
      
    //Handler  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            viewPager.setCurrentItem(currentItem);  
        }  
          
    };  
      
    public SlideShowView(Context context) {  
        this(context,null);  
        // TODO Auto-generated constructor stub  
    }  
    public SlideShowView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
        // TODO Auto-generated constructor stub  
    }  
    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;  
        initData();  
        if(isAutoPlay){  
            startPlay();  
        }  
          
    }  
    /** 
     * 开始轮播图切换 
     */  
    private void startPlay(){  
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);  
    }  
    /** 
     * 停止轮播图切换 
     */  
    private void stopPlay(){  
        scheduledExecutorService.shutdown();  
    }  
    /** 
     * 初始化相关Data 
     */  
    private void initData(){  
        imageViewsList = new ArrayList<ImageView>();  
        dotViewsList = new ArrayList<View>();  
        // 一步任务获取图片  
        new GetListTask().execute("");  
    }  
    /** 
     * 初始化Views等UI 
     */  
    private void initUI(Context context){  
        if(imageUrls == null || imageUrls.length == 0)  
            return;  
          
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);  
          
        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);  
        dotLayout.removeAllViews();  
          
        // 热点个数与图片特殊相等  
        for (int i = 0; i < imageUrls.length; i++) {  
            ImageView view =  new ImageView(context);  
            //给图片设置标签数据
            view.setTag(R.id.tag_first,imageUrls[i]);  
            view.setTag(R.id.tag_second,ids[i]);
            view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					//单击后进行处理
					// TODO Auto-generated method stub
					ImageView imageview=(ImageView)v;
					String id=imageview.getTag(R.id.tag_second).toString();
					Intent intent=new Intent();
					intent.putExtra("goods_id", id);
					intent.setClass(getContext(), GoodsActivity.class);
					getContext().startActivity(intent);
				}
            	
            });
            if(i==0)//给一个默认图  
                view.setBackgroundResource(R.drawable.slide_default);  
            view.setScaleType(ScaleType.FIT_XY);  
            imageViewsList.add(view);  
              
            ImageView dotView =  new ImageView(context);  
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
            params.leftMargin = 4;  
            params.rightMargin = 4;  
            dotLayout.addView(dotView, params);  
            dotViewsList.add(dotView);  
        }  
          
        viewPager = (ViewPager) findViewById(R.id.viewPager);  
        viewPager.setFocusable(true);  
          
        viewPager.setAdapter(new MyPagerAdapter());  
        viewPager.setOnPageChangeListener(new MyPageChangeListener());  
    }  
      
    /** 
     * 填充ViewPager的页面适配器 
     *  
     */  
    private class MyPagerAdapter  extends PagerAdapter{  
    	final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            // TODO Auto-generated method stub  
            //((ViewPag.er)container).removeView((View)object);  
            ((ViewPager)container).removeView(imageViewsList.get(position));  
        }  
  
        @Override  
        public Object instantiateItem(View container, int position) {  
            ImageView imageView = imageViewsList.get(position);  
  
            //imageLoader.displayImage(imageView.getTag() + "", imageView);  
              loadimg(imageView.getTag(R.id.tag_first).toString(), imageView);
            ((ViewPager)container).addView(imageViewsList.get(position));  
            return imageViewsList.get(position);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return imageViewsList.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            // TODO Auto-generated method stub  
            return arg0 == arg1;  
        }  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
              
        }  
        /** 
         *  
         * @param urlStr 所需要加载的图片的url，以String形式传进来，可以把这个url作为缓存图片的key 
         * @param image ImageView 控件 
         * @param resId 默认显示的图片资源
         */  
        protected void loadimg(String urlStr, ImageView image) {  
        	   	
        	
        	DisplayImageOptions options = new DisplayImageOptions.Builder()
        	        .showImageOnLoading(R.drawable.default_1px) // 设置图片在下载期间显示的图片
        	        .showImageForEmptyUri(R.drawable.default_1px)// 设置图片Uri为空或是错误的时候显示的图片  
        	        .showImageOnFail(R.drawable.default_1px)// 设置图片加载/解码过程中错误时候显示的图片  
        	        .resetViewBeforeLoading(true)  // default
        	        .delayBeforeLoading(1000)
        	        .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
        	        .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中  
        	       // .postProcessor(null)//在图片显示之前的操作
        	        //.preProcessor(null) //设置图片加入缓存前，对bitmap进行设置   
        	        .extraForDownloader(null)
        	        .considerExifParams(false) // default
        	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        	        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
        	      //  .decodingOptions(null)
        	        .displayer(new SimpleBitmapDisplayer()) // default
        	        .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
        	        .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
        	        .handler(new Handler()) // default
        	        .build();
        	
        	//返回一个实例
        	ImageLoader imageLoader = ImageLoader.getInstance();
        	//ImageAware imageAware = new ImageViewAware(image, false);
        	//imageLoader.displayImage(urlStr,image,options);
        	//ImageSize targetSize = new ImageSize(150, 150); // result Bitmap will be fit to this size
        	//image.setImageBitmap(imageLoader.loadImageSync(urlStr, targetSize, options));//同步加载
        	imageLoader.displayImage(urlStr, image, options, new SimpleImageLoadingListener() {
        	    @Override
        	    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        	        // Do whatever you want with Bitmap
        	    	Log.v("catch",view+"");
        	    	try{
                    if (loadedImage != null && view!=null) {  
                        ImageView imageView = (ImageView) view;  
                       // imageView.setImageBitmap(loadedImage);
                        // 是否第一次显示  
                        boolean firstDisplay = !displayedImages.contains(imageUri);  
                        if (firstDisplay) {  
                            // 图片淡入效果  
                            FadeInBitmapDisplayer.animate(imageView, 800);  
                            displayedImages.add(imageUri);  
                        } 
                    } 
        	    	}catch(Exception e){
        	    		Log.v("catch1",e.getMessage());
        	    	}
        	    }
        	});

        	//AsyncImageLoader asyncLoader = new AsyncImageLoader(urlStr,image,resId);//什么一个异步图片加载对象  
        }  
          
    }  
    /** 
     * ViewPager的监听器 
     * 当ViewPager中页面的状态发生改变时调用 
     *  
     */  
    private class MyPageChangeListener implements OnPageChangeListener{  
  
        boolean isAutoPlay = false;  
  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
            switch (arg0) {  
            case 1:// 手势滑动，空闲中  
                isAutoPlay = false;  
                break;  
            case 2:// 界面切换中  
                isAutoPlay = true;  
                break;  
            case 0:// 滑动结束，即切换完毕或者加载完毕  
                // 当前为最后一张，此时从右向左滑，则切换到第一张  
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {  
                    viewPager.setCurrentItem(0);  
                }  
                // 当前为第一张，此时从左向右滑，则切换到最后一张  
                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {  
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);  
                }  
                break;  
        }  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public void onPageSelected(int pos) {  
            // TODO Auto-generated method stub  
              
            currentItem = pos;  
            for(int i=0;i < dotViewsList.size();i++){  
                if(i == pos){  
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);  
                }else {  
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);  
                }  
            }  
        }  
          
    }  
      
    /** 
     *执行轮播图切换任务 
     * 
     */  
    private class SlideShowTask implements Runnable{  
  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            synchronized (viewPager) {  
                currentItem = (currentItem+1)%imageViewsList.size();  
                handler.obtainMessage().sendToTarget();  
            }  
        }  
          
    }  
      
    /** 
     * 销毁ImageView资源，回收内存 
     *  
     */  
    private void destoryBitmaps() {  
  
        for (int i = 0; i < IMAGE_COUNT; i++) {  
            ImageView imageView = imageViewsList.get(i);  
            Drawable drawable = imageView.getDrawable();  
            if (drawable != null) {  
                //解除drawable对view的引用  
                drawable.setCallback(null);  
            }  
        }  
    }  
   
  
    /** 
     * 异步任务,获取数据 
     *  
     */  
    class GetListTask extends AsyncTask<String, Integer, Boolean> {  
  
        @Override  
        protected Boolean doInBackground(String... params) {  
            try {  
                // 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片  
                  String str=getUrlPage("http://app.0yuanwang.com/Api/getSlide/");
                  JSONArray jsonarr=new JSONArray(str);
                  //初始化数组长度
                  imageUrls=new String[jsonarr.length()];
                  ids=new String[jsonarr.length()];
                  for(int i=0;i<jsonarr.length();i++){
                	  JSONObject obj=(JSONObject)jsonarr.opt(i);
                	  imageUrls[i]=obj.getString("pic").toString();
                	  ids[i]=obj.getString("id").toString();
                  }
/*                imageUrls = new String[]{  
                        "http://image.zcool.com.cn/56/35/1303967876491.jpg",  
                        "http://image.zcool.com.cn/59/54/m_1303967870670.jpg",  
                        "http://image.zcool.com.cn/47/19/1280115949992.jpg",  
                        "http://image.zcool.com.cn/59/11/m_1303967844788.jpg"  
                };  
                
                ids=new String[]{"1","2","3","4"};*/
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
                return false;  
            }  
        }  
  
        @Override  
        protected void onPostExecute(Boolean result) {  
            super.onPostExecute(result);  
            if (result) {  
                initUI(context);  
            }  
        } 
    	protected String getUrlPage(String url){
    	    String uriAPI = url;
    	 
    	    HttpGet httpRequest = new HttpGet(uriAPI);
    	    try {
    	 
    	        HttpResponse httpResponse = new DefaultHttpClient()
    	                .execute(httpRequest);
    	 
    	        if (httpResponse.getStatusLine().getStatusCode() == 200) {
    	 
    	            String strResult = EntityUtils.toString(httpResponse
    	                    .getEntity());
    	            //替换掉空行
    	           // strResult = eregi_replace("(\r\n|\r|\n|\n\r)", "",strResult);
    	            return strResult;
    	        } else {
    	            return "Error Response: "+ httpResponse.getStatusLine().toString();
    	        }
    	    } catch (ClientProtocolException e) {
    	        e.printStackTrace();
    	        return e.getMessage().toString();          
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        return e.getMessage().toString();      
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return e.getMessage().toString();
    	    }
    	     
    	}
    }  
}
