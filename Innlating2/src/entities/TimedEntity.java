package entities;

public interface TimedEntity {
    // dt is the number of seconds since the previous frame.
    void update(double dt);
    boolean isExpired();
}
