package impl;

import interfaces.IMainModel;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class DebugMode implements Runnable{
    private final IMainView view;
    private final IMainModel model;
    private final boolean isActivated;
    private boolean isContentChanged;
    private final PropertyChangeSupport changeObserver;
    public DebugMode(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;
        this.isActivated = true;
        this.isContentChanged = false;
        this.changeObserver = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeObserver.addPropertyChangeListener(listener);
    }

    @Override
    public void run() {
        String previousContent = "";
        while (isActivated){
            try {
            String currentContent = view.getTextArea().getDocument().getText(0, view.getTextArea().getDocument().getLength());

            if ( ! currentContent.contentEquals(previousContent)){
                    this.contentModify();
                    System.out.print("DEBUG: " + view.getTextArea().getText());
                    System.out.println("Raw: " + currentContent);
                previousContent = currentContent;
            }
            Thread.sleep(1000);

            } catch (InterruptedException | BadLocationException e) {
                throw new RuntimeException("Sleep failed.");
            }
        }
    }
    private String getRawContent() {
        try {
            return view.getTextArea().getDocument().getText(0, view.getTextArea().getDocument().getLength());
        }catch (BadLocationException ex){
            ex.printStackTrace();
        }
        return "";
    }

    private void contentModify(){
        String htmlText = view.getTextArea().getText();
        model.setContent(htmlText);
    }

    public void testingColor() {
        JTextPane textPane = view.getTextArea();
        StyledDocument doc = textPane.getStyledDocument();
        resetStyledDocument(doc, textPane);

        try {
            String text = doc.getText(0, doc.getLength());

            Style style = textPane.addStyle("style-tag", null);
//            StyleConstants.setForeground(style, Color.red);

            instructionHighlight(text, doc, style);
            textPane.setCaretPosition(doc.getLength());
        }catch (BadLocationException ex){
            ex.printStackTrace();
        }
    }

    private void instructionHighlight(String string, StyledDocument doc, Style style){
        String instructions[] = new String[]{"ldi", "lw", "sw"};

        for (String inst : instructions){
            ArrayList<Integer> indices = findWordIndices(string, inst);
            if (indices.size() == 0)
                continue;

            indices.forEach(index -> {
                try {
                    StyleConstants.setForeground(style, Color.decode("#6a5acd"));
                    doc.remove(index, inst.length());
                    doc.insertString(index, inst ,style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private ArrayList<Integer> findWordIndices(String text, String word) {
        ArrayList<Integer> indices = new ArrayList<>();
        int index = text.indexOf(word);

        while (index >= 0) {
            indices.add(index);
            index = text.indexOf(word, index + 1);
        }
        return indices;
    }

    private void resetStyledDocument(StyledDocument doc, JTextPane textPane) {
        // Remove all styled text
        doc.setCharacterAttributes(0, doc.getLength(), textPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Clear any additional styles
        Style style = textPane.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, textPane.getForeground());
//        StyleConstants.setBackground(style, textPane.getBackground());
//        StyleConstants.setFontFamily(style, textPane.getFont().getFamily());
//        StyleConstants.setFontSize(style, textPane.getFont().getSize());

        textPane.setCaretPosition(doc.getLength()); // Set the caret position to the beginning of the document
    }
}
