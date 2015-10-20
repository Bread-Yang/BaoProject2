package com.mdground.yizhida.activity.patientinfo;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;

/**
 * 病人详情界面
 * @author Vincent
 *
 */
public interface PatientDetailView extends BaseView{

	void showPaitent(Patient patient);

	void finishResult(int appiontmentResoultCode, AppointmentInfo appointment);
	
	void updateAnamnesisData(List<Anamnesis> anamnesisList);

}
