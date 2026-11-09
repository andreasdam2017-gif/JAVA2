
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class view {
	private final Controller controller;

	public view(Controller controller) {
		this.controller = controller;
	}

	public Region build() {
		BorderPane layout = new BorderPane();
		layout.backgroundProperty().bind(controller.getModel().backgroundProperty());

		VBox vBox = new VBox();

		Button StartGameBtn = new Button("Start Game");
		Button ChatBtn = new Button("Chat");
		Button SettingsMenuBtn = new Button("Settings");
		Button ExitGameBtn = new Button("Exit");

		Label label = new Label("Hello");
		vBox.getChildren().addAll(StartGameBtn, ChatBtn, SettingsMenuBtn, ExitGameBtn);
		vBox.setPadding(new Insets(250, 25, 150, 225));
		layout.setCenter(vBox);

		StartGameBtn.setOnAction(event -> controller.startGame());

		ChatBtn.setOnAction(event -> {
			ChatView chatView = new ChatView(controller);
			controller.showMenu(chatView.build(layout));
		});

		SettingsMenuBtn.setOnAction(event -> {
			SettingView settingView = new SettingView(controller);
			controller.showMenu(settingView.build(layout));

		});
		ExitGameBtn.setOnAction(event -> {
			controller.exitGame();
		});
		return layout;

	}

}
