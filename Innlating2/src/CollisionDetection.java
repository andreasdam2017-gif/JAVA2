import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import entities.Entity;

public class CollisionDetection implements AutoCloseable {
    private final ExecutorService workers = Executors.newFixedThreadPool(2);

    // Workers only read these copied numbers, never the live game objects.
    private record Bounds(int index, double x, double y, double width, double height) { }
    public record Pair(int first, int second) { }

    public static boolean overlaps(Entity a, Entity b) {
        return overlaps(a.getPositionX(), a.getPositionY(), a.getWidth(), a.getHeight(),
                b.getPositionX(), b.getPositionY(), b.getWidth(), b.getHeight());
    }

    private static boolean overlaps(double ax, double ay, double aw, double ah,
                                    double bx, double by, double bw, double bh) {
        boolean overlapsHorizontally = ax < bx + bw && ax + aw > bx;
        boolean overlapsVertically = ay < by + bh && ay + ah > by;
        return overlapsHorizontally && overlapsVertically;
    }

    public List<Pair> findCollisions(List<Entity> entities) {
        List<Bounds> snapshot = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (!entity.isRemoved()) {
                snapshot.add(new Bounds(i, entity.getPositionX(), entity.getPositionY(),
                        entity.getWidth(), entity.getHeight()));
            }
        }

        int middle = snapshot.size() / 2;
        Callable<List<Pair>> firstHalf = () -> checkPairs(snapshot, 0, middle);
        Callable<List<Pair>> secondHalf = () -> checkPairs(snapshot, middle, snapshot.size());
        List<Callable<List<Pair>>> tasks = List.of(firstHalf, secondHalf);
        List<Pair> collisions = new ArrayList<>();
        try {
            // Wait for both batches before the game thread changes any state.
            List<Future<List<Pair>>> results = workers.invokeAll(tasks);
            for (Future<List<Pair>> result : results) {
                List<Pair> detectedPairs = result.get();
                collisions.addAll(detectedPairs);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Collision checking interrupted", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Collision checking failed", exception.getCause());
        }
        return collisions;
    }

    private List<Pair> checkPairs(List<Bounds> snapshot, int start, int end) {
        List<Pair> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            Bounds a = snapshot.get(i);
            for (int j = i + 1; j < snapshot.size(); j++) {
                Bounds b = snapshot.get(j);
                if (overlaps(a.x(), a.y(), a.width(), a.height(),
                        b.x(), b.y(), b.width(), b.height())) {
                    result.add(new Pair(a.index(), b.index()));
                }
            }
        }
        return result;
    }

    @Override
    public void close() {
        workers.shutdownNow();
    }
}
