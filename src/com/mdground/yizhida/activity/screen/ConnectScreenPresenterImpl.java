package com.mdground.yizhida.activity.screen;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.screen.Command;
import com.mdground.yizhida.screen.ConnectStausListener;
import com.mdground.yizhida.screen.ScreenManager;
import com.mdground.yizhida.screen.Valid;
import com.mdground.yizhida.util.PreferenceUtils;

public class ConnectScreenPresenterImpl implements ConnectScreenPresenter, ConnectStausListener {
	private ConnectScreenView mView;
	private Context context;
	private Employee loginEmployee;
	ScreenManager screenManager;

	public ConnectScreenPresenterImpl(ConnectScreenView view) {
		this.mView = view;
		context = (Context) view;
		loginEmployee = ((MedicalAppliction) context.getApplicationContext()).getLoginEmployee();
		screenManager = ScreenManager.getInstance();
		screenManager.addConnectListener(this);
	}

	@Override
	public synchronized void connect(String ip, int port) {
		if (screenManager.isConnected()) {
			return;
		}
		screenManager.connectScreen(ip, port);
	}

	@Override
	public void disconnect() {
		mView.showToast("与导诊屏的连接已断开");
		screenManager.disconnect();
	}

	@Override
	public boolean isConnected() {
		return screenManager.isConnected();
	}

	@Override
	public String getConnectAddress() {
		return screenManager.getConnectAddress();
	}

	@Override
	public void onConnected() {
		// 發送驗證消息
		Command cmd = new Command();
		cmd.setAction(Command.ACTION_VAILD);
		cmd.setClinicID(loginEmployee.getClinicID());
		Gson gson = new Gson();
		screenManager.sendMessage(gson.toJson(cmd));
	}

	@Override
	public void onDisconnect() {
		mView.showDisconnect();
		mView.hidProgress();
	}

	@Override
	public void onReceive(String msg) {
		try {
			if (msg.contains("Valid")) {
				Gson gson = new Gson();
				Valid valid = gson.fromJson(msg.trim(), Valid.class);
				if (valid != null) {
					if (valid.isValid()) {
						// 验证通过连接
						mView.showConnect();
						screenManager.setConnected(true);
						screenManager.startHeartbeat();
						PreferenceUtils.setPrefString(context, MemberConstant.SCREEEN_IP, getConnectAddress());
					} else {
						mView.showToast("连接导诊屏失败");
						screenManager.disconnect();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnecting() {
		// mView.showConnecting();
		mView.showProgress();
	}

	@Override
	public void onConnecFailed() {
		mView.showToast("连接导诊屏失败");
		mView.hidProgress();
	}

	@Override
	public void addScreenCallBack() {
		screenManager.addConnectListener(this);
	}

	@Override
	public void removeScreenCallBack() {
		screenManager.removeConnectListener(this);
	}

}
