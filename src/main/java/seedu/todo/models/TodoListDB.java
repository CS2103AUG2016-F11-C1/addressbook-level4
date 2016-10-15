package seedu.todo.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.todo.commons.exceptions.CannotRedoException;
import seedu.todo.commons.exceptions.CannotUndoException;
import seedu.todo.storage.JsonStorage;
import seedu.todo.storage.Storage;

public class TodoListDB {

    private static TodoListDB instance = null;
    private static Storage storage = new JsonStorage();
    
    private Set<Task> tasks = new HashSet<Task>();
    private Set<Event> events = new HashSet<Event>();
    
    protected TodoListDB() {
        // Prevent instantiation.
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<Task>(tasks);
    }
    
    public List<Event> getAllEvents() {
        return new ArrayList<Event>(events);
    }
    
    public Task createTask() {
        Task task = new Task();
        tasks.add(task);
        return task;
    }
    
    public boolean destroyTask(Task task) {
        tasks.remove(task);
        return save();
    }
    
    public Event createEvent() {
        Event event = new Event();
        events.add(event);
        return event;
    }
    
    public boolean destroyEvent(Event event) {
        events.remove(event);
        return save();
    }
    
    public static TodoListDB getInstance() {
        if (instance == null) {
            instance = new TodoListDB();
        }
        return instance;
    }
    
    public boolean save() {
        try {
            storage.save(this);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public boolean load() {
        try {
            instance = storage.load();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public boolean move(String newPath) {
        try {
            storage.move(newPath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public int undoSize() {
        return storage.undoSize();
    }
    
    public boolean undo() {
        try {
            instance = storage.undo();
            return true;
        } catch (CannotUndoException | IOException e) {
            return false;
        }
    }
    
    public int redoSize() {
        return storage.redoSize();
    }
    
    public boolean redo() {
        try {
            instance = storage.redo();
            return true;
        } catch (CannotRedoException | IOException e) {
            return false;
        }
    }
}
