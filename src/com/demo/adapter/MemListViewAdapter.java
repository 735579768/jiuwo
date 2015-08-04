package com.demo.adapter;

import java.util.List;
import java.util.Map;

import com.demo.jiuwo.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MemListViewAdapter extends MyAdapter {
	  private Context context;                        //运行上下文   
	    private List<Map<String, Object>> listItems;    //商品信息集合   
	    private LayoutInflater listContainer;           //视图容器   
	    public final class ListItemView{                //自定义控件集合          
	            public TextView title;        
	            public ImageView image; 
	    }    
	      
	      
	    public MemListViewAdapter(Context context, List<Map<String, Object>> listItems) {  
	        this.context = context;           
	        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
	        this.listItems = listItems;  
	    }  
	  
	    public int getCount() {  
	        // TODO Auto-generated method stub   
	        return listItems.size();  
	    }  
	  
	    public Object getItem(int arg0) {  
	        // TODO Auto-generated method stub   
	        return null;  
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
	        Log.e("method", "getView");   
	        //自定义视图   
	        ListItemView  listItemView = null;  
	        if (convertView == null) {  
	            listItemView = new ListItemView();   
	            //获取list_item布局文件的视图   
	            convertView = listContainer.inflate(R.layout.memlist_item, null);  
	            //获取控件对象     
	            listItemView.title = (TextView)convertView.findViewById(R.id.titleItem);
	            listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem);
	            //注册按钮点击时间爱你   
	            listItemView.title.setOnClickListener(new View.OnClickListener() {  
	                @Override  
	                public void onClick(View v) {  
	                    //显示物品详情   
	                  //  showDetailInfo(selectID);  
	                }  
	            });  
	            //设置控件集到convertView   
	            convertView.setTag(listItemView);  
		          
		        //设置文字和图片   
		        listItemView.image.setBackgroundResource((Integer) listItems.get(  
		                position).get("image"));  
		        listItemView.title.setText((String) listItems.get(position)  
		                .get("title")); 
	        }else {  
	            listItemView = (ListItemView)convertView.getTag();  
	        }  
 
	        return convertView;  
	    }  

}
