package com.ex;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.demo.core.GLOBAL;
import com.demo.core.MainActivity;
import com.demo.jiuwo.R;
import com.demo.jiuwo.ui.GoodsActivity;
import com.demo.jiuwo.ui.MessageDetailActivity;
import com.demo.jiuwo.ui.Qidong1Activity;


public class PushTestReceiver extends PushMessageReceiver {
	public static final int MESSAGE_TYPE=1;//通知信息类型
	public static final int GOODS_TYPE=2;//通知信息类型
	   /** TAG to Log */
    public static final String TAG = PushMessageReceiver.class
            .getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context
     *            BroadcastReceiver的执行Context
     * @param errorCode
     *            绑定接口返回值，0 - 成功
     * @param appid
     *            应用id。errorCode非0时为null
     * @param userId
     *            应用user id。errorCode非0时为null
     * @param channelId
     *            应用channel id。errorCode非0时为null
     * @param requestId
     *            向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(final Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 绑定成功
        	GLOBAL.BAIDU_APP_ID=appid;
        	GLOBAL.BAIDU_USER_ID=userId;
        	GLOBAL.BAIDU_CHANNEL_ID=channelId;
        	GLOBAL.BAIDU_REQUEST_ID=requestId;
        	
    		//记录设备信息
    		//String str=GLOBAL.getData(context, "SHEBEIINFO");
    	//	if(!str.equals("true")){
				final List<NameValuePair> params = new ArrayList<NameValuePair>(); 
				params.add(new BasicNameValuePair("appid", appid));
				params.add(new BasicNameValuePair("userid", userId));
				params.add(new BasicNameValuePair("channelid", channelId));
				params.add(new BasicNameValuePair("requestid", requestId));
				params.add(new BasicNameValuePair("userinfo", GLOBAL.getData(context, "userinfo")));
    			new Thread(new Runnable(){
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    					String str=GLOBAL.postUrl(GLOBAL.SHEBEI_URL,params);
    					if(str.equals("success")){
    	//					GLOBAL.saveData(context, "SHEBEIINFO", "true");
    					}
    				}
    				
    			}).start();			
    		}
     //   }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context
     *            上下文
     * @param message
     *            推送的消息
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, messageString);
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);
        String goodsid="";
        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("goods_id")) {
                    myvalue = customJson.getString("goods_id");
                    goodsid=myvalue;
                } 
                int type=Integer.parseInt(customJson.getString("type").toString());
                Intent intent=new Intent();
                switch(type){
                case MESSAGE_TYPE :
                	//通知信息
                	String message_id=customJson.getString("message_id");
                	intent.putExtra("message_id", message_id);
                	intent.setClass(context.getApplicationContext(), MessageDetailActivity.class);
                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                	context.getApplicationContext().startActivity(intent);
                	break;
                case GOODS_TYPE:
                	//产品信息
                	String goods_id=customJson.getString("goods_id");
                	intent.putExtra("goods_id", goods_id);
                	intent.setClass(context.getApplicationContext(), GoodsActivity.class);
                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                	context.getApplicationContext().startActivity(intent);
                	break;
                }
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
/*        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("goods_id",goodsid);
        intent.setClass(context.getApplicationContext(), GoodsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.getApplicationContext().startActivity(intent);*/
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
            String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags
     *            设置成功的tag
     * @param failTags
     *            设置失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags
     *            成功删除的tag
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示列举tag成功；非0表示失败。
     * @param tags
     *            当前应用设置的所有tag。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
            String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {

        
    }
	   /**
* POST请求
* 添加参数方法如下
* List<NameValuePair> params = new ArrayList<NameValuePair>(); 
* params.add(new BasicNameValuePair("action", "downloadAndroidApp"));
* */
protected String postUrl(String uri,List<NameValuePair> params){
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
