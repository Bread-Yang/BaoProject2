package com.mdground.yizhida.activity.rota;

import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetEmployeeScheduleList;
import com.mdground.yizhida.bean.Schedule;

public class RotaPresenterImpl implements RotaPresenter {

	private RotaView mView;

	public RotaPresenterImpl(RotaView view) {
		this.mView = view;
	}

	@Override
	public void getEmployeeScheduleList(int employeeId, Date beginDate, Date endDate) {
		GetEmployeeScheduleList getEmployeeScheduleList = new GetEmployeeScheduleList((Context) mView);
		getEmployeeScheduleList.getEmployeeScheduleList(beginDate, endDate, employeeId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Schedule> schedulesList = response.getContent(new TypeToken<List<Schedule>>() {
					});
					mView.updateScheduleListView(schedulesList);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				mView.showProgress();
			}

			@Override
			public void onFinish() {
				mView.hidProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}
		});

	}

	

}
