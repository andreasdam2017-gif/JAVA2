import javafx.application.Platform;
import javafx.scene.layout.*;
import utilities.Assets;

public class Controller implements SettingsActions {
    private final StackPane screenContainer;
    private final Model model;
    private GameController game;
    private GameLoop loop;

    public Controller(StackPane screenContainer, Model model) {
        this.screenContainer = screenContainer;
        this.model = model;
    }

    private void showScreen(Region screen) {
        screenContainer.getChildren().setAll(screen);
    }

    public void showMainMenu() {
        stopGame();
        MainMenuView mainMenu = new MainMenuView(
                model,
                this::startGame,
                this::showChat,
                this::showSettings,
                Platform::exit
        );
        showScreen(mainMenu);
    }

    public void startGame() {
        stopGame();
        Arena arena = new Arena(model.arenaProperty().get());
        game = new GameController(arena);
        GameView view = new GameView(
                arena,
                game::getResult,
                game::keyPressed,
                game::keyReleased,
                game::clearInput,
                this::startGame,
                this::showMainMenu
        );
        showScreen(view);
        loop = new GameLoop(game::update, view::render);
        loop.start();
        Platform.runLater(view::requestGameFocus);
    }

    public void clearGameInput() {
        if (game != null) {
            game.clearInput();
        }
    }

    public void stopGame() {
        if (loop != null) {
            loop.stop();
            loop = null;
        }
        if (game != null) {
            game.close();
            game = null;
        }
    }

    private void showChat() {
        ChatView chat = new ChatView(model, this::sendMessage, this::showMainMenu);
        showScreen(chat);
    }

    private void showSettings() {
        SettingView settings = new SettingView(model, this);
        showScreen(settings);
    }

    private void sendMessage(String text) {
        String message = text.trim();
        if (!message.isEmpty()) {
            model.messagesProperty().set(model.messagesProperty().get() + "You: " + message + "\n");
        }
    }

    @Override
    public void setBackground(String filename) {
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage image = new BackgroundImage(Assets.image(filename),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        model.backgroundProperty().set(new Background(image));
    }

    @Override
    public void setArena(String arena) {
        model.arenaProperty().set(arena);
    }
}
