package com.demo.jiuwo.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.core.BaseActivity;
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
				if(!Verify.getInstance().checkVerify(verify.getText().toString())){
					Toast.makeText(this,"验证码不正确",3000).show();
				}else{
					String uStr=username.getText().toString();
					String pStr=password.getText().toString();
					String repStr=repassword.getText().toString();
					if(pStr.equals(repStr)){
					String uri="http://www.0yuanwang.com/Public/login/username/"+uStr+"/password/"+pStr+"/";
					String reStr=getUrlPage(uri);
					}else{
						Toast.makeText(this,"两次输入密码不一致",3000).show();
					}
				}
				break;
		}
	}  
}
