import java.util.function.DoubleConsumer;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private final DoubleConsumer update;
    private final Runnable render;
    private long lastTime;

    public GameLoop(DoubleConsumer update, Runnable render) {
        this.update = update;
        this.render = render;
    }

    @Override
    public void start() {
        lastTime = 0;
        super.start();
    }

    @Override
    public void handle(long now) {
        double dt = 0;
        if (lastTime != 0) {
            double elapsedSeconds = (now - lastTime) / 1_000_000_000.0;
            dt = Math.min(elapsedSeconds, 0.05);
        }

        lastTime = now;
        update.accept(dt);
        render.run();
    }
}
