package com.mdground.yizhida.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.home.MainActivity;
import com.mdground.yizhida.activity.password.VerifyCodeActivity;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;

public class WelcomeActivity extends BaseActivity {
	private Employee loginEmployee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		loginEmployee = ((MedicalAppliction) getApplication()).getLoginEmployee();
	}

	public void nextStep(View view) {
		if (loginEmployee == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(WelcomeActivity.this, VerifyCodeActivity.class);
		intent.putExtra(MemberConstant.PHONE, loginEmployee.getWorkPhone());
		startActivityForResult(intent, MemberConstant.PASSWORD_REQUEST_CODE);
		finish();
	}

	public void navigateToHome(View view) {
		Intent intent = new Intent(this, MainActivity.class);

		intent.putExtra(MemberConstant.EMPLOYEE_ROLE,
				((MedicalAppliction) this.getApplication()).getLoginEmployee().getEmployeeRole());
		startActivity(intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
			setNeedResetPwd();
			finish();
		}
	}

	private void setNeedResetPwd() {
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

}
