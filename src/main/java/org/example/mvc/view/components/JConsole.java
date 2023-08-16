package org.example.mvc.view.components;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JConsole extends JPanel {
    private final JPanel consolePane = new JPanel();
    private final GridBagConstraints consolePaneConstraints = new GridBagConstraints();
    private final JTextArea outputPane = new JTextArea();
    private final JTextField inputPane = new JTextField();
    private boolean inputEnabled;
    public JConsole(){
        outputPane.setEditable(false);

        consolePane.setLayout(new GridBagLayout());

        consolePaneConstraints.gridx = 0;
        consolePaneConstraints.gridy = 0;
        consolePaneConstraints.weightx = 1;
        consolePaneConstraints.weighty = 1;
        consolePaneConstraints.fill = GridBagConstraints.BOTH;

        JPanel filler = new JPanel();
        consolePane.add(filler, consolePaneConstraints);

        setLayout(new GridLayout());
        var scrollPane = new JScrollPane(consolePane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane);

        consolePaneConstraints.gridy = 1;
        consolePaneConstraints.weighty = 0;
        consolePane.add(outputPane, consolePaneConstraints);

        setInputEnabled(true);
    }

    private void scrollToBottom(){
        consolePane.revalidate();
        int height = (int) consolePane.getPreferredSize().getHeight();
        var rect = new Rectangle(0, height, 0, 0);
        consolePane.scrollRectToVisible(rect);
    }

    public void appendConsole(){
        var doc = inputPane.getDocument();
        try {
            var command = doc.getText(0, doc.getLength());
            outputPane.append("\n" + command);
            inputPane.setText("");
            scrollToBottom();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void spitOutput(String message){
        outputPane.append("\n" + message);
        scrollToBottom();
    }

    public void clearConsole(){
        outputPane.setText("");
        scrollToBottom();
    }

    private final Action inputEntered = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendConsole();
        }
    };

    public boolean getInputEnabled(){ return inputEnabled; }
    public void setInputEnabled(boolean isEnabled){
        if (inputEnabled == isEnabled) return;
        inputEnabled = isEnabled;

        if (inputEnabled) {
            consolePaneConstraints.gridy = 2;
            consolePaneConstraints.weighty = 0;
            consolePane.add(inputPane, consolePaneConstraints);

            inputPane.addActionListener(inputEntered);
        }
        else {
            consolePane.remove(inputPane);
            inputPane.removeActionListener(inputEntered);
        }
    }
}