package seedu.todo.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.todo.commons.util.StringUtil;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isUnsignedPositiveInteger() {
        assertFalse(StringUtil.isUnsignedInteger(null));
        assertFalse(StringUtil.isUnsignedInteger(""));
        assertFalse(StringUtil.isUnsignedInteger("a"));
        assertFalse(StringUtil.isUnsignedInteger("aaa"));
        assertFalse(StringUtil.isUnsignedInteger("  "));
        assertFalse(StringUtil.isUnsignedInteger("-1"));
        assertFalse(StringUtil.isUnsignedInteger("0"));
        assertFalse(StringUtil.isUnsignedInteger("+1")); //should be unsigned
        assertFalse(StringUtil.isUnsignedInteger("-1")); //should be unsigned
        assertFalse(StringUtil.isUnsignedInteger(" 10")); //should not contain whitespaces
        assertFalse(StringUtil.isUnsignedInteger("10 ")); //should not contain whitespaces
        assertFalse(StringUtil.isUnsignedInteger("1 0")); //should not contain whitespaces

        assertTrue(StringUtil.isUnsignedInteger("1"));
        assertTrue(StringUtil.isUnsignedInteger("10"));
    }

    @Test
    public void getDetails_exceptionGiven(){
        assertThat(StringUtil.getDetails(new FileNotFoundException("file not found")),
                   containsString("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_assertionError(){
        thrown.expect(AssertionError.class);
        StringUtil.getDetails(null);
    }

    @Test
    public void test_pluralizer_singular() {
        String singular = "apple";
        String plural = "apples";
        assertEquals(StringUtil.pluralizer(1, singular, plural), singular);
    }
    
    @Test
    public void test_pluralizer_plural() {
        String singular = "apple";
        String plural = "apples";
        assertEquals(StringUtil.pluralizer(0, singular, plural), plural);
        assertEquals(StringUtil.pluralizer(2, singular, plural), plural);
    }
    
    @Test
    public void test_replacenull_no_replace() {
        String first = "firstString";
        String last = "lastString";
        assertEquals(StringUtil.replaceEmpty(first, last), first);
    }
    
    @Test
    public void test_replacenull_replace() {
        String first = null;
        String last = "lastString";
        assertEquals(StringUtil.replaceEmpty(first, last), last);
    }
    
    //@@author A0139922Y
    @Test
    public void testSplitStringBySpace_null_test() {
        assertNull(StringUtil.splitStringBySpace(null));
    }

    //@@author A0139922Y
    @Test 
    public void testSplitStringBySpace_equals() {
        String testcase1 = "TEST";
        String testcase2 = "TEST TEST";
        assertNotNull(StringUtil.splitStringBySpace(testcase1));
        assertArrayEquals(testcase1.split(" "), StringUtil.splitStringBySpace(testcase1));
        assertNotNull(StringUtil.splitStringBySpace(testcase2));
        assertArrayEquals(testcase2.split(" "),StringUtil.splitStringBySpace(testcase2));
    }
    
    //@@author A0139922Y    
    @Test
    public void testFormatNumberOfTaskWithPuralizer_equals() {
        int single = 1;
        assertEquals(String.format("%d task", single), StringUtil.formatNumberOfTaskWithPuralizer(single));
        int pural = 2;
        assertEquals(String.format("%d tasks", pural), StringUtil.formatNumberOfTaskWithPuralizer(pural));
    }
    
    //@@author A0139922Y    
    @Test
    public void testFormatNumberOfTaskWithPuralizer_not_equals() {
        int single = 1;
        assertNotEquals(String.format("%d tasks", single), StringUtil.formatNumberOfTaskWithPuralizer(single));
        int pural = 2;
        assertNotEquals(String.format("%d task", pural), StringUtil.formatNumberOfTaskWithPuralizer(pural));
    }

    //@@author A0139922Y
    @Test
    public void testFormatNumberOfEventWithPuralizer_equals() {
        int single = 1;
        assertEquals(String.format("%d event", single), StringUtil.formatNumberOfEventWithPuralizer(single));
        int pural = 2;
        assertEquals(String.format("%d events", pural), StringUtil.formatNumberOfEventWithPuralizer(pural));
    }
    
    //@@author A0139922Y    
    @Test
    public void testFormatNumberOfEventWithPuralizer_not_equals() {
        int single = 1;
        assertNotEquals(String.format("%d events", single), StringUtil.formatNumberOfEventWithPuralizer(single));
        int pural = 2;
        assertNotEquals(String.format("%d event", pural), StringUtil.formatNumberOfEventWithPuralizer(pural));
    }
    
    //@@author A0139922Y    
    @Test
    public void testDisplayNumberOfTaskAndEventFoundWithPuralizer_equals() {
        int numTasks = 0;
        int numEvents = 0;
        assertEquals("No item found!", StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(numTasks, numEvents));
        numTasks = 1;
        assertEquals("1 task", StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(numTasks, numEvents));
        numEvents = 1;
        assertEquals("1 task and 1 event", StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(numTasks, numEvents));
        numTasks = 0;
        assertEquals("1 event", StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(numTasks, numEvents));
    }
}
