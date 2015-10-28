package com.mdground.yizhida.api.server.global;

import org.json.JSONException;
import org.json.JSONObject;

import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;

import android.content.Context;

public class UpdateEmployeeWeChat extends GlobalRequest {

	private static final String FUNCTION_NAME = "UpdateEmployeeWeChat";

	public UpdateEmployeeWeChat(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateEmployeeWeChat(String openId, String unionID, String weChatName, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		JSONObject obj = new JSONObject();
		try {
			obj.put("OpenID", openId);
			obj.put("UnionID", unionID);
			obj.put("WeChatName", weChatName);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
