package co.edu.uptc.views.UfoMainFrame;

import javax.swing.*;
import co.edu.uptc.utilities.DesignButton;
import co.edu.uptc.utilities.DesignSpinner;
import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.views.GlobalView;
import java.awt.*;

public class OptionsDialog extends JDialog {
    private PropertiesService propertiesService;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel ufoCountLabel;
    private JLabel appearanceLabel;
    private JLabel speedLabel;
    private JLabel ufoLabel;
    private DesignButton okButton;
    private JButton lastSelectedButton;
    private DesignSpinner ufoCountSpinner;
    private DesignSpinner appearanceTimeSpinner;
    private JComboBox<String> speedComboBox; 
    private int selectedUfoCount;
    private int selectedAppearanceTime;
    private int selectedSpeed; 
    private int selectedUfoType; 

    public OptionsDialog(Frame owner, int ufoCount, int appearanceTime, int speed, int ufoType) {
        super(owner, "Opciones", true);
        propertiesService = new PropertiesService();
        setSize(500, 400);
        setLocationRelativeTo(owner);
    
        this.selectedUfoCount = ufoCount;
        this.selectedAppearanceTime = appearanceTime;
        this.selectedSpeed = speed;
        this.selectedUfoType = ufoType;
        initOptionsPanel();
    }

    private void initOptionsPanel() {
        mainPanel = createMainPanel();
        addTitle();
        addUfoCount();
        addAppearanceTime();
        addSpeed();
        addUfoType();
        addOkButton();
        add(mainPanel);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(GlobalView.OPTIONS_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private void addTitle() {
        titleLabel = createLabel("OPCIONES", GlobalView.TITLE_FONT_SMALL, GlobalView.TITLE_TEXT);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(false);
        mainPanel.add(titlePanel);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private void addUfoCount() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ufoCountLabel = createLabel("Cantidad de OVNIS:", GlobalView.ALL_TEXT_FONT, GlobalView.TITLE_TEXT);
        panel.add(ufoCountLabel);
        ufoCountSpinner = new DesignSpinner(selectedUfoCount, 5, 15, 1);
        panel.add(ufoCountSpinner);
        panel.setOpaque(false);
        mainPanel.add(panel);
    }

    private void addAppearanceTime() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        appearanceLabel = createLabel("Tiempo de aparición (ms):", GlobalView.ALL_TEXT_FONT, GlobalView.TITLE_TEXT);
        panel.add(appearanceLabel);
        appearanceTimeSpinner = new DesignSpinner(selectedAppearanceTime, 1000, 5000, 100);
        panel.add(appearanceTimeSpinner);
        panel.setOpaque(false);
        mainPanel.add(panel);
    }

    private void addSpeed() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedLabel = createLabel("Velocidad:", GlobalView.ALL_TEXT_FONT, GlobalView.TITLE_TEXT);
        panel.add(speedLabel);
    
        String[] speedOptions = createSpeedOptions();
    
        speedComboBox = new JComboBox<>(speedOptions);
        speedComboBox.setSelectedIndex(getSpeedIndex(selectedSpeed)); 
        speedComboBox.addActionListener(e -> selectedSpeed = getSpeedFromSelection((String) speedComboBox.getSelectedItem()));
    
        panel.add(speedComboBox);
        panel.setOpaque(false);
        mainPanel.add(panel);
    }
    
    private String[] createSpeedOptions() {
        String slowSpeed = String.valueOf(propertiesService.getIntValue("ufoSlowSpeed"));
        String mediumSpeed = String.valueOf(propertiesService.getIntValue("ufoMediumSpeed"));
        String fastSpeed = String.valueOf(propertiesService.getIntValue("ufoFastSpeed"));
        return new String[]{
            "Lenta (" + slowSpeed + ")",
            "Media (" + mediumSpeed + ")",
            "Rápida (" + fastSpeed + ")"
        };
    }
    
    private int getSpeedFromSelection(String selection) {
        if (selection.contains("Lenta")) return propertiesService.getIntValue("ufoSlowSpeed");
        if (selection.contains("Media")) return propertiesService.getIntValue("ufoMediumSpeed");
        if (selection.contains("Rápida")) return propertiesService.getIntValue("ufoFastSpeed");
        return 2; 
    }
    
    private int getSpeedIndex(int speed) {
        switch (speed) {
            case 1: return 0;
            case 2: return 1; 
            case 3: return 2; 
            default: return 1;
        }
    }    

    private void addUfoType() {
        JPanel ufoTypePanel = createUfoTypePanel();
        addUfoButtons(ufoTypePanel);
        mainPanel.add(ufoTypePanel);
    }

    private JPanel createUfoTypePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ufoLabel = createLabel("Tipo de OVNI:", GlobalView.ALL_TEXT_FONT, GlobalView.TITLE_TEXT);
        panel.add(ufoLabel);
        panel.setOpaque(false);
        return panel;
    }

    private void addUfoButtons(JPanel panel) {
        for (int i = 1; i <= 4; i++) {
            JButton ufoTypeButton = createUfoButton(i);
            if (i == selectedUfoType) {
                ufoTypeButton.setBackground(GlobalView.SECONDARY_BTN_BACKGROUND);
                lastSelectedButton = ufoTypeButton;
            }
            panel.add(ufoTypeButton);
        }
    }

    private JButton createUfoButton(int type) {
        DesignButton button = new DesignButton("", true);
        button.setIcon(new ImageIcon(propertiesService.getKeyValue("ufo" + type + "Path")));
        button.addActionListener(e -> ufoButtonSelection(button, type));
        return button;
    }

    private void ufoButtonSelection(JButton button, int type) {
        selectedUfoType = type;
        if (lastSelectedButton != null) {
            lastSelectedButton.setBackground(GlobalView.DEFAULT_BTN_BACKGROUND);
        }
        button.setBackground(GlobalView.SECONDARY_BTN_BACKGROUND);
        lastSelectedButton = button;
    }

    private void addOkButton() {
        okButton = new DesignButton("Ok", true);
        okButton.setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);
        okButton.addActionListener(e -> {
            updateSelectedValues();
            dispose();
        });
        mainPanel.add(okButton);
    }

    private void updateSelectedValues() {
        selectedUfoCount = getUfoCount();
        selectedAppearanceTime = getAppearanceTime();
        selectedSpeed = getSpeed();
        selectedUfoType = getSelectedUfoType();
    }

    public int getUfoCount() {
        return (int) ufoCountSpinner.getValue();
    }

    public int getAppearanceTime() {
        return (int) appearanceTimeSpinner.getValue();
    }

    public int getSpeed() {
        return selectedSpeed;
    }

    public int getSelectedUfoType() {
        return selectedUfoType;
    }   
}
