package com.demo.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.demo.jiuwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyAdapter extends BaseAdapter {
	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
    /** 
     *  
     * @param urlStr 所需要加载的图片的url，以String形式传进来，可以把这个url作为缓存图片的key 
     * @param image ImageView 控件 
     * @param resId 默认显示的图片资源
     */  
    protected void loadBitmap(String urlStr, ImageView image,int resId) {  
    	   	
    	
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
    	        //.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
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
