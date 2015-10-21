package com.mdground.yizhida.api.server.global;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.util.DateUtils;

public class SaveEmployee extends GlobalRequest {
	private static final String FUNCTION_NAME = "SaveEmployee";

	public SaveEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveEmployee(Employee employee, RequestCallBack listener) {
		if (employee == null) {
			return;
		}

		setRequestCallBack(listener);
		JSONObject obj = new JSONObject();
		try {
			obj.put("EmployeeName", employee.getEmployeeName());
			obj.put("Gender", String.valueOf(employee.getGender()));
			obj.put("DOB", DateUtils.getDateString(employee.getDOB(),
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
			obj.put("CountryID", String.valueOf(employee.getCountryID()));
			obj.put("ProvinceID", String.valueOf(employee.getProvinceID()));
			obj.put("CityID", String.valueOf(employee.getCityID()));
			obj.put("DistrictID", String.valueOf(employee.getDistrictID()));
			obj.put("Street", employee.getStreet());
			obj.put("Address", employee.getAddress());
			obj.put("GraduateSchool", employee.getGraduateSchool());
			obj.put("SpecialtyName", employee.getSpecialtyName());
			obj.put("unionID", employee.getUnionID());
			obj.put("openID", employee.getOpenID());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
