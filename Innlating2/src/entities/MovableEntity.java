package entities;

import utilities.Position;
import javafx.scene.paint.Color;

public abstract class MovableEntity extends Entity {
    public MovableEntity(Position position, double width, double height, Color color) {
        super(position, width, height, color);
    }

    public void move(double dx, double dy) {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
    }
}
