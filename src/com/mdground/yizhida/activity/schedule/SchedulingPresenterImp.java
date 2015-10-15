package com.mdground.yizhida.activity.schedule;

import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.rota.ScheduleHelper;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetEmployeeScheduleList;
import com.mdground.yizhida.api.server.clinic.SaveEmployeeScheduleList;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.ScheduleDao;

public class SchedulingPresenterImp implements SchedulingPresenter {

	SchedulingView mView;
	ScheduleDao mScheduleDao;

	public SchedulingPresenterImp(SchedulingView view) {
		this.mView = view;
		mScheduleDao = ScheduleDao.getInstance((Context) mView);
	}

	@Override
	public void saveEmployeeScheduleList(Date currentDate, List<Schedule> schedules) {
		SaveEmployeeScheduleList saveEmployeeScheduleList = new SaveEmployeeScheduleList((Context) mView);
		saveEmployeeScheduleList.saveEmployeeScheduleList(currentDate, schedules, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.finishSaveSchedule();
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

	@Override
	public void getEmployeeSchedules(Date currentDate, int employeeId) {
		List<Schedule> dbSchedules = mScheduleDao.getSchedules(currentDate, employeeId);
		dbSchedules = ScheduleHelper.mergeSchedulesToList(dbSchedules);
		if (dbSchedules != null && dbSchedules.size() > 0) {
			mView.updateScheduleListView(dbSchedules);
			return;
		}
		
		GetEmployeeScheduleList getEmployeeScheduleList = new GetEmployeeScheduleList((Context) mView);
		getEmployeeScheduleList.getEmployeeScheduleList(currentDate, currentDate, employeeId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Schedule> schedulesList = response.getContent(new TypeToken<List<Schedule>>() {
					});
					mScheduleDao.saveSchedules(schedulesList);
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
