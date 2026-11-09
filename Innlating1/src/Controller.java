import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class Controller {
    private final StackPane container;
    private final Model model;

    public Controller(StackPane container, Model model) {
        this.container = container;
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public void showMenu(Region menu) {
        container.getChildren().setAll(menu);
    }

    public void setBackground(String filename) {
        try {
            Image img = new Image(new java.io.File("img/" + filename).toURI().toString());
            if (img.isError()) {
                throw img.getException();
            }
            // Fill the menu while keeping the image's proportions.
            BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage backgroundImage = new BackgroundImage(
                    img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    size);
            Background background = new Background(backgroundImage);

            // Update the model once. Both menus update through their bindings.
            model.backgroundProperty().set(background);
        } catch (Exception e) {
            System.out.println("Could not load background: " + filename);
        }
    }

    public void exitGame() {
        Platform.exit();

    }

    public void gameSettings() {

    }

}
