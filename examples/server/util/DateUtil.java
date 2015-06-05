package xptopackage.server.util;

import java.util.Map;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public final class DateUtil {

	private DateUtil() {
	}

	public static final Locale PT_BR_LOCALE = new Locale("pt", "BR");
	public static final Locale EN_US_LOCALE = Locale.US;
	public static final Locale IT_LOCALE = Locale.ITALIAN;
	public static final Locale JA_LOCALE = Locale.JAPANESE;
	public static final Locale ZH_CN_LOCALE = Locale.TRADITIONAL_CHINESE;

	public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");
	public static Map<String, Locale> LOCALES = new HashMap<String, Locale>();

	static {
		LOCALES.put("pt_BR", PT_BR_LOCALE);
		LOCALES.put("en", EN_US_LOCALE);
		LOCALES.put("it", IT_LOCALE);
		LOCALES.put("ja", JA_LOCALE);
		LOCALES.put("zh_TW", ZH_CN_LOCALE);
	}

	/**
	 * return zero date setting minute to 0, second to 0, and millisecond to 0
	 * 
	 * @param date {@link Date}
	 * @return {@link Date}
	 */
	public static Date getDateZeroOClock(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * addOrSubtractDays
	 * 
	 * @param date {@link Date}
	 * @param daysToAddOrRemove int
	 * @return {@link Date}
	 */
	public static Date addOrSubtractDays(Date date, int daysToAddOrRemove) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, daysToAddOrRemove);

		return calendar.getTime();
	}

	/**
	 * return is date1 equal date2 comparing day, year and month
	 * 
	 * @param date1 {@link Date}
	 * @param date2 {@link Date}
	 * @return boolean
	 */
	public static boolean isEquals(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	}

	/**
	 * return date with last time of day
	 * 
	 * @param date {@link Date}
	 * @return {@link Date}
	 */
	public static Date getDateLastTime(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * return current date using pt_BR locale
	 * 
	 * @return {@link Date}
	 */
	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance(PT_BR_LOCALE);
		calendar.setTime(new Date());

		return calendar.getTime();
	}

	/**
	 * return a Date based in a string that represents a date in this format
	 * dd/MM/yyyy with a pt_BR locale
	 * 
	 * @param dateString {@link String}
	 * @return {@link Date}
	 */
	public static Date ddMMyyyyStringToDate(String dateString) throws ParseException {
		return ddMMyyyyStringToDate(dateString, PT_BR_LOCALE);
	}

	/**
	 * return a Date with no time, based in a string that represents a date in
	 * this format dd/MM/yyyy with a pt_BR locale
	 * 
	 * @param dateString {@link String}
	 * @return {@link Date}
	 */
	public static Date ddMMyyyyStringToDateWithoutTime(String dateString) throws ParseException {
		return ddMMyyyyStringToDateWithoutTime(dateString, PT_BR_LOCALE);
	}

	/**
	 * return a date based in a string( that represents a date in format
	 * dd/MM/yyyy ) using the locale that you pass
	 * 
	 * @param dateString {@link String}
	 * @param locale {@link Locale}
	 * @return {@link Date}
	 */
	public static Date ddMMyyyyStringToDate(String dateString, Locale locale) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", locale);

		return df.parse(dateString);
	}

	/**
	 * return a date with no time based in a string( that represents a date in
	 * format dd/MM/yyyy ) using the locale that you pass
	 * 
	 * @param dateString {@link Date}
	 * @param locale {@link Locale}
	 * @return {@link Date}
	 */
	public static Date ddMMyyyyStringToDateWithoutTime(String dateString, Locale locale) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", locale);

		return df.parse(df.format(dateString));
	}

	/**
	 * return a current day as string in dd/MM/yyyy format using the current
	 * defaut_locale
	 * 
	 * @return {@link String} 
	 */
	public static String getCurrentDayAndMonth() {
		Calendar calendar = new GregorianCalendar(DEFAULT_LOCALE);
		calendar.setTime(new Date());

		StringBuilder out = new StringBuilder(getDay(calendar.get(Calendar.DAY_OF_MONTH)) + "-" + getMonth((Calendar.MONTH)));

		return out.toString();
	}

	/**
	 * return the current time as string in format like this sample: 12-05-00
	 * 
	 * @return {@link String}
	 */
	public static String getCurrentTime() {
		Calendar calendar = new GregorianCalendar(DEFAULT_LOCALE);
		calendar.setTime(new Date());

		StringBuilder out = new StringBuilder(getHour(calendar.get(Calendar.HOUR_OF_DAY)) + "-" + getMinute(calendar.get(Calendar.MINUTE)) + "-"
				+ calendar.get(Calendar.SECOND));
		return out.toString();
	}

	/**
	 * return current day month and year in this format dayOfMonth/Month/Year
	 * 
	 * @return {@link String}
	 */
	public static String getCurrentDayMonthAndYear() {
		Calendar calendar = new GregorianCalendar(DEFAULT_LOCALE);
		calendar.setTime(new Date());

		return getDay(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + getMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
	}

	/**
	 * return current day month and year with time like this
	 * day_month_year-hour_of_day-minute-second
	 * 
	 * @return {@link String}
	 */
	public static String getCurrentDayMonthAndYearWithTime() {
		Calendar calendar = new GregorianCalendar(DEFAULT_LOCALE);
		calendar.setTime(new Date());

		StringBuilder out = new StringBuilder(getDay(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + getMonth(calendar.get(Calendar.MONTH)) + "_"
				+ calendar.get(Calendar.YEAR) + "-" + getHour(calendar.get(Calendar.HOUR_OF_DAY)) + "-" + getMinute(calendar.get(Calendar.MINUTE)) + "-"
				+ getSecond(calendar.get(Calendar.SECOND)));
		return out.toString();
	}

	/**
	 * @param day {@link String}
	 * 
	 * @return {@link String} day with 0 before if equal or less than 9
	 */
	public static String getDay(int day) {
		return day < 10 ? "0" + day : "" + day;
	}

	/**
	 * @param second {@link String}
	 * @return {@link String} second with 0 before if equal or less than 9
	 */
	public static String getSecond(int second) {
		return getDay(second);
	}

	/**
	 * @param hour {@link String}
	 * @return {@link String} hour with 0 before if equal or less than 9
	 */
	public static String getHour(int hour) {
		return getDay(hour);
	}

	/**
	 * @param month of {@link Calendar}
	 * @return {@link String} month in month type not Calendar type
	 */
	public static String getMonth(int month) {
		return getDay(month + 1);
	}

	/**
	 * 
	 * @param minute {@link String}
	 * @return {@link String} minute with 0 before if equal or less than 9
	 */
	public static String getMinute(int minute) {
		return getDay(minute);
	}

}