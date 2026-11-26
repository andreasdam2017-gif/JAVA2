package entities.powerups;

import entities.Player;
import javafx.scene.paint.Color;
import utilities.Position;

public class RangePowerUp extends PowerUp {
    public RangePowerUp(Position position, double size) {
        super(position, size, Color.PURPLE);
    }

    @Override
    protected void apply(Player player) {
        player.increaseRange();
    }
}
