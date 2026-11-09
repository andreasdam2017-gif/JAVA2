import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class Controller implements SettingsActions {
    private final StackPane container;
    private final Model model;

    public Controller(StackPane container, Model model) {
        this.container = container;
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public ReadOnlyObjectProperty<Background> backgroundProperty() {
        return model.backgroundProperty();
    }

    @Override
    public ReadOnlyStringProperty arenaProperty() {
        return model.arenaProperty();
    }

    @Override
    public void showMenu(Region menu) {
        container.getChildren().setAll(menu);
    }

    public void startGame() {
        // The actual game will be added later.
        showMenu(new StackPane());
    }

    @Override
    public void setArena(String arena) {
        model.arenaProperty().set(arena);
    }

    public void sendMessage(String text) {
        String message = text.trim();
        if (message.isEmpty()) {
            return;
        }
        // Add the new message after the existing conversation.
        model.messagesProperty().set(
                model.messagesProperty().get() + "You: " + message + "\n");
    }

    @Override
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
