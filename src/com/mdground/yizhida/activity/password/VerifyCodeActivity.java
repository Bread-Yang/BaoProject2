package com.mdground.yizhida.activity.password;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.RegexUtil;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.TitleBar;

public class VerifyCodeActivity extends TitleBarActivity implements VerifyPasswordView, OnClickListener {

	private VerifyPasswordPresenter presenter;
	private Button btn_confirm;
	private TextView tvGetAuthCode;
	private TextView tvPrompt;
	private EditText etInputAuthCode;

	private String mPhone;

	@Override
	public int getContentLayout() {
		return R.layout.activity_verify_code;
	}

	@Override
	public void findView() {
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		tvGetAuthCode = (TextView) this.findViewById(R.id.get_auth_code);
		tvPrompt = (TextView) this.findViewById(R.id.prompt);
		etInputAuthCode = (EditText) this.findViewById(R.id.input_auth_code);
	}

	@Override
	public void initView() {
		if (mPhone != null && mPhone.length() == 11) {
			String phone = mPhone.substring(0, 3) + "****" + mPhone.substring(7, 11);
			tvPrompt.setText(Tools.getFormat(this, R.string.activity_pwd_verify_prompt, phone));
		} else {
			tvPrompt.setText(Tools.getFormat(this, R.string.activity_pwd_verify_prompt, mPhone));
		}
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {

		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);

		titleBar.setTitle("修改密码");
		titleBar.setBackgroundResource(R.drawable.top_bg1);

	}

	@Override
	public void initMemberData() {
		presenter = new VerifyPasswordPresenterImpl(this);

		Intent intent = getIntent();
		if (intent != null) {
			mPhone = intent.getStringExtra(MemberConstant.PHONE);
		}
	}

	@Override
	public void setListener() {
		btn_confirm.setOnClickListener(this);
		tvGetAuthCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:// 校验验证码
			presenter.checkAuthCode(mPhone, etInputAuthCode.getText().toString());
			break;
		case R.id.get_auth_code:// 获取验证码
			if (mPhone == null || !RegexUtil.isPhoneNumber(mPhone)) {
				Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
				return;
			}
			
			tvGetAuthCode.setClickable(false);

			presenter.getAuthCode(mPhone);
			
			final CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
				@Override
				public void onTick(long arg0) {
					tvGetAuthCode.setText("正在发送(" + arg0 / 1000 + ")");
					tvGetAuthCode.setTextColor(getResources().getColor(R.color.mynor_text));
				}

				@Override
				public void onFinish() {
					tvGetAuthCode.setText("重新发送");
					tvGetAuthCode.setTextColor(getResources().getColor(R.color.myblue));
					tvGetAuthCode.setClickable(true);
					
				}
			};
			countDownTimer.start();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void authCodeError() {
		showToast(R.string.auth_code_error);
	}

	@Override
	public void updateTime(int time) {
//		tvGetAuthCode.setText(Tools.getFormat(this, R.string.activity_pwd_verify_sending, time));
	}

	@Override
	public void showReSend() {
//		tvGetAuthCode.setText(getResources().getString(R.string.activity_pwd_verify_regetAuthCode));
//		tvGetAuthCode.setClickable(true);
	}

	@Override
	public void showError(String error) {
		showToast(error);
	}

	@Override
	public void authCodeNull() {
		showToast(R.string.auth_code_null);
	}

	@Override
	public void navigateToModifyPwd() {
		Intent intent = new Intent();
		intent.setClass(this, ModifyPasswordActivity.class);
		intent.putExtra(MemberConstant.PHONE, mPhone);
		startActivityForResult(intent, MemberConstant.PASSWORD_REQUEST_CODE);
	}

}
