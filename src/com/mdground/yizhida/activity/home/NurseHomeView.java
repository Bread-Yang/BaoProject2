package com.mdground.yizhida.activity.home;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.api.bean.DoctorWaittingCount;
import com.mdground.yizhida.bean.Doctor;

public interface NurseHomeView extends BaseView{

	void updateDoctorListView(List<Doctor> doctors);

	void updateWaitingCount(List<DoctorWaittingCount> waittingCount);

	void refreshComplete();
}
