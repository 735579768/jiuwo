package com.demo.core;

public interface MyProgressBar {
	final static int SHOW_PROGRESSBAR = 100013;
	final static int HIDE_PROGRESSBAR = 100014;
    //显示进度条
    public void showProgressBar();
    //隐藏进度条
    public void hideProgressBar();
}
