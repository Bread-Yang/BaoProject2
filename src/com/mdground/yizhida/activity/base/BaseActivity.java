package com.mdground.yizhida.activity.base;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.dialog.LoadingDialog;
import com.mdground.yizhida.util.AppManager;

public abstract class BaseActivity extends Activity implements BaseView {

	private LoadingDialog mLoadingDialog;

	/**
	 * 用户获取控件方法
	 */
	public abstract void findView();

	/**
	 * 初始化控件数据
	 */
	public abstract void initView();

	/**
	 * 初始化成员变量
	 */
	public abstract void initMemberData();

	/**
	 * 设置监听方法
	 */
	public abstract void setListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置所有activity为竖直
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resId) {
		String text = getResources().getString(resId);
		showToast(text);
	}

	public MedicalAppliction getMedicalAppliction() {
		Application app = getApplication();
		if (app instanceof MedicalAppliction) {
			return (MedicalAppliction) app;
		}

		return null;
	}

	@Override
	public void showProgress() {
		getLoadingDialog().show();
	}

	@Override
	public void hidProgress() {
		getLoadingDialog().dismiss();
	}

	protected LoadingDialog getLoadingDialog() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(this);
		}

		return mLoadingDialog;
	}

	@Override
	public void requestError(int errorCode, String message) {
		if (errorCode >= ResponseCode.AppCustom0.getValue()) {
			showToast(message);
		} else {
			showToast(R.string.request_error);
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
	
}
