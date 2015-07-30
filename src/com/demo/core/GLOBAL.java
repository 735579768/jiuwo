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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GLOBAL {
	//百度云推送信息
	public static  String BAIDU_USER_ID;
	public static  String BAIDU_APP_ID;
	public static  String BAIDU_CHANNEL_ID;
	public static  String BAIDU_REQUEST_ID;
	
	//数据请求地址
	
	//记录设备信息
	public static String SHEBEI_URL="http://app.0yuanwang.com/Api/shebeijilu/";
	public static String MESSAGE_URL="http://app.0yuanwang.com/Api/getMessageList/";
	public static String MESSAGE_DETAIL_URL="http://app.0yuanwang.com/Api/getMessageInfo/";
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
      e.printStackTrace(); 
  } catch (IOException e) { 
      e.printStackTrace(); 
  } 
  return reStr;
}
}
