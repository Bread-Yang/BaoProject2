package com.mdground.yizhida.activity.patientinfo;

import java.io.File;

import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;

public interface PatientEditPresenter {

	//保存病人信息
	public void savePatient(Patient patient);

	public void saveAppointment(AppointmentInfo appointmentInfo);

	public void uploadPhoto(Patient patient, File file);
	public void uploadPhotoSID(Patient patient, File file);
	
	public void saveEmployeePhoto(int PhotoID, int PhotoSID);
	

}
