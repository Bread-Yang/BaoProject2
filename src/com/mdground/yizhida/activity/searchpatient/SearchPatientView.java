package com.mdground.yizhida.activity.searchpatient;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;

public interface SearchPatientView extends BaseView{

	void updateResult(List<Patient> patients);

	void finishResult(int appiontmentResoultCode, AppointmentInfo appointment);

}
