import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChatView extends BorderPane {
    public ChatView(Model model, Consumer<String> sendMessage, Runnable mainMenu) {
        setPadding(new Insets(20));
        backgroundProperty().bind(model.backgroundProperty());
        TextArea messages = new TextArea();
        messages.setEditable(false);
        messages.setWrapText(true);
        messages.textProperty().bind(model.messagesProperty());
        setCenter(messages);

        ListView<String> users = new ListView<>(model.getUsers());
        users.setPrefWidth(110);
        VBox userBox = new VBox(5, new Label("Users"), users);
        VBox.setVgrow(users, Priority.ALWAYS);
        setMargin(userBox, new Insets(0, 0, 0, 10));
        setRight(userBox);

        TextField input = new TextField();
        input.setPromptText("Type a message...");
        Button send = new Button("Send");
        Button back = new Button("Main menu");
        HBox controls = new HBox(10, input, send, back);
        HBox.setHgrow(input, Priority.ALWAYS);
        setMargin(controls, new Insets(10, 0, 0, 0));
        setBottom(controls);
        send.setOnAction(event -> {
            sendMessage.accept(input.getText());
            input.clear();
            messages.positionCaret(messages.getLength());
        });
        input.setOnAction(event -> send.fire());
        back.setOnAction(event -> mainMenu.run());
    }
}
