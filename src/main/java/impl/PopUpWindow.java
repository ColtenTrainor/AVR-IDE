package impl;

import javax.swing.*;
import java.awt.*;

public class PopUpWindow {
    private JFrame windowFrame;
    private JPanel windowPane;
    private JLabel message;
    private JButton okButton;
    private JButton cancelButton;
    public PopUpWindow(String title, String msg, String oneButton){
        this.windowFrame = new JFrame(title);
        this.windowPane = new JPanel();
        this.okButton = new JButton(oneButton);
        this.message = new JLabel(msg);

        this.setPopUpLayout();
    }
    public PopUpWindow(String title, String msg, String firstButton, String secondButton){
        this.windowFrame = new JFrame(title);
        this.windowPane = new JPanel();
        this.okButton = new JButton(firstButton);
        this.cancelButton = new JButton(secondButton);
        this.message = new JLabel(msg);

        this.setPopUpLayout();
    }

    private void setPopUpLayout(){
        this.windowPane.add(okButton);
        if (cancelButton != null)
            this.windowPane.add(cancelButton);

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

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
