package entities.doodads;

import entities.Entity;
import utilities.Position;
import javafx.scene.paint.Color;

public class Wall extends Entity {
    public Wall(Position position, double tileSize) {
        super(position, tileSize, tileSize, Color.DARKGRAY);
    }

    @Override
    public boolean isOccupying() {
        return true;
    }

    @Override
    public void interact(Entity entity) {

    }
}
