package com.demo.core;
/**系统自定义消息**/
public class MSG_TYPE{
	/**消息基数**/
	private static final int MSG_BASE=111111;	//更新秒杀列表
	/**更新秒杀列表**/
	public static final int MSG_UPDATE_MIAOSHA=MSG_BASE+1;	//更新秒杀列表
	/**秒杀加载完毕**/
	public static final int MSG_LOADOVER_MIAOSHA=MSG_BASE+2;	
}


