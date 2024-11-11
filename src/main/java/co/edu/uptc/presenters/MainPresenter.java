package co.edu.uptc.presenters;

import java.awt.Point;
import java.util.List;

import co.edu.uptc.interfaces.UfoInterface;
import co.edu.uptc.pojos.Ufo;
import lombok.Getter;

@Getter
public class MainPresenter implements UfoInterface.Presenter{

    private UfoInterface.Model model;
    private UfoInterface.View view;

    @Override
    public void setModel(UfoInterface.Model model) {
        this.model = model;
    }

    @Override
    public void setView(UfoInterface.View view) {
       this.view = view;
    }

    @Override
    public int[] areaSize() {
        return view.areaSize();
    }

    @Override
    public void startGame(int ufoNumber, double speed, int appearance) {
       model.startGame(ufoNumber, speed, appearance);
    }

    @Override
    public boolean isRunning() {
        return model.isRunning();
    }

    @Override
    public List<Ufo> getUfos() {
       return model.getUfosList();
    }

    @Override
    public void updateUfos(List<Ufo> ufos) {
        view.updateUfoDisplay(ufos);
    }

    @Override
    public void updateScore(int crashedCount) {
        view.updateScoreDisplay(crashedCount);
    }

    @Override
    public void updateArrival(int arrivedCount) {
        view.updateArrivalDisplay(arrivedCount);
    }

    @Override
    public void countMovingUfos(int movingNumber) {
       view.updateMovingCount(movingNumber);
    }

    @Override
    public int[] destinationAreaSize() {
        return view.destinationAreaSize();
    }

    @Override
    public int[] ufoSize() {
        return view.ufoSize();
    }

    @Override
    public void updateSpeed(double newSpeed) {
        if (model.getSelectedUfo() != null) {
            model.getSelectedUfo().setSpeed(newSpeed);
            view.refresh(); 
        }
    }
    
    @Override
    public Ufo selectUfoAtPosition(int x, int y) {
        return model.selectUfoAtPosition(x, y);
    }

    @Override
    public void changeSelectedUfoSpeed(int delta) {
        model.changeSelectedUfoSpeed(delta);
    }

    @Override
    public void addTrajectoryPointToUfo(Ufo ufo, Point point) {
            model.addTrajectoryPointToSelectedUfo(point);;
    }

    @Override
    public void startUfoMovement(Ufo ufo) {
        model.startUfoMovement(ufo);
    }

    @Override
    public boolean allUfosStopped() {
        return model.allUfosStopped();
    }

    @Override
    public int getUfoNumber() {
        return view.getUfoNumber();
    }
}
