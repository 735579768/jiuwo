package com.demo.jiuwo.ui;

import com.demo.jiuwo.R;
import com.ex.UpdateVersion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainLayoutActivity extends FragmentActivity{
	private FragmentManager fm ;
	boolean isExit;
	private Fragment home,proinfo,cart;
	private ImageView img4,img1,img2,img3;
	/* (non-Javadoc)
	 * @see com.demo.jiuwo.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);


		img1=(ImageView)findViewById(R.id.iv_menu_1);
		img2=(ImageView)findViewById(R.id.iv_menu_2);
		img3=(ImageView)findViewById(R.id.iv_menu_3);
		img4=(ImageView)findViewById(R.id.iv_menu_4);
	
		fm = getSupportFragmentManager();
		home = new HomeFragment();
		proinfo = new ProinfoFragment();
		cart = new CartFragment();
		//初始化的时候需要显示一个fragment，假设我们显示第二个fragment
		//向容器中添加或者替换fragment时必须  开启事务  操作完成后   提交事务
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.main_container,home).commit();
		img1.setSelected(true);
		new UpdateVersion(this).checkVersion("http://app.0yuanwang.com/version.xml",true);
	}
	public void click(View v){
		v.setSelected(true);
		int id=v.getId();
		if(id==R.id.iv_menu_11 || id== R.id.iv_menu_1){
			img2.setSelected(false);
			img3.setSelected(false);
			img4.setSelected(false);
			FragmentTransaction ft1 = fm.beginTransaction();
			ft1.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left,
					R.anim.in_from_right, R.anim.out_to_left);
			ft1.replace(R.id.main_container,home );
			ft1.commit();
		}else if(id==R.id.iv_menu_22 || id== R.id.iv_menu_2){
			//跳转到搜索界面
			img1.setSelected(false);
			img3.setSelected(false);
			img4.setSelected(false);
			Intent intent=new Intent();
			intent.setClass(this,MessageListActivity.class);
			startActivity(intent);

	/*		FragmentTransaction ft2 = fm.beginTransaction();
			ft2.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
	                 android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			ft2.replace(R.id.main_container,home);
			ft2.commit();		*/
		}else if(id==R.id.iv_menu_33 || id== R.id.iv_menu_3){//购物车
			img1.setSelected(false);
			img2.setSelected(false);
			img4.setSelected(false);
			FragmentTransaction ft3 = fm.beginTransaction();
			ft3.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left,
					R.anim.in_from_right, R.anim.out_to_left);
			ft3.replace(R.id.main_container,cart);
			ft3.addToBackStack(null);
			ft3.commit();
		}else if(id==R.id.iv_menu_44 || id== R.id.iv_menu_4){//个人中心
			img1.setSelected(false);
			img2.setSelected(false);
			img3.setSelected(false);
			FragmentTransaction ft31 = fm.beginTransaction();
			ft31.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left,
					R.anim.in_from_right, R.anim.out_to_left);
			ft31.replace(R.id.main_container,proinfo);
			ft31.addToBackStack(null);
			ft31.commit();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出九沃网",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};
}
