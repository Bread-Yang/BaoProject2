package com.mdground.yizhida.activity.income;

import java.math.BigDecimal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.api.bean.IncomeStatisticInfo;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.view.RiseNumberTextView;

public class IncomeActivity extends BaseActivity implements OnClickListener, IncomeView {

	private RiseNumberTextView tvTodayIncome;
	private RiseNumberTextView tvTotalIncome;
	private RiseNumberTextView tvWeekIncome;
	private RiseNumberTextView tvMonthIncome;
	private RiseNumberTextView tvTurnOut;
	private RiseNumberTextView tvBalance;
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
		tvTodayIncome = (RiseNumberTextView) findViewById(R.id.tv_today_income);
		tvTotalIncome = (RiseNumberTextView) findViewById(R.id.tv_total_income);
		tvWeekIncome = (RiseNumberTextView) findViewById(R.id.tv_week_income);
		tvMonthIncome = (RiseNumberTextView) findViewById(R.id.tv_month_income);
		tvTurnOut = (RiseNumberTextView) findViewById(R.id.tv_turn_out);
		tvBalance = (RiseNumberTextView) findViewById(R.id.tv_balance);
		findViewById(R.id.roll_out).setVisibility(View.GONE);
	}

	@Override
	public void initView() {
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
		case R.id.tv_today_income:
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
		case R.id.tv_turn_out:
			type = MemberConstant.INCOME_TURN_OUT;
			break;
		case R.id.tv_balance: {
			type = MemberConstant.INCOME_BALANCE;
			break;
		}
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
		//个人收益
		tvTodayIncome.setText(String.valueOf(new BigDecimal(incomeStatisticInfo.getYesterdayIncome()).divide(new BigDecimal(100)).floatValue()), TextView.BufferType.NORMAL, mHandler);
		tvTotalIncome.setText(String.valueOf(new BigDecimal(incomeStatisticInfo.getTotalIncome()).divide(new BigDecimal(100)).floatValue()), TextView.BufferType.NORMAL, mHandler);
		tvWeekIncome.setText(String.valueOf(new BigDecimal(incomeStatisticInfo.getWeekIncome()).divide(new BigDecimal(100)).floatValue()), TextView.BufferType.NORMAL, mHandler);
		tvMonthIncome.setText(String.valueOf(new BigDecimal(incomeStatisticInfo.getMonthIncome()).divide(new BigDecimal(100)).floatValue()), TextView.BufferType.NORMAL, mHandler);
//		tvTurnOut.setText(String.valueOf(incomeStatisticInfo.getTotalWithdrawal()), TextView.BufferType.NORMAL, mHandler);
//		tvBalance.setText(String.valueOf(incomeStatisticInfo.getYesterdayIncome()), TextView.BufferType.NORMAL, mHandler);
	}

}
