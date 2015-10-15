package com.mdground.yizhida.activity.home;

import java.util.List;

import com.mdground.yizhida.bean.Doctor;


public interface NurseHomePresenter {

	public void getDoctorList();

	public void getWaitingCountForDoctorList(List<Doctor> doctorList);
}
