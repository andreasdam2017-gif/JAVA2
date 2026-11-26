package entities.doodads;

import entities.Entity;
import utilities.Position;
import javafx.scene.paint.Color;

public class EmptyTile extends Entity {
    public EmptyTile(Position position, double tileSize) {
        super(position, tileSize, tileSize, Color.DARKSEAGREEN);
    }

    @Override
    public boolean isOccupying() {
        return false;
    }

    @Override
    public void interact(Entity entity) {
    }
}
