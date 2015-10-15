package com.mdground.yizhida.activity.screen;

public interface ConnectScreenPresenter {

	public void connect(String ip, int port);

	public void disconnect();

	public boolean isConnected();

	public String getConnectAddress();

	public void addScreenCallBack();

	public void removeScreenCallBack();
}
