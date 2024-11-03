package co.edu.uptc.pojos;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ufo {
    
    private Point position; 
    private double speed;
    private double angle; 
    private boolean moving; 

    public Ufo(Point position, double speed, double angle){
        this.position = position;
        this.speed = speed;
        this.angle = angle;
        this.moving = true;
    }

    public void stop() {
        this.moving = false; 
    }
}
