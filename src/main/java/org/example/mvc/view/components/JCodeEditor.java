package org.example.mvc.view.components;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class JCodeEditor extends JPanel{
    public final JTextPane TextPane = new JTextPane();
    public JCodeEditor() {
        TextPane.putClientProperty("FlatLaf.style", "font: $monospaced.font");

        JPanel noWrapPanel = new JPanel( new BorderLayout() );
        noWrapPanel.add(TextPane);

        setLayout(new GridLayout());
        var scrollPane = new JScrollPane(noWrapPanel);
        add(scrollPane);
    }

    public void setDocumentListener(DocumentListener documentListener){
        TextPane.getDocument().addDocumentListener(documentListener);
    }
}
