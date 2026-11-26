package entities.powerups;

import entities.Entity;
import entities.Player;
import javafx.scene.paint.Color;
import utilities.Position;

public abstract class PowerUp extends Entity {
    public PowerUp(Position position, double size, Color color) {
        super(position, size, size, color);
    }

    protected abstract void apply(Player player);

    @Override
    public boolean isOccupying() {
        return false;
    }

    @Override
    public void interact(Entity entity) {
        if (!isRemoved() && entity instanceof Player) {
            Player player = (Player) entity;
            if (!player.isDead()) {
                apply(player);
                remove();
            }
        }
    }
}
