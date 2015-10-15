package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class SaveLog extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveLog";

	public SaveLog(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveLog(int platform, String platformVersion, String message, String stackTrace, String source,RequestCallBack callBack){
		setRequestCallBack(callBack);
		
		String query = new QueryDataBuilder()
		.putData("Platform", String.valueOf(platform))
		.putData("PlatformVersion", platformVersion)
		.putData("Message", message)
		.putData("StackTrace", stackTrace)
		.putData("Source", source)
		.create();
		
		getData().setQueryData(query);
		pocess();
	}
}
