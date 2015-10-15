package com.mdground.yizhida.api.server.clinic;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Schedule;

public class SaveDoctorEmergencyLeave extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveDoctorEmergencyLeave";

	public SaveDoctorEmergencyLeave(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveDoctorEmergencyLeave(List<Schedule> schedules, RequestCallBack callBack) {
		if (schedules == null || schedules.size() == 0) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < schedules.size(); i++) {
			Schedule s = schedules.get(i);
			sb.append(s.getEsid());
			sb.append(",");
		}
		String esids = sb.substring(0, sb.length() - 1);

		saveDoctorEmergencyLeave(esids, callBack);
	}

	public void saveDoctorEmergencyLeave(String esids, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("ESIDS", esids);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		RequestData data = getData();
		data.setQueryData(jsonObj.toString());

		pocess();
	}
}
