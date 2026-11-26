package entities.powerups;

import entities.Player;
import javafx.scene.paint.Color;
import utilities.Position;

public class LifePowerUp extends PowerUp {
    public LifePowerUp(Position position, double size) {
        super(position, size, Color.RED);
    }

    @Override
    protected void apply(Player player) {
        player.addLife();
    }
}
