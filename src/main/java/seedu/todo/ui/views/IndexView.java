package seedu.todo.ui.views;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.ui.components.TagList;
import seedu.todo.ui.components.TaskList;

public class IndexView extends View {

	private static final String FXML_PATH = "views/IndexView.fxml";
	
	// FXML
	@FXML
	private AnchorPane tagsPane;
	@FXML
	private AnchorPane tasksPane;
	
	// Props
	public ArrayList<Object> tasks = new ArrayList<Object>(); // stub
	public ArrayList<Object> tags = new ArrayList<Object>(); // stub
	public String indexTextValue;


	@Override
	public String getFxmlPath() {
		return FXML_PATH;
	}
	
	@Override
	public void componentDidMount() {
		// Makes full width wrt parent container.
		FxViewUtil.makeFullWidth(this.mainNode);
		
		// Load sub components
		loadComponents();
	}
	
	private void loadComponents() {
		// Render TagList
		TagList tagList = new TagList();
		tagList.setHookModifyView(v -> {
			TagList view = (TagList) v;
			view.tags = tags;
			return view;
		});
		tagList.render(primaryStage, tagsPane);
		
		// Render TaskList
		TaskList taskList = new TaskList();
		taskList.setHookModifyView(v -> {
			TaskList view = (TaskList) v;
			view.tasks = tasks;
			return view;
		});
		taskList.render(primaryStage, tasksPane);
	}
	
	
}
