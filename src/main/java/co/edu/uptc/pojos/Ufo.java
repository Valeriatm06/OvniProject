package co.edu.uptc.pojos;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ufo {
    private Point position;
    private double speed; 
    private double angle; 
    private boolean moving; 
    private List<Point> trajectory; // Lista para almacenar la trayectoria

    public Ufo(Point position, double speed, double angle) {
        this.position = position;
        this.speed = speed;
        this.angle = angle;
        this.moving = true; 
        this.trajectory = new ArrayList<>(); // Inicializar la trayectoria
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isMoving() {
        return moving;
    }

    public void start() {
        moving = true; 
    }

    public void stop() {
        moving = false; 
    }

    public boolean isStopped() {
        return !moving; 
    }

    public void addTrajectoryPoint(Point point) {
        trajectory.add(point);
    }

    public List<Point> getTrajectory() {
        return trajectory;
    }

    public void clearTrajectory() {
        trajectory.clear();
    }

    @Override
    public String toString() {
        return "Ufo{" +
                "position=" + position +
                ", speed=" + speed +
                ", angle=" + angle +
                ", moving=" + moving +
                ", trajectory=" + trajectory +
                '}';
    }
}
