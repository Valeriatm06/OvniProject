package co.edu.uptc.views.UfoMainFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.PropertiesService;
import lombok.Getter;

@Getter
public class UfoAreaPanel extends JPanel {

    private GamePanel gamePanel;
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
    private Ufo selectedUfo;

    public UfoAreaPanel(Image ufo, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        propertiesService = new PropertiesService();
        this.ufoImage = ufo;
        selectedUfo = null;
        setOpaque(false); 
        setLayout(null);
        loadArrivalAreaImage();
        initMouseListener();
        initKeyListener();
        setFocusable(true); // Asegúrate de que el panel es enfocable
        requestFocusInWindow();

        // Agregar un listener para el foco
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("UfoAreaPanel tiene el foco");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("UfoAreaPanel ha perdido el foco");
            }
        });
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
                if (ufo == selectedUfo) { // Resaltar el OVNI seleccionado
                    g.setColor(Color.RED);
                    g.drawRect(position.x, position.y, ufoImage.getWidth(this), ufoImage.getHeight(this));
                }
            }
        }
    }

    private void loadArrivalAreaImage() {
        arrivalAreaImage = new ImageIcon(propertiesService.getKeyValue("balckHoLePath")).getImage();
    }

    public void initMouseListener(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectUfoAtPosition(e.getX(), e.getY());
                requestFocusInWindow(); // Asegúrate de que el panel obtenga el foco
                repaint(); // Volver a dibujar para mostrar el OVNI seleccionado
            }
        });
    }

    public void initKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Tecla presionada: " + e.getKeyCode());
                if (selectedUfo != null) {
                    System.out.println("OVNI seleccionado: " + selectedUfo);
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            System.out.println("Aumentando velocidad...");
                            gamePanel.getUfoMainView().getPresenter().changeSelectedUfoSpeed(1);
                            break;
                        case KeyEvent.VK_DOWN:
                            System.out.println("Disminuyendo velocidad...");
                            gamePanel.getUfoMainView().getPresenter().changeSelectedUfoSpeed(-1);
                            break;
                    }
                } else {
                    System.out.println("No hay OVNI seleccionado.");
                }
            }
        });
    }

    public void selectUfoAtPosition(int x, int y) {
        Ufo ufo = gamePanel.getUfoMainView().getPresenter().selectUfoAtPosition(x, y);
        if (ufo != null) {
            selectedUfo = ufo;
            System.out.println("OVNI seleccionado: " + ufo);
        } else {
            System.out.println("No se seleccionó ningún OVNI en la posición: " + x + ", " + y);
        }
        repaint(); // Vuelve a dibujar para reflejar el cambio
    }
}
