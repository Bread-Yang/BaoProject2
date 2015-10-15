package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class GetChiefComplaintTemplateList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetChiefComplaintTemplateList";

	public GetChiefComplaintTemplateList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getChiefComplaintTemplateList(RequestCallBack listener) {
		setRequestCallBack(listener);
		getData().setQueryData("{}");
		pocess();
	}

}
