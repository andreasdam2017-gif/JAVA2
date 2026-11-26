package entities.doodads;

import entities.Entity;
import utilities.Position;
import javafx.scene.paint.Color;

public abstract class Destructible extends Entity {
    public Destructible(Position position, double width, double height, Color color) {
        super(position, width, height, color);
    }

    public void destroy() {
        remove();
    }
}
