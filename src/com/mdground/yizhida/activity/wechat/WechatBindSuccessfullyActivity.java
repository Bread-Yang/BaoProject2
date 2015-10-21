package com.mdground.yizhida.activity.wechat;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.home.MainActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.AppManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WechatBindSuccessfullyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wechat_bind_successfully);
	}
	
	public void navigateToHome(View view) {
		Intent intent = new Intent(this, MainActivity.class);

		intent.putExtra(MemberConstant.EMPLOYEE_ROLE,
				((MedicalAppliction) this.getApplication()).getLoginEmployee().getEmployeeRole());
		startActivity(intent);
		
		finish();
	}
}
