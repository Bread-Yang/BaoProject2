package com.mdground.yizhida.screen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mdground.yizhida.api.utils.L;

import android.os.Handler;

/**
 * 连接导诊屏
 * 
 * @author qinglong.huang
 * 
 */
public class ScreenManager {
	private static final int CONNECTED = 1;
	private static final int DISCONNECTED = 2;
	private static final int RECIVE_MSG = 3;
	private static final int CONNECTING = 4;
	private static final int CONNECT_FAILED = 5;
	private static ScreenManager instance;
	private Socket socket;
	private boolean isConnected = false;

	private boolean connecting = false;

	private DataInputStream dis;
	private DataOutputStream dos;

	private String ip;
	private int port;

	private List<ConnectStausListener> mConnectCallBacks = new ArrayList<ConnectStausListener>();
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String message = null;
			if (msg.obj != null) {
				message = msg.obj.toString();
			}
			notifyListener(msg.what, message);
		};
	};

	private ExecutorService pool = Executors.newFixedThreadPool(1);

	class ConnectRunnable implements Runnable {

		@Override
		public void run() {
			try {
				socket = new Socket(ip, port);
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				connecting = false;
				// 连接成功后接收服务器响应消息
				byte buffer[] = new byte[1024];
				mHandler.sendEmptyMessage(CONNECTED);
				
				while (dis.read(buffer) != -1) {
					mHandler.obtainMessage(RECIVE_MSG, new String(buffer).trim()).sendToTarget();
				}
			} catch (IOException e) {
				e.printStackTrace();
				L.e(ScreenManager.class,"连接断开了" );
				if (e.getMessage() != null && e.getMessage().contains("connect failed")) {
					mHandler.sendEmptyMessage(CONNECT_FAILED);
				} else {
					mHandler.sendEmptyMessage(DISCONNECTED);
				}
			} finally {
				try {
					isConnected = false;
					connecting = false;
					if (dis != null) {
						dis.close();
						dis = null;
					}
					if (dos != null) {
						dos.close();
						dos = null;
					}
					if (socket != null) {
						socket.close();
						socket = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

/*	private Runnable mReConnect = new Runnable() {

		@Override
		public void run() {
			if (!isConnected) {
				connectScreen();
			}
			// 每隔2s检测一次是否连接
			mHandler.postDelayed(this, 30 * 1000);
		}
	};*/

	public static ScreenManager getInstance() {
		if (instance == null) {
			instance = new ScreenManager();
		}

		return instance;
	}

	public void connectScreen(String ip, int port) {
		if (isConnected) {
			mHandler.sendEmptyMessage(CONNECTED);
			return;
		}

		if (connecting) {
			mHandler.sendEmptyMessage(CONNECTING);
			return;
		}

		this.ip = ip;
		this.port = port;
		connecting = true;
		mHandler.sendEmptyMessage(CONNECTING);
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		pool.execute(new ConnectRunnable());
	}

	public void startHeartbeat() {
//		mHandler.post(mReConnect);
	}

	public void stopHeadBeat() {
//		mHandler.removeCallbacks(mReConnect);
	}

	public void disconnect() {
		if (socket != null) {
			try {
				socket.close();
				isConnected = false;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
//				mHandler.removeCallbacks(mReConnect);
			}
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void sendMessage(String message) {
		if (message == null || message.equals("")) {
			return;
		}

		if (dos == null) {
			return;
		}
		
		try {
			dos.write(message.getBytes());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.sendEmptyMessage(DISCONNECTED);
		}
	}

	public void addConnectListener(ConnectStausListener listener) {
		if (mConnectCallBacks.contains(listener)) {
			return;
		}
		mConnectCallBacks.add(listener);
	}

	public void removeConnectListener(ConnectStausListener listener) {
		if (!mConnectCallBacks.contains(listener)) {
			return;
		}
		mConnectCallBacks.remove(listener);
	}

	/**
	 * 提供断开重连调用
	 */
	public void connectScreen() {
		if (isConnected) {
			return;
		}

		if (ip != null && !ip.equals("")) {
			connectScreen(ip, port);
		}
	}

	public void notifyListener(int type, String msg) {
		if (mConnectCallBacks == null || mConnectCallBacks.size() == 0) {
			return;
		}

		for (int i = 0; i < mConnectCallBacks.size(); i++) {
			ConnectStausListener listener = mConnectCallBacks.get(i);
			switch (type) {
			case CONNECTED:
				listener.onConnected();
				break;
			case DISCONNECTED:
				listener.onDisconnect();
				break;
			case CONNECTING:
				listener.onConnecting();
				break;
			case CONNECT_FAILED:
				listener.onConnecFailed();
				break;
			case RECIVE_MSG:
				listener.onReceive(msg);
				break;

			default:
				break;
			}
		}

	}

	public String getConnectAddress() {
		return this.ip;
	}

}
