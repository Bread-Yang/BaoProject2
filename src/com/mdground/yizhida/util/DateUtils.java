package com.mdground.yizhida.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 
 * @author Vincent
 * 
 */
public class DateUtils {
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
	static java.text.DecimalFormat df = new java.text.DecimalFormat("00");

	public static long getDateMILL(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据出生年月计算年龄
	 * 
	 * @param date
	 * @return
	 */
	public static int calcAgeByBrithday(Date birthDay) {

		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}

		return age;
	}

	public static int calcAgeMonthByBrithday(Date birthDay) {

		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int monthNow = cal.get(Calendar.MONTH);

		cal.setTime(birthDay);
		int monthBirth = cal.get(Calendar.MONTH);

		int mounth = 0;
		if (monthNow < monthBirth) {
			mounth = monthBirth;
		} else {
			mounth = monthBirth - monthNow;
		}

		return Math.abs(mounth);
	}

	/**
	 * 计算现在离生日那天还有多少天
	 * 
	 * @param birthDay
	 * @return
	 */
	public static int daysBetween(Date birthDay) {
		Calendar cal = Calendar.getInstance();
		long time1 = cal.getTimeInMillis();
		cal.setTime(birthDay);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Math.abs(Integer.parseInt(String.valueOf(between_days)));
	}

	public static String getDateString(Date date) {
		return simpleDateFormat.format(date);
	}

	/**
	 * 格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date, SimpleDateFormat simpleDataFormat) {
		if (simpleDataFormat == null) {
			return "";
		}

		if (date == null) {
			date = new Date();
		}
		return simpleDataFormat.format(date);
	}

	/**
	 * 格式4月9日
	 * 
	 * @return
	 */
	public static String getMouthDay(Date date) {
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
		return simpleDataFormat.format(date);
	}

	public static String getDay(Date date) {
		return getDateString(date, new SimpleDateFormat("dd", Locale.CHINA));
	}

	/**
	 * 获取星期几
	 * 
	 * @return
	 */
	public static String getWeekDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (dayOfWeek) {
		case 1:
			str = "星期日";
			break;
		case 2:
			str = "星期一";
			break;
		case 3:
			str = "星期二";
			break;
		case 4:
			str = "星期三";
			break;
		case 5:
			str = "星期四";
			break;
		case 6:
			str = "星期五";
			break;
		case 7:
			str = "星期六";
			break;

		default:
			break;
		}
		return str;
	}

	/**
	 * 将字符转成date
	 * 
	 * @param dateStr
	 * @param simpleDateFormat2
	 * @return
	 */
	public static Date toDate(String dateStr, SimpleDateFormat simpleDateFormat) {
		if (simpleDateFormat == null || dateStr == null) {
			return null;
		}

		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 判断是否为一个月的第一天
	public static boolean isMouthBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int count = calendar.get(Calendar.DAY_OF_MONTH);
		if (count == 1) {
			return true;
		}
		return false;

	}

	// 将时间转换成时间范围
	public static String minToTimeRange(int beginHour) {
		if (beginHour < 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(df.format(beginHour / 60) + ":" + df.format(beginHour % 60));
		return sb.toString();
	}

	// 将时间转换成分钟
	public static int timeToHour(String beginTime) {
		String[] times = beginTime.split(":");
		int hour = 0;
		if (times.length == 2) {
			hour += Integer.parseInt(times[0]) * 60;
			hour += Integer.parseInt(times[1]);
		}
		return hour;
	}

	public static String getMounth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
		int nMounth = Integer.parseInt(sdf.format(date));
		String mounthStr = null;
		switch (nMounth) {
		case 1:
			mounthStr = "一";
			break;
		case 2:
			mounthStr = "二";
			break;
		case 3:
			mounthStr = "三";
			break;
		case 4:
			mounthStr = "四";
			break;
		case 5:
			mounthStr = "五";
			break;
		case 6:
			mounthStr = "六";
			break;
		case 7:
			mounthStr = "七";
			break;
		case 8:
			mounthStr = "八";
			break;
		case 9:
			mounthStr = "九";
			break;
		case 10:
			mounthStr = "十";
			break;
		case 11:
			mounthStr = "十一";
			break;
		case 12:
			mounthStr = "十二";
			break;
		default:
			break;
		}
		return mounthStr;
	}

	public static Date getDate(SimpleDateFormat simpleDateFormat2) {
		if (simpleDateFormat2 == null) {
			return null;
		}
		Date today = new Date();
		String date = simpleDateFormat2.format(today);
		try {
			return simpleDateFormat2.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 当前时段和预约的时段对比
	 * 
	 * @param opPeriod 
	 * 预约的时段
	 * 
	 * @return  
	 * 1 : 大于, 0 : 等于, -1 : 小于
	 */
	public static int compareToCurrentPeriod(int opPeriod) {
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

		currentHour = currentHour / 2 + 1;
		
		if (opPeriod > currentHour) {
			return 1;
		} else if (opPeriod == currentHour) {
			return 0;
		} else {
			return -1;
		}
		
	}

}
