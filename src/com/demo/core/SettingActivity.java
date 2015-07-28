package com.demo.core;

import com.demo.jiuwo.R;
import com.demo.jiuwo.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity {
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        addPreferencesFromResource(R.xml.setting);  
    }  
}
