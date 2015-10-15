package com.mdground.yizhida.activity.rota;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.Schedule;

public interface RotaView extends BaseView {

	void updateScheduleListView(List<Schedule> schedulesList);

}
