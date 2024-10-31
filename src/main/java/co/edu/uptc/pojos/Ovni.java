package co.edu.uptc.pojos;

import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Setter
@Getter
public class Ovni {
    
    private Point positionPoint;
    private Point destinationPoint;
    private int speed;
    private double angle;
    private boolean isMoving;
    private boolean hasCollided;
    private boolean reachedDestination;

    public Ovni(Point initialPosition, int speed, double angle) {
        this.positionPoint = initialPosition;
        this.speed = speed;
        this.angle = angle;
        this.isMoving = true;
        this.hasCollided = false;
        this.reachedDestination = false;
    } 

}
