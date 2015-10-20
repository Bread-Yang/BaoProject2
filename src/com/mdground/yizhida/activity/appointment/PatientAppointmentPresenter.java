package com.mdground.yizhida.activity.appointment;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface PatientAppointmentPresenter {

	public void updateAppointment(AppointmentInfo appointment, int status);

	// 重新排队
	public void saveAppointment(AppointmentInfo appointment);

	public void getPatientDetail(int patientId);
	
	public void getPatientMedicalHistory(int patientId);

	// 叫号
	public void callPatient(AppointmentInfo appointment, String doctorName);

	public void reportOpStatus(AppointmentInfo appointment, String doctorName);
	
	//是否连接
	public boolean isConnected();
	
	public void addScreenCallBack();

	public void removeScreenCallBack();

}
