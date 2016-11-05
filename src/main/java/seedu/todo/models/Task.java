package seedu.todo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @@author A0093907W
 * 
 * Task model
 */
public class Task implements CalendarItem {
    
    private String name;
    private LocalDateTime dueDate;
    private boolean isCompleted = false;
    private ArrayList<String> tagList = new ArrayList<String>();
    
    private static final int MAX_TAG_LIST_SIZE = 20;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the due date of a Task.
     * @return dueDate
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Set the due date of a Task.
     * @param dueDate
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    @Override
    public LocalDateTime getCalendarDateTime() {
        return getDueDate();
    }
    
    @Override
    public void setCalendarDateTime(LocalDateTime date) {
        setDueDate(date);
    }
    
    @Override
    public boolean isOver() {
        if (dueDate == null) {
            return false;
        } else {
            return dueDate.isBefore(LocalDateTime.now());
        }
    }

    /**
     * Returns true if the Task is completed, false otherwise.
     * @return isCompleted
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    
    /**
     * Marks a Task as completed.
     */
    public void setCompleted() {
        this.isCompleted = true;
    }

    /**
     * Marks a Task as incomplete.
     */
    public void setIncomplete() {
        this.isCompleted = false;
    }

    
    /**
     * @@author A0139922Y
     * Return the tag list that belong to the calendar item
     * 
     */
    @Override
    public ArrayList<String> getTagList() {
        return tagList;
    }

    /**
     * @@author A0139922Y
     * Adding the tag into the tag list that belong to the calendar item
     * @param tagName
     *               name of the tag
     *               
     * @return true tag name is successfully added, false if tag list if full         
     */
    @Override
    public boolean addTag(String tagName) {
        if(tagList.size() < MAX_TAG_LIST_SIZE) {
            tagList.add(tagName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @@author A0139922Y
     * Removing the tag from the tag list that belong to the calendar item
     * @param tagName
     *               name of the tag
     *               
     * @return true tag name is successfully removed, false if tag name does not exist    
     */
    @Override
    public boolean removeTag(String tagName) {
        return tagList.remove(tagName);
    }

    /**
     * @@author A0139922Y
     * Returning the maximum tag list size that is allowed
     *               
     */
    @Override
    public int getTagListLimit() {
        return MAX_TAG_LIST_SIZE;
    }

}
