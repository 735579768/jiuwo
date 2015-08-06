package com.demo.jiuwo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends BaseActivity implements OnClickListener{
	private Button regster_btn;
	private ImageView verifty_img;
	private EditText username,password,repassword,verify;

	/* (non-Javadoc)
	 * @see com.demo.jiuwo.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		/*imageLoader = ImageLoader.getInstance();*/
		regster_btn=(Button)findViewById(R.id.register_btn);
		regster_btn.setOnClickListener(this);

		verifty_img=(ImageView)findViewById(R.id.verifty_img);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		repassword=(EditText)findViewById(R.id.repassword);
		verify=(EditText)findViewById(R.id.verify);
		
		verifty_img.setImageBitmap(Verify.getInstance().createBitmap());
		//imageLoader.displayImage("http://app.0yuanwang.com/Public/verify/random/"+Math.random()+".html",verifty_img);
		verifty_img.setOnClickListener(this);
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
			case R.id.verifty_img:
				verifty_img.setImageBitmap(Verify.getInstance().createBitmap());
				//imageLoader.displayImage("http://app.0yuanwang.com/Public/verify/random/"+Math.random()+".html",verifty_img);
				break;
			case R.id.register_btn:
/*				if(!Verify.getInstance().checkVerify(verify.getText().toString())){
					Toast.makeText(this,"验证码不正确",3000).show();
				}else{*/
					String uStr=username.getText().toString();
					String pStr=password.getText().toString();
					String repStr=repassword.getText().toString();
					if(pStr.equals(repStr)){
						boolean a =false,b = false;
						try{
						//验证账号是不是合法
						String regEx = "^(1[0-9]{10})|([a-zA-Z0-9]{1,15}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5})$";  
					    Pattern pattern = Pattern.compile(regEx);  
					    Matcher matcher = pattern.matcher(uStr);  
					    a = matcher.matches();
					    //验证密码是不是合法6到10位
						regEx = "^.{6,10}$";  
						pattern = Pattern.compile(regEx);  
						matcher= pattern.matcher(pStr);  
					    b = matcher.matches();
						}catch(Exception e){
							e.printStackTrace();
						}
					    if(!a){
					    	GLOBAL.msg(this,"用户名请输入手机号或邮箱！");
					    }else if(!b){
					    	GLOBAL.msg(this,"请输入6至10位的密码！");
					    }else{   
							 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
							 params.add(new BasicNameValuePair("rusername", uStr));
							 params.add(new BasicNameValuePair("rpassword",pStr));
							String reStr=GLOBAL.postUrl(GLOBAL.USER_REGISTER, params);
							String status;
							try {
								status = JSONDecode.getInstance(reStr).getString("status").toString();
								if(status.equals("1")){
									//注册成功
									GLOBAL.msg(this,"恭喜您注册成功");
									//跳转到登陆界面
									Intent intent=new Intent();
									intent.setClass(this,LoginActivity.class);
									startActivity(intent);
									finish();
									inleft();
								}else{
									//注册失败提示信息
									GLOBAL.msg(this,JSONDecode.getInstance(reStr).getString("info").toString());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    }
					}else{
						GLOBAL.msg(this,"两次输入密码不一致");
					}
					
				//}
				break;
		}
	}  
}
