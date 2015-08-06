package com.demo.jiuwo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.adapter.MemListViewAdapter;
import com.demo.core.GLOBAL;
import com.demo.core.LoginVerifyFragment;
import com.demo.core.MSG_TYPE;
import com.demo.jiuwo.R;
import com.ex.UpdateVersion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProinfoFragment extends  LoginVerifyFragment{
	private JSONObject userobj;
	protected  ProgressBar  progressBar;
	protected ListView listview;
	protected ImageView goback;
	private TextView tvTopTitle,account;
	private LinearLayout login_reg_block;
	private Integer[] imageIDs={R.drawable.rico,R.drawable.rico,R.drawable.rico,R.drawable.rico,R.drawable.rico,R.drawable.rico};
	String [] menutitle={"购物车","全部订单","未付款订单","已付款订单","检查更新","注销账户"};
	private List<Map<String, Object>> listItems; //菜单列表
	private MemListViewAdapter memviewadapter;   //菜单适配器
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view= inflater.inflate(R.layout.activity_proinfo,
				container, false);
		//setContentView(R.layout.activity_proinfo);
		listview= (ListView) view.findViewById(R.id.menulist);	
		login_reg_block= (LinearLayout) view.findViewById(R.id.login_reg_block);	
		goback= (ImageView)view.findViewById(R.id.goback);
		tvTopTitle= (TextView)view.findViewById(R.id.tv_top_title);
		account= (TextView)view.findViewById(R.id.account);
		tvTopTitle.setText("个人中心");

		goback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}});
		this.getUserinfo();
		//列表元素添加
		listItems=getListItems();
		memviewadapter = new MemListViewAdapter(getActivity(), listItems); //创建适配器   
	    listview.setAdapter(memviewadapter);  
	    initListeren();
		return view;
	}
	   /** 
     * 初始化列表信息
     */  
    private List<Map<String, Object>> getListItems() {  
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();  
        for(int i = 0; i < menutitle.length; i++) {  
            Map<String, Object> map = new HashMap<String, Object>();               //图片资源   
            map.put("image", imageIDs[i]); 
            map.put("title", menutitle[i]);              //物品标题   
            listItems.add(map);  
        }     
        return listItems;  
    }  
    /**
     * 初始化监听器
     * */
    private void initListeren(){
		//设置点击
	    listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			//arg0:就是你的listview   arg2:点击的item的位置。和你的数组的下标相等。arg3:被电击view的id
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	               // TODO Auto-generated method stub
				//String titleitem=listItems.get(arg2).get("title").toString();	
				Intent intent=new Intent();
				switch(arg2){
				case 0://购物车
					intent.setClass(getActivity(),CartActivity.class);
					startActivity(intent);
					inright();						
					break;
				case 1://全部订单

					break;
				case 2://未付订单
					break;
				case 3://已付订单
					break;
				case 4://更新
					new UpdateVersion(getActivity()).checkVersion(GLOBAL.UPDATE_VERSION_URL,false);
					break;
				case 5://注销账户
					GLOBAL.saveData(getActivity(),"userinfo","");
					GLOBAL.USERINFO="";
					GLOBAL.msg(getActivity(), "账户注销成功");
					intent.setClass(getActivity(),MainLayoutActivity.class);
					startActivity(intent);
					getActivity().finish();
					inright();					
					break;
				}
			}
             
        });
    }
    private void getUserinfo(){
    	new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//取用户信息
				List<NameValuePair> params = new ArrayList<NameValuePair>(); 
				params.add(new BasicNameValuePair("userinfo",GLOBAL.USERINFO));
				String str=GLOBAL.postUrl(GLOBAL.GET_MEMBERINFO_URL, params);
				String status;
				try {
					JSONObject obj=new JSONObject(str);
					status = obj.getString("status").toString();
					if(status.equals("1")){
						userobj=new JSONObject(obj.getString("info"));
						//更新界面
						sendMessage(MSG_TYPE.MSG_UPDATE_PROINFO);
					}else{
						//去登陆
						Intent intent=new Intent();
						intent.setClass(getActivity(), LoginActivity.class);
						startActivity(intent);
						getActivity().finish();
						inright();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    		
    	}).start();

    }
	private Handler handler=new Handler(){
	    public void handleMessage(Message msg) { 
        	switch(msg.what){
        	case MSG_TYPE.MSG_UPDATE_PROINFO:
        		try {
        			String htmlStr="账号："+userobj.getString("account").toString();
        			htmlStr+="<br>邮箱："+userobj.getString("email").toString();
        			htmlStr+="<br>手机号："+userobj.getString("mobile").toString();
					account.setText(Html.fromHtml(htmlStr)); 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		break;
        	default:
        		break;
        	}
        	super.handleMessage(msg);  
        };  
	};
	
	public void sendMessage(int msg_type){
        Message msg1=handler.obtainMessage();
        msg1.what=msg_type;
        handler.sendMessage(msg1);
	}
}
