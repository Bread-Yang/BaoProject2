package com.mdground.yizhida.api.server.clinic;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Doctor;

public class GetAppointmentCountForDoctor extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetAppointmentCountForDoctor";

	public static final String FILED_FINISHED_COUNT = "FinishedCount";
	public static final String FILED_MISSED_COUNT = "MissedCount";
	public static final String FILED_TOTAL_COUNT = "TotalCount";
	public static final String FILED_WAITING_COUNT = "WaitingCount";

	public GetAppointmentCountForDoctor(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAppointmentCountForWaiting(List<Doctor> doctorList, RequestCallBack listener) {
		if (doctorList == null || doctorList.size() == 0) {
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
