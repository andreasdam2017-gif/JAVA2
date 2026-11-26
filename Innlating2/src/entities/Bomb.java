package entities;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.paint.Color;
import utilities.Position;

public class Bomb extends Entity implements TimedEntity {
    private final Player owner;
    private final int range;
    private double timeRemaining = 2.0;
    private final Set<Player> leavingPlayers = new HashSet<>();
    private final Set<Player> damagedPlayers = new HashSet<>();

    // Players create bombs through placeBomb() so capacity is counted correctly.
    Bomb(Position position, double size, Player owner, int range) {
        super(position, size, size, Color.BLACK);
        this.owner = owner;
        this.range = range;
    }

    public void allowLeaving(Player player) {
        leavingPlayers.add(player);
    }

    public boolean canLeave(Player player) {
        return leavingPlayers.contains(player);
    }

    public void finishLeaving(Player player) {
        leavingPlayers.remove(player);
    }

    public Player getOwner() {
        return owner;
    }

    public int getRange() {
        return range;
    }

    public double getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public void update(double dt) {
        if (!isRemoved()) {
            timeRemaining = Math.max(0, timeRemaining - dt);
        }
    }

    @Override
    public boolean isExpired() {
        return timeRemaining <= 0.000000001;
    }

    public void trigger() {
        timeRemaining = 0;
    }

    // All explosion tiles from this bomb share the same damage history.
    boolean markPlayerHit(Player player) {
        return damagedPlayers.add(player);
    }

    @Override
    public void remove() {
        if (!isRemoved()) {
            super.remove();
            owner.bombFinished();
        }
    }

    @Override
    public boolean isOccupying() {
        return !isRemoved();
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Explosion && !entity.isRemoved()) {
            trigger();
        }
    }
}
