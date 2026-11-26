import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainMenuView extends VBox {
    public MainMenuView(Model model, Runnable start, Runnable chat, Runnable settings, Runnable exit) {
        super(14);
        setAlignment(Pos.CENTER);
        backgroundProperty().bind(model.backgroundProperty());
        Label title = new Label("BOMBASTIC");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: white;"
                + " -fx-background-color: #202630; -fx-padding: 12px;");
        getChildren().add(title);
        addButton("Start Game", start);
        addButton("Chat", chat);
        addButton("Settings", settings);
        addButton("Exit", exit);
    }

    private void addButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefWidth(180);
        button.setOnAction(event -> action.run());
        getChildren().add(button);
    }
}
