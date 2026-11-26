package entities;

import javafx.scene.paint.Color;
import utilities.Position;

public class Explosion extends Entity implements TimedEntity {
    private final Bomb source;
    private double timeRemaining = 0.5;

    public Explosion(Position position, double tileSize, Bomb source) {
        super(position, tileSize, tileSize, Color.ORANGE);
        this.source = source;
    }

    @Override
    public void update(double dt) {
        timeRemaining = Math.max(0, timeRemaining - dt);
        if (isExpired()) {
            remove();
        }
    }

    @Override
    public boolean isExpired() {
        return timeRemaining <= 0.000000001;
    }

    public void damage(Player player) {
        if (!isRemoved() && !player.isDead() && source.markPlayerHit(player)) {
            player.takeDamage();
        }
    }

    @Override
    public boolean isOccupying() {
        return false;
    }

    @Override
    public void interact(Entity entity) {
        if (!isRemoved() && !(entity instanceof Explosion)) {
            entity.interact(this);
        }
    }
}
