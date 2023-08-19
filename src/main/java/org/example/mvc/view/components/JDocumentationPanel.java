package org.example.mvc.view.components;

import org.example.mvc.codeassist.InstructionData;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class JDocumentationPanel extends JPanel{

    private JTextPane[] instructionBoxes;

    private void loadInstructionData(){
        instructionBoxes = new JTextPane[InstructionData.getInstructionSet().size()];
        for (int i = 0; i < instructionBoxes.length; i++) {
            var box = instructionBoxes[i];
            var doc = new DefaultStyledDocument();
            box.setStyledDocument(doc);
            box.setEditable(false);
            var style = box.addStyle("docs-style", null);
            try {
                doc.insertString(0, "", style);
            } catch (BadLocationException ignored) { }
        }
    }
}
