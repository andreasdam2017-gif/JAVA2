import java.util.ArrayList;
import java.util.List;
import entities.Entity;
import entities.Player;
import entities.Bomb;
import entities.doodads.*;
import javafx.scene.paint.Color;
import utilities.Assets;
import utilities.Position;

public class Arena {
    public static final int TILE_SIZE = 32;
    public static final int COLUMNS = 13;
    public static final int ROWS = 11;

    private final List<Entity> floors = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final Player player1 = new Player(new Position(36, 36), 24, Color.BLUE);
    private final Player player2 = new Player(new Position(356, 292), 24, Color.RED);

    public Arena() {
        this("Classic");
    }

    public Arena(String layout) {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Position position = new Position(column * TILE_SIZE, row * TILE_SIZE);
                EmptyTile floor = new EmptyTile(position, TILE_SIZE);
                floor.setImage(Assets.image("terrain/grass.png"));
                floors.add(floor);

                boolean boundary = row == 0 || column == 0 || row == ROWS - 1 || column == COLUMNS - 1;
                boolean pillar = row % 2 == 0 && column % 2 == 0;
                if (boundary || pillar) {
                    Wall wall = new Wall(position, TILE_SIZE);
                    wall.setImage(Assets.image("terrain/wall.png"));
                    entities.add(wall);
                } else if (!isStartingArea(column, row) && hasCrate(layout, column, row)) {
                    DWall crate = new DWall(position, TILE_SIZE);
                    crate.setImage(Assets.image("terrain/crate.png"));
                    entities.add(crate);
                }
            }
        }
        player1.setImage(Assets.image("players/blue_down_0.png"));
        player2.setImage(Assets.image("players/red_down_0.png"));
        entities.add(player1);
        entities.add(player2);
    }

    private boolean isStartingArea(int column, int row) {
        // Clear room to turn a corner and escape the first bomb's blast.
        return (column <= 3 && row <= 3) || (column >= 9 && row >= 7);
    }

    private boolean hasCrate(String layout, int column, int row) {
        if (layout.equals("Forest")) {
            return (column + row) % 3 != 0;
        }
        if (layout.equals("Desert")) {
            return (column * row) % 3 == 0;
        }
        return true;
    }

    public boolean isInside(int column, int row) {
        return column >= 0 && column < COLUMNS && row >= 0 && row < ROWS;
    }

    public Entity blockerAt(int column, int row) {
        for (Entity entity : entities) {
            if (entity.isRemoved()) {
                continue;
            }
            if (!(entity instanceof Wall || entity instanceof DWall || entity instanceof Bomb)) {
                continue;
            }
            int entityColumn = (int) (entity.getPositionX() / TILE_SIZE);
            int entityRow = (int) (entity.getPositionY() / TILE_SIZE);
            if (entityColumn == column && entityRow == row) {
                return entity;
            }
        }
        return null;
    }

    public void movePlayer(Player player, double directionX, double directionY, double dt) {
        double length = Math.hypot(directionX, directionY);
        if (player.isDead() || length == 0) {
            return;
        }
        double dx = directionX / length * player.getSpeed() * dt;
        double dy = directionY / length * player.getSpeed() * dt;
        // Test small steps so a fast player cannot jump through a wall.
        double longestDistance = Math.max(Math.abs(dx), Math.abs(dy));
        int steps = (int) Math.ceil(longestDistance / 4);
        steps = Math.max(1, steps);
        for (int step = 0; step < steps; step++) {
            double oldX = player.getPositionX();
            player.move(dx / steps, 0);
            if (isBlocked(player)) {
                player.getPosition().setX(oldX);
            }
            double oldY = player.getPositionY();
            player.move(0, dy / steps);
            if (isBlocked(player)) {
                player.getPosition().setY(oldY);
            }
            for (Entity entity : entities) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (!CollisionDetection.overlaps(player, bomb)) {
                        bomb.finishLeaving(player);
                    }
                }
            }
        }
    }

    private boolean isBlocked(Player player) {
        for (Entity other : entities) {
            if (other == player || other.isRemoved() || !other.isOccupying()) {
                continue;
            }
            if (other instanceof Bomb && ((Bomb) other).canLeave(player)) {
                continue;
            }
            if (CollisionDetection.overlaps(player, other)) {
                return true;
            }
        }
        return false;
    }

    public int getWidth() {
        return COLUMNS * TILE_SIZE;
    }

    public int getHeight() {
        return ROWS * TILE_SIZE;
    }

    public List<Entity> getFloors() {
        return floors;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
