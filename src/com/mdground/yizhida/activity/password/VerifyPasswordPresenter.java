package com.mdground.yizhida.activity.password;

public interface VerifyPasswordPresenter {

	void checkAuthCode(String phone, String code);

	void getAuthCode(String phone);

}
