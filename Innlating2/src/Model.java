import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Background;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private final ObjectProperty<Background> background = new SimpleObjectProperty<>();

    private final StringProperty arena = new SimpleStringProperty("Classic");

    private final StringProperty messages = new SimpleStringProperty("");
    private final ObservableList<String> users =
            FXCollections.observableArrayList("You", "Anna", "Peter");

    public StringProperty messagesProperty() {
        return messages;
    }

    public ObservableList<String> getUsers() {
        return users;
    }

    public StringProperty arenaProperty() {
        return arena;
    }

    public ObjectProperty<Background> backgroundProperty() {
        return background;
    }
}
