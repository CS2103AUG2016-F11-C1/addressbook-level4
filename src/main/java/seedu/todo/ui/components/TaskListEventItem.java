package seedu.todo.ui.components;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.todo.models.Event;
import seedu.todo.ui.UiPartLoader;

public class TaskListEventItem extends MultiComponent {

    private static final String FXML_PATH = "components/TaskListEventItem.fxml";

    // Props
    public Event event;
    public int displayIndex;

    // FXML
    @FXML
    private Text eventText;
    @FXML
    private Text rowIndex;

    public static TaskListEventItem load(Stage primaryStage, Pane placeholderPane) {
        return UiPartLoader.loadUiPart(primaryStage, placeholderPane, new TaskListEventItem());
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    @Override
    public void componentDidMount() {
        eventText.setText(event.getName());
        rowIndex.setText(displayIndex + "");
    }

}
