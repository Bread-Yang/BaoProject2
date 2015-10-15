package com.mdground.yizhida.api.server.global;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.bean.Device;

/**
 * 
 * @author qinglong.huang
 * 
 */
public class LoginEmployee extends GlobalRequest {

	public static final String FUNCTION_NAME = "LoginEmployee";

	public LoginEmployee(Context context) {
		super(context);
	}

	// 用户登录
	public void loginEmployee(String loginId, String pwd, Device device, RequestCallBack listener) {
		if (loginId == null || pwd == null || device == null) {
			Log.e(TAG, "employee or device is null");
			return;
		}

		setRequestCallBack(listener);

		MdgAppliction appliction = null;
		if (mContext != null && mContext.getApplicationContext() instanceof MdgAppliction) {
			appliction = (MdgAppliction) mContext.getApplicationContext();
		} else {
			throw new RuntimeException("please create MdgAppliction !!!");
		}

		if (appliction == null) {
			return;
		}
		device.setDeviceID(appliction.getDeviceId());
		String deviceToken = appliction.getDeviceToken();
		if (deviceToken == null || deviceToken.equals("")) {
			if (listener != null) {
				ResponseData response = new ResponseData();
				response.setCode(ResponseCode.AppCustom0.getValue());
				response.setMessage("登录失败");
				listener.onSuccess(response);
				return;
			}
		}
		device.setDeviceToken(deviceToken);
		LoginData loginData = new LoginData();
		loginData.setLoginID(loginId);
		loginData.setPwd(pwd);
		loginData.setDevice(device);

		Gson gson = new Gson();
		String requestData = gson.toJson(loginData);
		getData().setQueryData(requestData);
		pocess(true);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	protected static class LoginData {
		@SerializedName("LoginID")
		public String LoginID;
		@SerializedName("Pwd")
		public String Pwd;

		@SerializedName("Device")
		private Device device;

		public String getLoginID() {
			return LoginID;
		}

		public void setLoginID(String loginID) {
			LoginID = loginID;
		}

		public String getPwd() {
			return Pwd;
		}

		public void setPwd(String pwd) {
			Pwd = pwd;
		}

		public Device getDevice() {
			return device;
		}

		public void setDevice(Device device) {
			this.device = device;
		}

	}

}
