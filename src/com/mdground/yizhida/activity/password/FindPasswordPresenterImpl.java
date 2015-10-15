package com.mdground.yizhida.activity.password;

import org.apache.http.Header;

import android.content.Context;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.CheckEmployeePhone;

public class FindPasswordPresenterImpl implements FindPasswordPresenter, RequestCallBack {

	private FindPasswordView mFindPwdView;

	public FindPasswordPresenterImpl(FindPasswordView findPwdView) {
		this.mFindPwdView = findPwdView;
	}

	@Override
	public void checkPhone(String phone) {
		CheckEmployeePhone checkPhone = new CheckEmployeePhone((Context) mFindPwdView);
		checkPhone.checkEmployeePhone(phone, this);
	}

	@Override
	public void onStart() {
		mFindPwdView.showProgress();
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		// TODO Auto-generated method stub
		mFindPwdView.showToast(R.string.request_error);
	}

	@Override
	public void onFinish() {
		mFindPwdView.hidProgress();
	}

	@Override
	public void onSuccess(ResponseData response) {
		if (response.getCode() == ResponseCode.Normal.getValue()) {
			// 验证成功
			mFindPwdView.navigateToVerfyCode();
		} else {
			mFindPwdView.requestError(response.getCode(), response.getMessage());
		}
	}

}
