package co.edu.uptc.views.OvniMainFrame;

import javax.swing.*;
import co.edu.uptc.utilities.DesignButton;
import co.edu.uptc.utilities.DesignSpinner;
import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.utilities.Speed;
import co.edu.uptc.views.GlobalView;
import java.awt.*;

public class OptionsDialog extends JDialog {

    private PropertiesService propertiesService;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel ovniCountLabel;
    private JLabel appearanceLabel;
    private JLabel speedLabel;
    private JLabel ovniLabel;
    private DesignButton okButton;
    private JButton lastSelectedButton;
    private DesignSpinner ovniCountSpinner;
    private DesignSpinner appearanceTimeSpinner;
    private JComboBox<Speed> speedComboBox; 
    private int selectedOvniCount;
    private int selectedAppearanceTime;
    private int selectedSpeed; 
    private int selectedOvniType; 

    public OptionsDialog(Frame owner, int ovniCount, int appearanceTime, int speed, int ovniType) {
        super(owner, "Opciones", true);
        propertiesService = new PropertiesService();
        setSize(500, 400);
        setLocationRelativeTo(owner);
    
        this.selectedOvniCount = ovniCount;
        this.selectedAppearanceTime = appearanceTime;
        this.selectedSpeed = speed;
        this.selectedOvniType = ovniType;
        initOptionsPanel();
    }
    

    private void initOptionsPanel() {
        initMainPanel();
        initTitle();
        initCountOvni();
        initAppearanceTime();
        initSpeed();
        initOvniType();
        initOkButton();
        add(mainPanel);
    }

    public void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(GlobalView.OPTIONS_BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initTitle() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel = new JLabel("OPCIONES");
        titleLabel.setFont(GlobalView.TITLE_FONT_SMALL);
        titleLabel.setForeground(GlobalView.TITLE_TEXT);
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(false);
        mainPanel.add(titlePanel);
    }

    public void initCountOvni() {
        JPanel ovniCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ovniCountLabel = new JLabel("Cantidad de OVNIS:");
        ovniCountLabel.setFont(GlobalView.ALL_TEXT_FONT);
        ovniCountLabel.setForeground(GlobalView.TITLE_TEXT);
        ovniCountPanel.add(ovniCountLabel);
        ovniCountSpinner = new DesignSpinner(selectedOvniCount, 5, 15, 1);
        ovniCountPanel.add(ovniCountSpinner);
        ovniCountPanel.setOpaque(false);
        mainPanel.add(ovniCountPanel);
    }

    public void initAppearanceTime() {
        JPanel appearanceTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        appearanceLabel = new JLabel("Tiempo de aparición (ms):");
        appearanceLabel.setFont(GlobalView.ALL_TEXT_FONT);
        appearanceLabel.setForeground(GlobalView.TITLE_TEXT);
        appearanceTimePanel.add(appearanceLabel);
        appearanceTimeSpinner = new DesignSpinner(selectedAppearanceTime, 1000, 5000, 100);
        appearanceTimePanel.add(appearanceTimeSpinner);
        appearanceTimePanel.setOpaque(false);
        mainPanel.add(appearanceTimePanel);
    }

    public void initSpeed() {
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedLabel = new JLabel("Velocidad:");
        speedLabel.setFont(GlobalView.ALL_TEXT_FONT);
        speedLabel.setForeground(GlobalView.TITLE_TEXT);
        speedPanel.add(speedLabel);

        Speed[] speedOptions = Speed.values();
        speedComboBox = new JComboBox<>(speedOptions);

        for (int i = 0; i < speedOptions.length; i++) {
            if (speedOptions[i].getValue() == selectedSpeed) {
                speedComboBox.setSelectedIndex(i);
                break;
            }
        }

        speedComboBox.addActionListener(e -> {
            Speed selectedSpeedEnum = (Speed) speedComboBox.getSelectedItem();
            selectedSpeed = selectedSpeedEnum.getValue();
        });

        speedPanel.add(speedComboBox);
        speedPanel.setOpaque(false);
        mainPanel.add(speedPanel);
    }

    private void initOvniType() {
        JPanel ovniTypePanel = createOvniTypePanel();
        addOvniButtons(ovniTypePanel);
        mainPanel.add(ovniTypePanel);
    }

    private JPanel createOvniTypePanel() {
        JPanel ovniTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ovniLabel = new JLabel("Tipo de OVNI:");
        ovniLabel.setFont(GlobalView.ALL_TEXT_FONT);
        ovniLabel.setForeground(GlobalView.TITLE_TEXT);
        ovniTypePanel.add(ovniLabel);
        ovniTypePanel.setOpaque(false);
        return ovniTypePanel;
    }

    private void addOvniButtons(JPanel ovniTypePanel) {
        for (int i = 1; i <= 4; i++) {
            JButton ovniTypeButton = createOvniButton(i);
            if (i == selectedOvniType) {
                ovniTypeButton.setBackground(GlobalView.SECONDARY_BTN_BACKGROUND);
                lastSelectedButton = ovniTypeButton;
            }
            ovniTypePanel.add(ovniTypeButton);
        }
    }

    private JButton createOvniButton(int type) {
        DesignButton ovniTypeButton = new DesignButton("", true);
        ovniTypeButton.setIcon(new ImageIcon(propertiesService.getKeyValue("ufo" + type + "Path")));
        ovniTypeButton.addActionListener(e -> ovniButtonSelection(ovniTypeButton, type));
        return ovniTypeButton;
    }

    private void ovniButtonSelection(JButton button, int type) {
        selectedOvniType = type;

        if (lastSelectedButton != null) {
            lastSelectedButton.setBackground(GlobalView.DEFAULT_BTN_BACKGROUND);
        }

        button.setBackground(GlobalView.SECONDARY_BTN_BACKGROUND);
        lastSelectedButton = button;
    }

    private void initOkButton() {
        okButton = new DesignButton("Ok", true);
        okButton.setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);
        okButton.addActionListener(e -> {
            updateSelectedValues();
            dispose();
        });
        mainPanel.add(okButton);
    }
    
    private void updateSelectedValues() {
        selectedOvniCount = getOvniCount();
        selectedAppearanceTime = getAppearanceTime();
        selectedSpeed = getSpeed();
        selectedOvniType = getSelectedOvniType();
    }
    

    @Override
    public void dispose() {
        super.dispose();
    }

    public int getOvniCount() {
        return (int) (ovniCountSpinner.getValue());
    }

    public int getAppearanceTime() {
        return (int) appearanceTimeSpinner.getValue();
    }

    public int getSpeed() {
        return selectedSpeed;
    }

    public int getSelectedOvniType() {
        return selectedOvniType;
    }   
}