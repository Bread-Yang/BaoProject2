package com.mdground.yizhida.activity.password;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

import com.mdground.yizhida.MedicalConstant;

public class VerifyPasswordPresenterImpl implements VerifyPasswordPresenter {
	VerifyPasswordView mVerifyPasswordView;

	private int mTime = 60;// 倒计时60s

	// 倒计时
	private Runnable mCountdownRunnable = new Runnable() {
		@Override
		public void run() {
			if (mTime > 0 && mTime <= 60) {
				mVerifyPasswordView.updateTime(mTime--);
				mHandler.postDelayed(this, 1000);
			} else {
				mTime = 60;
				mVerifyPasswordView.showReSend();
			}
		}
	};

	private EventHandler mEventHandler = new EventHandler() {
		@Override
		public void afterEvent(int event, int result, Object data) {
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码成功
					mVerifyPasswordView.navigateToModifyPwd();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功,开始倒计时
					mHandler.post(mCountdownRunnable);
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				mHandler.sendEmptyMessage(0);
			}
		}
	};

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			mVerifyPasswordView.authCodeError();
			return false;
		}
	});

	public VerifyPasswordPresenterImpl(VerifyPasswordView view) {
		this.mVerifyPasswordView = view;
		// 初始化短信接口
		SMSSDK.initSDK((Context) mVerifyPasswordView, MedicalConstant.APP_KEY, MedicalConstant.APP_SECRECT);
		SMSSDK.registerEventHandler(mEventHandler);
	}

	@Override
	public void checkAuthCode(String phone, String code) {
		if (code == null || code.equals("")) {
			mVerifyPasswordView.authCodeNull();
			return;
		}

		SMSSDK.submitVerificationCode("86", phone, code);
	}

	@Override
	public void getAuthCode(String phone) {
		SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
			/**
			 * 此方法在发送验证短信前被调用，传入参数为接收者号码返回true表示此号码无须实际接收短信
			 */
			@Override
			public boolean onSendMessage(String country, String phone) {
				// 可添加测试白名单
				/*
				 * if (phone.equals("18576627750")) { return true; }
				 */
				return false;
			}
		});
	}
}
