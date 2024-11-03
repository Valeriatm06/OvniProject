package co.edu.uptc.views.UfoMainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.utilities.UtilThread;
import lombok.Getter;

@Getter
public class GamePanel extends JPanel {

    private UfoMainView ufoMainView;
    private GameInfoPanel infoArea;
    private PropertiesService propertiesService;
    private JPanel backgroundPanel;
    private UfoAreaPanel ufoAreaPanel;
    private int ufoType;


    public GamePanel(UfoMainView ufoMainView) {
        propertiesService = new PropertiesService();
        this.ufoMainView = ufoMainView;
        setLayout(new BorderLayout());
        ufoType = 1;
        initBackgroundPanel();
        initufoArea();
        initInfoArea();
    }
    
    private String getUfoPath(int newUfoType) {
        return propertiesService.getKeyValue("ufo" + newUfoType + "Path");
    }

    private void initBackgroundPanel() {
        backgroundPanel = new BackgroundPanel(propertiesService.getKeyValue("fontImagePath"));
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void initufoArea() {
        ufoAreaPanel = new UfoAreaPanel(new ImageIcon(getUfoPath(ufoType)).getImage());
        backgroundPanel.add(ufoAreaPanel, BorderLayout.CENTER);
    }
    

    private void initInfoArea() {
        infoArea = new GameInfoPanel(ufoMainView);
        add(infoArea, BorderLayout.EAST);
    }

    
    public void updateufoType(int newufoType) {
        this.ufoType = newufoType;
    
        if (ufoAreaPanel != null) {
            backgroundPanel.remove(ufoAreaPanel);
        }
    
        ufoAreaPanel = new UfoAreaPanel(new ImageIcon(getUfoPath(ufoType)).getImage());
        backgroundPanel.add(ufoAreaPanel, BorderLayout.CENTER);    
        revalidate(); 
        repaint(); 
    }

    public void startUfoGame(int ufoCount, double speed) {
        ufoMainView.getPresenter().startGame(ufoCount, speed);
    
        Thread thread = new Thread(() -> {
            while (ufoMainView.getPresenter().isRunning()) {
                List<Ufo> ufos = ufoMainView.getPresenter().getUfos();
                ufoAreaPanel.setUfos(ufos);
                ufoAreaPanel.repaint(); 
                UtilThread.sleep(100);
            }
        });
    
        thread.start(); 
    }

    public int[] getUfoArea(){
        int[] size = new int[2];
        size[0] = UfoAreaPanel.WIDTH; 
        size[1] = UfoAreaPanel.HEIGHT; 
        return size;
    }

    public int[] getDestinationArea(){
        int[] size = new int[4];
        size[0] = UfoAreaPanel.ARRIVAL_AREA_X; 
        size[1] = UfoAreaPanel.ARRIVAL_AREA_Y; 
        size[2] = UfoAreaPanel.ARRIVAL_AREA_WIDTH; 
        size[3] = UfoAreaPanel.ARRIVAL_AREA_HEIGHT;
        return size; 
    }

    public void updateUfos(List<Ufo> ufos) {
        ufoAreaPanel.setUfos(ufos); 
        ufoAreaPanel.repaint(); 
    }
      
}
