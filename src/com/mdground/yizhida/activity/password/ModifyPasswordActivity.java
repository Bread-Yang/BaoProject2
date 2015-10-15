package com.mdground.yizhida.activity.password;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.dialog.LoadingDialog;
import com.mdground.yizhida.util.AppManager;
import com.mdground.yizhida.util.RegexUtil;
import com.mdground.yizhida.view.TitleBar;

public class ModifyPasswordActivity extends TitleBarActivity implements OnClickListener, ModifyPasswordView {

	private EditText EtPassword;
	private EditText EtComfirmPassword;
	private Button BtConfirm;
	private String mPhone;
	private ModifyPasswordPresenter presenter;

	private LoadingDialog mLoadingDialog;

	@Override
	public int getContentLayout() {
		return R.layout.activity_password_modify;
	}

	@Override
	public void findView() {
		EtPassword = (EditText) this.findViewById(R.id.password_value);
		EtComfirmPassword = (EditText) this.findViewById(R.id.comfirm_password_value);
		BtConfirm = (Button) this.findViewById(R.id.commit);
	}

	@Override
	public void initView() {

	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);

		titleBar.setTitle("修改密码");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		if (intent != null) {
			mPhone = intent.getStringExtra(MemberConstant.PHONE);
			if (mPhone == null || !RegexUtil.isPhoneNumber(mPhone)) {
				Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		} else {
			Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
			finish();
		}

		presenter = new ModifyPasswordPresenterImpl(this);
		mLoadingDialog = LoadingDialog.getInstance(this, "修改中");
	}

	@Override
	public void setListener() {
		BtConfirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			presenter.modifyPassword(mPhone, EtPassword.getText().toString(), EtComfirmPassword.getText().toString());
			break;
		}

	}

	@Override
	public void showError(int resId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void errorPwdNull() {
		showToast(R.string.activity_pwd_edit_pwd_null);
		EtPassword.requestFocus();
	}

	@Override
	public void errorComfirmPwdNull() {
		showToast(R.string.activity_pwd_edit_comfirm_pwd_null);
		EtComfirmPassword.requestFocus();
	}

	@Override
	public void errorDiffPwd() {
		showToast(R.string.activity_pwd_edit_pwd_different);
		EtPassword.requestFocus();
	}

	@Override
	public void finishModify() {

		Dialog dialog = new AlertDialog.Builder(this).setMessage("修改密码成功，请重新登陆！").setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppManager.getAppManager().finishAllActivity();
				Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	@Override
	public void showProgress() {
		mLoadingDialog.show();
	}

	@Override
	public void hidProgress() {
		mLoadingDialog.dismiss();
	}

}
