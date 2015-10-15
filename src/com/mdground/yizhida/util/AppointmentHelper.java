package com.mdground.yizhida.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

	public static List<AppointmentInfo> groupAppointment(List<AppointmentInfo> appointmentList, Comparator<Integer> comparator) {
		if (appointmentList == null || appointmentList.size() == 0) {
			return null;
		}

		Map<Integer, List<AppointmentInfo>> mapAppointment = new TreeMap<Integer, List<AppointmentInfo>>(comparator);
		List<AppointmentInfo> disgnosingAppointment = new ArrayList<AppointmentInfo>();
		for (int i = 0; i < appointmentList.size(); i++) {
			AppointmentInfo appointment = appointmentList.get(i);
			if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
				disgnosingAppointment.add(appointment);
				continue;
			}

			List<AppointmentInfo> tmpList = mapAppointment.get(appointment.getOPDatePeriod());
			if (tmpList == null) {
				tmpList = new ArrayList<AppointmentInfo>();
				mapAppointment.put(appointment.getOPDatePeriod(), tmpList);
			}
			tmpList.add(appointment);
		}

		List<AppointmentInfo> resultList = new ArrayList<AppointmentInfo>();
		resultList.addAll(disgnosingAppointment);
		for (Entry<Integer, List<AppointmentInfo>> entry : mapAppointment.entrySet()) {
			AppointmentInfo appointmentInfo = new AppointmentInfo();
			appointmentInfo.setType(AppointmentInfo.GROUP);
			appointmentInfo.setOPDatePeriod(entry.getKey());
			resultList.add(appointmentInfo);
			resultList.addAll(entry.getValue());
		}

		return resultList;
	}

}
