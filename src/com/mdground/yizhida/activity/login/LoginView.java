package com.mdground.yizhida.activity.login;

import com.mdground.yizhida.activity.base.BaseView;

public interface LoginView extends BaseView{
	// 跳转到main
	public void navigateToHome(int role);
	
	public void navigateToWelcome();

	public void userNameNull();

	public void passwordNull();

	public void showProgress();

	public void hidProgress();
	
	public void showError(String message);
}
