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
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.views.GlobalView;
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
        setFocusable(true); 
        requestFocusInWindow();
        initFocus();
    }

    public void setUfos(List<Ufo> ufos) {
        this.ufos = ufos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawArrivalArea(g);
        drawUfos(g);
    }

    private void drawArrivalArea(Graphics g) {
        g.drawImage(arrivalAreaImage, ARRIVAL_AREA_X, ARRIVAL_AREA_Y, ARRIVAL_AREA_WIDTH, ARRIVAL_AREA_HEIGHT, this);
    }

    private void drawUfos(Graphics g) {
        if (ufos != null) {
            for (Ufo ufo : ufos) {
                drawUfo(g, ufo);
                if (ufo == selectedUfo) {
                    highlightSelectedUfo(g, ufo);
                }
                if (gamePanel.getInfoArea().isTrajectoryVisible()) {
                    drawTrajectory(g, ufo);
                }
            }
        }
    }

    private void drawUfo(Graphics g, Ufo ufo) {
        Point position = ufo.getPosition();
        g.drawImage(ufoImage, position.x, position.y, this);
    }

    private void highlightSelectedUfo(Graphics g, Ufo ufo) {
        Point position = ufo.getPosition();
        g.setColor(Color.RED);
        g.drawRect(position.x, position.y, ufoImage.getWidth(this), ufoImage.getHeight(this));
    }

    private void drawTrajectory(Graphics g, Ufo ufo) {
        List<Point> trajectory = ufo.getTrajectory();
        if (trajectory != null && !trajectory.isEmpty()) {
            g.setColor(GlobalView.TITLE_TEXT);
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
                handleMousePressed(e);
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });
    
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }
    
    private void handleMousePressed(MouseEvent e) {
        Ufo clickedUfo = selectUfoAtPosition(e.getX(), e.getY());
        if (clickedUfo != null) {
            toggleSelectedUfo(clickedUfo);
        }
        requestFocusInWindow();
        repaint();
    }
    
    private void handleMouseReleased(MouseEvent e) {
        if (selectedUfo != null && !selectedUfo.getTrajectory().isEmpty()) {
            gamePanel.getUfoMainView().getPresenter().startUfoMovement(selectedUfo);
        }
        repaint();
    }
    
    private void handleMouseDragged(MouseEvent e) {
        if (selectedUfo != null) {
            addTrajectoryPoint(e.getPoint());
            repaint();
        }
    }
    
    private void toggleSelectedUfo(Ufo clickedUfo) {
        if (selectedUfo == clickedUfo) {
            return;
        }
        selectedUfo = clickedUfo;
    }
    
    
    public Ufo selectUfoAtPosition(int x, int y) {
        Ufo ufo = gamePanel.getUfoMainView().getPresenter().selectUfoAtPosition(x, y);
        if (ufo != null) {
            selectedUfo = ufo;
        }
        repaint();
        return selectedUfo; 
    }
    

    private void addTrajectoryPoint(Point point) {
        if (selectedUfo != null && point != null) {
            selectedUfo.getTrajectory().add(point); 
        }
    }

    public void initKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });
    }
    
    private void handleKeyPressed(KeyEvent e) {
        if (selectedUfo != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    changeSelectedUfoSpeed(1);
                    break;
                case KeyEvent.VK_DOWN:
                    changeSelectedUfoSpeed(-1);
                    break;
            }
        }
    }

    private void initFocus(){
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }
    
    private void changeSelectedUfoSpeed(int delta) {
        gamePanel.getUfoMainView().getPresenter().changeSelectedUfoSpeed(delta);
    }
    
} 