package utilities;

import java.net.URL;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public final class Assets {
    private static final Map<String, Image> images = new HashMap<>();

    private Assets() { }

    public static Image image(String name) {
        if (!images.containsKey(name)) {
            URL resource = Assets.class.getResource("/img/" + name);
            String location;
            if (resource == null) {
                File imageFile = new File("img/" + name);
                location = imageFile.toURI().toString();
            } else {
                location = resource.toExternalForm();
            }

            Image image = new Image(location);
            if (image.isError()) {
                throw new IllegalStateException("Cannot load " + name, image.getException());
            }
            images.put(name, image);
        }
        return images.get(name);
    }
}
