package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class RefreshEmployee extends ClinicRequest {
	private static final String FUNCTION_NAME = "RefreshEmployee";

	public RefreshEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void refreshEmployee(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
