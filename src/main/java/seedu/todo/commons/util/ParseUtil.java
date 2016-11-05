package seedu.todo.commons.util;

import java.util.Map;

/**
 * Helper functions for parsing.
 * 
 * @@author A0139922Y
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
    /*
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
