package org.example.mvc.codeassist;

import org.example.util.ConfigIOHandler;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter implements Runnable{
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final JTextPane textPane;
    private final StyledDocument document;
    private final Style defaultStyle;
    private final Style highlightStyle;
    private TextRegion textRegion;
    private Pattern highlightPattern;
    private String[] categories;

    private static final HashMap<String, String> initialCategories = new HashMap<>();
    static {
        initialCategories.put("ignore", "(;[^\\n]*)");
        initialCategories.put("constant", "(\\b(?:(?:0b[0-1]*)|(?:0x(?:\\d|[a-f])*)|\\d+)\\b)");
        initialCategories.put("directive", "(?:\\s|^)(\\.\\S+)(?:\\s|$)");
        initialCategories.put("register", "(\\b(?:(?:r\\d+)|(?:(?:X|Y|Z)(?:H|L)))\\b)");
    }

    public SyntaxHighlighter(StyledDocument document, JTextPane textPane) {
        this.document = document;
        this.textPane = textPane;
        this.defaultStyle = textPane.addStyle("default-style", null);
        this.highlightStyle = textPane.addStyle("highlight-style", defaultStyle);
        this.categories = new String[initialCategories.size() + InstructionData.getCategorySet().size()];

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

        int categoryIndex = 0;

        for (String s : initialCategories.keySet()){
            pattern.append(initialCategories.get(s)).append("|");
            categories[categoryIndex] = s;
            categoryIndex++;
        }

        for (String s : InstructionData.getCategorySet()) {
            appendInstructionRegex(pattern, s);
            if (categoryIndex < categories.length - 1) pattern.append("|");
            categories[categoryIndex] = s;
            categoryIndex++;
        }

        System.out.println(pattern);
        System.out.println(Arrays.toString(categories));

        highlightPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
    }

    private void appendInstructionRegex(StringBuilder pattern, String category) {
        pattern.append("(\\b(?:");
        var instructionSet = InstructionData.getInstructionSetFromCategory(category).toArray();
        for (int i = 0; i < instructionSet.length - 1; i++) {
            pattern.append(instructionSet[i]).append("|");
        }
        pattern.append(instructionSet[instructionSet.length - 1]).append(")\\b)");
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
        document.setCharacterAttributes(textRegion.startOffset, textRegion.length, defaultStyle, true);
        var text = "";
        try {
            text = textReFormatting(document.getText(textRegion.startOffset, textRegion.length));
        } catch (BadLocationException e) {
            return;
        }
        Matcher matcher = highlightPattern.matcher(text);

        while (matcher.find()){
            for (int i = 0; i < categories.length; i++) {
                applyStyle(matcher, i + 1, categories[i]);
            }
        }
    }

    private String textReFormatting(String text){
        String pattern = "\"[^\"]*\"";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);

        StringBuilder replacedBuffer = new StringBuilder();
        while (matcher.find()){
            String match = matcher.group();
            String replacement = "(".repeat(match.length() ) ;
            matcher.appendReplacement(replacedBuffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(replacedBuffer);
        return replacedBuffer.toString();
    }

    private void applyStyle(Matcher matcher, int group, String category){
        if (matcher.group(group) == null) return;
//        System.out.printf("%s: Group %d: %s%n", matcher.group(), group, category);
        StyleConstants.setForeground(highlightStyle, colorMap.get(category));
        document.setCharacterAttributes(textRegion.startOffset + matcher.start(group),
                matcher.end(group) - matcher.start(group), highlightStyle, true);
    }

    @Override
    public void run() {
        matchRegion();
    }

    private static class TextRegion {
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
