package com.mdground.yizhida.activity.waiting;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface WaitingRomPresenter {

	//获取预约列表
	public void getAppointmentInfoListByDoctor(int status, int doctorId);

	// 重新分配医生
	public void updateAppointmentByAssign();

	// 修改预约状态
	public void updateAppointment(AppointmentInfo appointment, int status);

}
