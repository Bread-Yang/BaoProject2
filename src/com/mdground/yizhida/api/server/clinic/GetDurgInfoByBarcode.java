package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

/**
 * 根据二维码获取药物详情
 * 
 * @author yoghourt
 *
 */
public class GetDurgInfoByBarcode extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDurgInfoByBarcode";

	public GetDurgInfoByBarcode(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDurgInfoByBarcode(String barCode, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(barCode);

		pocess();
	}
}
