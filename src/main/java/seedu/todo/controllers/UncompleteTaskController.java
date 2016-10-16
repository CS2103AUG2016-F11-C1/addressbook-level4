package seedu.todo.controllers;

import seedu.todo.commons.EphemeralDB;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.IndexView;

public class UncompleteTaskController implements Controller {
    
    private static String NAME = "Uncomplete Task";
    private static String DESCRIPTION = "Marks a task as incomplete, by listed index";
    private static String COMMAND_SYNTAX = "uncomplete <index>";

    private static final String MESSAGE_SUCCESS = "Task marked as incomplete!";
    private static final String MESSAGE_INVALID_ITEM = "Could not mark task as incomplete: Invalid index provided!";
    private static final String MESSAGE_CANNOT_UNCOMPLETE_EVENT = "An event cannot be marked as incomplete!";
    private static final String MESSAGE_ALREADY_INCOMPLETE = "Could not mark task as incomplete: Task is not completed!";
    private static final String MESSAGE_COULD_NOT_SAVE = "Could not mark task as incomplete: An error occured while saving the database file.";
    
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX);

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        // TODO
        return input.startsWith("uncomplete") ? 1 : 0;
    }

    @Override
    public void process(String args) {
        // TODO: Example of last minute work
        
        // Get index.
        int index = Integer.decode(args.replaceFirst("uncomplete", "").trim());
        
        // Get record
        EphemeralDB edb = EphemeralDB.getInstance();
        CalendarItem calendarItem = edb.getCalendarItemsByDisplayedId(index);
        TodoListDB db = TodoListDB.getInstance();
        
        if (calendarItem == null) {
            renderAndOutput(db, MESSAGE_INVALID_ITEM);
            return;
        }
        
        if (!(calendarItem instanceof Task)) {
            renderAndOutput(db, MESSAGE_CANNOT_UNCOMPLETE_EVENT);
            return;
        }
        
        Task task = (Task) calendarItem;
        
        if (!task.isCompleted()) {
            renderAndOutput(db, MESSAGE_ALREADY_INCOMPLETE);
            return;
        }
        
        // Set task as completed
        task.setIncomplete();
        boolean hadSaved = db.save();
        
        if (!hadSaved) {
            task.setCompleted();
            renderAndOutput(db, MESSAGE_COULD_NOT_SAVE);
            return;
        }
        
        // Show success message
        renderAndOutput(db, MESSAGE_SUCCESS);
    }
    
    private void renderAndOutput(TodoListDB db, String message) {
        // Re-render
        IndexView view = UiManager.loadView(IndexView.class);
        view.tasks = db.getAllTasks();
        view.events = db.getAllEvents();
        view.render();

        // Update console message
        UiManager.updateConsoleMessage(message);
    }

}
