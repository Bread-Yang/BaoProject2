package com.mdground.yizhida.activity.schedule;

import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetEmployeeScheduleList;
import com.mdground.yizhida.api.server.global.GetEmployeeList;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.EmployeeDao;
import com.mdground.yizhida.db.dao.ScheduleDao;

import android.content.Context;

public class ScheduleTablePresenterImp implements ScheduleTablePresenter {

	ScheduleTableView mView;
	EmployeeDao mEmployeeDao;
	ScheduleDao mScheduleDao;

	public ScheduleTablePresenterImp(ScheduleTableView view) {
		this.mView = view;
		mEmployeeDao = EmployeeDao.getInstance((Context) mView);
		mScheduleDao = ScheduleDao.getInstance((Context) mView);
	}

	@Override
	public void getEmployeeList() {
		List<Employee> dbEmployees = mEmployeeDao.getAll();
		if (dbEmployees == null || dbEmployees.size() == 0) {
			GetEmployeeList getEmloyeeList = new GetEmployeeList((Context) mView);
			getEmloyeeList.getEmployeeList(new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					L.e(ScheduleTablePresenterImp.this, "getEmployeeList()返回的数据 : " + response.getContent());
					
					if (response.getCode() == ResponseCode.Normal.getValue()) {
						List<Employee> employees = response.getContent(new TypeToken<List<Employee>>() {
						});
						mEmployeeDao.saveEmployeess(employees);
						mView.updateEmployees(employees);
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
		} else {
			mView.updateEmployees(dbEmployees);
		}
	}

	@Override
	public void getEmployeeScheduleList(Date beginDate, int count) {

		Date endDate = new Date(beginDate.getTime() + count * 24 * 60 * 60 * 1000);

		GetEmployeeScheduleList getEmployeeScheduleList = new GetEmployeeScheduleList((Context) mView);
		getEmployeeScheduleList.getEmployeeScheduleList(beginDate, endDate, 0, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				L.e(ScheduleTablePresenterImp.this, "getEmployeeScheduleList()返回的数据 : " + response.getContent());
				
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Schedule> schedules = response.getContent(new TypeToken<List<Schedule>>() {
					});
					mScheduleDao.saveSchedules(schedules);
					mView.updateSchedules(schedules);
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
				mView.refreshComplete();
				mView.hidProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}
		});

	}

}
