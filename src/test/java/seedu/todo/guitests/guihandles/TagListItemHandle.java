package seedu.todo.guitests.guihandles;

import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.todo.guitests.GuiRobot;

/**
 * @@author A0139922Y
 */
public class TagListItemHandle extends GuiHandle {

    private static final String TAGLISTITEM_LABEL = "#labelText";
    private Node node;

    public TagListItemHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }
    
    public String getName() {
        return getStringFromText(TAGLISTITEM_LABEL, node);
    }

}