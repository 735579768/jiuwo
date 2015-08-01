package com.demo.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.demo.jiuwo.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryListViewAdapter extends MyAdapter {

	   private Context context;                        //运行上下文   
	    private List<Map<String, Object>> listItems;    //商品信息集合   
	    private LayoutInflater listContainer;           //视图容器   
	    
	    public final class ListItemView{                //自定义控件集合          
	            public TextView title;
	    }    
	    public void removeAllItem(){
	    	//this.listItems=null;
	    	this.listItems.clear();
	    	//listItems.removeAll(listItems);
	    }
	    public CategoryListViewAdapter(Context context) {  
	        this.context = context;           
	        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
	        this.listItems=new ArrayList<Map<String, Object>>();
	    } 	      
	    public CategoryListViewAdapter(Context context, List<Map<String, Object>> listItems) {  
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
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub 
	    	try{
	    	Log.v("adapter", "getView " + position + " " + convertView+"产品ID:"+((Map)(listItems.get(position)))  
	                .get("id"));
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	//自定义视图   
	        ListItemView  listItemView = null;  
	        if (convertView == null) {  
	            listItemView = new ListItemView();   
	            //获取list_item布局文件的视图   
	            convertView = listContainer.inflate(R.layout.list_item_text, null);  
	            //获取控件对象     
	            listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);
		        //设置控件集到convertView   
	            convertView.setTag(listItemView); 
		                 
	            //注册按钮点击时间爱你   
/*	            listItemView.title.setOnClickListener(new View.OnClickListener() {  
	                @Override  
	                public void onClick(View v) {  
	                    //显示物品详情   
	                  //  showDetailInfo(selectID);  
	                }  
	            });  */
 

	          

	        }else{
	        	listItemView = (ListItemView)convertView.getTag(); 	 
	        }	        
	        //设置文字和图片   
	        listItemView.title.setText((String) listItems.get(position)  
	                .get("title"));  
	        return convertView; 
	    }  
	 
}
