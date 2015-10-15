package com.mdground.yizhida.receiver;

import com.mdground.yizhida.screen.ScreenManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStatusChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// Toast.makeText(context, intent.getAction(), 1).show();
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeInfo = manager.getActiveNetworkInfo();
		if (activeInfo == null) {
			if (ScreenManager.getInstance().isConnected()) {
				ScreenManager.getInstance().stopHeadBeat();
				ScreenManager.getInstance().disconnect();
			}
		} else if (wifiInfo != null && wifiInfo.isConnected()) {
//			ScreenManager.getInstance().connectScreen();
		}
	}

}