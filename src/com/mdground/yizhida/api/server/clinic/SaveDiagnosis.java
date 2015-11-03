package com.mdground.yizhida.api.server.clinic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.ChiefComplaint;
import com.mdground.yizhida.bean.Diagnosis;
import com.mdground.yizhida.bean.VitalSigns;

import android.content.Context;

/**
 * 保存诊断
 * 
 * @author yoghourt
 *
 */
public class SaveDiagnosis extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveDiagnosis";

	public SaveDiagnosis(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}
	
	public void saveDiagnosis(ArrayList<Diagnosis> diagnosisList, RequestCallBack callBack) {
		if (diagnosisList == null || diagnosisList.size() == 0) {
			return;
		}
		
		setRequestCallBack(callBack);
		
		JSONObject obj = new JSONObject();
		try {
			String jsonString = new Gson().toJson(diagnosisList);
			JSONArray array = new JSONArray(jsonString);
			obj.put("DiagnosisList",array);  // 这是一个array,不是一个string
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}

