package com.mdground.yizhida.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
/**
 * 手机验证码界面
 * @author Administrator
 *
 */
public class AuthCodeActivity extends BaseActivity implements
		OnClickListener {
	private ImageView TvBack;
	private EditText EtInputAuthCode;
	private TextView TvGetAuthCode;
	private Button BtConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_verify);
		findView();
		setListener();
	}

	@Override
	public void findView() {
		EtInputAuthCode = (EditText) this.findViewById(R.id.input_auth_code);
		TvGetAuthCode = (TextView) this.findViewById(R.id.get_auth_code);
		BtConfirm = (Button) this.findViewById(R.id.sure);
		TvBack = (ImageView) this.findViewById(R.id.back);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initMemberData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		BtConfirm.setOnClickListener(this);
		TvBack.setOnClickListener(this);
		TvGetAuthCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure: {

			break;
		}
		case R.id.back: {
			onBackPressed();
			break;
		}
		case R.id.get_auth_code: {
			setGetCodeButton();
			break;

		}
		}
	}

	public void setGetCodeButton() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 61; i++) {

					final int k = i;
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (k == 60) {
								TvGetAuthCode.setText("重新发送");
								TvGetAuthCode.setTextColor(getResources()
										.getColor(R.color.myblue));
								TvGetAuthCode.setClickable(true);
							} else {
								TvGetAuthCode.setText("正在发送(" + (60 - k) + ")");
								TvGetAuthCode.setTextColor(getResources()
										.getColor(R.color.mynor_text));
								TvGetAuthCode.setClickable(false);
							}

						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

}
