package seedu.todo.models;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

public class TodoListDBTest {
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    @Test
    public void check_singleton() {
        TodoListDB one = TodoListDB.getInstance();
        TodoListDB two = TodoListDB.getInstance();
        assertEquals(one, two);
    }
    
    @Test
    public void test_aliases() {
        TodoListDB.getInstance().getAliases().put("key", "value");
        assertEquals(TodoListDB.getInstance().getAliases().get("key"), "value");
    }
    
    @Test
    public void test_CRUD_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        Task task = TodoListDB.getInstance().createTask();
        assertNotEquals(TodoListDB.getInstance().getAllTasks().indexOf(task), -1);
    }
    
    @Test
    public void test_incomplete_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        TodoListDB.getInstance().createTask();
        assertEquals(TodoListDB.getInstance().countIncompleteTasks(), 1);
    }
    
    @Test
    public void test_overdue_tasks() {
        TodoListDB.getInstance().destroyAllTask();
        Task task = TodoListDB.getInstance().createTask();
        task.setDueDate(LocalDateTime.now().minusSeconds(10));
        assertEquals(TodoListDB.getInstance().countOverdueTasks(), 1);
    }
    
    @Test
    public void test_CRUD_events() {
        TodoListDB.getInstance().destroyAllEvent();
        Event event = TodoListDB.getInstance().createEvent();
        assertNotEquals(TodoListDB.getInstance().getAllEvents().indexOf(event), -1);
    }
    
    @Test
    public void test_future_events() {
        TodoListDB.getInstance().destroyAllEvent();
        Event event = TodoListDB.getInstance().createEvent();
        event.setStartDate(LocalDateTime.now().plusSeconds(10));
        event.setEndDate(LocalDateTime.now().plusSeconds(30));
        assertEquals(TodoListDB.getInstance().countFutureEvents(), 1);
    }

}
