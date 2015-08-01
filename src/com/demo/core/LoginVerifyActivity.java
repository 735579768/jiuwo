package com.demo.core;

import com.demo.jiuwo.ui.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class LoginVerifyActivity extends BaseActivity{
	private String userinfo;
	/* (non-Javadoc)
	 * @see com.demo.core.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//登陆验证
	     userinfo=GLOBAL.getData(this, "userinfo");
	     if(TextUtils.isEmpty(userinfo)){
	    	 loginJump();
	     }
	}
	public  void loginJump(){
		Intent intent=new Intent();
		intent.setClass(LoginVerifyActivity.this,LoginActivity.class);
		startActivity(intent);
	} 

}
