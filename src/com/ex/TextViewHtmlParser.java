package com.ex;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class TextViewHtmlParser implements Html.ImageGetter{
	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
	private TextView mTextView;
	private String URL_PREFIX="";
	private Context mContext;
	private boolean isFullWidth=false;
    public TextViewHtmlParser(Context c,TextView tv) {
        this.mContext=c;
        this.mTextView=tv;
      }
	public TextViewHtmlParser(Context c) {
      this.mContext=c;
    }
    public void setTextViewHtml(TextView textview,String htmlStr){
    	htmlStr=regStr(htmlStr);
    	this.mTextView = textview;
    	mTextView.setText(Html.fromHtml(htmlStr,this,null));  
    }
    public TextViewHtmlParser setUrlPrefix(String str){
    	this.URL_PREFIX=str;
    	return this;
    }
    public TextViewHtmlParser setFullWidth(boolean b){
    	this.isFullWidth=b;
    	return this;
    }
    //对字符串进行一些操作
    private String regStr(String str){
    	//str=str.replaceAll("", "");
    	return str;
    }
    @Override
    public Drawable getDrawable(String source) {
      final URLDrawable urlDrawable = new URLDrawable();
		// 不存在文件时返回默认图片，并异步加载网络图片
/*		Resources res = mContext.getResources();
		URLDrawable drawable = new URLDrawable(res.getDrawable(R.drawable.default_1px));*/
		
      WindowManager wm = (WindowManager)mContext.getApplicationContext()
              .getSystemService(Context.WINDOW_SERVICE);
    final int width = wm.getDefaultDisplay().getWidth();  
      //预先设置图片加载前的宽高
    urlDrawable.setBounds(0, 0, width,width/3); 
  	source=this.URL_PREFIX+source;
  	//返回一个实例
  	ImageLoader imageLoader = ImageLoader.getInstance();
  	imageLoader.loadImage(source, new SimpleImageLoadingListener() {  
          @Override  
          public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {  
        	  
        	  //缩放图片宽为全屏
        	  Matrix matrix = new Matrix();         
		      int w=bitmap.getWidth();
		      if(isFullWidth){
		      float bili=(float)width/(float)w;
        	  matrix.postScale(bili,bili); //长和宽放大缩小的比例
		      }
        	  bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		      urlDrawable.bitmap = bitmap; 
		      urlDrawable.setBounds(0, 0,dip2px(mContext,bitmap.getWidth()), dip2px(mContext,bitmap.getHeight())); 
              mTextView.invalidate();  
              mTextView.setText(mTextView.getText()); // 解决图文重叠  
          }  
      });  
      return urlDrawable;
    }
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
	   public  class URLDrawable extends BitmapDrawable {
	  	  protected Bitmap bitmap;
	
	  	  @Override
	  	  public void draw(Canvas canvas) {
	  	    if (bitmap != null) {
	  	      canvas.drawBitmap(bitmap, 0, 0, getPaint());
	  	    }
	  	  }
	  }
}

