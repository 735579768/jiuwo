package com.demo.core;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.demo.jiuwo.R;
import com.demo.jiuwo.R.drawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BaseFragment extends Fragment{
	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	boolean isExit;
	protected int animsj=3000;//启动动画跳转时间
	public BaseFragment(){

	}
	//左进右出
	protected void inleft(){
		//overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		//overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	//右进左出
	protected void inright(){
		//overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return true;
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getActivity(), "再按一次退出九沃网",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

	public boolean onCreateOptionsMenu(Menu menu) {
		// this.exit(0);
		return true;
		
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
	   /**
  * POST请求
  * 添加参数方法如下
  * List<NameValuePair> params = new ArrayList<NameValuePair>(); 
  * params.add(new BasicNameValuePair("action", "downloadAndroidApp"));
  * */
 protected String postUrl(String uri,List<NameValuePair> params){
 	String reStr="";
 	// 第一步，创建HttpPost对象 
     HttpPost httpPost = new HttpPost(uri); 
     HttpResponse httpResponse = null; 
     try { 
         // 设置httpPost请求参数 
         httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
         httpResponse = new DefaultHttpClient().execute(httpPost); 
         //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
         if (httpResponse.getStatusLine().getStatusCode() == 200) { 
             // 第三步，使用getEntity方法活得返回结果 
             reStr= EntityUtils.toString(httpResponse.getEntity()); 
         } 
     } catch (ClientProtocolException e) { 
         e.printStackTrace(); 
     } catch (IOException e) { 
         e.printStackTrace(); 
     } 
     return reStr;
 }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return null;
	}
    /** 
     *  
     * @param urlStr 所需要加载的图片的url，以String形式传进来，可以把这个url作为缓存图片的key 
     * @param image ImageView 控件 
     * @param resId 默认显示的图片资源
     */  
    protected void loadBitmap(String urlStr, ImageView image) {  
    	   	
    	
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
