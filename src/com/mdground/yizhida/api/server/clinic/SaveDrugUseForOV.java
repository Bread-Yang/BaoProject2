package com.mdground.yizhida.api.server.clinic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.bean.OfficeVisitFee;

import android.content.Context;

/**
 * 保存处方
 * 
 * @author yoghourt
 *
 */
public class SaveDrugUseForOV extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveDrugUseForOV";

	public SaveDrugUseForOV(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveDrugUseForOV(int VisitID, List<DrugUse> drugUseList, List<OfficeVisitFee> feeList,
			RequestCallBack callBack) {
		if (drugUseList == null || feeList == null) {
			return;
		}

		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			JSONArray sendDrugUseList = new JSONArray(new Gson().toJson(drugUseList));
			JSONArray sendFeeList = new JSONArray(new Gson().toJson(feeList));
			obj.put("VisitID", VisitID);
			obj.put("DrugUseList", sendDrugUseList);
			obj.put("FeeList", sendFeeList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
