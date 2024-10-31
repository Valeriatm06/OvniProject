package co.edu.uptc.views.OvniMainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import co.edu.uptc.utilities.DesignButton;
import co.edu.uptc.views.GlobalView;

import java.awt.*;

public class GameFinishedDialog extends JDialog {

    private JPanel BackgroundPanel;
    private JLabel titLabel;
    private DesignButton playButton;
    private DesignButton menuButton;

    
    public GameFinishedDialog(JFrame owner) {
        super(owner, "", true);
        setUndecorated(true);
        setSize(600, 340);
        setLocationRelativeTo(owner);
        initBackgroundPanel();
        initTitleLabel();
        initButtons();
        setVisible(true);
    }

    public void initBackgroundPanel(){
        BackgroundPanel = new JPanel(new BorderLayout());
        BackgroundPanel.setBackground(GlobalView.OPTIONS_BACKGROUND);
        BackgroundPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        add(BackgroundPanel);
    }

    public void initTitleLabel(){
        titLabel = new JLabel("JUEGO TERMINADO", SwingConstants.CENTER);
        titLabel.setFont(GlobalView.TITLE_FONT_MEDIUM);  
        titLabel.setForeground(GlobalView.TITLE_TEXT);
        titLabel.setBorder(new EmptyBorder(80, 0, 5, 0));
        BackgroundPanel.add(titLabel, BorderLayout.NORTH);
    }

    public void initButtons(){
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)); 
        buttonsPanel.setOpaque(false);  
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 50, 0));  

        playButton = new DesignButton("Volver a jugar", true);
        playButton.setBackground(GlobalView.DEFAULT_BTN_BACKGROUND);
        playButton.setPreferredSize(new Dimension(170, 40));  

        menuButton = new DesignButton("Volver al menu", true);
        menuButton.setBackground(GlobalView.DEFAULT_BTN_BACKGROUND);
        menuButton.setPreferredSize(new Dimension(175, 40)); 

        buttonsPanel.add(playButton);
        buttonsPanel.add(menuButton);
        BackgroundPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }
}
