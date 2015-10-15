package com.mdground.yizhida.activity.home;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.AppointmentInfo;

public interface DoctorHomeView extends BaseView {

	void refresh(int status, List<AppointmentInfo> appointmentInfos);

	void refreshComplete();

	void updateComplete();

	void refreshView();
}
