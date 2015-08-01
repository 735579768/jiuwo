package com.demo.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class DecodeCodeActivity extends Activity{
	private String strCode;
public DecodeCodeActivity(){
	
}
public DecodeCodeActivity(String str){
	this.strCode=str;
}
public void toActivity(){
	String regEx = "^http\\://.*$";  
    Pattern pattern = Pattern.compile(regEx);  
    Matcher matcher = pattern.matcher(this.strCode);  
    boolean b = matcher.matches();

    if(b){
    	try{
    	Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(this.strCode));
		startActivity(intent);
    	}catch(Exception e){
    		Log.v("liulan",e.getMessage());
    	}
    	}
}
}
