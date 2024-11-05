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
    private int speed = Speed.MEDIUM.getValue();
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
    

    public void buttonsEvent() {
        mainPanel.getOptionsButton().addActionListener(e -> showOptionsDialog());
        mainPanel.getExitButton().addActionListener(e -> exitGame());
        mainPanel.getPlayButton().addActionListener(e -> startGameFromMainPanel());
    }
    
    private void exitGame() {
        System.exit(0);
    }
    
    private void startGameFromMainPanel() {
        switchToGamePanel();
        updateOptionsFromDialog();
        startGame();
    }
    
    private void switchToGamePanel() {
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "GamePanel");
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
        resetCountersInView();  
        gamePanel.setVisible(true);
        gamePanel.startUfoGame(ufoCount, speed, appearanceTime);
    }
    
    public void checkGameFinished() {
        if (presenter.allUfosStopped()) {
            showGameFinishedDialog();
        }
    }
    
    private void showGameFinishedDialog() {
        GameFinishedDialog gameFinishedDialog = new GameFinishedDialog(this);
        
        gameFinishedDialog.getMenuButton().addActionListener(e -> returnToMainMenu(gameFinishedDialog));
        gameFinishedDialog.getPlayButton().addActionListener(e -> restartGame(gameFinishedDialog));
        
        gameFinishedDialog.setVisible(true);
    }
    
    private void returnToMainMenu(GameFinishedDialog gameFinishedDialog) {
        switchToMainPanel();
        gameFinishedDialog.dispose();
    }
    
    private void switchToMainPanel() {
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "MainPanel");
    }
    
    private void restartGame(GameFinishedDialog gameFinishedDialog) {
        switchToGamePanel();
        gameFinishedDialog.dispose();
        startGame();
    }
    
    
    public void resetCountersInView() {
        gamePanel.getInfoArea().updatCrashedUfoCount(0);  
        gamePanel.getInfoArea().upDateArrivalUfoCount(0); 
        gamePanel.getInfoArea().upDateMovingUfoCount(0);
    }
    
    
    @Override
    public void updateUfoDisplay(List<Ufo> ufos) {
        gamePanel.updateUfos(ufos);
        checkGameFinished();
    }

    @Override
    public void updateScoreDisplay(int crashedCount) {
        gamePanel.getInfoArea().updatCrashedUfoCount(crashedCount);
    }

    @Override
    public void updateMovingCount(int movingCount) {
        gamePanel.getInfoArea().upDateMovingUfoCount(movingCount);
    }

    @Override
    public void updateArrivalDisplay(int arrivedCount) {
        gamePanel.getInfoArea().upDateArrivalUfoCount(arrivedCount);
    }


    @Override
    public void setPresenter(UfoInterface.Presenter presenter) {
       this.presenter = presenter;
    }

    @Override
    public void refresh() {
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
