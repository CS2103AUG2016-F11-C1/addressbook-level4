package seedu.todo.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import seedu.todo.commons.EphemeralDB;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.controllers.concerns.Renderer;
import seedu.todo.controllers.concerns.Tokenizer;
import seedu.todo.models.CalendarItem;
import seedu.todo.models.TodoListDB;

/**
 * Controller to destroy a CalendarItem.
 * 
 * @@author Tiong YaoCong A0139922Y
 *
 */
public class TagController implements Controller {
    
    private static final String NAME = "Tag";
    private static final String DESCRIPTION = "Tag a task/event by listed index";
    private static final String COMMAND_SYNTAX = "tag <index> <tag name>";
    
    private static final String TAG_FORMAT = "tag %d";
    private static final String MESSAGE_TAG_SUCCESS = "Item has been tagged successfully.";
    private static final String MESSAGE_INDEX_OUT_OF_RANGE = "Could not tag task/event: Invalid index provided!";
    private static final String MESSAGE_MISSING_INDEX_AND_TAG_NAME = "Please specify the index of the item and the tag name to tag.";
    private static final String MESSAGE_INDEX_NOT_NUMBER = "Index has to be a number!";
    private static final String MESSAGE_TAG_NAME_NOT_FOUND = "Could not tag task/event: Tag name not provided!";
    private static final String MESSAGE_EXCEED_TAG_SIZE = "Could not tag task/event : Tag size exceed";
    private static final String MESSAGE_TAG_NAME_EXIST = "Could not tag task/event: Tag name already exist or Duplicate Tag Names!";
    
    private static final int ITEM_INDEX = 0;
    private static final int TOKENIZER_DEFAULT_INDEX = 1;
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX); 

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        // TODO
        return (input.toLowerCase().startsWith("tag") || input.startsWith("tags")) ? 1 : 0;
    }
    
    /**
     * Get the token definitions for use with <code>tokenizer</code>.<br>
     * This method exists primarily because Java does not support HashMap
     * literals...
     * 
     * @return tokenDefinitions
     */
    private static Map<String, String[]> getTokenDefinitions() {
        Map<String, String[]> tokenDefinitions = new HashMap<String, String[]>();
        tokenDefinitions.put("default", new String[] {"tag"});
        return tokenDefinitions;
    }

    @Override
    public void process(String input) throws ParseException {
        
        Map<String, String[]> parsedResult;
        parsedResult = Tokenizer.tokenize(getTokenDefinitions(), input);
        
        // Extract param
        String param = parsedResult.get("default")[TOKENIZER_DEFAULT_INDEX];
        
        if (param.length() <= 0) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_MISSING_INDEX_AND_TAG_NAME);
            return;
        }
        
        assert param.length() > 0;
        
        String[] parameters = parseParam(param);
        // Get index.
        int index = 0;
        String tagNames = null;
        try {
            index = Integer.decode(parameters[ITEM_INDEX]);
            tagNames = param.replaceFirst(parameters[ITEM_INDEX], "").trim();
        } catch (NumberFormatException e) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_INDEX_NOT_NUMBER);
            return;
        }
        
        // Get record
        EphemeralDB edb = EphemeralDB.getInstance();
        CalendarItem calendarItem = edb.getCalendarItemsByDisplayedId(index);
        TodoListDB db = TodoListDB.getInstance();
        
        if (calendarItem == null) {
            Renderer.renderDisambiguation(String.format(TAG_FORMAT, index), MESSAGE_INDEX_OUT_OF_RANGE);
            return;
        }
        
        // Check if tag name is provided
        if (parameters.length <= 1) {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_TAG_NAME_NOT_FOUND);
            return;
        }
        
        assert calendarItem != null;
        
        String[] parsedTagNames = parseTags(tagNames);
        
        boolean isTagNameDuplicate = checkDuplicateTagName(parsedTagNames, calendarItem);
        
        if (isTagNameDuplicate) {
            Renderer.renderDisambiguation(String.format(TAG_FORMAT, index), MESSAGE_TAG_NAME_EXIST);
            return;
        }
        
        boolean resultOfTagging = addingTagNames(parsedTagNames, calendarItem);
        
        // Re-render
        if (resultOfTagging) {
            db.addIntoTagList(parsedTagNames);
            db.save();
            Renderer.renderIndex(db, MESSAGE_TAG_SUCCESS);
        } else {
            Renderer.renderDisambiguation(COMMAND_SYNTAX, MESSAGE_EXCEED_TAG_SIZE);
        }
    }

    private boolean addingTagNames(String[] parsedTagNames, CalendarItem calendarItem) {
        assert parsedTagNames != null;
        
        boolean result = true;
        for (int i = 0; i < parsedTagNames.length; i ++) {
            result = calendarItem.addTag(parsedTagNames[i].trim()) & result;
        }
        return result;
    }
    
    private boolean checkDuplicateTagName(String[] parsedTagNames, CalendarItem calendarItem) {
        HashSet<String> parsedTagNamesList = new HashSet<String>();
        for (int i = 0; i < parsedTagNames.length; i ++) {
            //checking with overall tag list in db
            if (calendarItem.getTagList().contains(parsedTagNames[i].trim())) {
                return true;
            }
            
            //checking with the current array, if there are duplicate tags
            parsedTagNamesList.add(parsedTagNames[i]);
        }
        
        if (parsedTagNamesList.size() != parsedTagNames.length) {
            return true;
        }
        
        return false;
    }
    
    private String[] parseParam(String param) {
        return param.split(" ");
    }
    
    private String [] parseTags(String tags) {
        return tags.split(",");
    }

}
