package entities.powerups;

import entities.Player;
import javafx.scene.paint.Color;
import utilities.Position;

public class CapacityPowerUp extends PowerUp {
    public CapacityPowerUp(Position position, double size) {
        super(position, size, Color.BLUE);
    }

    @Override
    protected void apply(Player player) {
        player.increaseCapacity();
    }
}
