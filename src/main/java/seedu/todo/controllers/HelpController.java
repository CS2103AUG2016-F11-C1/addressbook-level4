package seedu.todo.controllers;

import java.util.Arrays;

import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.HelpView;

/**
 * Controller to show commands help.
 * 
 * @author louietyj
 *
 */
public class HelpController extends Controller {

    private static final String NAME = "Help";
    private static final String DESCRIPTION = "Shows documentation for all valid commands.";
    private static final String COMMAND_SYNTAX = "help";
    private static final String COMMAND_KEYWORD = "help";
    
    private static final String MESSAGE_HELP_SUCCESS = "Showing all commands.";
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX, COMMAND_KEYWORD); 

    @Override
    public CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public void process(String input) {
        HelpView view = UiManager.loadView(HelpView.class);
        view.commandDefinitions = Arrays.asList(getAllCommandDefinitions());
        UiManager.renderView(view);
        
        UiManager.updateConsoleMessage(MESSAGE_HELP_SUCCESS);
    }
    
    private CommandDefinition[] getAllCommandDefinitions() {
        return new CommandDefinition[] { new HelpController().getCommandDefinition(),
                                         new AddController().getCommandDefinition(),
                                         new ListController().getCommandDefinition(),
                                         new UpdateController().getCommandDefinition(),
                                         new CompleteTaskController().getCommandDefinition(),
                                         new UncompleteTaskController().getCommandDefinition(),
                                         new DestroyController().getCommandDefinition(),
                                         new ConfigController().getCommandDefinition(),
                                         new DestroyController().getCommandDefinition(),
                                         new ClearController().getCommandDefinition(),
                                         new FindController().getCommandDefinition(),
                                         new TagController().getCommandDefinition(),
                                         new UntagController().getCommandDefinition(),
                                         new ExitController().getCommandDefinition() };
    }
}
