import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class SettingView extends VBox {
    public SettingView(Model model, SettingsActions actions) {
        super(16);
        setAlignment(Pos.CENTER);
        backgroundProperty().bind(model.backgroundProperty());

        MenuButton backgrounds = new MenuButton("Background");
        MenuItem sunrise = new MenuItem("Sunrise");
        MenuItem midnight = new MenuItem("Midnight");
        sunrise.setOnAction(event -> actions.setBackground("bg1.png"));
        midnight.setOnAction(event -> actions.setBackground("bg2.png"));
        backgrounds.getItems().addAll(sunrise, midnight);

        MenuButton arenas = new MenuButton();
        arenas.textProperty().bind(model.arenaProperty().concat(" arena"));
        for (String name : new String[]{"Classic", "Forest", "Desert"}) {
            MenuItem item = new MenuItem(name);
            item.setOnAction(event -> actions.setArena(name));
            arenas.getItems().add(item);
        }
        Button back = new Button("Main menu");
        back.setOnAction(event -> actions.showMainMenu());
        getChildren().addAll(backgrounds, arenas, back);
    }
}
