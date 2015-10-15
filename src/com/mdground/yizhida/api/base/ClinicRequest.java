package com.mdground.yizhida.api.base;

import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.bean.Employee;

import android.content.Context;

public abstract class ClinicRequest extends BaseRequest {

	public ClinicRequest(Context context) {
		super(context);
	}

	@Override
	protected int getBusinessCode() {
		return BusinessType.Clinic.getType();
	}

	@Override
	protected String getUrl() {
		MdgAppliction appliction = null;
		if (mContext.getApplicationContext() instanceof MdgAppliction) {
			appliction = (MdgAppliction) mContext.getApplicationContext();
		}
		Employee loginEmployee = appliction.getLoginEmployee();
		if (loginEmployee != null) {
			return loginEmployee.getURL()+"/Api/RpcService.ashx";
		}
		return "";
	}
	
	
	
}
