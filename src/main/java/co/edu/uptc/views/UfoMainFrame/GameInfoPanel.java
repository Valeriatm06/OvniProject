package co.edu.uptc.views.UfoMainFrame;

import javax.swing.*;
import java.awt.*;

import co.edu.uptc.utilities.PropertiesService;
import co.edu.uptc.views.GlobalView;
import lombok.Getter;

@Getter
public class GameInfoPanel extends JPanel {

    private UfoMainView ufoMainView;
    private PropertiesService propertiesService;
    private JLabel movingUfoLabel;
    private JLabel crashedUfoLabel;
    private JLabel arrivalUfoLabel;
    private JLabel ufoLabel;
    private JCheckBox showTrajectoryCheckBox;
    private int ufoType;

    public GameInfoPanel(UfoMainView ufoMainView) {
        this.ufoMainView = ufoMainView;
        propertiesService = new PropertiesService();
        ufoType = 1;
        setPreferredSize(new Dimension(270, 300)); 
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
        initUfoStatus(gbc);
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
        addUfoSelector(gbc);
        
        JLabel directionKeysLabel = createLabel("Usar las teclas:");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(directionKeysLabel, gbc);
        
        addSpeedPanel(gbc, "upArrow", "+ Velocidad");
        addSpeedPanel(gbc, "downArrow", "- Velocidad");
    }
    
    
    private void addUfoSelector(GridBagConstraints gbc) {
        JLabel selectLabel = createLabel("Hacer click al OVNI:");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(selectLabel, gbc);
    
        ufoLabel = new JLabel(new ImageIcon(getUfoPath(ufoType)));
        gbc.gridx = 1;
        add(ufoLabel, gbc);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel trajectoryInstructionsLabel = createLabel("<html><div style='text-align: center;'>Click al OVNI deseado,<br>mantener presionado y<br>dibujar trayectoria con el mouse</div></html>");
        add(trajectoryInstructionsLabel, gbc);
        
        showTrajectoryCheckBox = new JCheckBox("Ver trayectoria");
        showTrajectoryCheckBox.setFont(GlobalView.ALL_TEXT_FONT);
        showTrajectoryCheckBox.setForeground(GlobalView.TITLE_TEXT);
        showTrajectoryCheckBox.setOpaque(false);
    
        gbc.gridy++;
        add(showTrajectoryCheckBox, gbc);
    }
    

    private void initUfoStatus(GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        movingUfoLabel = new JLabel("OVNIS en movimiento:");
        movingUfoLabel.setFont(GlobalView.ALL_TEXT_FONT);
        movingUfoLabel.setForeground(GlobalView.TITLE_TEXT);
        add(movingUfoLabel, gbc);

        gbc.gridy++;
        crashedUfoLabel = new JLabel("OVNIS estrellados:");
        crashedUfoLabel.setFont(GlobalView.ALL_TEXT_FONT);
        crashedUfoLabel.setForeground(GlobalView.TITLE_TEXT);
        add(crashedUfoLabel, gbc);

        gbc.gridy++;
        arrivalUfoLabel = new JLabel("OVNIS en llegada:");
        arrivalUfoLabel.setFont(GlobalView.ALL_TEXT_FONT);
        arrivalUfoLabel.setForeground(GlobalView.TITLE_TEXT);
        add(arrivalUfoLabel, gbc);
    }

    private String getUfoPath(int ufoType) {
        return propertiesService.getKeyValue("ufo" + ufoType + "Path");
    }

    public void updateufoType(int newufoType) {
        this.ufoType = newufoType;
        ufoLabel.setIcon(new ImageIcon(getUfoPath(newufoType)));
        revalidate();
        repaint();
    }

    public void upDateMovingUfoCount(int count) {
        movingUfoLabel.setText("OVNIS en movimiento: " + count);
    }

    public void updatCrashedUfoCount(int count) {
        crashedUfoLabel.setText("OVNIS estrellados: " + count);
    }

    public void upDateArrivalUfoCount(int count){
        arrivalUfoLabel.setText("OVNIS en llegada: " + count);
    }

    public boolean isTrajectoryVisible() {
        return showTrajectoryCheckBox.isSelected();
    }
}
