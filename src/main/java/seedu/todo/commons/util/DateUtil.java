package seedu.todo.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * A utility class for Dates and LocalDateTimes
 */
public class DateUtil {

    private static final String FROM_NOW = "later";
    private static final String TILL_NOW = "ago";
    private static final String TODAY = "Today";
    private static final String TOMORROW = "Tomorrow";
    private static final String DAY = "day";
    private static final String DAYS = "days";
    
    /**
     * Converts a LocalDateTime object to a legacy java.util.Date object.
     * 
     * @param dateTime   LocalDateTime object.
     * @return           Date object.
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Performs a "floor" operation on a LocalDateTime, and returns a new LocalDateTime
     * with time set to 00:00.
     * 
     * @param dateTime   LocalDateTime for operation to be performed on.
     * @return           "Floored" LocalDateTime.
     */
    public static LocalDateTime floorDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(0, 0);
    }

    /**
     * Formats a LocalDateTime to a relative date. 
     * Prefers DayOfWeek format, for dates up to 6 days from today.
     * Otherwise, returns a relative time (e.g. 13 days from now).
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           Formatted relative day.
     */
    public static String formatDay(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        long daysDifference = LocalDate.now().until(date, ChronoUnit.DAYS);

        // Consider today's date.
        if (date.isEqual(LocalDate.now())) {
            return String.format("%s %s", TODAY, formatDateDisplay(date, true));
        }
        
        if (daysDifference == 1) {
            return String.format("%s %s", TOMORROW, formatDateDisplay(date, true));
        }

        // Consider dates up to 6 days from today.
        if (daysDifference > 1 && daysDifference <= 6) {
            return String.format("%s %s", date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US), formatDateDisplay(date, false));
                   
        }

        // Otherwise, dates should be a relative days ago/from now format.
        return String.format("%d %s %s", Math.abs(daysDifference), 
                StringUtil.pluralizer((int) Math.abs(daysDifference), DAY, DAYS), 
                daysDifference > 0 ? FROM_NOW : TILL_NOW);
    }
    
    /**
     * Formats a LocalDateTime to a shorten date. 
     * 
     * @param dateTime   LocalDateTime to format, withDaysOfWeek.
     * @return           Formatted shorten day.
     */
    private static String formatDateDisplay(LocalDate date, boolean withDaysOfWeek) {
        //return with the days of the week
        if (withDaysOfWeek) {
            return String.format("%s %s %s", date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US),
                    date.getDayOfMonth(), date.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));
        }
        else {
            return String.format("%s %s", date.getDayOfMonth(), date.getMonth().getDisplayName(TextStyle.FULL, Locale.US).substring(0, 3));            
        }
    }

}
