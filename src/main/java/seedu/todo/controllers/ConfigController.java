package seedu.todo.controllers;

import java.io.IOException;
import java.util.List;

import seedu.todo.MainApp;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.exceptions.CannotConfigureException;
import seedu.todo.commons.util.ConfigUtil;
import seedu.todo.models.TodoListDB;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.ConfigView;

public class ConfigController implements Controller {
    
    private static String NAME = "Configure";
    private static String DESCRIPTION = "Shows current configuration settings or updates them.";
    private static String COMMAND_SYNTAX = "config [<setting> <value>]";
    
    private static final String MESSAGE_SHOWING = "Showing all settings.";
    private static final String MESSAGE_SUCCESS = "Successfully updated %s.";
    private static final String MESSAGE_FAILURE = "Could not update settings: %s";
    private static final String MESSAGE_INVALID_INPUT = "Invalid config setting provided!";
    private static final String MESSAGE_WRONG_EXTENSION = "Could not change storage path: File must end with %s";
    private static final String SPACE = " ";
    private static final int ARGS_LENGTH = 2;
    private static final String DB_FILE_EXTENSION = ".json";
    
    private static CommandDefinition commandDefinition =
            new CommandDefinition(NAME, DESCRIPTION, COMMAND_SYNTAX);

    public static CommandDefinition getCommandDefinition() {
        return commandDefinition;
    }

    @Override
    public float inputConfidence(String input) {
        // TODO
        return input.startsWith("config") ? 1 : 0;
    }

    @Override
    public void process(String input) {
        String params = input.replaceFirst("config", "").trim();
        
        if (params.length() <= 0) {
            
            // Update console message
            UiManager.updateConsoleMessage(MESSAGE_SHOWING);
        
        } else {
            
            String[] args = params.split(SPACE, ARGS_LENGTH);
            
            // Check args length
            if (args.length != ARGS_LENGTH) {
                failWithMessage(MESSAGE_INVALID_INPUT);
                return;
            }
            
            assert args.length == ARGS_LENGTH;
            
            // Split by args
            String configName = args[0];
            String configValue = args[1];
            
            // Get current config
            Config config = MainApp.getConfig();
            
            // Check name
            List<String> validConfigDefinitions = config.getDefinitionsNames();
            if (!validConfigDefinitions.contains(configName)) {
                failWithMessage(MESSAGE_INVALID_INPUT);
                return;
            }
            
            assert validConfigDefinitions.contains(configName);
            
            // Update config value
            try {
                config = updateConfigByName(config, configName, configValue);
            } catch (CannotConfigureException e) {
                failWithMessage(e.getMessage());
                return;
            }
            
            // Save config to file
            try {
                ConfigUtil.saveConfig(config, MainApp.getConfigFilePath());
            } catch (IOException e) {
                failWithMessage(String.format(MESSAGE_FAILURE, e.getMessage()));
                return;
            }
            
            // Update console
            UiManager.updateConsoleMessage(String.format(MESSAGE_SUCCESS, configName));
        }
        
        // Re-render
        ConfigView view = UiManager.loadView(ConfigView.class);
        view.render();
    }
    
    private void failWithMessage(String message) {
        ConfigView view = UiManager.loadView(ConfigView.class);
        view.render();
        UiManager.updateConsoleMessage(message);
    }
    
    private Config updateConfigByName(Config config, String configName, String configValue) throws CannotConfigureException {
        switch (configName) {
        case "appTitle" :
            // Updates MainWindow title
            UiManager.getInstance().getMainWindow().setTitle(configValue);
            
            // Update config
            config.setAppTitle(configValue);
            
            break;
            
        case "databaseFilePath" :
            // Make sure the new path has a .json extension
            if (!configValue.endsWith(DB_FILE_EXTENSION)) {
                throw new CannotConfigureException(String.format(MESSAGE_WRONG_EXTENSION, DB_FILE_EXTENSION));
            }
            
            // Move the DB file to the new location
            try {
                TodoListDB.getInstance().move(configValue);
            } catch (IOException e) {
                throw new CannotConfigureException(e.getMessage());
            }
            
            // Update config
            config.setDatabaseFilePath(configValue);
            
            break;
            
        default :
            break;
        }
        
        return config;
    }

}
