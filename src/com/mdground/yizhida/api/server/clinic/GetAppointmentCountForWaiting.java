package com.mdground.yizhida.api.server.clinic;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Doctor;

public class GetAppointmentCountForWaiting extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetAppointmentCountForWaiting";

	public GetAppointmentCountForWaiting(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAppointmentCountForWaiting(String doctorList, RequestCallBack listener) {
		if (doctorList == null || doctorList.equals("")) {
			return;
		}
		setRequestCallBack(listener);

		JSONObject obj = new JSONObject();
		try {
			obj.put("DoctorIDList", doctorList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
	
	
	public void getAppointmentCountForWaiting(List<Doctor> doctorList, RequestCallBack listener) {
		if (doctorList == null || doctorList.equals("")) {
			return;
		}
		setRequestCallBack(listener);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < doctorList.size(); i++) {
			Doctor doctor = doctorList.get(i);
			sb.append(doctor.getDoctorID());
			sb.append(",");
		}
		String doctorStr = sb.substring(0, sb.length() - 1);
		JSONObject obj = new JSONObject();
		try {
			obj.put("DoctorIDList", doctorStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
