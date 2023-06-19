package impl;

import interfaces.IMainModel;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DebugMode implements Runnable{
    private final IMainView view;
    private final IMainModel model;
    private final boolean isActivated;
    private HashMap<String, String> colorMap;

    public DebugMode(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;
        this.isActivated = true;
        colorMap = new HashMap<>();
    }

    @Override
    public void run() {
        String previousContent = "";
        while (isActivated){
            try {
            String currentContent = getViewRawContent();

            if ( ! currentContent.contentEquals(previousContent)){
                this.contentModify();
                System.out.print("DEBUG: " + view.getTextArea().getText());
                System.out.println("Raw: " + currentContent);
                previousContent = currentContent;
            }
            Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException("Sleep failed.");
            }
        }
    }

    private String getViewRawContent() {
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

    public void addColorHighlighting() {
        JTextPane textPane = view.getTextArea();
        StyledDocument doc = textPane.getStyledDocument();
        resetStyledDocument(doc, textPane);

        try {
            String text = doc.getText(0, doc.getLength());

            Style style = textPane.addStyle("style-tag", null);

            instructionHighlight(text, doc, style);
            textPane.setCaretPosition(doc.getLength());
        }catch (BadLocationException ex){
            ex.printStackTrace();
        }
    }

    private void instructionHighlight(String string, StyledDocument doc, Style style){
        String instructions[] = new String[]{"add", "sub", "inc", "dec", "and", "or", "mul",
                "nop", "break", "sleep", "mov", "in", "out", "push", "pop", "ldi",
                "rjmp", "ijmp", "jmp",
                "rcall", "icall", "ret"};

        for (String inst : instructions){
            ArrayList<Integer> indices = findWordIndices(string.toLowerCase(), inst);
            if (indices.size() == 0)
                continue;

            indices.forEach(index -> {
                try {
                    StyleConstants.setForeground(style, Color.decode("#6a5acd"));
                    doc.remove(index, inst.length());
                    doc.insertString(index, inst.toUpperCase() ,style);
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
        // Remove styled text
        doc.setCharacterAttributes(0, doc.getLength(), textPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Clear any additional styles
        Style style = textPane.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(style, textPane.getForeground());

        textPane.setCaretPosition(doc.getLength()); // Set the caret position to the beginning of the document
    }
}
