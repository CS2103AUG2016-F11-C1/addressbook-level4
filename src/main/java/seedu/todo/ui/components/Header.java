package seedu.todo.ui.components;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.ui.UiPartLoader;

public class Header extends Component {

    private static final String LOGO_IMAGE_PATH = "/images/logo-64x64.png";
    private static final String FXML_PATH = "components/Header.fxml";
    private static final String VERSION_TEXT = "version ";
    
    // Props
    public String versionString;
    public String appTitle;

    // FXML
    @FXML
    private Text headerAppTitle;
    @FXML
    private Text headerVersionText;
    @FXML
    private ImageView headerLogoImageView;

    public static Header load(Stage primaryStage, Pane placeholderPane) {
        return UiPartLoader.loadUiPart(primaryStage, placeholderPane, new Header());
    }

    @Override
    public String getFxmlPath() {
        return FXML_PATH;
    }

    @Override
    public void componentDidMount() {
        // Makes the Component full width wrt parent container.
        FxViewUtil.makeFullWidth(this.mainNode);

        // Set text.
        headerAppTitle.setText(appTitle);
        headerVersionText.setText(VERSION_TEXT + versionString);

        // Set logo image.
        Image image = new Image(LOGO_IMAGE_PATH);
        headerLogoImageView.setImage(image);
    }

}
