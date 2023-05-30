package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Test {
    public static void main(String[] args) {
        final String text = "XvX";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final mainWindow wnd = new mainWindow(text);
                wnd.setVisible(true);
            }
        });
    }

    public static class mainWindow extends JFrame{
        public mainWindow(String text){
            super("Main Window");
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    mainWindow.this.setVisible(false);
                    mainWindow.this.dispose();
                }
            });

            final JButton btn = new JButton(text);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(mainWindow.this, "Button Pressed", "Hey", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            setLayout(new FlowLayout());
            add(btn);
            pack();
        }
    }
}
