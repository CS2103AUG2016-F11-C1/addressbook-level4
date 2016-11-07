package seedu.todo.ui.views;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import seedu.todo.commons.core.CommandDefinition;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.ui.components.HelpCommandItem;

/**
 * Help View, which shows all the commands available.
 * 
 * @@author A0139812A
 */
public class HelpView extends View {

    private static final String FXML_PATH = "views/HelpView.fxml";
    
    // Props
    public List<CommandDefinition> commandDefinitions = new ArrayList<CommandDefinition>();
    
    // FXML
    @FXML
    private Pane helpCommandsPlaceholder;

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }
    
    @Override
    public void componentDidMount() {
        // Makes the Component full width wrt parent container.
        FxViewUtil.makeFullWidth(this.mainNode);
        
        // Clear help commands
        HelpCommandItem.reset(helpCommandsPlaceholder);
        
        // Load help commands
        for (CommandDefinition command : commandDefinitions) {
            HelpCommandItem item = load(primaryStage, helpCommandsPlaceholder, HelpCommandItem.class);
            item.commandName = command.getCommandName();
            item.commandDescription = command.getCommandDescription();
            item.commandSyntax = command.getCommandSyntax();
            item.render();
        }
    }
    

}
