package co.edu.uptc.models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import co.edu.uptc.interfaces.OvniInterface;
import co.edu.uptc.pojos.Ovni;
import co.edu.uptc.utilities.UtilThread;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OvniModel implements OvniInterface.Model {

    private OvniInterface.Presenter presenter;
    private List<Ovni> ovnis;
    private List<Point> userPath;
    private boolean isFollowingPath;
    private int areaWidth; 
    private int areaHeight;
    private int defaultSpeed;
    public static final int DESTINATION_RADIUS = 30;
    public static final int COLLISION_RADIUS = 5;

    public OvniModel(int numberOfOvnis, int defaultSpeed, int areaWidth, int areaHeight) {
        this.ovnis = new ArrayList<>();
        this.userPath = new ArrayList<>();
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.defaultSpeed = defaultSpeed;
        moveAllOvnis();  // Iniciar el movimiento de OVNIs en el constructor
    }

    public void initOvnis(int numberOfOvnis, int appearance) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < numberOfOvnis; i++) {
                Point randomPosition = new Point((int) (Math.random() * areaWidth), (int) (Math.random() * areaHeight));
                double angle = Math.random() * 360;
                Ovni ovni = new Ovni(randomPosition, defaultSpeed, angle);
                ovnis.add(ovni);
                UtilThread.sleep(appearance);
            }
        });
        thread.start();
    }

    public void moveAllOvnis() {
        Thread thread = new Thread(() -> {
            while (true) {
                List<Ovni> toRemove = new CopyOnWriteArrayList<>(); // para evitar conflictos de concurrencia
                
                for (Ovni ovni : ovnis) {
                    if (ovni.isMoving() && !ovni.isHasCollided() && !ovni.isReachedDestination()) {
                        updatePosition(ovni);
                    }
                    
                    if (ovni.isHasCollided() || ovni.isReachedDestination()) {
                        toRemove.add(ovni);
                    }
                }

                checkOvniCollisions(toRemove);

                ovnis.removeAll(toRemove);
                UtilThread.sleep(30);
            }
        });
        thread.start();
    }

    private void updatePosition(Ovni ovni) {
        if (isFollowingPath && userPath != null && !userPath.isEmpty()) {
            followUserPath(ovni);
        } else {
            moveInDirection(ovni);
        }
        checkCollisions(ovni);
        checkIfReachedDestination(ovni);
    }

    private void moveInDirection(Ovni ovni) {
        int dx = (int) (ovni.getSpeed() * Math.cos(Math.toRadians(ovni.getAngle())));
        int dy = (int) (ovni.getSpeed() * Math.sin(Math.toRadians(ovni.getAngle())));
        Point newPosition = ovni.getPositionPoint();
        newPosition.translate(dx, dy);
        ovni.setPositionPoint(newPosition);
    }

    private void followUserPath(Ovni ovni) {
        if (!userPath.isEmpty()) {
            Point nextPoint = userPath.remove(0);
            ovni.setPositionPoint(nextPoint);
        } else {
            isFollowingPath = false;
        }
    }

    private void checkCollisions(Ovni ovni) {
        Point position = ovni.getPositionPoint();
        if (position.x <= 0 || position.x >= areaWidth || position.y <= 0 || position.y >= areaHeight) {
            ovni.setHasCollided(true);
            ovni.setMoving(false);
        }
    }

    private void checkOvniCollisions(List<Ovni> toRemove) {
        for (int i = 0; i < ovnis.size(); i++) {
            for (int j = i + 1; j < ovnis.size(); j++) {
                Ovni ovniOne = ovnis.get(i);
                Ovni ovniTwo = ovnis.get(j);
                if (ovniOne.getPositionPoint().distance(ovniTwo.getPositionPoint()) <= COLLISION_RADIUS) {
                    ovniOne.setHasCollided(true);
                    ovniTwo.setHasCollided(true);
                    toRemove.add(ovniOne);
                    toRemove.add(ovniTwo);
                }
            }
        }
    }

    public int getMovingOvniCount() {
        int count = 0;
        for (Ovni ovni : ovnis) {
            if (ovni.isMoving()) count++;
        }
        return count;
    }

    public int getCrashedOvniCount() {
        int count = 0;
        for (Ovni ovni : ovnis) {
            if (ovni.isHasCollided()) count++;
        }
        return count;
    }

    private void checkIfReachedDestination(Ovni ovni) {
        Point position = ovni.getPositionPoint();
        double distance = position.distance(ovni.getDestinationPoint());
        if (distance <= DESTINATION_RADIUS) {
            ovni.setReachedDestination(true);
            ovni.setMoving(false);
        }
    }

    public void setIndividualSpeed(int ovniIndex, int speed) {
        if (ovniIndex >= 0 && ovniIndex < ovnis.size()) {
            ovnis.get(ovniIndex).setSpeed(speed);
        }
    }

    public void setDefaultSpeed(int speed) {
        this.defaultSpeed = speed;
        for (Ovni ovni : ovnis) {
            ovni.setSpeed(speed);
        }
    }

    public void setUserPath(List<Point> path) {
        this.userPath = path;
        this.isFollowingPath = true;
    }

    public List<Ovni> getOvnis() {
        return ovnis;
    }

    @Override
    public void setPresenter(OvniInterface.Presenter presenter) {
        this.presenter = presenter;
    }
}
