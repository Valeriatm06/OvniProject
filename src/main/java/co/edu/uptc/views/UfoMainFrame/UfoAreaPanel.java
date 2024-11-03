package co.edu.uptc.views.UfoMainFrame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.PropertiesService;

public class UfoAreaPanel extends JPanel {

    
    public static final int WIDTH = 1051;
    public static final int HEIGHT = 667;
    public static final int ARRIVAL_AREA_X = 800; 
    public static final int ARRIVAL_AREA_Y = 500; 
    public static final int ARRIVAL_AREA_WIDTH = 200; 
    public static final int ARRIVAL_AREA_HEIGHT = 100;
    PropertiesService propertiesService;
    private Image ufoImage;
    private List<Ufo> ufos;
    private Image arrivalAreaImage; 

    public UfoAreaPanel(Image ufo) {
        propertiesService = new PropertiesService();
        this.ufoImage = ufo;
        setOpaque(false); 
        setLayout(null);
        loadArrivalAreaImage();
    }

    public void setUfos(List<Ufo> ufos) {
        this.ufos = ufos;
        repaint();
    }

     @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(arrivalAreaImage, ARRIVAL_AREA_X, ARRIVAL_AREA_Y, ARRIVAL_AREA_WIDTH, ARRIVAL_AREA_HEIGHT, this);
        if (ufos != null) {
            for (Ufo ufo : ufos) {
                Point position = ufo.getPosition();
                g.drawImage(ufoImage, position.x, position.y, this);
            }
        }
    }

    private void loadArrivalAreaImage() {
        arrivalAreaImage = new ImageIcon(propertiesService.getKeyValue("balckHoLePath")).getImage();
    }
    
}
