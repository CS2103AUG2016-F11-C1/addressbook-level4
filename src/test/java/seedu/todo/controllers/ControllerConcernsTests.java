package seedu.todo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;

import seedu.todo.commons.exceptions.UnmatchedQuotesException;
import seedu.todo.controllers.concerns.Tokenizer;

/**
 * @@author A0093907W
 */
public class ControllerConcernsTests {
    
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put("tokenType1", new String[] {"token11"});
        tokenDefinitions.put("tokenType2", new String[] {"token21", "token22"});
        tokenDefinitions.put("tokenType3", new String[] {"token31", "token32", "token33"});
        return tokenDefinitions;
    }
    
    @Test
    public void tokenizer_noMatch_notFound() throws Exception {
        String input = "abcdefg hijklmnop";
        Map<String, String[]> output = Tokenizer.tokenize(getTokenDefinitions(), input);
        assertTrue(output.isEmpty());
    }
    
    @Test
    public void tokenizer_emptyString_notFound() throws Exception {
        String input = "";
        Map<String, String[]> output = Tokenizer.tokenize(getTokenDefinitions(), input);
        assertTrue(output == null);
    }
    
    @Test
    public void tokenizer_singleMatch_found() throws Exception {
        String input = "token11 answer";
        Map<String, String[]> output = Tokenizer.tokenize(getTokenDefinitions(), input);
        assertEquals(output.get("tokenType1")[0], "token11");
        assertEquals(output.get("tokenType1")[1], "answer");
    }
    
    @Test
    public void tokenizer_emptyMatch_found() throws Exception {
        String input = "alamak token11 token21";
        Map<String, String[]> output = Tokenizer.tokenize(getTokenDefinitions(), input);
        assertEquals(output.get("tokenType1")[0], "token11");
        assertEquals(output.get("tokenType1")[1], null);
        assertEquals(output.get("tokenType2")[0], "token21");
        assertEquals(output.get("tokenType2")[1], null);
    }
    
    @Test
    public void tokenizer_matchQuotes_found() throws Exception {
        String input = "\"token11\" answer";
        Map<String, String[]> output = Tokenizer.tokenize(getTokenDefinitions(), input);
        assertTrue(output.isEmpty());
    }
    
    @Test(expected=UnmatchedQuotesException.class)
    public void tokenizer_unmatchedQuotes_error() throws Exception {
        String input = "\"\"\"";
        Tokenizer.tokenize(getTokenDefinitions(), input);
    }
    
}
