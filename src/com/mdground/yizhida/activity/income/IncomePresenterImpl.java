package com.mdground.yizhida.activity.income;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;

import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.bean.IncomeStatisticInfo;
import com.mdground.yizhida.api.server.clinic.GetEmployeeIncomeStatisticInfo;
import com.mdground.yizhida.bean.Employee;

public class IncomePresenterImpl implements IncomePresenter {

	IncomeView mView;
	Employee loginEmployee;

	public IncomePresenterImpl(IncomeView view) {
		this.mView = view;
	}

	@Override
	public void getEmployeeIncomeStatisticInfo() {
		GetEmployeeIncomeStatisticInfo getIncome = new GetEmployeeIncomeStatisticInfo((Context) mView);
		getIncome.getEmployeeIncomeStatisticInfo(getEmployee().getEmployeeID(), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					IncomeStatisticInfo incomeStatisticInfo = response.getContent(IncomeStatisticInfo.class);
					mView.updateView(incomeStatisticInfo);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Employee getEmployee() {
		if (loginEmployee == null) {
			MdgAppliction appliction = (MdgAppliction) ((Activity) mView).getApplication();
			loginEmployee = appliction.getLoginEmployee();
		}

		return loginEmployee;
	}

}
