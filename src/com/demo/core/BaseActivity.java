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
import com.demo.jiuwo.R.anim;
import com.demo.jiuwo.R.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected WebView webview;
	protected  ProgressBar  progressBar;
	protected String uri="http://app.0yuanwang.com/";
	boolean isExit;
	protected int animsj=3000;//启动动画跳转时间
	public BaseActivity(){

	}
	//左进右出
	protected void inleft(){
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		//overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	//右进左出
	protected void inright(){
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出九沃网",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	menu.add(0, 10, 10, "退出").setIcon(
	//			android.R.drawable.ic_menu_delete);
		menu.add(0,1,0,"首页");
		menu.add(0,8,9,"关于");
		menu.add(0,10,10,"退出");
		menu.add(0, 2, 3, "刷新");
		menu.add(0, 3, 1, "返回");
		menu.add(0, 4, 2, "前进");
		getMenuInflater().inflate(R.menu.main, menu);
		// this.exit(0);
		return true;
		
		/*或者：
        MenuInflater mflater=new MenuInflater(this);  

        mflater.inflate(R.menu.mainmenu, menu);  

        return super.onCreateOptionsMenu(menu); 
        

        getMenuInflater().inflate(R.menu.menuitem);

        return true;   */
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//取用户数据
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);   
		Editor editor = sharedPreferences.edit();//获取编辑器   
		editor.putString("account", "cu56");   
		editor.putInt("password", 123456);   
		editor.commit();//提交修改 
		
		SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);   
        String account = preferences.getString("account", "");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 10:
			showDialog(1);
			break;
		case 1:
			webview.loadUrl(uri);
			break;
		case 2:
			webview.reload();
			break;
		case 3:
			webview.goBack();
			break;
		case 4:
			webview.goForward();
			break;
		}// 可以再此扩展
		return false;
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new AlertDialog.Builder(this)
					.setTitle("你确定要退出")
					//.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									System.exit(0);
									// TODO Auto-generated method stub
								}
							}).setNegativeButton("取消", null).create();
		default:
			return null;
		}
	}
	protected String getUrlPage(String url){
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
}
