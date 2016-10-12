package seedu.todo.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.todo.MainApp;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.core.GuiSettings;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.events.ui.ExitAppRequestEvent;
import seedu.todo.ui.components.Console;
import seedu.todo.ui.components.ConsoleInput;
import seedu.todo.ui.components.Header;
import seedu.todo.ui.views.View;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends View {

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    private static final String FXML_PATH = "MainWindow.fxml";
    private static final String ICON_PATH = "/images/logo-512x512.png";
    private static final String OPEN_HELP_KEY_COMBINATION = "F1";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 600;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    // FXML Components
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private AnchorPane childrenPlaceholder;
    @FXML
    private AnchorPane consolePlaceholder;
    @FXML
    private AnchorPane consoleInputPlaceholder;
    @FXML
    private AnchorPane headerPlaceholder;

    public static MainWindow load(Stage primaryStage, Config config) {
        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, null, new MainWindow());
        mainWindow.configure(config);
        return mainWindow;
    }

    public void configure(Config config) {
        String appTitle = config.getAppTitle();

        // Configure the UI
        setTitle(appTitle);
        setIcon(ICON_PATH);
        setWindowMinSize();
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        // Bind accelerators
        setAccelerators();

        // Load other components.
        loadComponents();
    }

    private void loadComponents() {
        // Load Header
        Header header = Header.load(primaryStage, getHeaderPlaceholder());
        header.versionString = MainApp.VERSION.toString();
        header.render();

        // Load ConsoleInput
        ConsoleInput consoleInput = ConsoleInput.load(primaryStage, getConsoleInputPlaceholder());
        consoleInput.render();

        // Load Console
        Console console = Console.load(primaryStage, getConsolePlaceholder());
        console.render();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    public void show() {
        primaryStage.show();
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T loadView(Class<T> viewClass) {
        View loadedView = null;

        try {
            Method loadMethod = viewClass.getMethod("load", Stage.class, Pane.class);
            loadedView = (View) loadMethod.invoke(null, primaryStage, getChildrenPlaceholder());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.severe(String.format("View class %s does not have a mandatory method with the method signature: \n" + 
                                        "public static View load(Stage stage, Pane placeholder) \n" +
                                        "This method is mandatory.", 
                                        viewClass.getName()));
            e.printStackTrace();
        }

        return (T) loadedView;

    }

    /** ================ FXML COMPONENTS ================== **/

    public AnchorPane getChildrenPlaceholder() {
        return childrenPlaceholder;
    }

    public AnchorPane getConsolePlaceholder() {
        return consolePlaceholder;
    }

    public AnchorPane getConsoleInputPlaceholder() {
        return consoleInputPlaceholder;
    }

    public AnchorPane getHeaderPlaceholder() {
        return headerPlaceholder;
    }

    /** ================ ACCELERATORS ================== **/

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf(OPEN_HELP_KEY_COMBINATION));
    }

    /** ================ ACTION HANDLERS ================== **/

    @FXML
    public void handleHelp() {
        // TODO: Auto-generated method stub
    }

    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
}
