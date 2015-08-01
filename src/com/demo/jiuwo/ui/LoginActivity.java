package com.demo.jiuwo.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.core.BaseActivity;
import com.demo.core.GLOBAL;
import com.demo.core.JSONDecode;
import com.demo.jiuwo.R;
import com.ex.Verify;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private Button login_btn,regster_btn;
	private ImageView verifty_img;

	private EditText username,password,verify;

	/* (non-Javadoc)
	 * @see com.demo.jiuwo.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		regster_btn=(Button)findViewById(R.id.register_btn);
		login_btn=(Button)findViewById(R.id.login_btn);
		regster_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		/*imageLoader = ImageLoader.getInstance();*/
		verifty_img=(ImageView)findViewById(R.id.verifty_img);
		verifty_img=(ImageView)findViewById(R.id.verifty_img);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		verify=(EditText)findViewById(R.id.verify);
		
		verifty_img.setImageBitmap(Verify.getInstance().createBitmap());
		verifty_img.setOnClickListener(this);
		}
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {       
			finish();
			inleft();
             return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.register_btn:
			Intent intent=new Intent();
			intent.setClass(this,RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login_btn:
			login_btn.setEnabled(false);
			if(!Verify.getInstance().checkVerify(verify.getText().toString())){
				Toast.makeText(this,"验证码不正确",3000).show();
			}else{
				String uStr=username.getText().toString();
				String pStr=password.getText().toString();
			     List<NameValuePair> params = new ArrayList<NameValuePair>(); 
			     params.add(new BasicNameValuePair("username", uStr));
			     params.add(new BasicNameValuePair("password", pStr));
				String uri="http://app.0yuanwang.com/Public/login.html";
				String reStr=postUrl(uri,params);
				login_btn.setEnabled(true);
				if(!TextUtils.isEmpty(reStr)){
				try {
					reStr=new String(reStr.getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					try {
						String status=JSONDecode.getInstance(reStr).getString("status").toString();
						if(status.equals("1")){
							//登陆成功保存用户名密码
							String userinfo=JSONDecode.getInstance(reStr).getString("info").toString();
							GLOBAL.saveData(this, "userinfo", userinfo);
							Toast.makeText(this,"登陆成功", 3000).show();
							intent=new Intent();
							intent.setClass(this, MainLayoutActivity.class);
							//Toast.makeText(this,reStr,3000).show();
							//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//刷新
							startActivity(intent);
							finish();					
						}else{
							Toast.makeText(this,reStr,3000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			break;
		case R.id.verifty_img:
			verifty_img.setImageBitmap(Verify.getInstance().createBitmap());
			break;
		}
	}  
	/**
     * 使用SharedPreferences保存用户登录信息
     * @param context
     * @param username
     * @param password
     */
    public  void saveLoginInfo(Context context,String userinfo){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        //获取Editor对象
        Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("userinfo", userinfo);
        //提交
        editor.commit();
        
/*        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        String password=sharedPre.getString("password", "");*/

    }
    /**
     * POST请求
     * 添加参数方法如下
     * List<NameValuePair> params = new ArrayList<NameValuePair>(); 
     * params.add(new BasicNameValuePair("action", "downloadAndroidApp"));
     * */
    private String postUrl(String uri,List<NameValuePair> params){
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
