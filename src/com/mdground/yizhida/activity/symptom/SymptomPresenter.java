package com.mdground.yizhida.activity.symptom;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface SymptomPresenter {

	//获取症状列表
	public void getChiefComplaintTemplateList();
	//保存预约
	public void saveAppointment(AppointmentInfo appointment);
	
}
