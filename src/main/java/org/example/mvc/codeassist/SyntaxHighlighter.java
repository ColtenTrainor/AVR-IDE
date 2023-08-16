package org.example.mvc.codeassist;

import org.example.util.ConfigIOHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter implements Runnable{
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private JTextPane textPane;
    private StyledDocument document;
    private Style style;
    private TextRegion textRegion;

//    private String masterRegexPatternIncompleteExampleDoNotUse =
//            "(;[^\\n]*)|(\\b(?:0(?:b|x))?\\d+\\b)|\\s(\\.\\S+)\\s|((?:r|R)\\d+)|(\\b(?:ldi|dec|brne)\\b)";
    private Pattern highlightPattern;

    public SyntaxHighlighter(StyledDocument document, JTextPane textPane) {
        this.document = document;
        this.textPane = textPane;
        this.style = textPane.addStyle("editor-style", null);

        ConfigIOHandler configHandler = new ConfigIOHandler(
                new File("cfg/highlightColors.cfg"));
        configHandler.deserialize();
        decodeColorData(configHandler.getConfigData());

        compileHighlightPattern();
    }

    private void decodeColorData(HashMap<String, String> data) {
        colorMap.clear();
        for (var tokenType : data.keySet()) {
            try {
                colorMap.put(tokenType, Color.decode(data.get(tokenType)));
            } catch (NumberFormatException ignored) {}
        }
    }

    public Color getColor(String tokenType) { return colorMap.get(tokenType); }

    private void compileHighlightPattern() {
        var pattern = new StringBuilder();
        pattern.append("(;[^\\n]*)") // comment
                .append("|(\\b(?:0(?:b|x))?\\d+\\b)") // constant
                .append("|\\s(\\.\\S+)\\s") // directive
                .append("|((?:r|R)\\d+)") // register TODO: replace with dynamic macro list
                .append("|(\\b(?:"); // beginning of instruction section
        var instructionSet = InstructionData.getInstructionSet().toArray();
        for (int i = 0; i < instructionSet.length - 1; i++) {
            pattern.append(instructionSet[i]).append("|");
        }
        pattern.append(instructionSet[instructionSet.length - 1]).append(")\\b)");
        System.out.println(pattern);

        highlightPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
    }

    private void calculateLocalHighlightRegion(int offset, int length){
        var root = document.getDefaultRootElement();

        var startOffset = 0;
        var endOffset = document.getLength() - 1;

        try {
            startOffset = root.getElement(root.getElementIndex(offset) - 1).getStartOffset();
        } catch (Exception ignored) {}

        try {
            endOffset = root.getElement(root.getElementIndex(offset + length) + 1).getEndOffset();
        } catch (Exception ignored) {}

        textRegion = new TextRegion(startOffset, endOffset);
    }

    public void refreshLocalHighlights(int offset, int length) {
        calculateLocalHighlightRegion(offset, length);
        SwingUtilities.invokeLater(this);
    }

    private void matchRegion(){
        var text = "";
        try {
            text = document.getText(textRegion.startOffset, textRegion.length);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        Matcher matcher = highlightPattern.matcher(text);

        while (matcher.find()){
            System.out.println(matcher.group());
            applyStyle(matcher, 1, "ignore");
            applyStyle(matcher, 2, "constant");
            applyStyle(matcher, 3, "directive");
            applyStyle(matcher, 4, "macro");
            applyStyle(matcher, 5, "inst_logic");
        }
    }

    private void applyStyle(Matcher matcher, int group, String colorKey){
        if (matcher.group(group) == null) return;
        StyleConstants.setForeground(style, colorMap.get(colorKey));
        document.setCharacterAttributes(textRegion.startOffset + matcher.start(group),
                matcher.end(group) - matcher.start(group), style, true);
    }

    @Override
    public void run() {
        matchRegion();
    }

    private class TextRegion {
        public int startOffset;
        public int endOffset;
        public int length;
        public TextRegion(int startOffset, int endOffset){
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            length = endOffset - startOffset;
        }
    }
}
