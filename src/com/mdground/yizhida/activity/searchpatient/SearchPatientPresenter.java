package com.mdground.yizhida.activity.searchpatient;

import com.mdground.yizhida.bean.AppointmentInfo;

public interface SearchPatientPresenter {
	void searchPatient(String keyword);

	void saveAppointment(AppointmentInfo appointmentInfo);
}
