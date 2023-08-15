package org.example.mvc.view;

import javax.swing.*;
import java.awt.*;

public class PopUpWindow {
    private JFrame windowFrame;
    private JPanel windowPane;
    private JLabel message;
    private JButton[] buttons;

    public PopUpWindow(String title, String msg, String... buttonTexts){
        this.windowFrame = new JFrame(title);
        this.windowPane = new JPanel();
        this.message = new JLabel(msg);

        this.buttons = new JButton[buttonTexts.length];
        for (int i = 0; i < buttonTexts.length; i++) {
            this.buttons[i] = new JButton(buttonTexts[i]);
        }

        this.setPopUpLayout();
    }

    private void setPopUpLayout(){
        for (var button : buttons) {
            windowPane.add(button);
        }

        this.windowFrame.setSize(400,100);
        this.windowFrame.add(message, BorderLayout.NORTH);
        this.windowFrame.add(windowPane, BorderLayout.CENTER);
        this.windowFrame.setLocationRelativeTo(null);

        this.message.setHorizontalAlignment(JLabel.CENTER);
    }
    public void setVisible(){
        this.windowFrame.setVisible(true);
    }
    public void setInvisible(){
        this.windowFrame.setVisible(false);
    }

    public JButton getButton(int index) {
        return buttons[index];
    }
}
