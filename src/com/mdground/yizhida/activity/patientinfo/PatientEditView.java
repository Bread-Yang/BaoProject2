package com.mdground.yizhida.activity.patientinfo;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;

/**
 * 定义修改病人信息View接口
 * @author Vincent
 *
 */
public interface PatientEditView extends BaseView{

	void finishSave(Patient patient);

	void finishResult(int appiontmentResoultCode, AppointmentInfo appointmentInfo);

}
