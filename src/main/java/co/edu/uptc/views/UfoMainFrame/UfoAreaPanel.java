package co.edu.uptc.views.UfoMainFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseMotionListener;
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

        // Listener para el foco
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
            // Dibujar trayectoria
            drawTrajectory(g, ufo);
        }
    }
}


private void drawTrajectory(Graphics g, Ufo ufo) {
    List<Point> trajectory = ufo.getTrajectory();
    if (trajectory != null && !trajectory.isEmpty()) { // Asegúrate de que haya puntos
        g.setColor(Color.BLUE);
        for (int i = 0; i < trajectory.size() - 1; i++) {
            Point start = trajectory.get(i);
            Point end = trajectory.get(i + 1);
            if (start != null && end != null) {
                g.drawLine(start.x, start.y, end.x, end.y);
            }
        }
    }
}
    
    

    private void loadArrivalAreaImage() {
        arrivalAreaImage = new ImageIcon(propertiesService.getKeyValue("balckHoLePath")).getImage();
    }

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Ufo clickedUfo = selectUfoAtPosition(e.getX(), e.getY());
    
                if (clickedUfo != null) {
                    // Si se clickeó el mismo OVNI, solo permitir cambiar la velocidad
                    if (selectedUfo == clickedUfo) {
                        System.out.println("Cambiando velocidad del OVNI seleccionado");
                    } else {
                        // Se clickeó un nuevo OVNI, seleccionar sin eliminar la trayectoria
                        selectedUfo = clickedUfo; 
                        System.out.println("OVNI seleccionado: " + selectedUfo);
                    }
                } else {
                    // Si se clickeó en el área vacía y hay un OVNI seleccionado, no hacer nada
                    if (selectedUfo != null) {
                        System.out.println("Clic en el área vacía, la trayectoria no se elimina.");
                    }
                }
    
                requestFocusInWindow();
                repaint();
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedUfo != null && !selectedUfo.getTrajectory().isEmpty()) {
                    // Reiniciar el movimiento del OVNI con la nueva trayectoria
                    gamePanel.getUfoMainView().getPresenter().startUfoMovement(selectedUfo);
                }
                repaint(); // Redibuja después de soltar el mouse
            }
        });
    
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedUfo != null) {
                    addTrajectoryPoint(e.getPoint()); // Añadir punto a la trayectoria
                    repaint(); // Redibujar para mostrar la nueva trayectoria
                }
            }
        });
    }
    

    
    
    
    
    public Ufo selectUfoAtPosition(int x, int y) {
        Ufo ufo = gamePanel.getUfoMainView().getPresenter().selectUfoAtPosition(x, y);
        if (ufo != null) {
            selectedUfo = ufo;
            System.out.println("OVNI seleccionado: " + ufo);
        } else {
            System.out.println("No se seleccionó ningún OVNI en la posición: " + x + ", " + y);
        }
        repaint();
        return selectedUfo; // Vuelve a dibujar para reflejar el cambio
    }
    

    private void addTrajectoryPoint(Point point) {
        if (selectedUfo != null && point != null) {
            selectedUfo.getTrajectory().add(point); // Agregar el nuevo punto a la trayectoria
        }
    }

    public void initKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (selectedUfo != null) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            gamePanel.getUfoMainView().getPresenter().changeSelectedUfoSpeed(1);
                            break;
                        case KeyEvent.VK_DOWN:
                            gamePanel.getUfoMainView().getPresenter().changeSelectedUfoSpeed(-1);
                            break;
                    }
                }
            }
        });
    }

    // public void selectUfoAtPosition(int x, int y) {
    //     Ufo ufo = gamePanel.getUfoMainView().getPresenter().selectUfoAtPosition(x, y);
    //     if (ufo != null) {
    //         selectedUfo = ufo;
    //         System.out.println("OVNI seleccionado: " + ufo);
    //     } else {
    //         System.out.println("No se seleccionó ningún OVNI en la posición: " + x + ", " + y);
    //     }
    //     repaint(); // Vuelve a dibujar para reflejar el cambio
    // }
} 