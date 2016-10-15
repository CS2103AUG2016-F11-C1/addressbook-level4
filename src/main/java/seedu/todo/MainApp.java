package seedu.todo;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.todo.commons.core.EventsCenter;
import seedu.todo.ui.UiManager;
import seedu.todo.ui.views.IndexView;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.exceptions.DataConversionException;
import seedu.todo.commons.util.ConfigUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.core.Version;
import seedu.todo.commons.events.ui.ExitAppRequestEvent;
import seedu.todo.models.TodoListDB;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(1, 0, 0, true);
    
    private static final String MESSAGE_WELCOME = "Welcome! What would like to get done today?";

    private static Config config;
    
    protected UiManager ui;

    public MainApp() {}

    @Override
    public void init() throws Exception {
        super.init();

        // Initialize config from config file, or create a new one.
        setConfig(initConfig(getApplicationParameter("config")));

        // Initialize logging
        initLogging(getConfig());

        // Initialize events center
        initEventsCenter();

        // Initialize UI config
        UiManager.initialize(getConfig());
        ui = UiManager.getInstance();

        // Load DB
        if (!TodoListDB.getInstance().load())
            TodoListDB.getInstance().save();
    }

    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);

        IndexView view = UiManager.loadView(IndexView.class);
        view.tasks = TodoListDB.getInstance().getAllTasks();
        view.events = TodoListDB.getInstance().getAllEvents();
        UiManager.renderView(view);
        
        // Show welcome message
        UiManager.updateConsoleMessage(MESSAGE_WELCOME);
    }

    @Override
    public void stop() {
        ui.stop();
        Platform.exit();
        System.exit(0);
    }

    /** ================== UTILS ====================== **/

    /**
     * Gets command-line parameter by name.
     * 
     * @param parameterName   Name of parameter
     * @return   Value of parameter
     */
    private String getApplicationParameter(String parameterName){
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    /** ================== INITIALIZATION ====================== **/

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    private Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if(configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. " +
                           "Using default config properties");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedConfig;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    /** ================== SUBSCRIPTIONS ====================== **/

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    /** ================== MAIN METHOD ====================== **/

    public static void main(String[] args) {
        launch(args);
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        MainApp.config = config;
    }
}
