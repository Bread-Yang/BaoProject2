package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

public class GetPatient extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetPatient";

	public GetPatient(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getPatient(int patientId, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		JSONObject obj = new JSONObject();
		try {
			obj.put("PatientID", String.valueOf(patientId));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
