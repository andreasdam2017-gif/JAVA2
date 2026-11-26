import java.util.*;
import entities.*;
import entities.doodads.*;
import entities.powerups.*;
import javafx.scene.input.KeyCode;
import utilities.Assets;
import utilities.Position;

public class GameController implements AutoCloseable {
    private final Arena arena;
    private final CollisionDetection collisions = new CollisionDetection();
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final List<Player> bombRequests = new ArrayList<>();
    private final Random random;
    private String result = "";
    private boolean closed;

    public GameController(Arena arena) {
        this(arena, new Random());
    }

    GameController(Arena arena, Random random) {
        this.arena = arena;
        this.random = random;
    }

    public String getResult() {
        return result;
    }

    public boolean isClosed() {
        return closed;
    }

    public void keyPressed(KeyCode key) {
        if (closed || !result.isEmpty()) {
            return;
        }
        if (pressedKeys.contains(key)) {
            return;
        }

        pressedKeys.add(key);

        if (key == KeyCode.SPACE) {
            bombRequests.add(arena.getPlayer1());
        }
        if (key == KeyCode.ENTER) {
            bombRequests.add(arena.getPlayer2());
        }
    }

    public void keyReleased(KeyCode key) {
        pressedKeys.remove(key);
    }

    public void clearInput() {
        pressedKeys.clear();
        bombRequests.clear();
    }

    private int getDirection(KeyCode positiveKey, KeyCode negativeKey) {
        int direction = 0;

        if (pressedKeys.contains(positiveKey)) {
            direction++;
        }
        if (pressedKeys.contains(negativeKey)) {
            direction--;
        }

        return direction;
    }

    public void update(double dt) {
        if (closed || !result.isEmpty()) {
            return;
        }
        // Place at the position where the key was pressed, before this frame's move.
        for (Player player : bombRequests) {
            placeBomb(player);
        }
        bombRequests.clear();

        int blueDirectionX = getDirection(KeyCode.D, KeyCode.A);
        int blueDirectionY = getDirection(KeyCode.S, KeyCode.W);
        int redDirectionX = getDirection(KeyCode.RIGHT, KeyCode.LEFT);
        int redDirectionY = getDirection(KeyCode.DOWN, KeyCode.UP);

        arena.movePlayer(arena.getPlayer1(), blueDirectionX, blueDirectionY, dt);
        arena.movePlayer(arena.getPlayer2(), redDirectionX, redDirectionY, dt);

        for (Entity entity : arena.getEntities()) {
            if (!entity.isRemoved()) {
                entity.update(dt);
            }
        }

        // A collision may trigger another bomb. Finish the whole chain before
        // deciding the winner, so simultaneous deaths can produce a draw.
        boolean anotherBomb;
        do {
            explodeReadyBombs();
            applyInteractions();
            anotherBomb = false;
            for (Entity entity : arena.getEntities()) {
                if (entity instanceof Bomb && !entity.isRemoved()) {
                    Bomb bomb = (Bomb) entity;
                    if (bomb.isExpired()) {
                        anotherBomb = true;
                        break;
                    }
                }
            }
        } while (anotherBomb);

        spawnPowerUps();
        List<Entity> entities = arena.getEntities();
        for (int index = entities.size() - 1; index >= 0; index--) {
            if (entities.get(index).isRemoved()) {
                entities.remove(index);
            }
        }
        checkWinner();
    }

    public void placeBomb(Player player) {
        if (closed || !result.isEmpty()) {
            return;
        }

        double centerX = player.getPositionX() + player.getWidth() / 2;
        double centerY = player.getPositionY() + player.getHeight() / 2;
        int column = (int) (centerX / Arena.TILE_SIZE);
        int row = (int) (centerY / Arena.TILE_SIZE);

        if (!arena.isInside(column, row)) {
            return;
        }
        if (arena.blockerAt(column, row) != null) {
            return;
        }

        Bomb bomb = player.placeBomb(Arena.TILE_SIZE);
        if (bomb == null) {
            return;
        }
        bomb.setImage(Assets.image("bombs/bomb_0.png"));
        for (Entity entity : arena.getEntities()) {
            if (entity instanceof Player && CollisionDetection.overlaps(entity, bomb)) {
                bomb.allowLeaving((Player) entity);
            }
        }
        arena.getEntities().add(bomb);
    }

    private void explodeReadyBombs() {
        Queue<Bomb> queue = new ArrayDeque<>();
        for (Entity entity : arena.getEntities()) {
            if (entity instanceof Bomb && !entity.isRemoved()) {
                Bomb bomb = (Bomb) entity;
                if (bomb.isExpired()) {
                    queue.add(bomb);
                }
            }
        }
        while (!queue.isEmpty()) {
            Bomb bomb = queue.remove();
            if (bomb.isRemoved()) {
                continue;
            }
            bomb.remove();
            int column = (int) (bomb.getPositionX() / Arena.TILE_SIZE);
            int row = (int) (bomb.getPositionY() / Arena.TILE_SIZE);
            addExplosion(column, row, bomb);
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] direction : directions) {
                for (int step = 1; step <= bomb.getRange(); step++) {
                    int x = column + direction[0] * step;
                    int y = row + direction[1] * step;
                    if (!arena.isInside(x, y)) {
                        break;
                    }
                    Entity blocker = arena.blockerAt(x, y);
                    if (blocker instanceof Wall) {
                        break;
                    }
                    Explosion explosion = addExplosion(x, y, bomb);
                    if (blocker instanceof DWall) {
                        explosion.interact(blocker);
                        break;
                    }
                    if (blocker instanceof Bomb) {
                        explosion.interact(blocker);
                        queue.add((Bomb) blocker);
                        break;
                    }
                }
            }
        }
    }

    private Explosion addExplosion(int column, int row, Bomb source) {
        Position position = new Position(column * Arena.TILE_SIZE, row * Arena.TILE_SIZE);
        Explosion explosion = new Explosion(position, Arena.TILE_SIZE, source);
        explosion.setImage(Assets.image("explosions/explosion_3.png"));
        arena.getEntities().add(explosion);
        return explosion;
    }

    private void applyInteractions() {
        List<Entity> entities = arena.getEntities();
        for (CollisionDetection.Pair pair : collisions.findCollisions(entities)) {
            Entity first = entities.get(pair.first());
            Entity second = entities.get(pair.second());
            if (first.isRemoved() || second.isRemoved()) {
                continue;
            }
            // Only the game thread calls methods that change entities.
            if (first instanceof Explosion) {
                first.interact(second);
            } else if (second instanceof Explosion) {
                second.interact(first);
            } else if (first instanceof PowerUp && second instanceof Player) {
                first.interact(second);
            } else if (second instanceof PowerUp && first instanceof Player) {
                second.interact(first);
            }
        }
    }

    private void spawnPowerUps() {
        List<Entity> drops = new ArrayList<>();
        for (Entity entity : arena.getEntities()) {
            if (!(entity instanceof DWall)) {
                continue;
            }
            if (!entity.isRemoved()) {
                continue;
            }
            if (random.nextDouble() >= 0.35) {
                continue;
            }

            Position position = new Position(entity.getPositionX(), entity.getPositionY());
            PowerUp powerUp;
            String image;
            int powerUpType = random.nextInt(4);

            switch (powerUpType) {
                case 0:
                    powerUp = new CapacityPowerUp(position, Arena.TILE_SIZE);
                    image = "capacity";
                    break;
                case 1:
                    powerUp = new RangePowerUp(position, Arena.TILE_SIZE);
                    image = "range";
                    break;
                case 2:
                    powerUp = new SpeedPowerUp(position, Arena.TILE_SIZE);
                    image = "speed";
                    break;
                default:
                    powerUp = new LifePowerUp(position, Arena.TILE_SIZE);
                    image = "life";
                    break;
            }
            powerUp.setImage(Assets.image("powerups/" + image + ".png"));
            drops.add(powerUp);
        }
        arena.getEntities().addAll(drops);
    }

    private void checkWinner() {
        boolean blueDead = arena.getPlayer1().isDead();
        boolean redDead = arena.getPlayer2().isDead();
        if (blueDead && redDead) {
            result = "Draw!";
        } else if (blueDead) {
            result = "Red wins!";
        } else if (redDead) {
            result = "Blue wins!";
        }

        if (!result.isEmpty()) {
            clearInput();
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        clearInput();
        collisions.close();
    }
}
