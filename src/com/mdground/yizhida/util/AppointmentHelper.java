package com.mdground.yizhida.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.AppointmentInfo;

/**
 * 预约进行排序等
 * 
 * @author qinglong.huang
 * 
 */
public class AppointmentHelper {

	/**
	 * 将预约排序，先按照就诊状态排序，再按照挂号id排序
	 * 
	 * @return
	 */
	public static List<AppointmentInfo> sortAppoint(List<AppointmentInfo> appointments) {
		Collections.sort(appointments, new Comparator<AppointmentInfo>() {

			@Override
			public int compare(AppointmentInfo lhs, AppointmentInfo rhs) {
				Integer lStatus = 0;
				if ((lhs.getOPStatus() & AppointmentInfo.STATUS_HAS_PAID) == AppointmentInfo.STATUS_HAS_PAID) {
					lStatus = lhs.getOPStatus() | AppointmentInfo.STATUS_HAS_PAID;
				} else {
					lStatus = lhs.getOPStatus();
				}

				Integer rStatus = 0;
				if ((lhs.getOPStatus() & AppointmentInfo.STATUS_HAS_PAID) == AppointmentInfo.STATUS_HAS_PAID) {
					rStatus = rhs.getOPStatus() | AppointmentInfo.STATUS_HAS_PAID;
				} else {
					rStatus = rhs.getOPStatus();
				}
				// 状态相同
				int ret = rStatus.compareTo(lStatus);
				if (ret == 0) {
					ret = lhs.getOPNo().compareTo(rhs.getOPNo());
				}
				return ret;
			}
		});

		return appointments;
	}

	/**
	 * 已就诊和过号排序，降序
	 * 
	 * @param appointments
	 * @return
	 */
	public static List<AppointmentInfo> sort2Appoint(List<AppointmentInfo> appointments) {
		Collections.sort(appointments, new Comparator<AppointmentInfo>() {

			@Override
			public int compare(AppointmentInfo lhs, AppointmentInfo rhs) {
				return rhs.getOPNo().compareTo(lhs.getOPNo());
			}
		});
		return appointments;
	}

	public static List<AppointmentInfo> groupAppointment(int opStatus, List<AppointmentInfo> appointmentList,
			Comparator<Integer> comparator) {

		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

		currentHour = currentHour / 2 + 1;

		List<AppointmentInfo> resultList = new ArrayList<AppointmentInfo>();

		if (appointmentList == null || appointmentList.size() == 0) {
			AppointmentInfo appointmentHeader = new AppointmentInfo();
			appointmentHeader.setType(AppointmentInfo.GROUP);
			appointmentHeader.setOPDatePeriod(currentHour);
			appointmentHeader.setGroup_num(0);
			resultList.add(appointmentHeader); // 增加一个类型位group的空类,作为ListView的Header显示出来

			AppointmentInfo appointmentEmpty = new AppointmentInfo();
			appointmentEmpty.setType(AppointmentInfo.EMPTY);
			appointmentEmpty.setOPStatus(opStatus);
			resultList.add(appointmentEmpty); // 提示 "当前时段无人XX"

			return resultList;
		}

		Map<Integer, List<AppointmentInfo>> mapAppointment = new TreeMap<Integer, List<AppointmentInfo>>(comparator);
		List<AppointmentInfo> disgnosingAppointment = new ArrayList<AppointmentInfo>();
		List<AppointmentInfo> emergencyAppointment = new ArrayList<AppointmentInfo>();
		for (int i = 0; i < appointmentList.size(); i++) {
			AppointmentInfo appointment = appointmentList.get(i);
			if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
				disgnosingAppointment.add(appointment);
				continue;
			}
			
			if (appointment.isEmergency() && opStatus == AppointmentInfo.STATUS_WATTING) {
				emergencyAppointment.add(appointment);
				continue;
			}

			List<AppointmentInfo> tmpList = mapAppointment.get(appointment.getOPDatePeriod());
			if (tmpList == null) {
				tmpList = new ArrayList<AppointmentInfo>();
				mapAppointment.put(appointment.getOPDatePeriod(), tmpList);
			}
			tmpList.add(appointment);
		}

		resultList.addAll(disgnosingAppointment); // 就诊中
		
		if (emergencyAppointment.size() > 0) {
			AppointmentInfo appointmentInfo = new AppointmentInfo();
			appointmentInfo.setType(AppointmentInfo.GROUP);
			appointmentInfo.setOPDatePeriod(0);
			appointmentInfo.setGroup_num(emergencyAppointment.size());
			resultList.add(appointmentInfo); // 增加一个类型位group的空类,作为ListView的Header显示出来
		}
		
		resultList.addAll(emergencyAppointment);  // 急诊
		
		int lastKey = 0;
		if (opStatus != AppointmentInfo.STATUS_WATTING) {
			lastKey = 12;
		}
		boolean isAlreadyAddCurrentTips = false;
		for (Entry<Integer, List<AppointmentInfo>> entry : mapAppointment.entrySet()) {
			if (opStatus == AppointmentInfo.STATUS_WATTING) {
				if (!mapAppointment.containsKey(currentHour) && lastKey < currentHour && currentHour < entry.getKey()) { // 有将来预约的情况
					AppointmentInfo appointmentHeader = new AppointmentInfo();
					appointmentHeader.setType(AppointmentInfo.GROUP);
					appointmentHeader.setOPDatePeriod(currentHour);
					appointmentHeader.setGroup_num(0);
					resultList.add(appointmentHeader); // 增加一个类型位group的空类,作为ListView的Header显示出来

					AppointmentInfo appointmentEmpty = new AppointmentInfo();
					appointmentEmpty.setOPStatus(opStatus);
					appointmentEmpty.setType(AppointmentInfo.EMPTY);
					resultList.add(appointmentEmpty);
				}
			} else {
				if (!mapAppointment.containsKey(currentHour) && lastKey > currentHour && currentHour > entry.getKey()) { // 有将来预约的情况
					isAlreadyAddCurrentTips = true;
					
					AppointmentInfo appointmentHeader = new AppointmentInfo();
					appointmentHeader.setType(AppointmentInfo.GROUP);
					appointmentHeader.setOPDatePeriod(currentHour);
					appointmentHeader.setGroup_num(0);
					resultList.add(appointmentHeader); // 增加一个类型位group的空类,作为ListView的Header显示出来

					AppointmentInfo appointmentEmpty = new AppointmentInfo();
					appointmentEmpty.setOPStatus(opStatus);
					appointmentEmpty.setType(AppointmentInfo.EMPTY);
					resultList.add(appointmentEmpty);
				}
			}

			AppointmentInfo appointmentInfo = new AppointmentInfo();
			appointmentInfo.setType(AppointmentInfo.GROUP);
			appointmentInfo.setOPDatePeriod(entry.getKey());
			appointmentInfo.setGroup_num(entry.getValue().size());
			resultList.add(appointmentInfo); // 增加一个类型位group的空类,作为ListView的Header显示出来
			// entry.getValue().get(0).setType(AppointmentInfo.GROUP); //
			// 这个时间段的第一个预约上面要显示预约时间
			// entry.getValue().get(0).setGroup_num(entry.getValue().size());
			resultList.addAll(entry.getValue());

			lastKey = entry.getKey();
		}
		
		if (opStatus != AppointmentInfo.STATUS_WATTING && !isAlreadyAddCurrentTips && !mapAppointment.containsKey(currentHour)) {
			AppointmentInfo appointmentHeader = new AppointmentInfo();
			appointmentHeader.setType(AppointmentInfo.GROUP);
			appointmentHeader.setOPDatePeriod(currentHour);
			appointmentHeader.setGroup_num(0);
			resultList.add(appointmentHeader); // 增加一个类型位group的空类,作为ListView的Header显示出来

			AppointmentInfo appointmentEmpty = new AppointmentInfo();
			appointmentEmpty.setOPStatus(opStatus);
			appointmentEmpty.setType(AppointmentInfo.EMPTY);
			resultList.add(appointmentEmpty);
		}

		if (opStatus == AppointmentInfo.STATUS_WATTING) {
			if (lastKey < currentHour) { // 所有预约都在当前时间之前的情况(没有将来预约)
				AppointmentInfo appointmentHeader = new AppointmentInfo();
				appointmentHeader.setType(AppointmentInfo.GROUP);
				appointmentHeader.setOPDatePeriod(currentHour);
				appointmentHeader.setGroup_num(0);
				resultList.add(appointmentHeader); // 增加一个类型位group的空类,作为ListView的Header显示出来

				AppointmentInfo appointmentEmpty = new AppointmentInfo();
				appointmentEmpty.setOPStatus(opStatus);
				appointmentEmpty.setType(AppointmentInfo.EMPTY);
				resultList.add(appointmentEmpty);
			}
		}
		
		return resultList;
	}
	
	private void createCurrentEmptyAppointSection() {
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

		currentHour = currentHour / 2 + 1;
	}

}
