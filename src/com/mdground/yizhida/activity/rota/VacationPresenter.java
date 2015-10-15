package com.mdground.yizhida.activity.rota;

import java.util.Date;
import java.util.List;

import com.mdground.yizhida.bean.Schedule;

public interface VacationPresenter {
	public void getEmployeeScheduleList(int employeeId, Date beginDate, Date endDate);
	
	//保存
	public void SaveDoctorEmergencyLeave(List<Schedule> schedules);
}
