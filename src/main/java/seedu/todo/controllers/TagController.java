package seedu.todo.controllers;

import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.core.CommandDefinition;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.TodoListDB;

// @@author A0139922Y
/**
 * Controller to destroy a CalendarItem.
 */
public class TagController extends Controller {
    
    private static final String NAME = "Tag";
    private static final String DESCRIPTION = "Tag a task/event by listed index";
    private static final String COMMAND_SYNTAX = "tag <index> <tag name>";
    private static final String COMMAND_KEYWORD = "tag";
    
    public static final String MESSAGE_TAG_SUCCESS = "Item has been tagged successfully.";
    public static final String MESSAGE_INDEX_OUT_OF_RANGE = "Could not tag task/event: Invalid index provided!";
    public static final String MESSAGE_MISSING_INDEX_AND_TAG_NAME = "Please specify the index of the item and the tag name to tag.";
    public static final String MESSAGE_INDEX_NOT_NUMBER = "Index has to be a number!";
    public static final String MESSAGE_TAG_NAME_NOT_FOUND = "Could not tag task/event: Tag name not provided!";
    public static final String MESSAGE_EXCEED_TAG_SIZE = "Could not tag task/event : Tag size exceed";
    public static final String MESSAGE_TAG_NAME_EXIST = "Could not tag task/event: Tag name already exist!";
    
    private static final int ITEM_INDEX = 0;
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX, COMMAND_KEYWORD); 

    @Override
    public CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public void process(String args) {
        // Extract param
        String param = args.replaceFirst("(tag|tags)", "").trim();
        
        if (param.length() <= 0) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_MISSING_INDEX_AND_TAG_NAME);
            return;
        }
        
        assert param.length() > 0;
        
        String[] parsedResult = parseParam(param);
        // Get index.
        int index = 0;
        String tagName = null;
        try {
            index = Integer.decode(parsedResult[ITEM_INDEX]);
            tagName = param.replaceFirst(parsedResult[ITEM_INDEX], "").trim();
        } catch (NumberFormatException e) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_INDEX_NOT_NUMBER);
            return;
        }
        
        // Get record
        EphemeralDB edb = EphemeralDB.getInstance();
        CalendarItem calendarItem = edb.getCalendarItemsByDisplayedId(index);
        TodoListDB db = TodoListDB.getInstance();
        
        if (calendarItem == null) {
            Renderer.renderDisambiguation(String.format("tag %d", index), MESSAGE_INDEX_OUT_OF_RANGE);
            return;
        }
        
        // Check if tag name is provided
        if (parsedResult.length <= 1) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_TAG_NAME_NOT_FOUND);
            return;
        }
        
        assert calendarItem != null;
        
        boolean isTagNameDuplicate = calendarItem.getTagList().contains(tagName);
        
        if (isTagNameDuplicate) {
            Renderer.renderDisambiguation(String.format("tag %d", index), MESSAGE_TAG_NAME_EXIST);
            return;
        }
        
        boolean resultOfTagging = calendarItem.addTag(tagName);
        
        // Re-render
        if (resultOfTagging) {
            db.updateTagList(tagName);
            db.save();
            Renderer.renderIndex(db, MESSAGE_TAG_SUCCESS);
        } else {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_EXCEED_TAG_SIZE);
        }
    }

    private String[] parseParam(String param) {
        return param.split(" ");
    }

}
