package com.demo.core;
/**系统自定义消息**/
public class MSG_TYPE{
	/**消息基数**/
	private static final int MSG_BASE=111111;	//更新秒杀列表
	/**更新秒杀列表**/
	public static final int MSG_UPDATE_MIAOSHA=MSG_BASE+1;	//更新秒杀列表
	/**秒杀加载完毕**/
	public static final int MSG_LOADOVER_MIAOSHA=MSG_BASE+2;	
	/**数据加载完毕**/
	public static final int MSG_DATA_LOADOVER=MSG_BASE+3;	
	/**显示进度条**/
	public static final int MSG_SHOW_PROGRESSBAR=MSG_BASE+5;	//更新秒杀列表
	/**隐藏进度条**/
	public static final int MSG_HIDE_PROGRESSBAR=MSG_BASE+6;	//更新秒杀列表
}


