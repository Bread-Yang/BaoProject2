package com.mdground.yizhida.activity.rota;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetEmployeeScheduleList;
import com.mdground.yizhida.api.server.clinic.SaveDoctorEmergencyLeave;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.ScheduleDao;

public class VacationPresenterImpl implements VacationPresenter {
	VacationView mView;
	ScheduleDao mScheduleDao;

	public VacationPresenterImpl(VacationView view) {
		this.mView = view;
		mScheduleDao = ScheduleDao.getInstance((Context) mView);
	}

	@Override
	public void getEmployeeScheduleList(final int employeeId, final Date beginDate, final Date endDate) {
		List<Schedule> scheduleList = mScheduleDao.getSchedules(employeeId, beginDate, new Date(endDate.getTime() + 24 * 60 * 60 * 1000));
		if (scheduleList != null && scheduleList.size() > 0) {
			List<EmployeeSchedule> emss = new ArrayList<EmployeeSchedule>();
			EmployeeSchedule ems = findScheduleByDate(employeeId, beginDate, scheduleList);
			if (ems != null) {
				emss.add(ems);
			}
			ems = findScheduleByDate(employeeId, endDate, scheduleList);
			if (ems != null) {
				emss.add(ems);
			}
			mView.updateScheduleListView(emss);
		} else {
			GetEmployeeScheduleList getEmployeeScheduleList = new GetEmployeeScheduleList((Context) mView);
			getEmployeeScheduleList.getEmployeeScheduleList(beginDate, endDate, employeeId, new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					if (response.getCode() == ResponseCode.Normal.getValue()) {
						List<Schedule> schedulesList = response.getContent(new TypeToken<List<Schedule>>() {
						});
						// mScheduleDao.saveSchedules(schedulesList);
						List<EmployeeSchedule> emss = new ArrayList<EmployeeSchedule>();
						EmployeeSchedule ems = findScheduleByDate(employeeId, beginDate, schedulesList);
						if (ems != null) {
							emss.add(ems);
						}
						ems = findScheduleByDate(employeeId, endDate, schedulesList);
						if (ems != null) {
							emss.add(ems);
						}
						mView.updateScheduleListView(emss);
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

	@Override
	public void SaveDoctorEmergencyLeave(List<Schedule> schedules) {

		SaveDoctorEmergencyLeave saveDoctor = new SaveDoctorEmergencyLeave((Context) mView);
		saveDoctor.saveDoctorEmergencyLeave(schedules, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.finishSaveEmergencyLeave();
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
				// TODO Auto-generated method stub
				mView.showToast(R.string.request_error);
			}
		});

	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.CHINA);

	private EmployeeSchedule findScheduleByDate(int employeeId, Date date, List<Schedule> schedules) {
		if (schedules == null || date == null) {
			return null;
		}
		String dateStr = sdf.format(date);

		List<Schedule> resultSchedule = new ArrayList<Schedule>();
		for (int i = 0; i < schedules.size(); i++) {
			Schedule s = schedules.get(i);
			if (sdf.format(s.getWorkDate()).equals(dateStr) && s.getEmployeeID() == employeeId) {
				resultSchedule.add(s);
			}
		}

		if (ScheduleHelper.isVaction(resultSchedule)) {
			return null;
		}

		EmployeeSchedule ems = new EmployeeSchedule();
		ems.setSchedules(resultSchedule);
		ems.setDate(date);

		return ems;
	}
}
