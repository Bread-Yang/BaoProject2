package com.mdground.yizhida.activity.password;

public interface VerifyPasswordView {
	// 跳转到验证码界面
	public void navigateToModifyPwd();

	// 验证码错误
	public void authCodeError();

	public void updateTime(int time);

	public void showReSend();

	public void showError(String error);

	public void authCodeNull();
}
