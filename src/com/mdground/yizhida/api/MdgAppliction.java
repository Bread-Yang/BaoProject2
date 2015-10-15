package com.mdground.yizhida.api;

import com.mdground.yizhida.bean.Employee;

import android.app.Application;
import android.content.Context;

public abstract class MdgAppliction extends Application {
	/** 对外提供整个应用生命周期的Context **/
	private static Context instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	/**
	 * 
	 * 对外提供Application Context
	 * 
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}

	public abstract Employee getLoginEmployee();
	
	public abstract void setLoginEmployee(Employee employee);

	public abstract int getDeviceId();
	
	public abstract String getDeviceToken();
}
