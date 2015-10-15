package com.mdground.yizhida.activity.home;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface DoctorHomePresenter {

	public void getAppointmentCountForDoctor();

	public void getAppointmentInfoListByDoctor(int status, int doctorId);

	// 重新分配医生
	public void updateAppointmentByAssign();

	// 修改预约状态
	public void updateAppointment(AppointmentInfo appointment, int status);

	// 叫号
	public void callPatient(AppointmentInfo appointment, String doctorName);

	public boolean isConnectScreen();

	public void addScreenCallBack();

	public void removeScreenCallBack();

}
