package org.example.mvc.codeassist;

import org.example.mvc.MainModel;
import org.example.mvc.view.MainView;
import org.example.util.ConfigIOHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter implements Runnable{
    private final MainView view;
    private final MainModel model;
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final boolean isActivated;
    private final ArrayList<String> registerIteration = new ArrayList<>();
    private final Pattern constantPattern = Pattern.compile("\\b\\d+\\b");;
    private final Pattern commentPattern = Pattern.compile(";([^\\n;]*)");

    public SyntaxHighlighter(MainView view, MainModel model){
        this.view = view;
        this.model = model;

        ConfigIOHandler configHandler = new ConfigIOHandler(
                new File("cfg/highlightColors.cfg"));
        configHandler.deserialize();
        decodeColorData(configHandler.getConfigData());

        isActivated = true;
        minorHighlightInit();
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
        try {
            String text = view.getTextArea().getDocument().getText(0, view.getTextArea().getDocument().getLength());
            model.setContent(text);
//            System.out.println(text);
        }catch (BadLocationException ex){
            ex.printStackTrace();
        }
    }

    public void addColorHighlighting() {
        JTextPane textPane = view.getTextArea();
        StyledDocument doc = textPane.getStyledDocument();
        resetStyledDocument(doc, textPane);

        try {
            String text = doc.getText(0, doc.getLength());

            Style style = textPane.addStyle("style-tag", null);

            instructionHighlight(text, doc, style); // add highlight to instructions
            registerHighlight(text, doc, style);
            constantHighlight(text, doc, style);
            commentHighlight(text, doc, style);

            textPane.setCaretPosition(doc.getLength()); // reposition caret to last character
        }catch (BadLocationException ex){
            ex.printStackTrace();
        }
    }

    private void instructionHighlight(String string, StyledDocument doc, Style style){

        for (String inst : InstructionData.getInstructionSet()){
            ArrayList<Integer> indices = findOneInstEachLine(string.toLowerCase(), inst.toLowerCase());
            if (indices.isEmpty())
                continue;

            indices.forEach(index -> {
                StyleConstants.setForeground(style, colorMap.get(InstructionData.getInstructionData(inst).getCategory()));
                // Setting attr instead of removing -> inserting
                doc.setCharacterAttributes(index, inst.length(), style, true);
            });
        }
    }

    private void minorHighlightInit(){
        for (int index = 0, end = 32; index < end ; ++index){
            registerIteration.add("r" + index);
        }
    }
    private void registerHighlight(String text, StyledDocument doc, Style style){

        for (String reg : registerIteration){
            int index = text.indexOf(reg);
            while (index >= 0) {
                StyleConstants.setForeground(style, colorMap.get("macro"));
                doc.setCharacterAttributes(index, reg.length(), style, true);
                index = text.indexOf(reg, index + 1);
            }
        }
    }
    private void constantHighlight(String text, StyledDocument doc, Style style){

        Matcher matcher = constantPattern.matcher(text);

        ArrayList<String> numbers = new ArrayList<>();
        while (matcher.find()) {
            String matchedNumber = matcher.group();
            numbers.add(matchedNumber);
        }

        for (String number: numbers){
            int index = text.indexOf(number);
            while (index >= 0) {
                StyleConstants.setForeground(style, colorMap.get("constant"));
                doc.setCharacterAttributes(index, number.length(), style, true);
                index = text.indexOf(number, index + 1);
            }
        }
    }

    private void commentHighlight(String text, StyledDocument doc, Style style){
        Matcher matcher = commentPattern.matcher(text);
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end(1);

            StyleConstants.setForeground(style, colorMap.get("ignore"));
            doc.setCharacterAttributes(startIndex, endIndex - startIndex, style, true);
        }
    }

    private ArrayList<Integer> findOneInstEachLine(String text, String word){
        ArrayList<Integer> indices = new ArrayList<>();

        Pattern pattern = Pattern.compile("(?<=\n|^)" + word);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int startIndex = matcher.start();
            indices.add(startIndex);
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
