package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

/**
 * 药品类型列表
 * 
 * @author yoghourt
 *
 */
public class GetDrugTypeList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetDrugTypeList";

	public GetDrugTypeList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDrugTypeList(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
