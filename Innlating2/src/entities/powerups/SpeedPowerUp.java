package entities.powerups;

import entities.Player;
import javafx.scene.paint.Color;
import utilities.Position;

public class SpeedPowerUp extends PowerUp {
    public SpeedPowerUp(Position position, double size) {
        super(position, size, Color.YELLOW);
    }

    @Override
    protected void apply(Player player) {
        player.increaseSpeed();
    }
}
