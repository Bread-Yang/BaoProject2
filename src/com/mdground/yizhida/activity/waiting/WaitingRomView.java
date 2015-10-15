package com.mdground.yizhida.activity.waiting;

import java.util.List;

import com.mdground.yizhida.activity.base.BaseView;
import com.mdground.yizhida.bean.AppointmentInfo;

public interface WaitingRomView extends BaseView{

	void refresh(int status, List<AppointmentInfo> appointmentInfos);

	void refreshComplete();

	//状态更新完成
	void updateStatusComplete();

}
