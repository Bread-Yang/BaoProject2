package com.mdground.yizhida.activity.income;

import org.apache.http.Header;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.PostEmployeeWithdrawal;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.TitleBar;

import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 收益详情
 * 
 * @author Vincent
 * 
 */
public class TurnOutActivity extends TitleBarActivity {

	private TextView tv_binding_tips;
	private Button btn_confirm;
	private EditText et_amount;
	private float not_settle_balance;

	@Override
	public void findView() {
		tv_binding_tips = (TextView) findViewById(R.id.tv_binding_tips);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		et_amount = (EditText) findViewById(R.id.et_amount);
	}

	@Override
	public void initView() {
		tv_binding_tips.setText(Tools.getFormat(getApplicationContext(), R.string.binding_tips,
				getMedicalAppliction().getLoginEmployee().getWeChatName()));
	}

	@Override
	public void initMemberData() {
		not_settle_balance = getIntent().getFloatExtra("not_settle_balance", 0);
		et_amount.setText(String.valueOf((int)not_settle_balance));
		int position = et_amount.length();
		Editable etext = et_amount.getText();
		Selection.setSelection(etext, position);
	}

	@Override
	public void setListener() {
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int amount = Integer.valueOf(et_amount.getText().toString());

				if (amount < 1) {
					Toast.makeText(getApplicationContext(), R.string.no_less_than_1_yuan, Toast.LENGTH_SHORT).show();
					return;
				} else if (amount > 200) {
					Toast.makeText(getApplicationContext(), R.string.no_more_than_200_yuan, Toast.LENGTH_SHORT).show();
					return;
				} else if (amount > not_settle_balance) {
					Toast.makeText(getApplicationContext(), R.string.more_than_no_settle_balance, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				new PostEmployeeWithdrawal(TurnOutActivity.this).postEmployeeWithDraw(amount * 100, new RequestCallBack() {

					@Override
					public void onSuccess(ResponseData response) {
						if (response.getCode() == ResponseCode.Normal.getValue()) {
							Toast.makeText(TurnOutActivity.this, R.string.withdrawal_success, Toast.LENGTH_LONG).show();
							finish();
						} else if (response.getCode() == ResponseCode.AppCustom0.getValue()) {
							Toast.makeText(TurnOutActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(TurnOutActivity.this, R.string.withdrawal_fail, Toast.LENGTH_LONG).show();
						}
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

						L.e(TurnOutActivity.this, "statusCode: " + statusCode);
						L.e(TurnOutActivity.this, "responseString: " + responseString);

					}
				});
			}
		});
	}

	@Override
	public void onLeftClick(View v) {
		onBackPressed();
	}

	@Override
	public int getContentLayout() {
		return R.layout.activity_turn_out;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		titleBar.setTitle("结算");
		titleBar.setBackgroundResource(R.drawable.top_bg4);
		
		TextView textView = titleBar.inflateView(TitleBar.RIGHT, TextView.class);
		textView.setText("hello");
	}

}
