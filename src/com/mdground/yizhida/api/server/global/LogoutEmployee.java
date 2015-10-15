package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class LogoutEmployee extends GlobalRequest {
	private static final String FUNCTION_NAME = "LogoutEmployee";

	public LogoutEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void logoutEmployee(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		getData().setQueryData("");
		pocess();
	}

}
