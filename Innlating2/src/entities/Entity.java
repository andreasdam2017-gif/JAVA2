package entities;

import utilities.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Entity {
    protected Position position;
    protected double width;
    protected double height;
    protected Image image;
    protected Color color;
    private boolean removed;

    public Entity(Position position, double width, double height, Color color) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public double getPositionX() {
        return position.getX();
    }

    public double getPositionY() {
        return position.getY();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void remove() {
        removed = true;
    }

    public abstract boolean isOccupying();
    public abstract void interact(Entity entity);

    public void update(double dt) {
        // Stationary entities do not need an update each frame.
    }

    public void render(GraphicsContext gc) {
        if (removed) {
            return;
        }

        if (image != null) {
            gc.drawImage(image, getPositionX(), getPositionY(), width, height);
        } else {
            gc.setFill(color);
            gc.fillRect(getPositionX(), getPositionY(), width, height);
        }
    }
}
