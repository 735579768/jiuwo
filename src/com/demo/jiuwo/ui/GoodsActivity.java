package com.demo.jiuwo.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.core.AsyncImageLoader;
import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.jiuwo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class GoodsActivity extends BaseActivity {
	private TextView title,price,check_goodsdescr;
	private ImageView goodspic;
	private String goods_id;
	protected ImageView goback;
	private TextView tvTopTitle;
	static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	/* (non-Javadoc)
	 * @see com.demo.jiuwo.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		title=(TextView)findViewById(R.id.goods_title);
		price=(TextView)findViewById(R.id.goods_price);
		check_goodsdescr=(TextView)findViewById(R.id.check_goodsdescr);
		goodspic=(ImageView)findViewById(R.id.goods_pic);
		
		goback= (ImageView)findViewById(R.id.goback);
		tvTopTitle= (TextView)findViewById(R.id.tv_top_title);
		tvTopTitle.setText("产品详情");
		goback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		Bundle bundle=this.getIntent().getExtras();
//		if(bundle==null){
			goods_id=getIntent().getStringExtra("goods_id").toString();
/*		}else{
		goods_id=bundle.getString("goodsid");
		}*/
		String jsongoods=getUrlPage(GLOBAL.GET_GOODSINFO_URL+"goods_id/"+goods_id);
		try{
		JSONObject info=new JSONObject(jsongoods);
		title.setText(info.getString("title"));
		price.setText(info.getString("price"));
		//loadUrl("http://app.0yuanwang.com/Api/getGoodsDesc/goods_id/"+goodsid);
		
		//加载图片
		loadimg(info.getString("pic"),goodspic);

		check_goodsdescr.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				Bundle bundle=new Bundle();
				bundle.putString("goodsid", goods_id);*/
				Intent intent=new Intent();
				intent.putExtra("goods_id", goods_id);
				intent.setClass(GoodsActivity.this, LoadurlActivity.class);
				startActivity(intent);
			}
			
		});
		//goodspic.setMinWidth(screenWidth);
		//goodspic.setMinHeight(screenWidth * 5);// 这里其实可以根据需求而定，我这里测试为最大宽度的5倍
		
		}catch(Exception e){
			e.printStackTrace();	
		}
		}
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
/*			Intent intent = new Intent();
			intent.setClass(GoodsActivity.this,HomeActivity.class);
			startActivity(intent);*/
			finish();
			inleft();
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }  
	private void loadimg(String urlStr,ImageView image){
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
                
/*        	    ViewGroup.LayoutParams params = imageView.getLayoutParams(); 
        	    params.height =loadedImage.getHeight(); 
        	    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        	    imageView.setLayoutParams(params);*/ 
            } 
        } 
    	}catch(Exception e){
    		Log.v("catch1",e.getMessage());
    	}
    }
});	
	}
}
