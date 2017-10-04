package utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class PZDate {
	
	/**
	 * 获取今天0时0分的日期
	 * @return
	 */
	public static Date todayTruncate(){
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}
	
	public static Date tomorrowTruncate(){
		return dayTruncate(1);
	}
	
	public static String today() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	/**
	 * 获取某日0时0分的日期
	 * @param day
	 * @return
	 */
	public static Date dayTruncate(int day){
		return DateUtils.truncate(DateUtils.addDays(new Date(), day), Calendar.DAY_OF_MONTH);
	}
	
}
