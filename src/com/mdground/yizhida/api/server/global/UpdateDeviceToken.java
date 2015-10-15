package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

public class UpdateDeviceToken extends GlobalRequest {
	private static final String FUNCTION_NAME = "UpdateDeviceToken";

	public UpdateDeviceToken(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateDeviceToken(String token, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(token);
		pocess(true);
	}
}
