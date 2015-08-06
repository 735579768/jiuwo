package com.ex;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.core.GLOBAL;
import com.demo.jiuwo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MyPopupWindow extends PopupWindow {
	private Context mContext;
	private String goods_id;
	private View mMenuView;
	private Button cancel,btn_addorder;
	private EditText order_name,order_mobile,order_address;
	public MyPopupWindow(Activity context,String goodsid) {
		super(context);
		goods_id=goodsid;
		mContext=context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|PopupWindow.INPUT_METHOD_NEEDED);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
		
		order_name=(EditText)mMenuView.findViewById(R.id.order_name);
		order_mobile=(EditText)mMenuView.findViewById(R.id.order_mobile);
		order_address=(EditText)mMenuView.findViewById(R.id.order_address);
		btn_addorder=(Button)mMenuView.findViewById(R.id.btn_addorder);
		btn_addorder.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ordername=order_name.getText().toString();
				String ordermobile=order_mobile.getText().toString();
				String orderaddress=order_address.getText().toString();
				if(TextUtils.isEmpty(ordername) || TextUtils.isEmpty(ordermobile)||TextUtils.isEmpty(orderaddress)){
					//Toast.makeText(mContext, "信息不能为空", 3000).show();
					GLOBAL.msg(mContext,"信息不能为空");
				}else{
					//提交订单信息
					List<NameValuePair> params = new ArrayList<NameValuePair>(); 
					params.add(new BasicNameValuePair("goods_id",goods_id));
					params.add(new BasicNameValuePair("order_name",ordername));
					params.add(new BasicNameValuePair("order_mobile",ordermobile));
					params.add(new BasicNameValuePair("order_address",orderaddress));
					String result=GLOBAL.postUrl(GLOBAL.ADD_MIAOSHA_ORDER, params);
					if(!TextUtils.isEmpty(result)){
						try {
							JSONObject obj=new JSONObject(result);
							GLOBAL.msg(mContext,obj.getString("info"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						GLOBAL.msg(mContext,"订单提交失败");
					}
				}
			}
			
		});		
		cancel=(Button)mMenuView.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		//View
		this.setContentView(mMenuView);
		//设置弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置弹出窗体可点击
		this.setFocusable(true);
		//设置弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
	}
}
