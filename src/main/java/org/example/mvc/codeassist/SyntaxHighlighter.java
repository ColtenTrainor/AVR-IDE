package org.example.mvc.codeassist;

import org.example.mvc.IMainModel;
import org.example.mvc.view.IMainView;
import org.example.util.ConfigIOHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SyntaxHighlighter implements Runnable{
    private final IMainView view;
    private final IMainModel model;
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final boolean isActivated;

    public SyntaxHighlighter(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;

        ConfigIOHandler configHandler = new ConfigIOHandler(
                new File("cfg/highlightColors.cfg"));
        configHandler.deserialize();
        decodeColorData(configHandler.getConfigData());

        isActivated = true;
    }

    private void decodeColorData(HashMap<String, String> data){
        colorMap.clear();
        for (var tokenType : data.keySet()) {
            try {
                colorMap.put(tokenType, Color.decode(data.get(tokenType)));
            } catch (NumberFormatException ignored) {}
        }
    }

    public Color getColor(String tokenType){ return colorMap.get(tokenType); }

    @Override
    public void run() {
        String previousContent = "";
        while (isActivated){
            try {
            String currentContent = getViewRawContent();

            if ( ! currentContent.contentEquals(previousContent)){
                this.contentModify();
                previousContent = currentContent;
            }
            Thread.sleep(500);

            } catch (InterruptedException e) {
                throw new RuntimeException("Sleep failed.");
            }
        }
    }

    private String getViewRawContent() {
        var doc = view.getTextArea().getDocument();
        try {
            return doc.getText(0, doc.getLength());
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

        for (String inst : InstructionData.getInstructionSet()){
            ArrayList<Integer> indices = findWordIndices(string.toLowerCase(), inst.toLowerCase());
            if (indices.size() == 0)
                continue;

            indices.forEach(index -> {
                StyleConstants.setForeground(style, colorMap.get(InstructionData.getInstructionData(inst).getCategory()));
                // Setting attr instead of removing -> inserting
                doc.setCharacterAttributes(index, inst.length(), style, true);
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