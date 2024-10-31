package co.edu.uptc.views.OvniMainFrame;

import javax.swing.*;
import java.awt.*;

import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.views.GlobalView;
import lombok.Getter;

@Getter
public class GameInfoPanel extends JPanel {

    private OvniMainView ovniMainView;
    private PropertiesService propertiesService;
    private JLabel movingOvniLabel;
    private JLabel crashedOvniLabel;
    private JLabel ovniLabel;
    private JCheckBox showTrajectoryCheckBox;
    private int ovniType;

    public GameInfoPanel(OvniMainView ovniMainView) {
        this.ovniMainView = ovniMainView;
        propertiesService = new PropertiesService();
        ovniType = 1;
        setPreferredSize(new Dimension(300, 300)); 
        setBackground(GlobalView.OPTIONS_BACKGROUND);
        setLayout(new GridBagLayout());
        initComponents();
    }

    private void initComponents(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        initTitle(gbc);
        addHorizontalLine(gbc);
        initSelectionSection(gbc);
        addHorizontalLine(gbc);
        initTrajectoryCheckbox(gbc);
        addHorizontalLine(gbc);
        initOvniStatusSection(gbc);
    }

    private void initTitle(GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("Parametros");
        titleLabel.setFont(GlobalView.TITLE_FONT_SMALL);
        titleLabel.setForeground(GlobalView.TITLE_TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.CENTER;

        add(titleLabel, gbc);
    }

    private void addHorizontalLine(GridBagConstraints gbc) {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.GRAY);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        add(separator, gbc);

        gbc.fill = GridBagConstraints.NONE;
    }

    private void initSelectionSection(GridBagConstraints gbc) {
        addOvniSelector(gbc);
        
        addSpeedPanel(gbc, "upArrow", "+ Velocidad");
    
        addSpeedPanel(gbc, "downArrow", "- Velocidad");
    }
    
    private void addOvniSelector(GridBagConstraints gbc) {
        JLabel selectLabel = createLabel("Seleccionar OVNI:");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(selectLabel, gbc);
    
        ovniLabel = new JLabel(new ImageIcon(getOvniImagePath(ovniType)));
        gbc.gridx = 1;
        add(ovniLabel, gbc);
    }
    
    private void addSpeedPanel(GridBagConstraints gbc, String arrowKey, String text) {
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel speedPanel = createArrowLabel(propertiesService.getKeyValue(arrowKey), text);
        add(speedPanel, gbc);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(GlobalView.ALL_TEXT_FONT);
        label.setForeground(GlobalView.TITLE_TEXT);
        return label;
    }
    

    private JPanel createArrowLabel(String path, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);

        JLabel arrowLabel = new JLabel(new ImageIcon(path));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(GlobalView.ALL_TEXT_FONT);
        textLabel.setForeground(GlobalView.TITLE_TEXT);

        panel.add(arrowLabel);
        panel.add(textLabel);

        return panel;
    }

    private void initTrajectoryCheckbox(GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        showTrajectoryCheckBox = new JCheckBox("Ver trayectoria");
        showTrajectoryCheckBox.setFont(GlobalView.ALL_TEXT_FONT);
        showTrajectoryCheckBox.setForeground(GlobalView.TITLE_TEXT);
        showTrajectoryCheckBox.setOpaque(false);

        add(showTrajectoryCheckBox, gbc);
    }

    private void initOvniStatusSection(GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        movingOvniLabel = new JLabel("OVNIS en movimiento:");
        movingOvniLabel.setFont(GlobalView.ALL_TEXT_FONT);
        movingOvniLabel.setForeground(GlobalView.TITLE_TEXT);
        add(movingOvniLabel, gbc);

        gbc.gridy++;
        crashedOvniLabel = new JLabel("OVNIS estrellados:");
        crashedOvniLabel.setFont(GlobalView.ALL_TEXT_FONT);
        crashedOvniLabel.setForeground(GlobalView.TITLE_TEXT);
        add(crashedOvniLabel, gbc);
    }

    private String getOvniImagePath(int ovniType) {
        return propertiesService.getKeyValue("ufo" + ovniType + "Path");
    }

    public void updateOvniType(int newOvniType) {
        this.ovniType = newOvniType;
        ovniLabel.setIcon(new ImageIcon(getOvniImagePath(newOvniType)));
        revalidate();
        repaint();
    }

    public void updateMovingOvniCount(int count) {
        movingOvniLabel.setText("OVNIS en movimiento: " + count);
    }

    public void updateCrashedOvniCount(int count) {
        crashedOvniLabel.setText("OVNIS estrellados: " + count);
    }

    public boolean isTrajectoryVisible() {
        return showTrajectoryCheckBox.isSelected();
    }
}
