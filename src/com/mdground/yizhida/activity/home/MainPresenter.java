package com.mdground.yizhida.activity.home;

import java.util.List;

import com.mdground.yizhida.bean.Doctor;

public interface MainPresenter {

	public void getAppointmentCountForDoctor(List<Doctor> doctorList);

	// 获取症状列表保存数据库中
	public void getChiefComplaintTemplateList();

	// 获取员工列表保存数据库
	public void getEmployeeList();

	//将deviceToken更新到服务器
	public void updateDeviceToken(String deviceToken);
}
