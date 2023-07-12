package impl;

import impl.regAndIns.InstructionRules;
import interfaces.IMainModel;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        this.setUpToolTipListener();
    }

    @Override
    public void run() {
        String previousContent = "";
        while (isActivated){
            try {
            String currentContent = getViewRawContent();

            if ( ! currentContent.contentEquals(previousContent)){
                this.contentModify();
//                System.out.print("DEBUG: " + view.getTextArea().getText());
//                System.out.println("Raw: " + currentContent);
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
        InstructionRules rules = new InstructionRules();
        List<String> instructions = rules.getKeySet();

        for (String inst : instructions){
            ArrayList<Integer> indices = findWordIndices(string.toLowerCase(), inst.toLowerCase());
            if (indices.size() == 0)
                continue;

            indices.forEach(index -> {
                StyleConstants.setForeground(style, Color.decode(rules.getColorCode(inst)));
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

    private void setUpToolTipListener(){
        JTextPane editableField = view.getTextArea();
        editableField.addMouseMotionListener(new MouseMotionAdapter() {
            final InstructionRules rules = new InstructionRules();
            @Override
            public void mouseMoved(MouseEvent e) {
                String tipDisplayText = "";
                Point pt = new Point(e.getX(), e.getY());
                int pos = editableField.viewToModel2D(pt);
                try {
                    String text = editableField.getDocument().getText(0, editableField.getDocument().getLength());
                    int[] wordPos = getWordOffsetAndLen(pos, text);
                    tipDisplayText = editableField.getDocument().getText(wordPos[0], wordPos[1]);

                    if (!rules.isAnInstruction(tipDisplayText.strip()))
                        tipDisplayText = "SCREAMING IN DESPAIR.";
                    else tipDisplayText = rules.getInstructionDescription(tipDisplayText.strip());
                }
                catch (BadLocationException ex) {
                    System.out.println("BAD LOCATION!");
                }
                editableField.setToolTipText(tipDisplayText);
            }
        });
    }

    private int[] getWordOffsetAndLen(int currentCaretPostion, String text){
        int head_pos = text.lastIndexOf(" ", currentCaretPostion);
        if (head_pos == -1)
            head_pos = text.lastIndexOf("\n", currentCaretPostion);
        if (head_pos == -1)
            head_pos = 0;

        int tail_pos = text.indexOf(" ", currentCaretPostion);
        if (tail_pos == -1)
            tail_pos = text.indexOf("\n", currentCaretPostion);
        if (tail_pos == -1)
            tail_pos = text.length();

        int len = tail_pos - head_pos;
        return new int[]{head_pos, len};
    }
}
