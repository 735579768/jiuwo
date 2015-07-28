package com.demo.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDecode {
	private String jsonStr;
	private String curJSONStr;//当前的json字符串
	private JSONObject jsonObj;
	private JSONArray jsonArr;
	public JSONDecode(String str) throws JSONException{
		jsonStr=str;
		curJSONStr=str;
	}
	private Boolean isJSONObject(String jsonStr){
		String regEx = "^\\{.*?$";  
	    Pattern pattern = Pattern.compile(regEx);  
	    Matcher matcher = pattern.matcher(jsonStr);  
	    boolean b = matcher.matches();  
	    return b;
	}
	public static JSONDecode getInstance(String JSONStr) throws JSONException{
		return new JSONDecode(JSONStr);
	}
	public JSONDecode reset(){
		curJSONStr=jsonStr;
		return this;
	}
	public JSONDecode reset(String resetjsonStr){
		curJSONStr=resetjsonStr;
		return this;
	}
	public JSONDecode getString(String key) throws JSONException{
			jsonObj=new JSONObject(curJSONStr);
			curJSONStr=jsonObj.getString(key).toString();					
			return this;
	}
	public JSONDecode getString(int key) throws JSONException{
			jsonArr=new JSONArray(curJSONStr);
			curJSONStr=((JSONObject)jsonArr.opt(key)).toString();
		return this;
}
	public String toString(){
		return curJSONStr;
	}
	public int toInt(){
		return  Integer.parseInt(curJSONStr);
	}
	public JSONObject toJSONObject() throws JSONException{
		jsonObj=new JSONObject(curJSONStr);
		return jsonObj;
	}
	public JSONArray toJSONArray() throws JSONException{
		jsonArr=new JSONArray(curJSONStr);
		return jsonArr;
	}
	public String[] toStringArray(){
		String [] str=new String[jsonArr.length()];
		 for(int i = 0; i < jsonArr.length() ; i++){ 
			 str[i]=jsonArr.optString(i).toString();
		 }
		return str;
	}
}
