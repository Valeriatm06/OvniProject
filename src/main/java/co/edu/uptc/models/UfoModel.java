package co.edu.uptc.models;

import lombok.Getter;
import lombok.Setter;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import co.edu.uptc.interfaces.UfoInterface;
import co.edu.uptc.pojos.Ufo;

@Getter
@Setter
public class UfoModel implements UfoInterface.Model {

    private UfoInterface.Presenter presenter;
    private List<Ufo> ufos;
    private boolean running;
    private Ufo selectedUfo;
    private int totalCrashedCount;
    private int totalArrivedCount;

    public UfoModel() {
        this.ufos = new CopyOnWriteArrayList<>();
        totalCrashedCount = 0;
        totalArrivedCount = 0;
        this.running = false;
    }

    @Override
    public void startGame(int ufoNumber, double speed, int appearanceTime) {
        ufos.clear();
        Random random = new Random();
        running = true;

        // Crear un hilo para manejar la aparición de OVNIs
        new Thread(() -> {
            for (int i = 0; i < ufoNumber; i++) {
                if (!running) break; // Detener si el juego se detiene

                // Crear y agregar el nuevo OVNI
                Point initialPosition = new Point(random.nextInt(getAreaWidth()), random.nextInt(getAreaHeight()));
                double angle = random.nextDouble() * 360;
                Ufo newUfo = new Ufo(initialPosition, speed, angle);
                ufos.add(newUfo);
                presenter.updateUfos(new ArrayList<>(ufos)); // Actualizar la interfaz

                // Comenzar el movimiento del OVNI
                startUfoMovement(newUfo);

                try {
                    Thread.sleep(appearanceTime); // Esperar el tiempo de aparición
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // Salir si se interrumpe
                }
            }
        }).start();
    }

    private void startUfoMovement(Ufo ufo) {
        new Thread(() -> {
            while (running && ufo.isMoving()) {
                updateUfoPosition(ufo);
                checkCollisions(); // Verificar colisiones en cada movimiento
                countMovingUfos(); // Contar OVNIs en movimiento
                try {
                    Thread.sleep(100); // Controla la velocidad de movimiento
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // Salir si se interrumpe
                }
            }
        }).start();
    }

    private void updateUfoPosition(Ufo ufo) {
        if (!ufo.getTrajectory().isEmpty()) {
            Point target = ufo.getTrajectory().get(0);
            double deltaX = target.x - ufo.getPosition().x;
            double deltaY = target.y - ufo.getPosition().y;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance < ufo.getSpeed()) {
                ufo.setPosition(target);
                ufo.getTrajectory().remove(0); // Remover punto alcanzado
            } else {
                double angleToTarget = Math.atan2(deltaY, deltaX);
                Point newPosition = new Point(
                        (int) (ufo.getPosition().x + ufo.getSpeed() * Math.cos(angleToTarget)),
                        (int) (ufo.getPosition().y + ufo.getSpeed() * Math.sin(angleToTarget))
                );
                ufo.setPosition(newPosition);
            }
        } else {
            // Si no hay trayectoria, sigue moviéndose en la dirección del ángulo
            Point position = ufo.getPosition();
            double speed = ufo.getSpeed();
            double angle = Math.toRadians(ufo.getAngle());
            position.x += speed * Math.cos(angle);
            position.y += speed * Math.sin(angle);
            ufo.setPosition(position);
        }

        // Verificar si el OVNI está fuera de límites
        if (isOutOfBounds(ufo.getPosition())) {
            ufo.stop(); // Detener el OVNI si sale de los límites
        }

        presenter.updateUfos(new ArrayList<>(ufos)); // Actualiza la interfaz después de mover
    }

    @Override
public void addTrajectoryPointToUfo(Ufo ufo, Point point) {
    if (ufo != null) {
        List<Point> trajectory = ufo.getTrajectory();
        if (trajectory == null) {
            trajectory = new ArrayList<>();
            ufo.setTrajectory(trajectory);
        }
        trajectory.add(point);
    }
}


    private boolean isOutOfBounds(Point position) {
        return position.x <= 0 || position.x >= getAreaWidth() || position.y <= 0 || position.y >= getAreaHeight();
    }

    public void stopGame() {
        running = false;
    }

    public int[] checkCollisions() {
        int crashedCount = 0;
        int arrivedCount = 0;
    
        synchronized (ufos) {
            List<Ufo> toRemove = new ArrayList<>();
            
            // Primero, contamos los choques y llegadas
            for (Ufo ufo : ufos) {
                Point position = ufo.getPosition();
    
                // Verificar límites
                if (isOutOfBounds(position)) {
                    if (!toRemove.contains(ufo)) {
                        toRemove.add(ufo);
                        crashedCount++;
                        totalCrashedCount++;
                        ufo.stop();
                    }
                    continue; // No verificar más si está fuera de límites
                }
    
                // Verificar colisiones
                for (Ufo other : ufos) {
                    if (ufo != other && areColliding(ufo, other)) {
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
                if (isInArrivalArea(position) && !toRemove.contains(ufo) && !ufo.isStopped()) {
                    toRemove.add(ufo);
                    arrivedCount++;
                    totalArrivedCount++;
                    ufo.stop();
                }
            }
            ufos.removeAll(toRemove);
        }
    
        // Eliminar los OVNIs que se van a eliminar
        
        
        // Actualizar los contadores en el presentador
        presenter.updateScore(totalCrashedCount);
        presenter.updateArrival(totalArrivedCount);
        
        
        return new int[]{crashedCount, arrivedCount};
    }
    
    
    private boolean areColliding(Ufo ufo1, Ufo ufo2) {
        Point pos1 = ufo1.getPosition();
        Point pos2 = ufo2.getPosition();
        int collisionDistance = 40;
        double distance = pos1.distance(pos2);
        return distance < collisionDistance;
    }

    public void countMovingUfos() {
        int count = 0;
        synchronized (ufos) {
            for (Ufo ufo : ufos) {
                if (ufo.isMoving()) {
                    count++;
                }
            }
        }
        presenter.countMovingUfos(count);
    }

    private boolean isInArrivalArea(Point position) {
        return position.x >= getArrivalAreaX() && position.x <= getArrivalAreaX() + getArrivalAreaWidth() &&
               position.y >= getArrivalAreaY() && position.y <= getArrivalAreaY() + getArrivalAreaHeight();
    }

    public Ufo selectUfoAtPosition(int x, int y) {
        selectedUfo = getUfoAtPosition(x, y);
        return selectedUfo;
    }
    
    private Ufo getUfoAtPosition(int x, int y) {
        if (ufos != null) {
            for (Ufo ufo : ufos) {
                Point position = ufo.getPosition();
                // Verificar si el clic está dentro de los límites del OVNI
                if (isPointInUfo(x, y, position)) {
                    return ufo;
                }
            }
        }
        return null;
    }
    
    private boolean isPointInUfo(int x, int y, Point position) {
        int ufoWidth = getUfoWidth(); // Asegúrate de tener métodos para obtener las dimensiones
        int ufoHeight = getUfoHeight();
        return x >= position.x && x <= position.x + ufoWidth && y >= position.y && y <= position.y + ufoHeight;
    }
    

    @Override
    public void changeSelectedUfoSpeed(int delta) {
        if (selectedUfo != null) {
            double newSpeed = Math.max(0, selectedUfo.getSpeed() + delta); // Evitar velocidad negativa
            selectedUfo.setSpeed(newSpeed);
            presenter.updateSpeed(newSpeed); // Actualizar en la interfaz
            System.out.println("Nueva velocidad: " + newSpeed); // Para depuración
        } else {
            System.out.println("No hay OVNI seleccionado."); // Para depuración
        }
    }


    
    public Ufo getSelectedUfo() {
        return selectedUfo;
    }

    private int getAreaWidth() {
        return presenter.areaSize()[0];
    }

    private int getAreaHeight() {
        return presenter.areaSize()[1];
    }

    private int getArrivalAreaX() {
        return presenter.destinationAreaSize()[0];
    }

    private int getArrivalAreaY() {
        return presenter.destinationAreaSize()[1];
    }

    private int getArrivalAreaWidth() {
        return presenter.destinationAreaSize()[2];
    }

    private int getArrivalAreaHeight() {
        return presenter.destinationAreaSize()[3];
    }

    private int getUfoWidth(){
        return presenter.ufoSize()[0];
    }

    private int getUfoHeight(){
        return presenter.ufoSize()[1];
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