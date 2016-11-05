package seedu.todo.controllers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import seedu.todo.controllers.concerns.DateParser;

//@@author A0139922Y
public class DateParserTest {
    
    Map<String, String[]> parsedResult = new HashMap<String, String[]>();
    public static final String EMPTY_TOKEN = "";
    public static final String [] NULL_TOKEN_RESULT = new String [] { null }; 
    public static final String [] TOKEN_RESULT = new String [] {"test", "test1, test2"};
    
    public static final String TEST_TOKEN = "test";
    public static final String TOKEN_KEYWORD_DOES_NOT_EXIST = "random";
    
    public static final int TOKEN_RESULT_INDEX_ONE = 0;
    public static final int TOKEN_RESULT_INDEX_TWO = 1;
    
    public static final String CORRECT_NATURAL_DATE = "today";
    public static final String INCORRECT_NATURAL_DATE = "todar";
    
    String[] test_result = { "3", "today" , "tuesday" , "wednesday" };
    String[] incorrect_test_result = { "2" , "today", "tuesday", null };
    String date_on_format = "time";
    String date_from_format = "timeFrom";
    String date_to_format = "timeTo";
    
    String [] date_on_result = { "time", "today" };
    String [] date_from_result = { "timeFrom" , "tuesday" };
    String [] date_to_result = { "timeTo" , "wednesday" };
    String [] incorrect_date_result = { "" , null };
    
    @Test
    public void testIsTokenNull_true() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertTrue(DateParser.isTokenNull(parsedResult, EMPTY_TOKEN));
        
        parsedResult.put(TEST_TOKEN, NULL_TOKEN_RESULT);
        assertTrue(DateParser.isTokenNull(parsedResult, TEST_TOKEN));
    }
    
    @Test
    public void testIsTokenNull_false() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertFalse(DateParser.isTokenNull(parsedResult, TEST_TOKEN));
    }

    @Test
    public void testDoesTokenContainKeyword_true() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertTrue(DateParser.doesTokenContainKeyword(parsedResult, TEST_TOKEN, TOKEN_RESULT[TOKEN_RESULT_INDEX_ONE]));
    }
    
    @Test
    public void testDoesTokenContainKeyword_false() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertFalse(DateParser.doesTokenContainKeyword(parsedResult, TEST_TOKEN, TOKEN_KEYWORD_DOES_NOT_EXIST));
        assertFalse(DateParser.doesTokenContainKeyword(parsedResult, EMPTY_TOKEN, TOKEN_RESULT[TOKEN_RESULT_INDEX_ONE]));
        assertFalse(DateParser.doesTokenContainKeyword(parsedResult, EMPTY_TOKEN, NULL_TOKEN_RESULT[TOKEN_RESULT_INDEX_ONE]));
    }

    @Test
    public void testGetTokenResult_null_test() {
        assertNotNull(DateParser.getTokenResult(parsedResult, TEST_TOKEN), TOKEN_RESULT[TOKEN_RESULT_INDEX_TWO]);
        assertNull(DateParser.getTokenResult(parsedResult, EMPTY_TOKEN));
    }
    
    @Test
    public void testGetTokenResult() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertEquals(DateParser.getTokenResult(parsedResult, TEST_TOKEN), TOKEN_RESULT[TOKEN_RESULT_INDEX_TWO]);
    }
    
    @Test
    public void testGetTokenResult_not_equals() {
        parsedResult.put(TEST_TOKEN, TOKEN_RESULT);
        assertNotEquals(DateParser.getTokenResult(parsedResult, TEST_TOKEN), TOKEN_RESULT[TOKEN_RESULT_INDEX_ONE]);
    }

    @Test
    public void testParseDates_atLeastOneDate_not_null() {
        parsedResult.put(date_on_format, date_on_result);
        parsedResult.put(date_from_format, incorrect_date_result);
        parsedResult.put(date_to_format, incorrect_date_result);
        assertNotNull(DateParser.parseDates(parsedResult));
        
        parsedResult.put(date_on_format, incorrect_date_result);
        parsedResult.put(date_to_format, date_to_result);
        assertNotNull(DateParser.parseDates(parsedResult));
    }
    
    @Test
    public void testParseDates_null() {
        parsedResult.put(date_on_format, incorrect_date_result);
        parsedResult.put(date_from_format, incorrect_date_result);
        parsedResult.put(date_to_format, incorrect_date_result);
        assertNull(DateParser.parseDates(parsedResult)); 
    }
    
    @Test
    public void testParseDates_parsedSuccesfully_equals() {
        parsedResult.put(date_on_format, date_on_result);
        parsedResult.put(date_from_format, date_from_result);
        parsedResult.put(date_to_format, date_to_result);
        assertArrayEquals(test_result, DateParser.parseDates(parsedResult));
    }
    
    @Test
    public void testParseDates_parsedSuccesfully_not_equals() {
        parsedResult.put(date_on_format, date_on_result);
        parsedResult.put(date_from_format, date_from_result);
        parsedResult.put(date_to_format, date_to_result);
        assertNotEquals(incorrect_test_result, DateParser.parseDates(parsedResult));
    }
}
