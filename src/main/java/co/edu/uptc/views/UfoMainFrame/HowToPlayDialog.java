package co.edu.uptc.views.UfoMainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import co.edu.uptc.utilities.DesignButton;
import co.edu.uptc.views.GlobalView;

public class HowToPlayDialog extends JDialog {

    private JPanel instructionsPanel;
    private JLabel titleLabel;

    public HowToPlayDialog(JFrame owner) {
        super(owner, "Instrucciones", true);
        setSize(620, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        initInstructionsPanel();
        initTitle();
        initInstructions();
        initOkButton();
    }

    public void initInstructionsPanel(){
        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBackground(GlobalView.OPTIONS_BACKGROUND);  
        add(instructionsPanel, BorderLayout.CENTER);
    }

    public void initTitle(){
        titleLabel = new JLabel("Instrucciones");
        titleLabel.setFont(GlobalView.TITLE_FONT_MEDIUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(GlobalView.TITLE_TEXT); 
        titleLabel.setBackground(GlobalView.OPTIONS_BACKGROUND);
        titleLabel.setOpaque(true);
        instructionsPanel.add(titleLabel);
        instructionsPanel.add(Box.createVerticalStrut(30));
    }

    public void initInstructions(){
        JTextArea instructionsText = new JTextArea();
        instructionsText.setText(
            "  - Su objetivo es llevar todos los ovnis al agujero negro.\n\n" +
            "  - Para definir una velocidad: Haga click en el OVNI deseado y   use las teclas de flecha para aumentar o disminuir la velocidad.\n\n" +
            "  - Para definir la trayectoria: Haga click en el OVNI deseado,   mantenga presionado y dibuje la trayectoria con el mouse."
        );
        instructionsText.setFont(GlobalView.ALL_TEXT_FONT);
        instructionsText.setEditable(false);
        instructionsText.setBackground(GlobalView.OPTIONS_BACKGROUND); 
        instructionsText.setForeground(GlobalView.TITLE_TEXT);  
        instructionsText.setWrapStyleWord(true);
        instructionsText.setLineWrap(true);
        instructionsPanel.add(instructionsText);
        instructionsPanel.add(Box.createVerticalStrut(10)); 
    }

    public void initOkButton(){
        DesignButton okButton = new DesignButton("OK", true);
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setBackground(GlobalView.DEFAULT_BTN_BACKGROUND); 
        okButton.setForeground(GlobalView.ALL_TEXT); 
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        instructionsPanel.add(okButton);
    }
    
}
