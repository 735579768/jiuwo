package com.demo.core;

import android.app.Activity;
import android.widget.Toast;

public class Jshtml extends Activity{
	public void toast(String str){
		Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT).show();			
	}

}
