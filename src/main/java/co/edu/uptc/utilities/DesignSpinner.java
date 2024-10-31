package co.edu.uptc.utilities;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import co.edu.uptc.views.GlobalView;

import java.awt.*;

public class DesignSpinner extends JSpinner {

    public DesignSpinner(int initialValue, int minValue, int maxValue, int stepSize) {
        super(new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize));
        customizeSpinner();
    }

    private void customizeSpinner() {
        setBackground(GlobalView.PRIMARY_BTN_BACKGROUND);
        setBorder(new MatteBorder(0, 0, 0, 0, GlobalView.DEFAULT_BTN_BACKGROUND)); 

        JFormattedTextField textField = ((JSpinner.DefaultEditor) getEditor()).getTextField();
        textField.setBackground(GlobalView.OPTIONS_BACKGROUND); 
        textField.setForeground(GlobalView.TITLE_TEXT);
        textField.setFont(GlobalView.ALL_TEXT_FONT);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBorder(new MatteBorder(0, 0, 2, 0, GlobalView.DEFAULT_BTN_BACKGROUND));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(90, 30);
    }
}
