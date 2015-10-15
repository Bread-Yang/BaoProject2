package com.mdground.yizhida.activity.doctorlist;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.api.bean.DoctorWaittingCount;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;

public interface DoctorSelectListView extends BaseView {

	public void finishResult(int resultCode, AppointmentInfo appointmentInfo);

	public void updateDoctorList(List<Doctor> doctorsList);

	public void updateWaitingCount(List<DoctorWaittingCount> waittingCount);
}
