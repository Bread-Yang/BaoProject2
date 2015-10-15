package com.mdground.yizhida.activity.home;

import org.apache.http.Header;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.LogoutEmployee;

public class PersonCenterPresenterImpl implements PersonCenterPresenter {

	PersonCenterView mView;
	private Context context;

	public PersonCenterPresenterImpl(PersonCenterView view) {
		this.mView = view;
		Fragment fragment = (Fragment) view;
		this.context = fragment.getActivity();
	}

	@Override
	public void loginOut() {
		LogoutEmployee logoutEmployee = new LogoutEmployee(context);
		logoutEmployee.logoutEmployee(new RequestCallBack() {

			@Override
			public void onStart() {
				// mView.showProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {

				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}
		});
	}

}
