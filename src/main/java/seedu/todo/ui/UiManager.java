package seedu.todo.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.commons.core.ComponentManager;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.ui.views.View;

import java.util.logging.Logger;

/**
 * The manager of the UI component.
 */
public class UiManager extends ComponentManager implements Ui {
    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    // Only one instance of UiManager should be present. 
    private static UiManager instance = null;

    private Config config;
    private MainWindow mainWindow;
    
    private static final String FATAL_ERROR_DIALOG = "Fatal error during initializing";
    private static final String LOAD_VIEW_ERROR = "Cannot loadView: UiManager not instantiated.";
    
    protected UiManager() {
        // Prevent instantiation.
    }

    public static UiManager getInstance() {
        return instance;
    }

    public static void initialize(Config config) {
        if (instance == null) {
            instance = new UiManager();
        }
        
        instance.config = config;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");

        // Show main window.
        try {
            mainWindow = MainWindow.load(primaryStage, config);
            mainWindow.render();
            mainWindow.show();
        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown(FATAL_ERROR_DIALOG, e);
        }
    }

    @Override
    public void stop() {
        mainWindow.hide();
    }

    public static <T extends View> T loadView(Class<T> viewClass) {
        if (instance == null) {
            logger.warning(LOAD_VIEW_ERROR);
            return null;
        }
        
        return instance.mainWindow.loadView(viewClass);
    }


    /** ================ DISPLAY ERRORS ================== **/

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(mainWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
            String contentText) {
        final Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

}
