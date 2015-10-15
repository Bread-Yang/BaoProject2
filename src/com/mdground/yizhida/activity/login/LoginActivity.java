package com.mdground.yizhida.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.WelcomeActivity;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.home.MainActivity;
import com.mdground.yizhida.activity.password.FindPasswordActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.dialog.LoadingDialog;
import com.mdground.yizhida.util.PreferenceUtils;
import com.mdground.yizhida.view.ClearEditText;
import com.mdground.yizhida.view.ResizeLayout;
import com.mdground.yizhida.view.ResizeLayout.OnResizeListener;

public class LoginActivity extends BaseActivity implements OnResizeListener, OnClickListener, LoginView {
	private ResizeLayout LoginRootLayout;
	private TextView TvHorizaontalName;
	private TextView TvVerticalName;
	private Button BtLogin;
	private ClearEditText EtLoginName;
	private ClearEditText EtPassword;
	private TextView TvFindPassword;
	private ScrollView mScrollView;

	private LoginPresenter presenter;

	private LoadingDialog mLoadIngDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
		initMemberData();
		setListener();
		initView();
	}

	@Override
	public void findView() {
		TvHorizaontalName = (TextView) this.findViewById(R.id.app_name_horizaontal);
		TvVerticalName = (TextView) this.findViewById(R.id.app_name_vertical);
		BtLogin = (Button) this.findViewById(R.id.login_button);
		EtLoginName = (ClearEditText) this.findViewById(R.id.login_name);
		EtPassword = (ClearEditText) this.findViewById(R.id.login_password);
		TvFindPassword = (TextView) this.findViewById(R.id.find_password);
		LoginRootLayout = (ResizeLayout) this.findViewById(R.id.login_root_layout);
		mScrollView = (ScrollView) this.findViewById(R.id.scroll_view);
	}

	@Override
	public void initView() {
		String username = PreferenceUtils.getPrefString(this, MemberConstant.USERNAME, "");
		if (username != null) {
			EtLoginName.setText(username);
		}

		TvHorizaontalName.setVisibility(View.GONE);
		TvVerticalName.setVisibility(View.VISIBLE);
	}

	@Override
	public void initMemberData() {
		presenter = new LoginPresenterImpl(this);
		mLoadIngDialog = new LoadingDialog(this).initText(getResources().getString(R.string.logining));
	}

	@Override
	public void setListener() {
		LoginRootLayout.setOnResizeListener(this);
		TvFindPassword.setOnClickListener(this);
		BtLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_password:
			Intent intent = new Intent(this, FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.login_button:
			if (EtLoginName.getText() == null || EtLoginName.getText().toString().equals("")) {
				showToast("请输入账号");
				return;
			}
			
			if (EtPassword.getText() == null || EtPassword.getText().toString().equals("")) {
				showToast("请输入密码");
				return;
			}
			
			presenter.validateCredentials(EtLoginName.getText().toString(), EtPassword.getText().toString());
			break;
		default:
			break;
		}
	}

	@Override
	public void OnResize(int w, final int h, int oldw, final int oldh) {
		Log.e("LoginActivity", "OnResize()");
		
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
//				int offset = oldh - h;
//				if (offset > 0) {
//					//tanqi
//					TvHorizaontalName.setVisibility(View.VISIBLE);
//					TvVerticalName.setVisibility(View.GONE);
//					mScrollView.scrollTo(0, offset);
//				} else {
//					TvHorizaontalName.setVisibility(View.GONE);
//					TvVerticalName.setVisibility(View.VISIBLE);
//				}

			}
		});

	}

	@Override
	public void navigateToHome(int role) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MemberConstant.EMPLOYEE_ROLE, role);
		startActivity(intent);
		finish();
	}

	@Override
	public void userNameNull() {
		showToast(R.string.error_username_null);
	}

	@Override
	public void passwordNull() {
		showToast(R.string.error_password_null);
	}

	@Override
	public void showProgress() {
		mLoadIngDialog.show();
	}

	@Override
	public void hidProgress() {
		mLoadIngDialog.dismiss();
	}

	@Override
	public void showError(String message) {
		showToast(message);
	}

	@Override
	public void navigateToWelcome() {
		Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
		startActivity(intent);
	}

}
