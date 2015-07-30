package com.demo.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.demo.jiuwo.ui.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		 SharedPreferences sharedPre=getActivity().getSharedPreferences("config",0);
	     userinfo=sharedPre.getString("userinfo", "");
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
