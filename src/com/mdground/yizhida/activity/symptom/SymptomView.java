package com.mdground.yizhida.activity.symptom;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.AppointmentInfo;

public interface SymptomView extends BaseView{

	void finishResult(int appiontmentResoultCode, AppointmentInfo appointmentInfo);

}
