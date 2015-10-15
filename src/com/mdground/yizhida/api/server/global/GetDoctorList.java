package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class GetDoctorList extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetDoctorList";

	public GetDoctorList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}


	public void getDoctorList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		pocess();
	}

}
