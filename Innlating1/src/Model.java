import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;

public class Model {
    // A property that notifies the menus when its value changes.
    private final ObjectProperty<Background> background = new SimpleObjectProperty<>();

    public ObjectProperty<Background> backgroundProperty() {
        return background;
    }
}
