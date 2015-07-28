package com.demo.jiuwo.ui;

import com.demo.jiuwo.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 使用方法       
 *显示
 *  if (progressDialog == null){  
            progressDialog = CustomProgressDialog.createDialog(this);  
            progressDialog.setMessage("正在加载中...");  
        }  
          
        progressDialog.show();  
        //隐藏
        if (progressDialog != null){  
            progressDialog.dismiss();  
            progressDialog = null;  
        } 
 * */
public class CustomProgressDialog extends Dialog {
	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;
	
	public CustomProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static CustomProgressDialog createDialog(Context context){
		if(customProgressDialog==null){
		customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
		customProgressDialog.setCancelable(true);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		//下面这一句去掉黑色边框
		customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		//customProgressDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		//去掉对话框的全屏背景start
		WindowManager.LayoutParams lp=customProgressDialog .getWindow().getAttributes();
		lp.dimAmount=0f;
		lp.alpha = 0.8f; 
		customProgressDialog .getWindow().setAttributes(lp);
		}
		//end
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable=(AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public CustomProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    public void hide(){
    	customProgressDialog.dismiss();
    	customProgressDialog=null;
    }
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public CustomProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
}
