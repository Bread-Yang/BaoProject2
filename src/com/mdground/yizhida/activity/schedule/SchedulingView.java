package com.mdground.yizhida.activity.schedule;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.Schedule;

public interface SchedulingView extends BaseView{

	void finishSaveSchedule();

	void updateScheduleListView(List<Schedule> schedulesList);

}
