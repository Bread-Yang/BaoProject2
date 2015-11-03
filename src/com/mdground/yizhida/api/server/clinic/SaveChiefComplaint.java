package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.ChiefComplaint;

import android.content.Context;

/**
 * 保存体征
 * 
 * @author yoghourt
 *
 */
public class SaveChiefComplaint extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveChiefComplaint";

	public SaveChiefComplaint(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveChiefComplaint(ChiefComplaint chiefComplaint, RequestCallBack callBack) {
		if (chiefComplaint == null) {
			return;
		}
		
		setRequestCallBack(callBack);
		
		JSONObject obj = new JSONObject();
		try {
			JSONObject chiefCoomplaintJson = new JSONObject(new Gson().toJson(chiefComplaint));
			obj.put("ChiefComplaint",chiefCoomplaintJson);  // 这是一个dict,不是一个string
			obj.put("PhotoList", "");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}

