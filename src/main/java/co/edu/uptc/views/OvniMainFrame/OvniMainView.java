package co.edu.uptc.views.OvniMainFrame;

import javax.swing.JFrame;
import java.awt.CardLayout;
import co.edu.uptc.interfaces.OvniInterface;
import co.edu.uptc.utilities.Speed;

public class OvniMainView extends JFrame implements OvniInterface.View{

    private OvniInterface.Presenter presenter;
    private MainPanel mainPanel;
    private GamePanel gamePanel;
    private OptionsDialog optionsDialog;
    private int ovniCount = 5; 
    private int appearanceTime = 1000;
    private int speed = Speed.MEDIO.getValue();
    private int ovniType = 1;


    public OvniMainView(){
        optionsDialog = new OptionsDialog(this, ovniCount, appearanceTime, speed, ovniType);
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
        });
        
    }
    

    private void showOptionsDialog() {
        if (optionsDialog == null) {
            optionsDialog = new OptionsDialog(this, ovniCount, appearanceTime, speed, ovniType);
        }
        optionsDialog.setVisible(true);
        
        updateOptionsFromDialog();
    }
    
    private void updateOptionsFromDialog() {
        ovniCount = optionsDialog.getOvniCount();
        appearanceTime = optionsDialog.getAppearanceTime();
        speed = optionsDialog.getSpeed();
        ovniType = optionsDialog.getSelectedOvniType();
        gamePanel.getInfoArea().updateOvniType(ovniType);
    }    
    

    @Override
    public void setPresenter(OvniInterface.Presenter presenter) {
       this.presenter = presenter;
    }

    @Override
    public void begin() {
        setVisible(true);
    }
    
    
}
