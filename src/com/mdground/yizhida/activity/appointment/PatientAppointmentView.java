package com.mdground.yizhida.activity.appointment;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.Patient;

public interface PatientAppointmentView extends BaseView {
	
	//修改预约状态更新界面
	void updateViewSatus(int status);
	//请求到病人详情更新界面
	void updateViewData(Patient patient);
	void showCallButton(boolean visable);
	void updateAnamnesisData(List<Anamnesis> anamnesisList);
	void updateAppointmentDetail(String jsonString);
}
