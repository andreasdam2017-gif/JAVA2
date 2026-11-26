import java.util.function.Consumer;
import java.util.function.Supplier;
import entities.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class GameView extends BorderPane {
    private final Arena arena;
    private final Canvas canvas;
    private final Label stats = new Label();
    private final Label resultLabel = new Label();
    private final Supplier<String> result;

    public GameView(Arena arena, Supplier<String> result, Consumer<KeyCode> press,
                    Consumer<KeyCode> release, Runnable clearInput, Runnable restart, Runnable mainMenu) {
        this.arena = arena;
        this.result = result;
        setPadding(new Insets(12));
        setStyle("-fx-background-color: #202630;");
        canvas = new Canvas(arena.getWidth(), arena.getHeight());
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> {
            press.accept(event.getCode());
            event.consume();
        });
        canvas.setOnKeyReleased(event -> {
            release.accept(event.getCode());
            event.consume();
        });
        canvas.focusedProperty().addListener((observable, before, focused) -> {
            if (!focused) {
                clearInput.run();
            }
        });
        canvas.setOnMouseClicked(event -> canvas.requestFocus());
        setCenter(new StackPane(canvas));

        stats.setStyle("-fx-text-fill: white;");
        resultLabel.setStyle("-fx-text-fill: #ffda6b; -fx-font-size: 22px; -fx-font-weight: bold;");
        VBox heading = new VBox(8, stats, resultLabel);
        heading.setAlignment(Pos.CENTER);
        setTop(heading);

        Label help = new Label("Blue: WASD + Space     |     Red: arrows + Enter");
        help.setStyle("-fx-text-fill: white;");
        Button restartButton = new Button("Restart round");
        Button menuButton = new Button("Main menu");
        restartButton.setOnAction(event -> restart.run());
        menuButton.setOnAction(event -> mainMenu.run());
        HBox buttons = new HBox(12, restartButton, menuButton);
        buttons.setAlignment(Pos.CENTER);
        VBox footer = new VBox(10, help, buttons);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));
        setBottom(footer);
        render();
    }

    public void requestGameFocus() {
        canvas.requestFocus();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Entity floor : arena.getFloors()) {
            floor.render(gc);
        }
        for (Entity entity : arena.getEntities()) {
            if (!(entity instanceof Player)) {
                entity.render(gc);
            }
        }
        for (Entity entity : arena.getEntities()) {
            if (entity instanceof Player && !((Player) entity).isDead()) {
                entity.render(gc);
            }
        }
        stats.setText(playerStats("Blue", arena.getPlayer1()) + "\n" + playerStats("Red", arena.getPlayer2()));
        resultLabel.setText(result.get());
    }

    private String playerStats(String name, Player player) {
        return name + "  Lives: " + player.getLives() + "   Bombs: " + player.getActiveBombs()
                + "/" + player.getMaxBombs() + "   Range: " + player.getBombRange()
                + "   Speed: " + (int) player.getSpeed();
    }
}
