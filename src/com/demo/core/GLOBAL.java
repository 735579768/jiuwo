package com.demo.core;

import java.io.IOException;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GLOBAL {
	//百度云推送信息
	public static  String BAIDU_USER_ID;
	public static  String BAIDU_APP_ID;
	public static  String BAIDU_CHANNEL_ID;
	public static  String BAIDU_REQUEST_ID;
	
	//数据请求地址
	/**登陆用户的JSON加密信息**/
	public static String USERINFO="";
	
	/**更新版本地址**/
	public static final String UPDATE_VERSION_URL="http://app.0yuanwang.com/version.xml";
	
	/**取秒杀订单列表地址**/
	public static final String GET_MIAOSHA_ORDER_URL="http://app.0yuanwang.com/Api/getMiaoshaOrderList/";
	
	/**取用户信息地址**/
	public static final String GET_MEMBERINFO_URL="http://app.0yuanwang.com/Api/getUserinfo/";
	
	/**记录设备信息地址**/
	public static final String SHEBEI_URL="http://app.0yuanwang.com/Api/shebeijilu/";
	
	/**取最新消息列表地址**/
	public static final String MESSAGE_URL="http://app.0yuanwang.com/Api/getMessageList/";
	
	/**取单个消息地址**/
	public static final String MESSAGE_DETAIL_URL="http://app.0yuanwang.com/Api/getMessageInfo/";
	
	/**搜索产品列表地址**/
	public static final String GOODS_LISTS_URL="http://app.0yuanwang.com/Api/getgoodslist/";
	
	/**取分类列表地址**/
	public static final String GOODS_CATEGORY_LISTS_URL="http://app.0yuanwang.com/Api/getcategorylist/";
	
	
	/**产品详情描述列表地址**/
	public static final String GOODS_DESCR_URL="http://app.0yuanwang.com/Api/getGoodsDesc/";
	
	/**商品选购和礼品赠送区地址**/
	public static final String GOODS_A_LISTS_URL="http://app.0yuanwang.com/Api/getXuangouList/";
	
	/**代购列表地址**/
	public static final String GOODS_DAIGOU_LISTS_URL="http://app.0yuanwang.com/Api/getDaigouList/";
	
	/**秒杀列表地址**/
	public static final String GOODS_MIAOSHA_LISTS_URL="http://app.0yuanwang.com/Api/getMiaoshaList/";
	
	/**首页礼品列表地址**/
	public static final String GOODS_B_LISTS_URL="http://app.0yuanwang.com/Api/getLipinList/";
	
	/**会员信息地址**/
	//public static final String PRO_INFO="http://app.0yuanwang.com/Member/info";
	
	/**购物车列表地址**/
	public static final String CART_INDEX_LIST="http://app.0yuanwang.com/Cart/index/";
	
	/**单个产品信息地址**/
	public static final String GET_GOODSINFO_URL="http://app.0yuanwang.com/Api/getGoodsInfo/";
	
	/**单个抢购产品信息地址**/
	public static final String GET_QG_GOODSINFO_URL="http://app.0yuanwang.com/Api/getQGGoodsInfo/";
	
	/**会员登陆地址**/
	public static final String USER_LOGIN="http://app.0yuanwang.com/Public/login/";
	
	/**会员注册地址**/
	public static final String USER_REGISTER="http://app.0yuanwang.com/Public/register/";
	
	/**提交秒杀订单地址**/
	public static final String ADD_MIAOSHA_ORDER="http://app.0yuanwang.com/Api/addMiaoshaOrder/";
	
	/**
     * 使用SharedPreferences保存用户登录信息
     * @param context
     * @param username
     * @param password
     */
    public static  void saveData(Context c,String key,String value){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=c.getSharedPreferences("config", c.MODE_PRIVATE);
        //获取Editor对象
        Editor editor=sharedPre.edit();
        //设置参数
        editor.putString(key,value);
        //提交
        editor.commit();

    }
    public static String getData(Context c,String key){
    	SharedPreferences sharedPre=c.getSharedPreferences("config",  c.MODE_PRIVATE);
        String str=sharedPre.getString(key, "");
        return str;
    }
	public static String getUrlPage(String url){
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
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
	        e.printStackTrace();
	        return e.getMessage().toString();          
	    } catch (IOException e) {
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
	        e.printStackTrace();
	        return e.getMessage().toString();      
	    } catch (Exception e) {
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
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
public static String postUrl(String uri,List<NameValuePair> params){
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
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
      e.printStackTrace(); 
  } catch (IOException e) { 
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
      e.printStackTrace(); 
  } 
  return reStr;
}
public static void msg(Context c,String str){
	Toast toast = Toast.makeText(c,
		     str, Toast.LENGTH_SHORT);
		   toast.setGravity(Gravity.CENTER, 0, 0);
		   LinearLayout view = (LinearLayout)toast.getView();
		   view.setPadding(100, 50, 100, 50);
		   TextView tv = (TextView)view.getChildAt(0);
		   tv.setTextSize(16);
		   toast.setView(view);
		   toast.show();
}
/** 
 * 半角转换为全角 
 *  
 * @param input 
 * @return 
 */  
public static String strToQuanjiao(String input) {  
    char[] c = input.toCharArray();  
    for (int i = 0; i < c.length; i++) {  
        if (c[i] == 12288) {  
            c[i] = (char) 32;  
            continue;  
        }  
        if (c[i] > 65280 && c[i] < 65375)  
            c[i] = (char) (c[i] - 65248);  
    }  
    return new String(c);  
}  
public static Handler handler = new Handler(){
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		//switch (msg.what) {
		GLOBAL.msg(MYApplication.mContext,"连接服务器失败");
		//}
		}
};
}
