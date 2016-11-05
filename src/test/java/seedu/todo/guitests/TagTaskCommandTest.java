package seedu.todo.guitests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import seedu.todo.guitests.guihandles.TagListItemHandle;
import seedu.todo.models.Task;

/**
 * @@author A0139922Y
 */
public class TagTaskCommandTest extends GuiTest {

    @Test
    public void tagTaskWithNewTagName() {
        console.runCommand("add 123");
        String command; 
        Task task = getInitialData().createTask();
        task.setName("testcase1");
        //adding 1 tag
        //task = new Task();
        //String[] testCase1 = {"CS3216"};
        //initializeTestCase(testCase1, task);
        command = "tag 1 CS3216";
        
        assertTaskTaggedSuccess(command, task);
        
        task.setName("testcase2");
        command = "tag 1 CS3216, CS3217";
        assertTaskTaggedSuccess(command, task);
        
        //adding multiple tags
//        task = new Task();
//        String[] testCase2 = {"CS3216" , "CS3217"};
//        tagList = initializeTestCase(testCase2, task);
//        command = "tag 1 CS3216, CS3217";
//        assertTaskTaggedSuccess(command , task, tagList);
    }
    
    /*
     * Utility method for testing if task has been successfully tagged.
     * This runs a command and checks if TagList contains the list of tags
     * 
     * */
    private void assertTaskTaggedSuccess(String command, Task taskToCheck) {
        // Run the command in the console.
        console.runCommand(command);
        ArrayList<String> taskTagList = taskToCheck.getTagList();
        for (int i = 0; i < taskTagList.size(); i ++) {
            String currentTag = taskTagList.get(i);
            TagListItemHandle tagItem = sidebar.getTagListItem(currentTag);
            assertTrue(currentTag.equals(tagItem.getName()));
        }
        

    }
    
    /**
     * @@author A0139922Y
     * Method to initialize test case
     * 
     */
    private ArrayList<String> initializeTestCase(String[] tagNameList, Task task) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < tagNameList.length; i ++) {
            String tagName = tagNameList[i];
            task.addTag(tagName);
            result.add(tagName);
        }
        return result;
    }

}
