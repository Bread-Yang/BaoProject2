package com.mdground.yizhida.activity.rota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.mdground.yizhida.bean.Schedule;

public class ScheduleHelper {

	/**
	 * 合并
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String mergeSchedulesToStr(List<Schedule> scheduleList) {
		String pattern = "00";
		java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);

		if (scheduleList == null || scheduleList.size() == 0) {
			return "";
		}

		Collections.sort(scheduleList, new Comparator<Schedule>() {

			@Override
			public int compare(Schedule lhs, Schedule rhs) {
				return lhs.compareTo(rhs);
			}
		});

		Stack<Integer> s = new Stack<Integer>();
		Stack<Integer> e = new Stack<Integer>();

		for (Schedule schedule : scheduleList) {
			if (schedule.getStatus() == 3) {
				continue;
			}

			if (schedule.getBeginHour() > schedule.getEndHour())
				continue;

			if (e.isEmpty()) {
				e.push(schedule.getEndHour());
			}

			if (s.isEmpty()) {
				s.push(schedule.getBeginHour());
			}

			if (schedule.getBeginHour() > (int) e.peek()) // 没有交集
			{
				s.push(schedule.getBeginHour());
				e.push(schedule.getEndHour());
			} else if (schedule.getEndHour() > (int) e.peek()) // 有部分交集，取并集
			{
				e.pop();
				e.push(schedule.getEndHour());
			}
			// else {} //完全被覆盖
		}

		List<String> result = new ArrayList<String>();
		int total = 0;
		while (!s.empty()) {
			StringBuffer sb = new StringBuffer();
			sb.append(df.format(s.peek() / 60) + ":" + df.format(s.peek() % 60));
			sb.append("~");
			sb.append(df.format(e.peek() / 60) + ":" + df.format(e.peek() % 60));
			sb.append("\n");
			result.add(sb.toString());
			total += (int) e.pop() - (int) s.pop();
		}

		StringBuffer sb = new StringBuffer();
		for (int i = result.size() - 1; i >= 0; i--) {
			sb.append(result.get(i));
		}
		return sb.toString().trim();
	}

	public static List<Schedule> mergeSchedulesToList(List<Schedule> scheduleList) {
		if (scheduleList == null || scheduleList.size() == 0) {
			return null;
		}

		Collections.sort(scheduleList, new Comparator<Schedule>() {

			@Override
			public int compare(Schedule lhs, Schedule rhs) {
				return lhs.compareTo(rhs);
			}
		});

		Schedule tmpSchedule = scheduleList.get(0);
		Stack<Integer> s = new Stack<Integer>();
		Stack<Integer> e = new Stack<Integer>();

		for (Schedule schedule : scheduleList) {
			if (schedule.getStatus() == 3) {
				continue;
			}

			if (schedule.getBeginHour() > schedule.getEndHour())
				continue;

			if (e.isEmpty()) {
				e.push(schedule.getEndHour());
			}

			if (s.isEmpty()) {
				s.push(schedule.getBeginHour());
			}

			if (schedule.getBeginHour() > (int) e.peek()) // 没有交集
			{
				s.push(schedule.getBeginHour());
				e.push(schedule.getEndHour());
			} else if (schedule.getEndHour() > (int) e.peek()) // 有部分交集，取并集
			{
				e.pop();
				e.push(schedule.getEndHour());
			}
			// else {} //完全被覆盖
		}

		List<Schedule> result = new ArrayList<Schedule>();
		int total = 0;
		Schedule schedule = null;
		while (!s.empty()) {
			schedule = new Schedule();
			schedule.setBeginHour(s.peek());
			schedule.setEndHour(e.peek());
			schedule.setClinicId(tmpSchedule.getClinicId());
			schedule.setEmployeeID(tmpSchedule.getEmployeeID());
			schedule.setStatus(1);
			schedule.setUpdateTime(tmpSchedule.getUpdateTime());
			schedule.setWorkDate(tmpSchedule.getWorkDate());
			result.add(schedule);
			total += (int) e.pop() - (int) s.pop();
		}

		Collections.sort(result, new Comparator<Schedule>() {

			@Override
			public int compare(Schedule lhs, Schedule rhs) {
				return lhs.compareTo(rhs);
			}
		});

		return result;
	}

	public static boolean isVaction(List<Schedule> schedules) {
		if (schedules == null || schedules.size() == 0) {
			return true;
		}

		boolean isVaction = true;
		for (int i = 0; i < schedules.size(); i++) {
			Schedule s = schedules.get(i);
			if (s.getStatus() == 1) {
				isVaction = false;
				break;
			}
		}

		return isVaction;
	}
}
