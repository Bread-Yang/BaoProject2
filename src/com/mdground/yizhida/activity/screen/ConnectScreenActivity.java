package com.mdground.yizhida.activity.screen;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.PreferenceUtils;
import com.mdground.yizhida.util.RegexUtil;
import com.mdground.yizhida.util.ToolNetwork;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.TitleBar;

/**
 * 连接导诊屏
 * 
 * @author qinglong.huang
 * 
 */
public class ConnectScreenActivity extends TitleBarActivity implements OnClickListener, ConnectScreenView {
	private static final int CONNECTED = 1;
	private static final int DISCONNECTED = 2;
	private static final int CONNECTING = 3;

	private Button btnConnect;
	private EditText etIpAddress;
	private ImageView connectedImg;
	private TextView tvWifiName;

	private ConnectScreenPresenter presenter;

	@Override
	public int getContentLayout() {
		return R.layout.activity_connect_screen;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		titleBar.setTitle("连接导诊屏");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void findView() {
		btnConnect = (Button) findViewById(R.id.btn_connect);
		etIpAddress = (EditText) findViewById(R.id.et_ip_address);
		connectedImg = (ImageView) findViewById(R.id.img_connected_icon);
		tvWifiName = (TextView) findViewById(R.id.tv_wifi_name);
	}

	@Override
	public void initView() {
		// 延迟刷新界面，确保service bind成功
		// mHandler.postDelayed(mRuannable, 500);
		etIpAddress.setText(PreferenceUtils.getPrefString(this, MemberConstant.SCREEEN_IP, ""));
		if (presenter.isConnected()) {
			showViewByStatus(CONNECTED);
		}
	}

	@Override
	public void initMemberData() {
		presenter = new ConnectScreenPresenterImpl(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.addScreenCallBack();
		if (!ToolNetwork.isWIFIConnected(this)) {
			showToast("请连接wifi");
		} else {
			tvWifiName.setText(Tools.getFormat(this, "Wi-Fi：%s", ToolNetwork.getConnectWifiSsid(this)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		presenter.removeScreenCallBack();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.removeScreenCallBack();
	}

	@Override
	public void setListener() {
		btnConnect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_connect:
			if (!ToolNetwork.isWIFIConnected(this)) {
				showToast("请连接wifi");
				return;
			}

			if (presenter.isConnected()) {
				presenter.disconnect();
			} else {
				if (etIpAddress.getText() == null || etIpAddress.getText().toString().equals("")) {
					showToast("请输入IP地址");
					return;
				}
				if (!RegexUtil.isIpv4(etIpAddress.getText().toString())) {
					showToast("IP格式不正确");
					return;
				}
				presenter.connect(etIpAddress.getText().toString(), 1212);// 断开固定为1212
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void showDisconnect() {
		showViewByStatus(DISCONNECTED);
	}

	@Override
	public void showConnect() {
		showViewByStatus(CONNECTED);
		finish();
	}

	private void showViewByStatus(int type) {
		switch (type) {
		case CONNECTED:
			etIpAddress.setText(presenter.getConnectAddress());
			etIpAddress.setFocusable(false);
			btnConnect.setBackgroundResource(R.drawable.selector_bg_button4);
			btnConnect.setText("断开");
			btnConnect.setEnabled(true);
			connectedImg.setVisibility(View.VISIBLE);
			break;
		case DISCONNECTED:
			etIpAddress.setText(presenter.getConnectAddress());
			etIpAddress.setFocusable(true);
			etIpAddress.setFocusableInTouchMode(true);
			btnConnect.setBackgroundResource(R.drawable.selector_bg_button1);
			btnConnect.setText("连接");
			btnConnect.setEnabled(true);
			connectedImg.setVisibility(View.GONE);
			break;
		case CONNECTING:
			etIpAddress.setText(presenter.getConnectAddress());
			etIpAddress.setFocusable(true);
			etIpAddress.setFocusableInTouchMode(true);
			btnConnect.setBackgroundResource(R.drawable.selector_bg_button1);
			btnConnect.setText("连接中...");
			btnConnect.setEnabled(false);
			connectedImg.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void showConnecting() {
		showViewByStatus(CONNECTING);
	}
}
