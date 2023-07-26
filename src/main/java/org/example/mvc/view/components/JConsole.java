package org.example.mvc.view.components;

import javax.swing.*;
import java.awt.*;

public class JConsole extends JPanel {
    public final JTextArea OutputPane = new JTextArea();
    public final JTextField InputPane = new JTextField();
    public JConsole(){
        OutputPane.setText("Test text.\nThis text is an output pane test.\n\n\n\n\n\n\n\n\n\n\naaaaaaaaaaa");
        InputPane.setText("This text is an input pane test.");

        OutputPane.setEditable(false);

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;

        JPanel filler = new JPanel();
        add(filler, constraints);

        constraints.gridy = 1;
        constraints.weighty = 0;

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(OutputPane, BorderLayout.CENTER);
        textPanel.add(InputPane, BorderLayout.SOUTH);

        add(textPanel, constraints);
    }
}