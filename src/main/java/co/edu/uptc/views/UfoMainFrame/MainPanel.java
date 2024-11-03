package co.edu.uptc.views.UfoMainFrame;

import javax.swing.*;
import co.edu.uptc.utilities.DesignButton;
import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.views.GlobalView;
import lombok.Getter;
import lombok.Setter;
import java.awt.*;

@Getter
@Setter
public class MainPanel extends JPanel {

    private PropertiesService propertiesService;
    private JPanel backgroundPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel buttonPanel;
    private GridBagConstraints gbc;
    private DesignButton playButton;
    private DesignButton optionsButton;
    private DesignButton exitButton;

    public MainPanel() {
        propertiesService = new PropertiesService();
        setLayout(new BorderLayout());
        initBackgroundPanel();
        initTitle();
        initButtons();
    }

    public void initBackgroundPanel() {
        backgroundPanel = new BackgroundPanel(propertiesService.getKeyValue("fontImagePath"));
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
    }

    public void initTitle() {
        titleLabel = new JLabel("VIAJE ESPACIAL", SwingConstants.CENTER);
        titleLabel.setFont(GlobalView.TITLE_FONT);
        titleLabel.setForeground(GlobalView.TITLE_TEXT);
        titleLabel.setOpaque(false);

        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 100));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
    }

    public void initButtons() {
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(40, 50, 50, 50);

        initPlayButton();
        initOptionsButton();
        initExitButton();

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    public void initPlayButton(){
        playButton = new DesignButton("JUGAR", true);
        playButton.setIcon(new ImageIcon(propertiesService.getKeyValue("playPath")));
        playButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        playButton.setVerticalTextPosition(SwingConstants.CENTER);
        playButton.setForeground(GlobalView.ALL_TEXT);
        playButton.setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(playButton, gbc);
    }

    public void initOptionsButton(){
        optionsButton = new DesignButton("OPCIONES", true);
        optionsButton.setIcon(new ImageIcon(propertiesService.getKeyValue("optionsPath")));
        optionsButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        optionsButton.setVerticalTextPosition(SwingConstants.CENTER);
        optionsButton.setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);
        optionsButton.setForeground(GlobalView.ALL_TEXT);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(optionsButton, gbc);
    }

    public void initExitButton(){
        exitButton = new DesignButton("SALIR", true);
        exitButton.setIcon(new ImageIcon(propertiesService.getKeyValue("exitPath")));
        exitButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        exitButton.setVerticalTextPosition(SwingConstants.CENTER);
        exitButton.setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);
        exitButton.setForeground(GlobalView.ALL_TEXT);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; 
        buttonPanel.add(exitButton, gbc);
    }
}
