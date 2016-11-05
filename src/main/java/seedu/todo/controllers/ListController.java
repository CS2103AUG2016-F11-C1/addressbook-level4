package seedu.todo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.commons.util.FilterUtil;
import seedu.todo.commons.util.ParseUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.Event;
import seedu.todo.models.Task;
import seedu.todo.models.TodoListDB;

/**
 * Controller to list CalendarItems.
 * 
 * @@author A0139922Y
 *
 */
public class ListController implements Controller {
    
    private static final String NAME = "List";
    private static final String DESCRIPTION = "List all tasks and events by type or status.";
    private static final String COMMAND_SYNTAX = "list [task complete/incomplete or event] [event over/ongoing] "
            + "[on date] or [from date to date]";
    private static final String COMMAND_WORD = "list";
    private static final String LIST_TASK_SYNTAX = "list task [complete/incomplete]";
    private static final String LIST_EVENT_SYNTAX = "list event [over/ongoing]";
    private static final String LIST_DATE_SYNTAX = "list [date] or [from <date> to <date>]";
    
    private static final String MESSAGE_RESULT_FOUND = "A total of %s found!";
    private static final String MESSAGE_NO_RESULT_FOUND = "No task or event found!";
    private static final String MESSAGE_LIST_SUCCESS = "Listing Today's, incompleted tasks and ongoing events";
    private static final String MESSAGE_INVALID_TASK_STATUS = "Unable to list!\nTry listing with [complete] or [incomplete]";
    private static final String MESSAGE_INVALID_EVENT_STATUS = "Unable to list!\nTry listing with [over] or [current]";
    private static final String MESSAGE_DATE_CONFLICT = "Unable to find!\nMore than 1 date criteria is provided!";
    private static final String MESSAGE_NO_DATE_DETECTED = "Unable to find!\nThe natural date entered is not supported.";
    
    //use to access parsing of dates
    private static final int NUM_OF_DATES_FOUND_INDEX = 0;
    private static final int COMMAND_INPUT_INDEX = 0;
    private static final int DATE_ON_INDEX = 1;
    private static final int DATE_FROM_INDEX = 2;
    private static final int DATE_TO_INDEX = 3;
    
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        return (StringUtil.splitStringBySpace(input.toLowerCase())[COMMAND_INPUT_INDEX]).equals(COMMAND_WORD) ? 1 : 0;
    }
    
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put("default", new String[] {"list"});
        tokenDefinitions.put("eventType", new String[] { "event", "events", "task", "tasks"});
        tokenDefinitions.put("taskStatus", new String[] { "complete" , "completed", "incomplete", "incompleted"});
        tokenDefinitions.put("eventStatus", new String[] { "over" , "ongoing", "current", "schedule" , "scheduled"});
        tokenDefinitions.put("time", new String[] { "at", "by", "on", "time", "date" });
        tokenDefinitions.put("timeFrom", new String[] { "from" });
        tokenDefinitions.put("timeTo", new String[] { "to", "before", "until" });
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        
        TodoListDB db = TodoListDB.getInstance();
        
        if (input.trim().equals(COMMAND_WORD)) {
            Renderer.renderIndex(db, MESSAGE_LIST_SUCCESS);
            return;
        }
        
        boolean isItemTypeProvided = !ParseUtil.isTokenNull(parsedResult, "eventType");
        boolean isTaskStatusProvided = !ParseUtil.isTokenNull(parsedResult, "taskStatus");
        boolean isEventStatusProvided = !ParseUtil.isTokenNull(parsedResult, "eventStatus");
        
        boolean isTask = true; //default
        if (isItemTypeProvided) {
            isTask = ParseUtil.doesTokenContainKeyword(parsedResult, "eventType", "task");
            
            if (isTask && isEventStatusProvided) {
                Renderer.renderDisambiguation(LIST_TASK_SYNTAX, MESSAGE_INVALID_TASK_STATUS);
                return;
            }
            
            if (!isTask && isTaskStatusProvided) {
                Renderer.renderDisambiguation(LIST_EVENT_SYNTAX, MESSAGE_INVALID_EVENT_STATUS);
                return;
            }
        }
        
        boolean isCompleted = false; //default 
        boolean isOver = false; //default
        if (isTaskStatusProvided) {
            isCompleted = !ParseUtil.doesTokenContainKeyword(parsedResult, "taskStatus", "incomplete");
        }
        if (isEventStatusProvided) {
            isOver = ParseUtil.doesTokenContainKeyword(parsedResult, "eventStatus", "over");
        }
        
        String[] parsedDates = ParseUtil.parseDates(parsedResult);
        
        //date enter with COMMAND_WORD e.g list today
        String date = ParseUtil.getTokenResult(parsedResult, "default");
        if (date != null && parsedDates != null) {
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
            return;
        }
        

        LocalDateTime dateCriteria = null;
        LocalDateTime dateOn = null;
        LocalDateTime dateFrom = null;
        LocalDateTime dateTo = null;
        
        if (date != null) {
            dateCriteria = DateUtil.floorDate(DateUtil.parseNatural(date));
            if (dateCriteria == null) {
                Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
                return ;
            } 
        }
        
        if (parsedDates != null) {
            String naturalOn = parsedDates[DATE_ON_INDEX];
            String naturalFrom = parsedDates[DATE_FROM_INDEX];
            String naturalTo = parsedDates[DATE_TO_INDEX];
            
            if (naturalOn != null && Integer.parseInt(parsedDates[NUM_OF_DATES_FOUND_INDEX]) > 1) {
                //date conflict detected
                Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
                return;
            }
    
            // Parse natural date using Natty.
            dateOn = naturalOn == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalOn)); 
            dateFrom = naturalFrom == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalFrom)); 
            dateTo = naturalTo == null ? null : DateUtil.floorDate(DateUtil.parseNatural(naturalTo));
        }
        
        if (parsedDates != null && dateOn == null && dateFrom == null && dateTo == null) {
            //Natty failed to parse date
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_NO_DATE_DETECTED);
            return ;
        }
        
        if (parsedDates != null && isEventStatusProvided) {
            //detect date conflict
            Renderer.renderDisambiguation(LIST_DATE_SYNTAX, MESSAGE_DATE_CONFLICT);
            return;
        }
        
        //Setting up views
        filterTasksAndEvents(db, isItemTypeProvided, isTaskStatusProvided, isEventStatusProvided, isTask, isCompleted,
                isOver, dateCriteria, dateOn, dateFrom, dateTo);
    }

    /*
     * Filter out the selected tasks and events based on the search criteria
     * 
     */
    private void filterTasksAndEvents(TodoListDB db, boolean isItemTypeProvided, boolean isTaskStatusProvided,
            boolean isEventStatusProvided, boolean isTask, boolean isCompleted, boolean isOver,
            LocalDateTime dateCriteria, LocalDateTime dateOn, LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Task> tasks = db.getAllTasks(); 
        List<Event> events = db.getAllEvents();; 
        if (isItemTypeProvided) {
            if (isTask) {
                events = new ArrayList<Event>();
            } else if (!isTask) {
                tasks = new ArrayList<Task>();
            }
        } 
        
        if (isTaskStatusProvided) {
            tasks = FilterUtil.filterTasksByStatus(tasks, isCompleted);
            events = new ArrayList<Event>();
        }
        
        if (isEventStatusProvided) {
            events = FilterUtil.filterEventsByStatus(events, isOver);
            tasks = new ArrayList<Task>();
        }
        
        if (dateCriteria != null) {
            tasks = FilterUtil.filterTaskBySingleDate(tasks, dateCriteria);
            events = FilterUtil.filterEventBySingleDate(events, dateCriteria);
        }
        
        if (dateOn != null) {
            //filter by single date
            tasks = FilterUtil.filterTaskBySingleDate(tasks, dateOn);
            events = FilterUtil.filterEventBySingleDate(events, dateOn);
        } else {
            //filter by range
            tasks = FilterUtil.filterTaskWithDateRange(tasks, dateFrom, dateTo);
            events = FilterUtil.filterEventWithDateRange(events, dateFrom, dateTo);
        }
        
        if (tasks.size() == 0 && events.size() == 0) {
            Renderer.renderIndex(db, MESSAGE_NO_RESULT_FOUND);
            return;
        }
        
        String consoleMessage = String.format(MESSAGE_RESULT_FOUND, 
                StringUtil.displayNumberOfTaskAndEventFoundWithPuralizer(tasks.size(), events.size()));
        Renderer.renderSelectedIndex(db, consoleMessage, tasks, events);
    }
}
