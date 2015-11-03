package com.mdground.yizhida.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

import android.content.Context;

public class PostEmployeeWithdrawal extends ClinicRequest  {
	
	private static final String FUNCTION_NAME = "PostEmployeeWithdrawal";
	
	public PostEmployeeWithdrawal(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}
	
	public void postEmployeeWithDraw(int withDraw, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();
		data.setQueryData(String.valueOf(withDraw));

		pocess();
	}

}
