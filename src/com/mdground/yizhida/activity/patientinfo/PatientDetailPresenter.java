package com.mdground.yizhida.activity.patientinfo;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface PatientDetailPresenter {

	/**
	 * 获取病人详情
	 * @param patientId
	 */
	public void getPatient(int patientId);
	
	public void updateAppointment(AppointmentInfo appointment, int status);
	
	public void saveAppointment(AppointmentInfo appointment);
}
