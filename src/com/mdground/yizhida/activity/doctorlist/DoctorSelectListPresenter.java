package com.mdground.yizhida.activity.doctorlist;

import java.util.List;

import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;

public interface DoctorSelectListPresenter {

	public void saveAppointment(AppointmentInfo appointment);
	
	//获取医生列表
	public void getDoctorList();

	public void assignAppointment(AppointmentInfo appointmentInfo, int doctorId);
	
	//获取医生候诊人数
	public void getWaitingCountForDoctorList(List<Doctor> doctors);
}
