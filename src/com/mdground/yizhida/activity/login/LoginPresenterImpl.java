package com.mdground.yizhida.activity.login;

import org.apache.http.Header;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.wechat.WechatBindActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.LoginEmployee;
import com.mdground.yizhida.api.utils.DeviceUtils;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.EmployeeDao;
import com.mdground.yizhida.util.MD5Util;
import com.mdground.yizhida.util.MdgConfig;
import com.mdground.yizhida.util.PreferenceUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

public class LoginPresenterImpl implements LoginPresenter, RequestCallBack {

	LoginView mLoginView;
	Context context;
	EmployeeDao mEmployeeDao;
	private String userName, password;

	public LoginPresenterImpl(LoginView view) {
		this.mLoginView = view;

		if (view instanceof Context) {
			this.context = (Context) view;
			mEmployeeDao = EmployeeDao.getInstance(context);
		}
	}

	@Override
	public void validateCredentials(String username, String password) {
		if (TextUtils.isEmpty(username)) {
			mLoginView.userNameNull();
			return;
		}

		if (TextUtils.isEmpty(password)) {
			mLoginView.passwordNull();
			return;
		}
		
		this.userName = username;
		this.password = password;

		LoginEmployee loginEmployee = new LoginEmployee(context);
		loginEmployee.loginEmployee(username, MD5Util.MD5(password), DeviceUtils.getDeviceInfo(context), this);
	}

	@Override
	public void onStart() {
		mLoginView.showProgress();
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		mLoginView.showError(throwable.getMessage());
	}

	@Override
	public void onFinish() {
		mLoginView.hidProgress();
	}

	@Override
	public void onSuccess(ResponseData response) {
		if (response.getCode() == ResponseCode.Normal.getValue()) {

			Employee employee = response.getContent(Employee.class);
			if ((employee.getEmployeeRole() & Employee.SCREEN) != 0
					|| ((employee.getEmployeeRole() & Employee.DOCTOR) != 0
							&& (employee.getEmployeeRole() & Employee.NURSE) != 0)) {
				mLoginView.showError("账号异常，请联系客服");
				return;
			}

			Activity activity = (Activity) mLoginView;
			((MedicalAppliction) activity.getApplication()).setLoginEmployee(employee);

			// 保存数据库
			PreferenceUtils.setPrefLong(context, MemberConstant.LOGIN_EMPLOYEE, employee.getEmployeeID());
			PreferenceUtils.setPrefInt(context, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_IN);
			PreferenceUtils.setPrefString(context, MemberConstant.USERNAME, employee.getLoginID());
			PreferenceUtils.setPrefString(context, MemberConstant.PASSWORD, employee.getLoginPwd());
			// PreferenceUtils.setPrefInt(context, MemberConstant.DEVICE_ID,
			// employee.getDeviceID());
			MdgConfig.setDeviceId(employee.getDeviceID());

			// 第一次登陆需要修改密码
			if (employee.isNeedResetPwd()) {
				mLoginView.navigateToWelcome();
				((EditText) ((Activity) mLoginView).findViewById(R.id.login_password)).setText("");
				return;
			}

			// 没有绑定微信
			if (employee.isNeedBindWechat()) {
				Intent intent = new Intent(context, WechatBindActivity.class);
				intent.putExtra("isFromLoginActivity", true);
				context.startActivity(intent);
				return;
			}

			// if (employee.getOpenID() == null ||
			// "".equals(employee.getOpenID())) {
			// Intent intent = new Intent(context, WechatBindActivity.class);
			// intent.putExtra("isFromLoginActivity", true);
			// context.startActivity(intent);
			// return;
			// }

			mLoginView.navigateToHome(employee.getEmployeeRole());
		} else if (response.getCode() == ResponseCode.InvalidDevice.getValue()) {  // deviceId无效,清空deviceid再重新登录一次
			MdgConfig.setDeviceId(0);
			validateCredentials(this.userName, this.password);
		} else {
			mLoginView.requestError(response.getCode(), response.getMessage());
		}
	}

}
