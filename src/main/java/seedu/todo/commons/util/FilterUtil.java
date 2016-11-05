package seedu.todo.commons.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import seedu.todo.models.CalendarItem;
import seedu.todo.models.Event;
import seedu.todo.models.Task;

/**
 * Helper function to help in filtering results
 * 
 * @@author A0139922Y
 *
 */

public class FilterUtil {
    
    /*==================== Filtering Methods for Tasks ======================*/
    
    /**
     * Filter the task list based on matching task name list
     * @param tasks 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     */
    public static List<Task> filterTaskByNames(List<Task> tasks, HashSet<String> nameList) {
        //if search list size is 0 , user not searching based on name
        List<Task> filteredTasks = new ArrayList<Task>();
        if (nameList.size() == 0) {
            return filteredTasks;
        }
        
        for (int i = 0; i < tasks.size(); i ++) {
            Task task = tasks.get(i);
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String matchingName = nameListIterator.next();
                if (matchWithFullName(task, matchingName) || matchWithSubName(task, matchingName)) {
                    filteredTasks.add(task);
                    break;
                }
            }
            nameListIterator = nameList.iterator();
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on matching tag name list
     * @param tasks 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     */
    public static List<Task> filterTaskByTags(List<Task> tasks, HashSet<String> nameList) {
        //if no search list is provided, user not searching based on tags
        List<Task> filteredTasks = new ArrayList<Task>();
        if (nameList.size() == 0) {
            return filteredTasks;
        }
        
        for (int i = 0; i < tasks.size(); i ++) {
            Task task = tasks.get(i);
            ArrayList<String> taskTagList = task.getTagList();
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String currentMatchingName = nameListIterator.next();
                if (taskTagList.contains(currentMatchingName)) {
                    filteredTasks.add(task);
                    break;
                }
            }
            nameListIterator = nameList.iterator();
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on incomplete status
     * @param tasks 
     *             Provided list for filtering
     * @param taskStatus
     *             True if searching for is completed, false if search for not completed!            
     */
    public static List<Task> filterTasksByStatus(List<Task> tasks, boolean taskStatus) {
        if (tasks.size() == 0) {
            return tasks;
        }
        
        List<Task> filteredTasks = new ArrayList<Task>();
        for (int i = 0; i < tasks.size(); i ++) {
            Task task = tasks.get(i);
            if (task.isCompleted() == taskStatus) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list based on single date
     * @param tasks 
     *             Provided list for filtering
     * @param date            
     *             Search based on this date
     */
    public static List<Task> filterTaskBySingleDate(List<Task> tasks, LocalDateTime date) {
        if (tasks.size() == 0) {
            return tasks;
        }
        
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            assert date != null;
            date = DateUtil.floorDate(date);
            LocalDateTime taskDate = DateUtil.floorDate(task.getCalendarDateTime());
            
            //May have floating tasks
            if (taskDate != null && taskDate.equals(date)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    /**
     * Filter the task list with a date range
     * @param tasks 
     *             Provided list for filtering
     * @param startDate            
     *             Search based on this as starting date
     * @param endDate            
     *             Search based on this as ending date
     */
    public static List<Task> filterTaskWithDateRange(List<Task> tasks, LocalDateTime startDate, LocalDateTime endDate) {
        if (tasks.size() == 0) {
            return tasks;
        }
    
        if (startDate == null) {
            startDate = LocalDateTime.MIN;
        }
        
        if (endDate == null) {
            endDate = LocalDateTime.MAX;
        }
        
        ArrayList<Task> filteredTasks = new ArrayList<Task>();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            LocalDateTime taskDate = DateUtil.floorDate(task.getDueDate());
            if (taskDate == null) {
                taskDate = DateUtil.floorDate(LocalDateTime.MIN);
            }
            
            startDate = DateUtil.floorDate(startDate);
            endDate = DateUtil.ceilDate(endDate);
            if (taskDate.compareTo(startDate) >= 0 && taskDate.compareTo(endDate) <= 0) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    /*==================== Filtering Methods for Events ======================*/
    
    /**
     * Filter the event list based on event name list
     * @param events 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     */
    public static List<Event> filterEventByNames(List<Event> events, HashSet<String> nameList) {
        List<Event> filteredEvents = new ArrayList<Event>();
        //if name list size is 0, means not searching by tags 
        if (nameList.size() == 0) {
            return filteredEvents;
        }
        
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String matchingName = nameListIterator.next().toLowerCase();
                if (matchWithFullName(event, matchingName) || matchWithSubName(event, matchingName)) {
                    filteredEvents.add(event);
                    break;
                }
            }
            nameListIterator = nameList.iterator();
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list based on tag name list
     * @param events 
     *             Provided list for filtering
     * @param namelist
     *             Search and filter based on the name list
     */
    public static List<Event> filterEventByTags(List<Event> events, HashSet<String> nameList) {
        List<Event> filteredEvents = new ArrayList<Event>();
        //if name list size is 0, means not searching by tags 
        if (nameList.size() == 0) {
            return filteredEvents;
        }
        
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            ArrayList<String> taskTagList = event.getTagList();
            Iterator<String> nameListIterator = nameList.iterator();
            while (nameListIterator.hasNext()) {
                String currentMatchingName = nameListIterator.next();
                if (taskTagList.contains(currentMatchingName)) {
                    filteredEvents.add(event);
                    break;
                }
            }
            nameListIterator = nameList.iterator();
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list if the event date is over
     * @param events 
     *             Provided list for filtering
     * @param isEventOver
     *             True if searching for event that are over, false if searching for current event            
     */            
    public static List<Event> filterEventsByStatus(List<Event> events, boolean eventStatus) {
        if (events.size() == 0) {
            return events;
        }
        
        List<Event> filteredEvents = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i ++) {
            Event event = events.get(i);
            if (event.isOver() == eventStatus) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list based on single date
     * @param events 
     *             Provided list for filtering
     * @param date            
     *             Search based on this date         
     */
    public static List<Event> filterEventBySingleDate(List<Event> events, LocalDateTime date) {
        if (events.size() == 0) {
            return events;
        }
        
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            date = DateUtil.floorDate(date);
            LocalDateTime eventDate = DateUtil.floorDate(event.getStartDate());
            if (eventDate != null && eventDate.equals(date)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /**
     * Filter the event list with range of dates
     * @param events 
     *             Provided list for filtering
     * @param startDate            
     *             Search based on this as starting date
     * @param endDate            
     *             Search based on this as ending date
     */
    public static List<Event> filterEventWithDateRange(List<Event> events, LocalDateTime startDate, LocalDateTime endDate) {
        if (events.size() == 0) {
            return events;
        }
    
        if (startDate == null) {
            startDate = LocalDateTime.MIN;
        }
        
        if (endDate == null) {
            endDate = LocalDateTime.MAX;
        }
        
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            LocalDateTime eventStartDate = DateUtil.floorDate(event.getStartDate());
            LocalDateTime eventEndDate = DateUtil.floorDate(event.getEndDate());
            
            if (eventStartDate == null) {
                eventStartDate = DateUtil.floorDate(LocalDateTime.MIN);
            }
            if (eventEndDate == null) {
                eventEndDate = DateUtil.floorDate(LocalDateTime.MAX);
            }
            
            startDate = DateUtil.floorDate(startDate);
            endDate = DateUtil.ceilDate(endDate);
            if (eventStartDate.compareTo(startDate) >= 0 && eventEndDate.compareTo(endDate) <= 0) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
    
    /*==================== Helper Methods for filtering CalendarItem name ======================*/
    
    /*
     * Use to check if calendarItem name starts with the matching name 
     * 
     * @return true if it calendarItem's name starts with the matching name, 
     * false if calendarItem's name does not starts with matching name
     */
    private static boolean matchWithFullName(CalendarItem calendarItem, String matchingName) {
        String taskName = calendarItem.getName().toLowerCase();
        return taskName.startsWith(matchingName.toLowerCase());
    }
    
    /*
     * Use to check if calendarItem name split by space starts with the matching name 
     * 
     * @return true if any of the calendarItem's name that is split by space that starts with the matching name, 
     * false if calendarItem's name that is split by space does not starts with matching name
     */
    private static boolean matchWithSubName(CalendarItem calendarItem, String matchingName) {
        String[] nameBySpace = StringUtil.splitStringBySpace(calendarItem.getName());
        for (int i = 0; i < nameBySpace.length; i ++) {
            if (nameBySpace[i].toLowerCase().startsWith(matchingName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
