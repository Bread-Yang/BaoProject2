package com.mdground.yizhida.activity.income;

import java.math.BigDecimal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.api.bean.IncomeStatisticInfo;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.MdgConfig;
import com.mdground.yizhida.view.RiseNumberTextView;

public class IncomeActivity extends BaseActivity implements OnClickListener, IncomeView {

	private RiseNumberTextView tv_yesterday_income;
	private RiseNumberTextView tv_total_income;
	private RiseNumberTextView tv_week_income;
	private RiseNumberTextView tv_month_income;
	private RiseNumberTextView tv_total_withdraw;
	private RiseNumberTextView tv_not_settle_balance;
	private Handler mHandler;

	private IncomePresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income);
		findView();
		setListener();
		initMemberData();
		initView();
	}

	@Override
	public void findView() {
		tv_yesterday_income = (RiseNumberTextView) findViewById(R.id.tv_yesterday_income);
		tv_total_income = (RiseNumberTextView) findViewById(R.id.tv_total_income);
		tv_week_income = (RiseNumberTextView) findViewById(R.id.tv_week_income);
		tv_month_income = (RiseNumberTextView) findViewById(R.id.tv_month_income);
		tv_total_withdraw = (RiseNumberTextView) findViewById(R.id.tv_total_withdraw);
		tv_not_settle_balance = (RiseNumberTextView) findViewById(R.id.tv_not_settle_balance);
		findViewById(R.id.roll_out).setVisibility(View.GONE);
	}

	@Override
	public void initView() {
		tv_yesterday_income.setTypeface(MedicalConstant.NotoSans_Regular);
		tv_total_income.setTypeface(MedicalConstant.NotoSans_Regular);
		tv_week_income.setTypeface(MedicalConstant.NotoSans_Regular);
		tv_month_income.setTypeface(MedicalConstant.NotoSans_Regular);
		tv_total_withdraw.setTypeface(MedicalConstant.NotoSans_Regular);
		tv_not_settle_balance.setTypeface(MedicalConstant.NotoSans_Regular);
	}

	@Override
	public void initMemberData() {
		mHandler = new Handler();
		presenter = new IncomePresenterImpl(this);
	}

	@Override
	public void setListener() {
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.getEmployeeIncomeStatisticInfo();
	}

	@Override
	public void onClick(View v) {
		int type = -1;
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.roll_out: {
			// Intent intent = new Intent(this, TurnOutActivity.class);
			// startActivity(intent);
			break;
		}
		case R.id.tv_yesterday_income:
			type = MemberConstant.INCOME_TODAY;
			break;
		case R.id.tv_total_income:
			type = MemberConstant.INCOME_TOTAL;
			break;
		case R.id.tv_week_income:
			type = MemberConstant.INCOME_WEEK;
			break;
		case R.id.tv_month_income:
			type = MemberConstant.INCOME_MONTH;
			break;
		default:
			break;
		}

		// if (type != -1) {
		// Intent intent = new Intent(this, IncomeInfoActivity.class);
		// intent.putExtra(MemberConstant.INCOME_TYPE, type);
		// startActivity(intent);
		// }
	}

	@Override
	public void updateView(IncomeStatisticInfo incomeStatisticInfo) {
		// 个人收益
		tv_yesterday_income.setText(String.valueOf(
				new BigDecimal(incomeStatisticInfo.getYesterdayIncome()).divide(new BigDecimal(100)).floatValue()),
				TextView.BufferType.NORMAL, mHandler);
		tv_total_income.setText(
				String.valueOf(
						new BigDecimal(incomeStatisticInfo.getTotalIncome()).divide(new BigDecimal(100)).floatValue()),
				TextView.BufferType.NORMAL, mHandler);
		tv_week_income.setText(
				String.valueOf(
						new BigDecimal(incomeStatisticInfo.getWeekIncome()).divide(new BigDecimal(100)).floatValue()),
				TextView.BufferType.NORMAL, mHandler);
		tv_month_income.setText(
				String.valueOf(
						new BigDecimal(incomeStatisticInfo.getMonthIncome()).divide(new BigDecimal(100)).floatValue()),
				TextView.BufferType.NORMAL, mHandler);
		tv_total_withdraw.setText(String.valueOf(
				new BigDecimal(incomeStatisticInfo.getTotalWithdrawal()).divide(new BigDecimal(100)).floatValue()),
				TextView.BufferType.NORMAL, mHandler);
		float not_settle_balance = (new BigDecimal(incomeStatisticInfo.getTotalIncome())
				.subtract(new BigDecimal(incomeStatisticInfo.getTotalWithdrawal()))).divide(new BigDecimal(100))
						.floatValue();
		tv_not_settle_balance.setText(String.valueOf(not_settle_balance), TextView.BufferType.NORMAL, mHandler);

	}

}
