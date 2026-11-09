import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;

// Only the actions and values that the settings screen needs.
public interface SettingsActions {
    void setBackground(String filename);
    void setArena(String arena);
    void showMenu(Region menu);

    // The view can observe these properties, but cannot call set() on them.
    ReadOnlyObjectProperty<Background> backgroundProperty();
    ReadOnlyStringProperty arenaProperty();
}
