package seedu.todo.guitests.guihandles;

import javafx.stage.Stage;
import seedu.todo.TestApp;
import seedu.todo.guitests.GuiRobot;

public class MainGuiHandle extends GuiHandle {
    
    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }
    
}
