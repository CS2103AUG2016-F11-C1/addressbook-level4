package seedu.todo.ui;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seedu.todo.MainApp;
import seedu.todo.commons.core.Config;
import seedu.todo.commons.core.ConfigCenter;
import seedu.todo.commons.core.GuiSettings;
import seedu.todo.commons.events.ui.ExitAppRequestEvent;
import seedu.todo.commons.exceptions.ParseException;
import seedu.todo.controllers.AliasController;
import seedu.todo.controllers.ConfigController;
import seedu.todo.controllers.HelpController;
import seedu.todo.controllers.ListController;
import seedu.todo.ui.components.Component;
import seedu.todo.ui.components.Console;
import seedu.todo.ui.components.Header;
import seedu.todo.ui.views.View;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 * 
 * @@author A0139812A
 */
public class MainWindow extends Component {

    private static final String FXML_PATH = "MainWindow.fxml";
    private static final String ICON_PATH = "/images/logo-512x512.png";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 600;
    
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_CONFIG = "config";
    private static final String COMMAND_ALIAS = "alias";

    private static final String KEY_OPEN_HELP = "F1";
    private static final String KEY_OPEN_LIST = "F5";
    private static final String KEY_OPEN_CONFIG = "F12";

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    // FXML Components
    @FXML
    private AnchorPane childrenPlaceholder;
    @FXML
    private AnchorPane consoleInputPlaceholder;
    @FXML
    private AnchorPane headerPlaceholder;
    @FXML
    private MenuItem homeMenuItem;
    @FXML
    private MenuItem configMenuItem;
    @FXML
    private MenuItem helpMenuItem;

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

    protected void loadComponents() {
        // Load Header
        Header header = UiPartLoader.loadUiPart(primaryStage, getHeaderPlaceholder(), Header.class);
        header.appTitle = ConfigCenter.getInstance().getConfig().getAppTitle();
        header.versionString = MainApp.VERSION.toString();
        header.render();

        // Load ConsoleInput
        Console console = UiPartLoader.loadUiPart(primaryStage, getConsoleInputPlaceholder(), Console.class);
        console.consoleOutput = UiManager.getConsoleMessage();
        console.consoleInputValue = UiManager.getConsoleInputValue();
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

    public void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    public void setWindowMinSize() {
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

    protected <T extends View> T loadView(Class<T> viewClass) {
        return load(primaryStage, getChildrenPlaceholder(), viewClass);
    }
    
    public Scene getScene() {
        return scene;
    }

    /** ================ FXML COMPONENTS ================== **/

    public AnchorPane getChildrenPlaceholder() {
        return childrenPlaceholder;
    }

    public AnchorPane getConsoleInputPlaceholder() {
        return consoleInputPlaceholder;
    }

    public AnchorPane getHeaderPlaceholder() {
        return headerPlaceholder;
    }

    /** ================ ACCELERATORS ================== **/

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf(KEY_OPEN_HELP));
        homeMenuItem.setAccelerator(KeyCombination.valueOf(KEY_OPEN_LIST));
        configMenuItem.setAccelerator(KeyCombination.valueOf(KEY_OPEN_CONFIG));
    }

    /** ================ ACTION HANDLERS ================== **/
    
    @FXML
    public void handleHelp() {
        // Pass directly to HelpController.
        new HelpController().process(COMMAND_HELP);
    }

    @FXML
    public void handleHome() {
        // Pass directly to ListController.
        try {
            new ListController().process(COMMAND_LIST);
        } catch (ParseException e) {
            return;
        }
    }
    
    @FXML
    public void handleConfig() {
        // Pass directly to HelpController.
        new ConfigController().process(COMMAND_CONFIG);
    }
    
    @FXML
    public void handleAlias() {
        // Pass directly to HelpController.
        new AliasController().process(COMMAND_ALIAS);
    }

    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }
}
