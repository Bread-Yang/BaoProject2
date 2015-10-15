package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.activity.QrcodeActivity;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class UpdateAppointment extends ClinicRequest {
	private static final String FUNCTION_NAME = "UpdateAppointment";

	public UpdateAppointment(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateAppointment(int oPid, int statusFinish, RequestCallBack requestCallBack) {

		setRequestCallBack(requestCallBack);

		String query = new QueryDataBuilder().putData("OPID", String.valueOf(oPid)).putData("OPStatus", String.valueOf(statusFinish)).create();
		getData().setQueryData(query);
		pocess();
	}

}
