package seedu.todo.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Helper functions for parsing.
 * 
 * @@author Tiong YaoCong A0139922Y
 */
public class ParseUtil {
    private static final int TOKEN_INDEX = 0;
    private static final int TOKEN_RESULT_INDEX = 1;
    
    /*
     * To be used to check if there exist an item tagged with the token
     * 
     * */
    public static boolean isTokenNull(Map<String, String[]> parsedResult, String token) {
        return parsedResult.get(token) == null || parsedResult.get(token)[TOKEN_INDEX] == null;
    }
    
    public static boolean doesTokenContainKeyword(Map<String, String[]> parsedResult, String token, String keyword) {
        if (!isTokenNull(parsedResult, token)) {
            return parsedResult.get(token)[TOKEN_INDEX].contains(keyword);
        }
        return false;
    }
    
    /*
     * To be used to get input from token
     * 
     * @return the parsed result from tokenizer
     * */
    public static String getTokenResult(Map<String, String[]> parsedResult, String token) {
        if(!isTokenNull(parsedResult, token)) {
            return parsedResult.get(token)[TOKEN_RESULT_INDEX];
        }
        return null;
    }
    
    /*
     * To be used to parse natural date into LocalDateTime 
     * @parser Natty
     * 
     * */
    public static LocalDateTime parseNatural(String natural) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(natural);
        Date date = null;
        try {
            date = groups.get(0).getDates().get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return DateUtil.floorDate(ldt);
    }
    
    /**
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
