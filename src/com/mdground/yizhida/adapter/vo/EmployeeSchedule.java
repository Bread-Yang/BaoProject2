package com.mdground.yizhida.adapter.vo;

import java.util.Date;
import java.util.List;

import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;

/**
 * 医生排班表
 * 
 * @author Vincent
 * 
 */
public class EmployeeSchedule {

	private Employee employee;

	private List<Schedule> schedules;
	
	private Date date;
	
	private boolean isVacation = false;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EmployeeSchedule() {
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public boolean isVacation() {
		return isVacation;
	}

	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}

}
