package org.example.mvc.view.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;
import java.awt.*;

public class JCodeEditor extends JPanel{
    public final JTextPane TextPane = new JTextPane();
    public JCodeEditor() {
        setLayout(new GridLayout());
        var scrollPane = new JScrollPane(TextPane);
        TextPane.setEditorKit(new StyledEditorKit());
        add(scrollPane);
    }

    public void setDocumentListener(DocumentListener documentListener){
        TextPane.getDocument().addDocumentListener(documentListener);
    }
}
