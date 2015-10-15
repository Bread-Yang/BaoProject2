package com.mdground.yizhida.activity.rota;

import java.util.Date;

public interface RotaPresenter {

	//获取
	public void getEmployeeScheduleList(int employeeId, Date beginDate, Date endDate);
}
