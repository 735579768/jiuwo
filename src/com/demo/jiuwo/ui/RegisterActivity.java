package com.demo.jiuwo.ui;

import java.util.ArrayList;
import java.util.List;

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
					 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
					 params.add(new BasicNameValuePair("username", uStr));
					 params.add(new BasicNameValuePair("password",pStr));
					String reStr=GLOBAL.postUrl(GLOBAL.USER_REGISTER, params);
					String status;
					try {
						status = JSONDecode.getInstance(reStr).getString("status").toString();
						if(status.equals("1")){
							//注册成功
						}else{
							Toast.makeText(this,reStr,3000).show();
							//跳转到登陆界面
							Intent intent=new Intent();
							intent.setClass(this,LoginActivity.class);
							startActivity(intent);
							finish();
							inleft();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					}else{
						Toast.makeText(this,"两次输入密码不一致",3000).show();
					}
				//}
				break;
		}
	}  
}
