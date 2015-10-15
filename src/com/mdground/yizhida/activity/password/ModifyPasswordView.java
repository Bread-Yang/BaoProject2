package com.mdground.yizhida.activity.password;

public interface ModifyPasswordView {

	public void showError(int resId);

	public void showError(String error);

	public void errorPwdNull();

	public void errorComfirmPwdNull();

	public void errorDiffPwd();

	public void finishModify();

	public void showProgress();

	public void hidProgress();
}
