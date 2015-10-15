package com.mdground.yizhida.activity.base;

public interface BaseView {
	void showProgress();

	void hidProgress();
	
	void showToast(String text);
	void showToast(int resId);
	
	void requestError(int errorCode, String message);
}
