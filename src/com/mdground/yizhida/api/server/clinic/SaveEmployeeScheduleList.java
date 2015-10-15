package com.mdground.yizhida.api.server.clinic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.Schedule;

public class SaveEmployeeScheduleList extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveEmployeeScheduleList";

	public SaveEmployeeScheduleList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveEmployeeScheduleList(Date workDate, List<Schedule> schedules, RequestCallBack callBack) {
		if (workDate == null || schedules == null) {
			return;
		}

		setRequestCallBack(callBack);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.CHINA);
		
		PostData postData = new PostData();
		try {
			postData.DSDate = sdf.parse(sdf.format(workDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		postData.DSList = schedules;

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String jsonSchedules = gson.toJson(postData);
		RequestData data = getData();
		data.setQueryData(jsonSchedules);

		pocess();
	}

	protected class PostData {
		private Date DSDate;
		private List<Schedule> DSList;

		public PostData() {
		}

		public Date getDSDate() {
			return DSDate;
		}

		public void setDSDate(Date dSDate) {
			DSDate = dSDate;
		}

		public List<Schedule> getDSList() {
			return DSList;
		}

		public void setDSList(List<Schedule> dSList) {
			DSList = dSList;
		}

	}
}
