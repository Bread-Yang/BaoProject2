package com.mdground.yizhida.activity.password;

import org.apache.http.Header;

import android.content.Context;

import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.UpdateEmployeePassword;
import com.mdground.yizhida.util.MD5Util;

public class ModifyPasswordPresenterImpl implements ModifyPasswordPresenter, RequestCallBack {

	private ModifyPasswordView mModifyPasswordView;

	public ModifyPasswordPresenterImpl(ModifyPasswordView view) {
		this.mModifyPasswordView = view;
	}

	@Override
	public void modifyPassword(String phone, String password, String comfirmPassword) {
		if (password == null || password.equals("")) {
			mModifyPasswordView.errorPwdNull();
			return;
		}

		if (comfirmPassword == null || comfirmPassword.equals("")) {
			mModifyPasswordView.errorComfirmPwdNull();
			return;
		}

		if (!password.equals(comfirmPassword)) {
			mModifyPasswordView.errorDiffPwd();
			return;
		}

		UpdateEmployeePassword updateEmployeePwd = new UpdateEmployeePassword((Context) mModifyPasswordView);
		updateEmployeePwd.updateEmployeePassword(phone, MD5Util.MD5(password), this);
	}

	@Override
	public void onStart() {
		mModifyPasswordView.showProgress();
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		mModifyPasswordView.showError(throwable.getMessage());
	}

	@Override
	public void onFinish() {
		mModifyPasswordView.hidProgress();
	}

	@Override
	public void onSuccess(ResponseData response) {
		mModifyPasswordView.finishModify();
	}

}
