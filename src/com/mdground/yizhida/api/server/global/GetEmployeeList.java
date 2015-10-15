package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class GetEmployeeList extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetEmployeeList";

	public GetEmployeeList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployeeList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		pocess();
	}

}
