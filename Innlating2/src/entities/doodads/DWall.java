package entities.doodads;

import entities.Entity;
import entities.Explosion;
import utilities.Position;
import javafx.scene.paint.Color;

public class DWall extends Destructible {
    public DWall(Position position, double tileSize) {
        super(position, tileSize, tileSize, Color.SADDLEBROWN);
    }

    @Override
    public boolean isOccupying() {
        return !isRemoved();
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Explosion && !entity.isRemoved()) {
            destroy();
        }
    }
}
