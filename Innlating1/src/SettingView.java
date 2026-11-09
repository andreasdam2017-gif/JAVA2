import java.lang.classfile.Label;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SettingView {
    private final Controller controller;

    public SettingView(Controller controller) {
        this.controller = controller;
    }

    public Region build(Region mainMenu) {

        BorderPane layout = new BorderPane();
        // Use the same background property as the main menu.
        layout.backgroundProperty().bind(controller.getModel().backgroundProperty());
        VBox vBox = new VBox();
        MenuItem Option1 = new MenuItem("Sunrise Background");
        MenuItem Option2 = new MenuItem("Midnight Background");
        MenuButton dropDownMenu = new MenuButton("Background", null, Option1, Option2);
        Button back = new Button("Return to Main Menu");

        vBox.getChildren().addAll(dropDownMenu, back);
        vBox.setAlignment(Pos.CENTER);
        layout.setCenter(vBox);

        Option1.setOnAction(event -> {
            controller.setBackground("bg1.png");
        });
        Option2.setOnAction(event -> {
            controller.setBackground("bg2.png");
        });
        back.setOnAction(event -> {
            controller.showMenu(mainMenu);
        });
        return layout;
    }

}
