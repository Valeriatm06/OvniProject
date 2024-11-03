package co.edu.uptc.views.UfoMainFrame;

import javax.swing.JFrame;
import java.awt.CardLayout;
import java.util.List;

import co.edu.uptc.interfaces.UfoInterface;
import co.edu.uptc.pojos.Ufo;
import co.edu.uptc.utilities.Speed;
import lombok.Getter;

@Getter
public class UfoMainView extends JFrame implements UfoInterface.View{

    private UfoInterface.Presenter presenter;
    private MainPanel mainPanel;
    private GamePanel gamePanel;
    private OptionsDialog optionsDialog;
    private int ufoCount = 5; 
    private int appearanceTime = 1000;
    private int speed = Speed.MEDIO.getValue();
    private int ufoType = 1;

    public UfoMainView(){
        optionsDialog = new OptionsDialog(this, ufoCount, appearanceTime, speed, ufoType);
        initFrame();
        initMainPanel();
        initGamePanel();
        buttonsEvent();
    }

    public void initFrame(){
        setTitle("Viaje Espacial");
        setLayout(new CardLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setLocationRelativeTo(null);
    }
    

    public void initMainPanel(){
        mainPanel = new MainPanel();
        add(mainPanel, "MainPanel"); 
    }
    
    public void initGamePanel(){
        gamePanel = new GamePanel(this);
        add(gamePanel, "GamePanel"); 
        gamePanel.setVisible(false); 
    }
    

    public void buttonsEvent(){
        mainPanel.getOptionsButton().addActionListener(e -> showOptionsDialog());
        mainPanel.getExitButton().addActionListener(e -> System.exit(0));
        mainPanel.getPlayButton().addActionListener(e -> {
            CardLayout layout = (CardLayout) getContentPane().getLayout();
            layout.show(getContentPane(), "GamePanel");
            updateOptionsFromDialog();
            startGame(); 
        });
        
    }
    

    private void showOptionsDialog() {
        if (optionsDialog == null) {
            optionsDialog = new OptionsDialog(this, ufoCount, appearanceTime, speed, ufoType);
        }
        optionsDialog.setVisible(true);
        
    }
    
    public void updateOptionsFromDialog() {
        ufoCount = optionsDialog.getUfoCount();
        appearanceTime = optionsDialog.getAppearanceTime();
        speed = optionsDialog.getSpeed();
        ufoType = optionsDialog.getSelectedUfoType();

        gamePanel.updateufoType(ufoType);
        gamePanel.getInfoArea().updateufoType(ufoType);
    }

    public void startGame() {
        gamePanel.startUfoGame(ufoCount, speed, appearanceTime);
    }
    
    @Override
    public void updateUfoDisplay(List<Ufo> ufos) {
        // Actualiza la visualización de los OVNIs en la interfaz
        gamePanel.updateUfos(ufos);
    }

    @Override
    public void updateScoreDisplay(int crashedCount) {
        // Actualiza la visualización de la puntuación
        // Por ejemplo, podrías tener un JLabel que muestre la puntuación
        gamePanel.getInfoArea().updatCrashedUfoCount(crashedCount);
    }

    @Override
    public void updateMovingCount(int movingCount) {
        // Actualiza la visualización de la puntuación
        // Por ejemplo, podrías tener un JLabel que muestre la puntuación
        gamePanel.getInfoArea().upDateMovingUfoCount(movingCount);
    }

    @Override
    public void updateArrivalDisplay(int arrivedCount) {
        // Actualiza la visualización de las llegadas
        gamePanel.getInfoArea().upDateArrivalUfoCount(arrivedCount);
    }


    @Override
    public void setPresenter(UfoInterface.Presenter presenter) {
       this.presenter = presenter;
    }

    @Override
    public void refresh() {
        // Esto puede incluir repaint() o actualizar la interfaz según sea necesario
        gamePanel.getUfoAreaPanel().repaint();
    }

    @Override
    public void begin() {
        setVisible(true);
    }

    @Override
    public int[] areaSize() {
       return gamePanel.getUfoArea();
    }

    @Override
    public int[] destinationAreaSize(){
        return gamePanel.getDestinationArea();
    }
    
    @Override
    public int[] ufoSize(){
        return gamePanel.getUfoSize();
    }
    
}
