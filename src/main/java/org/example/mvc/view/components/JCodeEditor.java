package org.example.mvc.view.components;

import javax.swing.*;
import java.awt.*;

public class JCodeEditor extends JPanel{
    public final JTextPane TextPane = new JTextPane();
    public JCodeEditor() {
        setLayout(new GridLayout());
        var scrollPane = new JScrollPane(TextPane);
        TextPane.setContentType("text/html");
        add(scrollPane);
    }
}
