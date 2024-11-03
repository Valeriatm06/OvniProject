package co.edu.uptc.interfaces;

import java.awt.Point;
import java.util.List;

import co.edu.uptc.pojos.Ufo;

public interface UfoInterface {
    
    public interface Model {
        public void setPresenter(Presenter presenter);

        public void startGame(int ufoNumber, double speed, int appearance);

        public boolean isRunning();

        public List<Ufo> getUfosList();

        public Ufo selectUfoAtPosition(int x, int y);

        public void changeSelectedUfoSpeed(int delta);

        public Ufo getSelectedUfo();

        public void addTrajectoryPointToUfo(Ufo ufo, Point point);

    }

    public interface Presenter {
        public void setModel(Model model);

        public void setView(View view);

        public int[] areaSize();

        public int[] destinationAreaSize();

        public int[] ufoSize();

        public void startGame(int ufoNumber, double speed,int appearance);

        public boolean isRunning();

        public List<Ufo> getUfos();

        public void updateUfos(List<Ufo> ufos);

        public void updateScore(int crashedUfoCount);

        public void updateArrival(int arrivedCount);

        public void countMovingUfos(int movingCount);

        public void updateSpeed(double newSpeed);

        public Ufo selectUfoAtPosition(int x, int y);

        public void changeSelectedUfoSpeed(int delta);

        public void addTrajectoryPointToSelectedUfo(Point point);
    }

    public interface View {
        public void setPresenter(Presenter presenter);

        public void begin();

        public int[] areaSize();

        public int[] destinationAreaSize();

        public int[] ufoSize();

        public void updateUfoDisplay(List<Ufo> ufos);

        public void updateScoreDisplay(int crashedCount);

        public void updateArrivalDisplay(int arrivedCount);

        public void updateMovingCount(int crashedCount);

        public void refresh();
    }
}
