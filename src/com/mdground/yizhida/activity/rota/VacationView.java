package com.mdground.yizhida.activity.rota;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;

public interface VacationView extends BaseView {

	void updateScheduleListView(List<EmployeeSchedule> schedulesList);

	void finishSaveEmergencyLeave();

}
