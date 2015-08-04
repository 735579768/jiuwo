package com.demo.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo.adapter.HomeGoodsListViewAdapter.ListItemView;
import com.demo.jiuwo.R;
import com.demo.jiuwo.ui.MiaoshaDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MiaoshaListViewAdapter extends MyAdapter {
	   private int showNum=-1;
	   private Context context;                        //运行上下文   
	    private List<Map<String, Object>> listItems;    //商品信息集合   
	    private LayoutInflater listContainer;           //视图容器   
	    
	    public final class ListItemView{                //自定义控件集合          
	            public TextView title,price,goodsid,qianggou_btn;
	            public ImageView image; 
	    }    
	    public void removeAllItem(){
	    	//this.listItems=null;
	    	this.listItems.clear();
	    	//listItems.removeAll(listItems);
	    }
	    public MiaoshaListViewAdapter(Context context) {  
	        this.context = context;           
	        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
	        this.listItems=new ArrayList<Map<String, Object>>();
	    } 	      
	    public MiaoshaListViewAdapter(Context context, List<Map<String, Object>> listItems) {  
	        this.context = context;           
	        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
	        this.listItems = listItems;  
	    }  
	  public void addItem(Map<String,Object> o){
		  listItems.add(o);
	  }
	    public int getCount() {  
	        // TODO Auto-generated method stub   
	        return listItems.size();  
	    }  
	  
	    public Object getItem(int arg0) {  
	        // TODO Auto-generated method stub   
	        return this.listItems.get(arg0);  
	    } 
	  
	    public long getItemId(int arg0) {  
	        // TODO Auto-generated method stub   
	        return 0;  
	    }  
	      
	      
	         
	    /** 
	     * ListView Item设置 
	     */  
	    public View getView(final int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub 


    	//自定义视图   
	    ListItemView  listItemView=null; 
        if (convertView == null) { 
	        	Log.v("suoyin",""+position);
	        	
	            listItemView = new ListItemView();   
	            //获取list_item布局文件的视图   
	            convertView = listContainer.inflate(R.layout.list_item_miaosha, null); 
	            //获取控件对象     
	            listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);
	            listItemView.price = (TextView)convertView.findViewById(R.id.price);
	            listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem);
	            listItemView.goodsid = (TextView)convertView.findViewById(R.id.goodsid);
	            listItemView.qianggou_btn = (TextView)convertView.findViewById(R.id.qianggou_btn);

		        convertView.setTag(listItemView); 
		  }else{
			  listItemView = (ListItemView)convertView.getTag(); 	
		  }
        String imgurl=(String) listItems.get(position).get("pic");
        //防止图片重新加载闪烁的情况
        if(listItemView.image.getTag(R.id.tag_first)!=null){
	        if(listItemView.image.getTag(R.id.tag_first).toString()!=imgurl){
	        	//设置一个标记
	            listItemView.image.setTag(R.id.tag_first,imgurl);
	            loadBitmap(imgurl,listItemView.image,R.drawable.default_ico);
	        }
        }else{
            listItemView.image.setTag(R.id.tag_first,imgurl);
            loadBitmap(imgurl,listItemView.image,R.drawable.default_ico);
        }
        listItemView.qianggou_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "索引："+position,1000).show();
				Map<String,Object> map=listItems.get(position);
				Object obj=map.get("goods_id");
				String str=obj.toString();
				Intent intent=new Intent();
				intent.putExtra("goods_id",str);
				intent.setClass(context,MiaoshaDetailActivity.class);
				context.startActivity(intent);
				
			}
        	
        });
        listItemView.goodsid.setText((String) listItems.get(position)  
                .get("goodsid"));  
        listItemView.title.setText((String) listItems.get(position)  
                .get("title"));  
        listItemView.price.setText((String) listItems.get(position)  
                .get("price"));   
	        return convertView; 
}  
	 
}
