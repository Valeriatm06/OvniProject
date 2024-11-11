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
    private int stoppedUfosCount;

    public UfoModel(){
        this.ufos = new CopyOnWriteArrayList<>();
        totalCrashedCount = 0;
        totalArrivedCount = 0;
        stoppedUfosCount = 0;
        this.running = false;
    }

    @Override
    public void startGame(int ufoNumber, double speed, int appearanceTime){
        resetGameCounters();
        ufos.clear();
        running = true;
        Random random = new Random();
        Thread thread = new Thread(() -> createAndMoveUfos(ufoNumber, random, speed, appearanceTime));
        thread.start();
    }

    private void createAndMoveUfos(int ufoNumber, Random random, double speed, int appearanceTime) {
        for (int i = 0; i < ufoNumber; i++) {
            if (!running) break;
            Ufo newUfo = createNewUfo(random, speed);
            ufos.add(newUfo);
            updateUfosList();
            startUfoMovement(newUfo);
            sleepBetweenAppearances(appearanceTime);
        }
    }

    private Ufo createNewUfo(Random random, double speed){
        Point initialPosition = new Point(random.nextInt(getAreaWidth()), random.nextInt(getAreaHeight()));
        double angle = random.nextDouble() * 360;
        return new Ufo(initialPosition, speed, angle);
    }

    private void updateUfosList(){
        presenter.updateUfos(new ArrayList<>(ufos));
    }

    private void sleepBetweenAppearances(int appearanceTime){
        try {
            Thread.sleep(appearanceTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void startUfoMovement(Ufo ufo){
        Thread movementThread = new Thread(() -> {
            try {
                while (running && ufo.isMoving()){
                    synchronized (ufo){  
                        updateUfoPosition(ufo); 
                        checkCollisions(); 
                        countMovingUfos(); 
                    }
                Thread.sleep(100);
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
        });
        movementThread.start();
    }

    private synchronized void updateUfoPosition(Ufo ufo){
        List<Point> trajectory = ufo.getTrajectory();
        if (trajectory != null && !trajectory.isEmpty()){
            moveUfoAlongTrajectory(ufo, trajectory);
        }else {
            continueUfoMovement(ufo);
        }
        checkAndHandleOutOfBounds(ufo);
        presenter.updateUfos(new ArrayList<>(ufos)); 
    }
    
    private void moveUfoAlongTrajectory(Ufo ufo, List<Point> trajectory){
        Point target = trajectory.get(0);
        double newX = target.x - ufo.getPosition().x;
        double newY = target.y - ufo.getPosition().y;
        double distance = Math.sqrt(newX * newX + newY * newY);
        if (distance < ufo.getSpeed()){
            ufo.setPosition(target);
            trajectory.remove(0);
        } else {
            moveUfoTowardsTarget(ufo, newX, newY);
        }
        ufo.setPreviousAngle(Math.atan2(newY, newX));
    }
    
    private void moveUfoTowardsTarget(Ufo ufo, double newX, double newY){
        double angleToTarget = Math.atan2(newY, newX);
        Point newPosition = new Point(
            (int) (ufo.getPosition().x + ufo.getSpeed() * Math.cos(angleToTarget)),
            (int) (ufo.getPosition().y + ufo.getSpeed() * Math.sin(angleToTarget))
        );
        ufo.setPosition(newPosition);
    }
    
    private void continueUfoMovement(Ufo ufo){
        double angleToContinue = ufo.getPreviousAngle();
        Point newPosition = new Point(
            (int) (ufo.getPosition().x + ufo.getSpeed() * Math.cos(angleToContinue)),
            (int) (ufo.getPosition().y + ufo.getSpeed() * Math.sin(angleToContinue))
        );
        ufo.setPosition(newPosition);
        ufo.setPreviousAngle(angleToContinue);
    }
    
    private void checkAndHandleOutOfBounds(Ufo ufo){
        if (isOutOfBounds(ufo.getPosition())){
            ufo.stop();
        }
    }

    @Override
    public void addTrajectoryPointToSelectedUfo(Point point){
        if (selectedUfo != null) {
            List<Point> trajectory = selectedUfo.getTrajectory();
            if (trajectory == null) {
                trajectory = new ArrayList<>();
                selectedUfo.setTrajectory(trajectory);
            }
            synchronized (trajectory) {
                if (point != null) {
                    trajectory.add(point);
                }
            }
        }
    }

    private boolean isOutOfBounds(Point position){
        return position.x <= 0 || position.x >= getAreaWidth() || position.y <= 0 || position.y >= getAreaHeight();
    }

    public int[] checkCollisions(){
        int crashedCount = 0;
        int arrivedCount = 0;
        List<Ufo> toRemove = new ArrayList<>();
        synchronized (ufos) {
            crashedCount += checkOutOfBounds(toRemove);
            crashedCount += checkCollisionsBetweenUfos(toRemove);
            arrivedCount += checkArrivals(toRemove);
            stoppedUfosCount += toRemove.size();
            removeUfos(toRemove);
        }
        totalCrashedCount += crashedCount;
        totalArrivedCount += arrivedCount;
        updatePresenter();
        return new int[]{crashedCount, arrivedCount};
    }
    
    private int checkOutOfBounds(List<Ufo> toRemove){
        int crashedCount = 0;
        for (Ufo ufo : ufos) {
            if (isOutOfBounds(ufo.getPosition())) {
                crashedCount++;
                toRemove.add(ufo);
                ufo.stop();
                ufo.destroy();
            }
        }
        return crashedCount;
    }
    
    private int checkCollisionsBetweenUfos(List<Ufo> toRemove){
        int crashedCount = 0;
        for (Ufo ufo : ufos) {
            for (Ufo other : ufos) {
                if (ufo != other && areColliding(ufo, other)) {
                    if (!toRemove.contains(ufo)) {
                        crashedCount++;
                        toRemove.add(ufo);
                        ufo.stop();
                        ufo.destroy();
                    }
                    if (!toRemove.contains(other)) {
                        crashedCount++;
                        toRemove.add(other);
                        other.stop();
                        other.destroy();
                    }
                }
            }
        }
        return crashedCount;
    }
    
    private int checkArrivals(List<Ufo> toRemove){
        int arrivedCount = 0;
        for (Ufo ufo : ufos) {
            if (isInArrivalArea(ufo.getPosition()) && !toRemove.contains(ufo) && !ufo.isStopped()) {
                toRemove.add(ufo);
                arrivedCount++;
                ufo.stop();
                ufo.destroy();
            }
        }
        return arrivedCount;
    }
    
    private void removeUfos(List<Ufo> toRemove){
        ufos.removeAll(toRemove);
    }
    
    private void updatePresenter(){
        presenter.updateScore(totalCrashedCount); 
        presenter.updateArrival(totalArrivedCount); 
        presenter.updateUfos(new ArrayList<>(ufos));
    }
    
    private boolean areColliding(Ufo ufoOne, Ufo ufoTwo){
        Point pos1 = ufoOne.getPosition();
        Point pos2 = ufoTwo.getPosition();
        int collisionDistance = 40;
        double distance = pos1.distance(pos2);
        return distance < collisionDistance;
    }

    public void countMovingUfos(){
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

    private boolean isInArrivalArea(Point position){
        return position.x >= getArrivalAreaX() && position.x <= getArrivalAreaX() + getArrivalAreaWidth() &&
               position.y >= getArrivalAreaY() && position.y <= getArrivalAreaY() + getArrivalAreaHeight();
    }

    public Ufo selectUfoAtPosition(int x, int y){
        selectedUfo = getUfoAtPosition(x, y);
        return selectedUfo;
    }
    
    private Ufo getUfoAtPosition(int x, int y){
        if (ufos != null) {
            for (Ufo ufo : ufos) {
                Point position = ufo.getPosition();
                if (isPointInUfo(x, y, position)) {
                    return ufo;
                }
            }
        }
        return null;
    }
    
    private boolean isPointInUfo(int x, int y, Point position){
        int ufoWidth = getUfoWidth();
        int ufoHeight = getUfoHeight();
        return x >= position.x && x <= position.x + ufoWidth && y >= position.y && y <= position.y + ufoHeight;
    }
    

    @Override
    public void changeSelectedUfoSpeed(int delta){
        if (selectedUfo != null) {
            double newSpeed = Math.max(0, selectedUfo.getSpeed() + delta);
            selectedUfo.setSpeed(newSpeed);
            presenter.updateSpeed(newSpeed); 
        }
    }

    @Override
    public boolean allUfosStopped(){
        if (stoppedUfosCount >= presenter.getUfoNumber()) {
            return true;
        }
        return false;
    }

    public void resetGameCounters(){
        totalCrashedCount = 0;
        totalArrivedCount = 0;
        stoppedUfosCount = 0;
    }    

    public Ufo getSelectedUfo(){
        return selectedUfo;
    }

    private int getAreaWidth(){
        return presenter.areaSize()[0];
    }

    private int getAreaHeight(){
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

    private int getArrivalAreaHeight(){
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
    public boolean isRunning(){
        return running;
    }

    @Override
    public List<Ufo> getUfosList(){
        return ufos;
    }
}
