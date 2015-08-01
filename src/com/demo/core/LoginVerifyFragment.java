package com.demo.core;

import com.demo.jiuwo.ui.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginVerifyFragment extends BaseFragment{
	protected String userinfo;
	/* (non-Javadoc)
	 * @see com.demo.core.BaseFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//登陆验证
		userinfo=GLOBAL.getData(getActivity(), "userinfo");
	     if(TextUtils.isEmpty(userinfo)){
	    	 loginJump();
	     }
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/* (non-Javadoc)
	 * @see com.demo.core.BaseActivity#onCreate(android.os.Bundle)
	 */
	public  void loginJump(){
		Intent intent=new Intent();
		intent.setClass(getActivity(),LoginActivity.class);
		startActivity(intent);
	} 
}
