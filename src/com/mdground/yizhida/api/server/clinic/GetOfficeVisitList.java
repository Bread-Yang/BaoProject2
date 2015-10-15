package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

/**
 * 既往史
 * 
 * @author yoghourt
 *
 */
public class GetOfficeVisitList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetOfficeVisitList";

	public GetOfficeVisitList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getOfficeVisitList(int patientId, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(patientId));

		pocess();
	}
}
