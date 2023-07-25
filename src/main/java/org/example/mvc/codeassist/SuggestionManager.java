package org.example.mvc.codeassist;

import org.example.mvc.actions.SuggestionPopup;
import org.example.mvc.view.IMainView;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SuggestionManager {
    private final IMainView view;
    private final SuggestionPopup popup;
    public SuggestionManager(IMainView view) {
        this.view = view;
        popup = new SuggestionPopup(this.view);
    }

    private void setUpToolTipListener(){
        JTextPane editableField = view.getTextArea();
        editableField.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String tipDisplayText = "";
                Point pt = new Point(e.getX(), e.getY());
                int pos = editableField.viewToModel2D(pt);
                try {
                    // TODO: only add tip if instruction in format "inst ..."
                    String text = editableField.getDocument().getText(0, editableField.getDocument().getLength());
                    int[] wordPos = getWordOffsetAndLen(pos, text);
                    tipDisplayText = editableField.getDocument().getText(wordPos[0], wordPos[1]);

                    if (InstructionData.getInstructionSet().contains(tipDisplayText.strip()))
                        tipDisplayText = InstructionData.getInstructionData(tipDisplayText.strip()).getShortDescription();
                    else tipDisplayText = null;
                }
                catch (BadLocationException ex) {
                    System.out.println("BAD LOCATION!");
                }
                editableField.setToolTipText(tipDisplayText);
            }
        });
    }

    private int[] getWordOffsetAndLen(int currentCaretPosition, String text){
        int head_pos = text.lastIndexOf("\n", currentCaretPosition);
        if (head_pos == -1)
            head_pos = text.lastIndexOf(" ", currentCaretPosition);
        if (head_pos == -1)
            head_pos = 0;

        int tail_pos = text.indexOf(" ", currentCaretPosition);
        if (tail_pos == -1)
            tail_pos = text.indexOf("\n", currentCaretPosition);
        if (tail_pos == -1)
            tail_pos = text.length();

        int len = tail_pos - head_pos;
        return new int[]{head_pos, len};
    }
    public void activateSuggestionPopUp(){
        SwingUtilities.invokeLater(this.popup);
    }
}
