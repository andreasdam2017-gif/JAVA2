import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ChatView {
    private final Controller controller;

    public ChatView(Controller controller) {
        this.controller = controller;
    }

    public Region build(Region mainMenu) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.backgroundProperty().bind(controller.getModel().backgroundProperty());

        // Centre: show the messages stored in the model.
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.textProperty().bind(controller.getModel().messagesProperty());
        layout.setCenter(messageArea);

        ListView<String> userList = new ListView<>(controller.getModel().getUsers());
        userList.setPrefWidth(110);
        VBox users = new VBox(5, new Label("Users"), userList);
        VBox.setVgrow(userList, Priority.ALWAYS);
        BorderPane.setMargin(users, new Insets(0, 0, 0, 10));
        layout.setRight(users);

        TextField input = new TextField();
        input.setPromptText("Type a message...");
        Button send = new Button("Send");
        Button back = new Button("Main menu");
        HBox controls = new HBox(10, input, send, back);
        HBox.setHgrow(input, Priority.ALWAYS);
        BorderPane.setMargin(controls, new Insets(10, 0, 0, 0));
        layout.setBottom(controls);

        send.setOnAction(event -> {
            controller.sendMessage(input.getText());
            input.clear();
            messageArea.positionCaret(messageArea.getLength());
        });
        input.setOnAction(event -> send.fire());
        back.setOnAction(event -> controller.showMenu(mainMenu));

        return layout;
    }
}
