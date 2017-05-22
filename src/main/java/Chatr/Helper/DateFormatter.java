package Chatr.Helper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateFormatter {
	/**
	 * converts a timestamp in milliseconds to a formatted time string
	 *
	 * @param timestamp time in ms since 01.01.1970
	 * @return formatted Time string
	 */
	public static String convertTimestamp(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		return String.format("%02d:%02d", zdt.getHour(), zdt.getMinute());
	}
}
