package com.mdground.yizhida.activity.schedule;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;

public interface ScheduleTableView extends BaseView{

	void updateEmployees(List<Employee> employees);

	void updateSchedules(List<Schedule> schedules);

	void refreshComplete();

}
