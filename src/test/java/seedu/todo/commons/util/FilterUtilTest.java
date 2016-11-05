package seedu.todo.commons.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import seedu.todo.models.Event;
import seedu.todo.models.Task;

//@@author A0139922Y
public class FilterUtilTest {
    public static final LocalDateTime today = LocalDateTime.now();
    public static final LocalDateTime tmr = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime ytd = LocalDateTime.now().minusDays(1);
    Task firstTestTask = generateFirstTestTask();
    Task secondTestTask = generateSecondTestTask();
    List<Event> overdueEvents = generateOverdueEvents();
    List<Event> currentEvents = generateCurrentEvents();
    
    @Test
    public void testFilterTaskByNames_filter_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        //empty task list and name list
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        //empty task list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        
        //filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        nameList.remove("Buy");
        nameList.add("Milk");
        assertEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        tasks.add(secondTestTask);
        List<Task> filteredResult = getEmptyTaskList();
        filteredResult.add(firstTestTask);
        assertEquals(filteredResult, (FilterUtil.filterTaskByNames(tasks, nameList)));
        
        //empty task list with name list
        tasks.remove(firstTestTask);
        tasks.remove(secondTestTask);
        assertEquals(tasks, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByNames_filter_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        tasks.add(firstTestTask);
        nameList.add("Nothing");
        assertNotEquals(tasks, FilterUtil.filterTaskByNames(tasks, nameList));
        
        nameList.add("Buy");
        assertNotEquals(getEmptyTaskList(), FilterUtil.filterTaskByNames(tasks, nameList));
    }

    @Test
    public void testFilterTaskByTags_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();
        //empty task list and name list
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        //empty task list
        nameList.add("Nothing");
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        
        //filter out first test task
        nameList.add("personal");
        tasks.add(firstTestTask);
        assertEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        
        //filter out both test tasks
        tasks.add(secondTestTask);
        nameList.add("CS2103");
        assertEquals(tasks, (FilterUtil.filterTaskByTags(tasks, nameList)));
        
        //empty task list with name list
        tasks.remove(firstTestTask);
        tasks.remove(secondTestTask);
        assertEquals(tasks, (FilterUtil.filterTaskByNames(tasks, nameList)));
    }
    
    @Test
    public void testFilterTaskByTags_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        HashSet<String> nameList = new HashSet<String>();

        //filter out first test task
        nameList.add("Buy");
        tasks.add(firstTestTask);
        assertNotEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        nameList.remove("Buy");
        nameList.add("Milk");
        assertNotEquals(tasks, FilterUtil.filterTaskByTags(tasks, nameList));
        tasks.add(secondTestTask);
        nameList.add("personal");
        assertNotEquals(tasks, (FilterUtil.filterTaskByTags(tasks, nameList)));
    }

    @Test
    public void testFilterCompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, true));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out completed tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, true));
    }
    
    @Test
    public void testFilterCompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out completed tasks
        assertNotEquals(tasks, FilterUtil.filterTasksByStatus(tasks, true));
    }

    @Test
    public void testFilterIncompletedTaskList_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTasksByStatus(tasks, false));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out incompleted tasks
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(secondTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTasksByStatus(tasks, false));
    }
    
    @Test
    public void testFilterIncompletedTaskList_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out incompleted tasks
        assertNotEquals(tasks, FilterUtil.filterTasksByStatus(tasks, false));
    }

    @Test
    public void testFilterTaskBySingleDate_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, DateUtil.floorDate(today)));
        assertEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, null));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out first task
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTaskBySingleDate(filteredTasks, DateUtil.floorDate(today)));
        
        //filter out second task
        filteredTasks = getEmptyTaskList();
        filteredTasks.add(secondTestTask);
        assertEquals(filteredTasks, FilterUtil.filterTaskBySingleDate(filteredTasks, DateUtil.floorDate(tmr)));
    }
    
    @Test
    public void testFilterTaskBySingleDate_not_equals() {
        List<Task> tasks = getEmptyTaskList();
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        assertNotEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, today));
        assertNotEquals(tasks, FilterUtil.filterTaskBySingleDate(tasks, tmr));
    }

    @Test
    public void testFilterTaskWithDateRange_equals() {
        List<Task> tasks = getEmptyTaskList();
        assertEquals(tasks, FilterUtil.filterTaskWithDateRange(tasks, 
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)));
        tasks.add(firstTestTask);
        tasks.add(secondTestTask);
        
        //filter out of range of test task
        assertEquals(getEmptyTaskList(), FilterUtil.filterTaskWithDateRange(tasks, 
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)));
        
        //filter out first task
        List<Task> filteredTasks = getEmptyTaskList();
        filteredTasks.add(firstTestTask);
        assertEquals(getEmptyTaskList(), FilterUtil.filterTaskWithDateRange(tasks, today, today));
        
        //filter out second task
        filteredTasks = getEmptyTaskList();
        filteredTasks.add(secondTestTask);
        assertEquals(getEmptyTaskList(), FilterUtil.filterTaskWithDateRange(tasks, tmr, tmr));
        
        //filter out both task
        assertEquals(tasks, FilterUtil.filterTaskWithDateRange(tasks, ytd, tmr));
        assertEquals(tasks, FilterUtil.filterTaskWithDateRange(tasks, null, tmr));
        assertEquals(tasks, FilterUtil.filterTaskWithDateRange(tasks, ytd, null));
    }

    @Test
    public void testFilterEventByNames_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        //empty evnt list and name list
        assertEquals(events, FilterUtil.filterEventByNames(events, nameList));
        //empty event list
        nameList.add("Nothing");
        assertEquals(events, FilterUtil.filterEventByNames(events, nameList));
        
        //filter out overdue events
        nameList.add("CS3216");
        nameList.add("roadshow");
        events.addAll(overdueEvents);
        events.addAll(currentEvents);
        List<Event> filteredResult = getEmptyEventList();
        filteredResult.addAll(overdueEvents);
        assertTrue(filteredResult.containsAll(FilterUtil.filterEventByNames(events, nameList)));
        
        //empty event list with name list
        events.removeAll(overdueEvents);
        events.removeAll(currentEvents);
        assertTrue(events.containsAll(FilterUtil.filterEventByNames(events, nameList)));
    }
    
    @Test
    public void testFilterEventByNames_filter_not_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        events.addAll(overdueEvents);
        nameList.add("Nothing");
        assertNotEquals(events, FilterUtil.filterEventByNames(events, nameList));
        
        nameList.add("Buy");
        assertNotEquals(events, FilterUtil.filterEventByNames(events, nameList));
    }

    @Test
    public void testFilterEventByTags_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();
        //empty event list and name list
        assertEquals(events, FilterUtil.filterEventByTags(events, nameList));
        //empty event list
        nameList.add("Nothing");
        assertEquals(events, FilterUtil.filterEventByTags(events, nameList));
        
        //filter out overdue events
        nameList.add("CS3216");
        nameList.add("CSIT");
        events.addAll(overdueEvents);
        assertEquals(events, FilterUtil.filterEventByTags(events, nameList));
        
        //filter out both overdue and current events
        events.addAll(currentEvents);
        nameList.add("CS3216");
        nameList.add("CS3217");
        assertEquals(events, (FilterUtil.filterEventByTags(events, nameList)));
        
        //empty task list with name list
        events.removeAll(overdueEvents);
        events.removeAll(currentEvents);
        assertEquals(events, (FilterUtil.filterEventByNames(events, nameList)));
    }
    
    @Test
    public void testFilterEventByTags_not_equals() {
        List<Event> events = getEmptyEventList();
        HashSet<String> nameList = new HashSet<String>();

        //filter out first test task
        nameList.add("test");
        events.addAll(overdueEvents);
        assertNotEquals(events, FilterUtil.filterEventByTags(events, nameList));
        nameList.remove("test");
        nameList.add("assignment");
        assertNotEquals(events, FilterUtil.filterEventByTags(events, nameList));
        events.addAll(currentEvents);
        nameList.add("roadshow");
        assertNotEquals(events, (FilterUtil.filterEventByTags(events, nameList)));
    }

    @Test
    public void testFilterIsOverEventList_equals() {
        List<Event> events = new ArrayList<Event>();
        assertEquals(events, FilterUtil.filterEventsByStatus(events, true));
        assertEquals(events, FilterUtil.filterEventsByStatus(events, false));
        
        events.addAll(overdueEvents);
        assertEquals(events, FilterUtil.filterEventsByStatus(events, true));
    }
    
    @Test
    public void testFilterIsOverEventList_not_equals() {
        List<Event> events = new ArrayList<Event>();
        
        events.addAll(overdueEvents);
        assertNotEquals(events, FilterUtil.filterEventsByStatus(events, false));
    }

    @Test
    public void testFilterCurrentEventList_equals() {
        List<Event> events = new ArrayList<Event>();
        assertEquals(events, FilterUtil.filterEventsByStatus(events, false));
        assertEquals(events, FilterUtil.filterEventsByStatus(events, true));
        
        events.addAll(currentEvents);
        assertEquals(events, FilterUtil.filterEventsByStatus(events, false));    
    }
    
    @Test
    public void testFilterCurrentEventList_not_equals() {
        List<Event> events = new ArrayList<Event>();
        
        events.addAll(currentEvents);
        assertNotEquals(events, FilterUtil.filterEventsByStatus(events, true));
    }

    @Test
    public void testFilterEventBySingleDate_equals() {
        List<Event> events = new ArrayList<Event>();
        //events is empty
        assertEquals(events, FilterUtil.filterEventBySingleDate(events, today));
        //filter out overdue events 
        events.addAll(overdueEvents);
        assertEquals(events, FilterUtil.filterEventBySingleDate(events, DateUtil.floorDate(ytd)));
    }
    
    @Test
    public void testFilterEventBySingleDate_not_equals() {
        List<Event> events = new ArrayList<Event>();
        
        //filter out current events 
        events.addAll(currentEvents);
        assertNotEquals(events, FilterUtil.filterEventBySingleDate(events, DateUtil.floorDate(ytd)));
    }

    @Test
    public void testFilterEventWithDateRange_equals() {
        List<Event> events = new ArrayList<Event>();
        //events is empty
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, ytd, ytd));
        //filter out overdue events 
        events.addAll(overdueEvents);
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(ytd), DateUtil.ceilDate(ytd)));
        //filter out both overdue and current events
        events.addAll(currentEvents);
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(ytd), DateUtil.ceilDate(tmr)));
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, null, DateUtil.ceilDate(tmr)));
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(ytd), null));
        assertEquals(events, FilterUtil.filterEventWithDateRange(events, null, null));
    }
    
    @Test
    public void testFilterEventWithDateRange_not_equals() {
        List<Event> events = new ArrayList<Event>();
        
        //filter out overdue events 
        events.addAll(currentEvents);
        assertNotEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.floorDate(ytd), DateUtil.ceilDate(ytd)));
        //filter out both overdue and current events
        events.addAll(currentEvents);
        assertNotEquals(events, FilterUtil.filterEventWithDateRange(events, DateUtil.ceilDate(tmr), DateUtil.ceilDate(tmr)));
    }
    
    private Task generateFirstTestTask() {
        Task task = new Task();
        task.setName("Buy Milk");
        task.setCalendarDateTime(today);
        task.addTag("personal");
        task.setCompleted();
        return task;
    }
    
    private Task generateSecondTestTask() {
        Task task = new Task();
        task.setName("CS2103");
        task.setCalendarDateTime(tmr);
        task.addTag("CS2103");
        return task;
    }
    
    private List<Task> getEmptyTaskList() {
        return new ArrayList<Task>();
    }
    
    private List<Event> getEmptyEventList() {
        return new ArrayList<Event>();
    }
    
    private List<Event> generateOverdueEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = new Event();
        event.setStartDate(ytd);
        event.setEndDate(ytd);
        event.setName("CS2103 V0.5");
        event.addTag("CS2103");
        events.add(event);
        event.removeTag("CS2103");
        event.setName("CSIT roadshow");
        event.addTag("CSIT");
        events.add(event);
        return events;
    }
    
    private List<Event> generateCurrentEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = new Event();
        event.setStartDate(today);
        event.setEndDate(tmr);
        event.setName("CS3216 9th Steps");
        event.addTag("CS3216");
        events.add(event);
        event.removeTag("CS3216");
        event.setName("CS3217 9th Steps");
        event.addTag("CS3217");
        events.add(event);
        return events;
        
    }
}
