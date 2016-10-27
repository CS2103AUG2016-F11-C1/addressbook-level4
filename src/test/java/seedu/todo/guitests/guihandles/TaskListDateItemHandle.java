package seedu.todo.guitests.guihandles;

import java.time.LocalDate;
import java.util.Optional;

import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.guitests.GuiRobot;

public class TaskListDateItemHandle extends GuiHandle {

    private static final String TASKLISTDATEITEM_DATE_ID = "#dateLabel";
    private static final String TASKLISTTASKITEM_PANEL = "#dateCalendarItemsPlaceholder";
    private Node node;

    public TaskListDateItemHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }
    
    /**
     * Gets the LocalDate parsed from the date label, which is in the date format {@code ddd dd MMM} (e.g. Thu 27 Oct).
     * If the date item is for floating tasks, it will return {@code NO_DATE_VALUE}.
     * 
     * @return Parsed LocalDate.
     */
    public LocalDate getDate() {
        String shortDateString = getStringFromText(TASKLISTDATEITEM_DATE_ID, node);
        LocalDate parsed = DateUtil.parseShortDate(shortDateString);
        
        if (parsed == null) {
            return DateUtil.NO_DATE_VALUE;
        } else {
            return parsed;
        }
    }
    
    /**
     * Returns a TaskListDateItemHandle that corresponds to the name specified.
     * If it doesn't exist, it returns null.
     */
    public TaskListTaskItemHandle getTaskListTaskItem(String taskName) {
        Optional<Node> taskItemNode = guiRobot.lookup(TASKLISTTASKITEM_PANEL).queryAll().stream()
                .filter(node -> new TaskListTaskItemHandle(guiRobot, primaryStage, node).getName().equals(taskName))
                .findFirst();
        
        if (taskItemNode.isPresent()) {
            return new TaskListTaskItemHandle(guiRobot, primaryStage, taskItemNode.get());
        } else {
            return null;
        }
    }

}
