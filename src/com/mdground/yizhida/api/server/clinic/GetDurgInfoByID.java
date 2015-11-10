package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

/**
 * 获取药品详情
 * 
 * @author yoghourt
 *
 */
public class GetDurgInfoByID extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDurgInfoByID";

	public GetDurgInfoByID(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDurgInfoByID(int drugID, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(drugID));

		pocess();
	}
}
