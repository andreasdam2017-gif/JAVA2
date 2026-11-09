import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class app extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        StackPane container = new StackPane();
        Model model = new Model();
        Controller controller = new Controller(container, model);
        controller.setBackground("bg1.png");
        view view = new view(controller);
        container.getChildren().setAll(view.build());

        Scene scene = new Scene(container, 500, 500);

        stage.setTitle("Hello");
        stage.setScene(scene);
        stage.show();

    }

}
