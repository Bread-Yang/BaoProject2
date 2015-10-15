package com.mdground.yizhida.receiver;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.WindowManager;

import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.RefreshEmployee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.service.ForceOfflineService;
import com.mdground.yizhida.util.AppManager;
import com.mdground.yizhida.util.PreferenceUtils;

public class ForceOfflineReceiver extends BroadcastReceiver {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		intent = new Intent(context, ForceOfflineService.class);
		context.stopService(intent);
		refresh(context);
	}

	public void refresh(final Context context) {
		RefreshEmployee refresh = new RefreshEmployee(context);
		refresh.refreshEmployee(new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == 512) {
					showDialog(context, response.getMessage());
				} else {
					showDialog(context, "您的账户已在另一个设备登录,请尝试重新登陆");
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				showDialog(context, "您的账户已在另一个设备登录,请尝试重新登陆");
			}
		});
	}

	private void showDialog(final Context context, String message) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("已下线");
		dialogBuilder.setMessage(message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("确   定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				PreferenceUtils.setPrefInt(context, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_OUT);
				PreferenceUtils.setPrefString(context, MemberConstant.PASSWORD, "");
				AppManager.getAppManager().finishAllActivity();
				Intent intent = new Intent(context, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
	}

}