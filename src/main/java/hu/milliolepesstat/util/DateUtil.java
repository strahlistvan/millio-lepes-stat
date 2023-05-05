package hu.milliolepesstat.util;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final Calendar CALENDAR = GregorianCalendar.getInstance();
	static {
		CALENDAR.set(2023, Calendar.JUNE, 11);
	}
	public static final Date END_DATE = CALENDAR.getTime();

	private static Integer monthNameToNumber(String monthName) {
		switch (monthName.toLowerCase()) {
			case "január":
				return Calendar.JANUARY;
			case "február":
				return Calendar.FEBRUARY;
			case "március":
				return Calendar.MARCH;
			case "április":
				return Calendar.APRIL;
			case "május":
				return Calendar.MAY;
			case "június":
				return Calendar.JUNE;
			case "július":
				return Calendar.JULY;
			case "augusztus":
				return Calendar.AUGUST;
			case "szeptember":
				return Calendar.SEPTEMBER;
			case "október":
				return Calendar.OCTOBER;
			case "november":
				return Calendar.NOVEMBER;
			case "december":
				return Calendar.DECEMBER;
			default:
				return -1;
		}
	}
	
	private static Date parseDate(String dateStr) {
		int year = 0, month = 0, day = 0;

		String[] splitted = dateStr.split(" ");
		if (splitted.length > 0 && splitted[0] != "") {
			year = Integer.parseInt(splitted[0].trim().replace(".", ""));
			month = monthNameToNumber(splitted[1].trim());
			day = Integer.parseInt(splitted[2].trim().replace(".", ""));	
		}

		Date result = new GregorianCalendar(year, month, day).getTime();
		System.out.println("parseDate eredménye " + result);
		return result;
	}
	
	private static Date getRefreshDate() {
		Date refreshDate = null;
		try {
			String refreshDateStr = WebScraper.scrapeRefreshDate();
			refreshDate = parseDate(refreshDateStr);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return refreshDate;
	}
	
	public static Integer getRemainingDays() {
		Date now = getRefreshDate();
		Long diffInMs = END_DATE.getTime() - now.getTime();

		if (diffInMs < 0) {
			return 0;
		}

		Integer diffInDays = (int) TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
		return diffInDays;
	}

}
