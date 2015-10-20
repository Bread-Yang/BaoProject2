package com.mdground.yizhida.activity.password;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.util.AppManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModifyPasswordSuccessfullyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_modify_password_successfully);
	}

	public void backToLoginActivity(View view) {
		AppManager.getAppManager().finishAllActivity();
		Intent intent = new Intent(ModifyPasswordSuccessfullyActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}
