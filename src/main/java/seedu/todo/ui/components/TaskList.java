package seedu.todo.ui.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import seedu.todo.commons.util.DateUtil;
import seedu.todo.ui.views.IndexView.TaskStub;

public class TaskList extends Component {

	private static final String FXML_PATH = "components/TaskList.fxml";
	
	// Props
	public ArrayList<TaskStub> tasks; // stub
	
	// FXML
	@FXML
	private VBox taskListDateItemsPlaceholder;

	@Override
	public String getFxmlPath() {
		return FXML_PATH;
	}
	
	@Override
	public void componentDidMount() {
		loadTasks();
	}
	
	private void loadTasks() {
		TaskListDateItem.reset(taskListDateItemsPlaceholder);
		
		HashMap<LocalDateTime, ArrayList<TaskStub>> tasksByDate = getTasksByDate(tasks);
		
		// Get unique task dates and sort them
		ArrayList<LocalDateTime> taskDates = new ArrayList<LocalDateTime>();
		taskDates.addAll(tasksByDate.keySet());
		java.util.Collections.sort(taskDates);
		
		// For each dateTime, individually the ArrayList of Tasks.
		for (LocalDateTime dateTime : taskDates) {
			TaskListDateItem item = new TaskListDateItem();
			ArrayList<TaskStub> tasksForDate = tasksByDate.get(dateTime);
			
			item.passInProps(c -> {
				TaskListDateItem view = (TaskListDateItem) c;
				view.dateTime = dateTime;
				view.tasks = tasksForDate;
				return view;
			});
			
			item.render(primaryStage, taskListDateItemsPlaceholder);
		}
	}
	
	private static HashMap<LocalDateTime, ArrayList<TaskStub>> getTasksByDate(ArrayList<TaskStub> tasks) {
		HashMap<LocalDateTime, ArrayList<TaskStub>> tasksByDate = new HashMap<LocalDateTime, ArrayList<TaskStub>>();
		for (TaskStub task : tasks) {
			LocalDateTime taskDate = DateUtil.floorDate(task.dateTime);
			
			// Creates ArrayList if not already exists.
			if (!tasksByDate.containsKey(taskDate)) 
				tasksByDate.put(taskDate, new ArrayList<TaskStub>());
			
			// Adds to the ArrayList.
			tasksByDate.get(taskDate).add(task);
		}
		
		return tasksByDate;
	}
	
}
