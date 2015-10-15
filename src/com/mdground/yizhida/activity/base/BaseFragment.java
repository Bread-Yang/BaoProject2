package com.mdground.yizhida.activity.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.dialog.LoadingDialog;

public class BaseFragment extends Fragment implements BaseView {
	private LoadingDialog mLoadingDialog;

	@Override
	public void showProgress() {
		getLoadingDialog().show();
	}

	@Override
	public void hidProgress() {
		getLoadingDialog().dismiss();
	}

	@Override
	public void showToast(String text) {
		if (getActivity() != null) {
			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void showToast(int resId) {
		String text = getResources().getString(resId);
		showToast(text);
	}

	protected LoadingDialog getLoadingDialog() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(getActivity());
		}

		return mLoadingDialog;
	}

	@Override
	public void requestError(int errorCode, String message) {
		if (errorCode >= ResponseCode.Normal.getValue()) {
			showToast(message);
		} else {
			showToast(R.string.request_error);
		}
	}

}
