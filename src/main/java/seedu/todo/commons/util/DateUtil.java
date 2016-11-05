package seedu.todo.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// @@author A0139812A
/**
 * A utility class for Dates and LocalDateTimes
 */
public class DateUtil {
    
    public static final LocalDateTime NO_DATETIME_VALUE = LocalDateTime.MIN;
    public static final LocalDate NO_DATE_VALUE = NO_DATETIME_VALUE.toLocalDate();

    private static final String FROM_NOW = "later";
    private static final String TILL_NOW = "ago";
    private static final String TODAY = "Today";
    private static final String TOMORROW = "Tomorrow";
    private static final String DAY = "day";
    private static final String DAYS = "days";
    
    // @@author A0139812A
    /**
     * Converts a LocalDateTime object to a legacy java.util.Date object.
     * 
     * @param dateTime   LocalDateTime object.
     * @return           Date object.
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // @@author A0139812A
    /**
     * Performs a "floor" operation on a LocalDateTime, and returns a new LocalDateTime
     * with time set to 00:00.
     * 
     * @param dateTime   LocalDateTime for operation to be performed on.
     * @return           "Floored" LocalDateTime.
     */
    public static LocalDateTime floorDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return dateTime.toLocalDate().atTime(0, 0);
    }
    
    // @@author A0139922Y
    /**
     * Performs a "ceiling" operation on a LocalDateTime, and returns a new LocalDateTime
     * with time set to 23:59.
     * 
     * @param dateTime   LocalDateTime for operation to be performed on.
     * @return           "Ceiled" LocalDateTime.
     */
    public static LocalDateTime ceilDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return dateTime.toLocalDate().atTime(23, 59);
    }
    
    // @@author A0139812A
    /**
     * Formats a LocalDateTime to a relative date. 
     * Prefers DayOfWeek format, for dates up to 6 days from today.
     * Otherwise, returns a relative time (e.g. 13 days from now).
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           Formatted relative day.
     */
    public static String formatDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        LocalDate date = dateTime.toLocalDate();
        long daysDifference = LocalDate.now().until(date, ChronoUnit.DAYS);

        // Consider today's date.
        if (date.isEqual(LocalDate.now())) {
            return TODAY;
        }
        
        if (daysDifference == 1) {
            return TOMORROW;
        }

        // Consider dates up to 6 days from today.
        if (daysDifference > 1 && daysDifference <= 6) {
            return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
        }

        // Otherwise, dates should be a relative days ago/from now format.
        return String.format("%d %s %s", Math.abs(daysDifference), 
                StringUtil.pluralizer((int) Math.abs(daysDifference), DAY, DAYS), 
                daysDifference > 0 ? FROM_NOW : TILL_NOW);
    }
    
    // @@author A0139812A
    /**
     * Formats a LocalDateTime to a formatted date, following the dd MMM yyyy format.
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           Formatted date in dd MMM yyyy format.
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /**
     * Formats a LocalDateTime to a ISO formatted date, following the ISO yyyy-MM-dd format.
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           Formatted date in ISO format.
     */
    public static String formatIsoDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    /**
     * Formats a LocalDateTime to a short date. Excludes the day of week only if
     * the date is within 2-6 days from now.
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           Formatted shorten day.
     */
    public static String formatShortDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        LocalDate date = dateTime.toLocalDate();
        long daysDifference = LocalDate.now().until(date, ChronoUnit.DAYS);
        String dateFormat;
        
        // Don't show dayOfWeek for days d, such that d = {n+2,...,n+6}, where n = date now
        if (daysDifference >= 2 && daysDifference <= 6) {
            dateFormat = "dd MMM";
        } else {
            dateFormat = "E dd MMM";
        }
        
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }
    
    // @@author A0139812A
    /**
     * Parses a short date (as defined in {@link formatShortDate}) back to a LocalDateTime.
     * We ignore the day of week portion for simplicity, since the shortDate can optionally omit it.
     * 
     * @param shortDateToParse   Date string to format.
     * @return                  Parsed LocalDateTime.
     */
    public static LocalDate parseShortDate(String shortDateToParse) {
        String[] dateParts = shortDateToParse.split(" ");
        String dateString;
        
        // Get the current year to add to the parsing since we cannot parse without a year...
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        if (dateParts.length < 2 || dateParts.length > 3) {
            return null;
        }
        
        if (dateParts.length == 3) {
            dateString = String.format("%s %s %d", dateParts[1], dateParts[2], currentYear);
        } else {
            dateString = String.format("%s %s %d", dateParts[0], dateParts[1], currentYear);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return LocalDate.parse(dateString, formatter);
    }
    
    // @@author A0139812A
    /**
     * Formats a LocalDateTime to a 24-hour time.
     * 
     * @param dateTime   LocalDateTime to format.
     * @return           24-hour time formatted string.
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    // @@author A0139812A
    /**
     * Formats a LocalDateTime into short date + time format.
     * @param dateTime   LocalDateTime to format.
     * @return           Short date + time formatted string.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return String.format("%s %s", formatShortDate(dateTime), formatTime(dateTime));
    }
    
    // @@author A0139812A
    /**
     * Formats a start date and end date to a date range, which will display only as much info as necessary.
     * @param dateFrom   LocalDateTime from.
     * @param dateTo     LocalDateTime to.
     * @return           Formatted string.
     */
    public static String formatDateFromTo(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom == null && dateTo == null) {
            return "";
        } else if (dateTo == null) {
            // No endDate
            return formatTime(dateFrom);
        } else if (dateFrom.isAfter(dateTo)) {
            // Unhandled error, just ignore endDate and assume it has no endDate
            return formatTime(dateFrom);
        } else if (dateFrom.toLocalDate().equals(dateTo.toLocalDate())) {
            return String.format("%s - %s", formatTime(dateFrom), formatTime(dateTo));
        } else {
            return String.format("%s - %s", formatDateTime(dateFrom), formatDateTime(dateTo));
        }
    }

    // @@author A0139812A
    /**
     * Parses a dateTime string with the standard ISO format {@code yyyy-MM-dd HH:mm:ss}.
     * 
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    // @@author A0139922Y    
    /**
     * Check a LocalDateTime if the time is the same as the current time
     * 
     * @param date
     * @return true if it is not the same as current time, false if it is the same as current time 
     */
    public static boolean checkIfTimeExist(LocalDateTime date) {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.getHour() != date.getHour() || currentTime.getMinute() != date.getMinute();
    }

    // @@author A0139922Y
    /**
     * Check a LocalDateTime if the date is the same as the current date
     * 
     * @param date
     * @return true if it is not the same as current date, false if it is the same as current date 
     */
    public static boolean checkIfDateExist(LocalDateTime date) {
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.getDayOfYear() != date.getDayOfYear() || currentDate.getMonth() != date.getMonth() || 
                currentDate.getYear() != date.getYear();
    }
    
    // @@author A0139922Y
    /**
     * To convert LocalDateTime to 00:00 or 23:59 if not specified
     * @param actualDate 
     *                  is the date that that is require for checking
     * @param checkedDate
     *                  is the date to be used for checking
     * @isDateFrom
     *                  if true, actualDate is dateFrom, false if actualDate is dateTo                 
     * 
     * @return the correct date format
     */
    public static LocalDateTime parseTimeStamp(LocalDateTime actualDate, LocalDateTime checkedDate, boolean isDateFrom) {
        //check for date
        if (checkedDate != null && actualDate != null && checkIfDateExist(checkedDate) && !checkIfDateExist(actualDate)) {
            if (!isDateFrom) {
                actualDate = checkedDate.toLocalDate().atTime(actualDate.getHour(), actualDate.getMinute());
            }
        }
        //check for time
        if (checkedDate != null && actualDate != null && checkIfTimeExist(checkedDate) && !checkIfTimeExist(actualDate)) {
            actualDate = actualDate.toLocalDate().atTime(checkedDate.getHour(), checkedDate.getMinute());            
        }
        if (actualDate != null && !checkIfTimeExist(actualDate)) {
            if (isDateFrom) {
                actualDate = floorDate(actualDate);
            } else {
                actualDate = ceilDate(actualDate);
            }
        }
        return actualDate;
    }

}
