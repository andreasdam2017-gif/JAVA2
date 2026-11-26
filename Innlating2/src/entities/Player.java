package entities;

import entities.powerups.PowerUp;
import javafx.scene.paint.Color;
import utilities.Position;

public class Player extends MovableEntity {
    private int lives = 3;
    private int maxBombs = 1;
    private int activeBombs = 0;
    private int bombRange = 2;
    private double speed = 120;

    public Player(Position position, double size, Color color) {
        super(position, size, size, color);
    }

    public int getLives() {
        return lives;
    }

    public int getMaxBombs() {
        return maxBombs;
    }

    public int getActiveBombs() {
        return activeBombs;
    }

    public int getBombRange() {
        return bombRange;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isDead() {
        return lives <= 0;
    }

    public void increaseCapacity() {
        maxBombs++;
    }

    public void increaseRange() {
        bombRange++;
    }

    public void increaseSpeed() {
        speed += 20;
    }

    public void addLife() {
        if (!isDead()) {
            lives++;
        }
    }

    public void takeDamage() {
        if (!isDead()) {
            lives--;
        }
    }

    // The caller checks that the tile is free and adds the bomb to the arena.
    public Bomb placeBomb(double tileSize) {
        if (isDead() || activeBombs >= maxBombs) {
            return null;
        }
        double centerX = getPositionX() + width / 2;
        double centerY = getPositionY() + height / 2;
        double x = Math.floor(centerX / tileSize) * tileSize;
        double y = Math.floor(centerY / tileSize) * tileSize;
        activeBombs++;
        return new Bomb(new Position(x, y), tileSize, this, bombRange);
    }

    void bombFinished() {
        activeBombs--;
    }

    @Override
    public boolean isOccupying() {
        return !isDead();
    }

    @Override
    public void interact(Entity entity) {
        if (entity instanceof Explosion) {
            ((Explosion) entity).damage(this);
        } else if (entity instanceof PowerUp) {
            entity.interact(this);
        }
    }
}
