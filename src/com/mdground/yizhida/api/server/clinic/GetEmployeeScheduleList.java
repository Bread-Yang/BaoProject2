package com.mdground.yizhida.api.server.clinic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.util.DateUtils;

public class GetEmployeeScheduleList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetEmployeeScheduleList";

	public GetEmployeeScheduleList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployeeScheduleList(Date beginDate, Date endDate, int employeeId, RequestCallBack callBack) {
		if (beginDate == null || endDate == null) {
			return;
		}
		setRequestCallBack(callBack);
		
		MdgAppliction appliction = null;
		if (mContext != null && mContext.getApplicationContext() instanceof MdgAppliction) {
			appliction = (MdgAppliction) mContext.getApplicationContext();
		} else {
			throw new RuntimeException("please create MdgAppliction !!!");
		}

		if (appliction == null) {
			return;
		}

		Employee loginEmployee = appliction.getLoginEmployee();
		if (loginEmployee == null) {
			throw new RuntimeException("please login ");
		}
		int clinicID = loginEmployee.getClinicID();
		
		JSONObject obj = new JSONObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ", Locale.CHINA);
			obj.put("BeginDate", DateUtils.getDateString(beginDate, sdf) + "00:00:00");
			obj.put("EndDate", DateUtils.getDateString(endDate, sdf) + "23:59:59");
			obj.put("EmployeeID", String.valueOf(employeeId));
			obj.put("ClinicID", String.valueOf(clinicID));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());
		pocess();

	}
}
