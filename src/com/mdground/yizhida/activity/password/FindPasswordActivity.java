package com.mdground.yizhida.activity.password;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.dialog.LoadingDialog;
import com.mdground.yizhida.util.RegexUtil;
import com.mdground.yizhida.view.TitleBar;

/**
 * 找回密码，填写手机号页面
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends TitleBarActivity implements OnClickListener, FindPasswordView {
	private EditText etPhone;
	private Button btnComfirm;

	private FindPasswordPresenter presenter;
	private LoadingDialog mLoadingDialog;

	@Override
	public int getContentLayout() {
		return R.layout.activity_password_find2;
	}

	@Override
	public void findView() {
		etPhone = (EditText) this.findViewById(R.id.input_phone);
		btnComfirm = (Button) this.findViewById(R.id.sure);
	}

	@Override
	public void initView() {

	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);

		titleBar.setTitle("找回密码");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void initMemberData() {
		presenter = new FindPasswordPresenterImpl(this);
		mLoadingDialog = LoadingDialog.getInstance(this, "验证中");
	}

	@Override
	public void setListener() {
		etPhone.setOnClickListener(this);
		btnComfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure:
			String strPhone = etPhone.getText().toString();
			if (strPhone == null || strPhone.equals("")) {
				showToast("请输入手机号码");
				return;
			}
			
			if (!RegexUtil.isPhoneNumber(strPhone)) {
				showToast("手机号码不存在");
				return;
			}
			presenter.checkPhone(etPhone.getText().toString());
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
			finish();
		}
	}

	@Override
	public void navigateToVerfyCode() {
		Intent intent = new Intent(this, VerifyPasswordActivity.class);
		intent.putExtra(MemberConstant.PHONE, etPhone.getText().toString());
		startActivityForResult(intent, MemberConstant.PASSWORD_REQUEST_CODE);
	}

	@Override
	public void showProgress() {
		mLoadingDialog.show();
	}

	@Override
	public void hidProgress() {
		mLoadingDialog.dismiss();
	}
}
