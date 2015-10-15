package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class UpdateAppointmentByAssign extends ClinicRequest {
	private static final String FUNCTION_NAME = "UpdateAppointmentByAssign";

	public UpdateAppointmentByAssign(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateAppointmentByAssign(int opid, int doctorId, RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);

		String query = new QueryDataBuilder().putData("OPID", String.valueOf(opid)).putData("DoctorID", String.valueOf(doctorId)).create();
		getData().setQueryData(query);
		pocess();
	}

}
