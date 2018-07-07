package mml.com.class_design.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {// 可以用new
									
		// Date().toLocalString()传递参数
		
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * @方法名: getCurrentDate
	 * @方法描述: 获取当前日期
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-8-18 上午11:44:59
	 */
	public static String getCurrentDate() {// 可以用new Date().toLocalString()传递参数
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINESE);
		String time = format.format(calendar.getTime());
		return time;
	}

	/**
	 * 获得指定日期的后一天
	 *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param dateString
	 * @return
	 */
	public static String getMonthDay(String dateString) {
		return dateString.substring(5, dateString.length());
	}

	/**
	 * @方法名: getYesterdayOrTomorrow
	 * @方法描述: 获取昨天或者明天
	 * @param timeStr
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-8-20 下午3:54:28
	 */
	public static String getYesterdayOrTomorrow(String crruentTime, String newtime) {
		try {
			// 日期转换为毫秒 两个日期想减得到天数
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			final long day = 1000 * 60 * 60 * 24;
			long timeOld = sdf.parse(crruentTime).getTime();
			long timeNew = sdf.parse(newtime).getTime();
			long dayCount = timeOld - timeNew;
			if (dayCount > 0) {
				return "明天";
			} else if (dayCount < 0) {
				return "昨天";
			} else {
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * @方法名: getDateStr
	 * @方法描述: 获取昨天或者明天
	 * @param timeStr
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-8-20 下午3:54:28
	 */
	public static String getDateStr(String time) {
		String tempDateString = time;
		Log.e("getDateStr", "getDateStr.1-->"+tempDateString);
		if(time.equals(getCurrentDate())){
			return getMonthDay(tempDateString)+"\n今天";
		}else if(time.equals(getSpecifiedDayAfter(getCurrentDate()))){
			return getMonthDay(getSpecifiedDayAfter(getCurrentDate()))+"\n明天";
		}else if(time.equals(getSpecifiedDayBefore(getCurrentDate()))){
			return getMonthDay(getSpecifiedDayBefore(getCurrentDate()))+"\n昨天";
		}
		return getMonthDay(tempDateString);
	}
	

}
