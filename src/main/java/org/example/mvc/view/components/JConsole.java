package org.example.mvc.view.components;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.util.Utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JConsole extends JPanel {
    private final JPanel consolePane = new JPanel();
    private final GridBagConstraints consolePaneConstraints = new GridBagConstraints();
    private final JTextPane outputPane = new JTextPane();
    private final JTextField inputPane = new JTextField();
    private boolean inputEnabled;

    private Style outputStyle;
    private Color outputColor;
    private Color errorColor = Color.decode("#cc5555");

    public JConsole(){
        outputPane.setEditable(false);
        outputColor = outputPane.getForeground();
        outputStyle = outputPane.addStyle("console-style", null);
        outputPane.putClientProperty("FlatLaf.style", "font: $monospaced.font");
        inputPane.putClientProperty("FlatLaf.style", "font: $monospaced.font");

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

    public void handleInput(){
        try {
            var command = Utils.getFullTextFromDoc(inputPane.getDocument());
            appendConsole(command, outputColor);
            inputPane.setText("");
            scrollToBottom();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void appendOutput(String output){
        appendConsole(output, outputColor);
    }

    public void appendError(String error){
        appendConsole(error, errorColor);
    }

    private void appendConsole(String message, Color color){
        var doc = outputPane.getDocument();
        StyleConstants.setForeground(outputStyle, color);
        try {
            doc.insertString(doc.getLength(), "\n" + message, outputStyle);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        scrollToBottom();
    }

    public void clearConsole(){
        outputPane.setText("");
        scrollToBottom();
    }

    private final Action inputEntered = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleInput();
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