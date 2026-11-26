import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    private Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane container = new StackPane();
        controller = new Controller(container, new Model());
        controller.setBackground("bg1.png");
        controller.showMainMenu();
        stage.setTitle("Bombastic - Assignment 2");
        stage.setScene(new Scene(container, 600, 570));
        stage.setMinWidth(500);
        stage.setMinHeight(600);
        stage.focusedProperty().addListener((observable, before, focused) -> {
            if (!focused) {
                controller.clearGameInput();
            }
        });
        stage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.stopGame();
        }
    }
}
