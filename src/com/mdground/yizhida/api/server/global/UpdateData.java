package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

/**
 * 	基本数据
 * @author Vincent
 *
 */
public class UpdateData extends GlobalRequest {
	private static final String FUNCTION_NAME = "UpdateData";

	public UpdateData(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateData(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
