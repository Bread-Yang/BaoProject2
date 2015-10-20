package com.mdground.yizhida.activity.wechat;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.home.MainActivity;
import com.mdground.yizhida.constant.MemberConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WechatBindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_wechat_bind);
	}
	
	public void bindWechat(View view) {
		
	}
	
	public void navigateToHome(View view) {
		Intent intent = new Intent(this, MainActivity.class);

		intent.putExtra(MemberConstant.EMPLOYEE_ROLE,
				((MedicalAppliction) this.getApplication()).getLoginEmployee().getEmployeeRole());
		startActivity(intent);
		finish();
	}
}
