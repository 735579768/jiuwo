package com.demo.core;
import java.io.IOException;  
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import android.os.AsyncTask;  
import android.support.v4.util.LruCache;  
import android.widget.ImageView;  
   
  
/** 
 * 图片异步加载类，有图片内存缓存 
 *  
 * @blog http://www.zhaokeli.com 
 *  
 */  
public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {  
    private ImageView image;  
    private LruCache<String, Bitmap> lruCache;  
    private final int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取当前应用程序所分配的最大内存
    private final int cacheSize = maxMemory / 5;//只分5分之一用来做图片缓存  
  
    /** 
     * 构造方法，需要把ImageView控件和LruCache 对象传进来 
     * @param image 加载图片到此 {@code}ImageView 
     * @param lruCache 缓存图片的对象 
     */  
    public AsyncImageLoader(String urlStr,ImageView image,int resId) {  
        super();  
        this.image = image;  
        //this.lruCache = lruCache;  
        this.lruCache=new LruCache<String, Bitmap>(  
	            cacheSize) {  
	        @Override  
	        protected int sizeOf(String key, Bitmap bitmap) {//复写sizeof()方法  
	            // replaced by getByteCount() in API 12  
	            return bitmap.getRowBytes() * bitmap.getHeight() / 1024; //这里是按多少KB来算  
	        }  
	    }; 
	    
        Bitmap bitmap = getBitmapFromMemoryCache(urlStr);//首先从内存缓存中获取图片  
        if (bitmap != null) {  
            image.setImageBitmap(bitmap);//如果缓存中存在这张图片则直接设置给ImageView  
        } else {  
            image.setImageResource(resId);//否则先设置成默认的图片  
            execute(urlStr);//然后执行异步任务AsycnTask 去网上加载图片  
        }  
    }  
  
    @Override  
    protected Bitmap doInBackground(String... params) {  
        Bitmap bitmap = null;  
        try {  
            bitmap = getBitmap(params[0]);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        addBitmapToMemoryCache(params[0], bitmap);  
        return bitmap;  
    }  
  
    @Override  
    protected void onPostExecute(Bitmap bitmap) {  
        image.setImageBitmap(bitmap);  
    }
    protected void setImageSize(){

		//goodspic.setMinimumWidth(screenWidth);
    	
    }
        //调用LruCache的put 方法将图片加入内存缓存中，要给这个图片一个key 方便下次从缓存中取出来  
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
            lruCache.put(key, bitmap);  
        }  
    }  
        //调用Lrucache的get 方法从内存缓存中去图片  
    public Bitmap getBitmapFromMemoryCache(String key) {  
        return lruCache.get(key);  
    }  
    public  Bitmap getBitmap(String urlStr) throws IOException{  
        Bitmap bitmap;  
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setRequestMethod("GET");  
        conn.setReadTimeout(5*1000);  
        conn.setDoInput(true);  
        conn.connect();  
        InputStream is = conn.getInputStream();  
        bitmap = BitmapFactory.decodeStream(is);  
        is.close();  
        return bitmap;  
    }  
}  