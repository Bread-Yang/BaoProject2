package com.mdground.yizhida.activity.starting;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.bean.Device;
import com.mdground.yizhida.api.server.global.LoginEmployee;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.EmployeeDao;
import com.mdground.yizhida.util.PreferenceUtils;

public class StartingPresenterImpl implements StartingPresenter, RequestCallBack {

	StartingView mView;
	Context context;
	EmployeeDao mEmployeeDao;

	public StartingPresenterImpl(StartingView startIngActivity) {
		this.mView = startIngActivity;

		if (startIngActivity instanceof Context) {
			this.context = (Context) startIngActivity;
			mEmployeeDao = EmployeeDao.getInstance(context);
		}
	}

	@Override
	public void loginEmployee(String username, String password, Device device) {
		LoginEmployee loginEmployee = new LoginEmployee((Context) mView);
		loginEmployee.loginEmployee(username, password, device, this);
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		mView.navigateToLogin();
	}

	@Override
	public void onFinish() {

	}

	@Override
	public void onSuccess(ResponseData response) {
		if (response.getCode() == ResponseCode.Normal.getValue()) {
			Employee employee = response.getContent(Employee.class);
			if ( (employee.getEmployeeRole() & Employee.SCREEN ) != 0 || ((employee.getEmployeeRole() & Employee.DOCTOR ) != 0 && (employee.getEmployeeRole() & Employee.NURSE ) != 0)) {
				mView.showToast("账号异常，请联系客服");
				return;
			}
			Activity activity = (Activity) mView;
			((MedicalAppliction) activity.getApplication()).setLoginEmployee(employee);
			
			if (employee.isNeedResetPwd()) {
				mView.navigateWelcome();
				return;
			}
			// 保存数据库
			PreferenceUtils.setPrefLong(context, MemberConstant.LOGIN_EMPLOYEE, employee.getEmployeeID());
			PreferenceUtils.setPrefInt(context, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_IN);

			mView.navigateToHome(employee.getEmployeeRole());
		} else {
			mView.navigateToLogin();
		}
	}

}
