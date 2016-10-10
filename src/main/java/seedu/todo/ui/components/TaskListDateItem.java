package seedu.todo.ui.components;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import seedu.todo.ui.views.IndexView.TaskStub;

public class TaskListDateItem extends MultiComponent {

	private static final String FXML_PATH = "components/TaskListDateItem.fxml";
	
	// Props
	public LocalDate dateTime;
	public ArrayList<TaskStub> tasks;
	
	// FXML
	@FXML
	private Text dateText;
	@FXML
	private VBox dateTaskItemsPlaceholder;
	
	@Override
	public String getFxmlPath() {
		return FXML_PATH;
	}
	
	@Override
	public void componentDidMount() {
		dateText.setText(dateTime.toString() + " (" + tasks.size() + " tasks)");
	}

}
