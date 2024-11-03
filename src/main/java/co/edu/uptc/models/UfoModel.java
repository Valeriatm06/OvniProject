package co.edu.uptc.models;

import lombok.Getter;
import lombok.Setter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.edu.uptc.interfaces.UfoInterface;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.UtilThread;

@Getter
@Setter
public class UfoModel implements UfoInterface.Model {

    private UfoInterface.Presenter presenter;
    private List<Ufo> ufos;
    private Thread movementThread;
    private boolean running;
    private int totalCrashedCount = 0;
    private int totalArrivedCount = 0;

    public UfoModel() {
        this.ufos = new ArrayList<>();
        this.running = false;
    }

    @Override
    public void startGame(int ufoNumber, double speed) {
        ufos.clear();
        Random random = new Random();
        for (int i = 0; i < ufoNumber; i++) {
            Point initialPosition = new Point(random.nextInt(getAreaWidth()), random.nextInt(getAreaHeight()));
            double angle = random.nextDouble() * 360;
            ufos.add(new Ufo(initialPosition, speed, angle));
        }
        running = true;
        startMovementThread();
    }

    public void startMovementThread() {
        movementThread = new Thread(() -> {
            while (running) {
                upDateUfoPositions();
                UtilThread.sleep(100);
            }
        });
        movementThread.start();
    }

    public void upDateUfoPositions() {
        for (Ufo ufo : ufos) {
            if (ufo.isMoving()) {
                Point position = ufo.getPosition();
                double speed = ufo.getSpeed();
                double angle = Math.toRadians(ufo.getAngle());
    
                // Calcular nueva posición
                position.x += speed * Math.cos(angle);
                position.y += speed * Math.sin(angle);
                ufo.setPosition(position);
    
                // Comprobar si el OVNI está fuera de límites después de moverlo
                if (position.x < 0 || position.x > getAreaWidth() || position.y < 0 || position.y > getAreaHeight()) {
                    System.out.println("OVNI fuera de límites después de mover: " + ufo + " en posición: " + position);
                    ufo.stop(); // Detener el OVNI si sale de los límites
                } else {
                    System.out.println("OVNI en movimiento: " + ufo + " en posición: " + position);
                }
            }
        }
        checkCollisions(); // Verificar colisiones después de actualizar posiciones
        countMovingUfos(); // Contar OVNIs en movimiento
        presenter.updateUfos(ufos); // Actualizar la interfaz
    }
    

    public void stopGame() {
        running = false;
        if (movementThread != null) {
            movementThread.interrupt();
        }
    }

    public int[] checkCollisions() {
        List<Ufo> toRemove = new ArrayList<>();
        int crashedCount = 0;
        int arrivedCount = 0;
    
        for (Ufo ufo : new ArrayList<>(ufos)) {
            Point position = ufo.getPosition();
    
            // Verificar límites
            if (position.x < 0 || position.x > getAreaWidth() || position.y < 0 || position.y > getAreaHeight()) {
                System.out.println("OVNI fuera de límites: " + ufo + " en posición: " + position);
                if (!toRemove.contains(ufo)) {
                    toRemove.add(ufo);
                    crashedCount++;
                    totalCrashedCount++;
                    ufo.stop();
                }
                continue; 
            }
    
            // Verificar colisiones
            for (Ufo other : ufos) {
                if (ufo != other && areColliding(ufo, other)) {
                    System.out.println("Colisión detectada entre OVNI: " + ufo + " y OVNI: " + other);
                    if (!toRemove.contains(ufo)) {
                        toRemove.add(ufo);
                        crashedCount++;
                        totalCrashedCount++;
                        ufo.stop();
                    }
                    if (!toRemove.contains(other)) {
                        toRemove.add(other);
                        crashedCount++;
                        totalCrashedCount++;
                        other.stop();
                    }
                }
            }
    
            // Verificar llegada
            if (isInArrivalArea(position)) {
                System.out.println("OVNI ha llegado a la zona de llegada: " + ufo + " en posición: " + position);
                if (!toRemove.contains(ufo)) {
                    toRemove.add(ufo);
                    arrivedCount++;
                    totalArrivedCount++;
                }
            }
        }
    
        ufos.removeAll(toRemove);
        presenter.updateScore(totalCrashedCount);
        presenter.updateArrival(arrivedCount);
        return new int[]{crashedCount, arrivedCount}; 
    }
    
    private boolean areColliding(Ufo ufo1, Ufo ufo2) {
        Point pos1 = ufo1.getPosition();
        Point pos2 = ufo2.getPosition();
        int collisionDistance = 40;
        boolean collision = pos1.distance(pos2) < collisionDistance; 
        return collision; 
    }
    

    public void countMovingUfos() {
        int count = 0;
        for (Ufo ufo : ufos) {
            if (ufo.isMoving()) {
                count++;
            }
        }
        presenter.countMovingUfos(count);
    }

    private boolean isInArrivalArea(Point position) {
        return position.x >= getArrivalAreaX() && position.x <= getArrivalAreaX() + getArrivalAreaWidth() &&
               position.y >= getArrivalAreaY() && position.y <= getArrivalAreaY() + getArrivalAreaHeigth();
    }
    

    private int getAreaWidth() {
        return presenter.areaSize()[0];
    }

    private int getAreaHeight() {
        return presenter.areaSize()[1];
    }

    private int getArrivalAreaX(){
        return presenter.destinationAreaSize()[0];
    }

    private int getArrivalAreaY(){
        return presenter.destinationAreaSize()[1];
    }

    private int getArrivalAreaWidth(){
        return presenter.destinationAreaSize()[2];
    }

    private int getArrivalAreaHeigth(){
        return presenter.destinationAreaSize()[3];
    }

    @Override
    public void setPresenter(UfoInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public List<Ufo> getUfosList() {
        return ufos;
    }
}
