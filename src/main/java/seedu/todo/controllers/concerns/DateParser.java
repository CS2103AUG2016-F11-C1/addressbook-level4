package seedu.todo.controllers.concerns;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import seedu.todo.commons.exceptions.InvalidNaturalDateException;

/**
 * @@author A0093907W
 * 
 * Class to store date parsing methods to be shared across controllers.
 *
 */
public class DateParser {
    private static final int TOKEN_INDEX = 0;
    private static final int TOKEN_RESULT_INDEX = 1;
    
    /**
     * Extracts the natural dates from parsedResult.
     * 
     * @param parsedResult
     * @return { naturalFrom, naturalTo }
     */
    public static String[] extractDatePair(Map<String, String[]> parsedResult) {
        String naturalFrom = null;
        String naturalTo = null;
        setTime: {
            if (parsedResult.get("time") != null && parsedResult.get("time")[1] != null) {
                naturalFrom = parsedResult.get("time")[1];
                break setTime;
            }
            if (parsedResult.get("timeFrom") != null && parsedResult.get("timeFrom")[1] != null) {
                naturalFrom = parsedResult.get("timeFrom")[1];
            }
            if (parsedResult.get("timeTo") != null && parsedResult.get("timeTo")[1] != null) {
                naturalTo = parsedResult.get("timeTo")[1];
            }
        }
        return new String[] { naturalFrom, naturalTo };
    }
    
    /**
     * Parse a natural date into a LocalDateTime object.
     * 
     * @param natural
     * @return LocalDateTime object
     * @throws InvalidNaturalDateException 
     */
    public static LocalDateTime parseNatural(String natural) throws InvalidNaturalDateException {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(natural);
        Date date = null;
        try {
            date = groups.get(0).getDates().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidNaturalDateException(natural);
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return ldt;
    }
    
    /*
     * @@author A0139922Y
     * To be used to check if there exist an item tagged with the token
     * 
     */
    public static boolean isTokenNull(Map<String, String[]> parsedResult, String token) {
        return parsedResult.get(token) == null || parsedResult.get(token)[TOKEN_INDEX] == null;
    }
    
    /*
     * @@author A0139922Y
     * To check if parsedResult with the token containing the keyword provided 
     * 
     * @return true if keyword is found, false if it is not found
     */
    public static boolean doesTokenContainKeyword(Map<String, String[]> parsedResult, String token, String keyword) {
        if (!isTokenNull(parsedResult, token)) {
            return parsedResult.get(token)[TOKEN_INDEX].contains(keyword);
        }
        return false;
    }
    
    /*
     * @@author A0139922Y
     * To be used to get input from token
     * 
     * @return the parsed result from tokenizer
     */
    public static String getTokenResult(Map<String, String[]> parsedResult, String token) {
        if(!isTokenNull(parsedResult, token)) {
            return parsedResult.get(token)[TOKEN_RESULT_INDEX];
        }
        return null;
    }
    
    /**
     * @@author A0139922Y
     * Extracts the natural dates from parsedResult.
     * 
     * @param parsedResult
     * @return { numOfdateFound, naturalOn, naturalFrom, naturalTo } 
     */
    public static String[] parseDates(Map<String, String[]> parsedResult) {
        String naturalFrom = getTokenResult(parsedResult, "timeFrom");
        String naturalTo = getTokenResult(parsedResult, "timeTo");
        String naturalOn = getTokenResult(parsedResult, "time");
        int numOfDateFound = 0;
        
        String [] dateResult = { null, naturalOn, naturalFrom, naturalTo };
        for (int i = 0; i < dateResult.length; i ++) {
            if (dateResult[i] != null) {
                numOfDateFound ++;
            }
        }
        
        if (numOfDateFound == 0) {
            return null;
        } else {
            dateResult[0] = Integer.toString(numOfDateFound);
        }

        return dateResult;
    }
}
