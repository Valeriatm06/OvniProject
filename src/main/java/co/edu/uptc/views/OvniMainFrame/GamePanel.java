package co.edu.uptc.views.OvniMainFrame;

import javax.swing.JPanel;

import co.edu.uptc.utilities.PropertiesService;
import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Dimension;

 @Getter
public class GamePanel extends JPanel{

    private OvniMainView ovniMainView;
    private GameInfoPanel infoArea;
    private PropertiesService propertiesService;
    private JPanel backgroundPanel;
    private JPanel ovniArea;


    public GamePanel(OvniMainView ovniMainView){
        propertiesService = new PropertiesService();
        this.ovniMainView = ovniMainView;
        setLayout(new BorderLayout());
        initBackgroundPanel();
        initOvniArea();
        initInfoArea();
    }
    
    public void initBackgroundPanel() {
        backgroundPanel = new BackgroundPanel(propertiesService.getKeyValue("fontImagePath"));
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void initOvniArea() {
        ovniArea = new JPanel();
        ovniArea.setOpaque(false);

        ovniArea.setPreferredSize(new Dimension(800, 600));
        ovniArea.setLayout(null);

        backgroundPanel.add(ovniArea, BorderLayout.CENTER); 
    }

    private void initInfoArea() {
        infoArea = new GameInfoPanel(ovniMainView);
        add(infoArea, BorderLayout.EAST);
    }


}
