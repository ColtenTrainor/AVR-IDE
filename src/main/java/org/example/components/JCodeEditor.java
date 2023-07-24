package org.example.components;

import javax.swing.*;
import java.awt.*;

public class JCodeEditor extends JPanel{
    public final JTextPane TextPane = new JTextPane();
    public JCodeEditor() {
        setLayout(new GridLayout());
        TextPane.setContentType("text/html");
        add(TextPane);
    }
}
