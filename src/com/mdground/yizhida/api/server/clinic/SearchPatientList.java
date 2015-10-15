package com.mdground.yizhida.api.server.clinic;

import android.content.Context;

import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class SearchPatientList extends ClinicRequest {
	private static final String FUNCTION_NAME = "SearchPatientList";

	public SearchPatientList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void searchPatient(String keyword, RequestCallBack requestCallBack) {
		if (keyword == null) {
			return;
		}
		
		setRequestCallBack(requestCallBack);
		String query = new QueryDataBuilder().putData("Keyword", keyword).create();
		getData().setQueryData(query);
		pocess();
	}

}
