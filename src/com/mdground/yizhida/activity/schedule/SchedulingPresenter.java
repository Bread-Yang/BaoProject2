package com.mdground.yizhida.activity.schedule;

import java.util.Date;
import java.util.List;

import com.mdground.yizhida.bean.Schedule;

public interface SchedulingPresenter {
	
	public void saveEmployeeScheduleList(Date currentDate, List<Schedule> schedules);

	public void getEmployeeSchedules(Date currentDate, int employeeId);
}
