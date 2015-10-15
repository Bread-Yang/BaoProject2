package com.mdground.yizhida.activity.starting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.WelcomeActivity;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.home.MainActivity;
import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.api.base.PlatformType;
import com.mdground.yizhida.api.bean.Device;
import com.mdground.yizhida.api.utils.DeviceUtils;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.MdgConfig;
import com.mdground.yizhida.util.PreferenceUtils;

public class StartingActivity extends BaseActivity implements StartingView {

	private StartingPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_starting, null);
		setContentView(view);
		presenter = new StartingPresenterImpl(this);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		aa.setFillAfter(true);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				MedicalAppliction app = (MedicalAppliction) getApplication();
				Employee employee = app.getLoginEmployee();
				if (employee != null) {
					if (employee.isNeedResetPwd()) {
						app.setLoginEmployee(null);
						navigateToLogin();
						return;
					} else {
						navigateToHome(employee.getEmployeeRole());
						PreferenceUtils.setPrefInt(StartingActivity.this, MemberConstant.DEVICE_ID, employee.getDeviceID());
						MdgConfig.setDeviceId(employee.getDeviceID());
					}
				} else {
					String username = PreferenceUtils.getPrefString(StartingActivity.this, MemberConstant.USERNAME, null);
					String password = PreferenceUtils.getPrefString(StartingActivity.this, MemberConstant.PASSWORD, null);
					int loginStatus = PreferenceUtils.getPrefInt(StartingActivity.this, MemberConstant.LOGIN_STATUS, 0);
					if (loginStatus == MemberConstant.LOGIN_OUT || username == null || username.equals("") || password == null || password.equals("")) {
						navigateToLogin();
						return;
					}

					presenter.loginEmployee(username, password, getDevice());
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	private Device getDevice() {
		Device device = new Device();
		boolean isPad = DeviceUtils.isPad(this);
		if (isPad) {
			device.setPlatform(PlatformType.ANDROID_PAD.value());
		} else {
			device.setPlatform(PlatformType.ANDROID_PHONE.value());
		}
		// 设置android版本号
		device.setPlatformVersion(android.os.Build.VERSION.RELEASE);
		// 型号
		device.setDeviceModel(android.os.Build.MODEL);

		return device;
	}

	@Override
	public void navigateToHome(int role) {
		// 修改为登陆状态
		PreferenceUtils.setPrefInt(StartingActivity.this, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_IN);
		Intent intent = new Intent();
		intent.setClass(StartingActivity.this, MainActivity.class);
		intent.putExtra(MemberConstant.EMPLOYEE_ROLE, role);
		StartingActivity.this.startActivity(intent);
		finish();
	}

	@Override
	public void navigateToLogin() {
		Intent intent = new Intent();
		intent.setClass(StartingActivity.this, LoginActivity.class);
		StartingActivity.this.startActivity(intent);
		finish();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initMemberData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateWelcome() {
		Intent intent = new Intent(StartingActivity.this, WelcomeActivity.class);
		startActivity(intent);
		finish();
	}
}
