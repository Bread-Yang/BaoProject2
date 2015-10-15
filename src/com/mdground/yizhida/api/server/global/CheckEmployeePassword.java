package com.mdground.yizhida.api.server.global;

import android.content.Context;
import android.util.Log;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

public class CheckEmployeePassword extends GlobalRequest {

	private static final String FUNCTION_NAME = "CheckEmployeePassword";

	public CheckEmployeePassword(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void checkEmployeePwd(String password, RequestCallBack listener) {
		if (password == null || password.equals("")) {
			Log.e(TAG, "password is null");
			return;
		}
		setRequestCallBack(listener);

		RequestData data = getData();
		data.setQueryData(password);

		pocess();
	}

}
