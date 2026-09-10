package braun.ui;

import braun.Braun;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI layout of the Braun chatbot.
 * Manages the chat scroll pane, dialog container, and user command inputs.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Braun braun;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private final Image braunImage = new Image(this.getClass().getResourceAsStream("/images/braun.png"));

    /**
     * Constructs a new {@code MainWindow} controller instance.
     */
    public MainWindow() {
    }

    /**
     * Initializes the controller and binds the scroll pane to the dialog container height.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Braun application instance and displays Braun's opening broadcast greeting.
     *
     * @param b the Braun logic instance.
     */
    public void setBraun(Braun b) {
        braun = b;
        dialogContainer.getChildren().add(
                DialogBox.getBraunDialog(braun.getWelcomeMessage(), braunImage)
        );
    }

    /**
     * Handles user input triggered by pressing Enter or clicking the Send button.
     * Queries Braun for a response, appends dialog boxes, and initiates exit if requested.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        String response = braun.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBraunDialog(response, braunImage)
        );
        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
