package com.mdground.yizhida.screen;

public interface ConnectStausListener {
	// 连接成功
	public void onConnected();

	// 断开连接
	public void onDisconnect();

	// 收到消息
	public void onReceive(String msg);

	public void onConnecting();

	public void onConnecFailed();
}
