package com.mdground.yizhida.activity.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.activity.wechat.WechatBindUtil.WechatBindSuccessCallBack;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.SaveEmployee;
import com.mdground.yizhida.api.server.global.UpdateEmployeeWeChat;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.api.utils.PxUtil;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.view.TitleBar;
import com.mdground.yizhida.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WechatBindActivity extends TitleBarActivity implements OnClickListener, WechatBindSuccessCallBack {

	private RelativeLayout rlt_bind;
	private LinearLayout llt_wechat;

	private ImageView iv_wechat;
	private TextView tv_wechat_username, tv_tips;
	private Button btn_operation;

	private boolean isFromLoginActivity;

	private WechatBindUtil wechatBindUtil;

	@Override
	public int getContentLayout() {
		return R.layout.activity_wechat_bind;
	}

	@Override
	public void findView() {
		rlt_bind = (RelativeLayout) findViewById(R.id.rlt_bind);
		llt_wechat = (LinearLayout) findViewById(R.id.llt_wechat);
		iv_wechat = (ImageView) findViewById(R.id.iv_wechat);
		tv_wechat_username = (TextView) findViewById(R.id.tv_wechat_username);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		btn_operation = (Button) findViewById(R.id.btn_operation);
	}

	@Override
	public void initView() {
	}

	@Override
	public void initMemberData() {
		isFromLoginActivity = getIntent().getBooleanExtra("isFromLoginActivity", false);

		if (isFromLoginActivity) {
			rlt_bind.setVisibility(View.GONE);
		} else {
			Employee loginEmployee = ((MedicalAppliction) getApplicationContext()).getLoginEmployee();
			if (loginEmployee.getOpenID() == null || "".equals(loginEmployee.getOpenID())) {
				iv_wechat.setImageResource(R.drawable.wechat_nor);
				tv_tips.setText(R.string.bind_tips);
				btn_operation.setText(R.string.bind);
				btn_operation.setBackgroundResource(R.drawable.selector_bg_button_green_oval);
			} else {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) llt_wechat
						.getLayoutParams();
				params.gravity = Gravity.LEFT;
				params.leftMargin = PxUtil.dip2px(getApplicationContext(), 10);
				llt_wechat.setLayoutParams(params);

				tv_wechat_username.setText(loginEmployee.getWeChatName());
				iv_wechat.setImageResource(R.drawable.wechat_sel);
				tv_tips.setText(R.string.unbind_tips);
				btn_operation.setText(R.string.unbind);
				btn_operation.setBackgroundResource(R.drawable.selector_bg_button_red_oval);
			}
		}

		wechatBindUtil = new WechatBindUtil(this, this);
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		if (isFromLoginActivity) {
			titleBar.setVisibility(View.GONE);
		} else {
			ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
			imageVeiw.setImageResource(R.drawable.back);
			titleBar.setTitle(getResources().getString(R.string.bind_wechat));
			titleBar.setBackgroundResource(R.drawable.top_bg1);
		}

	}

	@Override
	public void setListener() {

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (WXEntryActivity.wechat_code != null) {

			wechatBindUtil.getWechatOpenIdAndUnionIdAndNickname();
		}
	}

	public void navigateToWechatBindSuccessfullyActivity(View view) {

		Employee loginEmployee = ((MedicalAppliction) getApplicationContext()).getLoginEmployee();

		new UpdateEmployeeWeChat(getApplicationContext()).updateEmployeeWeChat(loginEmployee.getOpenID(),
				loginEmployee.getUnionID(), loginEmployee.getWeChatName(), new RequestCallBack() {

					@Override
					public void onSuccess(ResponseData response) {

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {

					}
				});

		Intent intent = new Intent(this, WechatBindSuccessfullyActivity.class);

		intent.putExtra(MemberConstant.EMPLOYEE_ROLE,
				((MedicalAppliction) this.getApplication()).getLoginEmployee().getEmployeeRole());
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind:
			wechatBindUtil.bindWechat();
			break;
		case R.id.btn_operation:
			if (((MedicalAppliction) getApplicationContext()).getLoginEmployee().getOpenID() == null) {
				wechatBindUtil.bindWechat();
			} else {
				wechatBindUtil.unBindWechat();
			}
			break;
		}
	}

	@Override
	public void bindSuccess(final String wechat_nickName) {

		if (isFromLoginActivity) {
			Intent intent = new Intent(WechatBindActivity.this, WechatBindSuccessfullyActivity.class);
			startActivity(intent);

			WXEntryActivity.wechat_code = null;

			finish();
		} else {

			runOnUiThread(new Runnable() {
				public void run() {
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) llt_wechat
							.getLayoutParams();
					params.gravity = Gravity.LEFT;
					params.leftMargin = PxUtil.dip2px(getApplicationContext(), 10);
					llt_wechat.setLayoutParams(params);

					tv_wechat_username.setText(wechat_nickName);
					iv_wechat.setImageResource(R.drawable.wechat_sel);
					tv_tips.setText(R.string.unbind_tips);
					btn_operation.setText(R.string.unbind);
					btn_operation.setBackgroundResource(R.drawable.selector_bg_button_red_oval);
				}
			});
		}
	}

	@Override
	public void unBindSuccess() {

		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) llt_wechat.getLayoutParams();
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.leftMargin = 0;
		llt_wechat.setLayoutParams(params);

		tv_wechat_username.setText(R.string.wechat);
		iv_wechat.setImageResource(R.drawable.wechat_nor);
		tv_tips.setText(R.string.bind_tips);
		btn_operation.setText(R.string.bind);
		btn_operation.setBackgroundResource(R.drawable.selector_bg_button_green_oval);
	}

	@Override
	public void bindFail() {

	}
}
